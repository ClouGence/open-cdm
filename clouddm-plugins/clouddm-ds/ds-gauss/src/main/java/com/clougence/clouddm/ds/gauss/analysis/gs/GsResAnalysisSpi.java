package com.clougence.clouddm.ds.gauss.analysis.gs;

import com.clougence.clouddm.dsfamily.postgres.analysis.PgResAnalysisSpi;
import com.clougence.clouddm.sdk.service.execute.MetaService;

public class GsResAnalysisSpi extends PgResAnalysisSpi {

    public GsResAnalysisSpi(MetaService metaService){
        super(metaService);
        this.resolveSpi = new GsSecDomainResolveSpi(metaService);
    }
}
