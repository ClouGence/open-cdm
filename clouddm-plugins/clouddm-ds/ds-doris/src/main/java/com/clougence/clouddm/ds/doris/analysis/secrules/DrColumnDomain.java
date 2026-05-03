package com.clougence.clouddm.ds.doris.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbColumnDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DrColumnDomain extends RdbColumnDomain {

    private boolean auto;
    private String  aggrType;
}
