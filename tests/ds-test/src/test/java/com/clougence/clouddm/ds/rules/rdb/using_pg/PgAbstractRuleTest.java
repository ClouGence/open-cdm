package com.clougence.clouddm.ds.rules.rdb.using_pg;

import com.clougence.clouddm.ds.rules.AbstractRuleTest;
import com.clougence.clouddm.dsfamily.postgres.analysis.PgSecDomainResolveSpi;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public abstract class PgAbstractRuleTest extends AbstractRuleTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new PgSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.PostgreSQL;
    }
}
