package com.clougence.clouddm.ds.secdomain.special.selectdb;

import com.clougence.clouddm.ds.secdomain.family.doris.DrSecDomainResolve4AdministratorTest;
import com.clougence.sql.doris.resource.DrResAnalysisSpi;
import com.clougence.sql.doris.security.DrSecDomainResolveSpi;
import com.clougence.sql.doris.split.DrSplitAnalysisSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class DrFamilySecDomainResolve4AdministratorTest extends DrSecDomainResolve4AdministratorTest {

    public DrFamilySecDomainResolve4AdministratorTest(){
        this.analysisSpi = new DrResAnalysisSpi(null);
        this.resolveSpi = new DrSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new DrSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.SelectDB;
    }

}
