//package com.clougence.clouddm.base;
//
//import java.util.List;
//
//import com.clougence.clouddm.base.api.qa.ServerRServiceForComm;
//import com.clougence.clouddm.base.metadata.qa.MySQLExampleConfig;
//import com.clougence.clouddm.comm.RSocketApiClass;
//
///**
// * [Attentions!!!] Do not delete this example api which is used to test rsocket communication
// *
// * @author wanshao create time is 2021/1/9
// **/
//@RSocketApiClass
//public class ServerSideServiceForCommCommProvider implements ServerRServiceForComm {
//
//    public List<MySQLExampleConfig> serverSideApi(List<MySQLExampleConfig> mySQLExampleConfigs) {
//        for (MySQLExampleConfig mySQLExampleConfig : mySQLExampleConfigs) {
//            mySQLExampleConfig.setPassword("newPassword");
//
//        }
//        return mySQLExampleConfigs;
//
//    }
//}
