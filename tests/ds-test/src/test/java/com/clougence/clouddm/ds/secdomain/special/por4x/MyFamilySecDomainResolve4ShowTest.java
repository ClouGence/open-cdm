package com.clougence.clouddm.ds.secdomain.special.por4x;

import com.clougence.clouddm.ds.polardb.sql.porx.resource.PorXResAnalysisSpi;
import com.clougence.clouddm.ds.polardb.sql.porx.security.PorXSecDomainResolveSpi;
import com.clougence.clouddm.ds.polardb.sql.porx.split.PorXSplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.mysql.MySecDomainResolve4ShowTest;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

// https://dev.mysql.com/doc/refman/8.4/en/show.html
public class MyFamilySecDomainResolve4ShowTest extends MySecDomainResolve4ShowTest {

    public MyFamilySecDomainResolve4ShowTest(){
        this.analysisSpi = new PorXResAnalysisSpi(null);
        this.resolveSpi = new PorXSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new PorXSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.PolarDbX;
    }
}
