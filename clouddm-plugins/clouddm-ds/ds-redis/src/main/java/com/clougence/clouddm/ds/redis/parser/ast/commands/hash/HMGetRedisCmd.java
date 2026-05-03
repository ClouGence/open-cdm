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
 * @Author: mode
 * @Date: 2023-06-30 14:35
 */
@Getter
@Setter
public class HMGetRedisCmd extends AbstractRedisCmd {

    private StrToken       keyName;
    private List<StrToken> fieldName = new ArrayList<>();

    public void addKey(StrToken key) {
        if (key != null) {
            this.fieldName.add(key);
        }
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.HMGET; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("HMGET");
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
