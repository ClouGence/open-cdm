package com.clougence.clouddm.ds.oracle.analysis.builder;

import com.clougence.clouddm.ds.oracle.analysis.secrules.OraTableDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.TruncateBuilder;

public class OraTruncateBuilder extends TruncateBuilder<OraTableDomain> {

    @Override
    protected OraTableDomain getTableDomain() { return new OraTableDomain(); }
}
