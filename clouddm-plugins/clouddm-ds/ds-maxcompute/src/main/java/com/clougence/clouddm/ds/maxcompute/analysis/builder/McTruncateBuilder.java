package com.clougence.clouddm.ds.maxcompute.analysis.builder;

import com.clougence.clouddm.ds.maxcompute.analysis.secrules.McTableDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.TruncateBuilder;

public class McTruncateBuilder extends TruncateBuilder<McTableDomain> {

    @Override
    protected McTableDomain getTableDomain() { return new McTableDomain(); }
}
