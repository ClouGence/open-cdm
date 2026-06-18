package com.clougence.clouddm.ds.secdomain.special.gsog;

import com.clougence.clouddm.ds.gauss.analysis.gsog.GsogResAnalysisSpi;
import com.clougence.clouddm.ds.gauss.analysis.gsog.GsogSecDomainResolveSpi;
import com.clougence.clouddm.ds.gauss.analysis.gsog.GsogSplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.postgres.PgSecDomainResolve4QueryJoin2Test;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PgFamilySecDomainResolve4QueryJoin2Test extends PgSecDomainResolve4QueryJoin2Test {

    public PgFamilySecDomainResolve4QueryJoin2Test(){
        this.analysisSpi = new GsogResAnalysisSpi(null);
        this.resolveSpi = new GsogSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new GsogSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.GaussDBForOpenGauss;
    }
}
