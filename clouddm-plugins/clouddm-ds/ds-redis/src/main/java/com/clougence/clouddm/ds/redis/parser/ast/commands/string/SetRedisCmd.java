package com.clougence.clouddm.ds.redis.parser.ast.commands.string;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.KeyOptToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.TagToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.TtlOptToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SetRedisCmd extends AbstractRedisCmd {

    private final StrToken keyName;
    private final StrToken value;
    private TtlOptToken    ttlOptToken;
    private TagToken       keepTtlTag;
    private KeyOptToken    optToken;
    private TagToken       getTag;

    public SetRedisCmd(StrToken keyName, StrToken value){
        this.keyName = keyName;
        this.value = value;
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.SET; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        // SET key value [EX seconds | PX milliseconds | EXAT unix-time-seconds | PXAT unix-time-milliseconds | KEEPTTL] [NX|XX] [GET]

        writer.append("SET");
        writer.append(" ");
        writer.append(fmtString(keyName));
        writer.append(" ");
        writer.append(fmtString(value));
        if (optToken != null) {
            writer.append(" ");
            writer.append(optToken.getOptionType().name());
        }

        if (getTag != null) {
            writer.append(" GET");
        }

        if (keepTtlTag != null) {
            writer.append(" KEEPTTL");
        } else if (ttlOptToken != null) {
            writer.append(" ");
            writer.append(ttlOptToken.getTtlType().name().toUpperCase());
            writer.append(" ");
            writer.append(fmtInt(ttlOptToken.getValue()));
        }
    }
}
