package com.clougence.clouddm.ds.maxcompute.analysis.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.clougence.clouddm.ds.maxcompute.analysis.secrules.McUpdateDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.UpdateBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.WithSelectDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbUpdateDomain;

public class McUpdateBuilder extends UpdateBuilder {

    public McUpdateBuilder(Stack<List<WithSelectDomain>> selectStack){
        super(selectStack);
    }

    @Override
    protected RdbUpdateDomain getRdbUpdateDomain() {
        McUpdateDomain pgUpdateDomain = new McUpdateDomain();
        pgUpdateDomain.setSetColumns(new ArrayList<>());
        return pgUpdateDomain;
    }
}
