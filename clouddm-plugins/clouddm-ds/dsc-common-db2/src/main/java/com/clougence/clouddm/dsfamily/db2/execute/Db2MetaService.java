package com.clougence.clouddm.dsfamily.db2.execute;

import java.sql.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.db2.definition.ui.editor.table.Db2EditorProvider;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.rdb.DefaultRdbMetaService;
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
public abstract class Db2MetaService extends DefaultRdbMetaService {

    public Db2MetaService(Session rdbSession){
        super(rdbSession);
    }

    @Override
    protected SqlBuilder getSqlBuilder() { return Db2EditorProvider.INSTANCE; }

    @Override
    public String getCurrentCatalog() {
        try {
            return this.rdbSession.executeQuery(con -> {
                try (Statement s = con.createStatement(); ResultSet resultSet = s.executeQuery("VALUES CURRENT SERVER")) {
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
    public String getCurrentSchema() {
        try {
            return this.rdbSession.executeQuery(con -> {
                try (Statement s = con.createStatement(); ResultSet resultSet = s.executeQuery("VALUES CURRENT SCHEMA")) {
                    return ((SingleValueRowMapper<String>) (rs, columnType, columnTypeName, columnClassName) -> rs.getString(1)).mapRow(resultSet);
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
                try (Statement s = con.createStatement(); ResultSet resultSet = s.executeQuery("VALUES 1")) {
                    return ((SingleValueRowMapper<Integer>) (rs, columnType, columnTypeName, columnClassName) -> rs.getInt(1)).mapRow(resultSet);
                }
            });
            if (res != 1) {
                throw new SQLException("Test SQL 'select 1 from sysibm.sysdummy1' failed.");
            }
        } catch (Exception e) {
            String msg = "testConnect failed, " + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public List<String> requestObjectScript(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName) {
        //https://blog.csdn.net/qq_40028149/article/details/118577981
        //        --2个参数，第1个输入参数  ... -t 模式.表名
        //                --2个参数，第1个输入参数  ... -v 模式.视图名
        //                --2个参数，第2个 ? 是输出参数
        //
        //        CALL SYSPROC.DB2LK_GENERATE_DDL('-e -x -td ;  -t S1.T1',?)
        //        CALL SYSPROC.DB2LK_GENERATE_DDL('-e -x -td ;  -v  S1.V1',?)
        try {
            return this.rdbSession.executeQuery(con -> {
                switch (leafType) {
                    case Procedure:
                        String showProcedureSql = "SELECT TEXT FROM SYSCAT.ROUTINES WHERE ROUTINETYPE = 'P' AND ROUTINESCHEMA = ? AND SPECIFICNAME = ? ";
                        return showCreateObject(con, showProcedureSql, StringUtils.toString(levelsParam.get(UmiTypes.Schema)), leafName);
                    case Function:
                        String showFunctionSql = "SELECT TEXT FROM SYSCAT.ROUTINES WHERE ROUTINETYPE = 'F' AND ROUTINESCHEMA = ? AND SPECIFICNAME = ? ";
                        return showCreateObject(con, showFunctionSql, StringUtils.toString(levelsParam.get(UmiTypes.Schema)), leafName);
                    case Trigger:
                        String showTriggerSql = "SELECT TEXT FROM SYSCAT.TRIGGERS WHERE TRIGSCHEMA = ? AND TRIGNAME = ? ";
                        return showCreateObject(con, showTriggerSql, StringUtils.toString(levelsParam.get(UmiTypes.Schema)), leafName);
                    default:
                        throw new UnsupportedOperationException("Oracle '" + leafType + "' Unsupported.");
                }
            });
        } catch (Exception e) {
            String msg = "requestObjectScript error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
        //        return super.requestObjectScript(levelsParam, leafType, leafName);
    }

    private List<String> showCreateObject(Connection con, String showSql, String schema, String name) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(showSql)) {
            ps.setString(1, schema);
            ps.setString(2, name);
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
