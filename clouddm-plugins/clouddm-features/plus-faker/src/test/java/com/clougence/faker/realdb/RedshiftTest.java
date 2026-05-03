package com.clougence.faker.realdb;

import org.junit.Test;

/**
 * @author: (百里)周健鑫
 * @date: 2023/7/12 20:25
 * @description:
 */

public class RedshiftTest {

    @Test
    public void workloadTest() throws Exception {
//        LoggerFactory.useStdOutLogger();
        // 全局配置
//        FakerConfig fakerConfig = new FakerConfig();
//        fakerConfig.setTransaction(false);
////        fakerConfig.setPolicy("extreme");
//        fakerConfig.setMinBatchSizePerOps(50);
//        fakerConfig.setMaxBatchSizePerOps(100);
//        fakerConfig.setMinOpsCountPerTransaction(50);
//        fakerConfig.setMaxOpsCountPerTransaction(150);
//        fakerConfig.setDataLoaderFactory(new PrecociousDataLoaderFactory());
//        fakerConfig.setOpsRatio("I#100");
////        fakerConfig.setCustomTpcConf("redshift-widely.tpc");
//
//        DruidDataSource dataDs = DsUtils.dsRedshift();
//        FakerFactory factory = new FakerFactory(dataDs, fakerConfig);
//        FakerRepository generator = new FakerRepository(factory);
//
//        FakerTable table = generator.addTable("dev", "public", "tb_user");
//        table.setInsertPolitic(SqlPolitic.FullCol);
//        table.setUpdateSetPolitic(SqlPolitic.RandomCol);
//        table.apply();

//        // 生成数据
//        FakerEngine engine = new FakerEngine(dataDs, generator);
//        engine.start(40, 80);
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
    }
}
