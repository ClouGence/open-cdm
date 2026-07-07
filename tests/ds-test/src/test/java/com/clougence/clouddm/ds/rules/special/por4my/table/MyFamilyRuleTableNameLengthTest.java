package com.clougence.clouddm.ds.rules.special.por4my.table;

import com.clougence.sql.mysql.security.MySecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.rdb.using_my.table.MyRuleTableNameLengthTest;
import com.clougence.clouddm.sdk.sql.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilyRuleTableNameLengthTest extends MyRuleTableNameLengthTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new MySecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.PolarDbMySQL;
    }
}
