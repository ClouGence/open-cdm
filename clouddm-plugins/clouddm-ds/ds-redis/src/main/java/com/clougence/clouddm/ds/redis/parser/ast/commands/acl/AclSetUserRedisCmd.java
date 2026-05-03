package com.clougence.clouddm.ds.redis.parser.ast.commands.acl;

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
 * @author mode
 * @date 2024/1/22 14:56
 */
@Getter
@Setter
public class AclSetUserRedisCmd extends AbstractRedisCmd {

    private StrToken       username;
    private List<StrToken> rule;

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.ACL_SETUSER; }

    public void addRule(StrToken pop) {
        if (this.rule == null) {
            this.rule = new ArrayList<>();
        }
        this.rule.add(pop);
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("ACL SETUSER");
        writer.append(" ");
        writer.append(fmtString(this.username));
        if (this.rule != null) {
            for (StrToken rule : this.rule) {
                writer.append(" ");
                writer.append(fmtString(rule));
            }
        }
    }
}
