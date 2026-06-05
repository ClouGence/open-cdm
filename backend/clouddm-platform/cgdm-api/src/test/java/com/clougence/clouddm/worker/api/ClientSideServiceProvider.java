//package com.clougence.clouddm.worker.api;
//
//import java.util.List;
//
//import com.clougence.clouddm.base.api.qa.ClientRServiceForDm;
//import com.clougence.clouddm.base.metadata.qa.MySQLExampleConfig;
//import com.clougence.clouddm.comm.RSocketApiClass;
//import com.clougence.clouddm.comm.model.RSocketSendDTO;
//import lombok.extern.slf4j.Slf4j;
//
///**
// * @author wanshao create time is 2021/1/15
// **/
//@Slf4j
//@RSocketApiClass()
//public class ClientSideServiceProvider implements ClientRServiceForDm {
//
//    @Override
//    public List<MySQLExampleConfig> clientSideApiForDm(RSocketSendDTO rSocketSendDTO, List<MySQLExampleConfig> mySQLExampleConfigs) {
//        for (MySQLExampleConfig mySQLExampleConfig : mySQLExampleConfigs) {
//            mySQLExampleConfig.setPassword("newPassword");
//
//        }
//        log.warn("=========== client side api is invoked =======");
//
//        return mySQLExampleConfigs;
//
//    }
//}
