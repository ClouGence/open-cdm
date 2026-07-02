package com.clougence.clouddm.ds.secdomain.special.adsmy;

import com.clougence.clouddm.ds.ads.analysis.ads4my.AdsMyResAnalysisSpi;
import com.clougence.clouddm.ds.ads.analysis.ads4my.AdsMySecDomainResolveSpi;
import com.clougence.clouddm.ds.secdomain.family.mysql.MySecDomainResolve4ColumnTest;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilySecDomainResolve4ColumnTest extends MySecDomainResolve4ColumnTest {

    public MyFamilySecDomainResolve4ColumnTest(){
        this.analysisSpi = new AdsMyResAnalysisSpi(null);
        this.resolveSpi = new AdsMySecDomainResolveSpi(null);
        this.dataSourceType = DataSourceType.AdbForMySQL;
    }
}
