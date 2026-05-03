//package com.clougence.faker;
//
//import java.util.List;
//
//import org.junit.Test;
//
//import com.clougence.faker.generator.*;
//import com.clougence.faker.realdb.DsUtils;
//
//public class ConfigTest {
//
//    @Test
//    public void insertTest() throws Exception {
//        FakerFactory fakerFactory = new FakerFactory(DsUtils.dsMySql());
//        FakerRepository producer = new FakerRepository(fakerFactory);
//
//        FakerTable table = producer.addTable(null, null, "tb_user");
//        table.setInsertPolitic(SqlPolitic.RandomCol);
//
//        for (int j = 0; j < 10; j++) {
//            List<BoundQuery> boundQueries = producer.generator(OpsType.Insert);
//
//            for (BoundQuery query : boundQueries) {
//                System.out.println(query.getSqlString());
//            }
//        }
//    }
//
//    @Test
//    public void deleteTest() throws Exception {
//        FakerFactory fakerFactory = new FakerFactory(DsUtils.dsMySql());
//        FakerRepository producer = new FakerRepository(fakerFactory);
//
//        FakerTable table = producer.addTable(null, null, "tb_user");
//        table.setWherePolitic(SqlPolitic.RandomCol);
//
//        for (int j = 0; j < 10; j++) {
//            List<BoundQuery> boundQueries = producer.generator(OpsType.Delete);
//
//            for (BoundQuery query : boundQueries) {
//                System.out.println(query.getSqlString());
//            }
//        }
//    }
//
//    @Test
//    public void updateTest() throws Exception {
//        FakerFactory fakerFactory = new FakerFactory(DsUtils.dsMySql());
//        FakerRepository producer = new FakerRepository(fakerFactory);
//
//        FakerTable table = producer.addTable(null, null, "tb_user");
//        table.setWherePolitic(SqlPolitic.RandomCol);
//        table.setUpdateSetPolitic(SqlPolitic.RandomCol);
//
//        for (int j = 0; j < 10; j++) {
//            List<BoundQuery> boundQueries = producer.generator(OpsType.Delete);
//
//            for (BoundQuery query : boundQueries) {
//                System.out.println(query.getSqlString());
//            }
//        }
//    }
//}
