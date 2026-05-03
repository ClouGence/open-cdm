package com.clougence.clouddm.ds.redis.parser.ast.commands.keys;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.IntToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScanRedisCmd extends AbstractRedisCmd {

    private final IntToken cursor;
    private final StrToken pattern;
    private IntToken       count;
    private StrToken       type;

    public ScanRedisCmd(IntToken cursor, StrToken pattern){
        this.cursor = cursor;
        this.pattern = pattern;
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.SCAN; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        // SCAN cursor [MATCH pattern] [COUNT count] [TYPE type]

        writer.append("SCAN");
        writer.append(" ");
        writer.append(fmtInt(cursor));
        if (pattern != null) {
            writer.append(" MATCH ");
            writer.append(fmtString(pattern));
        }
        if (count != null) {
            writer.append(" COUNT ");
            writer.append(fmtInt(count));
        }
        if (type != null) {
            writer.append(" TYPE ");
            writer.append(fmtString(type));
        }
    }
}
