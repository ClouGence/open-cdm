package com.clougence.clouddm.ds.redis.parser.ast.commands.hash;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.KeyAndStringToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Ekko
 * @Date: 2023-06-30 14:35
 */
@Getter
@Setter
public class HSetRedisCmd extends AbstractRedisCmd {

    private StrToken                keyName;
    private List<KeyAndStringToken> keyValues = new ArrayList<>();

    public void addKeyValue(KeyAndStringToken kvToken) {
        if (kvToken != null) {
            this.keyValues.add(kvToken);
        }
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.HSET; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("HSET");
        writer.append(" ");
        writer.append(fmtString(this.keyName));
        for (KeyAndStringToken keyVal : keyValues) {
            writer.append(" ");
            writer.append(fmtString(keyVal.getKeyName()));
            writer.append(" ");
            writer.append(fmtString(keyVal.getStringValue()));
        }
    }
}
