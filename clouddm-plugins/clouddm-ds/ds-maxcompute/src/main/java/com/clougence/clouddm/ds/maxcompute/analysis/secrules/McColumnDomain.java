package com.clougence.clouddm.ds.maxcompute.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbColumnDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class McColumnDomain extends RdbColumnDomain {

    private boolean auto;
    private boolean zerofill;
    private boolean unsigned;

    private String  collate;
    private String  characterSet;
}
