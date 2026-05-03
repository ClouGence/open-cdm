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
public class ZRangeStoreRedisCmd extends AbstractRedisCmd {

    private StrToken    dstKey;
    private StrToken    srcName;
    private StrIntToken min;
    private StrIntToken max;
    private ScoreLex    scoreLex;
    private boolean     rev;
    private IntToken    limitOffset;
    private IntToken    limitCount;

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.ZRANGESTORE; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("ZRANGESTORE");
        writer.append(" ");
        writer.append(fmtString(dstKey));
        writer.append(" ");
        writer.append(fmtString(srcName));
        writer.append(" ");
        writer.append(fmtString(this.min));
        writer.append(" ");
        writer.append(fmtString(this.max));
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
    }
}
