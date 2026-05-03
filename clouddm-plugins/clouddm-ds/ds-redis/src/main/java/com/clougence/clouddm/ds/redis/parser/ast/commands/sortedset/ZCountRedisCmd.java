package com.clougence.clouddm.ds.redis.parser.ast.commands.sortedset;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrIntToken;
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
public class ZCountRedisCmd extends AbstractRedisCmd {

    private StrToken    keyName;
    private StrIntToken min;
    private StrIntToken max;

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.ZCOUNT; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("ZCOUNT");
        writer.append(" ");
        writer.append(fmtString(keyName));
        writer.append(" ");
        writer.append(fmtString(min));
        writer.append(" ");
        writer.append(fmtString(max));
    }
}
