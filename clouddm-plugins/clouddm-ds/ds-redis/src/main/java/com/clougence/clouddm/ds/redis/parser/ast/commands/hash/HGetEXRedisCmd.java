package com.clougence.clouddm.ds.redis.parser.ast.commands.hash;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.IntToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.TagToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.TtlOptToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: mode
 * @Date: 2023-06-30 14:35
 */
@Getter
@Setter
public class HGetEXRedisCmd extends AbstractRedisCmd {

    private StrToken       key;
    private TtlOptToken    ttlOptToken;
    private TagToken       persistTag;

    private IntToken       numFields;
    private List<StrToken> fieldNames = new ArrayList<>();

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.HGETEX; }

    public void addField(StrToken strToken) {
        if (strToken != null) {
            this.fieldNames.add(strToken);
        }
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("HGETEX");
        writer.append(" ");
        writer.append(fmtString(this.key));

        if (this.ttlOptToken != null) {
            writer.append(" ");
            writer.append(this.ttlOptToken.getTtlType().name().toUpperCase());
            writer.append(" ");
            writer.append(fmtInt(this.ttlOptToken.getValue()));
        } else if (this.persistTag != null) {
            writer.append(" ");
            writer.append("PERSIST");
        }

        writer.append(" FIELDS ");
        writer.append(fmtInt(this.numFields));
        for (StrToken fieldName : this.fieldNames) {
            writer.append(" ");
            writer.append(fmtString(fieldName));
        }
    }
}
