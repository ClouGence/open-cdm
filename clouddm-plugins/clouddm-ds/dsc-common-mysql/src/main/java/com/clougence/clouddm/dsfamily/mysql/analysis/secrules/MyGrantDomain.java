package com.clougence.clouddm.dsfamily.mysql.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbGrantDomain;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyGrantDomain extends RdbGrantDomain {

    private String host;
}
