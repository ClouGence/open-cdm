package com.clougence.clouddm.ds.secdomain.special.gs;

import com.clougence.clouddm.ds.gauss.analysis.gs.GsResAnalysisSpi;
import com.clougence.clouddm.ds.gauss.analysis.gs.GsSecDomainResolveSpi;
import com.clougence.clouddm.ds.gauss.analysis.gs.GsSplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.postgres.PgSecDomainResolve4CommonTest;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PgFamilySecDomainResolve4CommonTest extends PgSecDomainResolve4CommonTest {

    public PgFamilySecDomainResolve4CommonTest(){
        this.analysisSpi = new GsResAnalysisSpi(null);
        this.resolveSpi = new GsSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new GsSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.GaussDB;
    }
}
