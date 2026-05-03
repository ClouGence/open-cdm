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
public class PubSubShardChannelsRedisCmd extends AbstractRedisCmd {

    private StrToken pattern;

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.PUBSUB_SHARDCHANNELS; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("PUBSUB SHARDCHANNELS");
        if (this.pattern != null) {
            writer.append(" ");
            writer.append(fmtString(pattern));
        }
    }
}
