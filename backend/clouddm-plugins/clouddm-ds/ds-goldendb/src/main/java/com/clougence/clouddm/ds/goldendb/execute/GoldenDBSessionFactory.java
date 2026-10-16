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
package com.clougence.clouddm.ds.goldendb.execute;

import java.sql.Connection;

import com.clougence.clouddm.ds.goldendb.dsconf.AbstractGoldenDBConfig;
import com.clougence.clouddm.ds.goldendb.execute.mysql.GoldenDBMySQLSession;
import com.clougence.clouddm.ds.goldendb.execute.oracle.GoldenDBOracleSession;
import com.clougence.clouddm.dsfamily.execute.RdbSessionFactory;
import com.clougence.clouddm.sdk.execute.resource.DsResourceManager;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.rdb.DefaultRdbSession;
import com.clougence.drivers.DsObject;

public class GoldenDBSessionFactory extends RdbSessionFactory<AbstractGoldenDBConfig> {

    @Override
    protected Session newSession(AbstractGoldenDBConfig dsConfig, SessionContextDTO contextDTO, DsObject<Connection> dsObject, DsResourceManager ownerRM) throws Exception {
        DefaultRdbSession session;
        switch (dsConfig.getDataSourceType()) {
            case GoldenDBMySQL:
                session = new GoldenDBMySQLSession(contextDTO.getSessionId(), dsConfig, dsObject);
                break;
            case GoldenDBOracle:
                session = new GoldenDBOracleSession(contextDTO.getSessionId(), dsConfig, dsObject);
                break;
            default:
                throw new IllegalArgumentException("Unsupported GoldenDB data source type: " + dsConfig.getDataSourceType());
        }
        session.initSession(ownerRM, contextDTO);
        return session;
    }
}
