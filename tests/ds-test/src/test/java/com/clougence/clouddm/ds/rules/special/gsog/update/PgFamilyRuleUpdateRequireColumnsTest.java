package com.clougence.clouddm.ds.rules.special.gsog.update;

import com.clougence.clouddm.ds.gauss.sql.gs.security.GsSecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.rdb.using_pg.update.PgRuleUpdateRequireColumnsTest;
import com.clougence.clouddm.sdk.sql.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PgFamilyRuleUpdateRequireColumnsTest extends PgRuleUpdateRequireColumnsTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new GsSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.GaussDBForOpenGauss;
    }
}
