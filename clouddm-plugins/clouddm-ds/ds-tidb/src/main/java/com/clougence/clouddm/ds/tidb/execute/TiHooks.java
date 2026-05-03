package com.clougence.clouddm.ds.tidb.execute;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.clougence.clouddm.ds.tidb.dialect.TiDBDialect;
import com.clougence.clouddm.dsfamily.mysql.execute.MyHooks;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.utils.StringUtils;

/**
 * https://docs.pingcap.com/zh/tidb/stable/transaction-isolation-levels
 *
 * @author mode create time is 2021/1/12
 **/
public class TiHooks extends MyHooks {

    public TiHooks(String mainVersion){
        super(mainVersion);
    }

    @Override
    public DsMetaService createMetaService(Session session) {
        return new TiMetaService(session);
    }

    @Override
    public void configSession(Connection resource, SessionContextDTO initContextDTO) throws SQLException {
        if (StringUtils.isNotBlank(initContextDTO.getRdbSchema())) {
            this.setCurrentSchema(resource, initContextDTO.getRdbSchema());
        }

        this.setAutoCommit(resource, initContextDTO.isRdbAutoCommit());
        this.setIsolation(resource, initContextDTO.getRdbTxIsolation());
        //this.setCurrentReadOnly(resource, initContextDTO.isRdbReadOnly());
    }

    @Override
    public void setCurrentCatalog(Connection conn, String catalogName) {
        throw new UnsupportedOperationException("TiDB Unsupported.");
    }

    @Override
    public void setCurrentSchema(Connection conn, String schemaName) throws SQLException {
        if (StringUtils.isNotBlank(schemaName)) {
            try (Statement s = conn.createStatement()) {
                s.executeUpdate("use " + TiDBDialect.INSTANCE.fmtName(true, schemaName));
            }
        }
    }

    @Override
    public boolean isReadOnly(Connection conn) throws SQLException {
        return false;
    }

    @Override
    public void setReadOnly(Connection conn, boolean readOnly) {
        throw new UnsupportedOperationException("TiDB Unsupported.");
    }
}
