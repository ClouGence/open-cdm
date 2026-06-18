package com.clougence.clouddm.ds.secdomain.special.adsmy;

import com.clougence.clouddm.ds.ads.analysis.ads4my.AdbMyResAnalysisSpi;
import com.clougence.clouddm.ds.ads.analysis.ads4my.AdbMySecDomainResolveSpi;
import com.clougence.clouddm.ds.secdomain.family.mysql.MySecDomainResolve4AdministratorUserAndRoleTest;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilySecDomainResolve4AdministratorUserAndRoleTest extends MySecDomainResolve4AdministratorUserAndRoleTest {

    public MyFamilySecDomainResolve4AdministratorUserAndRoleTest(){
        this.analysisSpi = new AdbMyResAnalysisSpi(null);
        this.resolveSpi = new AdbMySecDomainResolveSpi(null);
        this.dataSourceType = DataSourceType.AdbForMySQL;
    }
}
