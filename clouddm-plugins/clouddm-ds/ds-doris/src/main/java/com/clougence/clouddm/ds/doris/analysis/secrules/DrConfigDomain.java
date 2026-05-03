package com.clougence.clouddm.ds.doris.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbConfigDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class DrConfigDomain extends RdbConfigDomain {

    private DrScopeType type;

    public DrConfigDomain(String value, DrScopeType type){
        super(value);
        this.type = type;
    }
}
