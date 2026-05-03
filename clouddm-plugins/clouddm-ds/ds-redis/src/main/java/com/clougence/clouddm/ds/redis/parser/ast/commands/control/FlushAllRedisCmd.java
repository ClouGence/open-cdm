package com.clougence.clouddm.ds.redis.parser.ast.commands.control;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Ekko
 * @date 2024/1/22 14:56
*/
@Getter
@Setter
public class FlushAllRedisCmd extends AbstractRedisCmd {

    private Type type;

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.FLUSHALL; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("FLUSHALL");
        if (type != null) {
            switch (this.type) {
                case ASYNC: {
                    writer.append(" ");
                    writer.append("ASYNC");
                    break;
                }
                case SYNC: {
                    writer.append(" ");
                    writer.append("SYNC");
                    break;
                }
                default: {
                    throw new UnsupportedOperationException("Unsupported RedisCmdType " + this.type);
                }
            }
        }
    }

    public enum Type {
        ASYNC,
        SYNC
    }
}
