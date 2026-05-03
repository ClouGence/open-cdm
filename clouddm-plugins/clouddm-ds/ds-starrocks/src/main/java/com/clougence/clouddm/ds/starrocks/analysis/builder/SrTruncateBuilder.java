package com.clougence.clouddm.ds.starrocks.analysis.builder;

import com.clougence.clouddm.ds.starrocks.analysis.secrules.SrTableDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.TruncateBuilder;

public class SrTruncateBuilder extends TruncateBuilder<SrTableDomain> {

    @Override
    protected SrTableDomain getTableDomain() { return new SrTableDomain(); }
}
