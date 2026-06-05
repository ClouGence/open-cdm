package com.clougence.clouddm.ds.secdomain.special.greenplum;

import com.clougence.clouddm.ds.greenplum.analysis.GpResAnalysisSpi;
import com.clougence.clouddm.ds.greenplum.analysis.GpSecDomainResolveSpi;
import com.clougence.clouddm.ds.greenplum.analysis.GpSplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.postgres.PgSecDomainResolve4TableTest;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PgFamilySecDomainResolve4TableTest extends PgSecDomainResolve4TableTest {

    public PgFamilySecDomainResolve4TableTest(){
        this.analysisSpi = new GpResAnalysisSpi(null);
        this.resolveSpi = new GpSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new GpSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.Greenplum;
    }
}
