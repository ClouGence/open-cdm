package com.clougence.clouddm.ds.doris.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbUserDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DrUserDomain extends RdbUserDomain {

    private String host;
    private String newHost;
}
