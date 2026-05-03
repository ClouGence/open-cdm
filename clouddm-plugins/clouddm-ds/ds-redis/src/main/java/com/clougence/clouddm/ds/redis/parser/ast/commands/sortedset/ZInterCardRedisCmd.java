package com.clougence.clouddm.ds.redis.parser.ast.commands.sortedset;

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
 * @Date: 2023-07-10 11:15
 */
@Getter
@Setter
public class ZInterCardRedisCmd extends AbstractRedisCmd {

    private IntToken       numKeys;
    private List<StrToken> keyName = new ArrayList<>();
    private IntToken       limit;

    public void addKeyName(StrToken key) {
        if (key != null) {
            this.keyName.add(key);
        }
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.ZINTERCARD; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("ZINTERCARD");
        writer.append(" ");
        writer.append(fmtInt(this.numKeys));
        for (StrToken keyName : this.keyName) {
            writer.append(" ");
            writer.append(fmtString(keyName));
        }

        if (limit != null) {
            writer.append(" ");
            writer.append("LIMIT");
            writer.append(" ");
            writer.append(fmtInt(limit));
        }
    }
}
