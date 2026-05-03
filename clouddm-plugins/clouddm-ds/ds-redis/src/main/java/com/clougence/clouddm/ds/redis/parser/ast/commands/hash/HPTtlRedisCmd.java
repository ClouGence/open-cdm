package com.clougence.clouddm.ds.redis.parser.ast.commands.hash;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.IntToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: mode
 * @Date: 2023-06-30 14:35
 */
@Getter
@Setter
public class HPTtlRedisCmd extends AbstractRedisCmd {

    private StrToken       key;
    private IntToken       numFields;
    private List<StrToken> filedNames = new ArrayList<>();

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.HPTTL; }

    public void addField(StrToken field) {
        if (field != null) {
            this.filedNames.add(field);
        }
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("HPTTL");
        writer.append(" ");
        writer.append(fmtString(this.key));
        writer.append(" FIELDS ");
        writer.append(fmtInt(this.numFields));
        for (StrToken filed : this.filedNames) {
            writer.append(" ");
            writer.append(fmtString(filed));
        }
    }
}
