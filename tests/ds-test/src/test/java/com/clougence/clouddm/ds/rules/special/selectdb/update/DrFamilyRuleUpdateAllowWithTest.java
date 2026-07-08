package com.clougence.clouddm.ds.rules.special.selectdb.update;

import com.clougence.clouddm.ds.rules.rdb.using_dr.update.DrRuleUpdateAllowWithTest;
import com.clougence.sql.doris.security.DrSecDomainResolveSpi;
import com.clougence.clouddm.sdk.sql.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class DrFamilyRuleUpdateAllowWithTest extends DrRuleUpdateAllowWithTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new DrSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.SelectDB;
    }
}
