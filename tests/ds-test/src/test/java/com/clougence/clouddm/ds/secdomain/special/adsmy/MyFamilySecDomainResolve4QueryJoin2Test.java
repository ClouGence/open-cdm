package com.clougence.clouddm.ds.secdomain.special.adsmy;

import com.clougence.clouddm.ds.ads.analysis.ads4my.AdsMyResAnalysisSpi;
import com.clougence.clouddm.ds.ads.analysis.ads4my.AdsMySecDomainResolveSpi;
import com.clougence.clouddm.ds.secdomain.family.mysql.MySecDomainResolve4QueryJoin2Test;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilySecDomainResolve4QueryJoin2Test extends MySecDomainResolve4QueryJoin2Test {

    public MyFamilySecDomainResolve4QueryJoin2Test(){
        this.analysisSpi = new AdsMyResAnalysisSpi(null);
        this.resolveSpi = new AdsMySecDomainResolveSpi(null);
        this.dataSourceType = DataSourceType.AdbForMySQL;
    }
}
