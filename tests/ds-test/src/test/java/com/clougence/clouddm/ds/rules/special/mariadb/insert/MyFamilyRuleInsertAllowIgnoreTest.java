package com.clougence.clouddm.ds.rules.special.mariadb.insert;

import com.clougence.sql.mysql.security.MySecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.rdb.using_my.insert.MyRuleInsertAllowIgnoreTest;
import com.clougence.clouddm.sdk.sql.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilyRuleInsertAllowIgnoreTest extends MyRuleInsertAllowIgnoreTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new MySecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.MariaDB;
    }
}
