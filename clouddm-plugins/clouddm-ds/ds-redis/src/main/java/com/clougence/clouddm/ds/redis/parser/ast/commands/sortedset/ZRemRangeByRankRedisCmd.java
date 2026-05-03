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
 * @Author: mode
 * @Date: 2023-07-10 11:15
 */
@Getter
@Setter
public class ZRemRangeByRankRedisCmd extends AbstractRedisCmd {

    private StrToken keyName;
    private IntToken start;
    private IntToken end;

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.ZREMRANGEBYRANK; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("ZREMRANGEBYRANK");
        writer.append(" ");
        writer.append(fmtString(keyName));
        writer.append(" ");
        writer.append(fmtInt(this.start));
        writer.append(" ");
        writer.append(fmtInt(this.end));
    }
}
