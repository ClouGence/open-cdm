package com.clougence.clouddm.ds.redis.parser.ast.commands.bit;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.commands.bit.BitCountTypeEnum;
import com.clougence.clouddm.ds.redis.parser.ast.token.IntToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BitCountRedisCmd extends AbstractRedisCmd {

    private final StrToken   keyName;
    private IntToken         start;
    private IntToken         end;
    private BitCountTypeEnum typeEnum;

    public BitCountRedisCmd(StrToken keyName){
        this.keyName = keyName;
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.BITCOUNT; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("BITCOUNT");
        writer.append(" ");
        writer.append(fmtString(keyName));

        if (this.start != null && this.end != null) {
            writer.append(" ");
            writer.append(fmtInt(this.start));
            writer.append(" ");
            writer.append(fmtInt(this.end));
        }

        if (this.typeEnum != null) {
            writer.append(" ");
            writer.append(this.typeEnum.name());
        }
    }
}
