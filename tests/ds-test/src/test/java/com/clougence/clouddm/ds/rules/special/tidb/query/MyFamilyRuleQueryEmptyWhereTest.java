package com.clougence.clouddm.ds.rules.special.tidb.query;

import com.clougence.clouddm.ds.rules.rdb.using_my.query.MyRuleQueryEmptyWhereTest;
import com.clougence.clouddm.ds.tidb.analysis.TiSecDomainResolveSpi;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilyRuleQueryEmptyWhereTest extends MyRuleQueryEmptyWhereTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new TiSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.TiDB;
    }
}
