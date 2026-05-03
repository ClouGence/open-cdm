package com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.SqlConstraintType;
import com.clougence.clouddm.sdk.service.secrules.ModeDomain;
import lombok.Getter;

@Getter
public class ConstraintTypeDomain implements ModeDomain {

    private SqlConstraintType constraintType;

    public ConstraintTypeDomain(SqlConstraintType constraintType){
        this.constraintType = constraintType;
    }
}
