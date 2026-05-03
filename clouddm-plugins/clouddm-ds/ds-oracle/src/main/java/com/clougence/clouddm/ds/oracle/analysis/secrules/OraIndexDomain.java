package com.clougence.clouddm.ds.oracle.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbIndexDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class OraIndexDomain extends RdbIndexDomain {

    private Boolean visible;

}
