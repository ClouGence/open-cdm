package com.clougence.clouddm.ds.secdomain.special.tidb;

import com.clougence.clouddm.ds.secdomain.family.mysql.MySecDomainResolve4AdministratorTest;
import com.clougence.clouddm.ds.tidb.analysis.TiResAnalysisSpi;
import com.clougence.clouddm.ds.tidb.analysis.TiSecDomainResolveSpi;
import com.clougence.clouddm.ds.tidb.analysis.TiSplitAnalysisSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilySecDomainResolve4AdministratorTest extends MySecDomainResolve4AdministratorTest {

    public MyFamilySecDomainResolve4AdministratorTest(){
        this.analysisSpi = new TiResAnalysisSpi(null);
        this.resolveSpi = new TiSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new TiSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.TiDB;
    }
}
