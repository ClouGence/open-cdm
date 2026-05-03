package com.clougence.clouddm.ds.oceanbase.analysis.obformysql;

import com.clougence.clouddm.dsfamily.mysql.analysis.MyResAnalysisSpi;
import com.clougence.clouddm.sdk.service.execute.MetaService;

public class ObResAnalysisSpi extends MyResAnalysisSpi {

    public ObResAnalysisSpi(MetaService metaService){
        super(metaService);
        this.resolveSpi = new ObSecDomainResolveSpi(metaService);
    }
}
