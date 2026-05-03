package com.clougence.clouddm.ds.secdomain.special.por4my;

import com.clougence.clouddm.ds.polardb.analysis.pormy.PorMyResAnalysisSpi;
import com.clougence.clouddm.ds.polardb.analysis.pormy.PorMySecDomainResolveSpi;
import com.clougence.clouddm.ds.secdomain.family.mysql.MySecDomainResolve4DatabaseTest;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilySecDomainResolve4DatabaseTest extends MySecDomainResolve4DatabaseTest {

    public MyFamilySecDomainResolve4DatabaseTest(){
        this.analysisSpi = new PorMyResAnalysisSpi(null);
        this.resolveSpi = new PorMySecDomainResolveSpi(null);
        this.dataSourceType = DataSourceType.PolarDbMySQL;
    }
}
