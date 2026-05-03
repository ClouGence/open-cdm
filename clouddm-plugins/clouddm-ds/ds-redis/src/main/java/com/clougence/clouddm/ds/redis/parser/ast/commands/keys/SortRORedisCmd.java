package com.clougence.clouddm.ds.redis.parser.ast.commands.keys;

import java.io.IOException;
import java.util.List;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.IntToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.SortByToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.TagToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SortRORedisCmd extends AbstractRedisCmd {

    private final StrToken keyName;
    private StrToken       byPattern;
    private IntToken       limitOffset;
    private IntToken       limitCount;
    private List<StrToken> getPatterns;
    private SortByToken    sortToken;
    private TagToken       alphaTag;

    public SortRORedisCmd(StrToken keyName){
        this.keyName = keyName;
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.SORT_RO; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        // SORT_RO key [BY pattern] [LIMIT offset count] [GET pattern [GET pattern ...]] [ASC|DESC] [ALPHA]

        writer.append("SORT_RO");
        writer.append(" ");
        writer.append(fmtString(keyName));
        if (byPattern != null) {
            writer.append(" BY ");
            writer.append(fmtString(byPattern));
        }

        if (limitOffset != null && limitCount != null) {
            writer.append(" LIMIT ");
            writer.append(fmtInt(limitOffset));
            writer.append(" ");
            writer.append(fmtInt(limitCount));
        }
        if (getPatterns != null && !getPatterns.isEmpty()) {
            for (StrToken get : getPatterns) {
                writer.append(" GET ");
                writer.append(fmtString(get));
            }
        }
        if (sortToken != null) {
            writer.append(" ");
            writer.append(sortToken.getOptionType().name().toUpperCase());
        }
        if (alphaTag != null) {
            writer.append(" ALPHA");
        }
    }
}
