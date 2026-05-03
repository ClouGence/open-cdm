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
