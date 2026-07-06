package com.clougence.clouddm.ds.secdomain.special.tidb;

import com.clougence.clouddm.ds.secdomain.family.mysql.MySecDomainResolve4ColumnTest;
import com.clougence.clouddm.ds.tidb.sql.resource.TiResAnalysisSpi;
import com.clougence.clouddm.ds.tidb.sql.security.TiSecDomainResolveSpi;
import com.clougence.clouddm.ds.tidb.sql.split.TiSplitAnalysisSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilySecDomainResolve4ColumnTest extends MySecDomainResolve4ColumnTest {

    public MyFamilySecDomainResolve4ColumnTest(){
        this.analysisSpi = new TiResAnalysisSpi(null);
        this.resolveSpi = new TiSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new TiSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.TiDB;
    }
}
