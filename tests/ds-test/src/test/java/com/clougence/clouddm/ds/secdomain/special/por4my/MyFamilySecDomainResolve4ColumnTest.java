package com.clougence.clouddm.ds.secdomain.special.por4my;

import com.clougence.sql.mysql.resource.MyResAnalysisSpi;
import com.clougence.sql.mysql.security.MySecDomainResolveSpi;
import com.clougence.clouddm.ds.secdomain.family.mysql.MySecDomainResolve4ColumnTest;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilySecDomainResolve4ColumnTest extends MySecDomainResolve4ColumnTest {

    public MyFamilySecDomainResolve4ColumnTest(){
        this.analysisSpi = new MyResAnalysisSpi(null);
        this.resolveSpi = new MySecDomainResolveSpi(null);
        this.dataSourceType = DataSourceType.PolarDbMySQL;
    }
}
