package com.clougence.clouddm.ds.secdomain.special.por4pg;

import com.clougence.sql.postgres.resource.PgResAnalysisSpi;
import com.clougence.sql.postgres.security.PgSecDomainResolveSpi;
import com.clougence.sql.postgres.split.PgSplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.postgres.PgSecDomainResolve4Query1Test;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PgFamilySecDomainResolve4Query1Test extends PgSecDomainResolve4Query1Test {

    public PgFamilySecDomainResolve4Query1Test(){
        this.analysisSpi = new PgResAnalysisSpi(null);
        this.resolveSpi = new PgSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new PgSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.PolarDBPg;
    }
}
