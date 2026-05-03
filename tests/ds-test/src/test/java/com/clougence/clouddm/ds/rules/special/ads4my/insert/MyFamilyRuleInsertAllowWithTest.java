package com.clougence.clouddm.ds.rules.special.ads4my.insert;

import com.clougence.clouddm.ds.ads.analysis.ads4my.AdbMySecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.rdb.using_my.insert.MyRuleInsertAllowWithTest;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilyRuleInsertAllowWithTest extends MyRuleInsertAllowWithTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new AdbMySecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.AdbForMySQL;
    }
}
