package com.clougence.clouddm.ds.secdomain.special.gs;

import com.clougence.clouddm.ds.gauss.sql.gs.resource.GsResAnalysisSpi;
import com.clougence.clouddm.ds.gauss.sql.gs.security.GsSecDomainResolveSpi;
import com.clougence.clouddm.ds.gauss.sql.gs.split.GsSplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.postgres.PgSecDomainResolve4ColumnTest;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PgFamilySecDomainResolve4ColumnTest extends PgSecDomainResolve4ColumnTest {

    public PgFamilySecDomainResolve4ColumnTest(){
        this.analysisSpi = new GsResAnalysisSpi(null);
        this.resolveSpi = new GsSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new GsSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.GaussDB;
    }
}
