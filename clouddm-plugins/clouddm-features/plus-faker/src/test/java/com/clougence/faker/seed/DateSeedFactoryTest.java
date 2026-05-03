//package com.clougence.faker.seed;
//
//import com.clougence.faker.seed.date.DateSeedConfig;
//import com.clougence.faker.seed.date.DateSeedFactory;
//import com.clougence.faker.seed.date.GenType;
//import com.clougence.faker.seed.date.IntervalScope;
//import org.junit.Test;
//
//import java.io.Serializable;
//import java.util.function.Supplier;
//
//public class DateSeedFactoryTest {
//
//    @Test
//    public void buildTime_1() {
//        DateSeedFactory factory = new DateSeedFactory();
//        DateSeedConfig genConfig = new DateSeedConfig();
//        genConfig.setIntervalScope(IntervalScope.Day);
//        genConfig.setGenType(GenType.Interval);
//        genConfig.setMaxInterval(100);
//        genConfig.setAllowNullable(true);
//        genConfig.setNullableRatio(20f);
//
//        Supplier<Serializable> dateSupplier = factory.createSeed(genConfig);
//
//        for (int i = 0; i < 10; i++) {
//            System.out.println(dateSupplier.get());
//        }
//
//    }
//
//    @Test
//    public void buildTime_2() {
//        DateSeedFactory factory = new DateSeedFactory();
//        DateSeedConfig genConfig = new DateSeedConfig();
//        genConfig.setIntervalScope(IntervalScope.Year);
//        genConfig.setGenType(GenType.Interval);
//        genConfig.setMaxInterval(1000);
//
//        Supplier<Serializable> dateSupplier = factory.createSeed(genConfig);
//
//        for (int i = 0; i < 10; i++) {
//            System.out.println(dateSupplier.get());
//        }
//    }
//
//    @Test
//    public void buildTime_3() {
//        DateSeedFactory factory = new DateSeedFactory();
//        DateSeedConfig genConfig = new DateSeedConfig();
//        genConfig.setIntervalScope(IntervalScope.Milli);
//        genConfig.setGenType(GenType.Interval);
//        genConfig.setMaxInterval(500);
//
//        Supplier<Serializable> dateSupplier = factory.createSeed(genConfig);
//
//        for (int i = 0; i < 10; i++) {
//            System.out.println(dateSupplier.get());
//        }
//    }
//
//    @Test
//    public void buildTime_4() {
//        DateSeedFactory factory = new DateSeedFactory();
//        DateSeedConfig genConfig = new DateSeedConfig();
//        genConfig.setGenType(GenType.Fixed);
//        genConfig.setRangeForm("2021-02-13 22:11:12");
//        genConfig.setRangeTo("1988-01-13 14:32:57");
//
//        Supplier<Serializable> dateSupplier = factory.createSeed(genConfig);
//
//        for (int i = 0; i < 10; i++) {
//            System.out.println(dateSupplier.get());
//        }
//    }
//
//    @Test
//    public void buildTime_5() {
//        DateSeedFactory factory = new DateSeedFactory();
//        DateSeedConfig genConfig = new DateSeedConfig();
//        genConfig.setGenType(GenType.Fixed);
//        genConfig.setRangeTo("1988-01-13 14:32:57");
//
//        Supplier<Serializable> dateSupplier = factory.createSeed(genConfig);
//
//        for (int i = 0; i < 10; i++) {
//            System.out.println(dateSupplier.get());
//        }
//    }
//
//    @Test
//    public void buildTime_6() throws InterruptedException {
//        DateSeedFactory factory = new DateSeedFactory();
//        DateSeedConfig genConfig = new DateSeedConfig();
//        genConfig.setGenType(GenType.SysData);
//
//        Supplier<Serializable> dateSupplier = factory.createSeed(genConfig);
//
//        for (int i = 0; i < 10; i++) {
//            Thread.sleep(1);
//            System.out.println(dateSupplier.get());
//        }
//    }
//
//    @Test
//    public void buildTime_7() throws InterruptedException {
//        DateSeedFactory factory = new DateSeedFactory();
//        DateSeedConfig genConfig = new DateSeedConfig();
//        genConfig.setGenType(GenType.Random);
//        genConfig.setRangeForm("1988-01-13 14:32:57");
//        genConfig.setRangeTo("2021-02-13 22:11:12");
//
//        Supplier<Serializable> dateSupplier = factory.createSeed(genConfig);
//
//        for (int i = 0; i < 10; i++) {
//            Thread.sleep(1);
//            System.out.println(dateSupplier.get());
//        }
//    }
//
//    @Test
//    public void buildTime_8() throws InterruptedException {
//        DateSeedFactory factory = new DateSeedFactory();
//        DateSeedConfig genConfig = new DateSeedConfig();
//        genConfig.setGenType(GenType.Random);
//
//        Supplier<Serializable> dateSupplier = factory.createSeed(genConfig);
//
//        for (int i = 0; i < 10; i++) {
//            Thread.sleep(1);
//            System.out.println(dateSupplier.get());
//        }
//    }
//}
