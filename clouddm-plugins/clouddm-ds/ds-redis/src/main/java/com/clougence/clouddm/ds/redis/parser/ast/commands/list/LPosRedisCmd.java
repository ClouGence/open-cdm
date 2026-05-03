package com.clougence.clouddm.ds.redis.parser.ast.commands.list;

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
 * @Date: 2023-06-30 14:35
 */
@Getter
@Setter
public class LPosRedisCmd extends AbstractRedisCmd {

    private StrToken keyName;
    private StrToken element;
    private IntToken rank;
    private IntToken count;
    private IntToken maxLen;

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.LPOS; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("LPOS");
        writer.append(" ");
        writer.append(fmtString(this.keyName));
        writer.append(" ");
        writer.append(fmtString(this.element));
        if (this.rank != null) {
            writer.append(" ");
            writer.append("RANK");
            writer.append(" ");
            writer.append(fmtInt(this.rank));
        }
        if (this.count != null) {
            writer.append(" ");
            writer.append("COUNT");
            writer.append(" ");
            writer.append(fmtInt(this.count));
        }
        if (this.maxLen != null) {
            writer.append(" ");
            writer.append("MAXLEN");
            writer.append(" ");
            writer.append(fmtInt(this.maxLen));
        }
    }

}
