/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.console.web.service.ai;

import java.time.Instant;
import java.util.*;

import org.springframework.stereotype.Component;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForBiz;
import com.clougence.clouddm.console.web.component.auth.DmResAuthService;
import com.clougence.clouddm.console.web.component.auth.model.ResourceAccessInfo;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsConfig;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.component.schema.DsSchemaService;
import com.clougence.clouddm.console.web.model.ai.*;
import com.clougence.clouddm.console.web.util.DsResPath;
import com.clougence.clouddm.console.web.util.RdpAuthUtils;
import com.clougence.clouddm.platform.dal.access.ObjectCacheDao;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.sdk.execute.meta.DsElement;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel;
import com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.special.rdb.RdbForeignKey;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;

import jakarta.annotation.Resource;

@Component
public class AiPlatformAccess {
    @Resource
    private ObjectCacheDao      ownership;
    @Resource
    private DmDsConfigService   configuration;
    @Resource
    private DmAuthServiceForBiz authorization;
    @Resource
    private DmResAuthService    resources;
    @Resource
    private DsSchemaService     schema;

    public DsLevels namespace(String puid, String uid, List<String> names) {
        if (!authorization.checkRoleAuthWithoutError(puid, uid, SecRoleAuthLabel.DM_QUERY_CONSOLE)) {
            throw AiError.FORBIDDEN.exception();
        }
        if (names == null || names.size() < 2 || names.size() > 16 || names.stream().anyMatch(s -> s == null || s.isEmpty() || s.codePointCount(0, s.length()) > 512)) {
            throw AiError.INVALID_REQUEST.exception();
        }
        long id;
        try {
            id = Long.parseLong(names.get(1));
        } catch (NumberFormatException e) {
            throw AiError.INVALID_REQUEST.exception();
        }
        try {
            ownership.ownDataSource(puid, id);
        } catch (ErrorMessageException e) {
            throw AiError.CONTEXT_REVOKED.exception();
        }
        DsLevels levels;
        try {
            levels = configuration.parseLevels(List.copyOf(names));
        } catch (ErrorMessageException e) {
            throw AiError.CONTEXT_REVOKED.exception();
        } catch (IndexOutOfBoundsException e) {
            throw AiError.INVALID_REQUEST.exception();
        }
        DmDsDO source = levels.dsDO();
        if (!Objects.equals(String.valueOf(source.getDsEnvId()), names.get(0))) {
            throw AiError.CONTEXT_REVOKED.exception();
        }
        DataSourceType type = source.getDataSourceType();
        if (Set.of(DataSourceType.MongoDB, DataSourceType.Redis, DataSourceType.Valkey).contains(type)) {
            throw AiError.INVALID_REQUEST.exception();
        }
        DsConfig config = configuration.dsConstantSettings(type);
        if (names.size() != config.getCategories().getLevels().size()) {
            throw AiError.INVALID_REQUEST.exception();
        }
        requireBrowse(puid, uid, levels, levels.asResPath());
        return levels;
    }

    public void authorizeObjects(String puid, String uid, DsLevels levels, List<AiObject> objects) {
        levels = namespace(puid, uid, levels.levels());
        requireObjects(uid, levels, objects);
    }

    private void requireObjects(String uid, DsLevels levels, List<AiObject> objects) {
        ResourceAccessInfo allowed = resources.getAllowBrowseInfo(levels, uid);
        for (AiObject object : objects) {
            if (object.getType() != UmiTypes.Table && object.getType() != UmiTypes.View) {
                throw AiError.INVALID_REQUEST.exception();
            }
            if (!allowed.isAllAllow() && !allowed.getAllowQueryList().contains(object.getName())) {
                throw AiError.CONTEXT_REVOKED.exception();
            }
        }
    }

    public List<AiObject> listObjects(String puid, String uid, DsLevels levels, String keyword) {
        levels = namespace(puid, uid, levels.levels());
        ResourceAccessInfo allowed = resources.getAllowBrowseInfo(levels, uid);
        Set<AiObject> result = new HashSet<>();
        for (UmiTypes type : List.of(UmiTypes.Table, UmiTypes.View)) {
            List<DsElement> entries = schema.listLeaf(levels.dsDO(), levels.levelsParam(), type, null, false);
            for (DsElement entry : entries) {
                if (!allowed.isAllAllow() && !allowed.getAllowQueryList().contains(entry.getObjName())) {
                    continue;
                }
                if (!entry.getObjName().contains(keyword)) {
                    continue;
                }
                result.add(new AiObject(type, entry.getObjName()));
            }
        }
        List<AiObject> objects = result.stream().sorted(Comparator.comparing(AiObject::getName).thenComparing(o -> o.getType().name())).toList();
        authorizeObjects(puid, uid, levels, objects);
        return objects;
    }

    public List<AiTableSchema> describeTables(String puid, String uid, DsLevels levels, List<AiObject> objects, AiRunHandle handle) {
        handle.checkActive();
        levels = namespace(puid, uid, levels.levels());
        requireObjects(uid, levels, objects);
        Map<AiObject, RdbTable> tables = new LinkedHashMap<>();
        Map<AiObject, AiTableSchema> authorized = new LinkedHashMap<>();
        for (AiObject object : objects) {
            handle.checkActive();
            Value value = schema.detailLeaf(levels.dsDO(), levels.levelsParam(), object.getType(), object.getName(), false);
            handle.checkActive();
            if (!(value instanceof RdbTable table) || !object.getName().equals(table.getName()) || table.getColumns() == null || table.getColumns().isEmpty()) {
                throw AiError.METADATA_FAILED.exception();
            }
            tables.put(object, table);
            authorized.put(object, describeColumns(puid, uid, levels, object, table));
        }
        List<AiTableSchema> result = new ArrayList<>();
        for (AiTableSchema visible : authorized.values()) {
            handle.checkActive();
            List<AiForeignKey> keys = authorizedForeignKeys(tables.get(visible.getObject()), visible, authorized);
            result.add(new AiTableSchema(visible.getObject(),
                visible.getComment(),
                visible.getColumns(),
                visible.isColumnsComplete(),
                visible.getPrimaryKey(),
                keys,
                visible.isKeysKnown(),
                visible.getSchemaReadAt()));
        }
        authorizeReferences(puid, uid, levels, result.stream()
            .map(table -> new AiContextReference(table.getObject().getType(), table.getObject().getName(), table.getColumns().stream().map(AiColumn::getName).toList()))
            .toList());
        handle.checkActive();
        return List.copyOf(result);
    }

    private AiTableSchema describeColumns(String puid, String uid, DsLevels levels, AiObject object, RdbTable table) {
        List<AiColumn> columns = new ArrayList<>();
        List<String> objectPath = new ArrayList<>(levels.dbLevels());
        objectPath.add(object.getName());
        boolean fullTable = authorization
            .checkResAuthWithoutError(puid, uid, levels.dsDO().getId(), RdpAuthUtils.genResPathByList(objectPath), SecDataAuthLabel.DM_DAUTH_QUERY, AuthKind.DataSource);
        for (RdbColumn column : table.getColumns().values()) {
            if (!fullTable && !authorization.checkResAuthWithoutError(puid, uid, levels.dsDO().getId(), RdpAuthUtils
                .genResPathByList(objectPath, column.getName()), SecDataAuthLabel.DM_DAUTH_QUERY, AuthKind.DataSource)) {
                continue;
            }
            String type = null;
            if (column.getSqlType() != null) {
                type = column.getSqlType().name();
            }
            columns.add(new AiColumn(column.getName(),
                type,
                column.getCharLength(),
                column.getNumericPrecision(),
                column.getNumericScale(),
                column.getNumericUnsigned(),
                column.getArrayDimension(),
                column.getDatetimePrecision(),
                column.getComment()));
        }
        List<String> primaryKey = List.of();
        Set<String> visibleColumns = new HashSet<>();
        columns.forEach(column -> visibleColumns.add(column.getName()));
        if (columns.isEmpty()) {
            throw AiError.CONTEXT_REVOKED.exception();
        }
        if (table.getPrimaryKey() != null && table.getPrimaryKey().getColumnList() != null) {
            if (visibleColumns.containsAll(table.getPrimaryKey().getColumnList())) {
                primaryKey = List.copyOf(table.getPrimaryKey().getColumnList());
            }
        }
        // UMI currently has no completeness marker. An empty key list cannot prove that no keys exist.
        AiTableSchema result = new AiTableSchema(object,
            table.getComment(),
            List.copyOf(columns),
            columns.size() == table.getColumns().size(),
            primaryKey,
            List.of(),
            false,
            Instant.now());
        return result;
    }

    private List<AiForeignKey> authorizedForeignKeys(RdbTable table, AiTableSchema source, Map<AiObject, AiTableSchema> authorized) {
        List<AiForeignKey> result = new ArrayList<>();
        if (table.getForeignKeys() == null) {
            return result;
        }
        Set<String> sourceColumns = new HashSet<>();
        source.getColumns().forEach(column -> sourceColumns.add(column.getName()));
        for (RdbForeignKey key : table.getForeignKeys()) {
            AiObject target = new AiObject(UmiTypes.Table, key.getReferenceTable());
            if (!authorized.containsKey(target) || !Objects.equals(table.getCatalog(), key.getReferenceCatalog()) || !Objects.equals(table.getSchema(), key.getReferenceSchema())
                || key.getReferenceMapping() == null) {
                continue;
            }
            Set<String> targetColumns = new HashSet<>();
            authorized.get(target).getColumns().forEach(column -> targetColumns.add(column.getName()));
            if (sourceColumns.containsAll(key.getReferenceMapping().keySet()) && targetColumns.containsAll(key.getReferenceMapping().values())) {
                result.add(new AiForeignKey(target.getName(), Map.copyOf(key.getReferenceMapping())));
            }
        }
        return List.copyOf(result);
    }

    public void authorizeReferences(String puid, String uid, DsLevels levels, List<AiContextReference> references) {
        levels = namespace(puid, uid, levels.levels());
        requireObjects(uid, levels, references.stream().map(r -> new AiObject(r.getType(), r.getName())).toList());
        for (AiContextReference reference : references) {
            if (reference.getColumns().isEmpty()) {
                continue;
            }
            List<String> path = new ArrayList<>(levels.dbLevels());
            path.add(reference.getName());
            if (authorization
                .checkResAuthWithoutError(puid, uid, levels.dsDO().getId(), RdpAuthUtils.genResPathByList(path), SecDataAuthLabel.DM_DAUTH_QUERY, AuthKind.DataSource)) {
                continue;
            }
            for (String column : reference.getColumns()) {
                if (!authorization.checkResAuthWithoutError(puid, uid, levels.dsDO().getId(), RdpAuthUtils
                    .genResPathByList(path, column), SecDataAuthLabel.DM_DAUTH_QUERY, AuthKind.DataSource)) {
                    throw AiError.CONTEXT_REVOKED.exception();
                }
            }
        }
    }

    private void requireBrowse(String puid, String uid, DsLevels levels, DsResPath path) {
        try {
            authorization.checkBrowseAuth(puid, uid, levels.dsDO().getId(), AuthKind.DataSource, path, SecDataAuthLabel.DM_DAUTH_QUERY);
        } catch (ErrorMessageException e) {
            // Platform errors embed resource names; inaccessible history must not return those names.
            throw AiError.CONTEXT_REVOKED.exception();
        }
    }

}
