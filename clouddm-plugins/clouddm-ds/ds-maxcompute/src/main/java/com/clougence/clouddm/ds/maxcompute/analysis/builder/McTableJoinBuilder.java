package com.clougence.clouddm.ds.maxcompute.analysis.builder;

import java.util.LinkedHashMap;

import com.clougence.clouddm.ds.maxcompute.analysis.McSecDomainOptionKeys;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.TableJoinBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.JoinDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbJoinType;

public class McTableJoinBuilder extends TableJoinBuilder {

    public McTableJoinBuilder(String joinType){
        super(joinType);
        if ("naturaljoin".equalsIgnoreCase(joinType)) {
            this.joinDomain = new JoinDomain(RdbJoinType.INNER_JOIN);
            this.joinDomain.setOptions(new LinkedHashMap<>());
            this.joinDomain.getOptions().put(McSecDomainOptionKeys.OPT_JOIN_NATURAL, "true");
        }
    }
}
