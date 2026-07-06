package com.clougence.clouddm.ds.secdomain.special.ob4my;

import com.clougence.clouddm.ds.oceanbase.sql.ob4my.resource.ObResAnalysisSpi;
import com.clougence.clouddm.ds.oceanbase.sql.ob4my.security.ObSecDomainResolveSpi;
import com.clougence.clouddm.ds.oceanbase.sql.ob4my.split.ObSplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.mysql.MySecDomainResolve4DeleteTest;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilySecDomainResolve4DeleteTest extends MySecDomainResolve4DeleteTest {

    public MyFamilySecDomainResolve4DeleteTest(){
        this.analysisSpi = new ObResAnalysisSpi(null);
        this.resolveSpi = new ObSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new ObSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.OceanBase;
    }
}
