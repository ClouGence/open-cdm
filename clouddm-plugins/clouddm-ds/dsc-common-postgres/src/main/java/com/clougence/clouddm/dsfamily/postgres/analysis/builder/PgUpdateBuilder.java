package com.clougence.clouddm.dsfamily.postgres.analysis.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.UpdateBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.WithSelectDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbUpdateDomain;
import com.clougence.clouddm.dsfamily.postgres.analysis.secrules.PgUpdateDomain;

public class PgUpdateBuilder extends UpdateBuilder {

    public PgUpdateBuilder(Stack<List<WithSelectDomain>> selectStack){
        super(selectStack);
    }

    @Override
    protected RdbUpdateDomain getRdbUpdateDomain() {
        PgUpdateDomain pgUpdateDomain = new PgUpdateDomain();
        pgUpdateDomain.setSetColumns(new ArrayList<>());
        return pgUpdateDomain;
    }
}
