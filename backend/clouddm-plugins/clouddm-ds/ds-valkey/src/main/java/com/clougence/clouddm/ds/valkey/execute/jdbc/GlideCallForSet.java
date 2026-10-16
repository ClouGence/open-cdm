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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.clougence.sql.redis.parser.ast.commands.set.*;
import com.clougence.sql.redis.parser.ast.token.StrToken;
import com.clougence.drivers.adapter.AdapterReceive;
import com.clougence.drivers.adapter.AdapterRequest;
import com.clougence.drivers.adapter.AdapterResultCursor;
import com.clougence.utils.future.CgFuture;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class GlideCallForSet extends GlideUtils {

    public static CgFuture<?> cmdSAddRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, SAddRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        List<StrToken> elementList = command.getMembers();
        String[] member = new String[elementList.size()];
        for (int i = 0; i < elementList.size(); i++) {
            member[i] = argAsString(request, elementList.get(i));
        }
        long result = glideCmd.sadd(key, member);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdSCardRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, SCardRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        long result = glideCmd.scard(key);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdSDiffRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, SDiffRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        List<StrToken> contextList = command.getKeyNames();
        String[] keys = new String[contextList.size()];
        for (int i = 0; i < contextList.size(); i++) {
            keys[i] = argAsString(request, contextList.get(i));
        }

        Set<String> result = glideCmd.sdiff(keys);

        receive.responseResult(request, listResult(request, COL_ELEMENT_STRING, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdSDiffStoreRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, SDiffStoreRedisCmd command, AdapterRequest request,
                                                    AdapterReceive receive) throws SQLException {
        String dst = argAsString(request, command.getDstKey());

        List<StrToken> nameContexts = command.getKeys();
        String[] keys = new String[nameContexts.size()];
        for (int i = 0; i < nameContexts.size(); i++) {
            keys[i] = argAsString(request, nameContexts.get(i));
        }

        long result = glideCmd.sdiffstore(dst, keys);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdSInterRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, SInterRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        List<StrToken> nameContexts = command.getKeyNames();
        String[] keys = new String[nameContexts.size()];
        for (int i = 0; i < nameContexts.size(); i++) {
            keys[i] = argAsString(request, nameContexts.get(i));
        }

        Set<String> result = glideCmd.sinter(keys);

        receive.responseResult(request, listResult(request, COL_ELEMENT_STRING, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdSInterCardRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, SInterCardRedisCmd command, AdapterRequest request,
                                                    AdapterReceive receive) throws SQLException {
        int numKeys = argAsInt(request, command.getNumKeys());
        List<StrToken> kvContexts = command.getKeys();
        String[] keys = new String[kvContexts.size()];
        for (int i = 0; i < kvContexts.size(); i++) {
            keys[i] = argAsString(request, kvContexts.get(i));
        }
        numKeysCheck("SINTERCARD", keys.length, numKeys);

        Set<String> result = glideCmd.sinter(keys);

        receive.responseResult(request, listResult(request, COL_ELEMENT_STRING, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdSinterStoreRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, SinterStoreRedisCmd command, AdapterRequest request,
                                                     AdapterReceive receive) throws SQLException {
        String dstKey = argAsString(request, command.getDst());

        List<StrToken> kvContexts = command.getKeys();
        String[] keys = new String[kvContexts.size()];
        for (int i = 0; i < kvContexts.size(); i++) {
            keys[i] = argAsString(request, kvContexts.get(i));
        }

        long result = glideCmd.sinterstore(dstKey, keys);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdSISMemberRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, SISMemberRedisCmd command, AdapterRequest request,
                                                   AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String member = argAsString(request, command.getMember());

        boolean result = glideCmd.sismember(key, member);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result ? 1 : 0));
        return completed(sync);
    }

    public static CgFuture<?> cmdSMembersRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, SMembersRedisCmd command, AdapterRequest request,
                                                  AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        Set<String> result = glideCmd.smembers(key);

        receive.responseResult(request, listResult(request, COL_ELEMENT_STRING, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdSMISMemberRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, SMISMemberRedisCmd command, AdapterRequest request,
                                                    AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        List<StrToken> kvContexts = command.getMembers();
        String[] member = new String[kvContexts.size()];
        for (int i = 0; i < kvContexts.size(); i++) {
            member[i] = argAsString(request, kvContexts.get(i));
        }

        Boolean[] result = glideCmd.smismember(key, member);

        receive.responseResult(request, listResult(request, COL_RESULT_BOOLEAN, Arrays.asList(result)));
        return completed(sync);
    }

    public static CgFuture<?> cmdSMoveRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, SMoveRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        String srcKey = argAsString(request, command.getSrc());
        String dstKey = argAsString(request, command.getDst());
        String member = argAsString(request, command.getMember());

        boolean result = glideCmd.smove(srcKey, dstKey, member);

        receive.responseResult(request, singleResult(request, COL_RESULT_BOOLEAN, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdSPopRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, SPopRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        Long count = null;
        if (command.getCount() != null) {
            count = argAsLong(request, command.getCount());
        }

        Collection<String> result;
        if (count != null) {
            throw new UnsupportedOperationException("SPOP with count Unsupported by GLIDE.");
        } else {
            result = Collections.singletonList(glideCmd.spop(key));
        }

        receive.responseResult(request, listResult(request, COL_ELEMENT_STRING, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdSRandMemberRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, SRandMemberRedisCmd command, AdapterRequest request,
                                                     AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        Integer count = null;
        if (command.getCount() != null) {
            count = argAsInteger(request, command.getCount());
        }

        Collection<String> result;
        if (count != null) {
            result = Arrays.asList(glideCmd.srandmember(key, count.longValue()));
        } else {
            result = Collections.singletonList(glideCmd.srandmember(key));
        }

        receive.responseResult(request, listResult(request, COL_ELEMENT_STRING, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdSRemRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, SRemRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        List<StrToken> kvContexts = command.getMembers();
        String[] member = new String[kvContexts.size()];
        for (int i = 0; i < kvContexts.size(); i++) {
            member[i] = argAsString(request, kvContexts.get(i));
        }

        long result = glideCmd.srem(key, member);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdSSCanRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, SSCanRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String cursor = argAsString(request, command.getCursor());
        String pattern = null;
        if (command.getPattern() != null) {
            pattern = argAsString(request, command.getPattern());
        }
        Integer count = null;
        if (command.getCount() != null) {
            count = argAsInteger(request, command.getCount());
        }

        Object[] result = glideCmd.sscan(key, cursor, pattern, count == null ? null : count.longValue());

        if (!sync.isDone()) {
            List<String> members = result == null ? Collections.emptyList() : toStringList(result[1]);
            AdapterResultCursor receiveCur = listFixedColAndResult(request, COL_CURSOR_STRING, result == null ? "0" : result[0], COL_ELEMENT_STRING, members);
            receive.responseResult(request, receiveCur);
            return completed(sync);
        } else {
            SQLException err = new SQLException("command interrupted.");
            receive.responseFailed(request, err);
            throw err;
        }
    }

    public static CgFuture<?> cmdSUnionRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, SUnionRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        List<StrToken> kvContexts = command.getKeyNames();
        String[] keys = new String[kvContexts.size()];
        for (int i = 0; i < kvContexts.size(); i++) {
            keys[i] = argAsString(request, kvContexts.get(i));
        }

        Set<String> result = glideCmd.sunion(keys);

        receive.responseResult(request, listResult(request, COL_ELEMENT_STRING, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdSUnionStoreRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, SUnionStoreRedisCmd command, AdapterRequest request,
                                                     AdapterReceive receive) throws SQLException {
        String dstKey = argAsString(request, command.getDst());
        List<StrToken> kvContexts = command.getKeys();
        String[] keys = new String[kvContexts.size()];
        for (int i = 0; i < kvContexts.size(); i++) {
            keys[i] = argAsString(request, kvContexts.get(i));
        }

        long result = glideCmd.sunionstore(dstKey, keys);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }
}
