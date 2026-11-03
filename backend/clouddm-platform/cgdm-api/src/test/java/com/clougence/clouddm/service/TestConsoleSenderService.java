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
//package com.clougence.clouddm.service;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.stereotype.Service;
//import com.clougence.clouddm.base.metadata.qa.MySQLExampleConfig;
//import com.clougence.clouddm.base.metadata.qa.MySQLExampleTable;
//import com.clougence.clouddm.comm.RSocketApiClass;
//
///**
// * @author wanshao create time is 2021/1/8
// **/
//@Service
//@RSocketApiClass
//public class TestConsoleSenderService {
//
//    public List<Object> doExample(String param1, MySQLExampleConfig param2) {
//
//        List<MySQLExampleTable> tableList = new ArrayList<>();
//        MySQLExampleTable table1 = MySQLExampleTable.builder().tableName("table1").build();
//        MySQLExampleTable table2 = MySQLExampleTable.builder().tableName("table2").build();
//        tableList.add(table1);
//        tableList.add(table2);
//        Map<String, MySQLExampleTable> testMySQLTableMap = new HashMap<>();
//        testMySQLTableMap.put("1", table1);
//        testMySQLTableMap.put("2", table2);
//        MySQLExampleConfig mySQLExampleConfig = MySQLExampleConfig.builder()
//            .password("mypwd")
//            .userName("myName")
//            .version("123")
//            .tableList(tableList)
//            .tableMap(testMySQLTableMap)
//            .build();
//        List<Object> paramList = new ArrayList<>();
//        paramList.add("examplePram");
//        paramList.add(mySQLExampleConfig);
//        return paramList;
//    }
//}
