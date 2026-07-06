package com.clougence.clouddm.ds.secdomain.special.gs;

import com.clougence.clouddm.ds.gauss.sql.gs.resource.GsResAnalysisSpi;
import com.clougence.clouddm.ds.gauss.sql.gs.security.GsSecDomainResolveSpi;
import com.clougence.clouddm.ds.gauss.sql.gs.split.GsSplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.postgres.PgSecDomainResolve4InsertTest;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PgFamilySecDomainResolve4InsertTest extends PgSecDomainResolve4InsertTest {

    public PgFamilySecDomainResolve4InsertTest(){
        this.analysisSpi = new GsResAnalysisSpi(null);
        this.resolveSpi = new GsSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new GsSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.GaussDB;
    }
}
