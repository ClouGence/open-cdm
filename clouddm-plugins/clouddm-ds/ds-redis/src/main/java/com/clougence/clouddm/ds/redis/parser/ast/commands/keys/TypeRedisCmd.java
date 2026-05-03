package com.clougence.clouddm.ds.redis.parser.ast.commands.keys;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TypeRedisCmd extends AbstractRedisCmd {

    private final List<StrToken> keyName;

    public TypeRedisCmd(){
        this.keyName = new ArrayList<>();
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.TYPE; }

    public void addKeyName(StrToken keyName) {
        if (keyName != null) {
            this.keyName.add(keyName);
        }
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        // TYPE key
        writer.append("TYPE");

        for (StrToken strToken : this.keyName) {
            writer.append(" ");
            writer.append(fmtString(strToken));
        }
    }
}
