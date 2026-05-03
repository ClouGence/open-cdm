package com.clougence.clouddm.ds.starrocks.analysis.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.clougence.clouddm.ds.starrocks.analysis.secrules.SrUpdateDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.UpdateBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.WithSelectDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbUpdateDomain;

public class SrUpdateBuilder extends UpdateBuilder {

    public SrUpdateBuilder(Stack<List<WithSelectDomain>> selectStack){
        super(selectStack);
    }

    @Override
    protected RdbUpdateDomain getRdbUpdateDomain() {
        SrUpdateDomain pgUpdateDomain = new SrUpdateDomain();
        pgUpdateDomain.setSetColumns(new ArrayList<>());
        return pgUpdateDomain;
    }
}
