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
package com.clougence.clouddm.ds.yashandb.execute.oracle;

import java.sql.Connection;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.dsfamily.oracle.execute.OraSession;
import com.clougence.drivers.DsObject;

/**
 * Session for YashanDB.
 * <p>
 * YashanDB is an Oracle-compatible database, so it reuses the statement handling and Explain capabilities of
 * {@link OraSession} and only replaces the YashanDB-specific metadata behaviour through
 * {@link YashanDBOracleHooks}.
 *
 * @author open-cdm
 */
public class YashanDBOracleSession extends OraSession {

    public YashanDBOracleSession(String newSessionId, DataSourceConfig dsConfig, DsObject<Connection> dsObject){
        super(newSessionId, dsConfig, dsObject, new YashanDBOracleHooks(dsConfig));
    }
}
