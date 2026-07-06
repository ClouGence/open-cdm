package com.clougence.clouddm.ds.rules.rdb.using_ms;

import com.clougence.clouddm.ds.rules.AbstractRuleTest;
import com.clougence.sql.sqlserver.security.MsSqlSecDomainResolveSpi;
import com.clougence.clouddm.sdk.sql.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public abstract class MsAbstractRuleTest extends AbstractRuleTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new MsSqlSecDomainResolveSpi();
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.SQLServer;
    }
}
