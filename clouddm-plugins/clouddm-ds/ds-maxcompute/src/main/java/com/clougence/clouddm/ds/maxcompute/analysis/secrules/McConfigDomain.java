package com.clougence.clouddm.ds.maxcompute.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbConfigDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class McConfigDomain extends RdbConfigDomain {

    private McScopeType type;

    public McConfigDomain(String value, McScopeType type){
        super(value);
        this.type = type;
    }
}
