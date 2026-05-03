package com.clougence.clouddm.dsfamily.db2.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbSelectDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class Db2SelectDomain extends RdbSelectDomain {

    private boolean hasLimit;
}
