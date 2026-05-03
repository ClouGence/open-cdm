package com.clougence.clouddm.ds.starrocks.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbGrantDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SrGrantDomain extends RdbGrantDomain {

    private String host;
}
