package com.clougence.clouddm.ds.doris.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbVariableDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class DrVariableDomain extends RdbVariableDomain {

    private DrVariableType type;

    public DrVariableDomain(String value, DrVariableType type){
        super(value);
        this.type = type;
    }
}
