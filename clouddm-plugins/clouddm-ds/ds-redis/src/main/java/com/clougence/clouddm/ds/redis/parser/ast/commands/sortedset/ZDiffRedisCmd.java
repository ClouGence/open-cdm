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
public class ZDiffRedisCmd extends AbstractRedisCmd {

    private IntToken       numKeys;
    private List<StrToken> keyName = new ArrayList<>();
    private boolean        withScores;

    public void addKeyName(StrToken filedName) {
        if (filedName != null) {
            this.keyName.add(filedName);
        }
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.ZDIFF; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("ZDIFF");
        writer.append(" ");
        writer.append(fmtInt(numKeys));
        for (StrToken keyName : this.keyName) {
            writer.append(" ");
            writer.append(fmtString(keyName));
        }
        if (withScores) {
            writer.append("WITHSCORES");
        }
    }
}
