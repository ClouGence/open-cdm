//package com.clougence.faker.seed;
//
//import java.io.Serializable;
//import java.math.BigDecimal;
//import java.util.function.Supplier;
//
//import org.junit.Test;
//
//import com.clougence.faker.seed.number.NumberSeedConfig;
//import com.clougence.faker.seed.number.NumberSeedFactory;
//import com.clougence.faker.seed.number.NumberType;
//
//public class NumberSeedFactoryTest {
//
//    @Test
//    public void buildBytes_1() {
//        NumberSeedFactory factory = new NumberSeedFactory();
//        NumberSeedConfig genConfig = new NumberSeedConfig();
//        genConfig.setNumberType(NumberType.Bool);
//        genConfig.addMinMax(BigDecimal.valueOf(2), BigDecimal.valueOf(64));
//        genConfig.setAllowNullable(false);
//
//        Supplier<Serializable> bytesSupplier = factory.createSeed(genConfig);
//
//        for (int i = 0; i < 10; i++) {
//            Serializable number = bytesSupplier.get();
//            System.out.println(number.getClass().getSimpleName() + " - " + number);
//        }
//    }
//
//    @Test
//    public void buildBytes_2() {
//        NumberSeedFactory factory = new NumberSeedFactory();
//        NumberSeedConfig genConfig = new NumberSeedConfig();
//        genConfig.setNumberType(NumberType.Byte);
//        genConfig.addMinMax(BigDecimal.valueOf(2), BigDecimal.valueOf(6400));
//        genConfig.setAllowNullable(false);
//
//        Supplier<Serializable> bytesSupplier = factory.createSeed(genConfig);
//
//        for (int i = 0; i < 10; i++) {
//            Serializable number = bytesSupplier.get();
//            System.out.println(number.getClass().getSimpleName() + " - " + number);
//        }
//    }
//
//    @Test
//    public void buildBytes_3() {
//        NumberSeedFactory factory = new NumberSeedFactory();
//        NumberSeedConfig genConfig = new NumberSeedConfig();
//        genConfig.setNumberType(NumberType.Short);
//        genConfig.addMinMax(BigDecimal.valueOf(202), BigDecimal.valueOf(6400));
//        genConfig.setAllowNullable(false);
//
//        Supplier<Serializable> bytesSupplier = factory.createSeed(genConfig);
//
//        for (int i = 0; i < 10; i++) {
//            Serializable number = bytesSupplier.get();
//            System.out.println(number.getClass().getSimpleName() + " - " + number);
//        }
//    }
//
//    @Test
//    public void buildBytes_4() {
//        NumberSeedFactory factory = new NumberSeedFactory();
//        NumberSeedConfig genConfig = new NumberSeedConfig();
//        genConfig.setNumberType(NumberType.Long);
//        genConfig.addMinMax(BigDecimal.valueOf(202), BigDecimal.valueOf(640000000));
//        genConfig.setAllowNullable(false);
//
//        Supplier<Serializable> bytesSupplier = factory.createSeed(genConfig);
//
//        for (int i = 0; i < 10; i++) {
//            Serializable number = bytesSupplier.get();
//            System.out.println(number.getClass().getSimpleName() + " - " + number);
//        }
//    }
//
//    @Test
//    public void buildBytes_5() {
//        NumberSeedFactory factory = new NumberSeedFactory();
//        NumberSeedConfig genConfig = new NumberSeedConfig();
//        genConfig.setNumberType(NumberType.Long);
//        genConfig.addMinMax(BigDecimal.valueOf(202), BigDecimal.valueOf(640000000));
//        genConfig.setAllowNullable(true);
//        genConfig.setNullableRatio(20.0f);
//
//        Supplier<Serializable> bytesSupplier = factory.createSeed(genConfig);
//
//        for (int i = 0; i < 10; i++) {
//            Serializable number = bytesSupplier.get();
//            if (number == null) {
//                System.out.println("@@NULL@@");
//            } else {
//                System.out.println(number.getClass().getSimpleName() + " - " + number);
//            }
//        }
//    }
//
//    @Test
//    public void buildBytes_6() {
//        NumberSeedFactory factory = new NumberSeedFactory();
//        NumberSeedConfig genConfig = new NumberSeedConfig();
//        genConfig.setNumberType(NumberType.Decimal);
//        genConfig.setPrecision(10);
//        genConfig.setScale(4);
//        genConfig.setAllowNullable(true);
//        genConfig.setNullableRatio(20.0f);
//
//        Supplier<Serializable> bytesSupplier = factory.createSeed(genConfig);
//
//        for (int i = 0; i < 10; i++) {
//            Serializable number = bytesSupplier.get();
//            if (number == null) {
//                System.out.println("@@NULL@@");
//            } else {
//                System.out.println(number.getClass().getSimpleName() + " - " + number);
//            }
//        }
//    }
//
//    @Test
//    public void buildBytes_7() {
//        NumberSeedFactory factory = new NumberSeedFactory();
//        NumberSeedConfig genConfig = new NumberSeedConfig();
//        genConfig.setNumberType(NumberType.Decimal);
//        genConfig.setPrecision(0);
//        genConfig.setScale(0);
//        genConfig.setAllowNullable(true);
//        genConfig.setNullableRatio(20.0f);
//
//        Supplier<Serializable> bytesSupplier = factory.createSeed(genConfig);
//
//        for (int i = 0; i < 10; i++) {
//            Serializable number = bytesSupplier.get();
//            if (number == null) {
//                System.out.println("@@NULL@@");
//            } else {
//                System.out.println(number.getClass().getSimpleName() + " - " + number);
//            }
//        }
//    }
//
//}
