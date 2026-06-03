package com.clougence.clouddm.ds.rules.special.ob4ora.constraint;

import com.clougence.clouddm.ds.oceanbase.analysis.ob4ora.ObForOraSecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.rdb.using_ora.constraint.OraRuleConstraintAllowForeignTest;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class OraFamilyRuleConstraintAllowForeignTest extends OraRuleConstraintAllowForeignTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new ObForOraSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.ObForOracle;
    }
}
