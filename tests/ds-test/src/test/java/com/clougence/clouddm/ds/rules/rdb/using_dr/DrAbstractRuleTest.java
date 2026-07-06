package com.clougence.clouddm.ds.rules.rdb.using_dr;

import com.clougence.sql.doris.security.DrSecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.AbstractRuleTest;
import com.clougence.clouddm.sdk.sql.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public abstract class DrAbstractRuleTest extends AbstractRuleTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new DrSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.Doris;
    }
}
