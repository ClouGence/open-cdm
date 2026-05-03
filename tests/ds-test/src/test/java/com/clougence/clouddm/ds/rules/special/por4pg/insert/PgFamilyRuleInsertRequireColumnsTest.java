package com.clougence.clouddm.ds.rules.special.por4pg.insert;

import com.clougence.clouddm.ds.polardb.analysis.porpg.PorPgSecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.rdb.using_pg.insert.PgRuleInsertRequireColumnsTest;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PgFamilyRuleInsertRequireColumnsTest extends PgRuleInsertRequireColumnsTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new PorPgSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.PolarDBPg;
    }
}
