package com.clougence.clouddm.ds.secdomain.special.greenplum;

import com.clougence.clouddm.ds.greenplum.analysis.GpResAnalysisSpi;
import com.clougence.clouddm.ds.greenplum.analysis.GpSecDomainResolveSpi;
import com.clougence.clouddm.ds.greenplum.analysis.GpSplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.postgres.PgSecDomainResolve4ColumnTest;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PgFamilySecDomainResolve4ColumnTest extends PgSecDomainResolve4ColumnTest {

    public PgFamilySecDomainResolve4ColumnTest(){
        this.analysisSpi = new GpResAnalysisSpi(null);
        this.resolveSpi = new GpSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new GpSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.Greenplum;
    }
}
