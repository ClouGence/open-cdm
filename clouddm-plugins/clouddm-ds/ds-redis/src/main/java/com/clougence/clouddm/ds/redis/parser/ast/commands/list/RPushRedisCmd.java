package com.clougence.clouddm.ds.redis.parser.ast.commands.list;

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
 * @Author: Ekko
 * @Date: 2023-06-30 14:35
 */
@Getter
@Setter
public class RPushRedisCmd extends AbstractRedisCmd {

    private StrToken       keyName;
    private List<StrToken> elements = new ArrayList<>();

    public void addStrValue(StrToken strValue) {
        if (strValue != null) {
            this.elements.add(strValue);
        }
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.RPUSH; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("RPUSH");
        writer.append(" ");
        writer.append(fmtString(this.keyName));
        for (StrToken strValue : elements) {
            writer.append(" ");
            writer.append(fmtString(strValue));
        }
    }
}
