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
public class LTrimRedisCmd extends AbstractRedisCmd {

    private StrToken keyName;
    private IntToken startNum;
    private IntToken stopNum;

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.LTRIM; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("LTRIM");
        writer.append(" ");
        writer.append(fmtString(this.keyName));
        writer.append(" ");
        writer.append(fmtInt(this.startNum));
        writer.append(" ");
        writer.append(fmtInt(this.stopNum));
    }
}
