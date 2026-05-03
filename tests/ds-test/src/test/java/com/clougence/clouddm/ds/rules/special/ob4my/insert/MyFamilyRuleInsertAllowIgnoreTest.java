package com.clougence.clouddm.ds.rules.special.ob4my.insert;

import com.clougence.clouddm.ds.oceanbase.analysis.obformysql.ObSecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.rdb.using_my.insert.MyRuleInsertAllowIgnoreTest;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilyRuleInsertAllowIgnoreTest extends MyRuleInsertAllowIgnoreTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new ObSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.OceanBase;
    }
}
