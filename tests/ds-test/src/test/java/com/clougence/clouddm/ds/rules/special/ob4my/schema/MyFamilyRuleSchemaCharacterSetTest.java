package com.clougence.clouddm.ds.rules.special.ob4my.schema;

import com.clougence.clouddm.ds.oceanbase.analysis.ob4my.ObSecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.special.mysql.schema.MyRuleSchemaCharacterSetTest;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilyRuleSchemaCharacterSetTest extends MyRuleSchemaCharacterSetTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new ObSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.OceanBase;
    }
}
