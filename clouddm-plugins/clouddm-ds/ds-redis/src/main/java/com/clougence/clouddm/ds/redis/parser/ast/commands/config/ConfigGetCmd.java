package com.clougence.clouddm.ds.redis.parser.ast.commands.config;

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
 * @date 2024/1/22 14:56
*/
@Getter
@Setter
public class ConfigGetCmd extends AbstractRedisCmd {

    private List<StrToken> patterns;

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.CONFIG_GET; }

    public void addPattern(StrToken name) {
        if (this.patterns == null) {
            this.patterns = new ArrayList<>();
        }
        this.patterns.add(name);
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("CONFIG GET");

        for (StrToken pattern : this.patterns) {
            writer.append(" ");
            writer.append(fmtString(pattern));
        }
    }

}
