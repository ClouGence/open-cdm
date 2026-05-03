package com.clougence.clouddm.ds.redis.parser.ast.commands.keys;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.IntToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.KeyOptToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpireRedisCmd extends AbstractRedisCmd {

    private final StrToken keyName;
    private final IntToken secondsSec;
    private KeyOptToken    keyOpt;

    public ExpireRedisCmd(StrToken keyName, IntToken secondsSec){
        this.keyName = keyName;
        this.secondsSec = secondsSec;
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.EXPIRE; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        // EXPIRE key seconds [NX|XX|GT|LT]

        writer.append("EXPIRE");
        writer.append(" ");
        writer.append(fmtString(keyName));
        writer.append(" ");
        writer.append(fmtInt(secondsSec));
        if (keyOpt != null) {
            writer.append(" ");
            writer.append(keyOpt.getOptionType().name().toUpperCase());
        }
    }
}
