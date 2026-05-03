package com.clougence.clouddm.ds.redis.parser.ast.commands.acl;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.IntToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode
 * @date 2024/1/22 14:56
 */
@Getter
@Setter
public class AclGenPassRedisCmd extends AbstractRedisCmd {

    private IntToken bits;

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.ACL_GENPASS; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("ACL GENPASS");
        if (this.bits != null) {
            writer.append(" ");
            writer.append(fmtInt(this.bits));
        }
    }
}
