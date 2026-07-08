package com.clougence.clouddm.ds.rules.special.tidb.column;

import com.clougence.clouddm.ds.rules.rdb.using_my.column.MyRuleColumnTableNameCaseTypeTest;
import com.clougence.clouddm.ds.tidb.sql.security.TiSecDomainResolveSpi;
import com.clougence.clouddm.sdk.sql.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilyRuleColumnTableNameCaseTypeTest extends MyRuleColumnTableNameCaseTypeTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new TiSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.TiDB;
    }
}
