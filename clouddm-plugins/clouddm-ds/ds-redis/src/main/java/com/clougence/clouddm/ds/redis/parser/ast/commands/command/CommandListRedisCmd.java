package com.clougence.clouddm.ds.redis.parser.ast.commands.command;

import java.io.IOException;

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
public class CommandListRedisCmd extends AbstractRedisCmd {

    private StrToken moduleName;
    private StrToken category;
    private StrToken pattern;

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.COMMAND_LIST; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("COMMAND LIST");

        if (this.moduleName != null || this.category != null || this.pattern != null) {
            writer.append(" ");
            writer.append("FILTERBY");

            if (this.moduleName != null) {
                writer.append(" MODULE ");
                writer.append(fmtString(this.moduleName));
            } else if (this.category != null) {
                writer.append(" ACLCAT ");
                writer.append(fmtString(this.category));
            } else if (this.pattern != null) {
                writer.append(" PATTERN ");
                writer.append(fmtString(this.pattern));
            }
        }
    }
}
