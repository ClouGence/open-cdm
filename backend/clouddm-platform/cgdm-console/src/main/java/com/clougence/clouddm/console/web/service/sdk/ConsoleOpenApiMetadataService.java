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
package com.clougence.clouddm.console.web.service.sdk;

import static com.clougence.clouddm.sdk.openapi.OpenApiErrorCode.*;
import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_QUERY_CONSOLE;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.exception.DmErrorCode;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForBiz;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.model.vo.browse.BrowseLevelsVO;
import com.clougence.clouddm.console.web.service.browse.BrowseService;
import com.clougence.clouddm.console.web.service.browse.model.rdb.BrowseIndexMO;
import com.clougence.clouddm.console.web.service.browse.model.rdb.BrowseObjectMO;
import com.clougence.clouddm.console.web.service.browse.model.rdb.BrowseTableMO;
import com.clougence.clouddm.console.web.service.browse.model.rdb.BrowseTermMO;
import com.clougence.clouddm.sdk.openapi.OpenApiCaller;
import com.clougence.clouddm.sdk.openapi.OpenApiException;
import com.clougence.clouddm.sdk.openapi.metadata.*;
import com.clougence.schema.umi.special.rdb.RdbAttributeNames;
import com.clougence.schema.umi.struts.UmiTypes;

import jakarta.annotation.Resource;

@Service
public class ConsoleOpenApiMetadataService implements OpenApiMetadataService {

    @Resource
    private BrowseService       browseService;
    @Resource
    private DmDsConfigService   configService;
    @Resource
    private DmAuthServiceForBiz authorization;

    @Override
    public OpenApiTablePage listTables(OpenApiCaller caller, OpenApiListTablesQuery query) {
        DsLevels levels = resolveNamespace(caller, query.dataSourceId(), query.namespacePath(), true);
        List<UmiTypes> types;
        if (query.objectType() == null) {
            types = List.of(UmiTypes.Table, UmiTypes.View);
        } else {
            types = List.of(query.objectType());
        }
        List<OpenApiTableSummary> visible = browse(() -> browseService.listVisibleLeaves(caller.puid(), caller.uid(), levels, types, null, false)).stream()
            .filter(item -> types.stream().anyMatch(type -> type.getTypeName().equals(item.getObjType())))
            .map(ConsoleOpenApiMetadataService::tableSummary)
            .sorted(Comparator.comparing(OpenApiTableSummary::tableName).thenComparing(item -> item.objectType().getTypeName()))
            .toList();

        long offset = (query.pageNumber() - 1L) * query.pageSize();
        int from = (int) Math.min(visible.size(), offset);
        int to = (int) Math.min(visible.size(), offset + query.pageSize());
        return new OpenApiTablePage(visible.subList(from, to), visible.size());
    }

    @Override
    public List<OpenApiNamespace> listNamespaces(OpenApiCaller caller, OpenApiListNamespacesQuery query) {
        DsLevels levels = resolveNamespace(caller, query.dataSourceId(), query.namespacePath(), false);
        return browse(() -> browseService.listVisibleLevels(caller.puid(), caller.uid(), levels, false)).stream()
            .sorted(Comparator.comparing(BrowseLevelsVO::getObjName))
            .map(item -> {
                List<String> path = new ArrayList<>(levels.dbLevels());
                path.add(item.getObjName());
                return new OpenApiNamespace(item.getObjName(), UmiTypes.valueOfCode(item.getObjType()), path);
            })
            .toList();
    }

    @Override
    public OpenApiTableDetail getTable(OpenApiCaller caller, OpenApiGetTableQuery query) {
        DsLevels levels = resolveNamespace(caller, query.dataSourceId(), query.namespacePath(), true);
        BrowseObjectMO detail = browse(() -> browseService.rdbObjectDetail(caller.puid(), caller.uid(), levels, query.objectType(), query.tableName(), false));
        if (!(detail instanceof BrowseTableMO table)) {
            throw new OpenApiException(NOT_FOUND);
        }
        List<OpenApiColumn> columns = table.getColumns().stream().map(column -> new OpenApiColumn(column.getName(), column.getDbType(), column.isNullable())).toList();
        List<String> primaryKey = table.getKeys().stream().flatMap(key -> key.getColumns().stream()).map(BrowseTermMO::getName).toList();
        List<OpenApiIndex> indexes = table.getIndexes()
            .stream()
            .filter(index -> !index.isExpression() && !index.getColumns().isEmpty())
            .map(ConsoleOpenApiMetadataService::indexSummary)
            .toList();
        return new OpenApiTableDetail(table.getName(), query.objectType(), columns, primaryKey, indexes);
    }

    private DsLevels resolveNamespace(OpenApiCaller caller, long dataSourceId, List<String> namespacePath, boolean requireFullPath) {
        if (!authorization.checkRoleAuth(caller.puid(), caller.uid(), DM_QUERY_CONSOLE)) {
            throw new OpenApiException(FORBIDDEN);
        }
        DsLevels levels;
        try {
            levels = configService.parseLevels(dataSourceId, namespacePath);
        } catch (ErrorMessageException e) {
            throw new OpenApiException(NOT_FOUND);
        }
        if (!caller.puid().equals(levels.dsDO().getUid())) {
            throw new OpenApiException(NOT_FOUND);
        }
        int depth = configService.dsConstantSettings(levels.dsDO().getDataSourceType()).getCategories().getLevels().size() - 2;
        if (namespacePath.size() > depth || (requireFullPath && namespacePath.size() != depth)) {
            throw new OpenApiException(INVALID_ARGUMENT);
        }
        return levels;
    }

    private <T> T browse(Supplier<T> read) {
        try {
            return read.get();
        } catch (ErrorMessageException e) {
            if (DmErrorCode.O_AUTHORITY.code().equals(e.getErrorCode())) {
                throw new OpenApiException(FORBIDDEN);
            }
            if (DmErrorCode.O_DATA.code().equals(e.getErrorCode())) {
                throw new OpenApiException(NOT_FOUND);
            }
            throw e;
        }
    }

    private static OpenApiTableSummary tableSummary(BrowseLevelsVO item) {
        String comment = null;
        if (item.getObjAttr() != null && item.getObjAttr().get(RdbAttributeNames.COMMENT.getCodeKey()) instanceof String text) {
            comment = text;
        }
        return new OpenApiTableSummary(item.getObjName(), UmiTypes.valueOfCode(item.getObjType()), comment);
    }

    private static OpenApiIndex indexSummary(BrowseIndexMO index) {
        return new OpenApiIndex(index.getName(), index.isUnique(), index.getColumns().stream().map(BrowseTermMO::getName).toList());
    }
}
