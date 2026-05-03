package com.clougence.clouddm.ds.oracle.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbDeleteDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OraDeleteDomain extends RdbDeleteDomain {

    private boolean hasLimit;
    private boolean hasIgnore;
}
