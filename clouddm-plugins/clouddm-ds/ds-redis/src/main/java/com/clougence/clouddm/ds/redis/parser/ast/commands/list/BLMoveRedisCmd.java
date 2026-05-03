package com.clougence.clouddm.ds.redis.parser.ast.commands.list;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.Direction;
import com.clougence.clouddm.ds.redis.parser.ast.token.IntToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Ekko
 * @Date: 2023-06-30 14:35
 */
@Getter
@Setter
public class BLMoveRedisCmd extends AbstractRedisCmd {

    private StrToken  src;
    private StrToken  dst;
    private Direction srcDirection;
    private Direction dstDirection;
    private IntToken  timeout;

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.BLMOVE; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("BLMOVE");
        writer.append(" ");
        writer.append(fmtString(this.src));
        writer.append(" ");
        writer.append(fmtString(this.dst));
        if (srcDirection != null) {
            writer.append(" ");
            writer.append(this.srcDirection.name());
        }
        if (dstDirection != null) {
            writer.append(" ");
            writer.append(this.dstDirection.name());
        }
        writer.append(" ");
        writer.append(fmtInt(this.timeout));
    }
}
