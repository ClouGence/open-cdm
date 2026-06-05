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
package com.clougence.clouddm.ds.redis.execute;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.clougence.clouddm.ds.redis.definition.ui.editor.keys.RedisEditorProvider;
import com.clougence.clouddm.ds.redis.execute.jdbc.JedisConnection;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.rdb.DefaultRdbMetaService;
import com.clougence.clouddm.sdk.execute.session.rdb.DmRdbUmiService;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2021/1/15 17:11
 */
@Slf4j
public class RedisMetaService extends DefaultRdbMetaService {

    public RedisMetaService(Session rdbSession){
        super(rdbSession);
    }

    @Override
    protected DmRdbUmiService rdbUmiService(Connection con) {
        return new RedisUmiServiceDm(con);
    }

    @Override
    protected SqlBuilder getSqlBuilder() { return RedisEditorProvider.INSTANCE; }

    public String getCurrentCatalog() { return null; }

    @Override
    public String getCurrentSchema() {
        try {
            return this.rdbSession.executeQuery(con -> con.unwrap(JedisConnection.class).getSchema());
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
                try (PreparedStatement ps = con.prepareStatement("ping ?;")) {
                    String req = UUID.randomUUID().toString();
                    ps.setString(1, req);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            if (StringUtils.equals(req, rs.getString("PONG"))) {
                                return "OK.";
                            }
                        }
                        throw new SQLException("redis ping test failed.");
                    }
                }
            });
        } catch (Exception e) {
            String msg = "testConnect failed, " + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }
}
