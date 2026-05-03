package com.clougence.clouddm.dsfamily.mysql.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbDeleteDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyDeleteDomain extends RdbDeleteDomain {

    private boolean hasLimit;
    private boolean hasIgnore;
}
