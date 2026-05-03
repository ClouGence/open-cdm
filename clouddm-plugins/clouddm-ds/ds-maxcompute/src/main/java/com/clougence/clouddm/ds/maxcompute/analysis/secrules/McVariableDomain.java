package com.clougence.clouddm.ds.maxcompute.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbVariableDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class McVariableDomain extends RdbVariableDomain {

    private McScopeType type;

    public McVariableDomain(String value, McScopeType type){
        super(value);
        this.type = type;
    }
}
