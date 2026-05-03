package com.clougence.faker.realdb;


public class HanaTest {
//    @Test
//    public void workloadTest() throws Exception {
//        // 全局配置
//        FakerConfig fakerConfig = new FakerConfig();
//        fakerConfig.setTransaction(false);
//        fakerConfig.setDbType(DsType.Hana);
//        //fakerConfig.setPolicy("extreme");
//        fakerConfig.setDataLoaderFactory(new PrecociousDataLoaderFactory());
//        fakerConfig.addIgnoreError("Duplicate");
//        fakerConfig.addIgnoreError("Data truncation: Incorrect datetime value");
//        fakerConfig.addIgnoreError("Data truncation: Out of range value for column");
//        fakerConfig.setOpsRatio("INSERT#35;UPDATE#60;DELETE#5");
//        fakerConfig.setWriteQps(3);
//
//        // 生成器，配置表
//        DruidDataSource dataDs = DsUtils.dsHana();
//        FakerFactory factory = new FakerFactory(dataDs, fakerConfig);
//        FakerRepository generator = new FakerRepository(factory);
//
//        List<FakerTable> tables = new ArrayList<>();
//        tables.add(generator.addTable(null, "SYSTEM", "TEST_COLUMN"));
//        //        tables.add(generator.addTable("devtester", null, "1table"));
//
//        for (FakerTable tab : tables) {
//            tab.setInsertPolitic(SqlPolitic.RandomCol);
//            tab.apply();
//        }
//
//        // 生成数据
//        FakerEngine engine = new FakerEngine(dataDs, generator);
//        engine.start(3, 3);
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
//
//    @Test
//    public void workloadTest1() throws Exception {
//        // 全局配置
//        FakerConfig fakerConfig = new FakerConfig();
//        fakerConfig.setTransaction(false);
//        fakerConfig.setDbType(DsType.Hana);
//        //fakerConfig.setPolicy("extreme");
//        fakerConfig.setDataLoaderFactory(new PrecociousDataLoaderFactory());
//        fakerConfig.addIgnoreError("Duplicate");
//        fakerConfig.addIgnoreError("Data truncation: Incorrect datetime value");
//        fakerConfig.addIgnoreError("Data truncation: Out of range value for column");
//        fakerConfig.setOpsRatio("INSERT#35;UPDATE#60;DELETE#5");
//        fakerConfig.setWriteQps(3);
//
//        // 生成器，配置表
//        DruidDataSource dataDs = DsUtils.dsHana();
//        FakerFactory factory = new FakerFactory(dataDs, fakerConfig);
//        FakerRepository generator = new FakerRepository(factory);
//
//        List<FakerTable> tables = new ArrayList<>();
//        tables.add(generator.addTable(null, "SYSTEM", "TEST_COLUMN_1"));
//        //        tables.add(generator.addTable("devtester", null, "1table"));
//
//        for (FakerTable tab : tables) {
//            tab.setInsertPolitic(SqlPolitic.RandomCol);
//            tab.apply();
//        }
//
//        // 生成数据
//        FakerEngine engine = new FakerEngine(dataDs, generator);
//        engine.start(3, 3);
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
