package com.clougence.clouddm.dsfamily.db2.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbSchemaDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class Db2SchemaDomain extends RdbSchemaDomain {

    private boolean ifExists;
}
