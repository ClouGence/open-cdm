package com.clougence.clouddm.ds.redis.parser.ast.commands.cluster;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.IntToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode
 * @date 2024/1/22 14:56
 */
@Getter
@Setter
public class AddSlotsRangeRedisCmd extends AbstractRedisCmd {

    private final List<SlotsRange> range = new ArrayList<>();

    public void addRange(IntToken start, IntToken end) {
        SlotsRange range = new SlotsRange();
        range.start = start;
        range.end = end;
        this.range.add(range);
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.CLUSTER_ADDSLOTSRANGE; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("CLUSTER ADDSLOTSRANGE");
        for (SlotsRange range : this.range) {
            writer.append(" ");
            writer.append(fmtInt(range.start));
            writer.append(" ");
            writer.append(fmtInt(range.end));
        }
    }

    public static class SlotsRange {

        public IntToken start;
        public IntToken end;
    }
}
