package com.clougence.clouddm.ds.secdomain.special.greenplum;

import com.clougence.clouddm.ds.greenplum.analysis.GpResAnalysisSpi;
import com.clougence.clouddm.ds.greenplum.analysis.GpSecDomainResolveSpi;
import com.clougence.clouddm.ds.greenplum.analysis.GpSplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.postgres.PgSecDomainResolve4DatabaseTest;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PgFamilySecDomainResolve4DatabaseTest extends PgSecDomainResolve4DatabaseTest {

    public PgFamilySecDomainResolve4DatabaseTest(){
        this.analysisSpi = new GpResAnalysisSpi(null);
        this.resolveSpi = new GpSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new GpSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.Greenplum;
    }
}
