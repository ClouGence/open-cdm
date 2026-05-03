package com.clougence.clouddm.ds.sqlserver.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbSchemaDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class MsSchemaDomain extends RdbSchemaDomain {

    private String  owner;

    private boolean ifNotExists;
    private boolean ifExists;

    private boolean cascade;
    private boolean restrict;
}
