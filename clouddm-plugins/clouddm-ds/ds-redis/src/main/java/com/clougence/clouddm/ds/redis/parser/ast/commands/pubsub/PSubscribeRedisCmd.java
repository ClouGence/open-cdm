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
public class PSubscribeRedisCmd extends AbstractRedisCmd {

    private final List<StrToken> patterns = new ArrayList<>();

    public void addPattern(StrToken pattern) {
        if (pattern != null) {
            this.patterns.add(pattern);
        }
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.PSUBSCRIBE; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("PSUBSCRIBE");
        for (StrToken keyName : this.patterns) {
            writer.append(" ");
            writer.append(fmtString(keyName));
        }
    }
}
