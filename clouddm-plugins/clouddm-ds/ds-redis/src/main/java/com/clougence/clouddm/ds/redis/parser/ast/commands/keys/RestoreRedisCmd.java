package com.clougence.clouddm.ds.redis.parser.ast.commands.keys;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.IntToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.TagToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestoreRedisCmd extends AbstractRedisCmd {

    private final StrToken keyName;
    private final IntToken ttl;
    private final StrToken serializedValue;
    private TagToken       replace;
    private TagToken       absTTL;
    private IntToken       idleTime;
    private IntToken       frequency;

    public RestoreRedisCmd(StrToken keyName, IntToken ttl, StrToken serializedValue){
        this.keyName = keyName;
        this.ttl = ttl;
        this.serializedValue = serializedValue;
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.RESTORE; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        // RESTORE key ttl serialized-value [REPLACE] [ABSTTL] [IDLETIME seconds] [FREQ frequency]

        writer.append("RESTORE");
        writer.append(" ");
        writer.append(fmtString(keyName));
        writer.append(" ");
        writer.append(fmtInt(ttl));
        writer.append(" ");
        writer.append(fmtString(serializedValue));
        if (replace != null) {
            writer.append(" REPLACE");
        }
        if (absTTL != null) {
            writer.append(" ABSTTL");
        }
        if (idleTime != null) {
            writer.append(" IDLETIME ");
            writer.append(fmtInt(idleTime));
        }
        if (frequency != null) {
            writer.append(" FREQ ");
            writer.append(fmtInt(frequency));
        }
    }
}
