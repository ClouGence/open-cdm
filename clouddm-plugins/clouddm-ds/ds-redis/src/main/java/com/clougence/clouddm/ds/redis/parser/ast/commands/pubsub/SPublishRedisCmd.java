package com.clougence.clouddm.ds.redis.parser.ast.commands.pubsub;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Ekko
 * @Date: 2023-05-25 15:37
 */

@Getter
@Setter
public class SPublishRedisCmd extends AbstractRedisCmd {

    private StrToken channel;
    private StrToken message;

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.SPUBLISH; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("SPUBLISH");
        writer.append(" ");
        writer.append(fmtString(this.channel));
        writer.append(" ");
        writer.append(fmtString(this.message));
    }
}
