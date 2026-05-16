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
package com.clougence.clouddm.ds.oracle.execute;

import java.sql.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.oracle.definition.ui.editor.table.OraEditorProvider;
import com.clougence.clouddm.ds.oracle.dsconf.OraConfig;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.rdb.DefaultRdbMetaService;
import com.clougence.clouddm.sdk.execute.session.rdb.DmRdbUmiService;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.jdbc.mapper.SingleValueRowMapper;
import com.clougence.utils.jdbc.mapper.ValueRowMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2021/1/15 17:11
 */
@Slf4j
public class OraMetaService extends DefaultRdbMetaService {

    public OraMetaService(Session rdbSession){
        super(rdbSession);
    }

    @Override
    protected DmRdbUmiService rdbUmiService(Connection con) {
        return new OraUmiServiceDm(con, excludeOraMaintainedSchemas());
    }

    private boolean excludeOraMaintainedSchemas() {
        OraConfig oraConfig = (OraConfig) this.rdbSession.getDsConfig();
        return oraConfig.getExcludeOraMaintainedSchemas() == null || oraConfig.getExcludeOraMaintainedSchemas();
    }

    @Override
    protected SqlBuilder getSqlBuilder() { return OraEditorProvider.INSTANCE; }

    @Override
    public String getCurrentCatalog() {
        try {
            return this.rdbSession.executeQuery(con -> {
                try (PreparedStatement ps = con.prepareStatement("SELECT GLOBAL_NAME FROM GLOBAL_NAME"); ResultSet resultSet = ps.executeQuery()) {
                    return ((ValueRowMapper<String>) (rs, columnType, typeName, className) -> rs.getString(1)).mapRow(resultSet);
                }
            });
        } catch (Exception e) {
            String msg = "getCurrentCatalog failed, " + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public String getCurrentSchema() {
        try {
            return this.rdbSession.executeQuery(con -> {
                String queryString = "select SYS_CONTEXT('USERENV','CURRENT_SCHEMA') CURRENT_SCHEMA from dual";
                try (Statement ps = con.createStatement(); ResultSet resultSet = ps.executeQuery(queryString)) {
                    return ((ValueRowMapper<String>) (rs, columnType, typeName, className) -> rs.getString(1)).mapRow(resultSet);
                }
            });
        } catch (Exception e) {
            String msg = "getCurrentSchema failed, " + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public void testConnect() {
        try {
            int res = this.rdbSession.executeQuery(con -> {
                try (Statement s = con.createStatement(); ResultSet resultSet = s.executeQuery("select 1 from DUAL")) {
                    return ((SingleValueRowMapper<Integer>) (rs, columnType, columnTypeName, columnClassName) -> rs.getInt(1)).mapRow(resultSet);
                }
            });
            if (res != 1) {
                throw new SQLException("Test SQL 'select 1 from DUAL' failed.");
            }
        } catch (Exception e) {
            String msg = "testConnect failed, " + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public List<String> requestObjectScript(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName) {
        try {
            return this.rdbSession.executeQuery(con -> {
                switch (leafType) {
                    case Table:
                        String showTableSql = "select dbms_metadata.get_ddl('TABLE',?,?) from dual";
                        return showCreateObject(con, showTableSql, StringUtils.toString(levelsParam.get(UmiTypes.Schema)), leafName);
                    case View:
                        String showViewSql = "select dbms_metadata.get_ddl('VIEW',?,?) from dual";
                        return showCreateObject(con, showViewSql, StringUtils.toString(levelsParam.get(UmiTypes.Schema)), leafName);
                    case Materialized:
                        String showMaterializedViewSql = "select dbms_metadata.get_ddl('MATERIALIZED_VIEW',?,?) from dual";
                        return showCreateObject(con, showMaterializedViewSql, StringUtils.toString(levelsParam.get(UmiTypes.Schema)), leafName);
                    case Index:
                        String showIndexSql = "select dbms_metadata.get_ddl('INDEX',?,?) from dual";
                        return showCreateObject(con, showIndexSql, StringUtils.toString(levelsParam.get(UmiTypes.Schema)), leafName);
                    case Procedure:
                        String showProcedureSql = "select dbms_metadata.get_ddl('PROCEDURE',?,?) from dual";
                        return showCreateObject(con, showProcedureSql, StringUtils.toString(levelsParam.get(UmiTypes.Schema)), leafName);
                    case Function:
                        String showFunctionSql = "select dbms_metadata.get_ddl('FUNCTION',?,?) from dual";
                        return showCreateObject(con, showFunctionSql, StringUtils.toString(levelsParam.get(UmiTypes.Schema)), leafName);
                    case Trigger:
                        String showTriggerSql = "select dbms_metadata.get_ddl('TRIGGER',?,?) from dual";
                        return showCreateObject(con, showTriggerSql, StringUtils.toString(levelsParam.get(UmiTypes.Schema)), leafName);
                    case Synonym:
                        String showSynonymSql = "select dbms_metadata.get_ddl('SYNONYM',?,?) from dual";
                        return showCreateObject(con, showSynonymSql, StringUtils.toString(levelsParam.get(UmiTypes.Schema)), leafName);
                    default:
                        throw new UnsupportedOperationException("Oracle '" + leafType + "' Unsupported.");
                }
            });
        } catch (Exception e) {
            String msg = "requestObjectScript error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    private List<String> showCreateObject(Connection con, String showSql, String schema, String table) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(showSql)) {
            ps.setString(1, table);
            ps.setString(2, schema);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Collections.singletonList(rs.getString(1));
                } else {
                    return Collections.emptyList();
                }
            }
        }
    }
}
