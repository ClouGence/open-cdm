package com.clougence.clouddm.ds.tidb.analysis.secrules;

import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MyDeleteDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TiDeleteDomain extends MyDeleteDomain {

    private boolean usingQuick;
}
