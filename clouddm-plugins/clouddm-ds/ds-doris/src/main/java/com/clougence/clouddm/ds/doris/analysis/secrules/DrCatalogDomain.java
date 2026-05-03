package com.clougence.clouddm.ds.doris.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbCatalogDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DrCatalogDomain extends RdbCatalogDomain {

    private String  comment;
    private boolean ifExists;
}
