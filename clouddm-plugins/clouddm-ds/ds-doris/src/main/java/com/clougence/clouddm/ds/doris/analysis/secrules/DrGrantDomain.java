package com.clougence.clouddm.ds.doris.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbGrantDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DrGrantDomain extends RdbGrantDomain {

    private String host;
}
