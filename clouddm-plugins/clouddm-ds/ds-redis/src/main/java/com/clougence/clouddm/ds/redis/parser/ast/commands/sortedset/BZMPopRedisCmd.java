package com.clougence.clouddm.ds.redis.parser.ast.commands.sortedset;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.IntToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.MinMax;
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
public class BZMPopRedisCmd extends AbstractRedisCmd {

    private IntToken       timeout;
    private IntToken       numKeys;
    private List<StrToken> keyName = new ArrayList<>();
    private MinMax         minMax;
    private IntToken       count;

    public void addKeyName(StrToken filedName) {
        if (filedName != null) {
            this.keyName.add(filedName);
        }
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.BZMPOP; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("BZMPOP");
        writer.append(" ");
        writer.append(fmtInt(this.timeout));
        writer.append(" ");
        writer.append(fmtInt(this.numKeys));
        for (StrToken keyName : this.keyName) {
            writer.append(" ");
            writer.append(fmtString(keyName));
        }
        writer.append(" ");
        writer.append(this.minMax.name());

        if (count != null) {
            writer.append(" COUNT ");
            writer.append(fmtInt(this.count));
        }
    }
}
