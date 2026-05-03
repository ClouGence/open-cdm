package com.clougence.clouddm.ds.doris.analysis.builder;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.TableJoinBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.JoinDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbJoinType;
import com.clougence.utils.StringUtils;

public class DrTableJoinBuilder extends TableJoinBuilder {

    public DrTableJoinBuilder(String text){
        super(text);
        if (StringUtils.isEmpty(text)) {
            this.joinDomain = new JoinDomain(RdbJoinType.INNER_JOIN);
            return;
        }
        text = text.toLowerCase();
        if (text.equalsIgnoreCase("cross")) {
            this.joinDomain = new JoinDomain(RdbJoinType.CROSS_JOIN);
        } else if (text.startsWith("inner")) {
            this.joinDomain = new JoinDomain(RdbJoinType.INNER_JOIN);
        } else if (text.startsWith("left")) {
            this.joinDomain = new JoinDomain(RdbJoinType.LEFT_JOIN);
        } else if (text.startsWith("right")) {
            this.joinDomain = new JoinDomain(RdbJoinType.RIGHT_JOIN);
        } else {
            this.joinDomain = new JoinDomain(RdbJoinType.OTHER_JOIN);
        }
    }
}
