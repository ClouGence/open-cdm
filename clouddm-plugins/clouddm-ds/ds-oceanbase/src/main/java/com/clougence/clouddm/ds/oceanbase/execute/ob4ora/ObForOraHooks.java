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
package com.clougence.clouddm.ds.oceanbase.execute.ob4ora;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.ds.oracle.execute.OraHooks;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;
import com.clougence.utils.StringUtils;
import com.clougence.utils.jdbc.mapper.SingleRowMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * https://en.oceanbase.com/docs/enterprise-oceanbase-database-en-10000000000383474
 *
 * SELECT @@max_allowed_packet,@@system_time_zone,@@time_zone,@@auto_increment_increment,@@tx_isolation AS tx_isolation,@@session.tx_read_only AS tx_read_only from dual
 * @author Ekko 2022/11/03 16:48
 **/
@Slf4j
public class ObForOraHooks extends OraHooks {

    public ObForOraHooks(DataSourceConfig config){
        super(config);
    }

    @Override
    public DsMetaService createMetaService(Session session) {
        return new ObForOraMetaService(session);
    }

    @Override
    public void setCurrentCatalog(Connection conn, String catalogName) {
        throw new UnsupportedOperationException("OceanBase Unsupported.");
    }

    @Override
    public void setAutoCommit(Connection conn, boolean autoCommit) throws SQLException {
        try (Statement s = conn.createStatement()) {
            if (autoCommit) {
                s.executeUpdate("set autocommit = 1");
            } else {
                s.executeUpdate("set autocommit = 0");
            }
        }
    }

    @Override
    public boolean isAutoCommit(Connection conn) throws SQLException {
        //https://www.oceanbase.com/docs/common-oceanbase-database-cn-0000000002183537
        //select @@tx_isolation AS tx_isolation,@@session.tx_read_only AS tx_read_only from dual
        try (Statement s = conn.createStatement(); ResultSet rs = s.executeQuery("show variables like 'autocommit'")) {
            String status = ((SingleRowMapper<String>) r -> r.getString(2)).mapRow(rs);
            return StringUtils.equalsIgnoreCase(status, "on") || StringUtils.equalsIgnoreCase(status, "1");
        }
    }

    @Override
    public void configSession(Connection resource, SessionContextDTO initContextDTO) throws SQLException {
        if (StringUtils.isNotBlank(initContextDTO.getRdbSchema())) {
            this.setCurrentSchema(resource, initContextDTO.getRdbSchema());
        }

        this.setAutoCommit(resource, initContextDTO.isRdbAutoCommit());
        this.setIsolation(resource, initContextDTO.getRdbTxIsolation());
    }

    @Override
    public RdbIsolation getIsolation(Connection conn) throws SQLException {
        try (Statement s = conn.createStatement(); ResultSet rs = s.executeQuery("select @@tx_isolation as tx_isolation from dual")) {
            String status = ((SingleRowMapper<String>) r -> r.getString(1)).mapRow(rs);
            if (StringUtils.equalsIgnoreCase(status, "READ-COMMITTED")) {
                return RdbIsolation.READ_COMMITTED;
            } else if (StringUtils.equalsIgnoreCase(status, "REPEATABLE-READ")) {
                return RdbIsolation.REPEATABLE_READ;
            } else if (StringUtils.equalsIgnoreCase(status, "SERIALIZABLE")) {
                return RdbIsolation.SERIALIZABLE;
            } else {
                // TODO need print Warnings to console.
                return null;
            }
        }
    }

    @Override
    public void setIsolation(Connection conn, RdbIsolation isolation) throws SQLException {
        // http://www.oceanbase.wiki/concept/transaction-management/transaction-concurrency-and-consistency/transaction-isolation-levels/transaction-isolation-levels-mysql-mode/
        if (isolation != null) {
            try (Statement s = conn.createStatement()) {
                switch (isolation) {
                    case DEFAULT:
                    case READ_COMMITTED:
                        s.execute("SET SESSION TRANSACTION ISOLATION LEVEL READ COMMITTED");
                        break;
                    case REPEATABLE_READ:
                        s.execute("SET SESSION TRANSACTION ISOLATION LEVEL REPEATABLE READ");
                        break;
                    default:
                        throw new UnsupportedOperationException("OceanBase Unsupported.");
                }
            }
        }
    }

    @Override
    public boolean isReadOnly(Connection conn) throws SQLException {
        //select @@session.tx_read_only AS tx_read_only from dual
        return false;
    }

    @Override
    public void setReadOnly(Connection conn, boolean readOnly) {
        throw new UnsupportedOperationException("OceanBase Unsupported.");
    }

    //    @Override
    //    public void killProcess(Connection connection, String queryID) throws SQLException {
    //        String sql = "kill query " + queryID;
    //        Statement statement = connection.createStatement();
    //        statement.executeUpdate(sql);
    //    }
}
