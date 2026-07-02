package com.clougence.clouddm.ds.secdomain.special.adsmy;

import com.clougence.clouddm.ds.ads.analysis.ads4my.AdsMyResAnalysisSpi;
import com.clougence.clouddm.ds.ads.analysis.ads4my.AdsMySecDomainResolveSpi;
import com.clougence.clouddm.ds.secdomain.family.mysql.MySecDomainResolve4ShowTest;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

// https://dev.mysql.com/doc/refman/8.4/en/show.html
public class MyFamilySecDomainResolve4ShowTest extends MySecDomainResolve4ShowTest {

    public MyFamilySecDomainResolve4ShowTest(){
        this.analysisSpi = new AdsMyResAnalysisSpi(null);
        this.resolveSpi = new AdsMySecDomainResolveSpi(null);
        this.dataSourceType = DataSourceType.AdbForMySQL;
    }
}
