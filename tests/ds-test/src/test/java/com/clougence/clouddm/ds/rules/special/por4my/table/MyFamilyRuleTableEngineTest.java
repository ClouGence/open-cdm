package com.clougence.clouddm.ds.rules.special.por4my.table;

import com.clougence.clouddm.ds.polardb.analysis.pormy.PorMySecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.special.mysql.table.MyRuleTableEngineTest;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilyRuleTableEngineTest extends MyRuleTableEngineTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new PorMySecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.PolarDbMySQL;
    }
}
