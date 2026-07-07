package com.clougence.clouddm.ds.rules.special.greenplum.update;

import com.clougence.sql.postgres.security.PgSecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.rdb.using_pg.update.PgRuleUpdateAllowSubQueryTest;
import com.clougence.clouddm.sdk.sql.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PgFamilyRuleUpdateAllowSubQueryTest extends PgRuleUpdateAllowSubQueryTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new PgSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.Greenplum;
    }
}
