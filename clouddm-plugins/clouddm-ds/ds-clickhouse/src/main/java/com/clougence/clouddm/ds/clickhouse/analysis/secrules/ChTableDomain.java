package com.clougence.clouddm.ds.clickhouse.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbTableDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChTableDomain extends RdbTableDomain {

    private String  newSchemaName;

    private boolean ifNotExists;
    private boolean ifExists;
    private String  engine;

    private boolean external;

    private boolean temporary;

    private String  tableModel;

}
