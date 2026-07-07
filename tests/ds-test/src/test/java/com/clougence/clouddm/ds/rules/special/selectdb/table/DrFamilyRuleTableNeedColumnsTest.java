package com.clougence.clouddm.ds.rules.special.selectdb.table;

import com.clougence.clouddm.ds.rules.rdb.using_dr.table.DrRuleTableNeedColumnsTest;
import com.clougence.sql.doris.security.DrSecDomainResolveSpi;
import com.clougence.clouddm.sdk.sql.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class DrFamilyRuleTableNeedColumnsTest extends DrRuleTableNeedColumnsTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new DrSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.SelectDB;
    }
}
