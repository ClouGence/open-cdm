package com.clougence.clouddm.ds.hologres.analysis;

import com.clougence.clouddm.dsfamily.postgres.analysis.PgResAnalysisSpi;
import com.clougence.clouddm.sdk.service.execute.MetaService;

public class HgResAnalysisSpi extends PgResAnalysisSpi {

    public HgResAnalysisSpi(MetaService metaService){
        super(metaService);
        this.resolveSpi = new HgSecDomainResolveSpi(metaService);
    }
}
