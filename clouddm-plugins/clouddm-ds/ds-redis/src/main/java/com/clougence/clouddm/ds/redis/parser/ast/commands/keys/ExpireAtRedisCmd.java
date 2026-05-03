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
public class ExpireAtRedisCmd extends AbstractRedisCmd {

    private final StrToken keyName;
    private final IntToken unixTimestampSec;
    private KeyOptToken    keyOpt;

    public ExpireAtRedisCmd(StrToken keyName, IntToken unixTimestampSec){
        this.keyName = keyName;
        this.unixTimestampSec = unixTimestampSec;
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.EXPIREAT; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        // EXPIREAT key unix-time-seconds [NX|XX|GT|LT]

        writer.append("EXPIREAT");
        writer.append(" ");
        writer.append(fmtString(keyName));
        writer.append(" ");
        writer.append(fmtInt(unixTimestampSec));
        if (keyOpt != null) {
            writer.append(" ");
            writer.append(keyOpt.getOptionType().name().toUpperCase());
        }
    }
}
