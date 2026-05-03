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
public class SinterStoreRedisCmd extends AbstractRedisCmd {

    private StrToken       dst;
    private List<StrToken> keys = new ArrayList<>();

    public void addKey(StrToken keyName) {
        if (keyName != null) {
            this.keys.add(keyName);
        }
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.SINTERSTORE; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("SINTERSTORE");
        writer.append(" ");
        writer.append(fmtString(this.dst));
        for (StrToken keyName : this.keys) {
            writer.append(" ");
            writer.append(fmtString(keyName));
        }
    }
}
