package com.clougence.clouddm.ds.redis.parser.ast.commands.list;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.Direction;
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
public class LMoveRedisCmd extends AbstractRedisCmd {

    private StrToken  src;
    private StrToken  dst;
    private Direction srcDirection;
    private Direction dstDirection;

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.LMOVE; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("LMOVE");
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
    }

}
