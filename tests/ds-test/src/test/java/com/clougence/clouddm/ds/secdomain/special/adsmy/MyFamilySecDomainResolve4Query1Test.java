package com.clougence.clouddm.ds.secdomain.special.adsmy;

import com.clougence.clouddm.ds.ads.analysis.ads4my.AdbMyResAnalysisSpi;
import com.clougence.clouddm.ds.ads.analysis.ads4my.AdbMySecDomainResolveSpi;
import com.clougence.clouddm.ds.secdomain.family.mysql.MySecDomainResolve4Query1Test;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilySecDomainResolve4Query1Test extends MySecDomainResolve4Query1Test {

    public MyFamilySecDomainResolve4Query1Test(){
        this.analysisSpi = new AdbMyResAnalysisSpi(null);
        this.resolveSpi = new AdbMySecDomainResolveSpi(null);
        this.dataSourceType = DataSourceType.AdbForMySQL;
    }
}
