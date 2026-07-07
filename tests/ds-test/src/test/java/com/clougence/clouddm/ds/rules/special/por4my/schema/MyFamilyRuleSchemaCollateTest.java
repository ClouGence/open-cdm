package com.clougence.clouddm.ds.rules.special.por4my.schema;

import com.clougence.sql.mysql.security.MySecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.special.mysql.schema.MyRuleSchemaCollateTest;
import com.clougence.clouddm.sdk.sql.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilyRuleSchemaCollateTest extends MyRuleSchemaCollateTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new MySecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.PolarDbMySQL;
    }
}
