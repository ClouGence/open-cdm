package com.clougence.clouddm.ds.polardb.analysis.porpg;

import com.clougence.clouddm.dsfamily.postgres.analysis.PgResAnalysisSpi;
import com.clougence.clouddm.sdk.service.execute.MetaService;

public class PorPgResAnalysisSpi extends PgResAnalysisSpi {

    public PorPgResAnalysisSpi(MetaService metaService){
        super(metaService);
        this.resolveSpi = new PorPgSecDomainResolveSpi(metaService);
    }
}
