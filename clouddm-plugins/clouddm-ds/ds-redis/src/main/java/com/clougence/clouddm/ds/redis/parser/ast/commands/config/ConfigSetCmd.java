package com.clougence.clouddm.ds.redis.parser.ast.commands.config;

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
public class ConfigSetCmd extends AbstractRedisCmd {

    private StrToken configKey;
    private StrToken configValue;

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.CONFIG_SET; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("CONFIG SET");
        writer.append(" ");
        writer.append(fmtString(this.configKey));
        writer.append(" ");
        writer.append(fmtString(this.configValue));
    }
}
