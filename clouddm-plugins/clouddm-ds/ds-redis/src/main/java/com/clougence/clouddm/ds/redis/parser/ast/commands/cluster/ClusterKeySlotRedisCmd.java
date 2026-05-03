package com.clougence.clouddm.ds.redis.parser.ast.commands.cluster;

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
public class ClusterKeySlotRedisCmd extends AbstractRedisCmd {

    private StrToken keyName;

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.CLUSTER_KEYSLOT; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("CLUSTER KEYSLOT");
        writer.append(" ");
        writer.append(fmtString(this.keyName));
    }
}
