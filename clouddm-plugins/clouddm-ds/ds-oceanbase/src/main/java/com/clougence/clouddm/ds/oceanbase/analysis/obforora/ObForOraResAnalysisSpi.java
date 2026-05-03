package com.clougence.clouddm.ds.oceanbase.analysis.obforora;

import com.clougence.clouddm.ds.oracle.analysis.OraResAnalysisSpi;
import com.clougence.clouddm.sdk.service.execute.MetaService;

public class ObForOraResAnalysisSpi extends OraResAnalysisSpi {

    public ObForOraResAnalysisSpi(MetaService metaService){
        super(metaService);
        this.resolveSpi = new ObForOraSecDomainResolveSpi(metaService);
    }
}
