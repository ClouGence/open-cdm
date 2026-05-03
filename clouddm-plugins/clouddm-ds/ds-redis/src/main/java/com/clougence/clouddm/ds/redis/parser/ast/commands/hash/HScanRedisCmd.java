package com.clougence.clouddm.ds.redis.parser.ast.commands.hash;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.IntToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.TagToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: mode
 * @Date: 2023-06-30 14:35
 */
@Getter
@Setter
public class HScanRedisCmd extends AbstractRedisCmd {

    private StrToken keyName;
    private IntToken cursor;
    private StrToken pattern;
    private IntToken count;
    private TagToken noValues;

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.HSCAN; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("HSCAN");
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
        if (this.noValues != null) {
            writer.append(" NOVALUES");
        }
    }
}
