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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.clougence.sql.redis.parser.ast.commands.string.*;
import com.clougence.sql.redis.parser.ast.token.KeyAndStringToken;
import com.clougence.sql.redis.parser.ast.token.StrToken;
import com.clougence.sql.redis.parser.ast.token.TtlOptToken;
import com.clougence.drivers.adapter.*;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.future.CgFuture;

import glide.api.models.commands.GetExOptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class GlideCallForString extends GlideUtils {

    private static GetExOptions configOptionType(AdapterRequest request, TtlOptToken token) throws SQLException {
        long expire = argAsLong(request, token.getValue());
        switch (token.getTtlType()) {
            case EX:
                return GetExOptions.Seconds(expire);
            case PX:
                return GetExOptions.Milliseconds(expire);
            case EXAT:
                return GetExOptions.UnixSeconds(expire);
            case PXAT:
                return GetExOptions.UnixMilliseconds(expire);
            default:
                throw new SQLException("GetEx option " + token.getTtlType() + " not support.", JdbcErrorCode.SQL_STATE_ILLEGAL_ARGUMENT);
        }
    }

    //

    public static CgFuture<?> cmdAppendRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, AppendRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String append = argAsString(request, command.getValue());

        long result = glideCmd.append(key, append);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdDecrRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, DecrRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        long value = glideCmd.decr(key);

        receive.responseResult(request, singleResult(request, COL_VALUE_LONG, value));
        return completed(sync);
    }

    public static CgFuture<?> cmdDecrByRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, DecrByRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        long decrement = argAsLong(request, command.getDecrement());

        long value = glideCmd.decrBy(key, decrement);

        receive.responseResult(request, singleResult(request, COL_VALUE_LONG, value));
        return completed(sync);
    }

    public static CgFuture<?> cmdGetRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, GetRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        String value = glideCmd.get(key);

        receive.responseResult(request, singleResult(request, COL_VALUE_STRING, value));
        return completed(sync);
    }

    public static CgFuture<?> cmdGetDelRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, GetDelRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        String value = glideCmd.getDel(key);

        receive.responseResult(request, singleResult(request, COL_VALUE_STRING, value));
        return completed(sync);
    }

    public static CgFuture<?> cmdGetEXRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, GetEXRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        GetExOptions options = null;

        TtlOptToken token = command.getTtlOptToken();
        if (token != null) {
            options = configOptionType(request, token);
        }
        if (command.getPersistToken() != null) {
            options = GetExOptions.Persist();
        }

        String value = glideCmd.getEx(key, options);

        receive.responseResult(request, singleResult(request, COL_VALUE_STRING, value));
        return completed(sync);
    }

    public static CgFuture<?> cmdGetRangeRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, GetRangeRedisCmd command, AdapterRequest request,
                                                  AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        long start = argAsLong(request, command.getStart());
        long end = argAsLong(request, command.getEnd());

        String value = glideCmd.getrange(key, start, end);

        receive.responseResult(request, singleResult(request, COL_VALUE_STRING, value));
        return completed(sync);
    }

    public static CgFuture<?> cmdGetSetRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, GetSetRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String value = argAsString(request, command.getValue());

        String result = glideCmd.getSet(key, value);

        receive.responseResult(request, singleResult(request, COL_VALUE_STRING, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdIncrRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, IncrRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        long value = glideCmd.incr(key);

        receive.responseResult(request, singleResult(request, COL_VALUE_LONG, value));
        return completed(sync);
    }

    public static CgFuture<?> cmdIncrByRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, IncrByRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        long increment = argAsLong(request, command.getIncrement());

        long value = glideCmd.incrBy(key, increment);

        receive.responseResult(request, singleResult(request, COL_VALUE_LONG, value));
        return completed(sync);
    }

    public static CgFuture<?> cmdIncrByFloatRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, IncrByFloatRedisCmd command, AdapterRequest request,
                                                     AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        double increment = argAsDouble(request, command.getIncrement());
        double value = glideCmd.incrByFloat(key, increment);

        receive.responseResult(request, singleResult(request, COL_VALUE_DOUBLE, value));
        return completed(sync);
    }

    public static CgFuture<?> cmdLcsRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, LcsRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String key1 = argAsString(request, command.getKey1Name());
        String key2 = argAsString(request, command.getKey2Name());

        // GLIDE 仅提供基础 LCS（返回匹配字符串），LEN/IDX 选项暂不支持。
        String match = glideCmd.lcs(key1, key2);

        receive.responseResult(request, singleResult(request, new JdbcColumn("Match", AdapterType.String, "", "", ""), match));
        return completed(sync);
    }

    public static CgFuture<?> cmdMGetRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, MGetRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        List<StrToken> keyTokens = command.getKeys();
        String[] keys = new String[keyTokens.size()];
        for (int i = 0; i < keyTokens.size(); i++) {
            keys[i] = argAsString(request, keyTokens.get(i));
        }

        String[] values = glideCmd.mget(keys);

        Map<String, String> resultMap = new LinkedHashMap<>();
        for (int i = 0; i < keys.length; i++) {
            resultMap.put(keys[i], values[i]);
        }

        receive.responseResult(request, listResult(request, COL_KEY_STRING, COL_VALUE_STRING, resultMap));
        return completed(sync);
    }

    public static CgFuture<?> cmdMSetRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, MSetRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        List<KeyAndStringToken> kvList = command.getKeyValues();
        Map<String, String> keyValues = new LinkedHashMap<>();
        for (KeyAndStringToken keyValue : kvList) {
            keyValues.put(argAsString(request, keyValue.getKeyName()), argAsString(request, keyValue.getStringValue()));
        }

        String status = glideCmd.mset(keyValues);
        receive.responseResult(request, singleResult(request, COL_RESULT_STRING, status));
        return completed(sync);
    }

    public static CgFuture<?> cmdMSetNXRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, MSetNXRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        List<KeyAndStringToken> kvList = command.getKeyValues();
        Map<String, String> keyValues = new LinkedHashMap<>();
        for (KeyAndStringToken keyValue : kvList) {
            keyValues.put(argAsString(request, keyValue.getKeyName()), argAsString(request, keyValue.getStringValue()));
        }

        boolean status = glideCmd.msetnx(keyValues);
        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, status));
        return completed(sync);
    }

    public static CgFuture<?> cmdPSetEXRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, PSetEXRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String value = argAsString(request, command.getValue());
        long milliseconds = argAsLong(request, command.getTimeoutMs());

        String status = glideCmd.psetex(key, milliseconds, value);

        receive.responseResult(request, singleResult(request, COL_RESULT_STRING, status));
        return completed(sync);
    }

    public static CgFuture<?> cmdSetRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, SetRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String value = argAsString(request, command.getValue());

        String status = glideCmd.set(key, value);

        receive.responseResult(request, singleResult(request, COL_STATUS_STRING, status));
        return completed(sync);
    }

    public static CgFuture<?> cmdSetEXRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, SetEXRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String value = argAsString(request, command.getValue());
        long seconds = argAsLong(request, command.getSeconds());

        String status = glideCmd.setex(key, seconds, value);

        receive.responseResult(request, singleResult(request, COL_RESULT_STRING, status));
        return completed(sync);
    }

    public static CgFuture<?> cmdSetNXRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, SetNXRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String value = argAsString(request, command.getValue());

        boolean status = glideCmd.setnx(key, value);

        receive.responseResult(request, singleResult(request, COL_RESULT_BOOLEAN, status));
        return completed(sync);
    }

    public static CgFuture<?> cmdSetRangeRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, SetRangeRedisCmd command, AdapterRequest request,
                                                  AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        long offset = argAsLong(request, command.getOffset());
        String value = argAsString(request, command.getValue());

        long status = glideCmd.setrange(key, offset, value);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, status));
        return completed(sync);
    }

    public static CgFuture<?> cmdStrLenRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, StrLenRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        long status = glideCmd.strlen(key);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, status));
        return completed(sync);
    }

    public static CgFuture<?> cmdSubstrRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, SubstrRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        int start = argAsInt(request, command.getStart());
        int end = argAsInt(request, command.getEnd());

        String value = glideCmd.getrange(key, start, end);

        receive.responseResult(request, singleResult(request, COL_VALUE_STRING, value));
        return completed(sync);
    }
}
