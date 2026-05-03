package com.clougence.clouddm.ds.secdomain.special.hologres;

import com.clougence.clouddm.ds.hologres.analysis.HgResAnalysisSpi;
import com.clougence.clouddm.ds.hologres.analysis.HgSecDomainResolveSpi;
import com.clougence.clouddm.ds.hologres.analysis.HgSplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.postgres.PgSecDomainResolve4ConstraintTest;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PgFamilySecDomainResolve4ConstraintTest extends PgSecDomainResolve4ConstraintTest {

    public PgFamilySecDomainResolve4ConstraintTest(){
        this.analysisSpi = new HgResAnalysisSpi(null);
        this.resolveSpi = new HgSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new HgSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.Hologres;
    }
}
