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
package com.clougence.clouddm.ds.kingbasees.execute.postgresql;

import java.sql.Connection;
import java.util.Map;

import com.clougence.clouddm.ds.kingbasees.dsconf.KingbaseESCompatibilityMode;
import com.clougence.clouddm.ds.kingbasees.execute.KingbaseESMetaQueryUtils;
import com.clougence.clouddm.dsfamily.postgres.execute.PgMetaService;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.rdb.DmRdbUmiService;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;

public class KingbaseESPostgreSQLMetaService extends PgMetaService {

    public KingbaseESPostgreSQLMetaService(Session session){
        super(session);
    }

    @Override
    protected DmRdbUmiService rdbUmiService(Connection connection) {
        return new KingbaseESPostgreSQLUmiServiceDm(connection);
    }

    @Override
    public String getCurrentCatalog() { return KingbaseESMetaQueryUtils.query(rdbSession, "SELECT current_database()", "getCurrentCatalog"); }

    @Override
    public String getCurrentSchema() { return KingbaseESMetaQueryUtils.query(rdbSession, "SELECT current_schema()", "getCurrentSchema"); }

    @Override
    public Map<String, String> getSqlParserParameters() {
        if (!(rdbSession instanceof KingbaseESPostgreSQLSession session) || session.getCompatibility().compatibilityMode() != KingbaseESCompatibilityMode.POSTGRESQL) {
            return Map.of();
        }
        return Map.of(SqlParserParameters.VERSION, session.getCompatibility().pgGrammarMajorVersion());
    }
}
