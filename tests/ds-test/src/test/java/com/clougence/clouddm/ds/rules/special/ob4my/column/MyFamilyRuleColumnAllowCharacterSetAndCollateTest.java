package com.clougence.clouddm.ds.rules.special.ob4my.column;

import com.clougence.clouddm.ds.oceanbase.sql.ob4my.security.ObSecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.special.mysql.column.MyRuleColumnAllowCharacterSetAndCollateTest;
import com.clougence.clouddm.sdk.sql.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilyRuleColumnAllowCharacterSetAndCollateTest extends MyRuleColumnAllowCharacterSetAndCollateTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new ObSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.OceanBase;
    }
}
