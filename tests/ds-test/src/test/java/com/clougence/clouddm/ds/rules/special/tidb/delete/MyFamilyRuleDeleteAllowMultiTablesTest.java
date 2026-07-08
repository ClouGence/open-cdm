package com.clougence.clouddm.ds.rules.special.tidb.delete;

import com.clougence.clouddm.ds.rules.rdb.using_my.delete.MyRuleDeleteAllowMultiTablesTest;
import com.clougence.clouddm.ds.tidb.sql.security.TiSecDomainResolveSpi;
import com.clougence.clouddm.sdk.sql.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilyRuleDeleteAllowMultiTablesTest extends MyRuleDeleteAllowMultiTablesTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new TiSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.TiDB;
    }
}
