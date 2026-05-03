package com.clougence.clouddm.dsfamily.postgres.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbSchemaDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class PgSchemaDomain extends RdbSchemaDomain {

    private String  owner;

    private boolean ifNotExists;
    private boolean ifExists;

    private boolean cascade;
    private boolean restrict;
}
