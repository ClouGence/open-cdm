package com.clougence.clouddm.ds.secdomain.special.por4my;

import com.clougence.sql.mysql.resource.MyResAnalysisSpi;
import com.clougence.sql.mysql.security.MySecDomainResolveSpi;
import com.clougence.clouddm.ds.secdomain.family.mysql.MySecDomainResolve4AdministratorTest;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilySecDomainResolve4AdministratorTest extends MySecDomainResolve4AdministratorTest {

    public MyFamilySecDomainResolve4AdministratorTest(){
        this.analysisSpi = new MyResAnalysisSpi(null);
        this.resolveSpi = new MySecDomainResolveSpi(null);
        this.dataSourceType = DataSourceType.PolarDbMySQL;
    }
}
