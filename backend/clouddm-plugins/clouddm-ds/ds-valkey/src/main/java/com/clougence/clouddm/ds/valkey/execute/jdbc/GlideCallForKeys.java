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

import com.clougence.sql.redis.parser.ast.commands.keys.*;
import com.clougence.sql.redis.parser.ast.token.OrderType;
import com.clougence.sql.redis.parser.ast.token.StrToken;
import com.clougence.drivers.adapter.AdapterReceive;
import com.clougence.drivers.adapter.AdapterRequest;
import com.clougence.drivers.adapter.AdapterResultCursor;
import com.clougence.drivers.adapter.JdbcErrorCode;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.future.CgFuture;

import glide.api.models.commands.ExpireOptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class GlideCallForKeys extends GlideUtils {

    public static CgFuture<?> cmdDelRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, DelRedisCmd redisCmd, AdapterRequest request, AdapterReceive receive) throws SQLException {
        List<StrToken> keyNames = redisCmd.getKeyNames();
        String[] keys = new String[keyNames.size()];
        for (int i = 0; i < keyNames.size(); i++) {
            keys[i] = argAsString(request, keyNames.get(i));
        }

        long result = glideCmd.del(keys);

        receive.responseResult(request, singleResult(request, COL_VALUE_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdDumpRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, DumpRedisCmd redisCmd, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String keyName = argAsString(request, redisCmd.getKeyName());

        byte[] dump = glideCmd.dump(keyName);

        receive.responseResult(request, singleResult(request, COL_VALUE_BYTES, dump));
        return completed(sync);
    }

    public static CgFuture<?> cmdExistsRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, ExistsRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        List<StrToken> keysList = command.getKeyNames();
        String[] keys = new String[keysList.size()];
        for (int i = 0; i < keysList.size(); i++) {
            keys[i] = argAsString(request, keysList.get(i));
        }
        long found = glideCmd.exists(keys);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, found));
        return completed(sync);
    }

    public static CgFuture<?> cmdExpireRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, ExpireRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        int seconds = argAsInt(request, command.getSecondsSec());

        boolean result;
        if (command.getKeyOpt() != null) {
            ExpireOptions option = convertToExpiryOption(command.getKeyOpt().getOptionType());
            result = glideCmd.expire(key, seconds, option);
        } else {
            result = glideCmd.expire(key, seconds);
        }

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdExpireAtRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, ExpireAtRedisCmd command, AdapterRequest request,
                                                  AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        long unixTimestamp = argAsLong(request, command.getUnixTimestampSec());

        boolean result;
        if (command.getKeyOpt() != null) {
            ExpireOptions option = convertToExpiryOption(command.getKeyOpt().getOptionType());
            result = glideCmd.expireAt(key, unixTimestamp, option);
        } else {
            result = glideCmd.expireAt(key, unixTimestamp);
        }

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdExpireTimeRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, ExpireTimeRedisCmd command, AdapterRequest request,
                                                    AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        long result = glideCmd.expireTime(key);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdKeysRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, KeysRedisCmd redisCmd, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String pattern = argAsString(request, redisCmd.getPattern());

        int affectRows = 0;
        long maxRows = request.getMaxRows();
        int fetchSize = request.getFetchSize();
        String cursor = "0";

        AdapterResultCursor receiveCur = new AdapterResultCursor(request, Collections.singletonList(COL_KEY_STRING));
        receive.responseResult(request, receiveCur);

        while (!sync.isDone()) {
            Object[] result = glideCmd.scan(cursor, pattern, fetchSize > 0 ? (long) fetchSize : null);
            cursor = (String) result[0];
            List<String> list = toStringList(result[1]);
            boolean breakWhile = "0".equals(cursor);

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

    public static CgFuture<?> cmdMoveRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, MoveRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String keyName = argAsString(request, command.getKeyName());
        int destDataBase = argAsInt(request, command.getDestDatabase());

        boolean result = glideCmd.move(keyName, destDataBase);

        receive.responseResult(request, singleResult(request, COL_VALUE_BOOLEAN, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdObjectRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, ObjectRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        switch (command.getOperation()) {
            case ENCODING: {
                String result = glideCmd.objectEncoding(key);
                receive.responseResult(request, singleResult(request, COL_RESULT_STRING, result));
                break;
            }
            case FREQ: {
                Long result = glideCmd.objectFreq(key);
                receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
                break;
            }
            case IDLETIME: {
                Long result = glideCmd.objectIdletime(key);
                receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
            }
            case REFCOUNT: {
                Long result = glideCmd.objectRefcount(key);
                receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
            }
            default: {
                throw new SQLException("object options(" + command.getOperation().name() + ") not support.", JdbcErrorCode.SQL_STATE_ILLEGAL_ARGUMENT);
            }
        }

        return completed(sync);
    }

    public static CgFuture<?> cmdPersistRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, PersistRedisCmd command, AdapterRequest request,
                                                 AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        boolean result = glideCmd.persist(key);

        receive.responseResult(request, singleResult(request, COL_RESULT_BOOLEAN, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdPExpireRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, PExpireRedisCmd command, AdapterRequest request,
                                                 AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        long milliSeconds = argAsLong(request, command.getMilliseconds());

        ExpireOptions option = null;
        if (command.getKeyOpt() != null) {
            option = convertToExpiryOption(command.getKeyOpt().getOptionType());
        }

        boolean result;
        if (option != null) {
            result = glideCmd.pexpire(key, milliSeconds, option);
        } else {
            result = glideCmd.pexpire(key, milliSeconds);
        }

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdPExpireAtRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, PExpireAtRedisCmd command, AdapterRequest request,
                                                   AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        long unixTimeSeconds = argAsLong(request, command.getUnixTimeSeconds());

        ExpireOptions option = null;
        if (command.getKeyOpt() != null) {
            option = convertToExpiryOption(command.getKeyOpt().getOptionType());
        }

        boolean result;
        if (option != null) {
            result = glideCmd.pexpireAt(key, unixTimeSeconds, option);
        } else {
            result = glideCmd.pexpireAt(key, unixTimeSeconds);
        }

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdPExpireTimeRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, PExpireTimeRedisCmd command, AdapterRequest request,
                                                     AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        long result = glideCmd.pexpireTime(key);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdPTTLRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, PTTLRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        long result = glideCmd.pttl(key);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdTTLTimeRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, TTLRedisCmd command, AdapterRequest request,
                                                 AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        long result = glideCmd.ttl(key);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdRandomKeyRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, RandomKeyRedisCmd command, AdapterRequest request,
                                                   AdapterReceive receive) throws SQLException {
        String result = glideCmd.randomKey();

        receive.responseResult(request, singleResult(request, COL_KEY_STRING, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdRenameRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, RenameRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String oldKey = argAsString(request, command.getOldKey());
        String newKey = argAsString(request, command.getNewKey());

        String result = glideCmd.rename(oldKey, newKey);

        receive.responseResult(request, singleResult(request, COL_STATUS_STRING, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdRenameNXRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, RenameNXRedisCmd command, AdapterRequest request,
                                                  AdapterReceive receive) throws SQLException {
        String oldKey = argAsString(request, command.getOldKey());
        String newKey = argAsString(request, command.getNewKey());

        boolean result = glideCmd.renamenx(oldKey, newKey);

        receive.responseResult(request, singleResult(request, COL_RESULT_BOOLEAN, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdRestoreRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, RestoreRedisCmd command, AdapterRequest request,
                                                 AdapterReceive receive) throws SQLException {
        String keyName = argAsString(request, command.getKeyName());
        long ttl = argAsLong(request, command.getTtl());
        byte[] serialized = argAsBytes(request, command.getSerializedValue());

        String result = glideCmd.restore(keyName, ttl, serialized);

        receive.responseResult(request, singleResult(request, COL_STATUS_STRING, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdScanRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, ScanRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
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

        Long scanCount = count != null ? count.longValue() : (request.getMaxRows() > 0 ? request.getMaxRows() : null);

        Object[] result = glideCmd.scan(cursor, pattern, scanCount);

        if (!sync.isDone()) {
            List<String> keys = result == null ? Collections.emptyList() : toStringList(result[1]);
            AdapterResultCursor receiveCur = listFixedColAndResult(request, COL_CURSOR_STRING, result == null ? "0" : result[0], COL_KEY_STRING, keys);
            receive.responseResult(request, receiveCur);
            return completed(sync);
        } else {
            SQLException err = new SQLException("command interrupted.");
            receive.responseFailed(request, err);
            throw err;
        }
    }

    public static CgFuture<?> cmdSortRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, SortRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        String[] result = glideCmd.sort(key);

        receive.responseResult(request, listResult(request, COL_ELEMENT_STRING, Arrays.asList(result)));
        return completed(sync);
    }

    public static CgFuture<?> cmdSortRORedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, SortRORedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        String[] result = glideCmd.sort(key);

        receive.responseResult(request, listResult(request, COL_ELEMENT_STRING, Arrays.asList(result)));
        return completed(sync);
    }

    public static CgFuture<?> cmdTouchRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, TouchRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        List<StrToken> keyNameContexts = command.getKeyNames();
        String[] keys = new String[keyNameContexts.size()];
        for (int i = 0; i < keyNameContexts.size(); i++) {
            keys[i] = argAsString(request, keyNameContexts.get(i));
        }

        long result = glideCmd.touch(keys);

        receive.responseResult(request, singleResult(request, COL_STATUS_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdTypeRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, TypeRedisCmd redisCmd, AdapterRequest request, AdapterReceive receive) throws SQLException {
        List<StrToken> keyNames = redisCmd.getKeyName();
        String[] keys = new String[keyNames.size()];
        for (int i = 0; i < keyNames.size(); i++) {
            keys[i] = argAsString(request, keyNames.get(i));
        }

        AdapterResultCursor receiveCur = new AdapterResultCursor(request, Arrays.asList(COL_KEY_STRING, COL_TYPE_STRING));
        for (String key : keys) {
            String type = glideCmd.type(key);
            receiveCur.pushData(CollectionUtils.asMap(//
                    COL_KEY_STRING.name, key,         //
                    COL_TYPE_STRING.name, type        //
            ));
        }
        receiveCur.pushFinish();
        receive.responseResult(request, receiveCur);
        return completed(sync);
    }

    public static CgFuture<?> cmdUnlinkRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, UnlinkRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        List<StrToken> keyNames = command.getKeyNames();
        String[] keys = new String[keyNames.size()];
        for (int i = 0; i < keyNames.size(); i++) {
            keys[i] = argAsString(request, keyNames.get(i));
        }

        long result = glideCmd.unlink(keys);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }
}
