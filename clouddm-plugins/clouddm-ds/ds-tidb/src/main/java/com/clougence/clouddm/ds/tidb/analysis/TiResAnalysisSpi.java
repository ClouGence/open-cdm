package com.clougence.clouddm.ds.tidb.analysis;

import com.clougence.clouddm.dsfamily.mysql.analysis.MyResAnalysisSpi;
import com.clougence.clouddm.sdk.service.execute.MetaService;

public class TiResAnalysisSpi extends MyResAnalysisSpi {

    public TiResAnalysisSpi(MetaService metaService){
        super(metaService);
        this.resolveSpi = new TiSecDomainResolveSpi(metaService);
    }
}
