package com.clougence.clouddm.ds.mariadb.analysis;

import com.clougence.clouddm.dsfamily.mysql.analysis.MySecDomainResolveSpi;
import com.clougence.clouddm.sdk.service.execute.MetaService;

public class MarSecDomainResolveSpi extends MySecDomainResolveSpi {

    public MarSecDomainResolveSpi(MetaService metaService){
        super(metaService);
    }
}
