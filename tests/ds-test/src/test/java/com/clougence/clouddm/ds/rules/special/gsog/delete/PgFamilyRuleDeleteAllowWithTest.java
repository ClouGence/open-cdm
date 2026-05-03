package com.clougence.clouddm.ds.rules.special.gsog.delete;

import com.clougence.clouddm.ds.gauss.analysis.gsog.GsogSecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.rdb.using_pg.delete.PgRuleDeleteAllowWithTest;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PgFamilyRuleDeleteAllowWithTest extends PgRuleDeleteAllowWithTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new GsogSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.GaussDBForOpenGauss;
    }
}
