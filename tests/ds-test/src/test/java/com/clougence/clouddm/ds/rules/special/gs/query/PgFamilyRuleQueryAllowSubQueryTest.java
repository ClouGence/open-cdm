package com.clougence.clouddm.ds.rules.special.gs.query;

import com.clougence.clouddm.ds.gauss.analysis.gs.GsSecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.rdb.using_pg.query.PgRuleQueryAllowSubQueryTest;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PgFamilyRuleQueryAllowSubQueryTest extends PgRuleQueryAllowSubQueryTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new GsSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.GaussDB;
    }
}
