package com.clougence.clouddm.ds.rules.rdb.using_my;

import com.clougence.clouddm.ds.rules.AbstractRuleTest;
import com.clougence.clouddm.dsfamily.mysql.analysis.MySecDomainResolveSpi;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public abstract class MyAbstractRuleTest extends AbstractRuleTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new MySecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.MySQL;
    }
}
