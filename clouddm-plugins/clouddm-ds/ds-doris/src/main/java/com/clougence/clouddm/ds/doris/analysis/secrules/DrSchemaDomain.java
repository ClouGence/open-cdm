package com.clougence.clouddm.ds.doris.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbSchemaDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DrSchemaDomain extends RdbSchemaDomain {

    private boolean ifNotExists;

    // for drop
    private boolean ifExists;
    private boolean force;

}
