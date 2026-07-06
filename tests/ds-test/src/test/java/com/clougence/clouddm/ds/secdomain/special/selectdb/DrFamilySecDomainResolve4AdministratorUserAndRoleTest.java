package com.clougence.clouddm.ds.secdomain.special.selectdb;

import com.clougence.clouddm.ds.secdomain.family.doris.DrSecDomainResolve4AdministratorUserAndRoleTest;
import com.clougence.sql.doris.resource.DrResAnalysisSpi;
import com.clougence.sql.doris.security.DrSecDomainResolveSpi;
import com.clougence.sql.doris.split.DrSplitAnalysisSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class DrFamilySecDomainResolve4AdministratorUserAndRoleTest extends DrSecDomainResolve4AdministratorUserAndRoleTest {

    public DrFamilySecDomainResolve4AdministratorUserAndRoleTest(){
        this.analysisSpi = new DrResAnalysisSpi(null);
        this.resolveSpi = new DrSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new DrSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.SelectDB;
    }

}
