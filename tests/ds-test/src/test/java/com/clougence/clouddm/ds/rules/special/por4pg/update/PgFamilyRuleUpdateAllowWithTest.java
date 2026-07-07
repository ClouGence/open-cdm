package com.clougence.clouddm.ds.rules.special.por4pg.update;

import com.clougence.sql.postgres.security.PgSecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.rdb.using_pg.update.PgRuleUpdateAllowWithTest;
import com.clougence.clouddm.sdk.sql.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PgFamilyRuleUpdateAllowWithTest extends PgRuleUpdateAllowWithTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new PgSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.PolarDBPg;
    }
}
