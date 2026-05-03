package com.clougence.clouddm.dsfamily.mysql.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbConfigDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class MyConfigDomain extends RdbConfigDomain {

    private MyScopeType type;

    public MyConfigDomain(String value, MyScopeType type){
        super(value);
        this.type = type;
    }
}
