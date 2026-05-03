package com.clougence.clouddm.dsfamily.mysql.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbRevokeDomain;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyRevokeDomain extends RdbRevokeDomain {

    private String host;
}
