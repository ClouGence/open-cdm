package com.clougence.clouddm.ds.rules.special.ob4ora.delete;

import com.clougence.clouddm.ds.oceanbase.analysis.ob4ora.ObForOraSecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.rdb.using_ora.delete.OraRuleDeleteAllowWithTest;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class OraFamilyRuleDeleteAllowWithTest extends OraRuleDeleteAllowWithTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new ObForOraSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.ObForOracle;
    }

}
