package com.clougence.clouddm.dsfamily.mysql.execute;

import java.sql.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.table.MyEditorProvider;
import com.clougence.clouddm.dsfamily.mysql.dialect.MySqlDialect;
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
 * @author mode 2021/1/15 17:11
 */
@Slf4j
public class MyMetaService extends DefaultRdbMetaService {

    public MyMetaService(Session rdbSession){
        super(rdbSession);
    }

    @Override
    protected DmRdbUmiService rdbUmiService(Connection con) {
        return new MyUmiServiceDm(con);
    }

    @Override
    protected SqlBuilder getSqlBuilder() { return MyEditorProvider.INSTANCE; }

    @Override
    public String getCurrentCatalog() { return null; }

    @Override
    public String getCurrentSchema() {
        try {
            return this.rdbSession.executeQuery(con -> {
                try (Statement s = con.createStatement(); ResultSet resultSet = s.executeQuery("select database()")) {
                    return ((SingleValueRowMapper<String>) (rs, columnType, columnTypeName, columnClassName) -> rs.getString(1)).mapRow(resultSet);
                }
            });
        } catch (Exception e) {
            String msg = "getCurrentCatalog failed, " + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

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
                    case Trigger:
                        return showCreateTrigger(con, StringUtils.toString(levelsParam.get(UmiTypes.Schema)), leafName);
                    case Procedure:
                        return showCreateProcedure(con, StringUtils.toString(levelsParam.get(UmiTypes.Schema)), leafName);
                    case Function:
                        return showCreateFunction(con, StringUtils.toString(levelsParam.get(UmiTypes.Schema)), leafName);
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

    private List<String> showCreateProcedure(Connection con, String schema, String leafName) throws SQLException {
        String showSql = "show create procedure " + MySqlDialect.INSTANCE.fmtTableName(true, null, schema, leafName);
        try (Statement s = con.createStatement(); ResultSet rs = s.executeQuery(showSql)) {
            if (rs.next()) {
                return Collections.singletonList(rs.getString("Create Procedure"));
            } else {
                return Collections.emptyList();
            }
        }
    }

    private List<String> showCreateFunction(Connection con, String schema, String leafName) throws SQLException {
        String showSql = "show create function " + MySqlDialect.INSTANCE.fmtTableName(true, null, schema, leafName);
        try (Statement s = con.createStatement(); ResultSet rs = s.executeQuery(showSql)) {
            if (rs.next()) {
                return Collections.singletonList(rs.getString("Create Function"));
            } else {
                return Collections.emptyList();
            }
        }
    }

    private List<String> showCreateTrigger(Connection con, String schema, String leafName) throws SQLException {
        String showSql = "show create trigger " + MySqlDialect.INSTANCE.fmtTableName(true, null, schema, leafName);

        try (Statement s = con.createStatement(); ResultSet rs = s.executeQuery(showSql)) {
            if (rs.next()) {
                return Collections.singletonList(rs.getString("SQL Original Statement"));
            } else {
                return Collections.emptyList();
            }
        }
    }

    private List<String> showCreateSchema(Connection con, String schema) throws SQLException {
        String showSql = "show create database " + MySqlDialect.INSTANCE.fmtName(true, schema);

        try (Statement s = con.createStatement(); ResultSet rs = s.executeQuery(showSql)) {
            if (rs.next()) {
                return Collections.singletonList(rs.getString("Create Database"));
            } else {
                return Collections.emptyList();
            }
        }
    }

    protected List<String> showCreateTable(Connection con, String schema, String table) throws SQLException {
        String showSql = "show create table " + MySqlDialect.INSTANCE.fmtTableName(true, null, schema, table);

        try (Statement s = con.createStatement(); ResultSet rs = s.executeQuery(showSql)) {
            if (rs.next()) {
                return Collections.singletonList(rs.getString("Create Table"));
            } else {
                return Collections.emptyList();
            }
        }
    }

    private List<String> showCreateView(Connection con, String schema, String view) throws SQLException {
        String typeSql = "SELECT TABLE_TYPE FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ?";

        String type = "view";

        try (PreparedStatement s = con.prepareStatement(typeSql)) {
            s.setString(1, schema);
            s.setString(2, view);

            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                String string = rs.getString(1);
                if (string.equals("SYSTEM VIEW")) {
                    type = "table";
                }
            }
        }

        String showSql = "show create " + type + " " + MySqlDialect.INSTANCE.fmtTableName(true, null, schema, view);

        try (Statement s = con.createStatement(); ResultSet rs = s.executeQuery(showSql)) {
            if (rs.next()) {
                return Collections.singletonList(rs.getString(2));
            } else {
                return Collections.emptyList();
            }
        }
    }
}
