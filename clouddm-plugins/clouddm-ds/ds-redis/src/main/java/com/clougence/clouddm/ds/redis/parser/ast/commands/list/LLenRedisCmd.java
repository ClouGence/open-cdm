package com.clougence.clouddm.ds.redis.parser.ast.commands.list;

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
public class LLenRedisCmd extends AbstractRedisCmd {

    private StrToken keyName;

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.LLEN; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("LLEN");
        writer.append(" ");
        writer.append(fmtString(this.keyName));
    }
}
