package com.clougence.clouddm.dsfamily.mysql.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbTableDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class MyTableDomain extends RdbTableDomain {

    private String  newSchemaName;

    private boolean ifNotExists;
    private boolean ifExists;

    private String  collate;
    private String  characterSet;
    private String  engine;
    private String  autoIncrement;

    private boolean temporary;
}
