package com.clougence.clouddm.ds.secdomain.special.tidb;

import com.clougence.clouddm.ds.secdomain.family.mysql.MySecDomainResolve4InsertTest;
import com.clougence.clouddm.ds.tidb.sql.resource.TiResAnalysisSpi;
import com.clougence.clouddm.ds.tidb.sql.security.TiSecDomainResolveSpi;
import com.clougence.clouddm.ds.tidb.sql.split.TiSplitAnalysisSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilySecDomainResolve4InsertTest extends MySecDomainResolve4InsertTest {

    public MyFamilySecDomainResolve4InsertTest(){
        this.analysisSpi = new TiResAnalysisSpi(null);
        this.resolveSpi = new TiSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new TiSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.TiDB;
    }
}
