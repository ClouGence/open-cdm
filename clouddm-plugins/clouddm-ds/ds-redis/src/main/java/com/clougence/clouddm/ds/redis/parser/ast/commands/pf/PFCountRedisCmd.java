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
public class PFCountRedisCmd extends AbstractRedisCmd {

    private List<StrToken> keys = new ArrayList<>();

    public void addKey(StrToken key) {
        if (key != null) {
            this.keys.add(key);
        }
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.PFCOUNT; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("PFCOUNT");
        if (this.keys != null) {
            for (StrToken keyName : this.keys) {
                writer.append(" ");
                writer.append(fmtString(keyName));
            }
        }
    }
}
