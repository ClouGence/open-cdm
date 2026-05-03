package com.clougence.clouddm.ds.redis.parser.ast.commands.acl;

import java.io.IOException;

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
public class AclCatRedisCmd extends AbstractRedisCmd {

    private StrToken category;

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.ACL_CAT; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("ACL CAT");
        if (this.category != null) {
            writer.append(" ");
            writer.append(fmtString(this.category));
        }
    }
}
