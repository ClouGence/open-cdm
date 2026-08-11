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
import java.util.*;

import com.clougence.sql.redis.parser.ast.commands.hash.*;
import com.clougence.sql.redis.parser.ast.token.KeyAndStringToken;
import com.clougence.sql.redis.parser.ast.token.StrToken;
import com.clougence.sql.redis.parser.ast.token.TtlOptToken;
import com.clougence.drivers.adapter.AdapterReceive;
import com.clougence.drivers.adapter.AdapterRequest;
import com.clougence.drivers.adapter.AdapterResultCursor;
import com.clougence.drivers.adapter.JdbcErrorCode;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.future.CgFuture;

import glide.api.models.commands.ExpireOptions;
import glide.api.models.commands.ExpirySet;
import glide.api.models.commands.HGetExExpiry;
import glide.api.models.commands.HGetExOptions;
import glide.api.models.commands.HSetExOptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class GlideCallForHash extends GlideUtils {

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

    private static HGetExExpiry applyHGetExTTL(TtlOptToken.TtlType ttlType, long value) throws SQLException {
        switch (ttlType) {
            case EX:
                return HGetExExpiry.Seconds(value);
            case PX:
                return HGetExExpiry.Milliseconds(value);
            case EXAT:
                return HGetExExpiry.UnixSeconds(value);
            case PXAT:
                return HGetExExpiry.UnixMilliseconds(value);
            default:
                throw new SQLException("ttlType(" + ttlType.name() + ") not support.", JdbcErrorCode.SQL_STATE_ILLEGAL_ARGUMENT);
        }
    }

    private static ExpirySet applyHSetExTTL(TtlOptToken.TtlType ttlType, long value) throws SQLException {
        switch (ttlType) {
            case EX:
                return ExpirySet.Seconds(value);
            case PX:
                return ExpirySet.Milliseconds(value);
            case EXAT:
                return ExpirySet.UnixSeconds(value);
            case PXAT:
                return ExpirySet.UnixMilliseconds(value);
            default:
                throw new SQLException("ttlType(" + ttlType.name() + ") not support.", JdbcErrorCode.SQL_STATE_ILLEGAL_ARGUMENT);
        }
    }

    //

    public static CgFuture<?> cmdHDelRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, HDelRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        List<StrToken> fieldNames = command.getFieldName();
        String[] fields = new String[fieldNames.size()];
        for (int i = 0; i < fieldNames.size(); i++) {
            fields[i] = argAsString(request, fieldNames.get(i));
        }

        long result = glideCmd.hdel(key, fields);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdHExistsRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, HExistsRedisCmd command, AdapterRequest request,
                                                 AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String field = argAsString(request, command.getFieldName());

        boolean result = glideCmd.hexists(key, field);

        receive.responseResult(request, singleResult(request, COL_RESULT_BOOLEAN, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdHExpireRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, HExpireRedisCmd command, AdapterRequest request,
                                                 AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKey());
        long seconds = argAsLong(request, command.getTimeout());

        ExpireOptions option = null;
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

        Long[] result = glideCmd.hexpire(key, seconds, option, fields);

        receive.responseResult(request, listResult(request, COL_RESULT_LONG, Arrays.asList(result)));
        return completed(sync);
    }

    public static CgFuture<?> cmdHExpireAtRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, HExpireAtRedisCmd command, AdapterRequest request,
                                                   AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKey());
        long seconds = argAsLong(request, command.getTimeout());

        ExpireOptions option = null;
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

        Long[] result = glideCmd.hexpireAt(key, seconds, option, fields);

        receive.responseResult(request, listResult(request, COL_RESULT_LONG, Arrays.asList(result)));
        return completed(sync);
    }

    public static CgFuture<?> cmdHExpireTimeRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, HExpireTimeRedisCmd command, AdapterRequest request,
                                                     AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKey());
        int numKeys = argAsInt(request, command.getNumFields());
        List<StrToken> filedNames = command.getFiledNames();
        String[] fields = new String[filedNames.size()];
        for (int i = 0; i < filedNames.size(); i++) {
            fields[i] = argAsString(request, filedNames.get(i));
        }
        numKeysCheck("HEXPIRETIME", fields.length, numKeys);

        Long[] result = glideCmd.hexpireTime(key, fields);

        receive.responseResult(request, listResult(request, COL_RESULT_LONG, Arrays.asList(result)));
        return completed(sync);
    }

    public static CgFuture<?> cmdHGetRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, HGetRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String field = argAsString(request, command.getFieldName());

        String result = glideCmd.hget(key, field);

        receive.responseResult(request, singleResult(request, COL_VALUE_STRING, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdHGetAllRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, HGetAllRedisCmd command, AdapterRequest request,
                                                 AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        Map<String, String> result = glideCmd.hgetall(key);

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

    public static CgFuture<?> cmdHGetDelRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, HGetDelRedisCmd command, AdapterRequest request,
                                                 AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKey());
        int numKeys = argAsInt(request, command.getNumFields());
        List<StrToken> filedNames = command.getFiledNames();
        String[] fields = new String[filedNames.size()];
        for (int i = 0; i < filedNames.size(); i++) {
            fields[i] = argAsString(request, filedNames.get(i));
        }
        numKeysCheck("HGETDEL", fields.length, numKeys);

        throw new UnsupportedOperationException("HGETDEL Unsupported by GLIDE.");
    }

    public static CgFuture<?> cmdHGetEXRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, HGetEXRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKey());
        int numKeys = argAsInt(request, command.getNumFields());
        List<StrToken> filedNames = command.getFieldNames();
        String[] fields = new String[filedNames.size()];
        for (int i = 0; i < filedNames.size(); i++) {
            fields[i] = argAsString(request, filedNames.get(i));
        }
        numKeysCheck("HGETEX", fields.length, numKeys);

        HGetExOptions options = null;
        if (command.getPersistTag() != null) {
            options = HGetExOptions.builder().expiry(HGetExExpiry.Persist()).build();
        }
        if (command.getTtlOptToken() != null) {
            long ttlValue = argAsLong(request, command.getTtlOptToken().getValue());
            options = HGetExOptions.builder().expiry(applyHGetExTTL(command.getTtlOptToken().getTtlType(), ttlValue)).build();
        }

        String[] result = glideCmd.hgetex(key, options, fields);

        receive.responseResult(request, listResult(request, COL_RESULT_STRING, Arrays.asList(result)));
        return completed(sync);
    }

    public static CgFuture<?> cmdHIncrByRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, HIncrByRedisCmd command, AdapterRequest request,
                                                 AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String field = argAsString(request, command.getFiledName());
        long increment = argAsLong(request, command.getFloatValue());

        long value = glideCmd.hincrBy(key, field, increment);

        receive.responseResult(request, singleResult(request, COL_VALUE_LONG, value));
        return completed(sync);
    }

    public static CgFuture<?> cmdHIncrByFloatRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, HIncrByFloatRedisCmd command, AdapterRequest request,
                                                      AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String field = argAsString(request, command.getFiledName());
        double increment = argAsDouble(request, command.getFloatValue());

        double value = glideCmd.hincrByFloat(key, field, increment);

        receive.responseResult(request, singleResult(request, COL_VALUE_DOUBLE, value));
        return completed(sync);
    }

    public static CgFuture<?> cmdHKeysRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, HKeysRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        String[] value = glideCmd.hkeys(key);

        receive.responseResult(request, listResult(request, COL_FIELD_STRING, Arrays.asList(value)));
        return completed(sync);
    }

    public static CgFuture<?> cmdHLenRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, HLenRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        long result = glideCmd.hlen(key);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdHMGetRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, HMGetRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        List<StrToken> fieldNames = command.getFieldName();
        String[] fields = new String[fieldNames.size()];
        for (int i = 0; i < fieldNames.size(); i++) {
            fields[i] = argAsString(request, fieldNames.get(i));
        }

        String[] result = glideCmd.hmget(key, fields);

        receive.responseResult(request, listResult(request, COL_VALUE_STRING, Arrays.asList(result)));
        return completed(sync);
    }

    public static CgFuture<?> cmdHMSetRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, HMSetRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        List<KeyAndStringToken> kvContexts = command.getKeyValues();
        Map<String, String> data = new LinkedHashMap<>();
        for (KeyAndStringToken kv : kvContexts) {
            String vKey = argAsString(request, kv.getKeyName());
            String vValue = argAsString(request, kv.getStringValue());
            data.put(vKey, vValue);
        }

        long result = glideCmd.hmset(key, data);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdHPersistRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, HPersistRedisCmd command, AdapterRequest request,
                                                  AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKey());
        int numKeys = argAsInt(request, command.getNumFields());

        List<StrToken> fieldNames = command.getFiledNames();
        String[] fields = new String[fieldNames.size()];
        for (int i = 0; i < fieldNames.size(); i++) {
            fields[i] = argAsString(request, fieldNames.get(i));
        }
        numKeysCheck("HPERSIST", fields.length, numKeys);

        Long[] result = glideCmd.hpersist(key, fields);

        receive.responseResult(request, listResult(request, COL_RESULT_LONG, Arrays.asList(result)));
        return completed(sync);
    }

    public static CgFuture<?> cmdHPExpireRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, HPExpireRedisCmd command, AdapterRequest request,
                                                  AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKey());
        long timeout = argAsLong(request, command.getTimeout());

        ExpireOptions option = null;
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

        Long[] result = glideCmd.hpexpire(key, timeout, option, fields);

        receive.responseResult(request, listResult(request, COL_RESULT_LONG, Arrays.asList(result)));
        return completed(sync);
    }

    public static CgFuture<?> cmdHPExpireAtRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, HPExpireAtRedisCmd command, AdapterRequest request,
                                                    AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKey());
        long timeout = argAsLong(request, command.getTimeout());

        ExpireOptions option = null;
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

        Long[] result = glideCmd.hpexpireAt(key, timeout, option, fields);

        receive.responseResult(request, listResult(request, COL_RESULT_LONG, Arrays.asList(result)));
        return completed(sync);
    }

    public static CgFuture<?> cmdHPExpireTimeRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, HPExpireTimeRedisCmd command, AdapterRequest request,
                                                      AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKey());
        long numKeys = argAsLong(request, command.getNumFields());
        List<StrToken> fieldNames = command.getFiledNames();
        String[] fields = new String[fieldNames.size()];
        for (int i = 0; i < fieldNames.size(); i++) {
            fields[i] = argAsString(request, fieldNames.get(i));
        }
        numKeysCheck("HPEXPIRETIME", fields.length, numKeys);

        Long[] result = glideCmd.hpexpireTime(key, fields);

        receive.responseResult(request, listResult(request, COL_RESULT_LONG, Arrays.asList(result)));
        return completed(sync);
    }

    public static CgFuture<?> cmdHPTtlRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, HPTtlRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKey());
        long numKeys = argAsLong(request, command.getNumFields());
        List<StrToken> fieldNames = command.getFiledNames();
        String[] fields = new String[fieldNames.size()];
        for (int i = 0; i < fieldNames.size(); i++) {
            fields[i] = argAsString(request, fieldNames.get(i));
        }
        numKeysCheck("HPTTL", fields.length, numKeys);

        Long[] result = glideCmd.hpttl(key, fields);

        receive.responseResult(request, listResult(request, COL_RESULT_LONG, Arrays.asList(result)));
        return completed(sync);
    }

    public static CgFuture<?> cmdHTtlRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, HTtlRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKey());
        long numKeys = argAsLong(request, command.getNumFields());
        List<StrToken> fieldNames = command.getFiledNames();
        String[] fields = new String[fieldNames.size()];
        for (int i = 0; i < fieldNames.size(); i++) {
            fields[i] = argAsString(request, fieldNames.get(i));
        }
        numKeysCheck("HTTL", fields.length, numKeys);

        Long[] result = glideCmd.httl(key, fields);

        receive.responseResult(request, listResult(request, COL_RESULT_LONG, Arrays.asList(result)));
        return completed(sync);
    }

    public static CgFuture<?> cmdHRandFieldRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, HRandFieldRedisCmd command, AdapterRequest request,
                                                    AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        Long count = null;
        if (command.getCount() != null) {
            count = argAsLong(request, command.getCount());
        }

        if (count != null) {
            throw new UnsupportedOperationException("HRANDFIELD with count Unsupported by GLIDE.");
        } else {
            List<String> result = Collections.singletonList(glideCmd.hrandfield(key));
            receive.responseResult(request, listResult(request, COL_FIELD_STRING, result));
            return completed(sync);
        }
    }

    public static CgFuture<?> cmdHScanRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, HScanRedisCmd command, AdapterRequest request,
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

        Object[] result = glideCmd.hscan(key, cursor, pattern, count == null ? null : count.longValue());

        if (command.getNoValues() != null) {
            List<String> fields = result == null ? Collections.emptyList() : toStringList(result[1]);
            AdapterResultCursor receiveCur = listFixedColAndResult(request, COL_CURSOR_STRING, result == null ? "0" : result[0], COL_FIELD_STRING, fields);
            receive.responseResult(request, receiveCur);
            return completed(sync);
        } else {
            List<Map.Entry<String, String>> entries = new ArrayList<>();
            String[] fieldValues = result == null ? new String[0] : toStringList(result[1]).toArray(new String[0]);
            for (int i = 0; i + 1 < fieldValues.length; i += 2) {
                entries.add(new AbstractMap.SimpleEntry<>(fieldValues[i], fieldValues[i + 1]));
            }
            return resultFiledValueList2(sync, request, receive, result == null ? "0" : (String) result[0], entries);
        }
    }

    public static CgFuture<?> cmdHSetRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, HSetRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        List<KeyAndStringToken> kvContexts = command.getKeyValues();
        Map<String, String> data = new LinkedHashMap<>();
        for (KeyAndStringToken kv : kvContexts) {
            String vKey = argAsString(request, kv.getKeyName());
            String vValue = argAsString(request, kv.getStringValue());
            data.put(vKey, vValue);
        }

        long result = glideCmd.hset(key, data);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdHSetEXRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, HSetEXRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKey());

        HSetExOptions.HSetExOptionsBuilder optionsBuilder = HSetExOptions.builder();
        if (command.getFxOpt() != null) {
            switch (command.getFxOpt()) {
                case FNX:
                    optionsBuilder.onlyIfNoneExist();
                    break;
                case FXX:
                    optionsBuilder.onlyIfAllExist();
                    break;
            }
        }

        if (command.getKeepTtlTag() != null) {
            optionsBuilder.expiry(ExpirySet.KeepExisting());
        }
        if (command.getTtlOptToken() != null) {
            long ttlValue = argAsLong(request, command.getTtlOptToken().getValue());
            optionsBuilder.expiry(applyHSetExTTL(command.getTtlOptToken().getTtlType(), ttlValue));
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

        long result = glideCmd.hsetex(key, data, optionsBuilder.build());

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdHSetNXRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, HSetNXRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String vKey = argAsString(request, command.getFieldName());
        String vValue = argAsString(request, command.getValue());

        boolean result = glideCmd.hsetnx(key, vKey, vValue);

        receive.responseResult(request, singleResult(request, COL_RESULT_BOOLEAN, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdHStrLenRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, HStrLenRedisCmd command, AdapterRequest request,
                                                 AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String field = argAsString(request, command.getFieldName());

        long value = glideCmd.hstrlen(key, field);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, value));
        return completed(sync);
    }

    public static CgFuture<?> cmdHValsRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, HValsRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        String[] value = glideCmd.hvals(key);

        receive.responseResult(request, listResult(request, COL_VALUE_STRING, Arrays.asList(value)));
        return completed(sync);
    }
}
