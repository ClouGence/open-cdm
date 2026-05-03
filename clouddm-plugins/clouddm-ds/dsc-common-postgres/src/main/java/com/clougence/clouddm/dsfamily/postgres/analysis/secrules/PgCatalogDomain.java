package com.clougence.clouddm.dsfamily.postgres.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbCatalogDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class PgCatalogDomain extends RdbCatalogDomain {

    private boolean ifExists;

    private boolean usingForce;
}
