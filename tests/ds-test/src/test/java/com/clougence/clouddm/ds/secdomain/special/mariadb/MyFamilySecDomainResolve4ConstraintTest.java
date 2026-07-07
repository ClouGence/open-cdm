package com.clougence.clouddm.ds.secdomain.special.mariadb;

import com.clougence.sql.mysql.resource.MyResAnalysisSpi;
import com.clougence.sql.mysql.security.MySecDomainResolveSpi;
import com.clougence.sql.mysql.split.MySplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.mysql.MySecDomainResolve4ConstraintTest;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilySecDomainResolve4ConstraintTest extends MySecDomainResolve4ConstraintTest {

    public MyFamilySecDomainResolve4ConstraintTest(){
        this.analysisSpi = new MyResAnalysisSpi(null);
        this.resolveSpi = new MySecDomainResolveSpi(null);
        this.splitAnalysisSpi = new MySplitAnalysisSpi();
        this.dataSourceType = DataSourceType.MariaDB;
    }
}
