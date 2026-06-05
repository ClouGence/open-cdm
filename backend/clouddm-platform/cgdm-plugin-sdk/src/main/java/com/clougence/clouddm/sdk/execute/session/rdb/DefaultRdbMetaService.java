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
package com.clougence.clouddm.sdk.execute.session.rdb;

import java.sql.Connection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.clougence.clouddm.sdk.execute.meta.DsElement;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.schema.editor.EditorHelperDm;
import com.clougence.schema.editor.domain.ETable;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.schema.umi.service.RdbUmiServiceDm;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2021/1/15 17:11
 */
@Slf4j
public abstract class DefaultRdbMetaService implements DsMetaService {

    protected final Session rdbSession;

    protected DefaultRdbMetaService(Session rdbSession){
        this.rdbSession = rdbSession;
    }

    protected abstract DmRdbUmiService rdbUmiService(Connection con);

    protected abstract SqlBuilder getSqlBuilder();

    @Override
    public String loadTableEditor(Map<UmiTypes, Object> levelsParam, String table) {
        try {
            return this.rdbSession.executeQuery(con -> {
                RdbUmiServiceDm umiService = rdbUmiService(con);

                String catalog = StringUtils.toString(levelsParam.get(UmiTypes.Catalog));
                String schema = StringUtils.toString(levelsParam.get(UmiTypes.Schema));

                SqlBuilder sqlBuilder = this.getSqlBuilder();
                EditorHelperDm editorHelper = new EditorHelperDm(umiService, sqlBuilder);
                ETable eTable = editorHelper.loadTable(catalog, schema, table);
                return eTable == null ? null : new ObjectMapper().writeValueAsString(eTable);
            });
        } catch (Exception e) {
            String msg = "loadTableEditor error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public List<String> requestObjectScript(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getVersion() {
        try {
            return this.rdbSession.executeQuery(con -> rdbUmiService(con).getVersion());
        } catch (Exception e) {
            String msg = "getVersion error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public List<DsElement> listLevels(List<UmiTypes> depth, Map<UmiTypes, Object> levelsParam) {
        try {
            List<Value> result = this.rdbSession.executeQuery(con -> rdbUmiService(con).listLevels(depth, levelsParam));
            if (result == null) {
                return Collections.emptyList();
            }
            return result.stream().map(value -> {
                DsElement dsElement = new DsElement();
                dsElement.setObjId(-1);
                dsElement.setObjName(value.asValue());
                dsElement.setObjType(value.getUmiType());
                dsElement.setObjAttr(new HashMap<>(value.getAttributes()));
                return dsElement;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            String msg = "listLevels error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public Value fetchSelectObject(Map<UmiTypes, Object> levelsParam, String leafName) {
        try {
            return this.rdbSession.executeQuery(con -> rdbUmiService(con).fetchSelectObject(levelsParam, leafName));
        } catch (Exception e) {
            String msg = "detailLevel error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public DsElement detailLevel(List<UmiTypes> depth, Map<UmiTypes, Object> levelsParam) {
        try {
            Value result = this.rdbSession.executeQuery(con -> rdbUmiService(con).detailLevel(depth, levelsParam));
            if (result == null) {
                return null;
            }

            DsElement dsElement = new DsElement();
            dsElement.setObjId(-1);
            dsElement.setObjName(result.asValue());
            dsElement.setObjType(result.getUmiType());
            dsElement.setObjAttr(new HashMap<>(result.getAttributes()));
            return dsElement;
        } catch (Exception e) {
            String msg = "detailLevel error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public List<DsElement> listLeaf(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String pattern) {
        try {
            List<Value> result = this.rdbSession.executeQuery(con -> rdbUmiService(con).listLeaf(levelsParam, leafType, pattern));
            if (result == null) {
                return Collections.emptyList();
            }
            return result.stream().map(value -> {
                DsElement dsElement = new DsElement();
                dsElement.setObjId(-1);
                dsElement.setObjName(value.asValue());
                dsElement.setObjType(value.getUmiType());
                dsElement.setObjAttr(new HashMap<>(value.getAttributes()));
                return dsElement;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            String msg = "listLeaf error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public Value detailLeaf(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName) {
        try {
            return this.rdbSession.executeQuery(con -> rdbUmiService(con).detailLeaf(levelsParam, leafType, leafName));
        } catch (Exception e) {
            String msg = "detailLeaf error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public Map<String, List<RdbColumn>> batchColumns(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, List<String> leafNames) {
        try {
            return this.rdbSession.executeQuery(con -> rdbUmiService(con).loadColumns(levelsParam, leafType, leafNames));
        } catch (Exception e) {
            String msg = "batchColumns error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }
}
