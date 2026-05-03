package com.clougence.clouddm.dsfamily.db2.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbUpdateDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class Db2UpdateDomain extends RdbUpdateDomain {

    private boolean hasLimit;
    private boolean hasIgnore;

}
