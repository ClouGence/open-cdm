package com.clougence.clouddm.ds.secdomain.special.ob4my;

import com.clougence.clouddm.ds.oceanbase.analysis.ob4my.ObResAnalysisSpi;
import com.clougence.clouddm.ds.oceanbase.analysis.ob4my.ObSecDomainResolveSpi;
import com.clougence.clouddm.ds.oceanbase.analysis.ob4my.ObSplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.mysql.MySecDomainResolve4AdministratorUserAndRoleTest;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilySecDomainResolve4AdministratorUserAndRoleTest extends MySecDomainResolve4AdministratorUserAndRoleTest {

    public MyFamilySecDomainResolve4AdministratorUserAndRoleTest(){
        this.analysisSpi = new ObResAnalysisSpi(null);
        this.resolveSpi = new ObSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new ObSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.OceanBase;
    }
}
