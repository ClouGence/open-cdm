package com.clougence.clouddm.ds.secdomain.special.gsog;

import com.clougence.clouddm.ds.gauss.sql.gs.resource.GsResAnalysisSpi;
import com.clougence.clouddm.ds.gauss.sql.gs.security.GsSecDomainResolveSpi;
import com.clougence.clouddm.ds.gauss.sql.gs.split.GsSplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.postgres.PgSecDomainResolve4QueryJoin1Test;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PgFamilySecDomainResolve4QueryJoin1Test extends PgSecDomainResolve4QueryJoin1Test {

    public PgFamilySecDomainResolve4QueryJoin1Test(){
        this.analysisSpi = new GsResAnalysisSpi(null);
        this.resolveSpi = new GsSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new GsSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.GaussDBForOpenGauss;
    }
}
