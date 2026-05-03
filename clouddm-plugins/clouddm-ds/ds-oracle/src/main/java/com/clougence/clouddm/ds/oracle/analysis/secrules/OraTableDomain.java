package com.clougence.clouddm.ds.oracle.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbTableDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class OraTableDomain extends RdbTableDomain {

    private String  collate;
    private String  characterSet;
    private String  engine;
    private String  autoIncrement;

    private boolean temporary;
}
