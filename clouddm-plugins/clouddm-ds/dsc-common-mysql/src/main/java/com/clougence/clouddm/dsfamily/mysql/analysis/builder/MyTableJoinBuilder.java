package com.clougence.clouddm.dsfamily.mysql.analysis.builder;

import java.util.LinkedHashMap;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.TableJoinBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.JoinDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbJoinType;
import com.clougence.clouddm.dsfamily.mysql.analysis.MySecDomainOptionKeys;

public class MyTableJoinBuilder extends TableJoinBuilder {

    public MyTableJoinBuilder(String joinType){
        super(joinType);
        if ("naturaljoin".equalsIgnoreCase(joinType)) {
            this.joinDomain = new JoinDomain(RdbJoinType.INNER_JOIN);
            this.joinDomain.setOptions(new LinkedHashMap<>());
            this.joinDomain.getOptions().put(MySecDomainOptionKeys.OPT_JOIN_NATURAL, "true");
        } else if ("naturalleftjoin".equalsIgnoreCase(joinType)) {
            this.joinDomain = new JoinDomain(RdbJoinType.LEFT_JOIN);
            this.joinDomain.setOptions(new LinkedHashMap<>());
            this.joinDomain.getOptions().put(MySecDomainOptionKeys.OPT_JOIN_NATURAL, "true");
        } else if ("naturalrightjoin".equalsIgnoreCase(joinType)) {
            this.joinDomain = new JoinDomain(RdbJoinType.RIGHT_JOIN);
            this.joinDomain.setOptions(new LinkedHashMap<>());
            this.joinDomain.getOptions().put(MySecDomainOptionKeys.OPT_JOIN_NATURAL, "true");
        } else if ("naturalcrossjoin".equalsIgnoreCase(joinType)) {
            this.joinDomain = new JoinDomain(RdbJoinType.CROSS_JOIN);
            this.joinDomain.setOptions(new LinkedHashMap<>());
            this.joinDomain.getOptions().put(MySecDomainOptionKeys.OPT_JOIN_NATURAL, "true");
        }
    }
}
