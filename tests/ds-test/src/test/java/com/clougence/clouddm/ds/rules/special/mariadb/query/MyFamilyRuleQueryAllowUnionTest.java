package com.clougence.clouddm.ds.rules.special.mariadb.query;

import com.clougence.clouddm.ds.mariadb.analysis.MarSecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.rdb.using_my.query.MyRuleQueryAllowUnionTest;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilyRuleQueryAllowUnionTest extends MyRuleQueryAllowUnionTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new MarSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.MariaDB;
    }
}
