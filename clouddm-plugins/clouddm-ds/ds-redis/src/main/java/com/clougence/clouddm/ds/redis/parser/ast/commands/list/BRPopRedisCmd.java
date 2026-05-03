package com.clougence.clouddm.ds.redis.parser.ast.commands.list;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.IntToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Ekko
 * @Date: 2023-06-30 14:35
 */
@Getter
@Setter
public class BRPopRedisCmd extends AbstractRedisCmd {

    private IntToken       timeout;
    private List<StrToken> keyNames = new ArrayList<>();

    public void addKeyNames(StrToken keyName) {
        if (keyName != null) {
            this.keyNames.add(keyName);
        }
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.BRPOP; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("BRPOP");
        for (StrToken keyName : keyNames) {
            writer.append(" ");
            writer.append(fmtString(keyName));
        }
        writer.append(" ");
        writer.append(fmtInt(this.timeout));
    }
}
