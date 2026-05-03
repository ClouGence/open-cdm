package com.clougence.clouddm.ds.oracle.analysis.builder;

import com.clougence.clouddm.ds.oracle.analysis.secrules.OraTableDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.DropTableBuilder;

public class OraDropTableBuilder extends DropTableBuilder<OraTableDomain> {

    @Override
    protected OraTableDomain getTableDomain() { return new OraTableDomain(); }
}
