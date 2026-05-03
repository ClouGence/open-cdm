package com.clougence.clouddm.ds.secdomain.special.mariadb;

import com.clougence.clouddm.ds.mariadb.analysis.MarResAnalysisSpi;
import com.clougence.clouddm.ds.mariadb.analysis.MarSecDomainResolveSpi;
import com.clougence.clouddm.ds.mariadb.analysis.MarSplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.mysql.MySecDomainResolve4TableTest;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilySecDomainResolve4TableTest extends MySecDomainResolve4TableTest {

    public MyFamilySecDomainResolve4TableTest(){
        this.analysisSpi = new MarResAnalysisSpi(null);
        this.resolveSpi = new MarSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new MarSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.MariaDB;
    }
}
