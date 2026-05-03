package com.clougence.clouddm.ds.oracle.analysis.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.clougence.clouddm.ds.oracle.analysis.secrules.OraUpdateDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.UpdateBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.WithSelectDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbUpdateDomain;

public class OraUpdateBuilder extends UpdateBuilder {

    public OraUpdateBuilder(Stack<List<WithSelectDomain>> selectStack){
        super(selectStack);
    }

    @Override
    protected RdbUpdateDomain getRdbUpdateDomain() {
        OraUpdateDomain pgUpdateDomain = new OraUpdateDomain();
        pgUpdateDomain.setSetColumns(new ArrayList<>());
        return pgUpdateDomain;
    }
}
