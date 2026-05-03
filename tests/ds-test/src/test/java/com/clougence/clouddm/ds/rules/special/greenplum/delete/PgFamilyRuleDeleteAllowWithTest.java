package com.clougence.clouddm.ds.rules.special.greenplum.delete;

import com.clougence.clouddm.ds.greenplum.analysis.GpSecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.rdb.using_pg.delete.PgRuleDeleteAllowWithTest;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PgFamilyRuleDeleteAllowWithTest extends PgRuleDeleteAllowWithTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new GpSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.Greenplum;
    }
}
