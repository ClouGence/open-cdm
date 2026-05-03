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
