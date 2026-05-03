package com.clougence.faker.realdb;


public class DmTest {

//    @Test
//    public void workloadTest() throws Exception {
//        // 全局配置
//        FakerConfig fakerConfig = new FakerConfig();
//        fakerConfig.setDbType(DsType.Dameng);
//        fakerConfig.setTransaction(false);
//        //        fakerConfig.setPolicy("extreme");
//        fakerConfig.setDataLoaderFactory(new PrecociousDataLoaderFactory());
//        fakerConfig.addIgnoreError("唯一性约束");
//        fakerConfig.addIgnoreError("Data truncation: Incorrect datetime value");
//        fakerConfig.addIgnoreError("Data truncation: Out of range value for column");
//        //fakerConfig.setOpsRatio("INSERT#30");
//        fakerConfig.setWriteQps(10);
//
//        // 生成器，配置表
//        DruidDataSource dataDs = DsUtils.dsDm8();
//        FakerFactory factory = new FakerFactory(dataDs, fakerConfig);
//        FakerRepository generator = new FakerRepository(factory);
//
//        List<FakerTable> tables = new ArrayList<>();
//        tables.add(generator.addTable(null, "TESTER", "TB_DM_TYPES_FOR_V8"));
//        //        tables.add(generator.addTable("devtester", null, "1table"));
//        //        tables.add(generator.addTable("devtester", null, "alert_config_detail"));
//        //        tables.add(generator.addTable("devtester", null, "alert_event_log"));
//        //        tables.add(generator.addTable("devtester", null, "alert_receiver"));
//        //        tables.add(generator.addTable("mode_test", null, "td_ccwjq_2020"));
//        //        tables.add(generator.addTable("devtester", null, "pk_table_time"));
//        //        tables.add(generator.addTable("devtester", null, "column_default_datetime"));
//        //        tables.add(generator.addTable("devtester", null, "test_bit"));
//
//        for (FakerTable tab : tables) {
//            tab.setInsertPolitic(SqlPolitic.FullCol);
//            tab.apply();
//        }
//
//        // 生成数据
//        FakerEngine engine = new FakerEngine(dataDs, generator);
//        engine.start(3, 10);
//
//        // 监控信息
//        long t = System.currentTimeMillis();
//        while (!engine.isExitSignal()) {
//            if ((t + 1000) < System.currentTimeMillis()) {
//                t = System.currentTimeMillis();
//                System.out.println(engine.getMonitor());
//            }
//
//            if (engine.getMonitor().getSucceedInsert() > 1000000) {
//                System.out.println(engine.getMonitor());
//                engine.shutdown();
//            }
//        }
//    }
}
