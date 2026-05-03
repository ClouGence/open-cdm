package com.clougence.clouddm.ds.redis.execute.jdbc;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.redis.parser.ast.commands.string.*;
import com.clougence.clouddm.ds.redis.parser.ast.token.KeyAndStringToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.TtlOptToken;
import com.clougence.drivers.adapter.*;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.future.CgFuture;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.params.GetExParams;
import redis.clients.jedis.params.LCSParams;
import redis.clients.jedis.resps.LCSMatchResult;

@Slf4j
class JedisCallForString extends JedisUtils {

    private static void configOptionType(AdapterRequest request, TtlOptToken token, GetExParams params) throws SQLException {
        long expire = argAsLong(request, token.getValue());
        switch (token.getTtlType()) {
            case EX:
                params.ex(expire);
                break;
            case PX:
                params.px(expire);
                break;
            case EXAT:
                params.exAt(expire);
                break;
            case PXAT:
                params.pxAt(expire);
                break;
            default:
                throw new SQLException("GetEx option " + token.getTtlType() + " not support.", JdbcErrorCode.SQL_STATE_ILLEGAL_ARGUMENT);
        }
    }

    //

    public static CgFuture<?> cmdAppendRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, AppendRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String append = argAsString(request, command.getValue());

        long result = jedisCmd.getStringCommands().append(key, append);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdDecrRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, DecrRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        long value = jedisCmd.getStringCommands().decr(key);

        receive.responseResult(request, singleResult(request, COL_VALUE_LONG, value));
        return completed(sync);
    }

    public static CgFuture<?> cmdDecrByRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, DecrByRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        long decrement = argAsLong(request, command.getDecrement());

        long value = jedisCmd.getStringCommands().decrBy(key, decrement);

        receive.responseResult(request, singleResult(request, COL_VALUE_LONG, value));
        return completed(sync);
    }

    public static CgFuture<?> cmdGetRedisCmd(CgFuture<Object> sync, JedisCmd jedis, GetRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        String value = jedis.getJedisCommands().get(key);

        receive.responseResult(request, singleResult(request, COL_VALUE_STRING, value));
        return completed(sync);
    }

    public static CgFuture<?> cmdGetDelRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, GetDelRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        String value = jedisCmd.getStringCommands().getDel(key);

        receive.responseResult(request, singleResult(request, COL_VALUE_STRING, value));
        return completed(sync);
    }

    public static CgFuture<?> cmdGetEXRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, GetEXRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        GetExParams params = new GetExParams();

        TtlOptToken token = command.getTtlOptToken();
        if (token != null) {
            configOptionType(request, token, params);
        }
        if (command.getPersistToken() != null) {
            params.persist();
        }

        String value = jedisCmd.getStringCommands().getEx(key, params);

        receive.responseResult(request, singleResult(request, COL_VALUE_STRING, value));
        return completed(sync);
    }

    public static CgFuture<?> cmdGetRangeRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, GetRangeRedisCmd command, AdapterRequest request,
                                                  AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        long start = argAsLong(request, command.getStart());
        long end = argAsLong(request, command.getEnd());

        String value = jedisCmd.getStringCommands().getrange(key, start, end);

        receive.responseResult(request, singleResult(request, COL_VALUE_STRING, value));
        return completed(sync);
    }

    public static CgFuture<?> cmdGetSetRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, GetSetRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String value = argAsString(request, command.getValue());

        String result = jedisCmd.getStringCommands().getSet(key, value);

        receive.responseResult(request, singleResult(request, COL_VALUE_STRING, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdIncrRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, IncrRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        long value = jedisCmd.getStringCommands().incr(key);

        receive.responseResult(request, singleResult(request, COL_VALUE_LONG, value));
        return completed(sync);
    }

    public static CgFuture<?> cmdIncrByRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, IncrByRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        long increment = argAsLong(request, command.getIncrement());

        long value = jedisCmd.getStringCommands().incrBy(key, increment);

        receive.responseResult(request, singleResult(request, COL_VALUE_LONG, value));
        return completed(sync);
    }

    public static CgFuture<?> cmdIncrByFloatRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, IncrByFloatRedisCmd command, AdapterRequest request,
                                                     AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        double increment = argAsDouble(request, command.getIncrement());
        double value = jedisCmd.getStringCommands().incrByFloat(key, increment);

        receive.responseResult(request, singleResult(request, COL_VALUE_DOUBLE, value));
        return completed(sync);
    }

    public static CgFuture<?> cmdLcsRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, LcsRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String key1 = argAsString(request, command.getKey1Name());
        String key2 = argAsString(request, command.getKey2Name());

        LCSParams params = new LCSParams();
        if (command.getLenToken() != null) {
            params.len();
        }
        if (command.getIdxToken() != null) {
            params.idx();
        }
        if (command.getMinMatchLen() != null) {
            params.minMatchLen(argAsLong(request, command.getMinMatchLen()));
        }
        if (command.getWithMatchLen() != null) {
            params.withMatchLen();
        }

        LCSMatchResult value = jedisCmd.getStringCommands().lcs(key1, key2, params);
        String match = value.getMatchString();
        long len = value.getLen();

        //
        JdbcColumn COL_MATCH_VALUE = new JdbcColumn("Match", AdapterType.String, "", "", "");
        JdbcColumn COL_LEN_VALUE = new JdbcColumn("Len", AdapterType.Long, "", "", "");
        JdbcColumn COL_POS_A = new JdbcColumn("Position A", AdapterType.Long, "", "", "");
        JdbcColumn COL_POS_B = new JdbcColumn("Position B", AdapterType.Long, "", "", "");
        JdbcColumn COL_POS_MATCHED_LEN = new JdbcColumn("Matched Len", AdapterType.Long, "", "", "");
        AdapterResultCursor receiveCur = new AdapterResultCursor(request, Arrays.asList(//
                COL_MATCH_VALUE,   //
                COL_LEN_VALUE,     //
                COL_POS_A,         //
                COL_POS_B,         //
                COL_POS_MATCHED_LEN//
        ));

        List<LCSMatchResult.MatchedPosition> matches = value.getMatches();
        if (matches.isEmpty()) {
            receiveCur.pushData(CollectionUtils.asMap(//
                    COL_MATCH_VALUE.name, match,      //
                    COL_LEN_VALUE.name, len,          //
                    COL_POS_A.name, null,             //
                    COL_POS_B.name, null,             //
                    COL_POS_MATCHED_LEN.name, null    //
            ));
        } else {
            for (int i = 0; i < matches.size(); i++) {
                LCSMatchResult.MatchedPosition position = matches.get(i);
                LCSMatchResult.Position a = position.getA();
                LCSMatchResult.Position b = position.getB();
                long aStart = a.getStart();
                long aEnd = a.getEnd();
                long bStart = b.getStart();
                long bEnd = b.getEnd();
                long matchLen = position.getMatchLen();

                if (i == 0) {
                    receiveCur.pushData(CollectionUtils.asMap(      //
                            COL_MATCH_VALUE.name, match,            //
                            COL_LEN_VALUE.name, len,                //
                            COL_POS_A.name, (aStart + " - " + aEnd),//
                            COL_POS_B.name, (bStart + " - " + bEnd),//
                            COL_POS_MATCHED_LEN.name, matchLen      //
                    ));
                } else {
                    receiveCur.pushData(CollectionUtils.asMap(      //
                            COL_MATCH_VALUE.name, null,             //
                            COL_LEN_VALUE.name, null,               //
                            COL_POS_A.name, (aStart + " - " + aEnd),//
                            COL_POS_B.name, (bStart + " - " + bEnd),//
                            COL_POS_MATCHED_LEN.name, matchLen      //
                    ));
                }
            }
        }

        receiveCur.pushFinish();
        receive.responseResult(request, receiveCur);
        return completed(sync);
    }

    public static CgFuture<?> cmdMGetRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, MGetRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        List<StrToken> keyTokens = command.getKeys();
        String[] keys = new String[keyTokens.size()];
        for (int i = 0; i < keyTokens.size(); i++) {
            keys[i] = argAsString(request, keyTokens.get(i));
        }

        List<String> values = jedisCmd.getStringCommands().mget(keys);

        Map<String, String> resultMap = new LinkedHashMap<>();
        for (int i = 0; i < keys.length; i++) {
            resultMap.put(keys[i], values.get(i));
        }

        receive.responseResult(request, listResult(request, COL_KEY_STRING, COL_VALUE_STRING, resultMap));
        return completed(sync);
    }

    public static CgFuture<?> cmdMSetRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, MSetRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        List<KeyAndStringToken> kvList = command.getKeyValues();
        String[] keyValues = new String[kvList.size() * 2];
        int i = 0;
        for (KeyAndStringToken keyValue : kvList) {
            keyValues[i++] = argAsString(request, keyValue.getKeyName());
            keyValues[i++] = argAsString(request, keyValue.getStringValue());
        }

        String status = jedisCmd.getStringCommands().mset(keyValues);
        receive.responseResult(request, singleResult(request, COL_RESULT_STRING, status));
        return completed(sync);
    }

    public static CgFuture<?> cmdMSetNXRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, MSetNXRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        List<KeyAndStringToken> kvList = command.getKeyValues();
        String[] keyValues = new String[kvList.size() * 2];
        int i = 0;
        for (KeyAndStringToken keyValue : kvList) {
            keyValues[i++] = argAsString(request, keyValue.getKeyName());
            keyValues[i++] = argAsString(request, keyValue.getStringValue());
        }

        long status = jedisCmd.getStringCommands().msetnx(keyValues);
        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, status));
        return completed(sync);
    }

    public static CgFuture<?> cmdPSetEXRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, PSetEXRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String value = argAsString(request, command.getValue());
        long milliseconds = argAsLong(request, command.getTimeoutMs());

        String status = jedisCmd.getStringCommands().psetex(key, milliseconds, value);

        receive.responseResult(request, singleResult(request, COL_RESULT_STRING, status));
        return completed(sync);
    }

    public static CgFuture<?> cmdSetRedisCmd(CgFuture<Object> sync, JedisCmd jedis, SetRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String value = argAsString(request, command.getValue());

        String status = jedis.getJedisCommands().set(key, value);

        receive.responseResult(request, singleResult(request, COL_STATUS_STRING, status));
        return completed(sync);
    }

    public static CgFuture<?> cmdSetEXRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, SetEXRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String value = argAsString(request, command.getValue());
        long seconds = argAsLong(request, command.getSeconds());

        String status = jedisCmd.getStringCommands().setex(key, seconds, value);

        receive.responseResult(request, singleResult(request, COL_RESULT_STRING, status));
        return completed(sync);
    }

    public static CgFuture<?> cmdSetNXRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, SetNXRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String value = argAsString(request, command.getValue());

        long status = jedisCmd.getStringCommands().setnx(key, value);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, status));
        return completed(sync);
    }

    public static CgFuture<?> cmdSetRangeRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, SetRangeRedisCmd command, AdapterRequest request,
                                                  AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        long offset = argAsLong(request, command.getOffset());
        String value = argAsString(request, command.getValue());

        long status = jedisCmd.getStringCommands().setrange(key, offset, value);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, status));
        return completed(sync);
    }

    public static CgFuture<?> cmdStrLenRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, StrLenRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        long status = jedisCmd.getStringCommands().strlen(key);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, status));
        return completed(sync);
    }

    public static CgFuture<?> cmdSubstrRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, SubstrRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        int start = argAsInt(request, command.getStart());
        int end = argAsInt(request, command.getEnd());

        String value = jedisCmd.getStringCommands().substr(key, start, end);

        receive.responseResult(request, singleResult(request, COL_VALUE_STRING, value));
        return completed(sync);
    }
}
