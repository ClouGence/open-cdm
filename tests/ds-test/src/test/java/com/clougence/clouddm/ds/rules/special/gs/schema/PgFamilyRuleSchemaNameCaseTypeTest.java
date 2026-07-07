package com.clougence.clouddm.ds.rules.special.gs.schema;

import com.clougence.clouddm.ds.gauss.sql.gs.security.GsSecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.rdb.using_pg.schema.PgRuleSchemaNameCaseTypeTest;
import com.clougence.clouddm.sdk.sql.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PgFamilyRuleSchemaNameCaseTypeTest extends PgRuleSchemaNameCaseTypeTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new GsSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.GaussDB;
    }
}
