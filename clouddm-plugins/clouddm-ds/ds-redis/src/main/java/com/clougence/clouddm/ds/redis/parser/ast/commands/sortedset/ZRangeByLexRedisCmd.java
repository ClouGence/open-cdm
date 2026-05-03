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
public class ZRangeByLexRedisCmd extends AbstractRedisCmd {

    private StrToken    keyName;
    private StrIntToken min;
    private StrIntToken max;
    private IntToken    limitOffset;
    private IntToken    limitCount;

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.ZRANGEBYLEX; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("ZRANGEBYLEX");
        writer.append(" ");
        writer.append(fmtString(keyName));
        writer.append(" ");
        writer.append(fmtString(this.min));
        writer.append(" ");
        writer.append(fmtString(this.max));
        if (limitOffset != null && limitCount != null) {
            writer.append(" LIMIT ");
            writer.append(fmtInt(this.limitOffset));
            writer.append(" ");
            writer.append(fmtInt(this.limitCount));
        }
    }
}
