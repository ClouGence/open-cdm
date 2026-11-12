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
import java.sql.SQLException;

import com.clougence.clouddm.ds.greptimedb.i18n.GreptimeConfigI18nKeys;
import com.clougence.clouddm.dsfamily.mysql.execute.MyHooks;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;
import com.clougence.clouddm.sdk.execute.session.result.ColReader;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;

final class GreptimeSessionHook extends MyHooks {

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
