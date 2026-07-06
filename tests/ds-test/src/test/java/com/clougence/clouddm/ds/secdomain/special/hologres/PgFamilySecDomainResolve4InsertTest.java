package com.clougence.clouddm.ds.secdomain.special.hologres;

import com.clougence.sql.postgres.resource.PgResAnalysisSpi;
import com.clougence.sql.postgres.security.PgSecDomainResolveSpi;
import com.clougence.sql.postgres.split.PgSplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.postgres.PgSecDomainResolve4InsertTest;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PgFamilySecDomainResolve4InsertTest extends PgSecDomainResolve4InsertTest {

    public PgFamilySecDomainResolve4InsertTest(){
        this.analysisSpi = new PgResAnalysisSpi(null);
        this.resolveSpi = new PgSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new PgSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.Hologres;
    }
}
