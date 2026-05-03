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
public class LRemRedisCmd extends AbstractRedisCmd {

    private StrToken keyName;
    private IntToken count;
    private StrToken element;

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.LREM; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("LREM");
        writer.append(" ");
        writer.append(fmtString(this.keyName));
        writer.append(" ");
        writer.append(fmtInt(this.count));
        writer.append(" ");
        writer.append(fmtString(this.element));
    }
}
