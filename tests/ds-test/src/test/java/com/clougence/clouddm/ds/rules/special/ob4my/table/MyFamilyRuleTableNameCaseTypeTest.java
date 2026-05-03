package com.clougence.clouddm.ds.rules.special.ob4my.table;

import com.clougence.clouddm.ds.oceanbase.analysis.obformysql.ObSecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.rdb.using_my.table.MyRuleTableNameCaseTypeTest;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilyRuleTableNameCaseTypeTest extends MyRuleTableNameCaseTypeTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new ObSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.OceanBase;
    }
}
