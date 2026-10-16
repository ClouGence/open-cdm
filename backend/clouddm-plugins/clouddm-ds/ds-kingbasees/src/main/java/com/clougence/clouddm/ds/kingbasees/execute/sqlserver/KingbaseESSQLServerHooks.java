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
package com.clougence.clouddm.ds.kingbasees.execute.sqlserver;

import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.ds.kingbasees.execute.KingbaseESHooks;
import com.clougence.clouddm.dsfamily.sqlserver.execute.MsSqlHooks;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.result.ColReader;

public class KingbaseESSQLServerHooks extends MsSqlHooks {

    private final KingbaseESHooks metadataHooks = new KingbaseESHooks();

    @Override
    public DsMetaService createMetaService(Session session) {
        return new KingbaseESSQLServerMetaService(session);
    }

    @Override
    public ColReader createColReader() {
        return metadataHooks.createColReader();
    }

    @Override
    public void configSession(Connection resource, SessionContextDTO contextDTO) throws SQLException {
        metadataHooks.configSession(resource, contextDTO);
    }

    @Override
    public void setCurrentCatalog(Connection connection, String catalogName) {
        metadataHooks.setCurrentCatalog(connection, catalogName);
    }

    @Override
    public void setCurrentSchema(Connection connection, String schemaName) {
        try {
            metadataHooks.setCurrentSchema(connection, schemaName);
        } catch (SQLException e) {
            throw new RuntimeException("Set KingbaseES SQLServer schema failed.", e);
        }
    }

    @Override
    public Statement explainStatement(Connection connection, QueryRequest query) throws SQLException {
        return metadataHooks.explainStatement(connection, query);
    }

    @Override
    public String getQueryID(Connection connection) throws SQLException {
        return KingbaseESHooks.getQueryId(connection);
    }

    @Override
    public void killProcess(Connection connection, String queryID) throws SQLException {
        KingbaseESHooks.cancelQuery(connection, queryID);
    }

    @Override
    public ColMetaData getColumnMetaData(QueryRequest query, ResultSetMetaData metaData, int columnIndex) throws SQLException {
        return metadataHooks.getColumnMetaData(query, metaData, columnIndex);
    }
}
