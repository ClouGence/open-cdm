package com.clougence.clouddm.dsfamily.mysql.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbSchemaDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class MySchemaDomain extends RdbSchemaDomain {

    private boolean ifNotExists;
    private boolean ifExists;

    private String  collate;
    private String  characterSet;
}
