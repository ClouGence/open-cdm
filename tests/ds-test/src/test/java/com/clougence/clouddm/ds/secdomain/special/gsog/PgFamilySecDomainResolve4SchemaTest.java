package com.clougence.clouddm.ds.secdomain.special.gsog;

import com.clougence.clouddm.ds.gauss.analysis.gsog.GsogResAnalysisSpi;
import com.clougence.clouddm.ds.gauss.analysis.gsog.GsogSecDomainResolveSpi;
import com.clougence.clouddm.ds.gauss.analysis.gsog.GsogSplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.postgres.PgSecDomainResolve4SchemaTest;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PgFamilySecDomainResolve4SchemaTest extends PgSecDomainResolve4SchemaTest {

    public PgFamilySecDomainResolve4SchemaTest(){
        this.analysisSpi = new GsogResAnalysisSpi(null);
        this.resolveSpi = new GsogSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new GsogSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.GaussDBForOpenGauss;
    }
}
