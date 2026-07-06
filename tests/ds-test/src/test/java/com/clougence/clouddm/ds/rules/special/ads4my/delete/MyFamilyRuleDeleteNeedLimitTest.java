package com.clougence.clouddm.ds.rules.special.ads4my.delete;

import com.clougence.clouddm.ds.ads.sql.ads4my.security.AdsMySecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.special.mysql.delete.MyRuleDeleteNeedLimitTest;
import com.clougence.clouddm.sdk.sql.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilyRuleDeleteNeedLimitTest extends MyRuleDeleteNeedLimitTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new AdsMySecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.AdbForMySQL;
    }
}
