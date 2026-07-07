package com.clougence.clouddm.ds.rules.special.tidb.table;

import com.clougence.clouddm.ds.rules.special.mysql.table.MyRuleTableRequireAutoIncrementTest;
import com.clougence.clouddm.ds.tidb.sql.security.TiSecDomainResolveSpi;
import com.clougence.clouddm.sdk.sql.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilyRuleTableRequireAutoIncrementTest extends MyRuleTableRequireAutoIncrementTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new TiSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.TiDB;
    }
}
