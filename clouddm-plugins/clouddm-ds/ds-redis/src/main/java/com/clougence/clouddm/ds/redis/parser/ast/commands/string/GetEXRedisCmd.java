package com.clougence.clouddm.ds.redis.parser.ast.commands.string;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.TagToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.TtlOptToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetEXRedisCmd extends AbstractRedisCmd {

    private final StrToken keyName;
    private TtlOptToken    ttlOptToken;
    private TagToken       persistToken;

    public GetEXRedisCmd(StrToken keyName){
        this.keyName = keyName;
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.GETEX; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        // GETEX key [EX seconds | PX milliseconds | EXAT unix-time-seconds | PXAT unix-time-milliseconds | PERSIST]

        writer.append("GETEX");
        writer.append(" ");
        writer.append(fmtString(keyName));

        if (persistToken != null) {
            writer.append(" PERSIST");
        } else if (ttlOptToken != null) {
            writer.append(" ");
            writer.append(ttlOptToken.getTtlType().name().toUpperCase());
            writer.append(" ");
            writer.append(fmtInt(ttlOptToken.getValue()));
        }
    }
}
