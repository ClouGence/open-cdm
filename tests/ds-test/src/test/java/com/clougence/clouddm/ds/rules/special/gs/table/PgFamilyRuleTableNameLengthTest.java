package com.clougence.clouddm.ds.rules.special.gs.table;

import com.clougence.clouddm.ds.gauss.sql.gs.security.GsSecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.rdb.using_pg.table.PgRuleTableNameLengthTest;
import com.clougence.clouddm.sdk.sql.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PgFamilyRuleTableNameLengthTest extends PgRuleTableNameLengthTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new GsSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.GaussDB;
    }
}
