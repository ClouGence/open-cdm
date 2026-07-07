package com.clougence.clouddm.ds.rules.special.tidb.update;

import com.clougence.clouddm.ds.rules.special.mysql.update.MyRuleUpdateAllowIgnoreTest;
import com.clougence.clouddm.ds.tidb.sql.security.TiSecDomainResolveSpi;
import com.clougence.clouddm.sdk.sql.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilyRuleUpdateAllowIgnoreTest extends MyRuleUpdateAllowIgnoreTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new TiSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.TiDB;
    }
}
