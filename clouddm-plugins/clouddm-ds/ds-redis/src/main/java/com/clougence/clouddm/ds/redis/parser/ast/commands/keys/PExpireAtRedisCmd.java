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
public class PExpireAtRedisCmd extends AbstractRedisCmd {

    private final StrToken keyName;
    private final IntToken unixTimeSeconds;
    private KeyOptToken    keyOpt;

    public PExpireAtRedisCmd(StrToken keyName, IntToken unixTimeSeconds){
        this.keyName = keyName;
        this.unixTimeSeconds = unixTimeSeconds;
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.PEXPIREAT; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        // PEXPIREAT key unix-time-milliseconds [NX|XX|GT|LT]

        writer.append("PEXPIREAT");
        writer.append(" ");
        writer.append(fmtString(keyName));
        writer.append(" ");
        writer.append(fmtInt(unixTimeSeconds));
        if (keyOpt != null) {
            writer.append(" ");
            writer.append(keyOpt.getOptionType().name().toUpperCase());
        }
    }
}
