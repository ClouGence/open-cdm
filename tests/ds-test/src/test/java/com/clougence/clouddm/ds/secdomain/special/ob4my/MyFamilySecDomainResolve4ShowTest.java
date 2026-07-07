package com.clougence.clouddm.ds.secdomain.special.ob4my;

import com.clougence.clouddm.ds.oceanbase.sql.ob4my.resource.ObResAnalysisSpi;
import com.clougence.clouddm.ds.oceanbase.sql.ob4my.security.ObSecDomainResolveSpi;
import com.clougence.clouddm.ds.oceanbase.sql.ob4my.split.ObSplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.mysql.MySecDomainResolve4ShowTest;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

// https://dev.mysql.com/doc/refman/8.4/en/show.html
public class MyFamilySecDomainResolve4ShowTest extends MySecDomainResolve4ShowTest {

    public MyFamilySecDomainResolve4ShowTest(){
        this.analysisSpi = new ObResAnalysisSpi(null);
        this.resolveSpi = new ObSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new ObSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.OceanBase;
    }
}
