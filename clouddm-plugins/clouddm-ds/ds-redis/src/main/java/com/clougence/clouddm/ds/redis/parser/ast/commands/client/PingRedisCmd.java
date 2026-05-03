package com.clougence.clouddm.ds.redis.parser.ast.commands.client;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Ekko
 * @date 2024/1/22 14:56
*/
@Getter
@Setter
public class PingRedisCmd extends AbstractRedisCmd {

    private StrToken message;

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.PING; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("PING");
        if (this.message != null) {
            writer.append(" ");
            writer.append(fmtString(message));
        }
    }
}
