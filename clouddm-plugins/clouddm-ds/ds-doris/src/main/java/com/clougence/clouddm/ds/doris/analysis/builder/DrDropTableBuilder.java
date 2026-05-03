package com.clougence.clouddm.ds.doris.analysis.builder;

import com.clougence.clouddm.ds.doris.analysis.secrules.DrTableDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.DropTableBuilder;

public class DrDropTableBuilder extends DropTableBuilder<DrTableDomain> {

    @Override
    protected DrTableDomain getTableDomain() { return new DrTableDomain(); }
}
