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
public class PExpireRedisCmd extends AbstractRedisCmd {

    private final StrToken keyName;
    private final IntToken milliseconds;
    private KeyOptToken    keyOpt;

    public PExpireRedisCmd(StrToken keyName, IntToken milliseconds){
        this.keyName = keyName;
        this.milliseconds = milliseconds;
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.PEXPIRE; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        // PEXPIRE key milliseconds [NX|XX|GT|LT]

        writer.append("PEXPIRE");
        writer.append(" ");
        writer.append(fmtString(keyName));
        writer.append(" ");
        writer.append(fmtInt(milliseconds));
        if (keyOpt != null) {
            writer.append(" ");
            writer.append(keyOpt.getOptionType().name().toUpperCase());
        }
    }
}
