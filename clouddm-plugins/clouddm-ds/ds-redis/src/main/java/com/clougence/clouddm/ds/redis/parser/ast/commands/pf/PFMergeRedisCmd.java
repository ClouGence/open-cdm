package com.clougence.clouddm.ds.redis.parser.ast.commands.pf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PFMergeRedisCmd extends AbstractRedisCmd {

    private StrToken       dst;
    private List<StrToken> srcKey = new ArrayList<>();

    public void addSrcKey(StrToken key) {
        if (key != null) {
            this.srcKey.add(key);
        }
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.PFMERGE; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("PFMERGE");
        writer.append(" ");
        writer.append(fmtString(this.dst));
        if (this.srcKey != null) {
            for (StrToken keyName : this.srcKey) {
                writer.append(" ");
                writer.append(fmtString(keyName));
            }
        }
    }
}
