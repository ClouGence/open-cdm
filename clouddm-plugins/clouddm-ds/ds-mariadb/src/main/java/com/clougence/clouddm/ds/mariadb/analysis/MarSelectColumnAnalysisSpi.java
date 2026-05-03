package com.clougence.clouddm.ds.mariadb.analysis;

import com.clougence.clouddm.dsfamily.mysql.analysis.MySelectColumnAnalysisSpi;
import com.clougence.clouddm.sdk.service.execute.MetaService;

public class MarSelectColumnAnalysisSpi extends MySelectColumnAnalysisSpi {

    public MarSelectColumnAnalysisSpi(MetaService metaService){
        super(metaService);
    }

}
