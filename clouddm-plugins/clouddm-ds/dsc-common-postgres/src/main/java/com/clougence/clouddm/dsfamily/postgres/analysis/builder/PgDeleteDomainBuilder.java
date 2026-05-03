package com.clougence.clouddm.dsfamily.postgres.analysis.builder;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.DeleteDomainBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbDeleteDomain;
import com.clougence.clouddm.dsfamily.postgres.analysis.secrules.PgDeleteDomain;

public class PgDeleteDomainBuilder extends DeleteDomainBuilder {

    @Override
    protected RdbDeleteDomain getDeleteDomain() { return new PgDeleteDomain(); }
}
