package com.clougence.clouddm.ds.ads.execute.ads4my;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.clougence.clouddm.ds.ads.dialect.ads4my.AdbMySqlDialect;
import com.clougence.clouddm.dsfamily.mysql.execute.MyHooks;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;
import com.clougence.utils.StringUtils;

/**
 * only for integration test
 *
 * @author mode create time is 2021/1/12
 **/
public class AdsMyHooks extends MyHooks {

    public AdsMyHooks(String mainVersion){
        super(mainVersion);
    }

    @Override
    public DsMetaService createMetaService(Session session) {
        return new AdsMyMetaService(session);
    }

    @Override
    public void configSession(Connection resource, SessionContextDTO initContextDTO) throws SQLException {
        if (StringUtils.isNotBlank(initContextDTO.getRdbSchema())) {
            this.setCurrentSchema(resource, initContextDTO.getRdbSchema());
        }

        //this.setCurrentAutoCommit(resource, initContextDTO.isRdbAutoCommit());
        //this.setCurrentIsolation(resource, initContextDTO.getRdbTxIsolation());
        //this.setCurrentReadOnly(resource, initContextDTO.isRdbReadOnly());
    }

    @Override
    public void setCurrentCatalog(Connection conn, String catalogName) {
        throw new UnsupportedOperationException("AnalyticDB MySQL Unsupported.");
    }

    @Override
    public void setCurrentSchema(Connection conn, String schemaName) throws SQLException {
        if (StringUtils.isNotBlank(schemaName)) {
            try (Statement s = conn.createStatement()) {
                s.executeUpdate("use " + AdbMySqlDialect.INSTANCE.fmtName(true, schemaName));
            }
        }
    }

    @Override
    public boolean isAutoCommit(Connection conn) throws SQLException {
        return true;
    }

    @Override
    public void setAutoCommit(Connection conn, boolean autoCommit) {
        throw new UnsupportedOperationException("AnalyticDB MySQL Unsupported.");
    }

    @Override
    public RdbIsolation getIsolation(Connection conn) throws SQLException {
        return RdbIsolation.DEFAULT;//RdbIsolation.valueOfCode(conn.getTransactionIsolation());
    }

    @Override
    public void setIsolation(Connection conn, RdbIsolation isolation) {
        throw new UnsupportedOperationException("AnalyticDB MySQL Unsupported.");
    }

    @Override
    public boolean isReadOnly(Connection conn) throws SQLException {
        return false;//conn.isReadOnly();
    }

    @Override
    public void setReadOnly(Connection conn, boolean readOnly) {
        throw new UnsupportedOperationException("AnalyticDB MySQL Unsupported.");
    }
}
