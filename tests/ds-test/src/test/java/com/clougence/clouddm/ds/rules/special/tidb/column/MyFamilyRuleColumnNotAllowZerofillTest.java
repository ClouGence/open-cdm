//package com.clougence.clouddm.ds.rules.special.tidb.column;
//
//import com.clougence.clouddm.ds.rules.special.mysql.column.MyRuleColumnNotAllowZerofillTest;
//import com.clougence.clouddm.ds.tidb.analysis.TiSecDomainResolveSpi;
//import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
//import com.clougence.clouddm.base.metadata.ds.DataSourceType;
//
//public class MyFamilyRuleColumnNotAllowZerofillTest extends MyRuleColumnNotAllowZerofillTest {
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
