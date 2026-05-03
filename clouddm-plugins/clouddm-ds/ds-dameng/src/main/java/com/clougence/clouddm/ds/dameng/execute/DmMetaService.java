package com.clougence.clouddm.ds.dameng.execute;

import java.sql.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.dameng.definition.ui.editor.table.DmEditorProvider;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.rdb.DefaultRdbMetaService;
import com.clougence.clouddm.sdk.execute.session.rdb.DmRdbUmiService;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.jdbc.JdbcTemplate;
import com.clougence.utils.jdbc.mapper.SingleValueRowMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2021/1/15 17:11
 */
@Slf4j
public class DmMetaService extends DefaultRdbMetaService {

    public DmMetaService(Session rdbSession){
        super(rdbSession);
    }

    @Override
    protected DmRdbUmiService rdbUmiService(Connection con) {
        return new DmUmiServiceDm(con);
    }

    @Override
    protected SqlBuilder getSqlBuilder() { return DmEditorProvider.INSTANCE; }

    @Override
    public void testConnect() {
        try {
            int res = this.rdbSession.executeQuery(con -> new JdbcTemplate(con).queryForInt("select 1"));
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
    public String getCurrentCatalog() { return null; }

    @Override
    public String getCurrentSchema() {
        try {
            return this.rdbSession.executeQuery(con -> {
                String queryString = "select SYS_CONTEXT('USERENV','CURRENT_SCHEMA') CURRENT_SCHEMA from dual";
                try (Statement s = con.createStatement(); ResultSet resultSet = s.executeQuery(queryString)) {
                    return ((SingleValueRowMapper<String>) (rs, columnType, columnTypeName, columnClassName) -> rs.getString(1)).mapRow(resultSet);
                }
            });
        } catch (Exception e) {
            String msg = "getCurrentSchema error.msg:" + ExceptionUtils.getRootCauseMessage(e);
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
                    default:
                        throw new UnsupportedOperationException("Dameng '" + leafType + "' Unsupported.");
                }
            });
        } catch (Exception e) {
            String msg = "requestObjectScript error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    private List<String> showCreateObject(Connection con, String showTableSql, String schema, String table) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(showTableSql)) {
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

    /*
    public String requestObjectScript(UmiTypes umiType, String[] path) {
        try {
            return this.rdbSession.executeQuery(con -> {
                return new JdbcTemplate(con).execute(connection -> {
                    if (path.length == 1) {
                        return showCreateObject(connection, DsObjectType.Schema, path);
                    } else if (path.length == 2) {
                        return showCreateObject(connection, DsObjectType.Table, path);
                    } else {
                        throw new UnsupportedOperationException("dameng '" + StringUtils.join(path, ".") + "' Unsupported.");
                    }
                });
            });
        } catch (Exception e) {
            String msg = "requestObjectScript error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }
    
    protected String showCreateObject(Connection connection, DsObjectType requestObjectType, Object[] objects) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            if (requestObjectType == DsObjectType.Schema) {
                try (ResultSet resultSet = statement.executeQuery("show create database `" + objects[0] + "`");) {
                    List<Map<String, Object>> mapList = new ColumnMapResultSetExtractor().extractData(resultSet);
                    if (!mapList.isEmpty()) {
                        return (String) mapList.get(0).get("Create Database");
                    }
                }
            } else if (requestObjectType == DsObjectType.Table) {
                try (ResultSet resultSet = statement.executeQuery("show create table `" + objects[0] + "`.`" + objects[1] + "`");) {
                    List<Map<String, Object>> mapList = new ColumnMapResultSetExtractor().extractData(resultSet);
                    if (!mapList.isEmpty()) {
                        return (String) mapList.get(0).get("Create Table");
                    }
                }
            }
        }
        return "";
    }*/
}
