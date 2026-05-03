package com.clougence.clouddm.dsfamily.postgres.analysis.builder;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.TableJoinBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.JoinDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbJoinType;
import com.clougence.clouddm.dsfamily.postgres.analysis.PgSecDomainOptionKeys;

import java.util.LinkedHashMap;

public class PgTableJoinBuilder extends TableJoinBuilder {

    public PgTableJoinBuilder(String joinType){
        super(joinType);
        if ("naturaljoin".equalsIgnoreCase(joinType)) {
            this.joinDomain = new JoinDomain(RdbJoinType.INNER_JOIN);
            this.joinDomain.setOptions(new LinkedHashMap<>());
            this.joinDomain.getOptions().put(PgSecDomainOptionKeys.OPT_JOIN_NATURAL, "true");
        } else if ("naturalleftjoin".equalsIgnoreCase(joinType)) {
            this.joinDomain = new JoinDomain(RdbJoinType.LEFT_JOIN);
            this.joinDomain.setOptions(new LinkedHashMap<>());
            this.joinDomain.getOptions().put(PgSecDomainOptionKeys.OPT_JOIN_NATURAL, "true");
        } else if ("naturalrightjoin".equalsIgnoreCase(joinType)) {
            this.joinDomain = new JoinDomain(RdbJoinType.RIGHT_JOIN);
            this.joinDomain.setOptions(new LinkedHashMap<>());
            this.joinDomain.getOptions().put(PgSecDomainOptionKeys.OPT_JOIN_NATURAL, "true");
        }
    }
}
