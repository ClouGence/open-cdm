package com.clougence.clouddm.dsfamily.mysql.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbColumnDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class MyColumnDomain extends RdbColumnDomain {

    private boolean auto;
    private boolean zerofill;
    private boolean unsigned;

    private String  collate;
    private String  characterSet;
}
