package com.clougence.clouddm.ds.redis.parser.ast.commands.set;

import java.io.IOException;

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
public class SRandMemberRedisCmd extends AbstractRedisCmd {

    private StrToken keyName;
    private IntToken count;

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.SRANDMEMBER; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("SRANDMEMBER");
        writer.append(" ");
        writer.append(fmtString(this.keyName));
        if (this.count != null) {
            writer.append(" ");
            writer.append(fmtInt(this.count));
        }
    }
}
