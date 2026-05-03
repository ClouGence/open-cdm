package com.clougence.clouddm.ds.rules.special.por4my.delete;

import com.clougence.clouddm.ds.polardb.analysis.pormy.PorMySecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.rdb.using_my.delete.MyRuleDeleteAllowWithTest;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilyRuleDeleteAllowWithTest extends MyRuleDeleteAllowWithTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new PorMySecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.PolarDbMySQL;
    }
}
