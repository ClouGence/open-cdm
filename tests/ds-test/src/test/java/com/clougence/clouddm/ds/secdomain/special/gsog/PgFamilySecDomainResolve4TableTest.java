package com.clougence.clouddm.ds.secdomain.special.gsog;

import com.clougence.clouddm.ds.gauss.sql.gs.resource.GsResAnalysisSpi;
import com.clougence.clouddm.ds.gauss.sql.gs.security.GsSecDomainResolveSpi;
import com.clougence.clouddm.ds.gauss.sql.gs.split.GsSplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.postgres.PgSecDomainResolve4TableTest;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PgFamilySecDomainResolve4TableTest extends PgSecDomainResolve4TableTest {

    public PgFamilySecDomainResolve4TableTest(){
        this.analysisSpi = new GsResAnalysisSpi(null);
        this.resolveSpi = new GsSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new GsSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.GaussDBForOpenGauss;
    }
}
