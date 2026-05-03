//package com.clougence.clouddm.base.rsocket;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//import javax.annotation.Resource;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import com.clougence.clouddm.base.TestClientLauncher;
//import com.clougence.clouddm.base.TestConstants;
//import com.clougence.clouddm.base.metadata.qa.MySQLExampleConfig;
//import com.clougence.clouddm.base.metadata.qa.MySQLExampleTable;
//import com.clougence.clouddm.base.plugin.PluginLoader;
//import com.clougence.clouddm.comm.WorkerRSocketClient;
//import com.clougence.clouddm.comm.component.client.RSocketClientSender;
//import com.clougence.clouddm.comm.model.RSocketRespDTO;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//
///**
// * @author wanshao create time is 2021/1/7
// **/
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = TestClientLauncher.class)
//@Slf4j
//public class RSocketClientTest {
//
//    @Resource
//    private WorkerRSocketClient workerRSocketClient;
//
//    @Resource
//    private RSocketClientSender rSocketClientSender;
//
//    @SneakyThrows
//    @Test
//    public void testWorker() {
//
//        // load plugin ds jar
//        PluginLoader.load();
//
//        workerRSocketClient.start();
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
//
//        RSocketRespDTO<?> rSocketRespDTO = rSocketClientSender.requestNonBlock(TestConstants.TEST_SERVER_API, null, new Object[] { mySQLConfigs });
//
//        // do common response
//        System.out.println("hello");
//
//        // testReconnect(mySQLConfigs);
//
//        // wait to receive console's request
//        Thread.sleep(Long.MAX_VALUE);
//
//    }
//
//    private void testReconnect(List<MySQLExampleConfig> mySQLConfigs) {
//        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
//        scheduledExecutorService.scheduleWithFixedDelay(() -> {
//            log.info("Begin to send msg to server.....");
//            RSocketRespDTO<?> rSocketRespDTO = rSocketClientSender.requestNonBlock(TestConstants.TEST_SERVER_API, null, new Object[] { mySQLConfigs });
//
//        }, 5, 5, TimeUnit.SECONDS);
//    }
//}
