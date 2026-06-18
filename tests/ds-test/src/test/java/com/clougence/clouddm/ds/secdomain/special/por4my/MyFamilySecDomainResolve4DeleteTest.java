package com.clougence.clouddm.ds.secdomain.special.por4my;

import com.clougence.clouddm.ds.polardb.analysis.pormy.PorMyResAnalysisSpi;
import com.clougence.clouddm.ds.polardb.analysis.pormy.PorMySecDomainResolveSpi;
import com.clougence.clouddm.ds.secdomain.family.mysql.MySecDomainResolve4DeleteTest;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilySecDomainResolve4DeleteTest extends MySecDomainResolve4DeleteTest {

    public MyFamilySecDomainResolve4DeleteTest(){
        this.analysisSpi = new PorMyResAnalysisSpi(null);
        this.resolveSpi = new PorMySecDomainResolveSpi(null);
        this.dataSourceType = DataSourceType.PolarDbMySQL;
    }
}
