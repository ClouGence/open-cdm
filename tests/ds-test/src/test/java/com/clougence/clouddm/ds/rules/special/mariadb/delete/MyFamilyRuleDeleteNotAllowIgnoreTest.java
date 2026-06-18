package com.clougence.clouddm.ds.rules.special.mariadb.delete;

import com.clougence.clouddm.ds.mariadb.analysis.MarSecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.special.mysql.delete.MyRuleDeleteNotAllowIgnoreTest;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilyRuleDeleteNotAllowIgnoreTest extends MyRuleDeleteNotAllowIgnoreTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new MarSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.MariaDB;
    }
}
