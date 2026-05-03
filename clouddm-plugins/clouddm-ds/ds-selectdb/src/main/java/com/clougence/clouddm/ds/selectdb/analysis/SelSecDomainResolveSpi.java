package com.clougence.clouddm.ds.selectdb.analysis;

import com.clougence.clouddm.ds.doris.analysis.DrSecDomainResolveSpi;
import com.clougence.clouddm.sdk.service.execute.MetaService;

public class SelSecDomainResolveSpi extends DrSecDomainResolveSpi {

    public SelSecDomainResolveSpi(MetaService metaService){
        super(metaService);
    }

}
