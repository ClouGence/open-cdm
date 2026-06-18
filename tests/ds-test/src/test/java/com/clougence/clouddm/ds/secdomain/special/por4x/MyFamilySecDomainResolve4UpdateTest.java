package com.clougence.clouddm.ds.secdomain.special.por4x;

import com.clougence.clouddm.ds.polardb.analysis.porx.PorXResAnalysisSpi;
import com.clougence.clouddm.ds.polardb.analysis.porx.PorXSecDomainResolveSpi;
import com.clougence.clouddm.ds.polardb.analysis.porx.PorXSplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.mysql.MySecDomainResolve4UpdateTest;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilySecDomainResolve4UpdateTest extends MySecDomainResolve4UpdateTest {

    public MyFamilySecDomainResolve4UpdateTest(){
        this.analysisSpi = new PorXResAnalysisSpi(null);
        this.resolveSpi = new PorXSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new PorXSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.PolarDbX;
    }
}
