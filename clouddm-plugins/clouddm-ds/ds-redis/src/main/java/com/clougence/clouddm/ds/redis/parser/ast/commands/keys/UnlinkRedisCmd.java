package com.clougence.clouddm.ds.redis.parser.ast.commands.keys;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;

@Getter
public class UnlinkRedisCmd extends AbstractRedisCmd {

    private final List<StrToken> keyNames = new ArrayList<>();

    public void addKey(StrToken key) {
        if (key != null) {
            this.keyNames.add(key);
        }
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.UNLINK; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        // UNLINK key [key ...]

        writer.append("UNLINK");

        for (StrToken keyName : keyNames) {
            writer.append(" ");
            writer.append(fmtString(keyName));
        }
    }
}
