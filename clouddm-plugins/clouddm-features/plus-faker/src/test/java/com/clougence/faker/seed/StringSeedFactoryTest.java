//package com.clougence.faker.seed;
//
//import java.io.Serializable;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashSet;
//import java.util.function.Supplier;
//
//import org.junit.Test;
//
//import com.clougence.faker.seed.string.CharacterSet;
//import com.clougence.faker.seed.string.Characters;
//import com.clougence.faker.seed.string.StringSeedConfig;
//import com.clougence.faker.seed.string.StringSeedFactory;
//
//import net.hasor.cobble.RandomUtils;
//
//public class StringSeedFactoryTest {
//
//    @Test
//    public void characters_1() {
//        Characters characters = CharacterSet.ASCII_FULL;
//
//        for (int i = 0; i < 10; i++) {
//            int nextInt = RandomUtils.nextInt(0, characters.getSize());
//            System.out.println(nextInt + " - " + characters.getChar(nextInt));
//        }
//    }
//
//    @Test
//    public void characters_2() {
//        Characters characters = CharacterSet.ASCII_CONTROL;
//
//        for (int i = 0; i < 10; i++) {
//            int nextInt = RandomUtils.nextInt(0, characters.getSize());
//            System.out.println(nextInt + " - " + characters.getChar(nextInt));
//        }
//    }
//
//    @Test
//    public void characters_3() {
//        Characters characters = CharacterSet.NUMBER_HEX;
//
//        for (int i = 0; i < 10; i++) {
//            int nextInt = RandomUtils.nextInt(0, characters.getSize());
//            System.out.println(nextInt + " - " + characters.getChar(nextInt));
//        }
//    }
//
//    @Test
//    public void characters_4() {
//        Characters characters = CharacterSet.LETTER_SMALL;
//
//        for (int i = 0; i < 10; i++) {
//            int nextInt = RandomUtils.nextInt(0, characters.getSize());
//            System.out.println(nextInt + " - " + characters.getChar(nextInt));
//        }
//    }
//
//    @Test
//    public void characters_5() {
//        Characters characters = CharacterSet.NUMBER_DEC;
//
//        for (int i = 0; i < 10; i++) {
//            int nextInt = RandomUtils.nextInt(0, characters.getSize());
//            System.out.println(nextInt + " - " + characters.getChar(nextInt));
//        }
//    }
//
//    @Test
//    public void characters_6() {
//        Characters characters = CharacterSet.LETTER_CAPITAL;
//
//        for (int i = 0; i < 10; i++) {
//            int nextInt = RandomUtils.nextInt(0, characters.getSize());
//            System.out.println(nextInt + " - " + characters.getChar(nextInt));
//        }
//    }
//
//    @Test
//    public void characters_8() {
//
//        Characters characters = CharacterSet.LATIN_BASIC;
//
//        for (int i = 0; i < 100; i++) {
//            int nextInt = RandomUtils.nextInt(0, characters.getSize());
//            System.out.print(characters.getChar(nextInt));
//        }
//    }
//
//    @Test
//    public void buildString_1() {
//        StringSeedFactory factory = new StringSeedFactory();
//        StringSeedConfig genConfig = new StringSeedConfig();
//        genConfig.setMinLength(2);
//        genConfig.setMaxLength(64);
//        genConfig.setAllowNullable(false);
//        genConfig.setCharacterSet(new HashSet<>(Arrays.asList(CharacterSet.LETTER_CAPITAL, CharacterSet.NUMBER_DEC)));
//
//        Supplier<Serializable> stringSupplier = factory.createSeed(genConfig);
//        for (int i = 0; i < 10; i++) {
//            System.out.println(stringSupplier.get());
//        }
//    }
//
//    @Test
//    public void buildString_2() {
//        StringSeedFactory factory = new StringSeedFactory();
//        StringSeedConfig genConfig = new StringSeedConfig();
//        genConfig.setMinLength(2);
//        genConfig.setMaxLength(64);
//        genConfig.setAllowNullable(false);
//        genConfig.setCharacterSet(new HashSet<>(Collections.singletonList(CharacterSet.NUMBER_HEX)));
//
//        Supplier<Serializable> stringSupplier = factory.createSeed(genConfig);
//        for (int i = 0; i < 10; i++) {
//            System.out.println(stringSupplier.get());
//        }
//    }
//
//    @Test
//    public void buildString_3() {
//        StringSeedFactory factory = new StringSeedFactory();
//        StringSeedConfig genConfig = new StringSeedConfig();
//        genConfig.setMinLength(2);
//        genConfig.setMaxLength(64);
//        genConfig.setAllowNullable(true);
//        genConfig.setAllowEmpty(true);
//        genConfig.setNullableRatio(20.0f);
//        genConfig.setCharacterSet(new HashSet<>(Arrays.asList(CharacterSet.NUMBER_HEX)));
//
//        Supplier<Serializable> stringSupplier = factory.createSeed(genConfig);
//        for (int i = 0; i < 10; i++) {
//            System.out.println(stringSupplier.get());
//        }
//    }
//
//    @Test
//    public void buildString_4() {
//        StringSeedFactory factory = new StringSeedFactory();
//        StringSeedConfig genConfig = new StringSeedConfig();
//        genConfig.setMinLength(2);
//        genConfig.setMaxLength(64);
//        genConfig.setAllowNullable(true);
//        genConfig.setAllowEmpty(true);
//        genConfig.setNullableRatio(20.0f);
//        genConfig.setCharacterSet(new HashSet<>(Arrays.asList(CharacterSet.CJK_UNIFIED_IDEOGRAPHS)));
//
//        Supplier<Serializable> stringSupplier = factory.createSeed(genConfig);
//        for (int i = 0; i < 10; i++) {
//            System.out.println(stringSupplier.get());
//        }
//    }
//}
