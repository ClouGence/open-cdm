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

package com.clougence.clouddm.ds.greptimedb.execute;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.ds.greptimedb.i18n.GreptimeConfigI18nKeys;
import com.clougence.clouddm.dsfamily.mysql.execute.MyHooks;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;
import com.clougence.clouddm.sdk.execute.session.result.ColReader;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.service.secrules.Requester;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.utils.i18n.I18nUtils;

final class GreptimeSessionHook extends MyHooks {

    private static final I18nUtils I18N = I18nUtils.initI18n(GreptimeConfigI18nKeys.class);

    GreptimeSessionHook(String version){
        super(version);
    }

    @Override
    public DsMetaService createMetaService(Session session) {
        return new GreptimeMetaService(session);
    }

    @Override
    public ColReader createColReader() {
        return new GreptimeColReader();
    }

    @Override
    public void configSession(Connection connection, SessionContextDTO context) throws SQLException {
        setCurrentSchema(connection, context.getRdbSchema());
    }

    @Override
    public PreparedStatement executeStatement(Connection connection, QueryRequest query) throws SQLException {
        verifyQueryScope(connection, query);
        return super.executeStatement(connection, query);
    }

    @Override
    public PreparedStatement explainStatement(Connection connection, QueryRequest query) throws SQLException {
        verifyQueryScope(connection, query);
        return super.explainStatement(connection, query);
    }

    private void verifyQueryScope(Connection connection, QueryRequest query) throws SQLException {
        if (query.getRequester() != Requester.CONSOLE) {
            return;
        }

        var scope = query.getResolvedResources();
        if (query.getDsType() != DataSourceType.GreptimeDB || scope == null) {
            throw scopeMismatch();
        }

        var metadata = new GreptimeMetadata(connection);
        List<String> expectedDatabases = scope.get(TargetType.Schema);
        if (expectedDatabases != null) {
            String database = metadata.currentDatabase();
            if (database == null || !expectedDatabases.equals(List.of(database))) {
                throw scopeMismatch();
            }
        }

        // Resolve table types again on the execution connection in case the objects changed while queued.
        for (String path : scope.getOrDefault(TargetType.Table, List.of())) {
            String[] parts = path.split("/");
            if (parts.length != 3 || !metadata.isBaseTable(parts[1], parts[2])) {
                throw scopeMismatch();
            }
        }
    }

    private ThirdPartyApiException scopeMismatch() {
        return ThirdPartyApiException.as().with(I18N.getMessage(GreptimeConfigI18nKeys.GREPTIMEDB_QUERY_SCOPE_MISMATCH));
    }

    @Override
    public boolean isAutoCommit(Connection connection) {
        return true;
    }

    @Override
    public boolean isReadOnly(Connection connection) throws SQLException {
        return connection.isReadOnly();
    }

    @Override
    public RdbIsolation getIsolation(Connection connection) {
        return RdbIsolation.DEFAULT;
    }

    @Override
    public void setAutoCommit(Connection connection, boolean autoCommit) {
        if (!autoCommit) {
            throw ThirdPartyApiException.as().with(GreptimeConfigI18nKeys.GREPTIMEDB_TRANSACTIONS_UNSUPPORTED);
        }
    }

    @Override
    public void setIsolation(Connection connection, RdbIsolation isolation) {
        if (isolation != null && isolation != RdbIsolation.DEFAULT) {
            throw ThirdPartyApiException.as().with(GreptimeConfigI18nKeys.GREPTIMEDB_TRANSACTIONS_UNSUPPORTED);
        }
    }

    @Override
    public void commit(Connection connection) {
        throw ThirdPartyApiException.as().with(GreptimeConfigI18nKeys.GREPTIMEDB_TRANSACTIONS_UNSUPPORTED);
    }

    @Override
    public void rollback(Connection connection) {
        throw ThirdPartyApiException.as().with(GreptimeConfigI18nKeys.GREPTIMEDB_TRANSACTIONS_UNSUPPORTED);
    }
}
