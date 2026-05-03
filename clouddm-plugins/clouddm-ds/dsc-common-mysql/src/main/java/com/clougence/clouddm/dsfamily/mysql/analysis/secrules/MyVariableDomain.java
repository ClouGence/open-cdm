package com.clougence.clouddm.dsfamily.mysql.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbVariableDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class MyVariableDomain extends RdbVariableDomain {

    private MyScopeType type;

    public MyVariableDomain(String value, MyScopeType type){
        super(value);
        this.type = type;
    }
}
