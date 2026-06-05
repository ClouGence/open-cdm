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
package com.clougence.clouddm.ds.redis.parser.ast.commands.sortedset;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.AggregateType;
import com.clougence.clouddm.ds.redis.parser.ast.token.IntToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;
import com.clougence.utils.CollectionUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: mode
 * @Date: 2023-07-10 11:15
 */
@Getter
@Setter
public class ZInterRedisCmd extends AbstractRedisCmd {

    private IntToken       numKeys;
    private List<StrToken> keyName = new ArrayList<>();
    private List<IntToken> weights = new ArrayList<>();
    private AggregateType  aggregateType;
    private boolean        withScores;

    public void addKeyName(StrToken key) {
        if (key != null) {
            this.keyName.add(key);
        }
    }

    public void addWeight(IntToken weight) {
        if (weight != null) {
            this.weights.add(weight);
        }
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.ZINTER; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("ZINTER");
        writer.append(" ");
        writer.append(fmtInt(this.numKeys));
        for (StrToken keyName : this.keyName) {
            writer.append(" ");
            writer.append(fmtString(keyName));
        }

        if (CollectionUtils.isNotEmpty(weights)) {
            writer.append(" ");
            writer.append("WEIGHTS");
            for (IntToken weight : this.weights) {
                writer.append(" ");
                writer.append(fmtInt(weight));
            }
        }

        if (aggregateType != null) {
            writer.append(" ");
            writer.append("AGGREGATE");
            writer.append(" ");
            writer.append(aggregateType.name());
        }

        if (withScores) {
            writer.append(" ");
            writer.append("WITHSCORES");
        }
    }
}
