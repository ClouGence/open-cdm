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
import java.util.List;

import com.clougence.sql.redis.parser.ast.commands.bit.*;
import com.clougence.sql.redis.parser.ast.token.StrToken;
import com.clougence.drivers.adapter.AdapterReceive;
import com.clougence.drivers.adapter.AdapterRequest;
import com.clougence.utils.future.CgFuture;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class GlideCallForBit extends GlideUtils {

    private static String convertToBitOP(BitOPTypeEnum typeEnum) {
        switch (typeEnum) {
            case AND:
            case OR:
            case XOR:
            case NOT:
                return typeEnum.name();
            default:
                // BITOP DIFF/DIFF1/ANDOR/ONE are Redis 7.x extensions; GLIDE does not support them yet.
                throw new UnsupportedOperationException("Unsupported BitOPTypeEnum: " + typeEnum);
        }
    }

    public static CgFuture<?> cmdBitFieldRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, BitFieldRedisCmd command, AdapterRequest request, AdapterReceive receive) {
        throw new UnsupportedOperationException("redis command '" + command.getCmdType().getCommandStr() + "' Unsupported.");
    }

    public static CgFuture<?> cmdBitFieldRORedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, BitFieldRORedisCmd command, AdapterRequest request, AdapterReceive receive) {
        throw new UnsupportedOperationException("redis command '" + command.getCmdType().getCommandStr() + "' Unsupported.");
    }

    public static CgFuture<?> cmdBitCountRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, BitCountRedisCmd command, AdapterRequest request,
                                                  AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        int start = argAsInt(request, command.getStart());
        int end = argAsInt(request, command.getEnd());
        BitCountTypeEnum typeEnum = command.getTypeEnum();

        long value = glideCmd.bitcount(key, start, end);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, value));
        return completed(sync);
    }

    public static CgFuture<?> cmdBitOPRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, BitOPRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        String destKey = argAsString(request, command.getDestKey());
        List<StrToken> keyTokens = command.getKeyNames();
        String[] keys = new String[keyTokens.size()];
        for (int i = 0; i < keyTokens.size(); i++) {
            keys[i] = argAsString(request, keyTokens.get(i));
        }
        String opTypeEnum = convertToBitOP(command.getOpType());

        long value = glideCmd.bitop(opTypeEnum, destKey, keys);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, value));
        return completed(sync);
    }

    public static CgFuture<?> cmdBitPosRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, BitPosRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String keyName = argAsString(request, command.getKeyName());
        boolean bitValue = argAsBool(request, command.getBitValue());
        Integer start = argAsInteger(request, command.getStart());
        Integer end = argAsInteger(request, command.getEnd());
        BitCountTypeEnum typeEnum = command.getTypeEnum();

        long value = glideCmd.bitpos(keyName, bitValue ? 1 : 0);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, value));
        return completed(sync);
    }

    public static CgFuture<?> cmdGetBitRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, GetBitRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        int offset = argAsInt(request, command.getOffset());

        long value = glideCmd.getbit(key, offset);

        receive.responseResult(request, singleResult(request, COL_VALUE_LONG, value));
        return completed(sync);
    }

    public static CgFuture<?> cmdSetBitRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, SetBitRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        int offset = argAsInt(request, command.getOffset());
        boolean value = argAsBool(request, command.getValue());

        long result = glideCmd.setbit(key, offset, value ? 1 : 0);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }
}
