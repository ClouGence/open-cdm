package com.clougence.clouddm.ds.oracle.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbUpdateDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class OraUpdateDomain extends RdbUpdateDomain {

    private boolean hasLimit;
    private boolean hasIgnore;

}
