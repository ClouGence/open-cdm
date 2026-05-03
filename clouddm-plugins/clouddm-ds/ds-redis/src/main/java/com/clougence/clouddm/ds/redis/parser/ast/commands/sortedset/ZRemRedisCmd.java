package com.clougence.clouddm.ds.redis.parser.ast.commands.sortedset;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: mode
 * @Date: 2023-07-10 11:15
 */
@Getter
@Setter
public class ZRemRedisCmd extends AbstractRedisCmd {

    private StrToken       keyName;
    private List<StrToken> fieldNames = new ArrayList<>();

    public void addField(StrToken filed) {
        if (filed != null) {
            this.fieldNames.add(filed);
        }
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.ZREM; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("ZREM");
        writer.append(" ");
        writer.append(fmtString(this.keyName));
        for (StrToken field : this.fieldNames) {
            writer.append(" ");
            writer.append(fmtString(field));
        }
    }
}
