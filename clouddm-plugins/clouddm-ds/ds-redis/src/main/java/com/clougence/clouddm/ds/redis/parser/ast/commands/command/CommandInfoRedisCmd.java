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
 * @author mode
 * @date 2024/1/22 14:56
 */
@Getter
@Setter
public class CommandInfoRedisCmd extends AbstractRedisCmd {

    private List<StrToken> command = new ArrayList<>();

    public void addCommand(StrToken user) {
        this.command.add(user);
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.COMMAND_INFO; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("COMMAND INFO");
        for (StrToken user : this.command) {
            writer.append(" ");
            writer.append(fmtString(user));
        }
    }
}
