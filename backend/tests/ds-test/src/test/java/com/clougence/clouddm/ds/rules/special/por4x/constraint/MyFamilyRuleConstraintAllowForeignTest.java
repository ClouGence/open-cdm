package com.clougence.clouddm.ds.rules.special.por4x.constraint;

import com.clougence.clouddm.ds.polardb.analysis.porx.PorXSecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.rdb.using_my.constraint.MyRuleConstraintAllowForeignTest;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilyRuleConstraintAllowForeignTest extends MyRuleConstraintAllowForeignTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new PorXSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.PolarDbX;
    }
}
