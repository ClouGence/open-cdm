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
