package com.clougence.clouddm.ds.secdomain.special.gsog;

import com.clougence.clouddm.ds.gauss.analysis.gsog.GsogResAnalysisSpi;
import com.clougence.clouddm.ds.gauss.analysis.gsog.GsogSecDomainResolveSpi;
import com.clougence.clouddm.ds.gauss.analysis.gsog.GsogSplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.postgres.PgSecDomainResolve4ColumnTest;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PgFamilySecDomainResolve4ColumnTest extends PgSecDomainResolve4ColumnTest {

    public PgFamilySecDomainResolve4ColumnTest(){
        this.analysisSpi = new GsogResAnalysisSpi(null);
        this.resolveSpi = new GsogSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new GsogSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.GaussDBForOpenGauss;
    }
}
