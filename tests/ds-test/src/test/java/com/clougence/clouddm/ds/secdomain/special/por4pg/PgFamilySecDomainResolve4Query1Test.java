package com.clougence.clouddm.ds.secdomain.special.por4pg;

import com.clougence.clouddm.ds.polardb.analysis.porpg.PorPgResAnalysisSpi;
import com.clougence.clouddm.ds.polardb.analysis.porpg.PorPgSecDomainResolveSpi;
import com.clougence.clouddm.ds.polardb.analysis.porpg.PorPgSplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.postgres.PgSecDomainResolve4Query1Test;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PgFamilySecDomainResolve4Query1Test extends PgSecDomainResolve4Query1Test {

    public PgFamilySecDomainResolve4Query1Test(){
        this.analysisSpi = new PorPgResAnalysisSpi(null);
        this.resolveSpi = new PorPgSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new PorPgSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.PolarDBPg;
    }
}
