package com.clougence.clouddm.dsfamily.analysis.secrules.rdb;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;

import lombok.Getter;

@Getter
public class RdbConstantDomain extends RuleDomain {

    private final String constantValue;

    public RdbConstantDomain(String value){
        this.constantValue = value;
    }

    @Override
    public List<Map<TargetType, String>> resolveResource() {
        return Collections.emptyList();
    }
}
