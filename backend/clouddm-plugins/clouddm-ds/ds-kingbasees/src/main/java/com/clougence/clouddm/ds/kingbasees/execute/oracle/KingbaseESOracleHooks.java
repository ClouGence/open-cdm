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
package com.clougence.clouddm.ds.kingbasees.execute.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.ds.kingbasees.execute.KingbaseESHooks;
import com.clougence.clouddm.dsfamily.oracle.execute.OraHooks;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;
import com.clougence.clouddm.sdk.execute.session.result.ColReader;

public class KingbaseESOracleHooks extends OraHooks {

    private final KingbaseESHooks kingbaseESHooks = new KingbaseESHooks();

    public KingbaseESOracleHooks(DataSourceConfig config){
        super(config);
    }

    @Override
    public DsMetaService createMetaService(Session session) {
        return new KingbaseESOracleMetaService(session);
    }

    @Override
    public ColReader createColReader() {
        return kingbaseESHooks.createColReader();
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
    public void setReadOnly(Connection connection, boolean readOnly) throws SQLException {
        kingbaseESHooks.setReadOnly(connection, readOnly);
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
    public String getQueryID(Connection connection) throws SQLException {
        return KingbaseESHooks.getQueryId(connection);
    }

    @Override
    public void killProcess(Connection connection, String queryID) throws SQLException {
        KingbaseESHooks.cancelQuery(connection, queryID);
    }
}
