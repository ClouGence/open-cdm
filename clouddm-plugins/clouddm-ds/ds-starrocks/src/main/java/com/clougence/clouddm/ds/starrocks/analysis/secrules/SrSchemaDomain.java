package com.clougence.clouddm.ds.starrocks.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbSchemaDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SrSchemaDomain extends RdbSchemaDomain {

    private boolean ifNotExists;
    private boolean ifExists;
    private boolean force;

    private String  collate;
    private String  characterSet;
}
