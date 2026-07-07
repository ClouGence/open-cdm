package com.clougence.clouddm.ds.rules.special.ob4ora.table;

import com.clougence.clouddm.ds.oceanbase.sql.ob4ora.security.ObForOraSecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.rdb.using_ora.table.OraRuleTableNeedPrimaryTest;
import com.clougence.clouddm.sdk.sql.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class OraFamilyRuleTableNeedPrimaryTest extends OraRuleTableNeedPrimaryTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new ObForOraSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.ObForOracle;
    }
}
