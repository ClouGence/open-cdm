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
public class RPopLPushRedisCmd extends AbstractRedisCmd {

    private StrToken srcKey;
    private StrToken dstKey;

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.RPOPLPUSH; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("RPOPLPUSH");
        writer.append(" ");
        writer.append(fmtString(this.srcKey));
        writer.append(" ");
        writer.append(fmtString(this.dstKey));
    }

}
