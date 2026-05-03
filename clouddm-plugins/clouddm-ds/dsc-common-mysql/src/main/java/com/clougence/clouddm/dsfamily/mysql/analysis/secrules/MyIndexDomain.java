package com.clougence.clouddm.dsfamily.mysql.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbIndexDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class MyIndexDomain extends RdbIndexDomain {

    private Boolean visible;

}
