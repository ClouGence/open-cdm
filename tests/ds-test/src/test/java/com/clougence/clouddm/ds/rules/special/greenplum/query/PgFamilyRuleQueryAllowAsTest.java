package com.clougence.clouddm.ds.rules.special.greenplum.query;

import com.clougence.clouddm.ds.greenplum.analysis.GpSecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.rdb.using_pg.query.PgRuleQueryAllowAsTest;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PgFamilyRuleQueryAllowAsTest extends PgRuleQueryAllowAsTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new GpSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.Greenplum;
    }
}
