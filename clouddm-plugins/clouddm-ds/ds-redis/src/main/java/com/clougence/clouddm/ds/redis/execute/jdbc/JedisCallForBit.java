package com.clougence.clouddm.ds.redis.execute.jdbc;

import java.sql.SQLException;
import java.util.List;

import com.clougence.clouddm.ds.redis.parser.ast.commands.bit.*;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.drivers.adapter.AdapterReceive;
import com.clougence.drivers.adapter.AdapterRequest;
import com.clougence.utils.future.CgFuture;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.args.BitCountOption;
import redis.clients.jedis.args.BitOP;
import redis.clients.jedis.params.BitPosParams;

@Slf4j
class JedisCallForBit extends JedisUtils {

    private static BitOP convertToBitOP(BitOPTypeEnum typeEnum) {
        switch (typeEnum) {
            case AND:
                return BitOP.AND;
            case OR:
                return BitOP.OR;
            case XOR:
                return BitOP.XOR;
            case NOT:
                return BitOP.NOT;
            case DIFF:
                return BitOP.DIFF;
            case DIFF1:
                return BitOP.DIFF1;
            case ANDOR:
                return BitOP.ANDOR;
            case ONE:
                return BitOP.ONE;
            default:
                throw new UnsupportedOperationException("Unsupported BitOPTypeEnum: " + typeEnum);
        }
    }

    public static CgFuture<?> cmdBitFieldRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, BitFieldRedisCmd command, AdapterRequest request, AdapterReceive receive) {
        throw new UnsupportedOperationException("redis command '" + command.getCmdType().getCommandStr() + "' Unsupported.");
    }

    public static CgFuture<?> cmdBitFieldRORedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, BitFieldRORedisCmd command, AdapterRequest request, AdapterReceive receive) {
        throw new UnsupportedOperationException("redis command '" + command.getCmdType().getCommandStr() + "' Unsupported.");
    }

    public static CgFuture<?> cmdBitCountRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, BitCountRedisCmd command, AdapterRequest request,
                                                  AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        int start = argAsInt(request, command.getStart());
        int end = argAsInt(request, command.getEnd());
        BitCountTypeEnum typeEnum = command.getTypeEnum();

        long value;
        if (typeEnum == null) {
            value = jedisCmd.getBitCommands().bitcount(key, start, end);
        } else {
            value = jedisCmd.getBitCommands().bitcount(key, start, end, typeEnum == BitCountTypeEnum.BIT ? BitCountOption.BIT : BitCountOption.BYTE);
        }

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, value));
        return completed(sync);
    }

    public static CgFuture<?> cmdBitOPRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, BitOPRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        String destKey = argAsString(request, command.getDestKey());
        List<StrToken> keyTokens = command.getKeyNames();
        String[] keys = new String[keyTokens.size()];
        for (int i = 0; i < keyTokens.size(); i++) {
            keys[i] = argAsString(request, keyTokens.get(i));
        }
        BitOP opTypeEnum = convertToBitOP(command.getOpType());

        long value = jedisCmd.getBitCommands().bitop(opTypeEnum, destKey, keys);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, value));
        return completed(sync);
    }

    public static CgFuture<?> cmdBitPosRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, BitPosRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String keyName = argAsString(request, command.getKeyName());
        boolean bitValue = argAsBool(request, command.getBitValue());
        Integer start = argAsInteger(request, command.getStart());
        Integer end = argAsInteger(request, command.getEnd());
        BitCountTypeEnum typeEnum = command.getTypeEnum();

        BitPosParams params = null;
        if (start != null && end != null) {
            params = new BitPosParams(start, end);
        }
        if (typeEnum != null) {
            params = params == null ? new BitPosParams() : params;
            params.modifier(typeEnum == BitCountTypeEnum.BIT ? BitCountOption.BIT : BitCountOption.BYTE);
        }

        long value;
        if (params == null) {
            value = jedisCmd.getBitCommands().bitpos(keyName, bitValue);
        } else {
            value = jedisCmd.getBitCommands().bitpos(keyName, bitValue, params);
        }

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, value));
        return completed(sync);
    }

    public static CgFuture<?> cmdGetBitRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, GetBitRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        int offset = argAsInt(request, command.getOffset());

        boolean value = jedisCmd.getBitCommands().getbit(key, offset);

        receive.responseResult(request, singleResult(request, COL_VALUE_BOOLEAN, value));
        return completed(sync);
    }

    public static CgFuture<?> cmdSetBitRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, SetBitRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        int offset = argAsInt(request, command.getOffset());
        boolean value = argAsBool(request, command.getValue());

        boolean result = jedisCmd.getBitCommands().setbit(key, offset, value);

        receive.responseResult(request, singleResult(request, COL_RESULT_BOOLEAN, result));
        return completed(sync);
    }
}
