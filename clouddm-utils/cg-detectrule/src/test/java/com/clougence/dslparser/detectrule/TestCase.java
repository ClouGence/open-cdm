package com.clougence.dslparser.detectrule;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.util.*;

import org.junit.Test;

import com.clougence.detectrule.lang.LangObject;
import com.clougence.detectrule.lang.reflect.ReflectHelper;
import com.clougence.detectrule.lang.reflect.Type;
import com.clougence.detectrule.parser.DetectRuleDslProvider;
import com.clougence.detectrule.parser.DetectRulesFeature;
import com.clougence.detectrule.runtime.DefaultDataTimeValueParser;
import com.clougence.detectrule.runtime.EngineOption;
import com.clougence.detectrule.runtime.v1.DetectRuleEngineV1;
import com.clougence.dslparser.detectrule.test.TestMapDomain;
import com.clougence.dslparser.detectrule.test.TestRuleDomain;
import com.clougence.dslparser.detectrule.test.func.FuncGroupUtils;
import com.clougence.dslparser.detectrule.test.func.FuncStringUtils;
import com.clougence.dslparser.detectrule.test.rule.RdbColumnDomain;
import com.clougence.dslparser.detectrule.test.rule.SecQueryType;
import com.clougence.dslparser.detectrule.test.rule.SqlDdlKind;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.ast.StatementSet;
import com.clougence.utils.CollectionUtils;

public class TestCase {

    static {
        DslHelper.register("DetectRule", new DetectRuleDslProvider());
    }

    public static Object runScript(String script, Object domainData, Type domainType) {
        return runScript(script, domainData, domainType, null);
    }

    public static Object runScript(String script, Object domainData, Type domainType, Map<String, String> vars) {
        StatementSet statements = DslHelper.parserDsl("DetectRule", script);

        EngineOption option = new EngineOption();
        option.setDataTimeValueParser(new DefaultDataTimeValueParser());
        DetectRuleEngineV1 visitor = new DetectRuleEngineV1(domainData, domainType, option);
        visitor.putVariables(vars);

        statements.accept(visitor);
        LangObject returnData = visitor.returnData(null);
        return returnData.unwrap();
    }

    public static Object runScript(String script, Object domainData, Type domainType, Map<String, String> vars, DetectRulesFeature[] features) throws IOException {
        StatementSet statements = DslHelper.parserDsl("DetectRule", script);

        EngineOption option = new EngineOption();
        option.setDataTimeValueParser(new DefaultDataTimeValueParser());
        option.setFeatures(features);
        DetectRuleEngineV1 visitor = new DetectRuleEngineV1(domainData, domainType, option);
        visitor.putVariables(vars);

        statements.accept(visitor);
        LangObject returnData = visitor.returnData(null);
        return returnData.unwrap();
    }

    private TestRuleDomain createDomain() {
        TestRuleDomain domainData = new TestRuleDomain();
        domainData.setFun(new FuncGroupUtils());
        domainData.getFun().setStr(new FuncStringUtils());
        domainData.setDomain(new RdbColumnDomain());
        domainData.getDomain().setCatalog("abc");
        domainData.getDomain().setTable("tester");
        domainData.getDomain().setColumn("id");
        domainData.getDomain().setPrimary(true);

        domainData.getDomain().setDdlKind(SqlDdlKind.Create);
        domainData.getDomain().setSqlType(SecQueryType.INSERT);

        domainData.getStringList().add("a");
        domainData.getStringList().add("b");
        domainData.getStringList().add("c");
        domainData.getStringList().add(null);

        domainData.setStringArray(new String[] { "a", "b", "c", null });

        domainData.getDomain().setOptions(new HashMap<>());
        domainData.getDomain().getOptions().put("a", "aa");
        domainData.getDomain().getOptions().put("b", "bb");

        return domainData;
    }

    @Test
    public void resolveDomain() {
        Type domain = ReflectHelper.resolveDomain(TestRuleDomain.class);
        System.out.println(domain);
    }

    @Test
    public void primaryTypeTest() throws IOException {
        TestRuleDomain domainData = this.createDomain();
        Type domainType = ReflectHelper.resolveDomain(TestRuleDomain.class);
        Object object;

        // boolean
        object = runScript("return true", domainData, domainType);
        assert object instanceof Boolean && (boolean) object;
        object = runScript("return false", domainData, domainType);
        assert object instanceof Boolean && !((boolean) object);

        // Xx
        object = runScript("return 0b0", domainData, domainType);
        assert object instanceof Long && ((long) object == 0L);
        object = runScript("return 0B1", domainData, domainType);
        assert object instanceof Long && ((long) object == 1L);
        object = runScript("return 0b111", domainData, domainType);
        assert object instanceof Long && ((long) object == 7L);
        object = runScript("return 0b111111111111111111111111111111111111111111111111111111111111111", domainData, domainType);
        assert object instanceof Long && ((long) object == 9223372036854775807L);
        object = runScript("return 0b1111111111111111111111111111111111111111111111111111111111111111", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString().equals("18446744073709551615");
        object = runScript("return 0o1234567", domainData, domainType);
        assert object instanceof Long && ((long) object == 342391L);
        object = runScript("return 0o777777777777777777777", domainData, domainType);
        assert object instanceof Long && ((long) object == 9223372036854775807L);
        object = runScript("return 0o1777777777777777777777", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString().equals("18446744073709551615");
        object = runScript("return 0x1234567", domainData, domainType);
        assert object instanceof Long && ((long) object == 19088743L);
        object = runScript("return 0xFFFFFFFFFFFFFFF", domainData, domainType);
        assert object instanceof Long && ((long) object == 1152921504606846975L);
        object = runScript("return 0x7FFFFFFFFFFFFFFF", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString().equals("9223372036854775807");
        object = runScript("return 0o0000000000000000000000000000000000000000000000000000000001234567", domainData, domainType);
        assert object instanceof Long && ((long) object == 342391L);
        object = runScript("return 0o1234567123456712345671234567123456712345671234567123456712345671", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString().equals("1024829943232881663461763018686540030677667742086995758009");
        object = runScript("return 0o000000000000000000000000000000000000000000000000000000000", domainData, domainType);
        assert object instanceof Long && ((long) object == 0L);

        // number
        object = runScript("return 6", domainData, domainType);
        assert object instanceof Long && ((long) object == 6L);
        object = runScript("return 7", domainData, domainType);
        assert object instanceof Long && ((long) object == 7L);
        object = runScript("return 11", domainData, domainType);
        assert object instanceof Long && ((long) object == 11L);
        object = runScript("return 15", domainData, domainType);
        assert object instanceof Long && ((long) object == 15L);
        object = runScript("return 255", domainData, domainType);
        assert object instanceof Long && ((long) object == 255L);
        object = runScript("return 22141", domainData, domainType);
        assert object instanceof Long && ((long) object == 22141L);
        object = runScript("return 16777215", domainData, domainType);
        assert object instanceof Long && ((long) object == 16777215L);
        object = runScript("return 1248650613", domainData, domainType);
        assert object instanceof Long && ((long) object == 1248650613L);
        object = runScript("return 4294967295", domainData, domainType);
        assert object instanceof Long && ((long) object == 4294967295L);
        object = runScript("return 3825172218129898363", domainData, domainType);
        assert object instanceof Long && ((long) object == 3825172218129898363L);
        object = runScript("return 18446744073709551615", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString().equals("18446744073709551615");
        object = runScript("return 28446744073709551615", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString().equals("28446744073709551615");
        object = runScript("return 000000000000000000000000000000000000000000000000000000000000", domainData, domainType);
        assert object instanceof Long && ((long) object == 0L);
        object = runScript("return 123456789012345678901234567890123456789012345678901234567890", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString().equals("123456789012345678901234567890123456789012345678901234567890");
        object = runScript("return +6", domainData, domainType);
        assert object instanceof Long && ((long) object == 6L);
        object = runScript("return +7", domainData, domainType);
        assert object instanceof Long && ((long) object == 7L);
        object = runScript("return +11", domainData, domainType);
        assert object instanceof Long && ((long) object == 11L);
        object = runScript("return +15", domainData, domainType);
        assert object instanceof Long && ((long) object == 15L);
        object = runScript("return +255", domainData, domainType);
        assert object instanceof Long && ((long) object == 255L);
        object = runScript("return +22141", domainData, domainType);
        assert object instanceof Long && ((long) object == 22141L);
        object = runScript("return +16777215", domainData, domainType);
        assert object instanceof Long && ((long) object == 16777215L);
        object = runScript("return +1248650613", domainData, domainType);
        assert object instanceof Long && ((long) object == 1248650613L);
        object = runScript("return +4294967295", domainData, domainType);
        assert object instanceof Long && ((long) object == 4294967295L);
        object = runScript("return +3825172218129898363", domainData, domainType);
        assert object instanceof Long && ((long) object == 3825172218129898363L);
        object = runScript("return +18446744073709551615", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString().equals("18446744073709551615");
        object = runScript("return +28446744073709551615", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString().equals("28446744073709551615");
        object = runScript("return +000000000000000000000000000000000000000000000000000000000000", domainData, domainType);
        assert object instanceof Long && ((long) object == 0L);
        object = runScript("return +123456789012345678901234567890123456789012345678901234567890", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString().equals("123456789012345678901234567890123456789012345678901234567890");
        object = runScript("return -6", domainData, domainType);
        assert object instanceof Long && ((long) object == -6L);
        object = runScript("return -7", domainData, domainType);
        assert object instanceof Long && ((long) object == -7L);
        object = runScript("return -11", domainData, domainType);
        assert object instanceof Long && ((long) object == -11L);
        object = runScript("return -15", domainData, domainType);
        assert object instanceof Long && ((long) object == -15L);
        object = runScript("return -255", domainData, domainType);
        assert object instanceof Long && ((long) object == -255L);
        object = runScript("return -22141", domainData, domainType);
        assert object instanceof Long && ((long) object == -22141L);
        object = runScript("return -16777215", domainData, domainType);
        assert object instanceof Long && ((long) object == -16777215L);
        object = runScript("return -1248650613", domainData, domainType);
        assert object instanceof Long && ((long) object == -1248650613L);
        object = runScript("return -4294967295", domainData, domainType);
        assert object instanceof Long && ((long) object == -4294967295L);
        object = runScript("return -3825172218129898363", domainData, domainType);
        assert object instanceof Long && ((long) object == -3825172218129898363L);
        object = runScript("return -18446744073709551615", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString().equals("-18446744073709551615");
        object = runScript("return -28446744073709551615", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString().equals("-28446744073709551615");
        object = runScript("return -000000000000000000000000000000000000000000000000000000000000", domainData, domainType);
        assert object instanceof Long && ((long) object == 0L);
        object = runScript("return -123456789012345678901234567890123456789012345678901234567890", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString().equals("-123456789012345678901234567890123456789012345678901234567890");

        object = runScript("return 1.", domainData, domainType);
        assert object instanceof Double && ((Double) object == 1d);
        object = runScript("return .1", domainData, domainType);
        assert object instanceof Double && ((Double) object == 0.1d);
        object = runScript("return 0.1", domainData, domainType);
        assert object instanceof Double && ((Double) object == 0.1d);
        object = runScript("return 1230000000.0", domainData, domainType);
        assert object instanceof Double && ((Double) object == 1230000000d);
        object = runScript("return 123456789012345678901234567890123456789012345678901234567890.123456789012345678901234567890123456789012345678901234567890", domainData, domainType);
        assert object instanceof Double && object.toString().equals("1.2345678901234567E59");
        object = runScript("return +1.", domainData, domainType);
        assert object instanceof Double && ((Double) object == 1d);
        object = runScript("return +.1", domainData, domainType);
        assert object instanceof Double && ((Double) object == 0.1d);
        object = runScript("return +0.1", domainData, domainType);
        assert object instanceof Double && ((Double) object == 0.1d);
        object = runScript("return +1230000000.0", domainData, domainType);
        assert object instanceof Double && ((Double) object == 1230000000d);
        object = runScript("return +123456789012345678901234567890123456789012345678901234567890.123456789012345678901234567890123456789012345678901234567890", domainData, domainType);
        assert object instanceof Double && object.toString().equals("1.2345678901234567E59");
        object = runScript("return -1.", domainData, domainType);
        assert object instanceof Double && ((Double) object == -1d);
        object = runScript("return -.1", domainData, domainType);
        assert object instanceof Double && ((Double) object == -0.1d);
        object = runScript("return -0.1", domainData, domainType);
        assert object instanceof Double && ((Double) object == -0.1d);
        object = runScript("return -1230000000.0", domainData, domainType);
        assert object instanceof Double && ((Double) object == -1230000000d);
        object = runScript("return -123456789012345678901234567890123456789012345678901234567890.123456789012345678901234567890123456789012345678901234567890", domainData, domainType);
        assert object instanceof Double && object.toString().equals("-1.2345678901234567E59");

        object = runScript("return 1.E1", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString().equals("10");
        object = runScript("return .1E12", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString().equals("100000000000");
        object = runScript("return 0.1E11", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString().equals("10000000000");
        object = runScript("return 1230000000.0E234", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString()
            .equals("1230000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        object = runScript("return 123456789012345678901234567890123456789012345678901234567890.123456789012345678901234567890123456789012345678901234567890E5", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString()
            .equals("12345678901234567890123456789012345678901234567890123456789012345.6789012345678901234567890123456789012345678901234567890");

        object = runScript("return 1.E1", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString().equals("10");
        object = runScript("return .1E12", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString().equals("100000000000");
        object = runScript("return 0.1E11", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString().equals("10000000000");
        object = runScript("return 1230000000.0E234", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString()
            .equals("1230000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        object = runScript("return 123456789012345678901234567890123456789012345678901234567890.123456789012345678901234567890123456789012345678901234567890E5", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString()
            .equals("12345678901234567890123456789012345678901234567890123456789012345.6789012345678901234567890123456789012345678901234567890");
        object = runScript("return +1.E1", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString().equals("10");
        object = runScript("return +.1E12", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString().equals("100000000000");
        object = runScript("return +0.1E11", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString().equals("10000000000");
        object = runScript("return +1230000000.0E234", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString()
            .equals("1230000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        object = runScript("return +123456789012345678901234567890123456789012345678901234567890.123456789012345678901234567890123456789012345678901234567890E5", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString()
            .equals("12345678901234567890123456789012345678901234567890123456789012345.6789012345678901234567890123456789012345678901234567890");
        object = runScript("return -1.E1", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString().equals("-10");
        object = runScript("return -.1E12", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString().equals("-100000000000");
        object = runScript("return -0.1E11", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString().equals("-10000000000");
        object = runScript("return -1230000000.0E234", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString()
            .equals("-1230000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        object = runScript("return -123456789012345678901234567890123456789012345678901234567890.123456789012345678901234567890123456789012345678901234567890E5", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString()
            .equals("-12345678901234567890123456789012345678901234567890123456789012345.6789012345678901234567890123456789012345678901234567890");

        // null
        object = runScript("return null", domainData, domainType);
        assert object == null;

        // string
        object = runScript("return 'abcdefg...'", domainData, domainType);
        assert object instanceof String && object.equals("abcdefg...");
        object = runScript("return ''", domainData, domainType);
        assert object instanceof String && object.equals("");
        object = runScript("return \"\"", domainData, domainType);
        assert object instanceof String && object.equals("");
        object = runScript("return '\"'", domainData, domainType);
        assert object instanceof String && object.equals("\"");
        object = runScript("return 'abc\\u0041123'", domainData, domainType);
        assert object instanceof String && object.equals("abcA123");
        object = runScript("return '\\u0041'", domainData, domainType);
        assert object instanceof String && object.equals("A");
        object = runScript("return '\\u0041BC'", domainData, domainType);
        assert object instanceof String && object.equals("ABC");
        object = runScript("return 'CB\\u0041'", domainData, domainType);
        assert object instanceof String && object.equals("CBA");
        object = runScript("return '\\u0041\\u0042\\u0043'", domainData, domainType);
        assert object instanceof String && object.equals("ABC");
        object = runScript("return \"\\'\\'\"", domainData, domainType);
        assert object instanceof String && object.equals("''");
        object = runScript("return '\\\"\\\"'", domainData, domainType);
        assert object instanceof String && object.equals("\"\"");
        object = runScript("return '\\\\'", domainData, domainType);
        assert object instanceof String && object.equals("\\");

        // datetime
        object = runScript("return T'2023-05-23'", domainData, domainType);
        assert object instanceof LocalDate && object.toString().equals("2023-05-23");
        object = runScript("return T'12:23:56'", domainData, domainType);
        assert object instanceof LocalTime && object.toString().equals("12:23:56");
        object = runScript("return T'2023-05-23T12:23:56'", domainData, domainType);
        assert object instanceof LocalDateTime && object.toString().equals("2023-05-23T12:23:56");
        object = runScript("return T'2023-05-23 12:23:56'", domainData, domainType);
        assert object instanceof LocalDateTime && object.toString().equals("2023-05-23T12:23:56");
        object = runScript("return T'2023-05-23T12:23:56.123456789'", domainData, domainType);
        assert object instanceof LocalDateTime && object.toString().equals("2023-05-23T12:23:56.123456789");
        object = runScript("return T'12:23:56+03:30'", domainData, domainType);
        assert object instanceof OffsetTime && object.toString().equals("12:23:56+03:30");
        object = runScript("return T'12:23:56.123456789+05:34'", domainData, domainType);
        assert object instanceof OffsetTime && object.toString().equals("12:23:56.123456789+05:34");
        object = runScript("return T('yyyyMMdd')'20230523'", domainData, domainType);
        assert object instanceof LocalDate && object.toString().equals("2023-05-23");
        object = runScript("return T('yyyy-MM-dd HH:mm:ss.SSSSS')'2023-05-23 12:23:56.12345'", domainData, domainType);
        assert object instanceof LocalDateTime && object.toString().equals("2023-05-23T12:23:56.123450");
        object = runScript("return T('yyyy-MM-dd HH:mm:ss.SSSS')'2023-05-23 12:23:56.1234'", domainData, domainType);
        assert object instanceof LocalDateTime && object.toString().equals("2023-05-23T12:23:56.123400");

        // arrays
        object = runScript("return [1,2,3]", domainData, domainType);
        assert object instanceof List && ((List<?>) object).get(0).equals(1L) && ((List<?>) object).get(1).equals(2L) && ((List<?>) object).get(2).equals(3L);
        object = runScript("return [1,2.,'3']", domainData, domainType);
        assert object instanceof List && ((List<?>) object).get(0).equals(1L) && ((List<?>) object).get(1).equals(2D) && ((List<?>) object).get(2).equals("3");
        object = runScript("return [[],[]]", domainData, domainType);
        assert object instanceof List && ((List<?>) object).get(0) instanceof List && ((List<?>) object).get(1) instanceof List;
        object = runScript("return ['1',\"2\",3,T'2024-05-06',T(\"yyyy-MM-dd\")'2024-05-06',123,123e5,+23.,.123,-.123,true,null]", domainData, domainType);
        assert object instanceof List && ((List<?>) object).size() == 12;

        // object
        object = runScript("return {}", domainData, domainType);
        assert object instanceof Map && ((Map<?, ?>) object).isEmpty();
        object = runScript("return {'k1':1,'k2':2}", domainData, domainType);
        assert object instanceof Map && ((Map<?, ?>) object).get("k1").equals(1L) && ((Map<?, ?>) object).get("k2").equals(2L);
        object = runScript("return {'k1':{},'k2':[]}", domainData, domainType);
        assert object instanceof Map && ((Map<?, ?>) object).get("k1") instanceof Map && ((Map<?, ?>) object).get("k2") instanceof List;
        object = runScript("return {'k1':'1','k2':\"2\",'k3':3,'k4':T'2024-05-06','k5':T('yyyyMMdd')'20230523','k6':123,'k7':123e5,'k8':+23.,'k9':.123,'k10':-.123,'k11':true,'k12':null}", domainData, domainType);
        assert object instanceof Map && ((Map<?, ?>) object).size() == 12;
        object = runScript("return {"
                           + "'ka':{'k1':'1','k2':\"2\",'k3':3,'k4':T'2024-05-06','k5':T('yyyyMMdd')'20230523','k6':123,'k7':123e5,'k8':+23.,'k9':.123,'k10':-.123,'k11':true,'k12':null},\n"
                           + "'kb':{'k1':'1','k2':\"2\",'k3':3,'k4':T'2024-05-06','k5':T('yyyyMMdd')'20230523','k6':123,'k7':123e5,'k8':+23.,'k9':.123,'k10':-.123,'k11':true,'k12':null}"
                           + "}", domainData, domainType);
        assert object instanceof Map && ((Map<?, ?>) object).size() == 2;
    }

    @Test
    public void castTest() throws IOException {
        TestRuleDomain domainData = this.createDomain();
        Type domainType = ReflectHelper.resolveDomain(TestRuleDomain.class);
        Object object;

        // boolean
        object = runScript("return cast(true as bool)", domainData, domainType);
        assert object instanceof Boolean && (boolean) object;
        object = runScript("return cast(true as integer)", domainData, domainType);
        assert object instanceof Long && object.equals(1L);
        object = runScript("return cast(true as int)", domainData, domainType);
        assert object instanceof Long && object.equals(1L);
        object = runScript("return cast(true as float)", domainData, domainType);
        assert object instanceof Double && object.equals(1.0d);
        object = runScript("return cast(true as decimal)", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString().equals("1");
        object = runScript("return cast(true as string)", domainData, domainType);
        assert object instanceof String && object.equals("true");
        object = runScript("return cast(true as string)", domainData, domainType);
        assert object instanceof String && object.equals("true");

        // int
        object = runScript("return cast(1 as bool)", domainData, domainType);
        assert object instanceof Boolean && (boolean) object;
        object = runScript("return cast(1 as integer)", domainData, domainType);
        assert object instanceof Long && object.equals(1L);
        object = runScript("return cast(1 as int)", domainData, domainType);
        assert object instanceof Long && object.equals(1L);
        object = runScript("return cast(1 as float)", domainData, domainType);
        assert object instanceof Double && object.equals(1.0d);
        object = runScript("return cast(1 as decimal)", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString().equals("1");
        object = runScript("return cast(1 as string)", domainData, domainType);
        assert object instanceof String && object.equals("1");

        // float
        object = runScript("return cast(1.0 as bool)", domainData, domainType);
        assert object instanceof Boolean && (boolean) object;
        object = runScript("return cast(1.0 as integer)", domainData, domainType);
        assert object instanceof Long && object.equals(1L);
        object = runScript("return cast(1.0 as int)", domainData, domainType);
        assert object instanceof Long && object.equals(1L);
        object = runScript("return cast(1.0 as float)", domainData, domainType);
        assert object instanceof Double && object.equals(1.0d);
        object = runScript("return cast(1.0 as decimal)", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString().equals("1");
        object = runScript("return cast(1.0 as string)", domainData, domainType);
        assert object instanceof String && object.equals("1.0");

        // decimal
        object = runScript("return cast(1.0e1 as bool)", domainData, domainType);
        assert object instanceof Boolean && (boolean) object;
        object = runScript("return cast(1.0e1 as integer)", domainData, domainType);
        assert object instanceof Long && object.equals(10L);
        object = runScript("return cast(1.0e1 as int)", domainData, domainType);
        assert object instanceof Long && object.equals(10L);
        object = runScript("return cast(1.0e1 as float)", domainData, domainType);
        assert object instanceof Double && object.equals(10.0d);
        object = runScript("return cast(1.0e1 as decimal)", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString().equals("10");
        object = runScript("return cast(1.11e1 as string)", domainData, domainType);
        assert object instanceof String && object.equals("11.1");

        // string
        object = runScript("return cast('1' as bool)", domainData, domainType);
        assert object instanceof Boolean && (boolean) object;
        object = runScript("return cast('1' as integer)", domainData, domainType);
        assert object instanceof Long && object.equals(1L);
        object = runScript("return cast('1' as int)", domainData, domainType);
        assert object instanceof Long && object.equals(1L);
        object = runScript("return cast('1' as float)", domainData, domainType);
        assert object instanceof Double && object.equals(1.0d);
        object = runScript("return cast('1.1' as decimal)", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString().equals("1.1");
        object = runScript("return cast('1' as string)", domainData, domainType);
        assert object instanceof String && object.equals("1");

        // 0x
        object = runScript("return cast('0x01' as bool)", domainData, domainType);
        assert object instanceof Boolean && (boolean) object;
        object = runScript("return cast('0x01' as integer)", domainData, domainType);
        assert object instanceof Long && object.equals(1L);
        object = runScript("return cast('0x01' as int)", domainData, domainType);
        assert object instanceof Long && object.equals(1L);
        object = runScript("return cast('0x01' as float)", domainData, domainType);
        assert object instanceof Double && object.equals(1.0d);
        object = runScript("return cast('0x01' as decimal)", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString().equals("1");
        object = runScript("return cast('0x01' as string)", domainData, domainType);
        assert object instanceof String && object.equals("0x01");
        object = runScript("return cast('0x1' as bool)", domainData, domainType);
        assert object instanceof Boolean && (boolean) object;
        object = runScript("return cast('0x1' as integer)", domainData, domainType);
        assert object instanceof Long && object.equals(1L);
        object = runScript("return cast('0x1' as int)", domainData, domainType);
        assert object instanceof Long && object.equals(1L);
        object = runScript("return cast('0x1' as float)", domainData, domainType);
        assert object instanceof Double && object.equals(1.0d);
        object = runScript("return cast('0x1' as decimal)", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString().equals("1");
        object = runScript("return cast('0x1' as string)", domainData, domainType);
        assert object instanceof String && object.equals("0x1");

        // 0o
        object = runScript("return cast('0o01' as bool)", domainData, domainType);
        assert object instanceof Boolean && (boolean) object;
        object = runScript("return cast('0o01' as integer)", domainData, domainType);
        assert object instanceof Long && object.equals(1L);
        object = runScript("return cast('0o01' as int)", domainData, domainType);
        assert object instanceof Long && object.equals(1L);
        object = runScript("return cast('0o01' as float)", domainData, domainType);
        assert object instanceof Double && object.equals(1.0d);
        object = runScript("return cast('0o01' as decimal)", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString().equals("1");
        object = runScript("return cast('0o01' as string)", domainData, domainType);
        assert object instanceof String && object.equals("0o01");
        object = runScript("return cast('0o1' as bool)", domainData, domainType);
        assert object instanceof Boolean && (boolean) object;
        object = runScript("return cast('0o1' as integer)", domainData, domainType);
        assert object instanceof Long && object.equals(1L);
        object = runScript("return cast('0o1' as int)", domainData, domainType);
        assert object instanceof Long && object.equals(1L);
        object = runScript("return cast('0o1' as float)", domainData, domainType);
        assert object instanceof Double && object.equals(1.0d);
        object = runScript("return cast('0o1' as decimal)", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString().equals("1");
        object = runScript("return cast('0o1' as string)", domainData, domainType);
        assert object instanceof String && object.equals("0o1");

        // 0b
        object = runScript("return cast('0b01' as bool)", domainData, domainType);
        assert object instanceof Boolean && (boolean) object;
        object = runScript("return cast('0b11' as integer)", domainData, domainType);
        assert object instanceof Long && object.equals(3L);
        object = runScript("return cast('0b01' as int)", domainData, domainType);
        assert object instanceof Long && object.equals(1L);
        object = runScript("return cast('0b01' as float)", domainData, domainType);
        assert object instanceof Double && object.equals(1.0d);
        object = runScript("return cast('0b01' as decimal)", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString().equals("1");
        object = runScript("return cast('0b01' as string)", domainData, domainType);
        assert object instanceof String && object.equals("0b01");
        object = runScript("return cast('0b1' as bool)", domainData, domainType);
        assert object instanceof Boolean && (boolean) object;
        object = runScript("return cast('0b1' as integer)", domainData, domainType);
        assert object instanceof Long && object.equals(1L);
        object = runScript("return cast('0b1' as int)", domainData, domainType);
        assert object instanceof Long && object.equals(1L);
        object = runScript("return cast('0b1' as float)", domainData, domainType);
        assert object instanceof Double && object.equals(1.0d);
        object = runScript("return cast('0b1' as decimal)", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString().equals("1");
        object = runScript("return cast('0b1' as string)", domainData, domainType);
        assert object instanceof String && object.equals("0b1");

        // data
        object = runScript("return cast('2021-02-23' as date)", domainData, domainType);
        assert object instanceof LocalDate && object.toString().equals("2021-02-23");
        object = runScript("return cast('2021-02-23 12:23:23' as date)", domainData, domainType);
        assert object instanceof LocalDate && object.toString().equals("2021-02-23");
        object = runScript("return cast('2021-02-23T12:23:23' as date)", domainData, domainType);
        assert object instanceof LocalDate && object.toString().equals("2021-02-23");
        object = runScript("return cast('2021-02-23T12:23:23+12:12' as date)", domainData, domainType);
        assert object instanceof LocalDate && object.toString().equals("2021-02-23");
        object = runScript("return cast('12:23:23+12:12' as date)", domainData, domainType);
        assert object instanceof LocalDate && object.toString().equals("1970-01-01");
        object = runScript("return cast('2021-02-23 12:23:23' as date)", domainData, domainType);
        assert object instanceof LocalDate && object.toString().equals("2021-02-23");
        object = runScript("return cast('2021-02-23T12:23:23.123' as date)", domainData, domainType);
        assert object instanceof LocalDate && object.toString().equals("2021-02-23");
        object = runScript("return cast('2021-02-23T12:23:23.123+12:12' as date)", domainData, domainType);
        assert object instanceof LocalDate && object.toString().equals("2021-02-23");
        object = runScript("return cast('12:23:23.123+12:12' as date)", domainData, domainType);
        assert object instanceof LocalDate && object.toString().equals("1970-01-01");
        object = runScript("return cast(T'2023-05-23' as date)", domainData, domainType);
        assert object instanceof LocalDate && object.toString().equals("2023-05-23");
        object = runScript("return cast(T'12:23:56' as date)", domainData, domainType);
        assert object instanceof LocalDate && object.toString().equals("1970-01-01");
        object = runScript("return cast(T'2023-05-23T12:23:56' as date)", domainData, domainType);
        assert object instanceof LocalDate && object.toString().equals("2023-05-23");
        object = runScript("return cast(T'2023-05-23 12:23:56' as date)", domainData, domainType);
        assert object instanceof LocalDate && object.toString().equals("2023-05-23");
        object = runScript("return cast(T'2023-05-23T12:23:56.123456789' as date)", domainData, domainType);
        assert object instanceof LocalDate && object.toString().equals("2023-05-23");
        object = runScript("return cast(T'12:23:56+03:30' as date)", domainData, domainType);
        assert object instanceof LocalDate && object.toString().equals("1970-01-01");
        object = runScript("return cast(T'12:23:56.123456789+05:34' as date)", domainData, domainType);
        assert object instanceof LocalDate && object.toString().equals("1970-01-01");
        object = runScript("return cast(T('yyyyMMdd')'20230523' as date)", domainData, domainType);
        assert object instanceof LocalDate && object.toString().equals("2023-05-23");
        object = runScript("return cast(T('yyyy-MM-dd HH:mm:ss.SSSSS')'2023-05-23 12:23:56.12345' as date)", domainData, domainType);
        assert object instanceof LocalDate && object.toString().equals("2023-05-23");

        // time
        object = runScript("return cast('2021-02-23' as time)", domainData, domainType);
        assert object instanceof LocalTime && object.toString().equals("00:00");
        object = runScript("return cast('2021-02-23 12:23:23' as time)", domainData, domainType);
        assert object instanceof LocalTime && object.toString().equals("12:23:23");
        object = runScript("return cast('2021-02-23T12:23:23' as time)", domainData, domainType);
        assert object instanceof LocalTime && object.toString().equals("12:23:23");
        object = runScript("return cast('2021-02-23T12:23:23+12:12' as time)", domainData, domainType);
        assert object instanceof LocalTime && object.toString().equals("12:23:23");
        object = runScript("return cast('12:23:23+12:12' as time)", domainData, domainType);
        assert object instanceof LocalTime && object.toString().equals("12:23:23");
        object = runScript("return cast('2021-02-23 12:23:23' as time)", domainData, domainType);
        assert object instanceof LocalTime && object.toString().equals("12:23:23");
        object = runScript("return cast('2021-02-23T12:23:23.123' as time)", domainData, domainType);
        assert object instanceof LocalTime && object.toString().equals("12:23:23.123");
        object = runScript("return cast('2021-02-23T12:23:23.123+12:12' as time)", domainData, domainType);
        assert object instanceof LocalTime && object.toString().equals("12:23:23.123");
        object = runScript("return cast('12:23:23.123+12:12' as time)", domainData, domainType);
        assert object instanceof LocalTime && object.toString().equals("12:23:23.123");

        // datetime
        object = runScript("return cast('2021-02-23' as datetime)", domainData, domainType);
        assert object instanceof LocalDateTime && object.toString().equals("2021-02-23T00:00");
        object = runScript("return cast('2021-02-23 12:23:23' as datetime)", domainData, domainType);
        assert object instanceof LocalDateTime && object.toString().equals("2021-02-23T12:23:23");
        object = runScript("return cast('2021-02-23T12:23:23' as datetime)", domainData, domainType);
        assert object instanceof LocalDateTime && object.toString().equals("2021-02-23T12:23:23");
        object = runScript("return cast('2021-02-23T12:23:23+12:12' as datetime)", domainData, domainType);
        assert object instanceof LocalDateTime && object.toString().equals("2021-02-23T12:23:23");
        object = runScript("return cast('12:23:23+12:12' as datetime)", domainData, domainType);
        assert object instanceof LocalDateTime && object.toString().equals("1970-01-01T12:23:23");
        object = runScript("return cast('2021-02-23 12:23:23' as datetime)", domainData, domainType);
        assert object instanceof LocalDateTime && object.toString().equals("2021-02-23T12:23:23");
        object = runScript("return cast('2021-02-23T12:23:23.123' as datetime)", domainData, domainType);
        assert object instanceof LocalDateTime && object.toString().equals("2021-02-23T12:23:23.123");
        object = runScript("return cast('2021-02-23T12:23:23.123+12:12' as datetime)", domainData, domainType);
        assert object instanceof LocalDateTime && object.toString().equals("2021-02-23T12:23:23.123");
        object = runScript("return cast('12:23:23.123+12:12' as datetime)", domainData, domainType);
        assert object instanceof LocalDateTime && object.toString().equals("1970-01-01T12:23:23.123");

        // fmt
        object = runScript("return cast('2021-02-23' as T('yyyy-MM-dd'))", domainData, domainType);
        assert object instanceof LocalDate && object.toString().equals("2021-02-23");
        object = runScript("return cast('2021-02-23 12:23:23' as T('yyyy-MM-dd HH:mm:ss'))", domainData, domainType);
        assert object instanceof LocalDateTime && object.toString().equals("2021-02-23T12:23:23");
        object = runScript("return cast('2021-02-23' as T('yyyy-MM-dd'))", domainData, domainType);
        assert object instanceof LocalDate && object.toString().equals("2021-02-23");
        object = runScript("return cast('12:23:23' as T('HH:mm:ss'))", domainData, domainType);
        assert object instanceof LocalTime && object.toString().equals("12:23:23");

        object = runScript("return cast('2021-02-23 12:23:23' as datetime)", domainData, domainType);
        assert object instanceof LocalDateTime && object.toString().equals("2021-02-23T12:23:23");
        object = runScript("return cast('2021-02-23T12:23:23' as datetime)", domainData, domainType);
    }

    @Test
    public void castcastTest() throws IOException {
        TestRuleDomain domainData = this.createDomain();
        Type domainType = ReflectHelper.resolveDomain(TestRuleDomain.class);
        Object object;

        // date
        object = runScript("return cast(cast('2021-02-23' as date) as date)", domainData, domainType);
        assert object instanceof LocalDate && object.toString().equals("2021-02-23");
        object = runScript("return cast(cast('2021-02-23 12:23:23' as time) as date)", domainData, domainType);
        assert object instanceof LocalDate && object.toString().equals("1970-01-01");
        object = runScript("return cast(cast('2021-02-23T12:23:23' as datetime) as date)", domainData, domainType);
        assert object instanceof LocalDate && object.toString().equals("2021-02-23");

        // time
        object = runScript("return cast(cast('2021-02-23' as date) as time)", domainData, domainType);
        assert object instanceof LocalTime && object.toString().equals("00:00");
        object = runScript("return cast(cast('2021-02-23 12:23:23' as time) as time)", domainData, domainType);
        assert object instanceof LocalTime && object.toString().equals("12:23:23");
        object = runScript("return cast(cast('2021-02-23T12:23:23' as datetime) as time)", domainData, domainType);
        assert object instanceof LocalTime && object.toString().equals("12:23:23");

        // datetime
        object = runScript("return cast(cast('2021-02-23' as date) as datetime)", domainData, domainType);
        assert object instanceof LocalDateTime && object.toString().equals("2021-02-23T00:00");
        object = runScript("return cast(cast('2021-02-23 12:23:23' as time) as datetime)", domainData, domainType);
        assert object instanceof LocalDateTime && object.toString().equals("1970-01-01T12:23:23");
        object = runScript("return cast(cast('2021-02-23T12:23:23' as datetime) as datetime)", domainData, domainType);
        assert object instanceof LocalDateTime && object.toString().equals("2021-02-23T12:23:23");

        // cast cast cast
        object = runScript("return cast(cast(cast('2021-02-23' as date) as time) as datetime)", domainData, domainType);
        assert object instanceof LocalDateTime && object.toString().equals("1970-01-01T00:00");
        object = runScript("return cast(cast(cast('12:23:23' as date) as time) as datetime)", domainData, domainType);
        assert object instanceof LocalDateTime && object.toString().equals("1970-01-01T00:00");
        object = runScript("return cast(cast(cast('2021-02-23T12:23:23' as date) as time) as datetime)", domainData, domainType);
        assert object instanceof LocalDateTime && object.toString().equals("1970-01-01T00:00");
    }

    @Test
    public void unaryExprTest_1() throws IOException {
        TestRuleDomain domainData = this.createDomain();
        Type domainType = ReflectHelper.resolveDomain(TestRuleDomain.class);
        Object object;

        object = runScript("return ++1", domainData, domainType);
        assert object instanceof Long && ((Long) object) == 2L;
        object = runScript("return + +1", domainData, domainType);
        assert object instanceof Long && ((Long) object) == 1L;
        object = runScript("return ++(++1)", domainData, domainType);
        assert object instanceof Long && ((Long) object) == 3L;
        object = runScript("return ++ +1", domainData, domainType);
        assert object instanceof Long && ((Long) object) == 2L;
        object = runScript("return +++1", domainData, domainType);
        assert object instanceof Long && ((Long) object) == 2L;

        object = runScript("return --1", domainData, domainType);
        assert object instanceof Long && ((Long) object) == 0L;
        object = runScript("return - -1", domainData, domainType);
        assert object instanceof Long && ((Long) object) == 1L;
        object = runScript("return --(--1)", domainData, domainType);
        assert object instanceof Long && ((Long) object) == -1L;
        object = runScript("return -- -1", domainData, domainType);
        assert object instanceof Long && ((Long) object) == -2L;
        object = runScript("return ---1", domainData, domainType);
        assert object instanceof Long && ((Long) object) == -2L;

        object = runScript("return -1", domainData, domainType);
        assert object instanceof Long && ((Long) object) == -1L;
        object = runScript("return +1", domainData, domainType);
        assert object instanceof Long && ((Long) object) == 1L;
        object = runScript("return !true", domainData, domainType);
        assert object instanceof Boolean && !((Boolean) object);
        object = runScript("return ~true", domainData, domainType);
        assert object instanceof Boolean && !((Boolean) object);
        object = runScript("return ~false", domainData, domainType);
        assert object instanceof Boolean && ((Boolean) object);
        object = runScript("return ~3", domainData, domainType);
        assert object instanceof Long && ((Long) object) == -4L;
        object = runScript("return ~0x11", domainData, domainType);
        assert object instanceof Long && ((Long) object) == -18L;
        object = runScript("return ~cast(0x11 as decimal)", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString().equals("14"); // 0x11 (0001 0001), 14 (0000 1110)

        object = runScript("return !(!(!(!true)))", domainData, domainType);
        assert object instanceof Boolean && ((Boolean) object);
        object = runScript("return ~(~(~(~false)))", domainData, domainType);
        assert object instanceof Boolean && !((Boolean) object);
        object = runScript("return !(~(!(~true)))", domainData, domainType);
        assert object instanceof Boolean && ((Boolean) object);
        object = runScript("return ~(!(~(!false)))", domainData, domainType);
        assert object instanceof Boolean && !((Boolean) object);
    }

    @Test
    public void unaryExprTest_2() throws IOException {
        TestRuleDomain domainData = this.createDomain();
        Type domainType = ReflectHelper.resolveDomain(TestRuleDomain.class);
        Object object;

        object = runScript("return 1++", domainData, domainType);
        assert object instanceof Long && ((Long) object) == 2L;
        object = runScript("return (1++)++", domainData, domainType);
        assert object instanceof Long && ((Long) object) == 3L;

        object = runScript("return 1--", domainData, domainType);
        assert object instanceof Long && ((Long) object) == 0L;
        object = runScript("return (1--)--", domainData, domainType);
        assert object instanceof Long && ((Long) object) == -1L;
    }

    @Test
    public void dyadicExprTest_1() throws IOException {
        TestRuleDomain domainData = this.createDomain();
        Type domainType = ReflectHelper.resolveDomain(TestRuleDomain.class);
        Object object;

        object = runScript("return 1+1", domainData, domainType);
        assert object instanceof Long && ((Long) object) == 2L;
        object = runScript("return 1+ 1", domainData, domainType);
        assert object instanceof Long && ((Long) object) == 2L;
        object = runScript("return 1 +1", domainData, domainType);
        assert object instanceof Long && ((Long) object) == 2L;
        object = runScript("return 1 + 1", domainData, domainType);
        assert object instanceof Long && ((Long) object) == 2L;
        object = runScript("return 1+-1", domainData, domainType);
        assert object instanceof Long && ((Long) object) == 0L;
        object = runScript("return 1 +-1", domainData, domainType);
        assert object instanceof Long && ((Long) object) == 0L;
        object = runScript("return 1+ -1", domainData, domainType);
        assert object instanceof Long && ((Long) object) == 0L;
        object = runScript("return 1 + -1", domainData, domainType);
        assert object instanceof Long && ((Long) object) == 0L;
        object = runScript("return 1 + - 1", domainData, domainType);
        assert object instanceof Long && ((Long) object) == 0L;
        object = runScript("return -1+-1", domainData, domainType);
        assert object instanceof Long && ((Long) object) == -2L;
        object = runScript("return -1 +-1", domainData, domainType);
        assert object instanceof Long && ((Long) object) == -2L;
        object = runScript("return -1+ -1", domainData, domainType);
        assert object instanceof Long && ((Long) object) == -2L;
        object = runScript("return -1 + -1", domainData, domainType);
        assert object instanceof Long && ((Long) object) == -2L;
        object = runScript("return -+1+-+1", domainData, domainType);
        assert object instanceof Long && ((Long) object) == -2L;
        object = runScript("return 1+2", domainData, domainType);
        assert object instanceof Long && ((Long) object) == 3L;
        object = runScript("return 1-2", domainData, domainType);
        assert object instanceof Long && ((Long) object) == -1L;
        object = runScript("return 1*2", domainData, domainType);
        assert object instanceof Long && ((Long) object) == 2L;
        object = runScript("return 1/2", domainData, domainType);
        assert object instanceof Long && ((Long) object) == 0L;
        object = runScript("return cast(1 as float)/2", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString().equals("0.5");
        object = runScript("return 10%2", domainData, domainType);
        assert object instanceof Long && ((Long) object) == 0L;
        object = runScript("return 1%3", domainData, domainType);
        assert object instanceof Long && ((Long) object) == 1L;
        object = runScript("return 1&2", domainData, domainType);
        assert object instanceof Long && ((Long) object) == 0L;
        object = runScript("return 1&3", domainData, domainType);
        assert object instanceof Long && ((Long) object) == 1L;
        object = runScript("return 1|2", domainData, domainType);
        assert object instanceof Long && ((Long) object) == 3L;
        object = runScript("return 1|3", domainData, domainType);
        assert object instanceof Long && ((Long) object) == 3L;
        object = runScript("return 1^2", domainData, domainType);
        assert object instanceof Long && ((Long) object) == 3L;
        object = runScript("return 1^3", domainData, domainType);
        assert object instanceof Long && ((Long) object) == 2L;
        object = runScript("return 3>>>2", domainData, domainType);
        assert object instanceof Long && ((Long) object) == 0L;
        object = runScript("return 8>>>2", domainData, domainType);
        assert object instanceof Long && ((Long) object) == 2L;
        object = runScript("return -1>>>2", domainData, domainType);
        assert object instanceof Long && ((Long) object) == 4611686018427387903L;
        object = runScript("return -8>>>2", domainData, domainType);
        assert object instanceof Long && ((Long) object) == 4611686018427387902L;
        object = runScript("return cast(-8 as Decimal)>>>2", domainData, domainType);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString().equals("-2");
        object = runScript("return 1<<2", domainData, domainType);
        assert object instanceof Long && ((Long) object) == 4L;
        object = runScript("return 1==2", domainData, domainType);
        assert object instanceof Boolean && !((Boolean) object);
        object = runScript("return 1!=1", domainData, domainType);
        assert object instanceof Boolean && !((Boolean) object);
        object = runScript("return 1==='1'", domainData, domainType);
        assert object instanceof Boolean && ((Boolean) object);
        object = runScript("return 1=='1'", domainData, domainType);
        assert object instanceof Boolean && !((Boolean) object);
        object = runScript("return 2>1", domainData, domainType);
        assert object instanceof Boolean && ((Boolean) object);
        object = runScript("return 1>=1", domainData, domainType);
        assert object instanceof Boolean && ((Boolean) object);
        object = runScript("return 1<2", domainData, domainType);
        assert object instanceof Boolean && ((Boolean) object);
        object = runScript("return 1<=2", domainData, domainType);
        assert object instanceof Boolean && ((Boolean) object);

        object = runScript("return 1 in []", domainData, domainType);
        assert object instanceof Boolean && !((Boolean) object);
        object = runScript("return 1 in [1,2]", domainData, domainType);
        assert object instanceof Boolean && ((Boolean) object);
        object = runScript("return 3 in [1,2]", domainData, domainType);
        assert object instanceof Boolean && !((Boolean) object);
        object = runScript("return 1 not in [1,2]", domainData, domainType);
        assert object instanceof Boolean && !((Boolean) object);
        object = runScript("return 3 not in [1,2]", domainData, domainType);
        assert object instanceof Boolean && ((Boolean) object);
        object = runScript("return 1 in {}", domainData, domainType);
        assert object instanceof Boolean && !((Boolean) object);
        object = runScript("return 1 not in {}", domainData, domainType);
        assert object instanceof Boolean && ((Boolean) object);
        object = runScript("return 1 in {'1':'value1'}", domainData, domainType);
        assert object instanceof Boolean && ((Boolean) object);
        object = runScript("return 1 not in {'1':'value1'}", domainData, domainType);
        assert object instanceof Boolean && !((Boolean) object);
        assert object instanceof Boolean && !((Boolean) object);
        object = runScript("return [1] in [1,2]", domainData, domainType);
        assert object instanceof Boolean && ((Boolean) object);
        object = runScript("return [1,2] in [1,2]", domainData, domainType);
        assert object instanceof Boolean && ((Boolean) object);
        object = runScript("return [1,3] in [1,2]", domainData, domainType);
        assert object instanceof Boolean && !((Boolean) object);
        object = runScript("return ['k1','k2'] in {'k1':1,'k2':2}", domainData, domainType);
        assert object instanceof Boolean && ((Boolean) object);
        object = runScript("return ['k1','k2'] in {'k1':1,'k3':2}", domainData, domainType);
        assert object instanceof Boolean && !((Boolean) object);
        object = runScript("return ['k1','k2'] in null", domainData, domainType);
        assert object instanceof Boolean && !((Boolean) object);
        object = runScript("return null in ['k1','k2']", domainData, domainType);
        assert object instanceof Boolean && !((Boolean) object);
        object = runScript("return null in [null, 'k1','k2']", domainData, domainType);
        assert object instanceof Boolean && ((Boolean) object);
        object = runScript("return {'k1':1} in [1,2]", domainData, domainType);
        assert object instanceof Boolean && !((Boolean) object);
        object = runScript("return {'k1':1} in ['k1','k2']", domainData, domainType);
        assert object instanceof Boolean && ((Boolean) object);
        object = runScript("return {'k1':1,'k2':2} in ['k1','k3']", domainData, domainType);
        assert object instanceof Boolean && !((Boolean) object);
        object = runScript("return {'k1':1,'k2':2} in {'k1':1,'k2':2}", domainData, domainType);
        assert object instanceof Boolean && ((Boolean) object);
        object = runScript("return {'k1':1,'k2':2} in {'k1':11,'k2':12}", domainData, domainType);
        assert object instanceof Boolean && !((Boolean) object);
        object = runScript("return {'k1':1} in {'k1':1,'k2':12}", domainData, domainType);
        assert object instanceof Boolean && ((Boolean) object);
        object = runScript("return {'k1':11} in {'k1':21,'k2':22}", domainData, domainType);
        assert object instanceof Boolean && !((Boolean) object);
        object = runScript("return {'k1':11} in null", domainData, domainType);
        assert object instanceof Boolean && !((Boolean) object);
        object = runScript("return null in {'k1':11}", domainData, domainType);
        assert object instanceof Boolean && !((Boolean) object);
        object = runScript("return '1' matches '1'", domainData, domainType);
        assert object instanceof Boolean && ((Boolean) object);
        object = runScript("return '1' matches '\\d*'", domainData, domainType);
        assert object instanceof Boolean && ((Boolean) object);
        object = runScript("return '1' matches '\\d{2}'", domainData, domainType);
        assert object instanceof Boolean && !((Boolean) object);
        object = runScript("return '1' not matches '\\d{2}'", domainData, domainType);
        assert object instanceof Boolean && ((Boolean) object);
        object = runScript("return '1' matches null", domainData, domainType);
        assert object instanceof Boolean && !((Boolean) object);
        object = runScript("return null matches '1'", domainData, domainType);
        assert object instanceof Boolean && ((Boolean) object);

        object = runScript("return 1 > 2 or 2 > 1", domainData, domainType);
        assert object instanceof Boolean && ((Boolean) object);
        object = runScript("return 1 > 2 || 2 > 1", domainData, domainType);
        assert object instanceof Boolean && ((Boolean) object);
        object = runScript("return 1 > 2 and 2 > 1", domainData, domainType);
        assert object instanceof Boolean && !((Boolean) object);
        object = runScript("return 1 > 2 && 2 > 1", domainData, domainType);
        assert object instanceof Boolean && !((Boolean) object);
    }

    @Test
    public void dyadicExpr_not_AllowUndefinedVariableFeature_1() throws IOException {
        Set<DetectRulesFeature> features = new HashSet<>();
        features.addAll(Arrays.asList(DetectRulesFeature.values()));
        features.remove(DetectRulesFeature.AllowUndefinedVariableFeature);
        //
        TestRuleDomain domainData = this.createDomain();
        Type domainType = ReflectHelper.resolveDomain(TestRuleDomain.class);
        Object object;

        object = runScript("return cast(null as float)", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object.equals(0.0d);
        object = runScript("return 1 == null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object instanceof Boolean && !((Boolean) object);
        object = runScript("return 1 != null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object instanceof Boolean && ((Boolean) object);
        object = runScript("return 1 === null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object instanceof Boolean && !((Boolean) object);
        object = runScript("return ['1', null] in null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object instanceof Boolean && !((Boolean) object);
        object = runScript("return ['1'] in null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object instanceof Boolean && !((Boolean) object);
        object = runScript("return [null] in null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object instanceof Boolean && !((Boolean) object);
        object = runScript("return '1' matches null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object instanceof Boolean && !((Boolean) object);
        //
        try {
            object = runScript("return 1 + null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:11~1:15: Invalid expression.");
        }
        try {
            object = runScript("return 1 - null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:11~1:15: Invalid expression.");
        }
        try {
            object = runScript("return 1 * null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:11~1:15: Invalid expression.");
        }
        try {
            object = runScript("return 1 / null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:11~1:15: Invalid expression.");
        }
        try {
            object = runScript("return 1 % null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:11~1:15: Invalid expression.");
        }
        try {
            object = runScript("return 1 & null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:11~1:15: Invalid expression.");
        }
        try {
            object = runScript("return 1 | null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:11~1:15: Invalid expression.");
        }
        try {
            object = runScript("return 1 ^ null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:11~1:15: Invalid expression.");
        }
        try {
            object = runScript("return 1 >>> null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:13~1:17: Invalid expression.");
        }
        try {
            object = runScript("return 1 << null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:12~1:16: Invalid expression.");
        }
        try {
            object = runScript("return 1 > null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:11~1:15: Invalid expression.");
        }
        try {
            object = runScript("return 1 >= null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:12~1:16: Invalid expression.");
        }
        try {
            object = runScript("return 1 < null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:11~1:15: Invalid expression.");
        }
        try {
            object = runScript("return 1 <= null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:12~1:16: Invalid expression.");
        }
    }

    @Test
    public void dyadicExpr_not_AllowUndefinedVariableFeature_2() throws IOException {
        Set<DetectRulesFeature> features = new HashSet<>();
        features.addAll(Arrays.asList(DetectRulesFeature.values()));
        features.remove(DetectRulesFeature.AllowUndefinedVariableFeature);
        //
        TestRuleDomain domainData = this.createDomain();
        Type domainType = ReflectHelper.resolveDomain(TestRuleDomain.class);
        Object object;

        object = runScript("return cast(null as float)", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object.equals(0.0d);
        object = runScript("return null == 1", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object instanceof Boolean && !((Boolean) object);
        object = runScript("return null != 1", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object instanceof Boolean && ((Boolean) object);
        object = runScript("return null === 1", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object instanceof Boolean && !((Boolean) object);
        object = runScript("return null in ['1', null]", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object instanceof Boolean && ((Boolean) object);
        object = runScript("return null in ['1']", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object instanceof Boolean && !((Boolean) object);
        object = runScript("return null matches '1'", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object instanceof Boolean && ((Boolean) object);

        //
        try {
            object = runScript("return null + 1", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:7~1:11: Invalid expression.");
        }
        try {
            object = runScript("return null - 1", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:7~1:11: Invalid expression.");
        }
        try {
            object = runScript("return null * 1", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:7~1:11: Invalid expression.");
        }
        try {
            object = runScript("return null / 1", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:7~1:11: Invalid expression.");
        }
        try {
            object = runScript("return null % 1", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:7~1:11: Invalid expression.");
        }
        try {
            object = runScript("return null & 1", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:7~1:11: Invalid expression.");
        }
        try {
            object = runScript("return null | 1", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:7~1:11: Invalid expression.");
        }
        try {
            object = runScript("return null ^ 1", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:7~1:11: Invalid expression.");
        }
        try {
            object = runScript("return null >>> 1", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:7~1:11: Invalid expression.");
        }
        try {
            object = runScript("return null << 1", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:7~1:11: Invalid expression.");
        }
        try {
            object = runScript("return null > 1", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:7~1:11: Invalid expression.");
        }
        try {
            object = runScript("return null >= 1", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:7~1:11: Invalid expression.");
        }
        try {
            object = runScript("return null < 1", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:7~1:11: Invalid expression.");
        }
        try {
            object = runScript("return null <= 1", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:7~1:11: Invalid expression.");
        }
    }

    @Test
    public void dyadicExpr_not_AllowUndefinedVariableFeature_3() throws IOException {
        Set<DetectRulesFeature> features = new HashSet<>();
        features.addAll(Arrays.asList(DetectRulesFeature.values()));
        features.remove(DetectRulesFeature.AllowUndefinedVariableFeature);
        //
        TestRuleDomain domainData = this.createDomain();
        Type domainType = ReflectHelper.resolveDomain(TestRuleDomain.class);
        Object object;

        object = runScript("return null == null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object instanceof Boolean && ((Boolean) object);
        object = runScript("return null != null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object instanceof Boolean && !((Boolean) object);
        object = runScript("return null === null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object instanceof Boolean && ((Boolean) object);
        object = runScript("return null in null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object instanceof Boolean && ((Boolean) object);
        object = runScript("return null matches null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object instanceof Boolean && ((Boolean) object);

        //
        object = runScript("return cast(null as float)", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object.equals(0.0d);
        object = runScript("return null + null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object == null;
        object = runScript("return null - null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object == null;
        object = runScript("return null * null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object == null;
        object = runScript("return null / null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object == null;
        object = runScript("return null % null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object == null;
        object = runScript("return null & null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object == null;
        object = runScript("return null | null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object == null;
        object = runScript("return null ^ null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object == null;
        object = runScript("return null >>> null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object == null;
        object = runScript("return null << null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object == null;
        object = runScript("return null > null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object == null;
        object = runScript("return null >= null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object == null;
        object = runScript("return null < null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object == null;
        object = runScript("return null <= null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object == null;
    }

    @Test
    public void dyadicExpr_yes_AllowUndefinedVariableFeature_1() throws IOException {
        Set<DetectRulesFeature> features = new HashSet<>();
        features.addAll(Arrays.asList(DetectRulesFeature.values()));
        //features.remove(DetectRulesFeature.AllowUndefinedVariableFeature);
        //
        TestRuleDomain domainData = this.createDomain();
        Type domainType = ReflectHelper.resolveDomain(TestRuleDomain.class);
        Object object;

        object = runScript("return cast(null as float)", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object.equals(0.0d);
        object = runScript("return 1 == null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object instanceof Boolean && !((Boolean) object);
        object = runScript("return 1 != null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object instanceof Boolean && ((Boolean) object);
        object = runScript("return 1 === null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object instanceof Boolean && !((Boolean) object);
        object = runScript("return ['1', null] in null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object instanceof Boolean && !((Boolean) object);
        object = runScript("return ['1'] in null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object instanceof Boolean && !((Boolean) object);
        object = runScript("return [null] in null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object instanceof Boolean && !((Boolean) object);
        object = runScript("return '1' matches null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object instanceof Boolean && !((Boolean) object);

        //
        object = runScript("return 1 + null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object instanceof Long && object.equals(1L);

        //
        try {
            object = runScript("return 1 - null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:11~1:15: Invalid expression.");
        }
        try {
            object = runScript("return 1 * null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:11~1:15: Invalid expression.");
        }
        try {
            object = runScript("return 1 / null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:11~1:15: Invalid expression.");
        }
        try {
            object = runScript("return 1 % null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:11~1:15: Invalid expression.");
        }
        try {
            object = runScript("return 1 & null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:11~1:15: Invalid expression.");
        }
        try {
            object = runScript("return 1 | null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:11~1:15: Invalid expression.");
        }
        try {
            object = runScript("return 1 ^ null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:11~1:15: Invalid expression.");
        }
        try {
            object = runScript("return 1 >>> null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:13~1:17: Invalid expression.");
        }
        try {
            object = runScript("return 1 << null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:12~1:16: Invalid expression.");
        }
        try {
            object = runScript("return 1 > null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:11~1:15: Invalid expression.");
        }
        try {
            object = runScript("return 1 >= null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:12~1:16: Invalid expression.");
        }
        try {
            object = runScript("return 1 < null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:11~1:15: Invalid expression.");
        }
        try {
            object = runScript("return 1 <= null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:12~1:16: Invalid expression.");
        }
    }

    @Test
    public void dyadicExpr_yes_AllowUndefinedVariableFeature_2() throws IOException {
        Set<DetectRulesFeature> features = new HashSet<>();
        features.addAll(Arrays.asList(DetectRulesFeature.values()));
        //features.remove(DetectRulesFeature.AllowUndefinedVariableFeature);
        //
        TestRuleDomain domainData = this.createDomain();
        Type domainType = ReflectHelper.resolveDomain(TestRuleDomain.class);
        Object object;

        object = runScript("return cast(null as float)", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object.equals(0.0d);
        object = runScript("return null == 1", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object instanceof Boolean && !((Boolean) object);
        object = runScript("return null != 1", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object instanceof Boolean && ((Boolean) object);
        object = runScript("return null === 1", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object instanceof Boolean && !((Boolean) object);
        object = runScript("return null in ['1', null]", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object instanceof Boolean && ((Boolean) object);
        object = runScript("return null in ['1']", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object instanceof Boolean && !((Boolean) object);
        object = runScript("return null matches '1'", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object instanceof Boolean && ((Boolean) object);

        //
        object = runScript("return null + 1", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object instanceof Long && object.equals(1L);

        //
        try {
            object = runScript("return null - 1", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:7~1:11: Invalid expression.");
        }
        try {
            object = runScript("return null * 1", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:7~1:11: Invalid expression.");
        }
        try {
            object = runScript("return null / 1", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:7~1:11: Invalid expression.");
        }
        try {
            object = runScript("return null % 1", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:7~1:11: Invalid expression.");
        }
        try {
            object = runScript("return null & 1", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:7~1:11: Invalid expression.");
        }
        try {
            object = runScript("return null | 1", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:7~1:11: Invalid expression.");
        }
        try {
            object = runScript("return null ^ 1", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:7~1:11: Invalid expression.");
        }
        try {
            object = runScript("return null >>> 1", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:7~1:11: Invalid expression.");
        }
        try {
            object = runScript("return null << 1", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:7~1:11: Invalid expression.");
        }
        try {
            object = runScript("return null > 1", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:7~1:11: Invalid expression.");
        }
        try {
            object = runScript("return null >= 1", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:7~1:11: Invalid expression.");
        }
        try {
            object = runScript("return null < 1", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:7~1:11: Invalid expression.");
        }
        try {
            object = runScript("return null <= 1", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("line 1:7~1:11: Invalid expression.");
        }
    }

    @Test
    public void dyadicExpr_yes_AllowUndefinedVariableFeature_3() throws IOException {
        Set<DetectRulesFeature> features = new HashSet<>();
        features.addAll(Arrays.asList(DetectRulesFeature.values()));
        //features.remove(DetectRulesFeature.AllowUndefinedVariableFeature);
        //
        TestRuleDomain domainData = this.createDomain();
        Type domainType = ReflectHelper.resolveDomain(TestRuleDomain.class);
        Object object;

        object = runScript("return null == null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object instanceof Boolean && ((Boolean) object);
        object = runScript("return null != null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object instanceof Boolean && !((Boolean) object);
        object = runScript("return null === null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object instanceof Boolean && ((Boolean) object);
        object = runScript("return null in null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object instanceof Boolean && ((Boolean) object);
        object = runScript("return null matches null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object instanceof Boolean && ((Boolean) object);

        //
        object = runScript("return cast(null as float)", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object.equals(0.0d);
        object = runScript("return null + null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object == null;
        object = runScript("return null - null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object == null;
        object = runScript("return null * null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object == null;
        object = runScript("return null / null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object == null;
        object = runScript("return null % null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object == null;
        object = runScript("return null & null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object == null;
        object = runScript("return null | null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object == null;
        object = runScript("return null ^ null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object == null;
        object = runScript("return null >>> null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object == null;
        object = runScript("return null << null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object == null;
        object = runScript("return null > null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object == null;
        object = runScript("return null >= null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object == null;
        object = runScript("return null < null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object == null;
        object = runScript("return null <= null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
        assert object == null;
    }

    @Test
    public void ieq_test_1() throws IOException {
        TestRuleDomain domainData = this.createDomain();
        Type domainType = ReflectHelper.resolveDomain(TestRuleDomain.class);
        Object object;

        //
        object = runScript("return 1 === 1", domainData, domainType);
        assert object.equals(true);
        object = runScript("return 1 === '1'", domainData, domainType);
        assert object.equals(true);
        object = runScript("return 'a' === 'a'", domainData, domainType);
        assert object.equals(true);
        object = runScript("return 'A' === 'a'", domainData, domainType);
        assert object.equals(true);
    }

    @Test
    public void eq_test_1() throws IOException {
        TestRuleDomain domainData = this.createDomain();
        Type domainType = ReflectHelper.resolveDomain(TestRuleDomain.class);
        Object object;

        //
        object = runScript("return 1 == 1", domainData, domainType);
        assert object.equals(true);
        object = runScript("return 1 == '1'", domainData, domainType);
        assert object.equals(false);
        object = runScript("return 'a' == 'a'", domainData, domainType);
        assert object.equals(true);
        object = runScript("return 'A' == 'a'", domainData, domainType);
        assert object.equals(false);
    }

    @Test
    public void null_cast_1() throws IOException {
        TestRuleDomain domainData = this.createDomain();
        Type domainType = ReflectHelper.resolveDomain(TestRuleDomain.class);
        Object object;

        //
        object = runScript("return cast(null as bool)", domainData, domainType);
        assert object.equals(false);
        object = runScript("return cast(null as float)", domainData, domainType);
        assert object.equals(0.0D);
        object = runScript("return cast(null as int)", domainData, domainType);
        assert object.equals(0L);
        object = runScript("return cast(null as decimal)", domainData, domainType);
        assert object.equals(BigDecimal.ZERO);

        //
        object = runScript("return cast(null as date)", domainData, domainType);
        assert object == null;
        object = runScript("return cast(null as time)", domainData, domainType);
        assert object == null;
        object = runScript("return cast(null as datetime)", domainData, domainType);
        assert object == null;
        object = runScript("return cast(null as string)", domainData, domainType);
        assert object == null;
        object = runScript("return cast(null as T('yyyy-MM-dd HH:mm:ss'))", domainData, domainType);
        assert object == null;
    }

    @Test
    public void dyadicExprOfNullTest_2() throws IOException {
        TestRuleDomain domainData = this.createDomain();
        Type domainType = ReflectHelper.resolveDomain(TestRuleDomain.class);
        Object object;

        object = runScript("return @domain.table != null", domainData, domainType);
        assert object instanceof Boolean && ((Boolean) object);
        object = runScript("return @domain.comment == null", domainData, domainType);
        assert object instanceof Boolean && ((Boolean) object);
        object = runScript("return !null", domainData, domainType); // null as be false
        assert object instanceof Boolean && ((Boolean) object);
    }

    @Test
    public void identifyExprTest() throws IOException {
        TestRuleDomain domainData = this.createDomain();
        Type domainType = ReflectHelper.resolveDomain(TestRuleDomain.class);
        Object object;

        object = runScript("return @domain.catalog", domainData, domainType);
        assert object instanceof String && object.equals("abc");

        object = runScript("return @domain.ddlKind", domainData, domainType);
        assert object instanceof String && object.equals("c");
        object = runScript("return @domain.sqlType", domainData, domainType);
        assert object instanceof String && object.equals("INSERT");

        object = runScript("return @stringArray", domainData, domainType);
        assert object instanceof List && ((List<?>) object).get(0).equals("a") && ((List<?>) object).get(1).equals("b") && ((List<?>) object).get(2).equals("c")
               && ((List<?>) object).get(3) == null;
        object = runScript("return @stringList", domainData, domainType);
        assert object instanceof List && ((List<?>) object).get(0).equals("a") && ((List<?>) object).get(1).equals("b") && ((List<?>) object).get(2).equals("c")
               && ((List<?>) object).get(3) == null;
        object = runScript("return @stringArray[0]", domainData, domainType);
        assert object instanceof String && object.equals("a");
        object = runScript("return @stringArray[3]", domainData, domainType);
        assert object == null;
        object = runScript("return @stringList[0]", domainData, domainType);
        assert object instanceof String && object.equals("a");
        object = runScript("return @stringList[3]", domainData, domainType);
        assert object == null;

        object = runScript("return @domain.options", domainData, domainType);
        assert object instanceof Map && ((Map) object).containsKey("a") && ((Map) object).containsKey("b");
        assert object instanceof Map && ((Map) object).get("a").equals("aa") && ((Map) object).get("b").equals("bb");
        object = runScript("return @domain.options.a", domainData, domainType);
        assert object instanceof String && object.equals("aa");
        object = runScript("return @domain.options['a']", domainData, domainType);
        assert object instanceof String && object.equals("aa");

        object = runScript("return #{abc}", domainData, domainType, CollectionUtils.asMap("abc", "123"));
        assert object instanceof String && object.equals("123");
        object = runScript("return #{abc}", domainData, domainType, CollectionUtils.asMap("abc", "321"));
        assert object instanceof String && !object.equals("123");
        object = runScript("return @fun.str.trim(#{abc},' ')", domainData, domainType, CollectionUtils.asMap("abc", "   123   "));
        assert object instanceof String && object.equals("123");

        object = runScript("#define \"abc\" as string default \"value is abc\" return #{abc}", domainData, domainType, CollectionUtils.asMap("abc", "123"));
        assert object instanceof String && object.equals("123");
        object = runScript("#define \"abc\" as string default \"value is abc\" return #{abc}", domainData, domainType, null);
        assert object instanceof String && object.equals("value is abc");
    }

    @Test
    public void assignExprTest_1() throws IOException {
        TestRuleDomain domainData = this.createDomain();
        Type domainType = ReflectHelper.resolveDomain(TestRuleDomain.class);
        Object object;

        object = runScript("abc = #{abc} return abc", domainData, domainType, CollectionUtils.asMap("abc", "123"));
        assert object instanceof String && object.equals("123");
        object = runScript("abc = @domain.table return abc", domainData, domainType, null);
        assert object instanceof String && object.equals("tester");
        object = runScript("abc = @fun.str.isBlank(@domain.table) return abc", domainData, domainType, null);
        assert object instanceof Boolean && !(Boolean) object;

        object = runScript("abc = 'aabbcc' return abc", domainData, domainType, CollectionUtils.asMap("abc", "123"));
        assert object instanceof String && object.equals("aabbcc");
        object = runScript("abc = 123 return abc", domainData, domainType, null);
        assert object instanceof Long && object.equals(123L);
        object = runScript("abc = 123.123 return abc", domainData, domainType, null);
        assert object instanceof Double && object.equals(123.123D);
        object = runScript("abc = 123.123e3 return abc", domainData, domainType, null);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString().equals("123123");
        object = runScript("abc = true return abc", domainData, domainType, null);
        assert object instanceof Boolean && (Boolean) object;
        object = runScript("abc = 0b11 return abc", domainData, domainType, null);
        assert object instanceof Long && object.equals(3L);
        object = runScript("abc = null return abc", domainData, domainType, null);

        assert object == null;
        object = runScript("abc = T'2023-05-23' return abc", domainData, domainType, null);
        assert object instanceof LocalDate && object.toString().equals("2023-05-23");
        object = runScript("abc = T'12:23:33' return abc", domainData, domainType, null);
        assert object instanceof LocalTime && object.toString().equals("12:23:33");
        object = runScript("abc = T'2023-05-23T12:23:33' return abc", domainData, domainType, null);
        assert object instanceof LocalDateTime && object.toString().equals("2023-05-23T12:23:33");
        object = runScript("abc = T('yyyy-MM-dd HH:mm:ss.SSSS')'2023-05-23 12:23:56.1234' return abc", domainData, domainType);
        assert object instanceof LocalDateTime && object.toString().equals("2023-05-23T12:23:56.123400");

        object = runScript("abc = [1,2,3] return abc", domainData, domainType, null);
        assert object instanceof List && ((List<?>) object).size() == 3 &&//
               ((List<?>) object).get(0).equals(1L) && ((List<?>) object).get(1).equals(2L) && ((List<?>) object).get(2).equals(3L);
        object = runScript("abc = {'k1':1,'k2':2} return abc", domainData, domainType, null);
        assert object instanceof Map && ((Map<?, ?>) object).size() == 2 &&//
               ((Map<?, ?>) object).get("k1").equals(1L) && ((Map<?, ?>) object).get("k2").equals(2L);
    }

    @Test
    public void assignExprTest_2() throws IOException {
        TestRuleDomain domainData = this.createDomain();
        Type domainType = ReflectHelper.resolveDomain(TestRuleDomain.class);
        Object object;

        object = runScript("abc = 2 array = [1,2,3,4] return array[abc]", domainData, domainType, null);
        assert object instanceof Long && object.equals(3L);
        object = runScript("abc = 'a' array = {'a':'a1','b':'b1','c':'c1'} return array[abc]", domainData, domainType, null);
        assert object instanceof String && object.equals("a1");
        object = runScript("if true then checkName = @domain.table elseif false then checkName = @domain.newName else return false end return checkName", domainData, domainType, null);
        assert object instanceof String && object.equals("tester");
        object = runScript("a = 1 b = 2 return a + b", domainData, domainType, null);
        assert object instanceof Long && object.equals(3L);
        object = runScript("a = 1 b = 2 return a > b", domainData, domainType, null);
        assert object instanceof Boolean && !(Boolean) object;
        object = runScript("checkName = 'Abc' return @fun.str.lowerCase(checkName) == checkName", domainData, domainType, null);
        assert object instanceof Boolean && !(Boolean) object;
        object = runScript("checkName = 'abc' return @fun.str.lowerCase(checkName) == checkName", domainData, domainType, null);
        assert object instanceof Boolean && (Boolean) object;
    }

    @Test
    public void domainExprTest() throws IOException {
        TestRuleDomain domainData = this.createDomain();
        domainData.getQueryTypes().add(SecQueryType.CREATE_INDEX);
        domainData.getQueryTypes().add(SecQueryType.CREATE_TABLE);

        Type domainType = ReflectHelper.resolveDomain(TestRuleDomain.class);
        Object object;

        //
        object = runScript("return 'CREATE_INDEX' in @queryTypes", domainData, domainType, null);
        assert object instanceof Boolean && (Boolean) object;
        System.out.println();
    }

    @Test
    public void nullInMapTest_1() throws IOException {
        TestMapDomain domainData = new TestMapDomain();
        domainData.getValues().put("abc", null);

        Type domainType = ReflectHelper.resolveDomain(TestMapDomain.class);
        Object object;

        //
        object = runScript("return @fun.str.contains(@values['abc'],'123')", domainData, domainType, null);
        assert object instanceof Boolean && !((Boolean) object);

        //
        object = runScript("return @values['abc'] == null", domainData, domainType, null);
        assert object instanceof Boolean && ((Boolean) object);
        object = runScript("return @values.abc == null", domainData, domainType, null);
        assert object instanceof Boolean && ((Boolean) object);

        //
        object = runScript("return @values.abc === ''", domainData, domainType, null);
        assert object instanceof Boolean && ((Boolean) object);
        object = runScript("return @values['abc'] === ''", domainData, domainType, null);
        assert object instanceof Boolean && ((Boolean) object);
    }

    @Test
    public void nullInMapTest_2() throws IOException {
        TestMapDomain domainData = new TestMapDomain();
        domainData.getValues().put("abc", "123");

        Type domainType = ReflectHelper.resolveDomain(TestMapDomain.class);
        Object object;

        //
        object = runScript("return @fun.str.contains(@values['abc'],'123')", domainData, domainType, null);
        assert object instanceof Boolean && ((Boolean) object);

        //
        object = runScript("return @values['abc'] == null", domainData, domainType, null);
        assert object instanceof Boolean && !((Boolean) object);
        object = runScript("return @values.abc == null", domainData, domainType, null);
        assert object instanceof Boolean && !((Boolean) object);

        //
        object = runScript("return @values.abc === ''", domainData, domainType, null);
        assert object instanceof Boolean && !((Boolean) object);
        object = runScript("return @values['abc'] === ''", domainData, domainType, null);
        assert object instanceof Boolean && !((Boolean) object);
    }

    @Test
    public void undefinedVariableTest_1() throws IOException {
        TestRuleDomain domainData = this.createDomain();
        Type domainType = ReflectHelper.resolveDomain(TestRuleDomain.class);
        Object object;

        //
        object = runScript("return abc == null", domainData, domainType, null);
        assert object instanceof Boolean && (Boolean) object;
        object = runScript("return abc.b.c == null", domainData, domainType, null);
        assert object instanceof Boolean && (Boolean) object;
        object = runScript("return @abc.b.c(bcd) == null", domainData, domainType, null);
        assert object instanceof Boolean && (Boolean) object;

        try {
            Set<DetectRulesFeature> features = new HashSet<>();
            features.addAll(Arrays.asList(DetectRulesFeature.values()));
            features.remove(DetectRulesFeature.AllowUndefinedVariableFeature);
            //
            runScript("return abc == null", domainData, domainType, null, features.toArray(new DetectRulesFeature[0]));
            assert false;
        } catch (RuntimeException e) {
            assert e.getMessage().contains("abc undefined.");
        }
    }

    @Test
    public void variableTest_1() throws IOException {
        TestRuleDomain domainData = this.createDomain();
        Type domainType = ReflectHelper.resolveDomain(TestRuleDomain.class);
        Object object;

        //
        object = runScript("a =true return a", domainData, domainType, null);
        assert object instanceof Boolean && object.equals(true);
        object = runScript("a =false return a", domainData, domainType, null);
        assert object instanceof Boolean && object.equals(false);
        object = runScript("a =1 return a", domainData, domainType, null);
        assert object instanceof Long && object.equals(1L);
        object = runScript("a =1.1 return a", domainData, domainType, null);
        assert object instanceof Double && object.equals(1.1d);
        object = runScript("a =11111111111111111111111111111111111111111111111111111111 return a", domainData, domainType, null);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString().equals("11111111111111111111111111111111111111111111111111111111");
        object = runScript("a =1111111111111111111111111111111111111111111111111111111e1 return a", domainData, domainType, null);
        assert object instanceof BigDecimal && ((BigDecimal) object).toPlainString().equals("11111111111111111111111111111111111111111111111111111110");

        object = runScript("a ='1' return a", domainData, domainType, null);
        assert object instanceof String && object.equals("1");

        object = runScript("a =T'2020-12-12' return a", domainData, domainType, null);
        assert object instanceof LocalDate && object.toString().equals("2020-12-12");
        object = runScript("a =T'23:23:12' return a", domainData, domainType, null);
        assert object instanceof LocalTime && object.toString().equals("23:23:12");

        object = runScript("a =T'2020-12-12 23:23:12' return a", domainData, domainType, null);
        assert object instanceof LocalDateTime && object.toString().equals("2020-12-12T23:23:12");

        object = runScript("a =[1,2] return a", domainData, domainType, null);
        assert object instanceof List && ((List<?>) object).get(0).equals(1L) && ((List<?>) object).get(1).equals(2L);
        object = runScript("a ={'k1':1,'k2':2} return a", domainData, domainType, null);
        assert object instanceof Map && ((Map) object).get("k1").equals(1L) && ((Map) object).get("k2").equals(2L);

        object = runScript("dat =[1,2] idx = 1 return dat[idx]", domainData, domainType, null);
        assert object instanceof Long && object.equals(2L);
        object = runScript("dat ={'k1':1,'k2':2} idx = 'k2' return dat[idx]", domainData, domainType, null);
        assert object instanceof Long && object.equals(2L);
    }
}
