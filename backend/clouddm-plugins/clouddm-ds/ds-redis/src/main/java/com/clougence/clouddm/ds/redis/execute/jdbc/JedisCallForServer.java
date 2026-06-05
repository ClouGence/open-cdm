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
package com.clougence.clouddm.ds.redis.execute.jdbc;

import java.io.StringReader;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.redis.parser.ast.commands.client.PingRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.commands.control.DbSizeRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.commands.info.InfoRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.commands.keys.WaitAOFRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.commands.keys.WaitRedisCmd;
import com.clougence.drivers.adapter.*;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.future.CgFuture;
import com.clougence.utils.io.IOUtils;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.util.KeyValue;

@Slf4j
class JedisCallForServer extends JedisUtils {

    public static CgFuture<?> cmdInfoRedisCmd(CgFuture<Object> sync, JedisCmd jedis, InfoRedisCmd redisCmd, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String infoValue;
        if (CollectionUtils.isNotEmpty(redisCmd.getTypes())) {
            List<InfoRedisCmd.InfoType> infoOptToken = redisCmd.getTypes();
            StringBuilder sections = new StringBuilder();
            for (int i = 0; i < infoOptToken.size(); i++) {
                sections.append(infoOptToken.get(i).name().toUpperCase());
                if (i != infoOptToken.size() - 1) {
                    sections.append(" ");
                }
            }

            infoValue = jedis.getServerCommands().info(sections.toString());
        } else {
            infoValue = jedis.getServerCommands().info();
        }

        List<String> strings;
        try {
            strings = IOUtils.readLines(new StringReader(infoValue));
        } catch (Exception e) {
            return failed(sync, e);
        }

        String groupName = "INFO";
        AdapterResultCursor receiveCur = new AdapterResultCursor(request, Arrays.asList(COL_GROUP_STRING, COL_NAME_STRING, COL_VALUE_STRING));
        for (String line : strings) {
            if (StringUtils.isBlank(line)) {
                continue;
            }

            if (line.startsWith("#")) {
                groupName = line.replaceFirst("#\\s*", "").trim();
                continue;
            }

            int i = line.indexOf(":");
            if (i < 0) {
                continue;
            }

            receiveCur.pushData(CollectionUtils.asMap(          //
                    COL_GROUP_STRING.name, groupName,           //
                    COL_NAME_STRING.name, line.substring(0, i), //
                    COL_VALUE_STRING.name, line.substring(i + 1)//
            ));
        }
        receiveCur.pushFinish();
        receive.responseResult(request, receiveCur);
        return completed(sync);
    }

    public static CgFuture<?> cmdPingRedisCmd(CgFuture<Object> sync, JedisCmd jedis, PingRedisCmd redisCmd, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String pone;
        if (redisCmd.getMessage() != null) {
            String message = argAsString(request, redisCmd.getMessage());
            pone = jedis.getServerCommands().ping(message);
        } else {
            pone = jedis.getServerCommands().ping();
        }

        JdbcColumn COL_PONG_STRING = new JdbcColumn("PONG", AdapterType.String, "", "", "");
        receive.responseResult(request, singleResult(request, COL_PONG_STRING, pone));
        return completed(sync);
    }

    public static CgFuture<?> cmdWaitRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, WaitRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        long timeout = argAsLong(request, command.getTimeout());
        int numReplicas = argAsInt(request, command.getNumReplicas());

        long result = jedisCmd.getServerCommands().waitReplicas(numReplicas, timeout);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdWaitAOFRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, WaitAOFRedisCmd command, AdapterRequest request,
                                                 AdapterReceive receive) throws SQLException {
        long numLocal = argAsLong(request, command.getNumLocal());
        long numReplicas = argAsLong(request, command.getNumReplicas());
        long timeout = argAsLong(request, command.getTimeout());

        KeyValue<Long, Long> result = jedisCmd.getServerCommands().waitAOF(numLocal, numReplicas, timeout);
        Map<?, ?> data = CollectionUtils.asMap("local", result.getKey(), "replicas", result.getValue());

        receive.responseResult(request, listResult(request, COL_LOCAL_LONG, COL_REPLICAS_LONG, data));
        return completed(sync);
    }

    public static CgFuture<?> cmdDbSizeRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, DbSizeRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        long dbSize = jedisCmd.getDatabaseCommands().dbSize();

        receive.responseResult(request, singleResult(request, COL_COUNT_LONG, dbSize));
        return completed(sync);
    }
}
