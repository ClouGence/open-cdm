package com.clougence.clouddm.ds.secdomain.special.por4my;

import com.clougence.clouddm.ds.polardb.analysis.pormy.PorMyResAnalysisSpi;
import com.clougence.clouddm.ds.polardb.analysis.pormy.PorMySecDomainResolveSpi;
import com.clougence.clouddm.ds.secdomain.family.mysql.MySecDomainResolve4QueryJoin2Test;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilySecDomainResolve4QueryJoin2Test extends MySecDomainResolve4QueryJoin2Test {

    public MyFamilySecDomainResolve4QueryJoin2Test(){
        this.analysisSpi = new PorMyResAnalysisSpi(null);
        this.resolveSpi = new PorMySecDomainResolveSpi(null);
        this.dataSourceType = DataSourceType.PolarDbMySQL;
    }
}
