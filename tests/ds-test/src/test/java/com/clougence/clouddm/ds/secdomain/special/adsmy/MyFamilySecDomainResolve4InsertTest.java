package com.clougence.clouddm.ds.secdomain.special.adsmy;

import com.clougence.clouddm.ds.ads.sql.ads4my.resource.AdsMyResAnalysisSpi;
import com.clougence.clouddm.ds.ads.sql.ads4my.security.AdsMySecDomainResolveSpi;
import com.clougence.clouddm.ds.ads.sql.ads4my.split.AdsMySplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.mysql.MySecDomainResolve4InsertTest;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilySecDomainResolve4InsertTest extends MySecDomainResolve4InsertTest {

    public MyFamilySecDomainResolve4InsertTest(){
        this.analysisSpi = new AdsMyResAnalysisSpi(null);
        this.resolveSpi = new AdsMySecDomainResolveSpi(null);
        this.splitAnalysisSpi = new AdsMySplitAnalysisSpi();
        this.dataSourceType = DataSourceType.AdbForMySQL;
    }
}
