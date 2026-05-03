package com.clougence.clouddm.ds.starrocks.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbRevokeDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SrRevokeDomain extends RdbRevokeDomain {

    private String host;
}
