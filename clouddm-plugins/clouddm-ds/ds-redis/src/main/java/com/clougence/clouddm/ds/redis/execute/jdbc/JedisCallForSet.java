package com.clougence.clouddm.ds.redis.execute.jdbc;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.clougence.clouddm.ds.redis.parser.ast.commands.set.*;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.drivers.adapter.AdapterReceive;
import com.clougence.drivers.adapter.AdapterRequest;
import com.clougence.drivers.adapter.AdapterResultCursor;
import com.clougence.utils.future.CgFuture;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;

@Slf4j
class JedisCallForSet extends JedisUtils {

    public static CgFuture<?> cmdSAddRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, SAddRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        List<StrToken> elementList = command.getMembers();
        String[] member = new String[elementList.size()];
        for (int i = 0; i < elementList.size(); i++) {
            member[i] = argAsString(request, elementList.get(i));
        }
        long result = jedisCmd.getSetCommands().sadd(key, member);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdSCardRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, SCardRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        long result = jedisCmd.getSetCommands().scard(key);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdSDiffRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, SDiffRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        List<StrToken> contextList = command.getKeyNames();
        String[] keys = new String[contextList.size()];
        for (int i = 0; i < contextList.size(); i++) {
            keys[i] = argAsString(request, contextList.get(i));
        }

        Set<String> result = jedisCmd.getSetCommands().sdiff(keys);

        receive.responseResult(request, listResult(request, COL_ELEMENT_STRING, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdSDiffStoreRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, SDiffStoreRedisCmd command, AdapterRequest request,
                                                    AdapterReceive receive) throws SQLException {
        String dst = argAsString(request, command.getDstKey());

        List<StrToken> nameContexts = command.getKeys();
        String[] keys = new String[nameContexts.size()];
        for (int i = 0; i < nameContexts.size(); i++) {
            keys[i] = argAsString(request, nameContexts.get(i));
        }

        long result = jedisCmd.getSetCommands().sdiffstore(dst, keys);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdSInterRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, SInterRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        List<StrToken> nameContexts = command.getKeyNames();
        String[] keys = new String[nameContexts.size()];
        for (int i = 0; i < nameContexts.size(); i++) {
            keys[i] = argAsString(request, nameContexts.get(i));
        }

        Set<String> result = jedisCmd.getSetCommands().sinter(keys);

        receive.responseResult(request, listResult(request, COL_ELEMENT_STRING, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdSInterCardRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, SInterCardRedisCmd command, AdapterRequest request,
                                                    AdapterReceive receive) throws SQLException {
        int numKeys = argAsInt(request, command.getNumKeys());
        List<StrToken> kvContexts = command.getKeys();
        String[] keys = new String[kvContexts.size()];
        for (int i = 0; i < kvContexts.size(); i++) {
            keys[i] = argAsString(request, kvContexts.get(i));
        }
        numKeysCheck("SINTERCARD", keys.length, numKeys);

        Set<String> result = jedisCmd.getSetCommands().sinter(keys);

        receive.responseResult(request, listResult(request, COL_ELEMENT_STRING, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdSinterStoreRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, SinterStoreRedisCmd command, AdapterRequest request,
                                                     AdapterReceive receive) throws SQLException {
        String dstKey = argAsString(request, command.getDst());

        List<StrToken> kvContexts = command.getKeys();
        String[] keys = new String[kvContexts.size()];
        for (int i = 0; i < kvContexts.size(); i++) {
            keys[i] = argAsString(request, kvContexts.get(i));
        }

        long result = jedisCmd.getSetCommands().sinterstore(dstKey, keys);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdSISMemberRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, SISMemberRedisCmd command, AdapterRequest request,
                                                   AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String member = argAsString(request, command.getMember());

        boolean result = jedisCmd.getSetCommands().sismember(key, member);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result ? 1 : 0));
        return completed(sync);
    }

    public static CgFuture<?> cmdSMembersRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, SMembersRedisCmd command, AdapterRequest request,
                                                  AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        Set<String> result = jedisCmd.getSetCommands().smembers(key);

        receive.responseResult(request, listResult(request, COL_ELEMENT_STRING, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdSMISMemberRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, SMISMemberRedisCmd command, AdapterRequest request,
                                                    AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        List<StrToken> kvContexts = command.getMembers();
        String[] member = new String[kvContexts.size()];
        for (int i = 0; i < kvContexts.size(); i++) {
            member[i] = argAsString(request, kvContexts.get(i));
        }

        List<Boolean> result = jedisCmd.getSetCommands().smismember(key, member);

        receive.responseResult(request, listResult(request, COL_RESULT_BOOLEAN, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdSMoveRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, SMoveRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        String srcKey = argAsString(request, command.getSrc());
        String dstKey = argAsString(request, command.getDst());
        String member = argAsString(request, command.getMember());

        long result = jedisCmd.getSetCommands().smove(srcKey, dstKey, member);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdSPopRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, SPopRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        Long count = null;
        if (command.getCount() != null) {
            count = argAsLong(request, command.getCount());
        }

        Collection<String> result;
        if (count != null) {
            result = jedisCmd.getSetCommands().spop(key, count);
        } else {
            result = Collections.singletonList(jedisCmd.getSetCommands().spop(key));
        }

        receive.responseResult(request, listResult(request, COL_ELEMENT_STRING, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdSRandMemberRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, SRandMemberRedisCmd command, AdapterRequest request,
                                                     AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        Integer count = null;
        if (command.getCount() != null) {
            count = argAsInteger(request, command.getCount());
        }

        Collection<String> result;
        if (count != null) {
            result = jedisCmd.getSetCommands().srandmember(key, count);
        } else {
            result = Collections.singletonList(jedisCmd.getSetCommands().srandmember(key));
        }

        receive.responseResult(request, listResult(request, COL_ELEMENT_STRING, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdSRemRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, SRemRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        List<StrToken> kvContexts = command.getMembers();
        String[] member = new String[kvContexts.size()];
        for (int i = 0; i < kvContexts.size(); i++) {
            member[i] = argAsString(request, kvContexts.get(i));
        }

        long result = jedisCmd.getSetCommands().srem(key, member);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdSSCanRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, SSCanRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String cursor = argAsString(request, command.getCursor());
        String pattern = null;
        if (command.getPattern() != null) {
            pattern = argAsString(request, command.getPattern());
        }
        Integer count = null;
        if (command.getCount() != null) {
            count = argAsInteger(request, command.getCount());
        }

        ScanParams scanParams = new ScanParams();
        if (pattern != null) {
            scanParams.match(pattern);
        }
        if (count != null) {
            scanParams.count(count);
        }

        ScanResult<String> result = jedisCmd.getSetCommands().sscan(key, cursor, scanParams);

        if (!sync.isDone()) {
            AdapterResultCursor receiveCur = listFixedColAndResult(request, COL_CURSOR_STRING, result.getCursor(), COL_ELEMENT_STRING, result.getResult());
            receive.responseResult(request, receiveCur);
            return completed(sync);
        } else {
            SQLException err = new SQLException("command interrupted.");
            receive.responseFailed(request, err);
            throw err;
        }
    }

    public static CgFuture<?> cmdSUnionRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, SUnionRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        List<StrToken> kvContexts = command.getKeyNames();
        String[] keys = new String[kvContexts.size()];
        for (int i = 0; i < kvContexts.size(); i++) {
            keys[i] = argAsString(request, kvContexts.get(i));
        }

        Set<String> result = jedisCmd.getSetCommands().sunion(keys);

        receive.responseResult(request, listResult(request, COL_ELEMENT_STRING, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdSUnionStoreRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, SUnionStoreRedisCmd command, AdapterRequest request,
                                                     AdapterReceive receive) throws SQLException {
        String dstKey = argAsString(request, command.getDst());
        List<StrToken> kvContexts = command.getKeys();
        String[] keys = new String[kvContexts.size()];
        for (int i = 0; i < kvContexts.size(); i++) {
            keys[i] = argAsString(request, kvContexts.get(i));
        }

        long result = jedisCmd.getSetCommands().sunionstore(dstKey, keys);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }
}
