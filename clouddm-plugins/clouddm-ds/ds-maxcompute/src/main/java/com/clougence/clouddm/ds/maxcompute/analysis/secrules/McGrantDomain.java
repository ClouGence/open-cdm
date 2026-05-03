package com.clougence.clouddm.ds.maxcompute.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbGrantDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McGrantDomain extends RdbGrantDomain {

    private String host;
}
