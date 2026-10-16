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
package com.clougence.clouddm.ds.kingbasees.execute;

import java.sql.Connection;

import com.clougence.clouddm.ds.kingbasees.dsconf.AbstractKingbaseESConfig;
import com.clougence.clouddm.ds.kingbasees.execute.dsfactory.KingbaseESDsObject;
import com.clougence.clouddm.ds.kingbasees.execute.mysql.KingbaseESMySQLSession;
import com.clougence.clouddm.ds.kingbasees.execute.oracle.KingbaseESOracleSession;
import com.clougence.clouddm.ds.kingbasees.execute.postgresql.KingbaseESPostgreSQLSession;
import com.clougence.clouddm.ds.kingbasees.execute.sqlserver.KingbaseESSQLServerSession;
import com.clougence.clouddm.dsfamily.execute.RdbSessionFactory;
import com.clougence.clouddm.sdk.execute.resource.DsResourceManager;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.rdb.DefaultRdbSession;
import com.clougence.drivers.DsObject;

public class KingbaseESSessionFactory extends RdbSessionFactory<AbstractKingbaseESConfig> {

    @Override
    protected Session newSession(AbstractKingbaseESConfig dsConfig, SessionContextDTO contextDTO, DsObject<Connection> dsObject, DsResourceManager ownerRM) throws Exception {
        if (!(dsObject instanceof KingbaseESDsObject kingbaseESDsObject)) {
            throw new IllegalArgumentException("KingbaseES session requires KingbaseESDsObject.");
        }
        DefaultRdbSession session;
        switch (dsConfig.getDataSourceType()) {
            case KingbaseESPostgreSQL:
                session = new KingbaseESPostgreSQLSession(contextDTO.getSessionId(), dsConfig, kingbaseESDsObject, kingbaseESDsObject.getCompatibility());
                break;
            case KingbaseESMySQL:
                session = new KingbaseESMySQLSession(contextDTO.getSessionId(), dsConfig, kingbaseESDsObject);
                break;
            case KingbaseESOracle:
                session = new KingbaseESOracleSession(contextDTO.getSessionId(), dsConfig, kingbaseESDsObject);
                break;
            case KingbaseESSQLServer:
                session = new KingbaseESSQLServerSession(contextDTO.getSessionId(), dsConfig, kingbaseESDsObject);
                break;
            default:
                throw new IllegalArgumentException("Unsupported KingbaseES data source type: " + dsConfig.getDataSourceType());
        }
        session.initSession(ownerRM, contextDTO);
        return session;
    }
}
