package com.clougence.clouddm.ds.redis.parser.ast.commands.command;

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
 * @author Ekko
 * @date 2024/1/10 15:29
*/
@Getter
@Setter
public class CommandGetKeysAndFlagsRedisCmd extends AbstractRedisCmd {

    private final List<StrToken> args = new ArrayList<>();
    private StrToken             command;

    public void addArg(StrToken arg) {
        this.args.add(arg);
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.COMMAND_GETKEYSANDFLAGS; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("COMMAND GETKEYSANDFLAGS");
        writer.append(" ");
        writer.append(fmtString(command));
        for (StrToken arg : args) {
            writer.append(" ");
            writer.append(fmtString(arg));
        }
    }
}
