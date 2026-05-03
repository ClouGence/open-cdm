package com.clougence.clouddm.ds.redis.parser.ast.commands.bit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.commands.bit.BitOPTypeEnum;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BitOPRedisCmd extends AbstractRedisCmd {

    private BitOPTypeEnum opType;
    private StrToken      destKey;
    private List<StrToken> keyNames = new ArrayList<>();

    public void addKey(StrToken key) {
        if (key != null) {
            this.keyNames.add(key);
        }
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.BITOP; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("BITOP");
        writer.append(" ");
        writer.append(this.opType.name());
        writer.append(" ");
        writer.append(fmtString(this.destKey));
        for (StrToken keyName : this.keyNames) {
            writer.append(" ");
            writer.append(fmtString(keyName));
        }
    }
}
