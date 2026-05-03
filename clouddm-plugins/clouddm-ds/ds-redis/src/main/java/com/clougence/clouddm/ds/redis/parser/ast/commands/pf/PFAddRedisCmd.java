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
public class PFAddRedisCmd extends AbstractRedisCmd {

    private StrToken       keyName;
    private List<StrToken> element = new ArrayList<>();

    public void addElement(StrToken key) {
        if (key != null) {
            this.element.add(key);
        }
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.PFADD; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("PFADD");
        writer.append(" ");
        writer.append(fmtString(this.keyName));
        if (this.element != null) {
            for (StrToken keyName : this.element) {
                writer.append(" ");
                writer.append(fmtString(keyName));
            }
        }
    }
}
