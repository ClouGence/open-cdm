package com.clougence.clouddm.ds.secdomain.special.por4my;

import com.clougence.clouddm.ds.polardb.analysis.pormy.PorMyResAnalysisSpi;
import com.clougence.clouddm.ds.polardb.analysis.pormy.PorMySecDomainResolveSpi;
import com.clougence.clouddm.ds.secdomain.family.mysql.MySecDomainResolve4QueryJoin1Test;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilySecDomainResolve4QueryJoin1Test extends MySecDomainResolve4QueryJoin1Test {

    public MyFamilySecDomainResolve4QueryJoin1Test(){
        this.analysisSpi = new PorMyResAnalysisSpi(null);
        this.resolveSpi = new PorMySecDomainResolveSpi(null);
        this.dataSourceType = DataSourceType.PolarDbMySQL;
    }
}
