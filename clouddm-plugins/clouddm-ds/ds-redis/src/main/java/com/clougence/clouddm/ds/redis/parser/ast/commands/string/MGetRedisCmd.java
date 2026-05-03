package com.clougence.clouddm.ds.redis.parser.ast.commands.string;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;

@Getter
public class MGetRedisCmd extends AbstractRedisCmd {

    private final List<StrToken> keys = new ArrayList<>();

    public void addKey(StrToken key) {
        if (key != null) {
            this.keys.add(key);
        }
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.MGET; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        // MGET key [key ...]
        writer.append("MGET");
        for (StrToken keyName : keys) {
            writer.append(" ");
            writer.append(fmtString(keyName));
        }
    }
}
