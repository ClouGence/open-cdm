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
public class AclDelUserRedisCmd extends AbstractRedisCmd {

    private List<StrToken> users = new ArrayList<>();

    public void addUser(StrToken user) {
        this.users.add(user);
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.ACL_DELUSER; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("ACL DELUSER");
        for (StrToken user : this.users) {
            writer.append(" ");
            writer.append(fmtString(user));
        }
    }
}
