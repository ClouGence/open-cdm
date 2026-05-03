//package com.clougence.clouddm.ds.mysql.ghost;
//
//import org.junit.Assert;
//import org.junit.Test;
//
//import com.clougence.clouddm.base.api.session.GhostOptionsDTO;
//import com.clougence.clouddm.base.api.session.ToolSessionContextDTO;
//import com.clougence.clouddm.base.metadata.ds.rdb.mysql.MySqlConfig;
//
///**
// * @author bucketli 2022/6/6 20:52:34
// */
//public class GhostSessionTest {
//
//    @Test
//    public void testGetCmdWithoutSecret() {
//        GhostSession session = new GhostSession(new MySqlConfig(), new ToolSessionContextDTO());
//        MySqlConfig config = new MySqlConfig();
//        config.setUserName("user123");
//        config.setPassword("pwd123");
//        config.setHost("111.111.111.111:3306");
//
//        String cmd = session
//            .initCmd("./gh-ost", config, "db_1", "tab_1", "alter table `tab_1` add column `col_1` varchar(255) default null", "./logpath", "./sockpath", "./panicpath", "./pausepath", new GhostOptionsDTO());
//        String str = session.getCmdWithoutSecret();
//        Assert
//            .assertEquals("./gh-ost --host=111.111.111.111 --port=3306 --user=\"user123\" --password=\"pwd123\" --database=\"db_1\" --table=\"tab_1\" --alter=\"alter table `tab_1` add column `col_1` varchar(255) default null\" --assume-rbr --allow-on-master --cut-over-lock-timeout-seconds=1 --dml-batch-size=10 --chunk-size=3000 --cut-over=default --heartbeat-interval-millis=2000 --initially-drop-ghost-table --timestamp-old-table --serve-socket-file=\"./sockpath\" --throttle-flag-file=\"./pausepath\" --panic-flag-file=\"./panicpath\" --execute", cmd);
//        Assert
//            .assertEquals("./gh-ost --host=111.111.111.111 --port=3306 --user=\"******\" --password=\"******\" --database=\"db_1\" --table=\"tab_1\" --alter=\"alter table `tab_1` add column `col_1` varchar(255) default null\" --assume-rbr --allow-on-master --cut-over-lock-timeout-seconds=1 --dml-batch-size=10 --chunk-size=3000 --cut-over=default --heartbeat-interval-millis=2000 --initially-drop-ghost-table --timestamp-old-table --serve-socket-file=\"./sockpath\" --throttle-flag-file=\"./pausepath\" --panic-flag-file=\"./panicpath\" --execute", str);
//    }
//}
