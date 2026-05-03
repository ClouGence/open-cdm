//package com.clougence.faker.realdb;
//
//import org.junit.Test;
//
//import com.alibaba.druid.pool.DruidDataSource;
//import com.clougence.faker.execute.FakerConfig;
//import com.clougence.faker.engine.FakerEngine;
//import com.clougence.faker.generator.FakerFactory;
//import com.clougence.faker.generator.FakerRepository;
//import com.clougence.faker.generator.FakerTable;
//import com.clougence.faker.generator.SqlPolitic;
//import com.clougence.faker.generator.loader.PrecociousDataLoaderFactory;
//import com.clougence.schema.DsType;
//
//public class SqlServerTest {
//
//    @Test
//    public void workloadTest() throws Exception {
//        // 全局配置
//        FakerConfig fakerConfig = new FakerConfig();
//        fakerConfig.setDbType(DsType.SqlServer);
//        fakerConfig.setTransaction(false);
//        //fakerConfig.setPolicy("extreme");
//        fakerConfig.setDataLoaderFactory(new PrecociousDataLoaderFactory());
//        //        fakerConfig.addIgnoreError("Duplicate");
//        //        fakerConfig.addIgnoreError("restarting");
//        //        fakerConfig.addIgnoreError("deadlocked");
//        //        fakerConfig.addIgnoreError("was deadlocked on lock");
//        fakerConfig.addIgnoreError("违反了 PRIMARY KEY");
//        fakerConfig.addIgnoreError("违反了 UNIQUE KEY");
//        fakerConfig.addIgnoreError("The incoming tabular data stream (TDS) remote procedure call (RPC) protocol stream is incorrect");
//        fakerConfig.setOpsRatio("INSERT#50;UPDATE#50;DELETE#10");//");//UPDATE#50;DELETE#10");
//
//        // 生成器，配置表
//        DruidDataSource dataDs = DsUtils.dsSqlServer();
//        FakerFactory factory = new FakerFactory(dataDs, fakerConfig);
//        FakerRepository generator = new FakerRepository(factory);
//        generator.addTable("console", "dbo", "alert_config_detail");
//        generator.addTable("console", "dbo", "alert_event_log");
//        //        generator.addTable("console", "dbo", "alert_receiver");
//        //        generator.addTable("console", "dbo", "mt5_deals");
//        //        generator.addTable("console", "dbo", "mts_lang_data");
//        //        generator.addTable("console", "dbo", "tb_sqlserver_types");
//        //        generator.addTable("console", "dbo", "ver_table_1");
//        //        generator.addTable("console", "dbo", "ver_table_2");
//        //        generator.addTable("console", "dbo", "ver_table_3");
//
//        //tb_sqlserver_types
//
//        //        generator.scanTable("console", "dbo");
//        //        table.setInsertPolitic(SqlPolitic.FullCol);
//        //        table.apply();
//
////        // 生成数据
////        FakerEngine engine = new FakerEngine(dataDs, generator);
////        engine.start(1, 20);
////
////        // 监控信息
////        long t = System.currentTimeMillis();
////        while (!engine.isExitSignal()) {
////            if ((t + 1000) < System.currentTimeMillis()) {
////                t = System.currentTimeMillis();
////                System.out.println(engine.getMonitor());
////            }
////
////            if (engine.getMonitor().getSucceedInsert() > 100000) {
////                System.out.println(engine.getMonitor());
////                engine.shutdown();
////            }
////        }
//    }
//}
