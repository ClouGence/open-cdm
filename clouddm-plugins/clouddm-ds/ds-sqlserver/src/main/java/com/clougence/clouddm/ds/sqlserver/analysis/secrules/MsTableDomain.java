package com.clougence.clouddm.ds.sqlserver.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbTableDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class MsTableDomain extends RdbTableDomain {

    private boolean      ifNotExists;
    private boolean      ifExists;

    //    private boolean ifExists;
    //
    //    private String  collate;
    //    private String  characterSet;
}
