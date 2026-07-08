package com.clougence.clouddm.ds.rules.special.tidb.query;

import com.clougence.clouddm.ds.rules.rdb.using_my.query.MyRuleQueryAllowCrossInnerTest;
import com.clougence.clouddm.ds.tidb.sql.security.TiSecDomainResolveSpi;
import com.clougence.clouddm.sdk.sql.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilyRuleQueryAllowCrossInnerTest extends MyRuleQueryAllowCrossInnerTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new TiSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.TiDB;
    }
}
