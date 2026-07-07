package com.clougence.clouddm.ds.rules.special.por4my.delete;

import com.clougence.sql.mysql.security.MySecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.rdb.using_my.delete.MyRuleDeleteAllowSubQueryTest;
import com.clougence.clouddm.sdk.sql.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilyRuleDeleteAllowSubQueryTest extends MyRuleDeleteAllowSubQueryTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new MySecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.PolarDbMySQL;
    }
}
