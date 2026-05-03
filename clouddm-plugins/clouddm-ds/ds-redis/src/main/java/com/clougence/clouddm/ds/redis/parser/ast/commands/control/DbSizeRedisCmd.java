package com.clougence.clouddm.ds.redis.parser.ast.commands.control;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.dslpaser.foramt.FmtWriter;

/**
 * @author Ekko
 * @date 2024/1/10 15:29
*/
public class DbSizeRedisCmd extends AbstractRedisCmd {

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.DBSIZE; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("DBSIZE");
    }
}
