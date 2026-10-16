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
package com.clougence.clouddm.ds.valkey.execute.jdbc;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.sql.redis.parser.ast.commands.list.*;
import com.clougence.sql.redis.parser.ast.token.Direction;
import com.clougence.sql.redis.parser.ast.token.Position;
import com.clougence.sql.redis.parser.ast.token.StrToken;
import com.clougence.drivers.adapter.AdapterReceive;
import com.clougence.drivers.adapter.AdapterRequest;
import com.clougence.drivers.adapter.AdapterResultCursor;
import com.clougence.utils.future.CgFuture;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class GlideCallForList extends GlideUtils {

    private static String convertToDirection(Direction direction) {
        switch (direction) {
            case LEFT:
            case RIGHT:
                return direction.name();
            default:
                throw new IllegalArgumentException("Unsupported direction: " + direction);
        }
    }

    private static String convertToListPosition(Position position) {
        switch (position) {
            case BEFORE:
            case AFTER:
                return position.name();
            default:
                throw new IllegalArgumentException("Unsupported position: " + position);
        }
    }

    //

    public static CgFuture<?> cmdBLMoveRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, BLMoveRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String srcKey = argAsString(request, command.getSrc());
        String dstKey = argAsString(request, command.getDst());
        String from = convertToDirection(command.getSrcDirection());
        String to = convertToDirection(command.getDstDirection());
        double timeout = argAsDouble(request, command.getTimeout());

        String item = glideCmd.blmove(srcKey, dstKey, from, to, timeout);

        receive.responseResult(request, singleResult(request, COL_ELEMENT_STRING, item));
        return completed(sync);
    }

    public static CgFuture<?> cmdBLMPopRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, BLMPopRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        int timeout = argAsInt(request, command.getTimeout());
        int numKeys = argAsInt(request, command.getNumKeys());

        List<StrToken> kvContexts = command.getKeyNames();
        String[] keyValues = new String[kvContexts.size()];
        for (int i = 0; i < kvContexts.size(); i++) {
            keyValues[i] = argAsString(request, kvContexts.get(i));
        }
        numKeysCheck("BLMPOP", keyValues.length, numKeys);

        String lr = convertToDirection(command.getDirection());

        Map<String, String[]> values;
        if (command.getCount() != null) {
            int count = argAsInt(request, command.getCount());
            values = glideCmd.blmpop(keyValues, lr, count, timeout);
        } else {
            values = glideCmd.blmpop(keyValues, lr, timeout);
        }

        String poppedKey = values.keySet().iterator().next();
        AdapterResultCursor receiveCur = listFixedColAndResult(request, COL_KEY_STRING, poppedKey, COL_ELEMENT_STRING, Arrays.asList(values.get(poppedKey)));
        receive.responseResult(request, receiveCur);
        return completed(sync);
    }

    public static CgFuture<?> cmdBLPopRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, BLPopRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        List<StrToken> kvContexts = command.getKeyNames();
        String[] keys = new String[kvContexts.size()];
        for (int i = 0; i < kvContexts.size(); i++) {
            keys[i] = argAsString(request, kvContexts.get(i));
        }
        int timeout = argAsInt(request, command.getTimeout());

        String[] result = glideCmd.blpop(keys, timeout);

        receive.responseResult(request, listResult(request, COL_ELEMENT_STRING, Arrays.asList(result)));
        return completed(sync);
    }

    public static CgFuture<?> cmdBRPopRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, BRPopRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        List<StrToken> kvContexts = command.getKeyNames();
        String[] keys = new String[kvContexts.size()];
        for (int i = 0; i < kvContexts.size(); i++) {
            keys[i] = argAsString(request, kvContexts.get(i));
        }
        int timeout = argAsInt(request, command.getTimeout());

        String[] result = glideCmd.brpop(keys, timeout);

        receive.responseResult(request, listResult(request, COL_ELEMENT_STRING, Arrays.asList(result)));
        return completed(sync);
    }

    public static CgFuture<?> cmdBRPopLPushRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, BRPopLPushRedisCmd command, AdapterRequest request,
                                                    AdapterReceive receive) throws SQLException {
        String srcKey = argAsString(request, command.getSrcKey());
        String dstKey = argAsString(request, command.getDstKEy());
        int timeout = argAsInt(request, command.getTimeout());

        String result = glideCmd.blmove(srcKey, dstKey, "RIGHT", "LEFT", timeout);

        receive.responseResult(request, singleResult(request, COL_ELEMENT_STRING, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdLIndexRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, LIndexRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        int index = argAsInt(request, command.getIndex());

        String result = glideCmd.lindex(key, index);

        receive.responseResult(request, singleResult(request, COL_ELEMENT_STRING, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdLInsertRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, LInsertRedisCmd command, AdapterRequest request,
                                                 AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String pivot = argAsString(request, command.getPivot());
        String ele = argAsString(request, command.getKeyName());
        String where = convertToListPosition(command.getPosition());

        long result = glideCmd.linsert(key, where, pivot, ele);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdLLenRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, LLenRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        long result = glideCmd.llen(key);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdLMoveRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, LMoveRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        String srcKey = argAsString(request, command.getSrc());
        String dstKey = argAsString(request, command.getDst());
        String from = convertToDirection(command.getSrcDirection());
        String to = convertToDirection(command.getDstDirection());

        String item = glideCmd.lmove(srcKey, dstKey, from, to);

        receive.responseResult(request, singleResult(request, COL_ELEMENT_STRING, item));
        return completed(sync);
    }

    public static CgFuture<?> cmdLMPopRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, LMPopRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        int numKeys = argAsInt(request, command.getNumKeys());

        List<StrToken> kvContexts = command.getKeyNames();
        String[] keyValues = new String[kvContexts.size()];
        for (int i = 0; i < kvContexts.size(); i++) {
            keyValues[i] = argAsString(request, kvContexts.get(i));
        }
        numKeysCheck("LMPOP", keyValues.length, numKeys);

        String lr = convertToDirection(command.getDirection());

        Map<String, String[]> values;
        if (command.getCount() != null) {
            int count = argAsInt(request, command.getCount());
            values = glideCmd.lmpop(keyValues, lr, count);
        } else {
            values = glideCmd.lmpop(keyValues, lr);
        }

        String poppedKey = values.keySet().iterator().next();
        AdapterResultCursor receiveCur = listFixedColAndResult(request, COL_KEY_STRING, poppedKey, COL_ELEMENT_STRING, Arrays.asList(values.get(poppedKey)));
        receive.responseResult(request, receiveCur);
        return completed(sync);
    }

    public static CgFuture<?> cmdLPopRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, LPopRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String keyStr = argAsString(request, command.getKeyName());

        List<String> result;
        if (command.getCount() != null) {
            int count = argAsInt(request, command.getCount());
            result = Arrays.asList(glideCmd.lpopCount(keyStr, count));
        } else {
            result = Collections.singletonList(glideCmd.lpop(keyStr));
        }

        receive.responseResult(request, listResult(request, COL_ELEMENT_STRING, Arrays.asList(result)));
        return completed(sync);
    }

    public static CgFuture<?> cmdLPosRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, LPosRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String element = argAsString(request, command.getElement());

        long result = glideCmd.lpos(key, element);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdLPushRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, LPushRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        List<StrToken> eleList = command.getElements();
        String[] elements = new String[eleList.size()];
        for (int i = 0; i < eleList.size(); i++) {
            elements[i] = argAsString(request, eleList.get(i));
        }

        long result = glideCmd.lpush(key, elements);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdLPushXRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, LPushXRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        List<StrToken> eleList = command.getElements();
        String[] elements = new String[eleList.size()];
        for (int i = 0; i < eleList.size(); i++) {
            elements[i] = argAsString(request, eleList.get(i));
        }

        long result = glideCmd.lpushx(key, elements);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdLRangeRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, LRangeRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        long begin = argAsLong(request, command.getStart());
        long end = argAsLong(request, command.getStop());

        String[] result = glideCmd.lrange(key, begin, end);

        receive.responseResult(request, listResult(request, COL_ELEMENT_STRING, Arrays.asList(result)));
        return completed(sync);
    }

    public static CgFuture<?> cmdLRemRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, LRemRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        long count = argAsLong(request, command.getCount());
        String ele = argAsString(request, command.getElement());

        long result = glideCmd.lrem(key, count, ele);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdLSetRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, LSetRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        long index = argAsLong(request, command.getIndex());
        String ele = argAsString(request, command.getElement());

        String result = glideCmd.lset(key, index, ele);

        receive.responseResult(request, singleResult(request, COL_RESULT_STRING, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdLTrimRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, LTrimRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        long begin = argAsLong(request, command.getStartNum());
        long end = argAsLong(request, command.getStopNum());

        String result = glideCmd.ltrim(key, begin, end);

        receive.responseResult(request, singleResult(request, COL_RESULT_STRING, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdRPopRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, RPopRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        List<String> result;
        if (command.getCount() != null) {
            int count = argAsInt(request, command.getCount());
            result = Arrays.asList(glideCmd.rpopCount(key, count));
        } else {
            result = Collections.singletonList(glideCmd.rpop(key));
        }

        receive.responseResult(request, listResult(request, COL_ELEMENT_STRING, Arrays.asList(result)));
        return completed(sync);
    }

    public static CgFuture<?> cmdRPopLPushRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, RPopLPushRedisCmd command, AdapterRequest request,
                                                   AdapterReceive receive) throws SQLException {
        String srcKey = argAsString(request, command.getSrcKey());
        String dstKey = argAsString(request, command.getDstKey());

        String result = glideCmd.lmove(srcKey, dstKey, "RIGHT", "LEFT");

        receive.responseResult(request, singleResult(request, COL_ELEMENT_STRING, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdRPushRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, RPushRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        List<StrToken> eleList = command.getElements();
        String[] elements = new String[eleList.size()];
        for (int i = 0; i < eleList.size(); i++) {
            elements[i] = argAsString(request, eleList.get(i));
        }

        long result = glideCmd.rpush(key, elements);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdRPushXRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, RPushXRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        List<StrToken> eleList = command.getElements();
        String[] elements = new String[eleList.size()];
        for (int i = 0; i < eleList.size(); i++) {
            elements[i] = argAsString(request, eleList.get(i));
        }

        long result = glideCmd.rpushx(key, elements);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }
}
