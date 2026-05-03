package com.clougence.clouddm.ds.clickhouse.analysis.builder;

import java.util.ArrayList;

import com.clougence.clouddm.ds.clickhouse.analysis.secrules.ChInsertDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.InsertBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbInsertConflictStrategy;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbInsertDomain;

public class ChInsertBuilder extends InsertBuilder {

    @Override
    protected RdbInsertDomain getInsertDomain() {
        ChInsertDomain myInsertDomain = new ChInsertDomain();
        myInsertDomain.setColumns(new ArrayList<>());
        myInsertDomain.setConflict(RdbInsertConflictStrategy.NONE);
        return myInsertDomain;
    }

}
