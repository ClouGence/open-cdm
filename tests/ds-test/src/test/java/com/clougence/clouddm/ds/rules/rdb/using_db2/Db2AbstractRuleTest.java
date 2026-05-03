package com.clougence.clouddm.ds.rules.rdb.using_db2;

import com.clougence.clouddm.dsfamily.db2.analysis.Db2SecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.AbstractRuleTest;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public abstract class Db2AbstractRuleTest extends AbstractRuleTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new Db2SecDomainResolveSpi();
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.Db2;
    }
}
