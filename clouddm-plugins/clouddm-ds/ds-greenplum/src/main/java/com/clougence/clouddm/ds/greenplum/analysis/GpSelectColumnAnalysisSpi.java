package com.clougence.clouddm.ds.greenplum.analysis;

import com.clougence.clouddm.dsfamily.postgres.analysis.PgSelectColumnAnalysisSpi;
import com.clougence.clouddm.sdk.service.execute.MetaService;

public class GpSelectColumnAnalysisSpi extends PgSelectColumnAnalysisSpi {

    public GpSelectColumnAnalysisSpi(MetaService metaService){
        super(metaService);
    }
}
