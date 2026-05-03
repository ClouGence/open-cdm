package com.clougence.clouddm.ds.redis.parser.ast.commands.tx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;

@Getter
public class WatchRedisCmd extends AbstractRedisCmd {

    private final List<StrToken> watchKeys = new ArrayList<>();

    public void addKey(StrToken key) {
        if (key != null) {
            this.watchKeys.add(key);
        }
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.WATCH; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("WATCH");
        for (StrToken keyName : watchKeys) {
            writer.append(" ");
            writer.append(fmtString(keyName));
        }
    }
}
