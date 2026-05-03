package com.clougence.clouddm.ds.secdomain.special.selectdb;

import com.clougence.clouddm.ds.secdomain.family.doris.DrSecDomainResolve4QueryJoin1Test;
import com.clougence.clouddm.ds.selectdb.analysis.SelResAnalysisSpi;
import com.clougence.clouddm.ds.selectdb.analysis.SelSecDomainResolveSpi;
import com.clougence.clouddm.ds.selectdb.analysis.SelSplitAnalysisSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class DrFamilySecDomainResolve4QueryJoin1Test extends DrSecDomainResolve4QueryJoin1Test {

    public DrFamilySecDomainResolve4QueryJoin1Test(){
        this.analysisSpi = new SelResAnalysisSpi(null);
        this.resolveSpi = new SelSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new SelSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.SelectDB;
    }

}
