package com.clougence.clouddm.ds.redis.execute.jdbc;

import java.sql.SQLException;
import java.util.List;

import com.clougence.clouddm.ds.redis.parser.ast.commands.pf.PFAddRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.commands.pf.PFCountRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.commands.pf.PFMergeRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.drivers.adapter.AdapterReceive;
import com.clougence.drivers.adapter.AdapterRequest;
import com.clougence.utils.future.CgFuture;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class JedisCallForHyperLog extends JedisUtils {

    public static CgFuture<?> cmdPFAddRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, PFAddRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        List<StrToken> elementList = command.getElement();
        String[] ele = new String[elementList.size()];
        for (int i = 0; i < elementList.size(); i++) {
            ele[i] = argAsString(request, elementList.get(i));
        }

        long result = jedisCmd.getHyperLogLogCommands().pfadd(key, ele);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdPFCountRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, PFCountRedisCmd command, AdapterRequest request,
                                                 AdapterReceive receive) throws SQLException {
        List<StrToken> keyList = command.getKeys();
        String[] keys = new String[keyList.size()];
        for (int i = 0; i < keyList.size(); i++) {
            keys[i] = argAsString(request, keyList.get(i));
        }

        long result = jedisCmd.getHyperLogLogCommands().pfcount(keys);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdPFMergeRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, PFMergeRedisCmd command, AdapterRequest request,
                                                 AdapterReceive receive) throws SQLException {
        String dstKey = argAsString(request, command.getDst());

        List<StrToken> srcKeys = command.getSrcKey();
        String[] keys = new String[srcKeys.size()];
        for (int i = 0; i < srcKeys.size(); i++) {
            keys[i] = argAsString(request, srcKeys.get(i));
        }

        String result = jedisCmd.getHyperLogLogCommands().pfmerge(dstKey, keys);

        receive.responseResult(request, singleResult(request, COL_RESULT_STRING, result));
        return completed(sync);
    }
}
