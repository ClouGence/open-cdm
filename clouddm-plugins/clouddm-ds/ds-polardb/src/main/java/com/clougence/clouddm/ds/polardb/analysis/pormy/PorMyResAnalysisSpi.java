package com.clougence.clouddm.ds.polardb.analysis.pormy;

import com.clougence.clouddm.dsfamily.mysql.analysis.MyResAnalysisSpi;
import com.clougence.clouddm.sdk.service.execute.MetaService;

public class PorMyResAnalysisSpi extends MyResAnalysisSpi {

    public PorMyResAnalysisSpi(MetaService metaService){
        super(metaService);
        this.resolveSpi = new PorMySecDomainResolveSpi(metaService);
    }
}
