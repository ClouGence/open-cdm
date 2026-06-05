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
import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.ds.redis.parser.ast.commands.list.*;
import com.clougence.clouddm.ds.redis.parser.ast.token.Direction;
import com.clougence.clouddm.ds.redis.parser.ast.token.Position;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.drivers.adapter.AdapterReceive;
import com.clougence.drivers.adapter.AdapterRequest;
import com.clougence.drivers.adapter.AdapterResultCursor;
import com.clougence.utils.future.CgFuture;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.args.ListDirection;
import redis.clients.jedis.args.ListPosition;
import redis.clients.jedis.params.LPosParams;
import redis.clients.jedis.util.KeyValue;

@Slf4j
class JedisCallForList extends JedisUtils {

    private static ListDirection convertToDirection(Direction direction) {
        switch (direction) {
            case LEFT:
                return ListDirection.LEFT;
            case RIGHT:
                return ListDirection.RIGHT;
            default:
                throw new IllegalArgumentException("Unsupported direction: " + direction);
        }
    }

    private static ListPosition convertToListPosition(Position position) {
        switch (position) {
            case BEFORE:
                return ListPosition.BEFORE;
            case AFTER:
                return ListPosition.AFTER;
            default:
                throw new IllegalArgumentException("Unsupported position: " + position);
        }
    }

    //

    public static CgFuture<?> cmdBLMoveRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, BLMoveRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String srcKey = argAsString(request, command.getSrc());
        String dstKey = argAsString(request, command.getDst());
        ListDirection from = convertToDirection(command.getSrcDirection());
        ListDirection to = convertToDirection(command.getDstDirection());
        double timeout = argAsDouble(request, command.getTimeout());

        String item = jedisCmd.getListCommands().blmove(srcKey, dstKey, from, to, timeout);

        receive.responseResult(request, singleResult(request, COL_ELEMENT_STRING, item));
        return completed(sync);
    }

    public static CgFuture<?> cmdBLMPopRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, BLMPopRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        int timeout = argAsInt(request, command.getTimeout());
        int numKeys = argAsInt(request, command.getNumKeys());

        List<StrToken> kvContexts = command.getKeyNames();
        String[] keyValues = new String[kvContexts.size()];
        for (int i = 0; i < kvContexts.size(); i++) {
            keyValues[i] = argAsString(request, kvContexts.get(i));
        }
        numKeysCheck("BLMPOP", keyValues.length, numKeys);

        ListDirection lr = convertToDirection(command.getDirection());

        KeyValue<String, List<String>> values;
        if (command.getCount() != null) {
            int count = argAsInt(request, command.getCount());
            values = jedisCmd.getListCommands().blmpop(timeout, lr, count, keyValues);
        } else {
            values = jedisCmd.getListCommands().blmpop(timeout, lr, keyValues);
        }

        AdapterResultCursor receiveCur = listFixedColAndResult(request, COL_KEY_STRING, values.getKey(), COL_ELEMENT_STRING, values.getValue());
        receive.responseResult(request, receiveCur);
        return completed(sync);
    }

    public static CgFuture<?> cmdBLPopRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, BLPopRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        List<StrToken> kvContexts = command.getKeyNames();
        String[] keys = new String[kvContexts.size()];
        for (int i = 0; i < kvContexts.size(); i++) {
            keys[i] = argAsString(request, kvContexts.get(i));
        }
        int timeout = argAsInt(request, command.getTimeout());

        List<String> result = jedisCmd.getListCommands().blpop(timeout, keys);

        receive.responseResult(request, listResult(request, COL_ELEMENT_STRING, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdBRPopRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, BRPopRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        List<StrToken> kvContexts = command.getKeyNames();
        String[] keys = new String[kvContexts.size()];
        for (int i = 0; i < kvContexts.size(); i++) {
            keys[i] = argAsString(request, kvContexts.get(i));
        }
        int timeout = argAsInt(request, command.getTimeout());

        List<String> result = jedisCmd.getListCommands().brpop(timeout, keys);

        receive.responseResult(request, listResult(request, COL_ELEMENT_STRING, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdBRPopLPushRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, BRPopLPushRedisCmd command, AdapterRequest request,
                                                    AdapterReceive receive) throws SQLException {
        String srcKey = argAsString(request, command.getSrcKey());
        String dstKey = argAsString(request, command.getDstKEy());
        int timeout = argAsInt(request, command.getTimeout());

        String result = jedisCmd.getListCommands().brpoplpush(srcKey, dstKey, timeout);

        receive.responseResult(request, singleResult(request, COL_ELEMENT_STRING, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdLIndexRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, LIndexRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        int index = argAsInt(request, command.getIndex());

        String result = jedisCmd.getListCommands().lindex(key, index);

        receive.responseResult(request, singleResult(request, COL_ELEMENT_STRING, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdLInsertRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, LInsertRedisCmd command, AdapterRequest request,
                                                 AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String pivot = argAsString(request, command.getPivot());
        String ele = argAsString(request, command.getKeyName());
        ListPosition where = convertToListPosition(command.getPosition());

        long result = jedisCmd.getListCommands().linsert(key, where, pivot, ele);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdLLenRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, LLenRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        long result = jedisCmd.getListCommands().llen(key);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdLMoveRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, LMoveRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        String srcKey = argAsString(request, command.getSrc());
        String dstKey = argAsString(request, command.getDst());
        ListDirection from = convertToDirection(command.getSrcDirection());
        ListDirection to = convertToDirection(command.getDstDirection());

        String item = jedisCmd.getListCommands().lmove(srcKey, dstKey, from, to);

        receive.responseResult(request, singleResult(request, COL_ELEMENT_STRING, item));
        return completed(sync);
    }

    public static CgFuture<?> cmdLMPopRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, LMPopRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        int numKeys = argAsInt(request, command.getNumKeys());

        List<StrToken> kvContexts = command.getKeyNames();
        String[] keyValues = new String[kvContexts.size()];
        for (int i = 0; i < kvContexts.size(); i++) {
            keyValues[i] = argAsString(request, kvContexts.get(i));
        }
        numKeysCheck("LMPOP", keyValues.length, numKeys);

        ListDirection lr = convertToDirection(command.getDirection());

        KeyValue<String, List<String>> values;
        if (command.getCount() != null) {
            int count = argAsInt(request, command.getCount());
            values = jedisCmd.getListCommands().lmpop(lr, count, keyValues);
        } else {
            values = jedisCmd.getListCommands().lmpop(lr, keyValues);
        }

        AdapterResultCursor receiveCur = listFixedColAndResult(request, COL_KEY_STRING, values.getKey(), COL_ELEMENT_STRING, values.getValue());
        receive.responseResult(request, receiveCur);
        return completed(sync);
    }

    public static CgFuture<?> cmdLPopRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, LPopRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String keyStr = argAsString(request, command.getKeyName());

        List<String> result;
        if (command.getCount() != null) {
            int count = argAsInt(request, command.getCount());
            result = jedisCmd.getListCommands().lpop(keyStr, count);
        } else {
            result = Collections.singletonList(jedisCmd.getListCommands().lpop(keyStr));
        }

        receive.responseResult(request, listResult(request, COL_ELEMENT_STRING, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdLPosRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, LPosRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String element = argAsString(request, command.getElement());
        LPosParams params = null;
        Long count = null;

        if (command.getRank() != null) {
            params = new LPosParams();
            int rank = argAsInt(request, command.getRank());
            params.rank(rank);
        }

        if (command.getCount() != null) {
            count = argAsLong(request, command.getCount());
        }

        if (command.getMaxLen() != null) {
            params = params == null ? new LPosParams() : params;
            int maxLen = argAsInt(request, command.getMaxLen());
            params.maxlen(maxLen);
        }

        if (params == null) {
            long result = jedisCmd.getListCommands().lpos(key, element);
            receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        } else if (count == null) {
            long result = jedisCmd.getListCommands().lpos(key, element, params);
            receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        } else {
            List<Long> result = jedisCmd.getListCommands().lpos(key, element, params, count);
            receive.responseResult(request, listResult(request, COL_RESULT_LONG, result));
        }

        return completed(sync);
    }

    public static CgFuture<?> cmdLPushRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, LPushRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        List<StrToken> eleList = command.getElements();
        String[] elements = new String[eleList.size()];
        for (int i = 0; i < eleList.size(); i++) {
            elements[i] = argAsString(request, eleList.get(i));
        }

        long result = jedisCmd.getListCommands().lpush(key, elements);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdLPushXRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, LPushXRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        List<StrToken> eleList = command.getElements();
        String[] elements = new String[eleList.size()];
        for (int i = 0; i < eleList.size(); i++) {
            elements[i] = argAsString(request, eleList.get(i));
        }

        long result = jedisCmd.getListCommands().lpushx(key, elements);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdLRangeRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, LRangeRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        long begin = argAsLong(request, command.getStart());
        long end = argAsLong(request, command.getStop());

        List<String> result = jedisCmd.getListCommands().lrange(key, begin, end);

        receive.responseResult(request, listResult(request, COL_ELEMENT_STRING, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdLRemRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, LRemRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        long count = argAsLong(request, command.getCount());
        String ele = argAsString(request, command.getElement());

        long result = jedisCmd.getListCommands().lrem(key, count, ele);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdLSetRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, LSetRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        long index = argAsLong(request, command.getIndex());
        String ele = argAsString(request, command.getElement());

        String result = jedisCmd.getListCommands().lset(key, index, ele);

        receive.responseResult(request, singleResult(request, COL_RESULT_STRING, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdLTrimRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, LTrimRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        long begin = argAsLong(request, command.getStartNum());
        long end = argAsLong(request, command.getStopNum());

        String result = jedisCmd.getListCommands().ltrim(key, begin, end);

        receive.responseResult(request, singleResult(request, COL_RESULT_STRING, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdRPopRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, RPopRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        List<String> result;
        if (command.getCount() != null) {
            int count = argAsInt(request, command.getCount());
            result = jedisCmd.getListCommands().rpop(key, count);
        } else {
            result = Collections.singletonList(jedisCmd.getListCommands().rpop(key));
        }

        receive.responseResult(request, listResult(request, COL_ELEMENT_STRING, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdRPopLPushRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, RPopLPushRedisCmd command, AdapterRequest request,
                                                   AdapterReceive receive) throws SQLException {
        String srcKey = argAsString(request, command.getSrcKey());
        String dstKey = argAsString(request, command.getDstKey());

        String result = jedisCmd.getListCommands().rpoplpush(srcKey, dstKey);

        receive.responseResult(request, singleResult(request, COL_ELEMENT_STRING, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdRPushRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, RPushRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        List<StrToken> eleList = command.getElements();
        String[] elements = new String[eleList.size()];
        for (int i = 0; i < eleList.size(); i++) {
            elements[i] = argAsString(request, eleList.get(i));
        }

        long result = jedisCmd.getListCommands().rpush(key, elements);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdRPushXRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, RPushXRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        List<StrToken> eleList = command.getElements();
        String[] elements = new String[eleList.size()];
        for (int i = 0; i < eleList.size(); i++) {
            elements[i] = argAsString(request, eleList.get(i));
        }

        long result = jedisCmd.getListCommands().rpushx(key, elements);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }
}
