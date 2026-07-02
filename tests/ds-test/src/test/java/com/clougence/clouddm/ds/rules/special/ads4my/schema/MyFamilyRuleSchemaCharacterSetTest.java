package com.clougence.clouddm.ds.rules.special.ads4my.schema;

import com.clougence.clouddm.ds.ads.analysis.ads4my.AdsMySecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.special.mysql.schema.MyRuleSchemaCharacterSetTest;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilyRuleSchemaCharacterSetTest extends MyRuleSchemaCharacterSetTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new AdsMySecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.AdbForMySQL;
    }
}
