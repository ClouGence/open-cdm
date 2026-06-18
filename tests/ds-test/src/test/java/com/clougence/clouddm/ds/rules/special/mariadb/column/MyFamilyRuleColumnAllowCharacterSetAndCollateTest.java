package com.clougence.clouddm.ds.rules.special.mariadb.column;

import com.clougence.clouddm.ds.mariadb.analysis.MarSecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.special.mysql.column.MyRuleColumnAllowCharacterSetAndCollateTest;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilyRuleColumnAllowCharacterSetAndCollateTest extends MyRuleColumnAllowCharacterSetAndCollateTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new MarSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.MariaDB;
    }
}
