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
package com.clougence.clouddm.ds.goldendb.execute.oracle;

import java.sql.Connection;
import java.sql.SQLException;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.dsfamily.oracle.execute.OraHooks;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.result.ColReader;
import com.clougence.utils.StringUtils;

public class GoldenDBOracleHooks extends OraHooks {

    public GoldenDBOracleHooks(DataSourceConfig config){
        super(config);
    }

    @Override
    public DsMetaService createMetaService(Session session) {
        return new GoldenDBOracleMetaService(session);
    }

    @Override
    public ColReader createColReader() {
        return new GoldenDBOracleColReader();
    }

    @Override
    public void setCurrentSchema(Connection connection, String schemaName) throws SQLException {
        if (StringUtils.isBlank(schemaName)) {
            return;
        }
        connection.setCatalog(schemaName);
    }
}
