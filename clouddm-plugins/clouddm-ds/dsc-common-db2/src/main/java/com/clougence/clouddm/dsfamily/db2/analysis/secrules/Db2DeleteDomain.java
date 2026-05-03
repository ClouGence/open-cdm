package com.clougence.clouddm.dsfamily.db2.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbDeleteDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Db2DeleteDomain extends RdbDeleteDomain {

    private boolean hasLimit;
    private boolean hasIgnore;
}
