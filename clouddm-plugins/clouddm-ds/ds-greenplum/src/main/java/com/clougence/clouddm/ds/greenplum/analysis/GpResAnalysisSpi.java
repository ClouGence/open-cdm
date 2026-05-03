package com.clougence.clouddm.ds.greenplum.analysis;

import com.clougence.clouddm.dsfamily.postgres.analysis.PgResAnalysisSpi;
import com.clougence.clouddm.sdk.service.execute.MetaService;

public class GpResAnalysisSpi extends PgResAnalysisSpi {

    public GpResAnalysisSpi(MetaService metaService){
        super(metaService);
        this.resolveSpi = new GpSecDomainResolveSpi(metaService);
    }
}
