package com.clougence.clouddm.ds.oracle.analysis.builder;

import java.util.ArrayList;

import com.clougence.clouddm.ds.oracle.analysis.secrules.OraInsertDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.InsertBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbInsertConflictStrategy;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbInsertDomain;

public class OraInsertBuilder extends InsertBuilder {

    @Override
    protected RdbInsertDomain getInsertDomain() {
        OraInsertDomain domain = new OraInsertDomain();
        domain.setColumns(new ArrayList<>());
        domain.setConflict(RdbInsertConflictStrategy.NONE);
        return domain;
    }
}
