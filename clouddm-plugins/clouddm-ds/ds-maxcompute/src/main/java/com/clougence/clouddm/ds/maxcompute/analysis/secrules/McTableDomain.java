package com.clougence.clouddm.ds.maxcompute.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbTableDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class McTableDomain extends RdbTableDomain {

    private String  newSchemaName;

    private boolean ifNotExists;
    private boolean ifExists;

    private boolean temporary;
}
