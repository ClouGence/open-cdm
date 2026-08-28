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
package com.clougence.clouddm.ds.kingbasees.execute.mysql;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.ds.kingbasees.execute.dsfactory.KingbaseESDsObject;
import com.clougence.clouddm.dsfamily.mysql.execute.MySession;
import com.kingbase8.core.BaseConnection;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KingbaseESMySQLSession extends MySession {

    public KingbaseESMySQLSession(String sessionId, DataSourceConfig dsConfig, KingbaseESDsObject dsObject){
        super(sessionId, dsConfig, dsObject, new KingbaseESMySQLHooks(dsConfig.getVersion()));
    }

    @Override
    public void killCurrentQuery() {
        try {
            currentResource().unwrap(BaseConnection.class).cancelQuery();
        } catch (Exception e) {
            log.error("session " + this.getSessionId() + " killCurrentQuery failed, " + e.getMessage());
        } finally {
            super.markExecutingFinish();
            this.doClose();
        }
    }
}
