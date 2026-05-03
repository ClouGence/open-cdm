package com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode;

import java.util.Map;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbJoinType;
import com.clougence.clouddm.sdk.service.secrules.ModeDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinDomain implements ModeDomain {

    private RdbJoinType         joinType;

    private Map<String, String> options;

    public JoinDomain(RdbJoinType joinType){
        this.joinType = joinType;
    }

}
