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
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.redis.parser.ast.commands.config.ConfigGetCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.drivers.adapter.AdapterReceive;
import com.clougence.drivers.adapter.AdapterRequest;
import com.clougence.utils.future.CgFuture;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class JedisCallForConfig extends JedisUtils {

    public static CgFuture<?> cmdConfigGetCmd(CgFuture<Object> sync, JedisCmd jedis, ConfigGetCmd redisCmd, AdapterRequest request, AdapterReceive receive) throws SQLException {
        List<StrToken> patternsToken = redisCmd.getPatterns();
        String[] keys = new String[patternsToken.size()];
        for (int i = 0; i < patternsToken.size(); i++) {
            keys[i] = argAsString(request, patternsToken.get(i));
        }

        Map<String, String> type = jedis.getConfigCommands().configGet(keys);

        receive.responseResult(request, listResult(request, COL_NAME_STRING, COL_VALUE_STRING, type));
        return completed(sync);
    }

}
