package com.clougence.clouddm.ds.doris.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbRevokeDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DrRevokeDomain extends RdbRevokeDomain {

    private String host;
}
