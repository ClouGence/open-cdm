package com.clougence.clouddm.ds.rules.special.ob4ora.table;

import com.clougence.clouddm.ds.oceanbase.sql.ob4ora.security.ObForOraSecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.rdb.using_ora.table.OraRuleTableMaxColumnsTest;
import com.clougence.clouddm.sdk.sql.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class OraFamilyRuleTableMaxColumnsTest extends OraRuleTableMaxColumnsTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new ObForOraSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.ObForOracle;
    }
}
