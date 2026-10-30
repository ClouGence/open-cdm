/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
//package com.clougence.clouddm.worker.api;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import jakarta.annotation.Resource;
//
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import com.clougence.clouddm.base.api.qa.ServerRServiceForDm;
//import com.clougence.clouddm.base.metadata.qa.MySQLExampleConfig;
//import com.clougence.clouddm.base.metadata.qa.MySQLExampleTable;
//import com.clougence.clouddm.comm.component.impl.RSocketApiManager;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//
///**
// * Test send msg from client to server
// *
// * @author wanshao create time is 2021/1/15
// **/
//@RunWith(SpringJUnit4ClassRunner.class)
//// @SpringBootTest(classes = DmWorkerLauncher.class)
//@Slf4j
//public class ClientToServerSendTest {
//
//    @Resource
//    private ServerRServiceForDm serverSideApiForDm;
//
//    @BeforeClass
//    public static void loadApi() {
//        System.setProperty("LOG_PATH", System.getProperty("user.home"));
//        RSocketApiManager.scanAllApiAndRegister(ClientToServerSendTest.class.getClassLoader(), "com.clougence.clouddm");
//    }
//
//    @SneakyThrows
//    @Test
//    public void testClientSender() {
//
//        List<MySQLExampleTable> tableList = new ArrayList<>();
//        MySQLExampleTable table1 = MySQLExampleTable.builder().tableName("table1").build();
//        MySQLExampleTable table2 = MySQLExampleTable.builder().tableName("table2").build();
//
//        tableList.add(table1);
//        tableList.add(table2);
//        Map<String, MySQLExampleTable> testMySQLTableMap = new HashMap<>();
//        testMySQLTableMap.put("1", table1);
//        testMySQLTableMap.put("2", table2);
//
//        MySQLExampleConfig mySQLExampleConfig = MySQLExampleConfig.builder()
//            .password("mypwd")
//            .userName("myName")
//            .version("123")
//            .tableList(tableList)
//            .tableMap(testMySQLTableMap)
//            .build();
//        List<MySQLExampleConfig> mySQLConfigs = new ArrayList<>();
//        mySQLConfigs.add(mySQLExampleConfig);
//        serverSideApiForDm.serverSideApiForDm(mySQLConfigs);
//
//        // wait receive request from server
//        Thread.sleep(5000);
//    }
//
//}
