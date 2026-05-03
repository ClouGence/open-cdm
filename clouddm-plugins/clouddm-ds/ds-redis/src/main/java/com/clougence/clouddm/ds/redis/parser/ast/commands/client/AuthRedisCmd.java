package com.clougence.clouddm.ds.redis.parser.ast.commands.client;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Ekko
 * @date 2024/1/22 14:56
*/
@Getter
@Setter
public class AuthRedisCmd extends AbstractRedisCmd {

    private StrToken username;
    private StrToken password;

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.AUTH; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("AUTH");
        if (this.username != null) {
            writer.append(" ");
            writer.append(fmtString(this.username));
        }

        writer.append(" ");
        writer.append(fmtString(this.password));
    }
}
