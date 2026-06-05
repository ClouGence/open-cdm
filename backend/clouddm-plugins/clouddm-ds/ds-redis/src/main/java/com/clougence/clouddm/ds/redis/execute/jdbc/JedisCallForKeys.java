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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.ds.redis.parser.ast.commands.keys.*;
import com.clougence.clouddm.ds.redis.parser.ast.token.OrderType;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.drivers.adapter.AdapterReceive;
import com.clougence.drivers.adapter.AdapterRequest;
import com.clougence.drivers.adapter.AdapterResultCursor;
import com.clougence.drivers.adapter.JdbcErrorCode;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.future.CgFuture;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.args.ExpiryOption;
import redis.clients.jedis.params.RestoreParams;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.params.SortingParams;
import redis.clients.jedis.resps.ScanResult;

@Slf4j
class JedisCallForKeys extends JedisUtils {

    public static CgFuture<?> cmdDelRedisCmd(CgFuture<Object> sync, JedisCmd jedis, DelRedisCmd redisCmd, AdapterRequest request, AdapterReceive receive) throws SQLException {
        List<StrToken> keyNames = redisCmd.getKeyNames();
        String[] keys = new String[keyNames.size()];
        for (int i = 0; i < keyNames.size(); i++) {
            keys[i] = argAsString(request, keyNames.get(i));
        }

        long result = jedis.getKeyCommands().del(keys);

        receive.responseResult(request, singleResult(request, COL_VALUE_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdDumpRedisCmd(CgFuture<Object> sync, JedisCmd jedis, DumpRedisCmd redisCmd, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String keyName = argAsString(request, redisCmd.getKeyName());

        byte[] dump = jedis.getKeyCommands().dump(keyName);

        receive.responseResult(request, singleResult(request, COL_VALUE_BYTES, dump));
        return completed(sync);
    }

    public static CgFuture<?> cmdExistsRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, ExistsRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        List<StrToken> keysList = command.getKeyNames();
        String[] keys = new String[keysList.size()];
        for (int i = 0; i < keysList.size(); i++) {
            keys[i] = argAsString(request, keysList.get(i));
        }
        long found = jedisCmd.getKeyCommands().exists(keys);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, found));
        return completed(sync);
    }

    public static CgFuture<?> cmdExpireRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, ExpireRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        int seconds = argAsInt(request, command.getSecondsSec());

        long result;
        if (command.getKeyOpt() != null) {
            ExpiryOption option = convertToExpiryOption(command.getKeyOpt().getOptionType());
            result = jedisCmd.getKeyCommands().expire(key, seconds, option);
        } else {
            result = jedisCmd.getKeyCommands().expire(key, seconds);
        }

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdExpireAtRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, ExpireAtRedisCmd command, AdapterRequest request,
                                                  AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        long unixTimestamp = argAsLong(request, command.getUnixTimestampSec());

        long result;
        if (command.getKeyOpt() != null) {
            ExpiryOption option = convertToExpiryOption(command.getKeyOpt().getOptionType());
            result = jedisCmd.getKeyCommands().expireAt(key, unixTimestamp, option);
        } else {
            result = jedisCmd.getKeyCommands().expireAt(key, unixTimestamp);
        }

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdExpireTimeRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, ExpireTimeRedisCmd command, AdapterRequest request,
                                                    AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        long result = jedisCmd.getKeyCommands().expireTime(key);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdKeysRedisCmd(CgFuture<Object> sync, JedisCmd jedis, KeysRedisCmd redisCmd, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String pattern = argAsString(request, redisCmd.getPattern());

        int affectRows = 0;
        long maxRows = request.getMaxRows();
        int fetchSize = request.getFetchSize();
        ScanParams scanParams = new ScanParams();
        scanParams.match(pattern);
        if (fetchSize > 0) {
            scanParams.count(fetchSize);
        }
        String cursor = "0";

        AdapterResultCursor receiveCur = new AdapterResultCursor(request, Collections.singletonList(COL_KEY_STRING));
        receive.responseResult(request, receiveCur);

        while (!sync.isDone()) {
            ScanResult<String> result = jedis.getKeyCommands().scan(cursor, scanParams);
            cursor = result.getCursor();
            List<String> list = result.getResult();
            boolean breakWhile = result.isCompleteIteration();

            for (String key : list) {
                receiveCur.pushData(CollectionUtils.asMap(COL_KEY_STRING.name, key));
                affectRows++;

                if (maxRows > 0 && affectRows >= maxRows) {
                    breakWhile = true;
                    break;
                }

                if (sync.isDone()) {
                    breakWhile = true;
                    break;
                }
            }

            if (breakWhile) {
                break;
            }
        }

        if (!sync.isDone()) {
            receiveCur.pushFinish();
            return completed(sync);
        } else {
            SQLException err = new SQLException("command interrupted.");
            receiveCur.pushFinish();
            receive.responseFailed(request, err);
            throw err;
        }
    }

    public static CgFuture<?> cmdMoveRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, MoveRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String keyName = argAsString(request, command.getKeyName());
        int destDataBase = argAsInt(request, command.getDestDatabase());

        long result = jedisCmd.getDatabaseCommands().move(keyName, destDataBase);

        receive.responseResult(request, singleResult(request, COL_VALUE_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdObjectRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, ObjectRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        switch (command.getOperation()) {
            case ENCODING: {
                String result = jedisCmd.getKeyCommands().objectEncoding(key);
                receive.responseResult(request, singleResult(request, COL_RESULT_STRING, result));
                break;
            }
            case FREQ: {
                Long result = jedisCmd.getKeyCommands().objectFreq(key);
                receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
                break;
            }
            case IDLETIME: {
                Long result = jedisCmd.getKeyCommands().objectIdletime(key);
                receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
            }
            case REFCOUNT: {
                Long result = jedisCmd.getKeyCommands().objectRefcount(key);
                receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
            }
            default: {
                throw new SQLException("object options(" + command.getOperation().name() + ") not support.", JdbcErrorCode.SQL_STATE_ILLEGAL_ARGUMENT);
            }
        }

        return completed(sync);
    }

    public static CgFuture<?> cmdPersistRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, PersistRedisCmd command, AdapterRequest request,
                                                 AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        long result = jedisCmd.getKeyCommands().persist(key);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdPExpireRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, PExpireRedisCmd command, AdapterRequest request,
                                                 AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        long milliSeconds = argAsLong(request, command.getMilliseconds());

        ExpiryOption option = null;
        if (command.getKeyOpt() != null) {
            option = convertToExpiryOption(command.getKeyOpt().getOptionType());
        }

        long result;
        if (option != null) {
            result = jedisCmd.getKeyCommands().pexpire(key, milliSeconds, option);
        } else {
            result = jedisCmd.getKeyCommands().pexpire(key, milliSeconds);
        }

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdPExpireAtRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, PExpireAtRedisCmd command, AdapterRequest request,
                                                   AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        long unixTimeSeconds = argAsLong(request, command.getUnixTimeSeconds());

        ExpiryOption option = null;
        if (command.getKeyOpt() != null) {
            option = convertToExpiryOption(command.getKeyOpt().getOptionType());
        }

        long result;
        if (option != null) {
            result = jedisCmd.getKeyCommands().pexpireAt(key, unixTimeSeconds, option);
        } else {
            result = jedisCmd.getKeyCommands().pexpireAt(key, unixTimeSeconds);
        }

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdPExpireTimeRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, PExpireTimeRedisCmd command, AdapterRequest request,
                                                     AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        long result = jedisCmd.getKeyCommands().pexpireTime(key);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdPTTLRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, PTTLRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        long result = jedisCmd.getKeyCommands().pttl(key);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdTTLTimeRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, TTLRedisCmd command, AdapterRequest request,
                                                 AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        long result = jedisCmd.getKeyCommands().ttl(key);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdRandomKeyRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, RandomKeyRedisCmd command, AdapterRequest request,
                                                   AdapterReceive receive) throws SQLException {
        String result = jedisCmd.getKeyCommands().randomKey();

        receive.responseResult(request, singleResult(request, COL_KEY_STRING, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdRenameRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, RenameRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String oldKey = argAsString(request, command.getOldKey());
        String newKey = argAsString(request, command.getNewKey());

        String result = jedisCmd.getKeyCommands().rename(oldKey, newKey);

        receive.responseResult(request, singleResult(request, COL_STATUS_STRING, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdRenameNXRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, RenameNXRedisCmd command, AdapterRequest request,
                                                  AdapterReceive receive) throws SQLException {
        String oldKey = argAsString(request, command.getOldKey());
        String newKey = argAsString(request, command.getNewKey());

        long result = jedisCmd.getKeyCommands().renamenx(oldKey, newKey);

        receive.responseResult(request, singleResult(request, COL_STATUS_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdRestoreRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, RestoreRedisCmd command, AdapterRequest request,
                                                 AdapterReceive receive) throws SQLException {
        String keyName = argAsString(request, command.getKeyName());
        long ttl = argAsLong(request, command.getTtl());
        byte[] serialized = argAsBytes(request, command.getSerializedValue());

        RestoreParams params = null;
        if (command.getReplace() != null) {
            params = params == null ? new RestoreParams() : params;
            params.replace();
        }
        if (command.getAbsTTL() != null) {
            params = params == null ? new RestoreParams() : params;
            params.absTtl();
        }
        if (command.getIdleTime() != null) {
            params = params == null ? new RestoreParams() : params;
            long idleTime = argAsLong(request, command.getIdleTime());
            params.idleTime(idleTime);
        }
        if (command.getFrequency() != null) {
            params = params == null ? new RestoreParams() : params;
            long frequency = argAsLong(request, command.getFrequency());
            params.frequency(frequency);
        }

        String result;
        if (params == null) {
            result = jedisCmd.getKeyCommands().restore(keyName, ttl, serialized);
        } else {
            result = jedisCmd.getKeyCommands().restore(keyName, ttl, serialized, params);
        }

        receive.responseResult(request, singleResult(request, COL_STATUS_STRING, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdScanRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, ScanRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String cursor = argAsString(request, command.getCursor());
        String pattern = null;
        Integer count = null;
        String type = null;
        if (command.getPattern() != null) {
            pattern = argAsString(request, command.getPattern());
        }
        if (command.getCount() != null) {
            count = argAsInt(request, command.getCount());
        }
        if (command.getType() != null) {
            type = argAsString(request, command.getType());
        }

        ScanParams scanParams = new ScanParams();
        if (pattern != null) {
            scanParams.match(pattern);
        }
        if (count != null) {
            scanParams.count(count);
            request.setMaxRows(count);
        } else {
            if (request.getMaxRows() > 0) {
                scanParams.count(Math.toIntExact(request.getMaxRows()));
            }
        }

        ScanResult<String> result;
        if (type == null) {
            result = jedisCmd.getKeyCommands().scan(cursor, scanParams);
        } else {
            result = jedisCmd.getKeyCommands().scan(cursor, scanParams, type);
        }

        if (!sync.isDone()) {
            AdapterResultCursor receiveCur = listFixedColAndResult(request, COL_CURSOR_STRING, result.getCursor(), COL_KEY_STRING, result.getResult());
            receive.responseResult(request, receiveCur);
            return completed(sync);
        } else {
            SQLException err = new SQLException("command interrupted.");
            receive.responseFailed(request, err);
            throw err;
        }
    }

    public static CgFuture<?> cmdSortRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, SortRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        SortingParams params = null;

        if (command.getByPattern() != null) {
            params = params == null ? new SortingParams() : params;
            String pattern = argAsString(request, command.getByPattern());
            params.by(pattern);
        }

        if (command.getLimitOffset() != null && command.getLimitCount() != null) {
            params = params == null ? new SortingParams() : params;
            int limitOffset = argAsInt(request, command.getLimitOffset());
            int limitCount = argAsInt(request, command.getLimitCount());
            params.limit(limitOffset, limitCount);
        }

        if (command.getGetPatterns() != null) {
            params = params == null ? new SortingParams() : params;
            List<StrToken> getTokens = command.getGetPatterns();
            String[] gets = new String[getTokens.size()];
            for (int i = 0; i < getTokens.size(); i++) {
                gets[i] = argAsString(request, getTokens.get(i));
            }
            params.get(gets);
        }

        if (command.getSortToken() != null) {
            params = params == null ? new SortingParams() : params;
            OrderType sortType = command.getSortToken().getOptionType();
            switch (sortType) {
                case ASC:
                    params.asc();
                    break;
                case DESC:
                    params.desc();
                    break;
                default:
                    throw new SQLException("sort option(" + sortType.name() + ") not support.", JdbcErrorCode.SQL_STATE_ILLEGAL_ARGUMENT);
            }
        }

        if (command.getAlphaTag() != null) {
            params = params == null ? new SortingParams() : params;
            params.alpha();
        }

        String dst = null;
        if (command.getDestination() != null) {
            dst = argAsString(request, command.getDestination());
        }

        if (dst == null) {
            List<String> sort;
            if (params == null) {
                sort = jedisCmd.getKeyCommands().sort(key);
            } else {
                sort = jedisCmd.getKeyCommands().sort(key, params);
            }
            receive.responseResult(request, listResult(request, COL_KEY_STRING, sort));
        } else {
            long sort;
            if (params == null) {
                sort = jedisCmd.getKeyCommands().sort(key, dst);
            } else {
                sort = jedisCmd.getKeyCommands().sort(key, params, dst);
            }
            receive.responseResult(request, singleResult(request, COL_RESULT_LONG, sort));
        }

        return completed(sync);
    }

    public static CgFuture<?> cmdSortRORedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, SortRORedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        SortingParams params = null;

        if (command.getByPattern() != null) {
            params = params == null ? new SortingParams() : params;
            String pattern = argAsString(request, command.getByPattern());
            params.by(pattern);
        }

        if (command.getLimitOffset() != null && command.getLimitCount() != null) {
            params = params == null ? new SortingParams() : params;
            int limitOffset = argAsInt(request, command.getLimitOffset());
            int limitCount = argAsInt(request, command.getLimitCount());
            params.limit(limitOffset, limitCount);
        }

        if (command.getGetPatterns() != null) {
            params = params == null ? new SortingParams() : params;
            List<StrToken> getTokens = command.getGetPatterns();
            String[] gets = new String[getTokens.size()];
            for (int i = 0; i < getTokens.size(); i++) {
                gets[i] = argAsString(request, getTokens.get(i));
            }
            params.get(gets);
        }

        if (command.getSortToken() != null) {
            params = params == null ? new SortingParams() : params;
            OrderType sortType = command.getSortToken().getOptionType();
            switch (sortType) {
                case ASC:
                    params.asc();
                    break;
                case DESC:
                    params.desc();
                    break;
                default:
                    throw new SQLException("sort option(" + sortType.name() + ") not support.", JdbcErrorCode.SQL_STATE_ILLEGAL_ARGUMENT);
            }
        }

        if (command.getAlphaTag() != null) {
            params = params == null ? new SortingParams() : params;
            params.alpha();
        }

        List<String> sort;
        if (params == null) {
            sort = jedisCmd.getKeyCommands().sort(key);
        } else {
            sort = jedisCmd.getKeyCommands().sort(key, params);
        }
        receive.responseResult(request, listResult(request, COL_KEY_STRING, sort));
        return completed(sync);
    }

    public static CgFuture<?> cmdTouchRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, TouchRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        List<StrToken> keyNameContexts = command.getKeyNames();
        String[] keys = new String[keyNameContexts.size()];
        for (int i = 0; i < keyNameContexts.size(); i++) {
            keys[i] = argAsString(request, keyNameContexts.get(i));
        }

        long result = jedisCmd.getKeyCommands().touch(keys);

        receive.responseResult(request, singleResult(request, COL_STATUS_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdTypeRedisCmd(CgFuture<Object> sync, JedisCmd jedis, TypeRedisCmd redisCmd, AdapterRequest request, AdapterReceive receive) throws SQLException {
        List<StrToken> keyNames = redisCmd.getKeyName();
        String[] keys = new String[keyNames.size()];
        for (int i = 0; i < keyNames.size(); i++) {
            keys[i] = argAsString(request, keyNames.get(i));
        }

        AdapterResultCursor receiveCur = new AdapterResultCursor(request, Arrays.asList(COL_KEY_STRING, COL_TYPE_STRING));
        for (String key : keys) {
            String type = jedis.getKeyCommands().type(key);
            receiveCur.pushData(CollectionUtils.asMap(//
                    COL_KEY_STRING.name, key,         //
                    COL_TYPE_STRING.name, type        //
            ));
        }
        receiveCur.pushFinish();
        receive.responseResult(request, receiveCur);
        return completed(sync);
    }

    public static CgFuture<?> cmdUnlinkRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, UnlinkRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        List<StrToken> keyNames = command.getKeyNames();
        String[] keys = new String[keyNames.size()];
        for (int i = 0; i < keyNames.size(); i++) {
            keys[i] = argAsString(request, keyNames.get(i));
        }

        long result = jedisCmd.getKeyCommands().unlink(keys);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }
}
