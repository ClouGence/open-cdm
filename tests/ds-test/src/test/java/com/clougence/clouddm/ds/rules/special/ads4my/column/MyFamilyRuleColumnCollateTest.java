package com.clougence.clouddm.ds.rules.special.ads4my.column;

import com.clougence.clouddm.ds.ads.analysis.ads4my.AdsMySecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.special.mysql.column.MyRuleColumnCollateTest;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilyRuleColumnCollateTest extends MyRuleColumnCollateTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new AdsMySecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.AdbForMySQL;
    }
}
