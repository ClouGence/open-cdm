package com.clougence.clouddm.ds.secdomain.special.mariadb;

import com.clougence.clouddm.ds.mariadb.analysis.MarResAnalysisSpi;
import com.clougence.clouddm.ds.mariadb.analysis.MarSecDomainResolveSpi;
import com.clougence.clouddm.ds.mariadb.analysis.MarSplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.mysql.MySecDomainResolve4QueryJoin2Test;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilySecDomainResolve4QueryJoin2Test extends MySecDomainResolve4QueryJoin2Test {

    public MyFamilySecDomainResolve4QueryJoin2Test(){
        this.analysisSpi = new MarResAnalysisSpi(null);
        this.resolveSpi = new MarSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new MarSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.MariaDB;
    }
}
