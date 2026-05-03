package com.clougence.clouddm.ds.clickhouse.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbSchemaDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class ChSchemaDomain extends RdbSchemaDomain {

    private boolean ifNotExists;
    private boolean ifExists;

}
