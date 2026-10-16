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
package com.clougence.clouddm.ds.kingbasees.execute.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.clougence.clouddm.ds.kingbasees.execute.KingbaseESHooks;
import com.clougence.clouddm.dsfamily.mysql.execute.MyHooks;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;
import com.clougence.clouddm.sdk.execute.session.result.ColReader;

public class KingbaseESMySQLHooks extends MyHooks {

    private final KingbaseESHooks kingbaseESHooks = new KingbaseESHooks();

    public KingbaseESMySQLHooks(String mainVersion){
        super(mainVersion);
    }

    @Override
    public ColReader createColReader() {
        return kingbaseESHooks.createColReader();
    }

    @Override
    public DsMetaService createMetaService(Session session) {
        return new KingbaseESMySQLMetaService(session);
    }

    @Override
    public void configSession(Connection resource, SessionContextDTO contextDTO) throws SQLException {
        kingbaseESHooks.configSession(resource, contextDTO);
    }

    @Override
    public void setCurrentCatalog(Connection connection, String catalogName) {
        kingbaseESHooks.setCurrentCatalog(connection, catalogName);
    }

    @Override
    public void setCurrentSchema(Connection connection, String schemaName) throws SQLException {
        kingbaseESHooks.setCurrentSchema(connection, schemaName);
    }

    @Override
    public void setAutoCommit(Connection connection, boolean autoCommit) throws SQLException {
        kingbaseESHooks.setAutoCommit(connection, autoCommit);
    }

    @Override
    public boolean isAutoCommit(Connection connection) throws SQLException {
        return kingbaseESHooks.isAutoCommit(connection);
    }

    @Override
    public void setIsolation(Connection connection, RdbIsolation isolation) throws SQLException {
        kingbaseESHooks.setIsolation(connection, isolation);
    }

    @Override
    public RdbIsolation getIsolation(Connection connection) throws SQLException {
        return kingbaseESHooks.getIsolation(connection);
    }

    @Override
    public void setReadOnly(Connection connection, boolean readOnly) {
        try {
            kingbaseESHooks.setReadOnly(connection, readOnly);
        } catch (SQLException e) {
            throw new RuntimeException("Set KingbaseES MySQL read-only state failed.", e);
        }
    }

    @Override
    public boolean isReadOnly(Connection connection) throws SQLException {
        return kingbaseESHooks.isReadOnly(connection);
    }

    @Override
    public PreparedStatement explainStatement(Connection connection, QueryRequest query) throws SQLException {
        return kingbaseESHooks.explainStatement(connection, query);
    }

    @Override
    protected void setFetchSize(PreparedStatement statement) throws SQLException {
        statement.setFetchSize(200);
    }

    @Override
    public String getQueryID(Connection connection) throws SQLException {
        return KingbaseESHooks.getQueryId(connection);
    }

    @Override
    public void killProcess(Connection connection, String queryID) throws SQLException {
        KingbaseESHooks.cancelQuery(connection, queryID);
    }
}
