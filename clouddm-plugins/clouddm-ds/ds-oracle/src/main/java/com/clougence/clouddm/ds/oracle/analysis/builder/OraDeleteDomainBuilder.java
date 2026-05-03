package com.clougence.clouddm.ds.oracle.analysis.builder;

import com.clougence.clouddm.ds.oracle.analysis.secrules.OraDeleteDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.DeleteDomainBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbDeleteDomain;

public class OraDeleteDomainBuilder extends DeleteDomainBuilder {

    @Override
    protected RdbDeleteDomain getDeleteDomain() { return new OraDeleteDomain(); }
}
