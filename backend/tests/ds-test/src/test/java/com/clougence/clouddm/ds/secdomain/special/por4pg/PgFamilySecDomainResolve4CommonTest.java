package com.clougence.clouddm.ds.secdomain.special.por4pg;

import com.clougence.clouddm.ds.polardb.analysis.porpg.PorPgResAnalysisSpi;
import com.clougence.clouddm.ds.polardb.analysis.porpg.PorPgSecDomainResolveSpi;
import com.clougence.clouddm.ds.polardb.analysis.porpg.PorPgSplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.postgres.PgSecDomainResolve4CommonTest;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PgFamilySecDomainResolve4CommonTest extends PgSecDomainResolve4CommonTest {

    public PgFamilySecDomainResolve4CommonTest(){
        this.analysisSpi = new PorPgResAnalysisSpi(null);
        this.resolveSpi = new PorPgSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new PorPgSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.PolarDBPg;
    }
}
