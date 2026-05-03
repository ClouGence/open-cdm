package com.clougence.clouddm.ds.mariadb.analysis;

import com.clougence.clouddm.dsfamily.mysql.analysis.MyResAnalysisSpi;
import com.clougence.clouddm.sdk.service.execute.MetaService;

public class MarResAnalysisSpi extends MyResAnalysisSpi {

    public MarResAnalysisSpi(MetaService metaService){
        super(metaService);
        this.resolveSpi = new MarSecDomainResolveSpi(metaService);
    }
}
