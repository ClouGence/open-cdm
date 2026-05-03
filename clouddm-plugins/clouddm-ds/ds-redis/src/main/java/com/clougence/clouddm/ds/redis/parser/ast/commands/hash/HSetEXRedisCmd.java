package com.clougence.clouddm.ds.redis.parser.ast.commands.hash;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.*;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: mode
 * @Date: 2023-06-30 14:35
 */
@Getter
@Setter
public class HSetEXRedisCmd extends AbstractRedisCmd {

    private StrToken                key;
    private HSetExOpt               fxOpt;

    private TtlOptToken             ttlOptToken;
    private TagToken                keepTtlTag;

    private IntToken                numFields;
    private List<KeyAndStringToken> keyValues = new ArrayList<>();

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.HSETEX; }

    public void addKeyValue(KeyAndStringToken kvToken) {
        if (kvToken != null) {
            this.keyValues.add(kvToken);
        }
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("HSETEX");
        writer.append(" ");
        writer.append(fmtString(this.key));

        if (this.fxOpt != null) {
            writer.append(" ");
            writer.append(this.fxOpt.name());
        }

        if (this.ttlOptToken != null) {
            writer.append(" ");
            writer.append(this.ttlOptToken.getTtlType().name().toUpperCase());
            writer.append(" ");
            writer.append(fmtInt(this.ttlOptToken.getValue()));
        } else if (this.keepTtlTag != null) {
            writer.append(" KEEPTTL");
        }

        writer.append(" FIELDS ");
        writer.append(fmtInt(this.numFields));
        for (KeyAndStringToken keyVal : this.keyValues) {
            writer.append(" ");
            writer.append(fmtString(keyVal.getKeyName()));
            writer.append(" ");
            writer.append(fmtString(keyVal.getStringValue()));
        }
    }

    public static enum HSetExOpt {
        FNX,
        FXX
    }
}
