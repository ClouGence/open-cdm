package com.clougence.clouddm.ds.redis.parser.ast.commands.sortedset;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.IntToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.ScoreLex;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrIntToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: mode
 * @Date: 2023-07-10 11:15
 */
@Getter
@Setter
public class ZRangeRedisCmd extends AbstractRedisCmd {

    private StrToken    keyName;
    private StrIntToken start;
    private StrIntToken end;
    private ScoreLex    scoreLex;
    private boolean     rev;
    private IntToken    limitOffset;
    private IntToken    limitCount;
    private boolean     withScores;

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.ZRANGE; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("ZRANGE");
        writer.append(" ");
        writer.append(fmtString(keyName));
        writer.append(" ");
        writer.append(fmtString(this.start));
        writer.append(" ");
        writer.append(fmtString(this.end));
        if (scoreLex != null) {
            writer.append(" ");
            writer.append(this.scoreLex.name());
        }
        if (rev) {
            writer.append(" REV");
        }
        if (limitOffset != null && limitCount != null) {
            writer.append(" LIMIT ");
            writer.append(fmtInt(this.limitOffset));
            writer.append(" ");
            writer.append(fmtInt(this.limitCount));
        }
        if (this.withScores) {
            writer.append(" WITHSCORES");
        }
    }
}
