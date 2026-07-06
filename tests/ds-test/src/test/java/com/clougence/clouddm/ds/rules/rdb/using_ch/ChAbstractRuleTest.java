package com.clougence.clouddm.ds.rules.rdb.using_ch;

import com.clougence.clouddm.ds.clickhouse.sql.security.ChSecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.AbstractRuleTest;
import com.clougence.clouddm.sdk.sql.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public abstract class ChAbstractRuleTest extends AbstractRuleTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new ChSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.ClickHouse;
    }
}
