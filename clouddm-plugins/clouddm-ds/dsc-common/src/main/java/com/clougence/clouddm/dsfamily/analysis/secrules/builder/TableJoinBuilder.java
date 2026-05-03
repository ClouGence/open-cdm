package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.JoinDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbJoinType;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class TableJoinBuilder implements DomainBuilder {

    protected JoinDomain joinDomain;

    public TableJoinBuilder(String text){
        RdbJoinType type = RdbJoinType.OTHER_JOIN;
        if ("join".equalsIgnoreCase(text)) {
            type = RdbJoinType.INNER_JOIN;
        } else if ("leftjoin".equalsIgnoreCase(text) || "leftouterjoin".equalsIgnoreCase(text)) {
            type = RdbJoinType.LEFT_JOIN;
        } else if ("rightjoin".equalsIgnoreCase(text) || "rightouterjoin".equalsIgnoreCase(text)) {
            type = RdbJoinType.RIGHT_JOIN;
        } else if ("innerjoin".equalsIgnoreCase(text) || "innertouterjoin".equalsIgnoreCase(text)) {
            type = RdbJoinType.INNER_JOIN;
        } else if ("crossjoin".equalsIgnoreCase(text)) {
            type = RdbJoinType.CROSS_JOIN;
        }
        this.joinDomain = new JoinDomain(type);
    }

    @Override
    public List<Domain> build() {
        return Collections.singletonList(joinDomain);
    }
}
