package com.clougence.clouddm.ds.redis.parser.ast.commands.set;

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
public class SInterRedisCmd extends AbstractRedisCmd {

    private List<StrToken> keyNames = new ArrayList<>();

    public void addKeyName(StrToken keyName) {
        if (keyName != null) {
            this.keyNames.add(keyName);
        }
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.SINTER; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("SINTER");
        for (StrToken keyName : this.keyNames) {
            writer.append(" ");
            writer.append(fmtString(keyName));
        }
    }
}
