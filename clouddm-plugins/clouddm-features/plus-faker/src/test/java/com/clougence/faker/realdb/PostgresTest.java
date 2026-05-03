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
//public class PostgresTest {
//
//    @Test
//    public void workloadTest() throws Exception {
//        // 全局配置
//        FakerConfig fakerConfig = new FakerConfig();
//        fakerConfig.setDbType(DsType.PostgreSQL);
//        fakerConfig.setTransaction(false);
//        //        fakerConfig.setPolicy("extreme");
//        fakerConfig.setDataLoaderFactory(new PrecociousDataLoaderFactory());
//        fakerConfig.addIgnoreError("Duplicate");
//        fakerConfig.addIgnoreError("Data truncation: Incorrect datetime value");
//        fakerConfig.setWriteQps(10);
//        //        fakerConfig.setOpsRatio("I#10");//;U#30;D#10
//
//        // 生成器，配置表
//        DruidDataSource dataDs = DsUtils.dsPg();
//        FakerFactory factory = new FakerFactory(dataDs, fakerConfig);
//        FakerRepository generator = new FakerRepository(factory);
//        //
//        //
//        FakerTable table1 = generator.addTable("console", "public", "itd_product_op_log");
//
//        //        FakerTable table1 = generator.addTable("postgres", "public", "pg_huasheng1");
//        //        FakerTable table2 = generator.addTable("devtester", "public", "alert_config_detail");
//        //        FakerTable table3 = generator.addTable("devtester", "public", "alert_event_log");
//        //        FakerTable table4 = generator.addTable("devtester", "public", "alert_receiver");
//        //        FakerTable table5 = generator.addTable("devtester", "public", "column_default_datetime");
//        //        FakerTable table6 = generator.addTable("devtester", "public", "tb_postgre_types");
//        //        FakerTable table7 = generator.addTable("devtester", "public", "test_bit");
//
//        //        FakerTable table1 = generator.addTable("postgres", "public", "tb_postgre_types");
//        //        FakerTable table2 = generator.addTable("postgres", "pipi_test", "test_uuid_bool");
//        //        FakerTable table3 = generator.addTable("pggis", "pggis", "td_ccwjq_2020");
//        //        FakerTable table4 = generator.addTable("pggis", "pggis", "td_cjdcq_2020");
//        table1.setInsertPolitic(SqlPolitic.FullCol);
//        table1.apply();
//
////        // 生成数据
////        FakerEngine engine = new FakerEngine(dataDs, generator);
////        engine.start(30, 10);
////
////        // 监控信息
////        long t = System.currentTimeMillis();
////        while (!engine.isExitSignal()) {
////            if ((t + 1000) < System.currentTimeMillis()) {
////                t = System.currentTimeMillis();
////                System.out.println(engine.getMonitor());
////            }
////
////            if (engine.getMonitor().getSucceedInsert() > 10000) {
////                System.out.println(engine.getMonitor());
////                engine.shutdown();
////            }
////        }
//    }
//}
