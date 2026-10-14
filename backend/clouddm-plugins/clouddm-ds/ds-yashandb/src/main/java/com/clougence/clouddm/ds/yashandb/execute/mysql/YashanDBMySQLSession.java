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
package com.clougence.clouddm.ds.yashandb.execute.mysql;

import java.sql.Connection;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.dsfamily.mysql.execute.MySession;
import com.clougence.drivers.DsObject;

/**
 * Session for the YashanDB MySQL compatibility mode.
 * <p>
 * Reuses {@link MySession} directly: this variant connects to the MySQL protocol port of YashanDB with MySQL
 * Connector/J, so session behaviour (including the parent class {@code killCurrentQuery()} cast to
 * {@code com.mysql.cj.jdbc.ConnectionImpl}) is exactly the same as standard MySQL and needs no override.
 * </p>
 * <p>
 * For comparison: the MySQL variant of KingbaseES uses Kingbase's own driver and therefore has to override
 * {@code killCurrentQuery()} with {@code unwrap(BaseConnection.class)}; YashanDB would face the same problem if
 * it switched to {@code jdbc:yasdb://...?compatVector=mysql} (see the notes in {@code YashanDBMySQLDsFactory}),
 * which is one of the direct reasons this variant chooses the standard MySQL driver.
 * </p>
 *
 * @author open-cdm
 */
public class YashanDBMySQLSession extends MySession {

    public YashanDBMySQLSession(String newSessionId, DataSourceConfig dsConfig, DsObject<Connection> dsObject){
        super(newSessionId, dsConfig, dsObject, new YashanDBMySQLHooks(dsConfig.getVersion()));
    }
}
