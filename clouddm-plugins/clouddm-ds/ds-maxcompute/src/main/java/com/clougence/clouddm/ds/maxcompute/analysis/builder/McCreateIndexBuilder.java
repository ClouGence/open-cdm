package com.clougence.clouddm.ds.maxcompute.analysis.builder;

import com.clougence.clouddm.ds.maxcompute.analysis.secrules.McIndexDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.CreateIndexBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbIndexDomain;

public class McCreateIndexBuilder extends CreateIndexBuilder {

    @Override
    protected RdbIndexDomain getIndexDomain() { return new McIndexDomain(); }

}
