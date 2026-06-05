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
package com.clougence.clouddm.ds.clickhouse.execute;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.clickhouse.definition.ui.editor.table.ChEditorProvider;
import com.clougence.clouddm.ds.clickhouse.dialect.ClickHouseDialect;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.rdb.DefaultRdbMetaService;
import com.clougence.clouddm.sdk.execute.session.rdb.DmRdbUmiService;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.jdbc.mapper.SingleValueRowMapper;

import lombok.extern.slf4j.Slf4j;

/**
* @author Ekko
* @date 2023/3/28 14:13
*/
@Slf4j
public class ChMetaService extends DefaultRdbMetaService {

    public ChMetaService(Session rdbSession){
        super(rdbSession);
    }

    @Override
    protected DmRdbUmiService rdbUmiService(Connection con) {
        return new ChUmiServiceDm(con);
    }

    @Override
    protected SqlBuilder getSqlBuilder() { return ChEditorProvider.INSTANCE; }

    @Override
    public void testConnect() {
        try {
            int res = this.rdbSession.executeQuery(con -> {
                try (Statement s = con.createStatement(); ResultSet resultSet = s.executeQuery("select 1")) {
                    return ((SingleValueRowMapper<Integer>) (rs, columnType, columnTypeName, columnClassName) -> rs.getInt(1)).mapRow(resultSet);
                }
            });
            if (res != 1) {
                throw new SQLException("Test SQL 'select 1' failed.");
            }
        } catch (Exception e) {
            String msg = "testConnect failed, " + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public String getCurrentCatalog() {
        try {
            return this.rdbSession.executeQuery(con -> {
                try (Statement s = con.createStatement(); ResultSet resultSet = s.executeQuery("select database()")) {
                    return ((SingleValueRowMapper<String>) (rs, columnType, columnTypeName, columnClassName) -> rs.getString(1)).mapRow(resultSet);
                } catch (SQLException e) {
                    throw ChExceptionUtils.toException(e);
                }
            });
        } catch (Exception e) {
            String msg = "getCurrentCatalog error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public String getCurrentSchema() {
        try {
            return this.rdbSession.executeQuery(con -> {
                try (Statement s = con.createStatement(); ResultSet resultSet = s.executeQuery("select database()")) {
                    return ((SingleValueRowMapper<String>) (rs, columnType, columnTypeName, columnClassName) -> rs.getString(1)).mapRow(resultSet);
                } catch (SQLException e) {
                    throw ChExceptionUtils.toException(e);
                }
            });
        } catch (Exception e) {
            String msg = "getCurrentSchema error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }

    }

    public List<String> requestObjectScript(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName) {
        try {
            return this.rdbSession.executeQuery(con -> {
                switch (leafType) {
                    case View:
                        return showCreateView(con, StringUtils.toString(levelsParam.get(UmiTypes.Schema)), leafName);
                    case Table:
                        return showCreateTable(con, StringUtils.toString(levelsParam.get(UmiTypes.Schema)), leafName);
                    case Schema:
                        return showCreateSchema(con, StringUtils.toString(levelsParam.get(UmiTypes.Schema)));
                    default:
                        throw new UnsupportedOperationException("mysql '" + leafType + "' Unsupported.");
                }
            });
        } catch (Exception e) {
            String msg = "requestObjectScript error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    private List<String> showCreateSchema(Connection con, String schema) throws SQLException {
        String showSql = "show create database " + ClickHouseDialect.INSTANCE.fmtName(true, schema);

        try (Statement s = con.createStatement(); ResultSet rs = s.executeQuery(showSql)) {
            if (rs.next()) {
                return Collections.singletonList(rs.getString("statement"));
            } else {
                return Collections.emptyList();
            }
        }
    }

    protected List<String> showCreateTable(Connection con, String schema, String table) throws SQLException {
        String showSql = "show create table " + ClickHouseDialect.INSTANCE.fmtTableName(true, null, schema, table);

        try (Statement s = con.createStatement(); ResultSet rs = s.executeQuery(showSql)) {
            if (rs.next()) {
                return Collections.singletonList(rs.getString("statement"));
            } else {
                return Collections.emptyList();
            }
        }
    }

    private List<String> showCreateView(Connection con, String schema, String view) throws SQLException {
        String showSql = "show create view " + ClickHouseDialect.INSTANCE.fmtTableName(true, null, schema, view);

        try (Statement s = con.createStatement(); ResultSet rs = s.executeQuery(showSql)) {
            if (rs.next()) {
                return Collections.singletonList(rs.getString("statement"));
            } else {
                return Collections.emptyList();
            }
        }
    }
}
