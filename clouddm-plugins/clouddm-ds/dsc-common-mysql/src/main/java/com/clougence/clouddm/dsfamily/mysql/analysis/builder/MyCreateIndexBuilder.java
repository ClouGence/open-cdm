package com.clougence.clouddm.dsfamily.mysql.analysis.builder;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.CreateIndexBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbIndexDomain;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MyIndexDomain;

public class MyCreateIndexBuilder extends CreateIndexBuilder {

    @Override
    protected RdbIndexDomain getIndexDomain() { return new MyIndexDomain(); }

}
