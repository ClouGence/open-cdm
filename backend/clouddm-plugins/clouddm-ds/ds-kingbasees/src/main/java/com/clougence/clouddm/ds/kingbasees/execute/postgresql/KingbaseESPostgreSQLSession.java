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

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.ds.kingbasees.execute.KingbaseESHooks;
import com.clougence.clouddm.ds.kingbasees.execute.dsfactory.KingbaseESCompatibility;
import com.clougence.clouddm.ds.kingbasees.execute.dsfactory.KingbaseESDsObject;
import com.clougence.clouddm.dsfamily.postgres.execute.PgSession;

public class KingbaseESPostgreSQLSession extends PgSession {

    private final KingbaseESCompatibility compatibility;

    public KingbaseESPostgreSQLSession(String sessionId, DataSourceConfig dsConfig, KingbaseESDsObject dsObject, KingbaseESCompatibility compatibility){
        super(sessionId, dsConfig, dsObject, new KingbaseESHooks());
        this.compatibility = compatibility;
    }

    public KingbaseESCompatibility getCompatibility() { return compatibility; }
}
