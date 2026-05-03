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
public class ZUnionRedisCmd extends AbstractRedisCmd {

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
    public RedisCmdType getCmdType() { return RedisCmdType.ZUNION; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("ZUNION");
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
