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
public class AclDryRunRedisCmd extends AbstractRedisCmd {

    private List<StrToken> users    = new ArrayList<>();
    private List<StrToken> commands = new ArrayList<>();
    private List<StrToken> args     = new ArrayList<>();

    public void addUser(StrToken user) {
        this.users.add(user);
    }

    public void addCommand(StrToken command) {
        this.commands.add(command);
    }

    public void addArg(StrToken arg) {
        this.args.add(arg);
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.ACL_DRYRUN; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("ACL DRYRUN");
        for (StrToken user : this.users) {
            writer.append(" ");
            writer.append(fmtString(user));
        }
        for (StrToken command : this.commands) {
            writer.append(" ");
            writer.append(fmtString(command));
        }
        for (StrToken arg : this.args) {
            writer.append(" ");
            writer.append(fmtString(arg));
        }
    }
}
