package com.clougence.clouddm.ds.starrocks.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbConfigDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class SrConfigDomain extends RdbConfigDomain {

    private SrScopeType type;

    public SrConfigDomain(String value, SrScopeType type){
        super(value);
        this.type = type;
    }
}
