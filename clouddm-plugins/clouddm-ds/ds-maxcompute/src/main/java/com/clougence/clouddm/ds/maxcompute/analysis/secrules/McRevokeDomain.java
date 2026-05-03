package com.clougence.clouddm.ds.maxcompute.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbRevokeDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McRevokeDomain extends RdbRevokeDomain {

    private String host;
}
