package com.clougence.clouddm.ds.sqlserver.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbSelectDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class MsSelectDomain extends RdbSelectDomain {

    private boolean hasLimit;
}
