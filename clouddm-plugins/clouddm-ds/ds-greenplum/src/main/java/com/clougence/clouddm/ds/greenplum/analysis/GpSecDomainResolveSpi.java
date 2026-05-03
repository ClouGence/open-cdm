package com.clougence.clouddm.ds.greenplum.analysis;

import com.clougence.clouddm.dsfamily.postgres.analysis.PgSecDomainResolveSpi;
import com.clougence.clouddm.sdk.service.execute.MetaService;

public class GpSecDomainResolveSpi extends PgSecDomainResolveSpi {

    public GpSecDomainResolveSpi(MetaService metaService){
        super(metaService);
    }
}
