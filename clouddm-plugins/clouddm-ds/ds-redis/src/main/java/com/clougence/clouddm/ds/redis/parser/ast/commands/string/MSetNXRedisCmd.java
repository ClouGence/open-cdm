package com.clougence.clouddm.ds.redis.parser.ast.commands.string;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.KeyAndStringToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;

@Getter
public class MSetNXRedisCmd extends AbstractRedisCmd {

    private final List<KeyAndStringToken> keyValues = new ArrayList<>();

    public void addKeyValue(KeyAndStringToken kvToken) {
        if (kvToken != null) {
            this.keyValues.add(kvToken);
        }
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.MSETNX; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        // MSETNX key value [key value ...]

        writer.append("MSETNX");
        for (KeyAndStringToken keyVal : keyValues) {
            writer.append(" ");
            writer.append(fmtString(keyVal.getKeyName()));
            writer.append(" ");
            writer.append(fmtString(keyVal.getStringValue()));
        }
    }
}
