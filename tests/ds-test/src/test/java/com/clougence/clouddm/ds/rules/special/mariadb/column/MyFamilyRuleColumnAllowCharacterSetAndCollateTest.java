package com.clougence.clouddm.ds.rules.special.mariadb.column;

import com.clougence.sql.mysql.security.MySecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.special.mysql.column.MyRuleColumnAllowCharacterSetAndCollateTest;
import com.clougence.clouddm.sdk.sql.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilyRuleColumnAllowCharacterSetAndCollateTest extends MyRuleColumnAllowCharacterSetAndCollateTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new MySecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.MariaDB;
    }
}
