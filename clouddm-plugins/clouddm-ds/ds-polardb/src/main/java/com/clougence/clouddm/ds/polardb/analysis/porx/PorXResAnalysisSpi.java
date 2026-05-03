package com.clougence.clouddm.ds.polardb.analysis.porx;

import com.clougence.clouddm.dsfamily.mysql.analysis.MyResAnalysisSpi;
import com.clougence.clouddm.sdk.service.execute.MetaService;

public class PorXResAnalysisSpi extends MyResAnalysisSpi {

    public PorXResAnalysisSpi(MetaService metaService){
        super(metaService);
        this.resolveSpi = new PorXSecDomainResolveSpi(metaService);
    }
}
