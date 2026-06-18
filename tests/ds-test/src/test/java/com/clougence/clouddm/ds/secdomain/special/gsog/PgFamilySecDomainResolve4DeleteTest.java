package com.clougence.clouddm.ds.secdomain.special.gsog;

import com.clougence.clouddm.ds.gauss.analysis.gsog.GsogResAnalysisSpi;
import com.clougence.clouddm.ds.gauss.analysis.gsog.GsogSecDomainResolveSpi;
import com.clougence.clouddm.ds.gauss.analysis.gsog.GsogSplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.postgres.PgSecDomainResolve4DeleteTest;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PgFamilySecDomainResolve4DeleteTest extends PgSecDomainResolve4DeleteTest {

    public PgFamilySecDomainResolve4DeleteTest(){
        this.analysisSpi = new GsogResAnalysisSpi(null);
        this.resolveSpi = new GsogSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new GsogSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.GaussDBForOpenGauss;
    }
}
