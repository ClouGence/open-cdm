package com.clougence.clouddm.ds.redis.parser.ast.commands.sortedset;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.*;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: mode
 * @Date: 2023-07-10 11:15
 */
@Getter
@Setter
public class ZAddRedisCmd extends AbstractRedisCmd {

    private StrToken                  keyName;
    private NxXx                      nxxx;
    private GtLt                      gtlt;
    private TagToken                  chTag;
    private TagToken                  incrTag;
    private List<ScoreAndMemberToken> members = new ArrayList<>();

    public void addMember(ScoreAndMemberToken member) {
        if (member != null) {
            this.members.add(member);
        }
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.ZADD; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("ZADD");
        writer.append(" ");
        writer.append(fmtString(keyName));
        if (nxxx != null) {
            writer.append(" ");
            writer.append(nxxx.name());
        }
        if (gtlt != null) {
            writer.append(" ");
            writer.append(gtlt.name());
        }
        if (chTag != null) {
            writer.append(" CH");
        }
        if (incrTag != null) {
            writer.append(" INCR");
        }

        for (ScoreAndMemberToken member : this.members) {
            writer.append(" ");
            writer.append(fmtInt(member.getScore()));
            writer.append(" ");
            writer.append(fmtString(member.getMember()));
        }
    }
}
