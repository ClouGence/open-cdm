package com.clougence.clouddm.ds.polardb.analysis.porpg;

import com.clougence.clouddm.dsfamily.postgres.analysis.PgSecDomainResolveSpi;
import com.clougence.clouddm.sdk.service.execute.MetaService;

public class PorPgSecDomainResolveSpi extends PgSecDomainResolveSpi {

    public PorPgSecDomainResolveSpi(MetaService metaService){
        super(metaService);
    }
}
