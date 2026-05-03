package com.clougence.clouddm.dsfamily.db2.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbTableDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class Db2TableDomain extends RdbTableDomain {

    private boolean replace;
    private boolean ifExists;

    // boolean temporary;
}
