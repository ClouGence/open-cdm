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
package com.clougence.clouddm.ds.kafka.execute;

import java.sql.Connection;

import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.rdb.DefaultRdbMetaService;
import com.clougence.clouddm.sdk.execute.session.rdb.DmRdbUmiService;
import com.clougence.drivers.adapter.AdapterConnection;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.utils.ExceptionUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KafkaMetaService extends DefaultRdbMetaService {

    public KafkaMetaService(Session rdbSession){
        super(rdbSession);
    }

    @Override
    protected DmRdbUmiService rdbUmiService(Connection con) {
        return new KafkaUmiServiceDm(con);
    }

    @Override
    protected SqlBuilder getSqlBuilder() { return null; }

    @Override
    public String getCurrentCatalog() { return null; }

    @Override
    public String getCurrentSchema() {
        try {
            return this.rdbSession.executeQuery(con -> {
                AdapterConnection adapterConn = con.unwrap(AdapterConnection.class);
                if (adapterConn == null) {
                    throw new java.sql.SQLException("failed to unwrap AdapterConnection from " + con.getClass().getName());
                }
                return adapterConn.getSchema();
            });
        } catch (Exception e) {
            String msg = "getCurrentSchema failed, " + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public void testConnect() {
        try {
            this.rdbSession.executeQuery(con -> {
                new KafkaMetaProviderDm(con).testConnect();
                return "OK.";
            });
        } catch (Exception e) {
            String msg = "testConnect failed, " + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }
}
