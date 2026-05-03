package com.clougence.clouddm.ds.maxcompute.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbIndexDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class McIndexDomain extends RdbIndexDomain {

    private Boolean visible;

}
