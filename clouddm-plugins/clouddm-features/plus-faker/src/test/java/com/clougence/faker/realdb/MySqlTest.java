package com.clougence.faker.realdb;

public class MySqlTest {

//    @Test
//    public void workloadTest() throws Exception {
//        // 全局配置
//        FakerConfig fakerConfig = new FakerConfig();
//        fakerConfig.setDbType(DsType.MySQL);
//        fakerConfig.setTransaction(false);
//        //fakerConfig.setPolicy("extreme");
//        fakerConfig.setDataLoaderFactory(new PrecociousDataLoaderFactory());
//        fakerConfig.addIgnoreError("Duplicate");
//        fakerConfig.addIgnoreError("Data truncation: Incorrect datetime value");
//        fakerConfig.addIgnoreError("Data truncation: Out of range value for column");
//        //        fakerConfig.setOpsRatio("INSERT#30");
//        fakerConfig.setWriteQps(10);
//
//        // 生成器，配置表
//        DruidDataSource dataDs = DsUtils.dsMySql();
//        FakerFactory factory = new FakerFactory(dataDs, fakerConfig);
//        FakerRepository generator = new FakerRepository(factory);
//
//        List<FakerTable> tables = new ArrayList<>();
//        //        tables.add(generator.addTable(null, "console", "alert_config_detail"));
//        //        tables.add(generator.addTable(null, "console", "alert_event_log"));
//        //        tables.add(generator.addTable(null, "console", "alert_receiver"));
//        tables.add(generator.addTable(null, null, "test_table"));
//        //
//        //        tables.add(generator.addTable(null, "devtester", "1table"));
//        //        tables.add(generator.addTable(null, "devtester", "alert_config_detail"));
//        //        tables.add(generator.addTable(null, "devtester", "alert_event_log"));
//        //        tables.add(generator.addTable(null, "devtester", "alert_receiver"));
//        //        tables.add(generator.addTable(null, "mode_test", "td_ccwjq_2020"));
//        //        tables.add(generator.addTable(null, "devtester", "pk_table_time"));
//        //        tables.add(generator.addTable(null, "devtester", "column_default_datetime"));
//        //        tables.add(generator.addTable(null, "devtester", "test_bit"));
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
//            if (engine.getMonitor().getSucceedInsert() > 100) {
//                System.out.println(engine.getMonitor());
//                engine.shutdown();
//            }
//        }
//    }
}
