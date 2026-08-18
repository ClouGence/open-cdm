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

import com.clougence.sql.redis.parser.ast.commands.sortedset.*;
import com.clougence.sql.redis.parser.ast.token.IntToken;
import com.clougence.sql.redis.parser.ast.token.ScoreAndMemberToken;
import com.clougence.sql.redis.parser.ast.token.ScoreLex;
import com.clougence.sql.redis.parser.ast.token.StrToken;
import com.clougence.drivers.adapter.*;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.future.CgFuture;

import glide.api.models.commands.RangeOptions.LexRange;
import glide.api.models.commands.RangeOptions.ScoreRange;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class GlideCallForSortedSet extends GlideUtils {

    public static CgFuture<?> cmdBZMPopRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, BZMPopRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        throw new UnsupportedOperationException("GLIDE does not support this command yet.");
    }
    public static CgFuture<?> cmdBZPopMaxRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, BZPopMaxRedisCmd command, AdapterRequest request,
                                                  AdapterReceive receive) throws SQLException {
        throw new UnsupportedOperationException("GLIDE does not support this command yet.");
    }
    public static CgFuture<?> cmdBZPopMinRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, BZPopMinRedisCmd command, AdapterRequest request,
                                                  AdapterReceive receive) throws SQLException {
        throw new UnsupportedOperationException("GLIDE does not support this command yet.");
    }
    public static CgFuture<?> cmdZAddRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, ZAddRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        throw new UnsupportedOperationException("GLIDE does not support this command yet.");
    }
    public static CgFuture<?> cmdZCardRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, ZCardRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        long result = glideCmd.zcard(key);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdZCountRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, ZCountRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        ScoreRange min = parseScoreRange(argAsString(request, command.getMin()));
        ScoreRange max = parseScoreRange(argAsString(request, command.getMax()));

        long result = glideCmd.zcount(key, min, max);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdZDiffRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, ZDiffRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        throw new UnsupportedOperationException("GLIDE does not support this command yet.");
    }
    public static CgFuture<?> cmdZDiffStoreRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, ZDiffStoreRedisCmd command, AdapterRequest request,
                                                    AdapterReceive receive) throws SQLException {
        throw new UnsupportedOperationException("GLIDE does not support this command yet.");
    }
    public static CgFuture<?> cmdZIncrByRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, ZIncrByRedisCmd command, AdapterRequest request,
                                                 AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        long increment = argAsLong(request, command.getIncrement());
        String member = argAsString(request, command.getMember());

        double value = glideCmd.zincrby(key, increment, member);

        receive.responseResult(request, singleResult(request, COL_SCORE_DOUBLE, value));
        return completed(sync);
    }

    public static CgFuture<?> cmdZInterRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, ZInterRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        throw new UnsupportedOperationException("GLIDE does not support this command yet.");
    }
    public static CgFuture<?> cmdZInterCardRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, ZInterCardRedisCmd command, AdapterRequest request,
                                                    AdapterReceive receive) throws SQLException {
        throw new UnsupportedOperationException("GLIDE does not support this command yet.");
    }
    public static CgFuture<?> cmdZInterStoreRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, ZInterStoreRedisCmd command, AdapterRequest request,
                                                     AdapterReceive receive) throws SQLException {
        throw new UnsupportedOperationException("GLIDE does not support this command yet.");
    }
    public static CgFuture<?> cmdZLexCountRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, ZLexCountRedisCmd command, AdapterRequest request,
                                                   AdapterReceive receive) throws SQLException {
        throw new UnsupportedOperationException("GLIDE does not support this command yet.");
    }
    public static CgFuture<?> cmdZMPopRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, ZMPopRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        throw new UnsupportedOperationException("GLIDE does not support this command yet.");
    }
    public static CgFuture<?> cmdZMSCoreRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, ZMScoreRedisCmd command, AdapterRequest request,
                                                 AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        List<StrToken> nameContexts = command.getFieldNames();
        String[] member = new String[nameContexts.size()];
        for (int i = 0; i < nameContexts.size(); i++) {
            member[i] = argAsString(request, nameContexts.get(i));
        }

        Double[] result = glideCmd.zmscore(key, member);

        receive.responseResult(request, listResult(request, COL_SCORE_DOUBLE, Arrays.asList(result)));
        return completed(sync);
    }

    public static CgFuture<?> cmdZPopMaxRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, ZPopMaxRedisCmd command, AdapterRequest request,
                                                 AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        Integer count = null;
        if (command.getCount() != null) {
            count = argAsInt(request, command.getCount());
        }

        Map<String, Double> result = glideCmd.zpopmax(key);

        receive.responseResult(request, listResult(request, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdZPopMinRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, ZPopMinRedisCmd command, AdapterRequest request,
                                                 AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        Integer count = null;
        if (command.getCount() != null) {
            count = argAsInt(request, command.getCount());
        }

        Map<String, Double> result = glideCmd.zpopmin(key);

        receive.responseResult(request, listResult(request, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdZRAndMemberRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, ZRAndMemberRedisCmd command, AdapterRequest request,
                                                     AdapterReceive receive) throws SQLException {
        throw new UnsupportedOperationException("GLIDE does not support this command yet.");
    }
    public static CgFuture<?> cmdZRangeRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, ZRangeRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        throw new UnsupportedOperationException("GLIDE does not support this command yet.");
    }
    public static CgFuture<?> cmdZRangeByLexRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, ZRangeByLexRedisCmd command, AdapterRequest request,
                                                     AdapterReceive receive) throws SQLException {
        throw new UnsupportedOperationException("GLIDE does not support this command yet.");
    }
    public static CgFuture<?> cmdZRangeByScoreRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, ZRangeByScoreRedisCmd command, AdapterRequest request,
                                                       AdapterReceive receive) throws SQLException {
        throw new UnsupportedOperationException("GLIDE does not support this command yet.");
    }
    public static CgFuture<?> cmdZRangeStoreRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, ZRangeStoreRedisCmd command, AdapterRequest request,
                                                     AdapterReceive receive) throws SQLException {
        throw new UnsupportedOperationException("GLIDE does not support this command yet.");
    }
    public static CgFuture<?> cmdZRankRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, ZRankRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String member = argAsString(request, command.getMember());

        if (command.isWithScore()) {
            Object[] result = glideCmd.zrankWithScore(key, member);

            AdapterResultCursor receiveCur = new AdapterResultCursor(request, Arrays.asList(//
                    COL_SCORE_DOUBLE, COL_RANK_LONG));
            receive.responseResult(request, receiveCur);

            receiveCur.pushData(CollectionUtils.asMap(       //
                    COL_SCORE_DOUBLE.name, result[1],         //
                    COL_RANK_LONG.name, result[0]             //
            ));

            receiveCur.pushFinish();
            return completed(sync);
        } else {
            Long result = glideCmd.zrank(key, member);
            receive.responseResult(request, singleResult(request, COL_RANK_LONG, result));
            return completed(sync);
        }
    }

    public static CgFuture<?> cmdZRemRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, ZRemRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());

        List<StrToken> nameContexts = command.getFieldNames();
        String[] member = new String[nameContexts.size()];
        for (int i = 0; i < nameContexts.size(); i++) {
            member[i] = argAsString(request, nameContexts.get(i));
        }

        long result = glideCmd.zrem(key, member);
        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdZRemRangeByLexRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, ZRemRangeByLexRedisCmd command, AdapterRequest request,
                                                        AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        LexRange minStr = parseLexRange(argAsString(request, command.getMin()));
        LexRange maxStr = parseLexRange(argAsString(request, command.getMax()));

        long result = glideCmd.zremrangeByLex(key, minStr, maxStr);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdZRemRangeByRankRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, ZRemRangeByRankRedisCmd command, AdapterRequest request,
                                                         AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        long start = argAsLong(request, command.getStart());
        long stop = argAsLong(request, command.getEnd());

        long result = glideCmd.zremrangeByRank(key, start, stop);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdZRemRangeByScoreRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, ZRemRangeByScoreRedisCmd command, AdapterRequest request,
                                                          AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        ScoreRange min = parseScoreRange(argAsString(request, command.getMin()));
        ScoreRange max = parseScoreRange(argAsString(request, command.getMax()));

        long result = glideCmd.zremrangeByScore(key, min, max);

        receive.responseResult(request, singleResult(request, COL_RESULT_LONG, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdZRevRangeRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, ZRevRangeRedisCmd command, AdapterRequest request,
                                                   AdapterReceive receive) throws SQLException {
        throw new UnsupportedOperationException("GLIDE does not support this command yet.");
    }
    public static CgFuture<?> cmdZRevRangeByLexRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, ZRevRangeByLexRedisCmd command, AdapterRequest request,
                                                        AdapterReceive receive) throws SQLException {
        throw new UnsupportedOperationException("GLIDE does not support this command yet.");
    }
    public static CgFuture<?> cmdZRevRangeByScoreRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, ZRevRangeByScoreRedisCmd command, AdapterRequest request,
                                                          AdapterReceive receive) throws SQLException {
        throw new UnsupportedOperationException("GLIDE does not support this command yet.");
    }
    public static CgFuture<?> cmdZRevRankRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, ZRevRankRedisCmd command, AdapterRequest request,
                                                  AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String member = argAsString(request, command.getMember());

        if (command.isWithScore()) {
            Object[] result = glideCmd.zrevrankWithScore(key, member);

            AdapterResultCursor receiveCur = new AdapterResultCursor(request, Arrays.asList(//
                    COL_RANK_LONG,  //
                    COL_SCORE_DOUBLE));
            receive.responseResult(request, receiveCur);

            receiveCur.pushData(CollectionUtils.asMap(       //
                    COL_RANK_LONG.name, result[0],            //
                    COL_SCORE_DOUBLE.name, result[1]          //
            ));

            receiveCur.pushFinish();
            return completed(sync);
        } else {
            Long result = glideCmd.zrevrank(key, member);
            receive.responseResult(request, singleResult(request, COL_RANK_LONG, result));
            return completed(sync);
        }
    }

    public static CgFuture<?> cmdZScanRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, ZSCanRedisCmd command, AdapterRequest request,
                                               AdapterReceive receive) throws SQLException {
        throw new UnsupportedOperationException("GLIDE does not support this command yet.");
    }
    public static CgFuture<?> cmdZScoreRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, ZScoreRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        String key = argAsString(request, command.getKeyName());
        String member = argAsString(request, command.getMember());

        Double result = glideCmd.zscore(key, member);

        receive.responseResult(request, singleResult(request, COL_SCORE_DOUBLE, result));
        return completed(sync);
    }

    public static CgFuture<?> cmdZUnionRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, ZUnionRedisCmd command, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException {
        throw new UnsupportedOperationException("GLIDE does not support this command yet.");
    }
    public static CgFuture<?> cmdZUnionStoreRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, ZUnionStoreRedisCmd command, AdapterRequest request,
                                                     AdapterReceive receive) throws SQLException {
        throw new UnsupportedOperationException("GLIDE does not support this command yet.");
    }
}
