package com.clougence.clouddm.ds.redis.parser.ast.commands.pubsub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;
import com.clougence.utils.CollectionUtils;

/**
 * @Author: Ekko
 * @Date: 2023-05-25 15:37  
 */
public class PubSubShardNumSubRedisCmd extends AbstractRedisCmd {

    private final List<StrToken> channels = new ArrayList<>();

    public void addChannel(StrToken channel) {
        if (channel != null) {
            this.channels.add(channel);
        }
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.PUBSUB_SHARDNUMSUB; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("PUBSUB SHARDNUMSUB");
        if (CollectionUtils.isNotEmpty(this.channels)) {
            for (StrToken keyName : this.channels) {
                writer.append(" ");
                writer.append(fmtString(keyName));
            }
        }
    }
}
