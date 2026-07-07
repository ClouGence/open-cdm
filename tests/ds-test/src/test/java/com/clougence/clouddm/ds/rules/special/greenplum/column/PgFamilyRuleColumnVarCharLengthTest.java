package com.clougence.clouddm.ds.rules.special.greenplum.column;

import com.clougence.sql.postgres.security.PgSecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.rdb.using_pg.column.PgRuleColumnVarCharLengthTest;
import com.clougence.clouddm.sdk.sql.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PgFamilyRuleColumnVarCharLengthTest extends PgRuleColumnVarCharLengthTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new PgSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.Greenplum;
    }
}
