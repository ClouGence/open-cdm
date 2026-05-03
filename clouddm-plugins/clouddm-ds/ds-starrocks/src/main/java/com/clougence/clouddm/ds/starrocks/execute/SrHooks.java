package com.clougence.clouddm.ds.starrocks.execute;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.clougence.clouddm.ds.starrocks.dialect.StarRocksDialect;
import com.clougence.clouddm.dsfamily.mysql.execute.MyHooks;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;
import com.clougence.clouddm.sdk.execute.session.result.ColReader;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * https://docs.starrocks.io/zh/docs/introduction/StarRocks_intro/
 * @author Ekko 2022/11/03 16:48
 **/
@Slf4j
public class SrHooks extends MyHooks {

    public SrHooks(String mainVersion){
        super(mainVersion);
    }

    @Override
    public ColReader createColReader() {
        return new SrColReader();
    }

    @Override
    public DsMetaService createMetaService(Session session) {
        return new SrMetaService(session);
    }

    @Override
    public void configSession(Connection resource, SessionContextDTO initContextDTO) throws SQLException {
        if (StringUtils.isNotBlank(initContextDTO.getRdbCatalog())) {
            this.setCurrentCatalog(resource, initContextDTO.getRdbCatalog());
        }

        if (StringUtils.isNotBlank(initContextDTO.getRdbSchema())) {
            this.setCurrentSchema(resource, initContextDTO.getRdbSchema());
        }

        //this.setCurrentAutoCommit(resource, initContextDTO.isRdbAutoCommit());
        //this.setCurrentIsolation(resource, initContextDTO.getRdbTxIsolation());
        //this.setCurrentReadOnly(resource, initContextDTO.isRdbReadOnly());
    }

    @Override
    public void setCurrentCatalog(Connection conn, String catalogName) throws SQLException {
        if (StringUtils.isNotBlank(catalogName)) {
            try (Statement s = conn.createStatement()) {
                s.executeUpdate("set catalog " + StarRocksDialect.INSTANCE.fmtName(true, catalogName));
            }
        }
    }

    @Override
    public void setCurrentSchema(Connection conn, String schemaName) throws SQLException {
        if (StringUtils.isNotBlank(schemaName)) {
            try (Statement s = conn.createStatement()) {
                s.executeUpdate("use " + StarRocksDialect.INSTANCE.fmtName(true, schemaName));
            }
        }
    }

    @Override
    public boolean isAutoCommit(Connection conn) throws SQLException {
        return true;
    }

    @Override
    public void setAutoCommit(Connection conn, boolean autoCommit) {
        throw new UnsupportedOperationException("StarRocks Unsupported.");
    }

    @Override
    public RdbIsolation getIsolation(Connection conn) throws SQLException {
        return RdbIsolation.DEFAULT;//RdbIsolation.valueOfCode(conn.getTransactionIsolation());
    }

    @Override
    public void setIsolation(Connection conn, RdbIsolation isolation) {
        throw new UnsupportedOperationException("StarRocks Unsupported.");
    }

    @Override
    public boolean isReadOnly(Connection conn) throws SQLException {
        return false;
    }

    @Override
    public void setReadOnly(Connection conn, boolean readOnly) {
        throw new UnsupportedOperationException("StarRocks Unsupported.");
    }

    @Override
    public void killProcess(Connection connection, String queryID) throws SQLException {
        try (Statement s = connection.createStatement();) {
            s.executeUpdate("kill query " + queryID);
        }
    }

}
