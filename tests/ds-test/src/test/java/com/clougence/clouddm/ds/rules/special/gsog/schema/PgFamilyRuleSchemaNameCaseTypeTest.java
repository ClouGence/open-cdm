package com.clougence.clouddm.ds.rules.special.gsog.schema;

import com.clougence.clouddm.ds.gauss.analysis.gsog.GsogSecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.rdb.using_pg.schema.PgRuleSchemaNameCaseTypeTest;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PgFamilyRuleSchemaNameCaseTypeTest extends PgRuleSchemaNameCaseTypeTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new GsogSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.GaussDBForOpenGauss;
    }
}
