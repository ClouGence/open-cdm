package com.clougence.clouddm.ds.secdomain.special.adsmy;

import com.clougence.clouddm.ds.ads.analysis.ads4my.AdbMyResAnalysisSpi;
import com.clougence.clouddm.ds.ads.analysis.ads4my.AdbMySecDomainResolveSpi;
import com.clougence.clouddm.ds.ads.analysis.ads4my.AdbMySplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.mysql.MySecDomainResolve4InsertTest;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilySecDomainResolve4InsertTest extends MySecDomainResolve4InsertTest {

    public MyFamilySecDomainResolve4InsertTest(){
        this.analysisSpi = new AdbMyResAnalysisSpi(null);
        this.resolveSpi = new AdbMySecDomainResolveSpi(null);
        this.splitAnalysisSpi = new AdbMySplitAnalysisSpi();
        this.dataSourceType = DataSourceType.AdbForMySQL;
    }
}
