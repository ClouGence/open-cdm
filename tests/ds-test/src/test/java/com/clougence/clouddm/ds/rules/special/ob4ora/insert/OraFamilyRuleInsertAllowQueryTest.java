package com.clougence.clouddm.ds.rules.special.ob4ora.insert;

import com.clougence.clouddm.ds.oceanbase.analysis.obforora.ObForOraSecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.rdb.using_ora.insert.OraRuleInsertAllowQueryTest;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class OraFamilyRuleInsertAllowQueryTest extends OraRuleInsertAllowQueryTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new ObForOraSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.ObForOracle;
    }
}
