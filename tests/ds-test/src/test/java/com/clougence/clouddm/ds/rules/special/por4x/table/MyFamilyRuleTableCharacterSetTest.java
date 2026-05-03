package com.clougence.clouddm.ds.rules.special.por4x.table;

import com.clougence.clouddm.ds.polardb.analysis.porx.PorXSecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.special.mysql.table.MyRuleTableCharacterSetTest;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilyRuleTableCharacterSetTest extends MyRuleTableCharacterSetTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new PorXSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.PolarDbX;
    }
}
