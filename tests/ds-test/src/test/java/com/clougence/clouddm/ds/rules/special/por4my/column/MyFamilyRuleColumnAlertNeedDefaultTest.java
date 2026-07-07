package com.clougence.clouddm.ds.rules.special.por4my.column;

import com.clougence.sql.mysql.security.MySecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.rdb.using_my.column.MyRuleColumnAlertNeedDefaultTest;
import com.clougence.clouddm.sdk.sql.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilyRuleColumnAlertNeedDefaultTest extends MyRuleColumnAlertNeedDefaultTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new MySecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.PolarDbMySQL;
    }
}
