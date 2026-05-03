package com.clougence.clouddm.dsfamily.postgres.analysis.secrules;

import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbTableDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class PgTableDomain extends RdbTableDomain {

    private boolean      ifNotExists;
    private boolean      ifExists;

    private boolean      temporary;
    private boolean      unlogged;

    private boolean      inherits;
    private List<String> inheritTables;

    //    private boolean ifExists;
    //
    //    private String  collate;
    //    private String  characterSet;
}
