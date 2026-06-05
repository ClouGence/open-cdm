//package com.clougence.clouddm.base.rsocket;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import jakarta.annotation.Resource;
//
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.messaging.rsocket.RSocketRequester;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import com.clougence.clouddm.base.TestConstants;
//import com.clougence.clouddm.base.TestServerLauncher;
//import com.clougence.clouddm.base.metadata.qa.MySQLExampleConfig;
//import com.clougence.clouddm.base.metadata.qa.MySQLExampleTable;
//import com.clougence.clouddm.base.plugin.PluginLoader;
//import com.clougence.clouddm.comm.ConsoleRSocketServer;
//import com.clougence.clouddm.comm.component.server.RSocketServerSender;
//import com.clougence.clouddm.comm.component.server.ServerSideRegistry;
//import com.clougence.clouddm.comm.model.RSocketRespDTO;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//
///**
// * @author wanshao create time is 2021/1/7
// **/
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = TestServerLauncher.class)
//@Slf4j
//public class RSocketServerTest {
//
//    @Resource
//    private ServerSideRegistry   serverSideRegistry;
//
//    @Resource
//    private RSocketServerSender  rSocketServerSender;
//
//    @Resource
//    private ConsoleRSocketServer consoleRSocketServer;
//
//    @SneakyThrows
//    @Test
//    public void testServer() {
//        PluginLoader.load();
//        consoleRSocketServer.start();
//
//        testWorkerSeqNumberRegister();
//
//        testConsoleServerSender();
//
//        Thread.sleep(Long.MAX_VALUE);
//    }
//
//    private void testWorkerSeqNumberRegister() {
//        RSocketRequester requester = serverSideRegistry.getRSocketRequester("wrongWsn");
//        Assert.assertEquals(null, requester);
//    }
//
//    @SneakyThrows
//    private void testConsoleServerSender() {
//        while (true) {
//            if (serverSideRegistry.getRequesterMap().size() == 0) {
//                log.info("Waiting worker to register....");
//                Thread.sleep(1000);
//            } else {
//                break;
//            }
//        }
//
//        List<MySQLExampleTable> tableList = new ArrayList<>();
//        MySQLExampleTable table1 = MySQLExampleTable.builder().tableName("table1(server)").build();
//        MySQLExampleTable table2 = MySQLExampleTable.builder().tableName("table2(server)").build();
//
//        tableList.add(table1);
//        tableList.add(table2);
//        Map<String, MySQLExampleTable> testMySQLTableMap = new HashMap<>();
//        testMySQLTableMap.put("1", table1);
//        testMySQLTableMap.put("2", table2);
//
//        MySQLExampleConfig mySQLExampleConfig = MySQLExampleConfig.builder()
//            .password("mypwd(server)")
//            .userName("myName(server)")
//            .version("123(server)")
//            .tableList(tableList)
//            .tableMap(testMySQLTableMap)
//            .build();
//        List<MySQLExampleConfig> mySQLConfigs = new ArrayList<>();
//        mySQLConfigs.add(mySQLExampleConfig);
//        RSocketRespDTO<?> rSocketRespDTO = rSocketServerSender.requestNonBlock(TestConstants.TEST_CLIENT_API, TestConstants.TEST_WSN, new Object[] { mySQLConfigs });
//        Assert
//            .assertEquals("RSocketRespDTO(code=1, msg=Request success, data=[{\"version\":\"123(server)\",\"userName\":\"myName(server)\",\"password\":\"newPassword\",\"tableList\":[{\"tableName\":\"table1(server)\"},{\"tableName\":\"table2(server)\"}],\"tableMap\":{\"1\":{\"tableName\":\"table1(server)\"},\"2\":{\"tableName\":\"table2(server)\"}}}])", rSocketRespDTO
//                .toString());
//    }
//}
