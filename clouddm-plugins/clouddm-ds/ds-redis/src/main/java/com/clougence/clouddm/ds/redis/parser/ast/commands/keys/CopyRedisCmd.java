package com.clougence.clouddm.ds.redis.parser.ast.commands.keys;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.IntToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.TagToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CopyRedisCmd extends AbstractRedisCmd {

    private final StrToken srcKey;
    private final StrToken dstKey;
    private IntToken       dstDB;
    private TagToken       replace;

    public CopyRedisCmd(StrToken srcKey, StrToken dstKey){
        this.srcKey = srcKey;
        this.dstKey = dstKey;
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.COPY; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("COPY");
        writer.append(" ");
        writer.append(fmtString(srcKey));
        writer.append(" ");
        writer.append(fmtString(dstKey));
        if (dstDB != null) {
            writer.append(" DB ");
            writer.append(fmtInt(dstDB));
        }
        if (replace != null) {
            writer.append(" REPLACE");
        }
    }
}
