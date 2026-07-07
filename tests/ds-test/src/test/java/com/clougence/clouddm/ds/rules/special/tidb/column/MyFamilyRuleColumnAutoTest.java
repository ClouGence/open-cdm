//package com.clougence.clouddm.ds.rules.special.tidb.column;
//
//import com.clougence.clouddm.ds.rules.special.mysql.column.MyRuleColumnAutoTest;
//import com.clougence.clouddm.ds.tidb.sql.security.TiSecDomainResolveSpi;
//import com.clougence.clouddm.sdk.sql.secrules.SecDomainResolveSpi;
//import com.clougence.clouddm.base.metadata.ds.DataSourceType;
//
//public class MyFamilyRuleColumnAutoTest extends MyRuleColumnAutoTest {
//
//    @Override
//    protected SecDomainResolveSpi createSPI() {
//        return new TiSecDomainResolveSpi();
//    }
//
//    @Override
//    protected DataSourceType currentDsType() {
//        return DataSourceType.TiDB;
//    }
//}
