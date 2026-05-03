package com.clougence.clouddm.ds.redis.parser.ast.commands.pubsub;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.dslpaser.foramt.FmtWriter;

/**
 * @Author: Ekko
 * @Date: 2023-05-25 15:37  
 */
public class PubSubNumPatRedisCmd extends AbstractRedisCmd {

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.PUBSUB_NUMPAT; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("PUBSUB NUMPAT");
    }
}
