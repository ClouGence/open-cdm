package com.clougence.clouddm.ds.secdomain.special.ob4my;

import com.clougence.clouddm.ds.oceanbase.sql.ob4my.resource.ObResAnalysisSpi;
import com.clougence.clouddm.ds.oceanbase.sql.ob4my.security.ObSecDomainResolveSpi;
import com.clougence.clouddm.ds.oceanbase.sql.ob4my.split.ObSplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.mysql.MySecDomainResolve4ConstraintTest;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilySecDomainResolve4ConstraintTest extends MySecDomainResolve4ConstraintTest {

    public MyFamilySecDomainResolve4ConstraintTest(){
        this.analysisSpi = new ObResAnalysisSpi(null);
        this.resolveSpi = new ObSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new ObSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.OceanBase;
    }
}
