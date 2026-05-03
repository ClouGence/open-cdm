package com.clougence.clouddm.ds.doris.analysis.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.clougence.clouddm.ds.doris.analysis.secrules.DrUpdateDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.UpdateBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.WithSelectDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbUpdateDomain;

public class DrUpdateBuilder extends UpdateBuilder {

    public DrUpdateBuilder(Stack<List<WithSelectDomain>> selectStack){
        super(selectStack);
    }

    @Override
    protected RdbUpdateDomain getRdbUpdateDomain() {
        DrUpdateDomain pgUpdateDomain = new DrUpdateDomain();
        pgUpdateDomain.setSetColumns(new ArrayList<>());
        return pgUpdateDomain;
    }
}
