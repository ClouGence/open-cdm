package com.clougence.clouddm.ds.rules.special.ob4ora.column;

import com.clougence.clouddm.ds.oceanbase.analysis.ob4ora.ObForOraSecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.rdb.using_ora.column.OraRuleColumnVarCharLengthTest;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class OraFamilyRuleColumnVarCharLengthTest extends OraRuleColumnVarCharLengthTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new ObForOraSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.ObForOracle;
    }
}
