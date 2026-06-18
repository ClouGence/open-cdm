package com.clougence.clouddm.ds.rules.special.selectdb.query;

import com.clougence.clouddm.ds.rules.rdb.using_dr.query.DrRuleQueryAllowUnionTest;
import com.clougence.clouddm.ds.selectdb.analysis.SelSecDomainResolveSpi;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class DrFamilyRuleQueryAllowUnionTest extends DrRuleQueryAllowUnionTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new SelSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.SelectDB;
    }
}
