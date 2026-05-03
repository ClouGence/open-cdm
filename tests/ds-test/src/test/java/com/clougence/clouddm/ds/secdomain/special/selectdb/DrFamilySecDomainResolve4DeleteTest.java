package com.clougence.clouddm.ds.secdomain.special.selectdb;

import com.clougence.clouddm.ds.secdomain.family.doris.DrSecDomainResolve4DeleteTest;
import com.clougence.clouddm.ds.selectdb.analysis.SelResAnalysisSpi;
import com.clougence.clouddm.ds.selectdb.analysis.SelSecDomainResolveSpi;
import com.clougence.clouddm.ds.selectdb.analysis.SelSplitAnalysisSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class DrFamilySecDomainResolve4DeleteTest extends DrSecDomainResolve4DeleteTest {

    public DrFamilySecDomainResolve4DeleteTest(){
        this.analysisSpi = new SelResAnalysisSpi(null);
        this.resolveSpi = new SelSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new SelSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.SelectDB;
    }

}
