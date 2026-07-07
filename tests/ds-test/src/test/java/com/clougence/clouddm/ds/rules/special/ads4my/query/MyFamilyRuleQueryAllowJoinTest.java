package com.clougence.clouddm.ds.rules.special.ads4my.query;

import com.clougence.clouddm.ds.ads.sql.ads4my.security.AdsMySecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.rdb.using_my.query.MyRuleQueryAllowJoinTest;
import com.clougence.clouddm.sdk.sql.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilyRuleQueryAllowJoinTest extends MyRuleQueryAllowJoinTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new AdsMySecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.AdbForMySQL;
    }
}
