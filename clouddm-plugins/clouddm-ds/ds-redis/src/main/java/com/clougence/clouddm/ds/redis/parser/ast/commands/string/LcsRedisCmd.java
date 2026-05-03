package com.clougence.clouddm.ds.redis.parser.ast.commands.string;

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
public class LcsRedisCmd extends AbstractRedisCmd {

    private final StrToken key1Name;
    private final StrToken key2Name;
    private TagToken       lenToken;
    private TagToken       idxToken;
    private IntToken       minMatchLen;
    private TagToken       withMatchLen;

    public LcsRedisCmd(StrToken key1Name, StrToken key2Name){
        this.key1Name = key1Name;
        this.key2Name = key2Name;
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.LCS; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        // LCS key1 key2 [LEN] [IDX] [MINMATCHLEN len] [WITHMATCHLEN]

        writer.append("LCS");
        writer.append(" ");
        writer.append(fmtString(key1Name));
        writer.append(" ");
        writer.append(fmtString(key2Name));

        if (lenToken != null) {
            writer.append(" LEN");
        }
        if (idxToken != null) {
            writer.append(" IDX");
        }
        if (minMatchLen != null) {
            writer.append(" MINMATCHLEN ");
            writer.append(fmtInt(minMatchLen));
        }
        if (withMatchLen != null) {
            writer.append(" WITHMATCHLEN");
        }
    }
}
