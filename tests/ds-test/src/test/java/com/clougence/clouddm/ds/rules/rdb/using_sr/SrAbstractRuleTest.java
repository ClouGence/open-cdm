package com.clougence.clouddm.ds.rules.rdb.using_sr;

import com.clougence.clouddm.ds.rules.AbstractRuleTest;
import com.clougence.clouddm.ds.starrocks.analysis.SrSecDomainResolveSpi;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public abstract class SrAbstractRuleTest extends AbstractRuleTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new SrSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.StarRocks;
    }
}
