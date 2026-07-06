package com.clougence.clouddm.ds.secdomain.special.por4x;

import com.clougence.clouddm.ds.polardb.sql.porx.resource.PorXResAnalysisSpi;
import com.clougence.clouddm.ds.polardb.sql.porx.security.PorXSecDomainResolveSpi;
import com.clougence.clouddm.ds.polardb.sql.porx.split.PorXSplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.mysql.MySecDomainResolve4ConstraintTest;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilySecDomainResolve4ConstraintTest extends MySecDomainResolve4ConstraintTest {

    public MyFamilySecDomainResolve4ConstraintTest(){
        this.analysisSpi = new PorXResAnalysisSpi(null);
        this.resolveSpi = new PorXSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new PorXSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.PolarDbX;
    }
}
