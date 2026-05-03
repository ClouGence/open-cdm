package com.clougence.clouddm.ds.selectdb.analysis;

import com.clougence.clouddm.ds.doris.analysis.DrResAnalysisSpi;
import com.clougence.clouddm.sdk.analysis.secrules.ResAnalysisSpi;
import com.clougence.clouddm.sdk.service.execute.MetaService;

public class SelResAnalysisSpi extends DrResAnalysisSpi implements ResAnalysisSpi {

    public SelResAnalysisSpi(MetaService metaService){
        super(metaService);

    }

}
