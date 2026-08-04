package com.clougence.clouddm.ds.template;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import com.clougence.clouddm.ds.TextCaseSupport;
import com.clougence.clouddm.ds.TextCaseSupport.CaseBlock;
import com.clougence.clouddm.ds.TextTestCase;
import com.clougence.clouddm.ds.dameng.definition.ui.template.DmCmdTemplateSpi;
import com.clougence.clouddm.ds.hana.definition.ui.template.HanaCmdTemplateSpi;
import com.clougence.clouddm.dsfamily.db2.definition.ui.template.Db2CmdTemplateSpi;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.template.MyCmdTemplateSpi;
import com.clougence.clouddm.dsfamily.oracle.definition.ui.template.OraCmdTemplateSpi;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.template.PgCmdTemplateSpi;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateOption;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateSpi;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class TemplateTextTest {

    private static final ObjectMapper                          OBJECT_MAPPER = new ObjectMapper();
    private static final Map<String, Supplier<CmdTemplateSpi>> SPI_FACTORIES = new LinkedHashMap<>();

    static {
        SPI_FACTORIES.put("dameng", DmCmdTemplateSpi::new);
        SPI_FACTORIES.put("db2", Db2CmdTemplateSpi::new);
        SPI_FACTORIES.put("hana", HanaCmdTemplateSpi::new);
        SPI_FACTORIES.put("mysql", MyCmdTemplateSpi::new);
        SPI_FACTORIES.put("oracle", OraCmdTemplateSpi::new);
        SPI_FACTORIES.put("postgres", PgCmdTemplateSpi::new);
    }

    @TestFactory
    public Stream<DynamicTest> templateScripts() {
        return dynamicTests();
    }

    public static Stream<DynamicTest> dynamicTests() {
        List<DynamicTest> tests = new ArrayList<>();
        for (Fixture fixture : fixtures()) {
            for (TemplateCase testCase : loadCases(fixture.resourcePath())) {
                String spiName = testCase.spiName == null ? fixture.spiName() : testCase.spiName;
                Supplier<CmdTemplateSpi> spiFactory = SPI_FACTORIES.get(spiName);
                if (spiFactory == null) {
                    throw new IllegalStateException("No template SPI mapping for: " + spiName);
                }
                tests.add(DynamicTest.dynamicTest(testCase.displayName(), () -> assertCase(fixture.resourcePath(), testCase, spiFactory.get())));
            }
        }
        return tests.stream();
    }

    static List<TemplateCase> loadCases(String resourcePath) {
        return TextCaseSupport.loadBlocks(resourcePath).stream().map(TemplateTextTest::parseOneCase).toList();
    }

    static void assertCase(String resourcePath, TemplateCase testCase, CmdTemplateSpi spi) {
        List<String> actual = execute(testCase, spi);
        Assert.assertEquals(testCase.caseId() + " output size", testCase.expected, actual);
    }

    private static TemplateCase parseOneCase(CaseBlock block) {
        TemplateCase testCase = new TemplateCase(block);
        String body = block.body();
        testCase.spiName = readOptionalLine(body, "spi:");
        testCase.type = readRequiredLine(body, "type:");
        String optionJson = section(body, "option:", "expect:").strip();
        try {
            testCase.option = OBJECT_MAPPER.readValue(optionJson, CmdTemplateOption.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("invalid option JSON: " + testCase.name(), e);
        }
        testCase.expected = parseExpected(section(body, "expect:", null));
        return testCase;
    }

    private static String readRequiredLine(String body, String prefix) {
        try {
            return TextCaseSupport.readRequiredLine(body, prefix);
        } catch (IllegalArgumentException e) {
            Assert.fail(e.getMessage());
            return null;
        }
    }

    private static String readOptionalLine(String body, String prefix) {
        return TextCaseSupport.readOptionalLine(body, prefix);
    }

    private static String section(String body, String start, String end) {
        try {
            return TextCaseSupport.section(body, start, end);
        } catch (IllegalArgumentException e) {
            Assert.fail(e.getMessage());
            return null;
        }
    }

    private static List<String> parseExpected(String expectedPart) {
        List<String> expected = new ArrayList<>();
        int index = 0;
        while (true) {
            int start = expectedPart.indexOf("<<<", index);
            if (start < 0) {
                break;
            }
            start += "<<<".length();
            int end = expectedPart.indexOf(">>>", start);
            Assert.assertTrue("unclosed expected output block", end >= 0);
            expected.add(trimBlock(expectedPart.substring(start, end)));
            index = end + ">>>".length();
        }
        Assert.assertFalse("empty expected output", expected.isEmpty());
        return expected;
    }

    private static String trimBlock(String text) {
        String result = text;
        if (result.startsWith("\r\n")) {
            result = result.substring(2);
        } else if (result.startsWith("\n")) {
            result = result.substring(1);
        }
        if (result.endsWith("\r\n")) {
            result = result.substring(0, result.length() - 2);
        } else if (result.endsWith("\n")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    private static List<String> execute(TemplateCase testCase, CmdTemplateSpi spi) {
        return switch (testCase.type) {
            case "createView" -> spi.getCreateView(testCase.option);
            case "alterView" -> spi.getAlterView(testCase.option);
            case "createFunction" -> spi.getCreateFunction(testCase.option);
            case "createProcedure" -> spi.getCreateProcedure(testCase.option);
            case "createTrigger" -> spi.getCreateTrigger(testCase.option);
            default -> throw new IllegalArgumentException("unsupported template type: " + testCase.type);
        };
    }

    private static List<Fixture> fixtures() {
        return TextCaseSupport.resourceFiles("template").stream().map(TemplateTextTest::toFixture).toList();
    }

    private static Fixture toFixture(String resourcePath) {
        String relative = resourcePath.substring("template/".length());
        String spiName = relative.substring(0, relative.indexOf('/'));
        if (!SPI_FACTORIES.containsKey(spiName)) {
            throw new IllegalStateException("No template SPI mapping for directory: " + spiName);
        }
        return new Fixture(resourcePath, spiName);
    }

    private record Fixture(String resourcePath, String spiName) {
    }

    static final class TemplateCase extends TextTestCase {

        TemplateCase(CaseBlock block){
            super(block);
        }

        String            spiName;
        String            type;
        CmdTemplateOption option;
        List<String>      expected;

        String displayName() {
            return caseId() + " [" + type + "] " + summarize(expected.get(0));
        }
    }

    private static String summarize(String output) {
        String text = output.replaceAll("\\s+", " ").strip();
        if (text.length() <= 120) {
            return text;
        }
        return text.substring(0, 117) + "...";
    }
}
