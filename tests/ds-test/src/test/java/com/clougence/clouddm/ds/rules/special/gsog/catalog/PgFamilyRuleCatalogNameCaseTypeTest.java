package com.clougence.clouddm.ds.rules.special.gsog.catalog;

import com.clougence.clouddm.ds.gauss.sql.gs.security.GsSecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.rdb.using_pg.catalog.PgRuleCatalogNameCaseTypeTest;
import com.clougence.clouddm.sdk.sql.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PgFamilyRuleCatalogNameCaseTypeTest extends PgRuleCatalogNameCaseTypeTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new GsSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.GaussDBForOpenGauss;
    }
}
