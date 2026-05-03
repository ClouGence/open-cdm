package com.clougence.clouddm.ds.starrocks.analysis.builder;

import java.util.ArrayList;

import com.clougence.clouddm.ds.starrocks.analysis.secrules.SrInsertDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.InsertBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbInsertConflictStrategy;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbInsertDomain;

public class SrInsertBuilder extends InsertBuilder {

    @Override
    protected RdbInsertDomain getInsertDomain() {
        SrInsertDomain myInsertDomain = new SrInsertDomain();
        myInsertDomain.setColumns(new ArrayList<>());
        myInsertDomain.setConflict(RdbInsertConflictStrategy.NONE);
        return myInsertDomain;
    }
}
