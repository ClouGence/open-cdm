package com.clougence.clouddm.ds.secdomain.special.adsmy;

import com.clougence.clouddm.ds.ads.sql.ads4my.resource.AdsMyResAnalysisSpi;
import com.clougence.clouddm.ds.ads.sql.ads4my.security.AdsMySecDomainResolveSpi;
import com.clougence.clouddm.ds.secdomain.family.mysql.MySecDomainResolve4UpdateTest;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilySecDomainResolve4UpdateTest extends MySecDomainResolve4UpdateTest {

    public MyFamilySecDomainResolve4UpdateTest(){
        this.analysisSpi = new AdsMyResAnalysisSpi(null);
        this.resolveSpi = new AdsMySecDomainResolveSpi(null);
        this.dataSourceType = DataSourceType.AdbForMySQL;
    }
}
