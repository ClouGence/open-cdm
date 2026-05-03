package com.clougence.clouddm.ds.rules.special.selectdb.table;

import com.clougence.clouddm.ds.rules.rdb.using_dr.table.DrRuleTableNeedCommentTest;
import com.clougence.clouddm.ds.selectdb.analysis.SelSecDomainResolveSpi;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class DrFamilyRuleTableNeedCommentTest extends DrRuleTableNeedCommentTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new SelSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.SelectDB;
    }
}
