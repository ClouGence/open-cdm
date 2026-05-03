package com.clougence.clouddm.ds.redis.parser.ast.commands.hash;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
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
public class HStrLenRedisCmd extends AbstractRedisCmd {

    private StrToken keyName;
    private StrToken fieldName;

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.HSTRLEN; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("HSTRLEN");
        writer.append(" ");
        writer.append(fmtString(this.keyName));
        writer.append(" ");
        writer.append(fmtString(this.fieldName));
    }
}
