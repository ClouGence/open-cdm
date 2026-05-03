package com.clougence.clouddm.dsfamily.postgres.analysis.builder;

import java.util.ArrayList;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.InsertBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbInsertConflictStrategy;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbInsertDomain;
import com.clougence.clouddm.dsfamily.postgres.analysis.secrules.PgInsertDomain;

public class PgInsertBuilder extends InsertBuilder {

    @Override
    protected RdbInsertDomain getInsertDomain() {
        PgInsertDomain pgInsertDomain = new PgInsertDomain();
        pgInsertDomain.setColumns(new ArrayList<>());
        pgInsertDomain.setConflict(RdbInsertConflictStrategy.NONE);
        return pgInsertDomain;
    }
}
