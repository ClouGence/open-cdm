package com.clougence.clouddm.ds.rules.special.selectdb.insert;

import com.clougence.clouddm.ds.rules.rdb.using_dr.insert.DrRuleInsertAllowQueryTest;
import com.clougence.sql.doris.security.DrSecDomainResolveSpi;
import com.clougence.clouddm.sdk.sql.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class DrFamilyRuleInsertAllowQueryTest extends DrRuleInsertAllowQueryTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new DrSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.SelectDB;
    }
}
