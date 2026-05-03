package com.clougence.clouddm.ds.starrocks.analysis.builder;

import com.clougence.clouddm.ds.starrocks.analysis.secrules.SrDeleteDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.DeleteDomainBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbDeleteDomain;

public class SrDeleteBuilder extends DeleteDomainBuilder {

    protected RdbDeleteDomain getDeleteDomain() { return new SrDeleteDomain(); }

}
