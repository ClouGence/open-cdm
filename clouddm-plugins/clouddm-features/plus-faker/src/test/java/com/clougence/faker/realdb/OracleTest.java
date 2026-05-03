package com.clougence.faker.realdb;

public class OracleTest {

//    @Test
//    public void workloadTest() throws Exception {
//        // 全局配置
//        FakerConfig fakerConfig = new FakerConfig();
//        fakerConfig.setDbType(DsType.Oracle);
//        fakerConfig.setTransaction(true);
////        fakerConfig.setPolicy("extreme");
//        fakerConfig.setMinBatchSizePerOps(5);
//        fakerConfig.setMaxBatchSizePerOps(10);
//        fakerConfig.setMinOpsCountPerTransaction(5);
//        fakerConfig.setMaxOpsCountPerTransaction(15);
//        fakerConfig.setDataLoaderFactory(new PrecociousDataLoaderFactory());
//        fakerConfig.addIgnoreError("ORA-00001");
//        fakerConfig.setOpsRatio("I#10;U#10");
//        fakerConfig.setCustomTpcConf("oracle-varcharmax.tpc");
//        //        fakerConfig.addIgnoreError("restarting");
//        //        fakerConfig.addIgnoreError("deadlocked");
//        //        fakerConfig.addIgnoreError("was deadlocked on lock");
//        //        fakerConfig.setOpsRatio("D#30");
//
//        // 生成器，配置表
//        DruidDataSource dataDs = DsUtils.dsOracle();
//        FakerFactory factory = new FakerFactory(dataDs, fakerConfig);
//        FakerRepository generator = new FakerRepository(factory);
//        // FakerTable table = generator.addTable("console", "dbo", "tb_sqlserver_types");
//        // FakerTable table = generator.addTable("console", "dbo", "stock");
////        FakerTable table = generator.addTable(null, "SCOTT", "TB_ORACLE_TYPES");
////        FakerTable table = generator.addTable("ORCL", "JUNYU_ORCL", "WORKER_STATS");
//        FakerTable table2 = generator.addTable("ORCL", "JUNYU_ORCL", "WORKER_STATS_DATE");
////        FakerTable table2 = generator.addTable("ORCL", "JUNYU_ORCL", "KBS_QUESTION");
////        FakerTable table3 = generator.addTable("ORCL", "JUNYU_ORCL", "MULTI_PK");
//        FakerTable table = generator.addTable(null, "CANAL_TEST_CASE", "TB_ORACLE_TYPES");
//        table.setInsertPolitic(SqlPolitic.FullCol);
//        table.setUpdateSetPolitic(SqlPolitic.RandomCol);
//        table.apply();
//        table2.setInsertPolitic(SqlPolitic.FullCol);
//        table2.setUpdateSetPolitic(SqlPolitic.RandomCol);
//        table2.apply();
////        table3.setInsertPolitic(SqlPolitic.FullCol);
////        table3.setUpdateSetPolitic(SqlPolitic.RandomCol);
////        table3.apply();
//
////        table2.setInsertPolitic(SqlPolitic.FullCol);
////        table2.setUpdateSetPolitic(SqlPolitic.RandomCol);
////        table2.apply();
//
//        // 生成数据
//        FakerEngine engine = new FakerEngine(dataDs, generator);
//        engine.start(8, 16);
//
//        // 监控信息
//        long t = System.currentTimeMillis();
//        while (!engine.isExitSignal()) {
//            if ((t + 1000) < System.currentTimeMillis()) {
//                t = System.currentTimeMillis();
//                System.out.println(engine.getMonitor());
//            }
//
//            if (engine.getMonitor().getSucceedInsert() > 200000) {
//                System.out.println(engine.getMonitor());
//                engine.shutdown();
//            }
//        }
//    }
}
