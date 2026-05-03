package com.clougence.clouddm.ds.redis.parser.ast.commands.sortedset;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.IntToken;
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
public class ZRevRangeByScoreRedisCmd extends AbstractRedisCmd {

    private StrToken    keyName;
    private StrIntToken max;
    private StrIntToken min;
    private boolean     withScores;
    private IntToken    limitOffset;
    private IntToken    limitCount;

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.ZREVRANGEBYSCORE; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("ZREVRANGEBYSCORE");
        writer.append(" ");
        writer.append(fmtString(keyName));
        writer.append(" ");
        writer.append(fmtString(max));
        writer.append(" ");
        writer.append(fmtString(min));
        if (withScores) {
            writer.append(" WITHSCORES");
        }
        if (limitOffset != null && limitCount != null) {
            writer.append(" LIMIT ");
            writer.append(fmtInt(this.limitOffset));
            writer.append(" ");
            writer.append(fmtInt(this.limitCount));
        }
    }
}
