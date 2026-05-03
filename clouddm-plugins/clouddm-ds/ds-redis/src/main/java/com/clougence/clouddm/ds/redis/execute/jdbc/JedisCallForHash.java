package com.clougence.clouddm.ds.redis.execute.jdbc;

import java.sql.SQLException;
import java.util.*;

import com.clougence.clouddm.ds.redis.parser.ast.commands.hash.*;
import com.clougence.clouddm.ds.redis.parser.ast.token.KeyAndStringToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.TtlOptToken;
import com.clougence.drivers.adapter.AdapterReceive;
import com.clougence.drivers.adapter.AdapterRequest;
import com.clougence.drivers.adapter.AdapterResultCursor;
import com.clougence.drivers.adapter.JdbcErrorCode;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.future.CgFuture;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.args.ExpiryOption;
import redis.clients.jedis.params.HGetExParams;
import redis.clients.jedis.params.HSetExParams;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;

@Slf4j
class JedisCallForHash extends JedisUtils {

    private static CgFuture<?> resultFiledValueList1(CgFuture<Object> sync, AdapterRequest request, AdapterReceive receive,
                                                     List<Map.Entry<String, String>> result) throws SQLException {
        AdapterResultCursor receiveCur = new AdapterResultCursor(request, Arrays.asList(//
                COL_FIELD_STRING,   //
                COL_VALUE_STRING));
        receive.responseResult(request, receiveCur);

        for (Map.Entry<String, String> item : result) {
            receiveCur.pushData(CollectionUtils.asMap(    //
                    COL_FIELD_STRING.name, item.getKey(), //
                    COL_VALUE_STRING.name, item.getValue()//
            ));
        }
        receiveCur.pushFinish();
        return completed(sync);
    }

    private static CgFuture<?> resultFiledValueList2(CgFuture<Object> sync, AdapterRequest request, AdapterReceive receive, String cursor,
                                                     List<Map.Entry<String, String>> result) throws SQLException {
        AdapterResultCursor receiveCur = new AdapterResultCursor(request, Arrays.asList(//
                COL_CURSOR_STRING,//
                COL_FIELD_STRING, //
                COL_VALUE_STRING));
        receive.responseResult(request, receiveCur);

        for (Map.Entry<String, String> item : result) {
            receiveCur.pushData(CollectionUtils.asMap(    //
                    COL_CURSOR_STRING.name, cursor,       //
                    COL_FIELD_STRING.name, item.getKey(), //
                    COL_VALUE_STRING.name, item.getValue()//
            ));
        }
        receiveCur.pushFinish();
        return completed(sync);
    }

    private static void applyTTLType(HGetExParams params, TtlOptToken.TtlType ttlType, long value) throws SQLException {
        switch (ttlType) {
            case EX:
                params.ex(value);
                return;
            case PX:
                params.px(value);
                return;
            case EXAT:
                params.exAt(value);
                return;
            case PXAT:
                params.pxAt(value);
                return;
            default:
                throw new SQLException("ttlType(" + ttlType.name() + ") not support.", JdbcErrorCode.SQL_STATE_ILLEGAL_ARGUMENT);
        }
    }

    private static void applyTTLType(HSetExParams params, TtlOptToken.TtlType ttlType, long value) throws SQLException {
        switch (ttlType) {
            case EX:
                params.ex(value);
                return;
            case PX:
                params.px(value);
                return;
            case EXAT:
                params.exAt(value);
                return;
            case PXAT:
                params.pxAt(value);
                return;
            default:
                throw new SQLException("ttlType(" + ttlType.name() + ") not support.", JdbcErrorCode.SQL_STATE_ILLEGAL_ARGUMENT);
        }
    }

    //

    public static CgFuture<?> cmdHDelRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, HDelRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        List<StrToken> fieldNames = command.getFieldName();
        String[] fields = new String[fieldNames.size()];
        for (int i = 0; i < fieldNames.size(); i++) {
            fields[i] = argAsString(request, fieldNames.get(i));
        }

        long result = jedisCmd.getHashCommands().hdel(key, fields);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdHExistsRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, HExistsRedisCmd command, AdapterRequest request,
                                                 AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String field = argAsString(request, command.getFieldName());

        boolean result = jedisCmd.getHashCommands().hexists(key, field);

        receive.responseResult(request, singleResult(request, COL_RESULT_BOOLEAN, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdHExpireRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, HExpireRedisCmd command, AdapterRequest request,
                                                 AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKey());
        long seconds = argAsLong(request, command.getTimeout());

        ExpiryOption option = null;
        if (command.getKeyOpt() != null) {
            option = convertToExpiryOption(command.getKeyOpt().getOptionType());
        }

        int numKeys = argAsInt(request, command.getNumFields());
        List<StrToken> filedNames = command.getFiledNames();
        String[] fields = new String[filedNames.size()];
        for (int i = 0; i < filedNames.size(); i++) {
            fields[i] = argAsString(request, filedNames.get(i));
        }
        numKeysCheck("HEXPIRE", fields.length, numKeys);

        List<Long> result;
        if (option != null) {
            result = jedisCmd.getHashCommands().hexpire(key, seconds, option, fields);
        } else {
            result = jedisCmd.getHashCommands().hexpire(key, seconds, fields);
        }

        receive.responseResult(request, listResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdHExpireAtRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, HExpireAtRedisCmd command, AdapterRequest request,
                                                   AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKey());
        long seconds = argAsLong(request, command.getTimeout());

        ExpiryOption option = null;
        if (command.getKeyOpt() != null) {
            option = convertToExpiryOption(command.getKeyOpt().getOptionType());
        }

        int numKeys = argAsInt(request, command.getNumFields());
        List<StrToken> filedNames = command.getFiledNames();
        String[] fields = new String[filedNames.size()];
        for (int i = 0; i < filedNames.size(); i++) {
            fields[i] = argAsString(request, filedNames.get(i));
        }
        numKeysCheck("HEXPIREAT", fields.length, numKeys);

        List<Long> result;
        if (option != null) {
            result = jedisCmd.getHashCommands().hexpireAt(key, seconds, option, fields);
        } else {
            result = jedisCmd.getHashCommands().hexpireAt(key, seconds, fields);
        }

        receive.responseResult(request, listResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdHExpireTimeRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, HExpireTimeRedisCmd command, AdapterRequest request,
                                                     AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKey());
        int numKeys = argAsInt(request, command.getNumFields());
        List<StrToken> filedNames = command.getFiledNames();
        String[] fields = new String[filedNames.size()];
        for (int i = 0; i < filedNames.size(); i++) {
            fields[i] = argAsString(request, filedNames.get(i));
        }
        numKeysCheck("HEXPIRETIME", fields.length, numKeys);

        List<Long> result = jedisCmd.getHashCommands().hexpireTime(key, fields);

        receive.responseResult(request, listResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdHGetRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, HGetRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String field = argAsString(request, command.getFieldName());

        String result = jedisCmd.getHashCommands().hget(key, field);

        receive.responseResult(request, singleResult(request, COL_VALUE_STRING, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdHGetAllRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, HGetAllRedisCmd command, AdapterRequest request,
                                                 AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        Map<String, String> result = jedisCmd.getHashCommands().hgetAll(key);

        long maxRows = request.getMaxRows();
        AdapterResultCursor receiveCur = new AdapterResultCursor(request, Arrays.asList(COL_FIELD_STRING, COL_VALUE_STRING));

        int affectRows = 0;
        for (Map.Entry<String, String> item : result.entrySet()) {
            receiveCur.pushData(CollectionUtils.asMap(    //
                    COL_FIELD_STRING.name, item.getKey(), //
                    COL_VALUE_STRING.name, item.getValue()//
            ));
            affectRows++;

            if (maxRows > 0 && affectRows >= maxRows) {
                break;
            }
        }
        receiveCur.pushFinish();
        receive.responseResult(request, receiveCur);
        return completed(sync);
    }

    public static CgFuture<?> cmdHGetDelRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, HGetDelRedisCmd command, AdapterRequest request,
                                                 AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKey());
        int numKeys = argAsInt(request, command.getNumFields());
        List<StrToken> filedNames = command.getFiledNames();
        String[] fields = new String[filedNames.size()];
        for (int i = 0; i < filedNames.size(); i++) {
            fields[i] = argAsString(request, filedNames.get(i));
        }
        numKeysCheck("HGETDEL", fields.length, numKeys);

        List<String> result = jedisCmd.getHashCommands().hgetdel(key, fields);

        receive.responseResult(request, listResult(request, COL_RESULT_STRING, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdHGetEXRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, HGetEXRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKey());
        int numKeys = argAsInt(request, command.getNumFields());
        List<StrToken> filedNames = command.getFieldNames();
        String[] fields = new String[filedNames.size()];
        for (int i = 0; i < filedNames.size(); i++) {
            fields[i] = argAsString(request, filedNames.get(i));
        }
        numKeysCheck("HGETEX", fields.length, numKeys);

        HGetExParams params = new HGetExParams();
        if (command.getPersistTag() != null) {
            params.persist();
        }
        if (command.getTtlOptToken() != null) {
            long ttlValue = argAsLong(request, command.getTtlOptToken().getValue());
            applyTTLType(params, command.getTtlOptToken().getTtlType(), ttlValue);
        }

        List<String> result = jedisCmd.getHashCommands().hgetex(key, params, fields);

        receive.responseResult(request, listResult(request, COL_RESULT_STRING, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdHIncrByRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, HIncrByRedisCmd command, AdapterRequest request,
                                                 AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String field = argAsString(request, command.getFiledName());
        long increment = argAsLong(request, command.getFloatValue());

        long value = jedisCmd.getHashCommands().hincrBy(key, field, increment);

        receive.responseResult(request, singleResult(request, COL_VALUE_LONG, value));
        return completed(sync);
    }

    public static CgFuture<?> cmdHIncrByFloatRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, HIncrByFloatRedisCmd command, AdapterRequest request,
                                                      AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String field = argAsString(request, command.getFiledName());
        double increment = argAsDouble(request, command.getFloatValue());

        double value = jedisCmd.getHashCommands().hincrByFloat(key, field, increment);

        receive.responseResult(request, singleResult(request, COL_VALUE_DOUBLE, value));
        return completed(sync);
    }

    public static CgFuture<?> cmdHKeysRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, HKeysRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        Set<String> value = jedisCmd.getHashCommands().hkeys(key);

        receive.responseResult(request, listResult(request, COL_FIELD_STRING, value));
        return completed(sync);
    }

    public static CgFuture<?> cmdHLenRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, HLenRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        long result = jedisCmd.getHashCommands().hlen(key);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdHMGetRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, HMGetRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        List<StrToken> fieldNames = command.getFieldName();
        String[] fields = new String[fieldNames.size()];
        for (int i = 0; i < fieldNames.size(); i++) {
            fields[i] = argAsString(request, fieldNames.get(i));
        }

        List<String> result = jedisCmd.getHashCommands().hmget(key, fields);

        receive.responseResult(request, listResult(request, COL_VALUE_STRING, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdHMSetRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, HMSetRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        List<KeyAndStringToken> kvContexts = command.getKeyValues();
        Map<String, String> data = new LinkedHashMap<>();
        for (KeyAndStringToken kv : kvContexts) {
            String vKey = argAsString(request, kv.getKeyName());
            String vValue = argAsString(request, kv.getStringValue());
            data.put(vKey, vValue);
        }

        String result = jedisCmd.getHashCommands().hmset(key, data);

        receive.responseResult(request, singleResult(request, COL_RESULT_STRING, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdHPersistRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, HPersistRedisCmd command, AdapterRequest request,
                                                  AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKey());
        int numKeys = argAsInt(request, command.getNumFields());

        List<StrToken> fieldNames = command.getFiledNames();
        String[] fields = new String[fieldNames.size()];
        for (int i = 0; i < fieldNames.size(); i++) {
            fields[i] = argAsString(request, fieldNames.get(i));
        }
        numKeysCheck("HPERSIST", fields.length, numKeys);

        List<Long> result = jedisCmd.getHashCommands().hpersist(key, fields);

        receive.responseResult(request, listResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdHPExpireRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, HPExpireRedisCmd command, AdapterRequest request,
                                                  AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKey());
        long timeout = argAsLong(request, command.getTimeout());

        ExpiryOption option = null;
        if (command.getKeyOpt() != null) {
            option = convertToExpiryOption(command.getKeyOpt().getOptionType());
        }

        long numKeys = argAsLong(request, command.getNumFields());
        List<StrToken> fieldNames = command.getFiledNames();
        String[] fields = new String[fieldNames.size()];
        for (int i = 0; i < fieldNames.size(); i++) {
            fields[i] = argAsString(request, fieldNames.get(i));
        }
        numKeysCheck("HPEXPIRE", fields.length, numKeys);

        List<Long> result;
        if (option != null) {
            result = jedisCmd.getHashCommands().hpexpire(key, timeout, option, fields);
        } else {
            result = jedisCmd.getHashCommands().hpexpire(key, timeout, fields);
        }

        receive.responseResult(request, listResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdHPExpireAtRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, HPExpireAtRedisCmd command, AdapterRequest request,
                                                    AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKey());
        long timeout = argAsLong(request, command.getTimeout());

        ExpiryOption option = null;
        if (command.getKeyOpt() != null) {
            option = convertToExpiryOption(command.getKeyOpt().getOptionType());
        }

        long numKeys = argAsLong(request, command.getNumFields());
        List<StrToken> fieldNames = command.getFiledNames();
        String[] fields = new String[fieldNames.size()];
        for (int i = 0; i < fieldNames.size(); i++) {
            fields[i] = argAsString(request, fieldNames.get(i));
        }
        numKeysCheck("HPEXPIREAT", fields.length, numKeys);

        List<Long> result;
        if (option != null) {
            result = jedisCmd.getHashCommands().hpexpireAt(key, timeout, option, fields);
        } else {
            result = jedisCmd.getHashCommands().hpexpireAt(key, timeout, fields);
        }

        receive.responseResult(request, listResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdHPExpireTimeRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, HPExpireTimeRedisCmd command, AdapterRequest request,
                                                      AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKey());
        long numKeys = argAsLong(request, command.getNumFields());
        List<StrToken> fieldNames = command.getFiledNames();
        String[] fields = new String[fieldNames.size()];
        for (int i = 0; i < fieldNames.size(); i++) {
            fields[i] = argAsString(request, fieldNames.get(i));
        }
        numKeysCheck("HPEXPIRETIME", fields.length, numKeys);

        List<Long> result = jedisCmd.getHashCommands().hpexpireTime(key, fields);

        receive.responseResult(request, listResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdHPTtlRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, HPTtlRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKey());
        long numKeys = argAsLong(request, command.getNumFields());
        List<StrToken> fieldNames = command.getFiledNames();
        String[] fields = new String[fieldNames.size()];
        for (int i = 0; i < fieldNames.size(); i++) {
            fields[i] = argAsString(request, fieldNames.get(i));
        }
        numKeysCheck("HPTTL", fields.length, numKeys);

        List<Long> result = jedisCmd.getHashCommands().hpttl(key, fields);

        receive.responseResult(request, listResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdHTtlRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, HTtlRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKey());
        long numKeys = argAsLong(request, command.getNumFields());
        List<StrToken> fieldNames = command.getFiledNames();
        String[] fields = new String[fieldNames.size()];
        for (int i = 0; i < fieldNames.size(); i++) {
            fields[i] = argAsString(request, fieldNames.get(i));
        }
        numKeysCheck("HTTL", fields.length, numKeys);

        List<Long> result = jedisCmd.getHashCommands().httl(key, fields);

        receive.responseResult(request, listResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdHRandFieldRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, HRandFieldRedisCmd command, AdapterRequest request,
                                                    AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        Long count = null;
        if (command.getCount() != null) {
            count = argAsLong(request, command.getCount());
        }

        if (count != null) {
            if (command.isWithValues()) {
                List<Map.Entry<String, String>> result = jedisCmd.getHashCommands().hrandfieldWithValues(key, count);
                return resultFiledValueList1(sync, request, receive, result);
            } else {
                List<String> result = jedisCmd.getHashCommands().hrandfield(key, count);
                receive.responseResult(request, listResult(request, COL_FIELD_STRING, result));
                return completed(sync);
            }
        } else {
            List<String> result = Collections.singletonList(jedisCmd.getHashCommands().hrandfield(key));
            receive.responseResult(request, listResult(request, COL_FIELD_STRING, result));
            return completed(sync);
        }
    }

    public static CgFuture<?> cmdHScanRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, HScanRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String cursor = argAsString(request, command.getCursor());
        String pattern = null;
        Integer count = null;
        if (command.getPattern() != null) {
            pattern = argAsString(request, command.getPattern());
        }
        if (command.getCount() != null) {
            count = argAsInt(request, command.getCount());
        }

        ScanParams scanParams = new ScanParams();
        if (pattern != null) {
            scanParams.match(pattern);
        }
        if (count != null) {
            scanParams.count(count);
        }

        if (command.getNoValues() != null) {
            ScanResult<String> result = jedisCmd.getHashCommands().hscanNoValues(key, cursor, scanParams);
            AdapterResultCursor receiveCur = listFixedColAndResult(request, COL_CURSOR_STRING, result.getCursor(), COL_FIELD_STRING, result.getResult());
            receive.responseResult(request, receiveCur);
            return completed(sync);
        } else {
            ScanResult<Map.Entry<String, String>> result = jedisCmd.getHashCommands().hscan(key, cursor, scanParams);
            return resultFiledValueList2(sync, request, receive, result.getCursor(), result.getResult());
        }
    }

    public static CgFuture<?> cmdHSetRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, HSetRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        List<KeyAndStringToken> kvContexts = command.getKeyValues();
        Map<String, String> data = new LinkedHashMap<>();
        for (KeyAndStringToken kv : kvContexts) {
            String vKey = argAsString(request, kv.getKeyName());
            String vValue = argAsString(request, kv.getStringValue());
            data.put(vKey, vValue);
        }

        long result = jedisCmd.getHashCommands().hset(key, data);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdHSetEXRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, HSetEXRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKey());

        HSetExParams params = new HSetExParams();
        if (command.getFxOpt() != null) {
            switch (command.getFxOpt()) {
                case FNX:
                    params.fnx();
                    break;
                case FXX:
                    params.fxx();
                    break;
            }
        }

        if (command.getKeepTtlTag() != null) {
            params.keepTtl();
        }
        if (command.getTtlOptToken() != null) {
            long ttlValue = argAsLong(request, command.getTtlOptToken().getValue());
            applyTTLType(params, command.getTtlOptToken().getTtlType(), ttlValue);
        }

        int numKeys = argAsInt(request, command.getNumFields());
        List<KeyAndStringToken> kvContexts = command.getKeyValues();
        Map<String, String> data = new LinkedHashMap<>();
        for (KeyAndStringToken kv : kvContexts) {
            String vKey = argAsString(request, kv.getKeyName());
            String vValue = argAsString(request, kv.getStringValue());
            data.put(vKey, vValue);
        }
        numKeysCheck("HSETEX", kvContexts.size(), numKeys);

        long result = jedisCmd.getHashCommands().hsetex(key, params, data);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdHSetNXRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, HSetNXRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String vKey = argAsString(request, command.getFieldName());
        String vValue = argAsString(request, command.getValue());

        long result = jedisCmd.getHashCommands().hsetnx(key, vKey, vValue);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdHStrLenRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, HStrLenRedisCmd command, AdapterRequest request,
                                                 AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String field = argAsString(request, command.getFieldName());

        long value = jedisCmd.getHashCommands().hstrlen(key, field);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, value));
        return completed(sync);
    }

    public static CgFuture<?> cmdHValsRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, HValsRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        List<String> value = jedisCmd.getHashCommands().hvals(key);

        receive.responseResult(request, listResult(request, COL_VALUE_STRING, value));
        return completed(sync);
    }
}
