package com.clougence.clouddm.ds.redis.parser.ast.commands.hash;

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
public class HDelRedisCmd extends AbstractRedisCmd {

    private StrToken       keyName;
    private List<StrToken> fieldName = new ArrayList<>();

    public void addKey(StrToken key) {
        if (key != null) {
            this.fieldName.add(key);
        }
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.HDEL; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("HDEL");
        writer.append(" ");
        writer.append(fmtString(this.keyName));
        if (this.fieldName != null) {
            for (StrToken keyName : this.fieldName) {
                writer.append(" ");
                writer.append(fmtString(keyName));
            }
        }
    }
}
