package com.clougence.clouddm.ds.redis.parser.ast.commands.sortedset;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.IntToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Ekko
 * @Date: 2023-07-10 11:15
 */
@Getter
@Setter
public class ZSCanRedisCmd extends AbstractRedisCmd {

    private StrToken keyName;
    private IntToken cursor;
    private StrToken pattern;
    private IntToken count;

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.ZSCAN; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("ZSCAN");
        writer.append(" ");
        writer.append(fmtString(this.keyName));
        writer.append(" ");
        writer.append(fmtInt(this.cursor));
        if (this.pattern != null) {
            writer.append(" MATCH ");
            writer.append(fmtString(this.pattern));
        }
        if (this.count != null) {
            writer.append(" COUNT ");
            writer.append(fmtInt(this.count));
        }
    }
}
