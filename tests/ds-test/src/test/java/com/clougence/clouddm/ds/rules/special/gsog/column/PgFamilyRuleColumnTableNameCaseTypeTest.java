package com.clougence.clouddm.ds.rules.special.gsog.column;

import com.clougence.clouddm.ds.gauss.analysis.gsog.GsogSecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.rdb.using_pg.column.PgRuleColumnTableNameCaseTypeTest;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PgFamilyRuleColumnTableNameCaseTypeTest extends PgRuleColumnTableNameCaseTypeTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new GsogSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.GaussDBForOpenGauss;
    }
}
