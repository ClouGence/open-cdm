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

import java.sql.SQLException;

import com.clougence.clouddm.ds.redis.parser.ast.commands.keys.CopyRedisCmd;
import com.clougence.drivers.adapter.AdapterReceive;
import com.clougence.drivers.adapter.AdapterRequest;
import com.clougence.utils.future.CgFuture;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class JedisCallForDB extends JedisUtils {

    public static CgFuture<?> cmdCopyRedisCmd(CgFuture<Object> sync, JedisCmd jedis, CopyRedisCmd redisCmd, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String oldKeyName = argAsString(request, redisCmd.getSrcKey());
        String newKeyName = argAsString(request, redisCmd.getDstKey());
        boolean replace = redisCmd.getReplace() != null;

        boolean result;
        if (redisCmd.getDstDB() != null) {
            int destDataBase = argAsInt(request, redisCmd.getDstDB());
            result = jedis.getDatabaseCommands().copy(oldKeyName, newKeyName, destDataBase, replace);
        } else {
            result = jedis.getJedisCommands().copy(oldKeyName, newKeyName, replace);
        }

        receive.responseResult(request, singleResult(request, COL_VALUE_BOOLEAN, result));
        return completed(sync);
    }
}
