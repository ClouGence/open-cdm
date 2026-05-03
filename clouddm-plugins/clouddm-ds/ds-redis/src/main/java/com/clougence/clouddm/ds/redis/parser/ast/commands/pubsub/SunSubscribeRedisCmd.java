package com.clougence.clouddm.ds.redis.parser.ast.commands.pubsub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

/**
 * @Author: Ekko
 * @Date: 2023-05-25 15:37
 */
public class SunSubscribeRedisCmd extends AbstractRedisCmd {

    private final List<StrToken> channels = new ArrayList<>();

    public void addChannel(StrToken channel) {
        if (channel != null) {
            this.channels.add(channel);
        }
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.SUNSUBSCRIBE; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("SUNSUBSCRIBE");
        for (StrToken channel : this.channels) {
            writer.append(" ");
            writer.append(fmtString(channel));
        }
    }
}
