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
import java.util.*;

import com.clougence.clouddm.ds.redis.parser.ast.commands.sortedset.*;
import com.clougence.clouddm.ds.redis.parser.ast.token.IntToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.ScoreAndMemberToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.ScoreLex;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.drivers.adapter.*;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.future.CgFuture;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.args.SortedSetOption;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.params.ZAddParams;
import redis.clients.jedis.params.ZParams;
import redis.clients.jedis.params.ZRangeParams;
import redis.clients.jedis.resps.ScanResult;
import redis.clients.jedis.resps.Tuple;
import redis.clients.jedis.util.KeyValue;

@Slf4j
class JedisCallForSortedSet extends JedisUtils {

    public static CgFuture<?> cmdBZMPopRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, BZMPopRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        double timeout = argAsDouble(request, command.getTimeout());
        int numKeys = argAsInt(request, command.getNumKeys());
        List<StrToken> keyList = command.getKeyName();
        String[] keys = new String[keyList.size()];
        for (int i = 0; i < keyList.size(); i++) {
            keys[i] = argAsString(request, keyList.get(i));
        }
        numKeysCheck("BZMPOP", keys.length, numKeys);

        SortedSetOption option = convertToSortedSetOption(command.getMinMax());
        Integer count = null;
        if (command.getCount() != null) {
            count = argAsInt(request, command.getCount());
        }

        KeyValue<String, List<Tuple>> result;
        if (count == null) {
            result = jedisCmd.getSortedSetCommands().bzmpop(timeout, option, keys);
        } else {
            result = jedisCmd.getSortedSetCommands().bzmpop(timeout, option, count, keys);
        }

        AdapterResultCursor resultCursor = listResult(request, COL_KEY_STRING, result);
        receive.responseResult(request, resultCursor);
        return completed(sync);
    }

    public static CgFuture<?> cmdBZPopMaxRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, BZPopMaxRedisCmd command, AdapterRequest request,
                                                  AdapterReceive receive) throws SQLException {
        double timeout = argAsDouble(request, command.getTimeout());

        List<StrToken> keyList = command.getKeyName();
        String[] keys = new String[keyList.size()];
        for (int i = 0; i < keyList.size(); i++) {
            keys[i] = argAsString(request, keyList.get(i));
        }

        KeyValue<String, Tuple> result = jedisCmd.getSortedSetCommands().bzpopmax(timeout, keys);

        AdapterResultCursor resultCursor = listResult(request, COL_KEY_STRING, new KeyValue<>(result.getKey(), Collections.singletonList(result.getValue())));
        receive.responseResult(request, resultCursor);
        return completed(sync);
    }

    public static CgFuture<?> cmdBZPopMinRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, BZPopMinRedisCmd command, AdapterRequest request,
                                                  AdapterReceive receive) throws SQLException {
        double timeout = argAsDouble(request, command.getTimeout());

        List<StrToken> keyList = command.getKeyName();
        String[] keys = new String[keyList.size()];
        for (int i = 0; i < keyList.size(); i++) {
            keys[i] = argAsString(request, keyList.get(i));
        }

        KeyValue<String, Tuple> result = jedisCmd.getSortedSetCommands().bzpopmin(timeout, keys);

        AdapterResultCursor resultCursor = listResult(request, COL_KEY_STRING, new KeyValue<>(result.getKey(), Collections.singletonList(result.getValue())));
        receive.responseResult(request, resultCursor);
        return completed(sync);
    }

    public static CgFuture<?> cmdZAddRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, ZAddRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        ZAddParams zAddParams = null;
        if (command.getNxxx() != null) {
            zAddParams = zAddParams == null ? new ZAddParams() : zAddParams;
            switch (command.getNxxx()) {
                case NX:
                    zAddParams.nx();
                    break;
                case XX:
                    zAddParams.xx();
                    break;
                default:
                    throw new SQLException("NXXX " + command.getNxxx() + " not support.", JdbcErrorCode.SQL_STATE_ILLEGAL_ARGUMENT);
            }
        }

        if (command.getGtlt() != null) {
            zAddParams = zAddParams == null ? new ZAddParams() : zAddParams;
            switch (command.getGtlt()) {
                case GT:
                    zAddParams.gt();
                    break;
                case LT:
                    zAddParams.lt();
                    break;
                default:
                    throw new SQLException("GTLT " + command.getNxxx() + " not support.", JdbcErrorCode.SQL_STATE_ILLEGAL_ARGUMENT);
            }
        }

        if (command.getChTag() != null) {
            zAddParams = zAddParams == null ? new ZAddParams() : zAddParams;
            zAddParams.ch();
        }

        Map<String, Double> scoreMembers = new LinkedHashMap<>();
        double score = 0;
        String member = null;
        for (ScoreAndMemberToken scoreAndMember : command.getMembers()) {
            score = argAsDouble(request, scoreAndMember.getScore());
            member = argAsString(request, scoreAndMember.getMember());
            scoreMembers.put(member, score);
        }

        if (command.getIncrTag() != null) {
            if (scoreMembers.size() > 1) {
                throw new SQLException("Only one score-element pair can be specified in this mode.", JdbcErrorCode.SQL_STATE_ILLEGAL_ARGUMENT);
            }
            zAddParams = zAddParams == null ? new ZAddParams() : zAddParams;
            double result = jedisCmd.getSortedSetCommands().zaddIncr(key, score, member, zAddParams);
            receive.responseResult(request, singleResult(request, COL_RESULT_DOUBLE, result));
        } else {
            long result;
            if (zAddParams != null) {
                result = jedisCmd.getSortedSetCommands().zadd(key, scoreMembers, zAddParams);
            } else {
                result = jedisCmd.getSortedSetCommands().zadd(key, scoreMembers);
            }
            receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        }

        return completed(sync);
    }

    public static CgFuture<?> cmdZCardRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, ZCardRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        long result = jedisCmd.getSortedSetCommands().zcard(key);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdZCountRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, ZCountRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String min = argAsString(request, command.getMin());
        String max = argAsString(request, command.getMax());

        long result = jedisCmd.getSortedSetCommands().zcount(key, min, max);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdZDiffRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, ZDiffRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        long numKeys = argAsLong(request, command.getNumKeys());
        List<StrToken> nameContexts = command.getKeyName();
        String[] keys = new String[nameContexts.size()];
        for (int i = 0; i < nameContexts.size(); i++) {
            keys[i] = argAsString(request, nameContexts.get(i));
        }
        numKeysCheck("ZDIFF", keys.length, numKeys);

        if (command.isWithScores()) {
            List<Tuple> result = jedisCmd.getSortedSetCommands().zdiffWithScores(keys);
            receive.responseResult(request, listResult(request, result));
        } else {
            List<String> result = jedisCmd.getSortedSetCommands().zdiff(keys);
            receive.responseResult(request, listResult(request, COL_ELEMENT_STRING, result));
        }

        return completed(sync);
    }

    public static CgFuture<?> cmdZDiffStoreRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, ZDiffStoreRedisCmd command, AdapterRequest request,
                                                    AdapterReceive receive) throws SQLException {
        String dstKey = argAsString(request, command.getDst());
        long numKeys = argAsLong(request, command.getNumKeys());

        List<StrToken> nameContexts = command.getKeyName();
        String[] keys = new String[nameContexts.size()];
        for (int i = 0; i < nameContexts.size(); i++) {
            keys[i] = argAsString(request, nameContexts.get(i));
        }
        numKeysCheck("ZDIFFSTORE", keys.length, numKeys);

        long result = jedisCmd.getSortedSetCommands().zdiffstore(dstKey, keys);
        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdZIncrByRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, ZIncrByRedisCmd command, AdapterRequest request,
                                                 AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        long increment = argAsLong(request, command.getIncrement());
        String member = argAsString(request, command.getMember());

        double value = jedisCmd.getSortedSetCommands().zincrby(key, increment, member);

        receive.responseResult(request, singleResult(request, COL_SCORE_DOUBLE, value));
        return completed(sync);
    }

    public static CgFuture<?> cmdZInterRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, ZInterRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        long numKeys = argAsLong(request, command.getNumKeys());

        List<StrToken> nameContexts = command.getKeyName();
        String[] keys = new String[nameContexts.size()];
        for (int i = 0; i < nameContexts.size(); i++) {
            keys[i] = argAsString(request, nameContexts.get(i));
        }
        numKeysCheck("ZINTER", keys.length, numKeys);

        ZParams zParams = new ZParams();
        if (CollectionUtils.isNotEmpty(command.getWeights())) {
            List<Double> weights = new ArrayList<>();
            for (IntToken decimalContext : command.getWeights()) {
                weights.add(argAsDouble(request, decimalContext));
            }
            double[] weightArray = new double[weights.size()];
            for (int i = 0; i < weights.size(); i++) {
                weightArray[i] = weights.get(i);
            }
            zParams.weights(weightArray);
        }

        if (command.getAggregateType() != null) {
            zParams.aggregate(convertToAggregate(command.getAggregateType()));
        }

        if (command.isWithScores()) {
            List<Tuple> result = jedisCmd.getSortedSetCommands().zinterWithScores(zParams, keys);
            receive.responseResult(request, listResult(request, result));
        } else {
            List<String> result = jedisCmd.getSortedSetCommands().zinter(zParams, keys);
            receive.responseResult(request, listResult(request, COL_ELEMENT_STRING, result));
        }

        return completed(sync);
    }

    public static CgFuture<?> cmdZInterCardRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, ZInterCardRedisCmd command, AdapterRequest request,
                                                    AdapterReceive receive) throws SQLException {
        long numKeys = argAsLong(request, command.getNumKeys());

        List<StrToken> nameContexts = command.getKeyName();
        String[] keys = new String[nameContexts.size()];
        for (int i = 0; i < nameContexts.size(); i++) {
            keys[i] = argAsString(request, nameContexts.get(i));
        }
        numKeysCheck("ZINTERCARD", keys.length, numKeys);

        long result;
        if (command.getLimit() != null) {
            long limit = argAsLong(request, command.getLimit());
            result = jedisCmd.getSortedSetCommands().zintercard(limit, keys);
        } else {
            result = jedisCmd.getSortedSetCommands().zintercard(keys);
        }

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdZInterStoreRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, ZInterStoreRedisCmd command, AdapterRequest request,
                                                     AdapterReceive receive) throws SQLException {
        String dstKey = argAsString(request, command.getDst());
        long numKeys = argAsLong(request, command.getNumKeys());
        List<StrToken> nameContexts = command.getKeyName();
        String[] keys = new String[nameContexts.size()];
        for (int i = 0; i < nameContexts.size(); i++) {
            keys[i] = argAsString(request, nameContexts.get(i));
        }
        numKeysCheck("ZINTERSTORE", keys.length, numKeys);

        ZParams zParams = new ZParams();
        if (CollectionUtils.isNotEmpty(command.getWeights())) {
            List<Double> weights = new ArrayList<>();
            for (IntToken weight : command.getWeights()) {
                weights.add(argAsDouble(request, weight));
            }
            double[] weightArray = new double[weights.size()];
            for (int i = 0; i < weights.size(); i++) {
                weightArray[i] = weights.get(i);
            }
            zParams.weights(weightArray);
        }

        if (command.getAggregateType() != null) {
            zParams.aggregate(convertToAggregate(command.getAggregateType()));
        }

        long result = jedisCmd.getSortedSetCommands().zinterstore(dstKey, zParams, keys);
        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdZLexCountRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, ZLexCountRedisCmd command, AdapterRequest request,
                                                   AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String min = argAsString(request, command.getMin());
        String max = argAsString(request, command.getMax());

        long result = jedisCmd.getSortedSetCommands().zlexcount(key, min, max);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdZMPopRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, ZMPopRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        int numKeys = argAsInt(request, command.getNumKeys());
        List<StrToken> nameContexts = command.getKeyName();
        String[] keys = new String[nameContexts.size()];
        for (int i = 0; i < nameContexts.size(); i++) {
            keys[i] = argAsString(request, nameContexts.get(i));
        }
        numKeysCheck("keys", keys.length, numKeys);

        SortedSetOption option = convertToSortedSetOption(command.getMinMax());
        Integer count = null;
        if (command.getCount() != null) {
            count = argAsInt(request, command.getCount());
        }

        KeyValue<String, List<Tuple>> result;
        if (count == null) {
            result = jedisCmd.getSortedSetCommands().zmpop(option, keys);
        } else {
            result = jedisCmd.getSortedSetCommands().zmpop(option, count, keys);
        }

        AdapterResultCursor resultCursor = listResult(request, COL_KEY_STRING, result);
        receive.responseResult(request, resultCursor);
        return completed(sync);
    }

    public static CgFuture<?> cmdZMSCoreRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, ZMScoreRedisCmd command, AdapterRequest request,
                                                 AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        List<StrToken> nameContexts = command.getFieldNames();
        String[] member = new String[nameContexts.size()];
        for (int i = 0; i < nameContexts.size(); i++) {
            member[i] = argAsString(request, nameContexts.get(i));
        }

        List<Double> result = jedisCmd.getSortedSetCommands().zmscore(key, member);

        receive.responseResult(request, listResult(request, COL_SCORE_DOUBLE, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdZPopMaxRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, ZPopMaxRedisCmd command, AdapterRequest request,
                                                 AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        Integer count = null;
        if (command.getCount() != null) {
            count = argAsInt(request, command.getCount());
        }

        List<Tuple> result;
        if (count == null) {
            result = Collections.singletonList(jedisCmd.getSortedSetCommands().zpopmax(key));
        } else {
            result = jedisCmd.getSortedSetCommands().zpopmax(key, count);
        }

        receive.responseResult(request, listResult(request, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdZPopMinRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, ZPopMinRedisCmd command, AdapterRequest request,
                                                 AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        Integer count = null;
        if (command.getCount() != null) {
            count = argAsInt(request, command.getCount());
        }

        List<Tuple> result;
        if (count == null) {
            result = Collections.singletonList(jedisCmd.getSortedSetCommands().zpopmin(key));
        } else {
            result = jedisCmd.getSortedSetCommands().zpopmin(key, count);
        }

        receive.responseResult(request, listResult(request, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdZRAndMemberRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, ZRAndMemberRedisCmd command, AdapterRequest request,
                                                     AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        if (command.getCount() != null) {
            long count = argAsLong(request, command.getCount());
            if (command.isWithScores()) {
                List<Tuple> result = jedisCmd.getSortedSetCommands().zrandmemberWithScores(key, count);
                receive.responseResult(request, listResult(request, result));
            } else {
                List<String> result = jedisCmd.getSortedSetCommands().zrandmember(key, count);
                receive.responseResult(request, listResult(request, COL_ELEMENT_STRING, result));
            }
        } else {
            List<String> result = Collections.singletonList(jedisCmd.getSortedSetCommands().zrandmember(key));
            receive.responseResult(request, listResult(request, COL_ELEMENT_STRING, result));
        }

        return completed(sync);
    }

    public static CgFuture<?> cmdZRangeRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, ZRangeRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String min = argAsString(request, command.getStart());
        String max = argAsString(request, command.getEnd());

        ZRangeParams params;
        if (command.getScoreLex() != null) {
            if (command.getScoreLex() == ScoreLex.BYSCORE) {
                double minDouble = ConvertUtils.toDouble(min, true);
                double maxDouble = ConvertUtils.toDouble(max, true);
                params = ZRangeParams.zrangeByScoreParams(minDouble, maxDouble);
            } else if (command.getScoreLex() == ScoreLex.BYLEX) {
                params = ZRangeParams.zrangeByLexParams(min, max);
            } else {
                throw new SQLException("Unknown ScoreLex type: " + command.getScoreLex(), JdbcErrorCode.SQL_STATE_ILLEGAL_ARGUMENT);
            }
        } else {
            int minInt = Integer.parseInt(min);
            int maxInt = Integer.parseInt(max);
            params = ZRangeParams.zrangeParams(minInt, maxInt);
        }

        if (command.isRev()) {
            params.rev();
        }

        if (command.getLimitOffset() != null && command.getLimitCount() != null) {
            int offset = argAsInt(request, command.getLimitOffset());
            int count = argAsInt(request, command.getLimitCount());
            params.limit(offset, count);
        }

        if (command.isWithScores()) {
            List<Tuple> value = jedisCmd.getSortedSetCommands().zrangeWithScores(key, params);
            receive.responseResult(request, listResult(request, value));
        } else {
            List<String> value = jedisCmd.getSortedSetCommands().zrange(key, params);
            receive.responseResult(request, listResult(request, COL_ELEMENT_STRING, value));
        }

        return completed(sync);
    }

    public static CgFuture<?> cmdZRangeByLexRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, ZRangeByLexRedisCmd command, AdapterRequest request,
                                                     AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String min = argAsString(request, command.getMin());
        String max = argAsString(request, command.getMax());

        List<String> value;
        if (command.getLimitOffset() != null && command.getLimitCount() != null) {
            int offset = argAsInt(request, command.getLimitOffset());
            int count = argAsInt(request, command.getLimitCount());
            value = jedisCmd.getSortedSetCommands().zrangeByLex(key, min, max, offset, count);
        } else {
            value = jedisCmd.getSortedSetCommands().zrangeByLex(key, min, max);
        }

        receive.responseResult(request, listResult(request, COL_ELEMENT_STRING, value));
        return completed(sync);
    }

    public static CgFuture<?> cmdZRangeByScoreRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, ZRangeByScoreRedisCmd command, AdapterRequest request,
                                                       AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String min = argAsString(request, command.getMin());
        String max = argAsString(request, command.getMax());

        if (command.isWithScores()) {
            List<Tuple> value;
            if (command.getLimitOffset() != null && command.getLimitCount() != null) {
                int offset = argAsInt(request, command.getLimitOffset());
                int count = argAsInt(request, command.getLimitCount());
                value = jedisCmd.getSortedSetCommands().zrangeByScoreWithScores(key, min, max, offset, count);
            } else {
                value = jedisCmd.getSortedSetCommands().zrangeByScoreWithScores(key, min, max);
            }
            receive.responseResult(request, listResult(request, value));
        } else {
            List<String> value;
            if (command.getLimitOffset() != null && command.getLimitCount() != null) {
                int offset = argAsInt(request, command.getLimitOffset());
                int count = argAsInt(request, command.getLimitCount());
                value = jedisCmd.getSortedSetCommands().zrangeByScore(key, min, max, offset, count);
            } else {
                value = jedisCmd.getSortedSetCommands().zrangeByScore(key, min, max);
            }
            receive.responseResult(request, listResult(request, COL_ELEMENT_STRING, value));
        }

        return completed(sync);
    }

    public static CgFuture<?> cmdZRangeStoreRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, ZRangeStoreRedisCmd command, AdapterRequest request,
                                                     AdapterReceive receive) throws SQLException {
        String dst = argAsString(request, command.getDstKey());
        String src = argAsString(request, command.getSrcName());
        String minStr = argAsString(request, command.getMin());
        String maxStr = argAsString(request, command.getMax());

        ZRangeParams params;
        if (command.getScoreLex() != null) {
            switch (command.getScoreLex()) {
                case BYLEX: {
                    params = ZRangeParams.zrangeByLexParams(minStr, maxStr);
                    break;
                }
                case BYSCORE: {
                    double min = ConvertUtils.toDouble(minStr, true);
                    double max = ConvertUtils.toDouble(maxStr, true);
                    params = ZRangeParams.zrangeByScoreParams(min, max);
                    break;
                }
                default:
                    throw new SQLException("Unknown score lex: " + command.getScoreLex());
            }
        } else {
            int min = ConvertUtils.toInteger(minStr, true);
            int max = ConvertUtils.toInteger(maxStr, true);
            params = ZRangeParams.zrangeParams(min, max);
        }

        if (command.isRev()) {
            params.rev();
        }

        if (command.getLimitOffset() != null && command.getLimitCount() != null) {
            int offset = argAsInt(request, command.getLimitOffset());
            int count = argAsInt(request, command.getLimitCount());
            params.limit(offset, count);
        }

        long result = jedisCmd.getSortedSetCommands().zrangestore(dst, src, params);
        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdZRankRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, ZRankRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String member = argAsString(request, command.getMember());

        if (command.isWithScore()) {
            KeyValue<Long, Double> result = jedisCmd.getSortedSetCommands().zrankWithScore(key, member);

            AdapterResultCursor receiveCur = new AdapterResultCursor(request, Arrays.asList(//
                    COL_SCORE_DOUBLE, COL_RANK_LONG));
            receive.responseResult(request, receiveCur);

            receiveCur.pushData(CollectionUtils.asMap(       //
                    COL_SCORE_DOUBLE.name, result.getValue(),//
                    COL_RANK_LONG.name, result.getKey()      //
            ));

            receiveCur.pushFinish();
            return completed(sync);
        } else {
            Long result = jedisCmd.getSortedSetCommands().zrank(key, member);
            receive.responseResult(request, singleResult(request, COL_RANK_LONG, result));
            return completed(sync);
        }
    }

    public static CgFuture<?> cmdZRemRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, ZRemRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        List<StrToken> nameContexts = command.getFieldNames();
        String[] member = new String[nameContexts.size()];
        for (int i = 0; i < nameContexts.size(); i++) {
            member[i] = argAsString(request, nameContexts.get(i));
        }

        long result = jedisCmd.getSortedSetCommands().zrem(key, member);
        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdZRemRangeByLexRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, ZRemRangeByLexRedisCmd command, AdapterRequest request,
                                                        AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String minStr = argAsString(request, command.getMin());
        String maxStr = argAsString(request, command.getMax());

        long result = jedisCmd.getSortedSetCommands().zremrangeByLex(key, minStr, maxStr);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdZRemRangeByRankRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, ZRemRangeByRankRedisCmd command, AdapterRequest request,
                                                         AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        long start = argAsLong(request, command.getStart());
        long stop = argAsLong(request, command.getEnd());

        long result = jedisCmd.getSortedSetCommands().zremrangeByRank(key, start, stop);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdZRemRangeByScoreRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, ZRemRangeByScoreRedisCmd command, AdapterRequest request,
                                                          AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String min = argAsString(request, command.getMin());
        String max = argAsString(request, command.getMax());

        long result = jedisCmd.getSortedSetCommands().zremrangeByScore(key, min, max);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdZRevRangeRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, ZRevRangeRedisCmd command, AdapterRequest request,
                                                   AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        long start = argAsLong(request, command.getStart());
        long stop = argAsLong(request, command.getEnd());

        if (command.isWithScores()) {
            List<Tuple> result = jedisCmd.getSortedSetCommands().zrevrangeWithScores(key, start, stop);
            receive.responseResult(request, listResult(request, result));
        } else {
            List<String> result = jedisCmd.getSortedSetCommands().zrevrange(key, start, stop);
            receive.responseResult(request, listResult(request, COL_ELEMENT_STRING, result));
        }

        return completed(sync);
    }

    public static CgFuture<?> cmdZRevRangeByLexRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, ZRevRangeByLexRedisCmd command, AdapterRequest request,
                                                        AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String max = argAsString(request, command.getMax());
        String min = argAsString(request, command.getMin());

        List<String> result;
        if (command.getLimitOffset() != null && command.getLimitCount() != null) {
            int offset = argAsInt(request, command.getLimitOffset());
            int count = argAsInt(request, command.getLimitCount());
            result = jedisCmd.getSortedSetCommands().zrevrangeByLex(key, max, min, offset, count);
        } else {
            result = jedisCmd.getSortedSetCommands().zrevrangeByLex(key, max, min);
        }

        receive.responseResult(request, listResult(request, COL_ELEMENT_STRING, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdZRevRangeByScoreRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, ZRevRangeByScoreRedisCmd command, AdapterRequest request,
                                                          AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String max = argAsString(request, command.getMax());
        String min = argAsString(request, command.getMin());

        if (command.isWithScores()) {
            List<Tuple> result;
            if (command.getLimitOffset() != null && command.getLimitCount() != null) {
                int offset = argAsInt(request, command.getLimitOffset());
                int count = argAsInt(request, command.getLimitCount());
                result = jedisCmd.getSortedSetCommands().zrevrangeByScoreWithScores(key, max, min, offset, count);
            } else {
                result = jedisCmd.getSortedSetCommands().zrevrangeByScoreWithScores(key, max, min);
            }
            receive.responseResult(request, listResult(request, result));
        } else {
            List<String> result;
            if (command.getLimitOffset() != null && command.getLimitCount() != null) {
                int offset = argAsInt(request, command.getLimitOffset());
                int count = argAsInt(request, command.getLimitCount());
                result = jedisCmd.getSortedSetCommands().zrevrangeByScore(key, max, min, offset, count);
            } else {
                result = jedisCmd.getSortedSetCommands().zrevrangeByScore(key, max, min);
            }

            receive.responseResult(request, listResult(request, COL_ELEMENT_STRING, result));
        }

        return completed(sync);
    }

    public static CgFuture<?> cmdZRevRankRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, ZRevRankRedisCmd command, AdapterRequest request,
                                                  AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String member = argAsString(request, command.getMember());

        if (command.isWithScore()) {
            KeyValue<Long, Double> result = jedisCmd.getSortedSetCommands().zrevrankWithScore(key, member);

            AdapterResultCursor receiveCur = new AdapterResultCursor(request, Arrays.asList(//
                    COL_RANK_LONG,  //
                    COL_SCORE_DOUBLE));
            receive.responseResult(request, receiveCur);

            receiveCur.pushData(CollectionUtils.asMap(       //
                    COL_RANK_LONG.name, result.getKey(),     //
                    COL_SCORE_DOUBLE.name, result.getValue() //
            ));

            receiveCur.pushFinish();
            return completed(sync);
        } else {
            Long result = jedisCmd.getSortedSetCommands().zrevrank(key, member);
            receive.responseResult(request, singleResult(request, COL_RANK_LONG, result));
            return completed(sync);
        }
    }

    public static CgFuture<?> cmdZScanRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, ZSCanRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String cursor = argAsString(request, command.getCursor());
        String pattern = null;
        Integer count = null;
        long maxRows = request.getMaxRows();
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

        ScanResult<Tuple> result = jedisCmd.getSortedSetCommands().zscan(key, cursor, scanParams);
        if (sync.isDone()) {
            SQLException err = new SQLException("command interrupted.");
            receive.responseFailed(request, err);
            throw err;
        }

        AdapterResultCursor receiveCur = new AdapterResultCursor(request, Arrays.asList(//
                COL_CURSOR_STRING,   //
                COL_SCORE_DOUBLE,   //
                COL_ELEMENT_STRING));
        receive.responseResult(request, receiveCur);

        int affectRows = 0;
        for (Tuple tuple : result.getResult()) {
            receiveCur.pushData(CollectionUtils.asMap(          //
                    COL_CURSOR_STRING.name, result.getCursor(), //
                    COL_SCORE_DOUBLE.name, tuple.getScore(),    //
                    COL_ELEMENT_STRING.name, tuple.getElement() //
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

    public static CgFuture<?> cmdZScoreRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, ZScoreRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String member = argAsString(request, command.getMember());

        Double result = jedisCmd.getSortedSetCommands().zscore(key, member);

        receive.responseResult(request, singleResult(request, COL_SCORE_DOUBLE, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdZUnionRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, ZUnionRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        long numKeys = argAsLong(request, command.getNumKeys());
        List<StrToken> nameContexts = command.getKeyName();
        String[] keys = new String[nameContexts.size()];
        for (int i = 0; i < nameContexts.size(); i++) {
            keys[i] = argAsString(request, nameContexts.get(i));
        }
        numKeysCheck("ZUNION", keys.length, numKeys);

        ZParams zParams = new ZParams();
        if (CollectionUtils.isNotEmpty(command.getWeights())) {
            List<Double> weights = new ArrayList<>();
            for (IntToken intCtx : command.getWeights()) {
                weights.add(argAsDouble(request, intCtx));
            }
            double[] weightArray = new double[weights.size()];
            for (int i = 0; i < weights.size(); i++) {
                weightArray[i] = weights.get(i);
            }
            zParams.weights(weightArray);
        }

        if (command.getAggregateType() != null) {
            zParams.aggregate(convertToAggregate(command.getAggregateType()));
        }

        if (command.isWithScores()) {
            List<Tuple> result = jedisCmd.getSortedSetCommands().zunionWithScores(zParams, keys);
            receive.responseResult(request, listResult(request, result));
        } else {
            List<String> result = jedisCmd.getSortedSetCommands().zunion(zParams, keys);
            receive.responseResult(request, listResult(request, COL_ELEMENT_STRING, result));
        }

        return completed(sync);
    }

    public static CgFuture<?> cmdZUnionStoreRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, ZUnionStoreRedisCmd command, AdapterRequest request,
                                                     AdapterReceive receive) throws SQLException {
        String dstKey = argAsString(request, command.getDst());
        long numKeys = argAsLong(request, command.getNumKeys());
        List<StrToken> keyNames = command.getKeyName();
        String[] keys = new String[keyNames.size()];
        for (int i = 0; i < keyNames.size(); i++) {
            keys[i] = argAsString(request, keyNames.get(i));
        }
        numKeysCheck("ZUNIONSTORE", keys.length, numKeys);

        ZParams zParams = new ZParams();
        if (CollectionUtils.isNotEmpty(command.getWeights())) {
            List<Double> weights = new ArrayList<>();
            for (IntToken weight : command.getWeights()) {
                weights.add(argAsDouble(request, weight));
            }
            double[] weightArray = new double[weights.size()];
            for (int i = 0; i < weights.size(); i++) {
                weightArray[i] = weights.get(i);
            }
            zParams.weights(weightArray);
        }

        if (command.getAggregateType() != null) {
            zParams.aggregate(convertToAggregate(command.getAggregateType()));
        }

        long result = jedisCmd.getSortedSetCommands().zunionstore(dstKey, zParams, keys);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }
}
