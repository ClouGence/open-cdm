package com.clougence.clouddm.ds.polardb.analysis.porpg;

import com.clougence.clouddm.dsfamily.postgres.analysis.PgSelectColumnAnalysisSpi;
import com.clougence.clouddm.sdk.service.execute.MetaService;

public class PorPgSelectColumnAnalysisSpi extends PgSelectColumnAnalysisSpi {

    public PorPgSelectColumnAnalysisSpi(MetaService metaService) {
        super(metaService);
    }
}
