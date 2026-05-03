package com.clougence.clouddm.ds.redis.parser.ast.commands.set;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
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
public class SMISMemberRedisCmd extends AbstractRedisCmd {

    private StrToken       keyName;
    private List<StrToken> members = new ArrayList<>();

    public void addMember(StrToken keyName) {
        if (keyName != null) {
            this.members.add(keyName);
        }
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.SMISMEMBER; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("SMISMEMBER");
        writer.append(" ");
        writer.append(fmtString(this.keyName));
        for (StrToken keyName : this.members) {
            writer.append(" ");
            writer.append(fmtString(keyName));
        }
    }
}
