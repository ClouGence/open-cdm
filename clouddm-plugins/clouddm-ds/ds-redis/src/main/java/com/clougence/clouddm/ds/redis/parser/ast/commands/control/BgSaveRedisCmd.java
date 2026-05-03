package com.clougence.clouddm.ds.redis.parser.ast.commands.control;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode
 * @date 2024/1/22 14:56
 */
@Getter
@Setter
public class BgSaveRedisCmd extends AbstractRedisCmd {

    private boolean schedule;

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.AUTH; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("BGSAVE");
        if (this.schedule) {
            writer.append(" ");
            writer.append("SCHEDULE");
        }
    }
}
