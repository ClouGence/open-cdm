package com.clougence.clouddm.ds.secdomain.special.greenplum;

import com.clougence.sql.postgres.resource.PgResAnalysisSpi;
import com.clougence.sql.postgres.security.PgSecDomainResolveSpi;
import com.clougence.sql.postgres.split.PgSplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.postgres.PgSecDomainResolve4IndexTest;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PgFamilySecDomainResolve4IndexTest extends PgSecDomainResolve4IndexTest {

    public PgFamilySecDomainResolve4IndexTest(){
        this.analysisSpi = new PgResAnalysisSpi(null);
        this.resolveSpi = new PgSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new PgSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.Greenplum;
    }
}
