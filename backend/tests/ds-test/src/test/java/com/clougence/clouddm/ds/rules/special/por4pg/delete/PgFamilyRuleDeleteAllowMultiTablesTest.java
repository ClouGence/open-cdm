package com.clougence.clouddm.ds.rules.special.por4pg.delete;

import com.clougence.clouddm.ds.polardb.analysis.porpg.PorPgSecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.rdb.using_pg.delete.PgRuleDeleteAllowMultiTablesTest;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PgFamilyRuleDeleteAllowMultiTablesTest extends PgRuleDeleteAllowMultiTablesTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new PorPgSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.PolarDBPg;
    }
}
