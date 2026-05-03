package com.clougence.clouddm.ds.gauss.analysis.gsog;

import com.clougence.clouddm.dsfamily.postgres.analysis.PgResAnalysisSpi;
import com.clougence.clouddm.sdk.service.execute.MetaService;

public class GsogResAnalysisSpi extends PgResAnalysisSpi {

    public GsogResAnalysisSpi(MetaService metaService){
        super(metaService);
        this.resolveSpi = new GsogSecDomainResolveSpi(metaService);
    }
}
