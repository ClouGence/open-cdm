package com.clougence.clouddm.ds.ads.analysis.ads4my;

import com.clougence.clouddm.dsfamily.mysql.analysis.MyResAnalysisSpi;
import com.clougence.clouddm.sdk.service.execute.MetaService;

public class AdsMyResAnalysisSpi extends MyResAnalysisSpi {

    public AdsMyResAnalysisSpi(MetaService metaService){
        super(metaService);
        this.resolveSpi = new AdsMySecDomainResolveSpi(metaService);
    }
}
