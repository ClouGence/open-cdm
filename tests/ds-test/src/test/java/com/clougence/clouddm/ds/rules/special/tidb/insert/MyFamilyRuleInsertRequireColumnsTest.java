package com.clougence.clouddm.ds.rules.special.tidb.insert;

import com.clougence.clouddm.ds.rules.rdb.using_my.insert.MyRuleInsertRequireColumnsTest;
import com.clougence.clouddm.ds.tidb.analysis.TiSecDomainResolveSpi;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilyRuleInsertRequireColumnsTest extends MyRuleInsertRequireColumnsTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new TiSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.TiDB;
    }

}
