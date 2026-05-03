package com.clougence.clouddm.dsfamily.db2.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbColumnDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class Db2ColumnDomain extends RdbColumnDomain {

    private boolean auto;
    private boolean zerofill;
    private boolean unsigned;

    private String  collate;
    private String  characterSet;
}
