package com.clougence.clouddm.ds.rules.special.ob4ora.update;

import com.clougence.clouddm.ds.oceanbase.analysis.ob4ora.ObForOraSecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.rdb.using_ora.update.OraRuleUpdateEmptyWhereTest;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class OraFamilyRuleUpdateEmptyWhereTest extends OraRuleUpdateEmptyWhereTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new ObForOraSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.ObForOracle;
    }
}
