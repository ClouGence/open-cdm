package com.clougence.clouddm.ds.rules.special.ob4my.delete;

import com.clougence.clouddm.ds.oceanbase.analysis.ob4my.ObSecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.rdb.using_my.delete.MyRuleDeleteAllowMultiTablesTest;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilyRuleDeleteAllowMultiTablesTest extends MyRuleDeleteAllowMultiTablesTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new ObSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.OceanBase;
    }
}
