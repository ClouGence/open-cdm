package com.clougence.clouddm.ds.rules.special.por4my.update;

import com.clougence.sql.mysql.security.MySecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.special.mysql.update.MyRuleUpdateAllowIgnoreTest;
import com.clougence.clouddm.sdk.sql.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilyRuleUpdateAllowIgnoreTest extends MyRuleUpdateAllowIgnoreTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new MySecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.PolarDbMySQL;
    }
}
