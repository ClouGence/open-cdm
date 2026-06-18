package com.clougence.clouddm.ds.rules.special.ads4my.constraint;

import com.clougence.clouddm.ds.ads.analysis.ads4my.AdbMySecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.rdb.using_my.constraint.MyRuleConstraintAllowForeignTest;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilyRuleConstraintAllowForeignTest extends MyRuleConstraintAllowForeignTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new AdbMySecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.AdbForMySQL;
    }
}
