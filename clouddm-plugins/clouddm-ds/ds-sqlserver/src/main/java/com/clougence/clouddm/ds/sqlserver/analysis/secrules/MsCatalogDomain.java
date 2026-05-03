package com.clougence.clouddm.ds.sqlserver.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbCatalogDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class MsCatalogDomain extends RdbCatalogDomain {

    // for drop
    private boolean ifExists;

    private String  comment;

    private String  collate;
}
