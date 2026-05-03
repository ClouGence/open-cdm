package com.clougence.clouddm.dsfamily.mysql.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbUserDomain;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyUserDomain extends RdbUserDomain {

    private String host;
    private String newHost;
}
