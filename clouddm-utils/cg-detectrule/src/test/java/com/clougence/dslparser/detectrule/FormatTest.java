package com.clougence.dslparser.detectrule;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;

import com.clougence.detectrule.parser.DetectRulesFeature;
import com.clougence.detectrule.parser.antlr.DefaultDetectRulesVisitor;
import com.clougence.detectrule.parser.antlr.DetectRulesLexer;
import com.clougence.detectrule.parser.antlr.DetectRulesParser;
import com.clougence.detectrule.parser.ast.statement.StatementList;
import com.clougence.detectrule.parser.format.DetectRuleFmtOptions;
import com.clougence.dslpaser.antlr.ThrowingListener;
import com.clougence.dslpaser.foramt.FmtWriter;

public class FormatTest {

    public static Object queryParser(DetectRulesFeature[] featureArrays, CharStream charStream) {
        List<DetectRulesFeature> features = Arrays.asList(featureArrays);

        DetectRulesLexer lexer = new DetectRulesLexer(charStream, features);
        lexer.removeErrorListeners();
        lexer.addErrorListener(ThrowingListener.INSTANCE);

        DetectRulesParser qlParser = new DetectRulesParser(new CommonTokenStream(lexer), features);
        qlParser.removeErrorListeners();
        qlParser.addErrorListener(ThrowingListener.INSTANCE);

        DefaultDetectRulesVisitor visitor = new DefaultDetectRulesVisitor(features);
        return visitor.visit(qlParser.root());
    }

    public static CharStream fromString(String script) {
        return CharStreams.fromString(script);
    }

    @Test
    public void primaryValueTest_1() {
        Object obj = null;
        obj = queryParser(DetectRulesFeature.values(), fromString("return true"));
        assert obj.toString().equals("return true");
        obj = queryParser(DetectRulesFeature.values(), fromString("return not true"));
        assert obj.toString().equals("return not true");
        obj = queryParser(DetectRulesFeature.values(), fromString("return !true"));
        assert obj.toString().equals("return !true");
        obj = queryParser(DetectRulesFeature.values(), fromString("return not true"));
        assert obj.toString().equals("return not true");

        obj = queryParser(DetectRulesFeature.values(), fromString("return 0b0"));
        assert obj.toString().equals("return 0b0");
        obj = queryParser(DetectRulesFeature.values(), fromString("return 0B1"));
        assert obj.toString().equals("return 0B1");
        obj = queryParser(DetectRulesFeature.values(), fromString("return 0o1234567"));
        assert obj.toString().equals("return 0o1234567");
        obj = queryParser(DetectRulesFeature.values(), fromString("return    0O1234567"));
        assert obj.toString().equals("return 0O1234567");

        obj = queryParser(DetectRulesFeature.values(), fromString("return 6    // 8bit byte"));
        assert obj.toString().equals("return 6");
        obj = queryParser(DetectRulesFeature.values(), fromString("return +6"));
        assert obj.toString().equals("return +6");
        obj = queryParser(DetectRulesFeature.values(), fromString("return -+6"));
        assert obj.toString().equals("return - +6");
        obj = queryParser(DetectRulesFeature.values(), fromString("return - -6"));
        assert obj.toString().equals("return - -6");
        obj = queryParser(DetectRulesFeature.values(), fromString("return -0000000123"));
        assert obj.toString().equals("return -0000000123");
        obj = queryParser(DetectRulesFeature.values(), fromString("return --0000000123"));
        assert obj.toString().equals("return --0000000123");
        obj = queryParser(DetectRulesFeature.values(), fromString("return ---0000000123"));
        assert obj.toString().equals("return -- -0000000123");

        obj = queryParser(DetectRulesFeature.values(), fromString("return 1."));
        assert obj.toString().equals("return 1.");
        obj = queryParser(DetectRulesFeature.values(), fromString("return +1."));
        assert obj.toString().equals("return +1.");
        obj = queryParser(DetectRulesFeature.values(), fromString("return -+1."));
        assert obj.toString().equals("return - +1.");
        obj = queryParser(DetectRulesFeature.values(), fromString("return - -1."));
        assert obj.toString().equals("return - -1.");
        obj = queryParser(DetectRulesFeature.values(), fromString("return   -1230000000.0  "));
        assert obj.toString().equals("return -1230000000.0");

        obj = queryParser(DetectRulesFeature.values(), fromString("return 1.e1"));
        assert obj.toString().equals("return 1.e1");
        obj = queryParser(DetectRulesFeature.values(), fromString("return -1.e1"));
        assert obj.toString().equals("return -1.e1");
        obj = queryParser(DetectRulesFeature.values(), fromString("return - 1.e1"));
        assert obj.toString().equals("return -1.e1");
        obj = queryParser(DetectRulesFeature.values(), fromString("return - +1.e1"));
        assert obj.toString().equals("return - +1.e1");
        obj = queryParser(DetectRulesFeature.values(), fromString("return - -1.e1"));
        assert obj.toString().equals("return - -1.e1");

        obj = queryParser(DetectRulesFeature.values(), fromString("return 'abcdefg...'"));
        assert obj.toString().equals("return 'abcdefg...'");
        obj = queryParser(DetectRulesFeature.values(), fromString("return 'abcde\\'fg...'"));
        assert obj.toString().equals("return 'abcde\\'fg...'");
        obj = queryParser(DetectRulesFeature.values(), fromString("return 'abcde\"fg...'"));
        assert obj.toString().equals("return 'abcde\"fg...'");
        obj = queryParser(DetectRulesFeature.values(), fromString("return ''"));
        assert obj.toString().equals("return ''");
        obj = queryParser(DetectRulesFeature.values(), fromString("return \"abcdefg...\""));
        assert obj.toString().equals("return \"abcdefg...\"");
        obj = queryParser(DetectRulesFeature.values(), fromString("return \"abcde'fg...\""));
        assert obj.toString().equals("return \"abcde'fg...\"");
        obj = queryParser(DetectRulesFeature.values(), fromString("return \"abcde\\\"fg...\""));
        assert obj.toString().equals("return \"abcde\\\"fg...\"");
        obj = queryParser(DetectRulesFeature.values(), fromString("return \"\""));
        assert obj.toString().equals("return \"\"");
        obj = queryParser(DetectRulesFeature.values(), fromString("return '\\u0041'"));
        assert obj.toString().equals("return '\\u0041'");

        obj = queryParser(DetectRulesFeature.values(), fromString("return T'2023-05-23'"));
        assert obj.toString().equals("return T'2023-05-23'");
        obj = queryParser(DetectRulesFeature.values(), fromString("return T\"2023-05-23\""));
        assert obj.toString().equals("return T\"2023-05-23\"");
        obj = queryParser(DetectRulesFeature.values(), fromString("return T('yyyy-MM-dd')'2023-05-23'"));
        assert obj.toString().equals("return T('yyyy-MM-dd')'2023-05-23'");
        obj = queryParser(DetectRulesFeature.values(), fromString("return T('yyyy-MM-dd')\"2023-05-23\""));
        assert obj.toString().equals("return T('yyyy-MM-dd')\"2023-05-23\"");
        obj = queryParser(DetectRulesFeature.values(), fromString("return T(\"yyyy-MM-dd\")\"2023-05-23\""));
        assert obj.toString().equals("return T(\"yyyy-MM-dd\")\"2023-05-23\"");

        obj = queryParser(DetectRulesFeature.values(), fromString("return null"));
        assert obj.toString().equals("return null");
        obj = queryParser(DetectRulesFeature.values(), fromString("return NULL"));
        assert obj.toString().equals("return NULL");

        obj = queryParser(DetectRulesFeature.values(), fromString("return abc"));
        assert obj.toString().equals("return abc");

        obj = queryParser(DetectRulesFeature.values(), fromString("return []"));
        assert obj.toString().equals("return []");
        obj = queryParser(DetectRulesFeature.values(), fromString("return [[],[]]"));
        assert obj.toString().equals("return [[], []]");
        obj = queryParser(DetectRulesFeature.values(), fromString("return [{},{}]"));
        assert obj.toString().equals("return [{}, {}]");
        obj = queryParser(DetectRulesFeature.values(), fromString("return [[[],[],[]],[[],[],[]],[[],[],[]]]"));
        assert obj.toString().equals("return [[[], [], []], [[], [], []], [[], [], []]]");
        obj = queryParser(DetectRulesFeature.values(), fromString("return [1,2,3,4]"));
        assert obj.toString().equals("return [1, 2, 3, 4]");
        obj = queryParser(DetectRulesFeature.values(), fromString("return [abc, '1',\"2\",3,T'2024-05-06',T(\"xxx\")'2024-05-06',123,123e5,+23.,.123,-.123,true,null]"));
        assert obj.toString().equals("return [abc, '1', \"2\", 3, T'2024-05-06', T(\"xxx\")'2024-05-06', 123, 123e5, +23., .123, -.123, true, null]");

        obj = queryParser(DetectRulesFeature.values(), fromString("return {}"));
        assert obj.toString().equals("return {}");
        obj = queryParser(DetectRulesFeature.values(), fromString("return {'k1':{},'k2':{}}"));
        assert obj.toString().equals("return {'k1': {}, 'k2': {}}");
        obj = queryParser(DetectRulesFeature
            .values(), fromString("return {'k1':'1','k2':\"2\",'k3':3,'k4':T'2024-05-06','k5':T(\"xxx\")'2024-05-06','k6':123,'k7':123e5,'k8':+23.,'k9':.123,'k10':-.123,'k11':true,'k12':null,'k13':abc,'k14':@domain.abc,'k15':@domain.abc[aac],'k16':@domain.abc()}"));
        assert obj.toString()
            .equals("return {'k1': '1', 'k2': \"2\", 'k3': 3, 'k4': T'2024-05-06', 'k5': T(\"xxx\")'2024-05-06', 'k6': 123, 'k7': 123e5, 'k8': +23., 'k9': .123, 'k10': -.123, 'k11': true, 'k12': null, 'k13': abc, 'k14': @domain.abc, 'k15': @domain.abc[aac], 'k16': @domain.abc()}");
    }

    @Test
    public void primaryValueTest_2() throws IOException {
        Map<String, String> fmtOption1 = new HashMap<>();
        fmtOption1.put(DetectRuleFmtOptions.WRAPPING_ARRAY_ELEMENT_AS_NEW_LINE.getKey(), "Any");
        fmtOption1.put(DetectRuleFmtOptions.WRAPPING_PAIR_ELEMENT_AS_NEW_LINE.getKey(), "Pair");
        Map<String, String> fmtOption2 = new HashMap<>();
        fmtOption2.put(DetectRuleFmtOptions.WRAPPING_ARRAY_ELEMENT_AS_NEW_LINE.getKey(), "Inline");
        fmtOption2.put(DetectRuleFmtOptions.WRAPPING_PAIR_ELEMENT_AS_NEW_LINE.getKey(), "Inline");

        StatementList obj;
        obj = (StatementList) queryParser(DetectRulesFeature.values(), fromString("return [[],[]]"));
        assert toString(obj, fmtOption2).equals("return [[], []]");
        obj = (StatementList) queryParser(DetectRulesFeature.values(), fromString("return [[],[]]"));
        assert toString(obj, fmtOption1).equals("return [\n" + //
                                                "    [],\n" + //
                                                "    []\n" +//
                                                "]");

        obj = (StatementList) queryParser(DetectRulesFeature.values(), fromString("return [[1,2],[3,4],5]"));
        assert toString(obj, fmtOption2).equals("return [[1, 2], [3, 4], 5]");
        obj = (StatementList) queryParser(DetectRulesFeature.values(), fromString("return [[1,2],[3,4],5]"));
        assert toString(obj, fmtOption1).equals("return [\n" +//
                                                "    [\n" + //
                                                "        1,\n" + //
                                                "        2\n" + //
                                                "    ],\n" + //
                                                "    [\n" + //
                                                "        3,\n" +//
                                                "        4\n" +//
                                                "    ],\n" + //
                                                "    5\n" +//
                                                "]");

        obj = (StatementList) queryParser(DetectRulesFeature.values(), fromString("return {'k1':{},'k2':{}}"));
        assert toString(obj, fmtOption2).equals("return {'k1': {}, 'k2': {}}");
        obj = (StatementList) queryParser(DetectRulesFeature.values(), fromString("return {'k1':{},'k2':{}}"));
        assert toString(obj, fmtOption1).equals("return {\n" +//
                                                "    'k1': {},\n" +//
                                                "    'k2': {}\n" +//
                                                "}");

        obj = (StatementList) queryParser(DetectRulesFeature.values(), fromString("return {'k1':{'k1_1':{},'k1_2':{}},'k2':{'k2_1':{},'k2_2':{}}}"));
        assert toString(obj, fmtOption2).equals("return {'k1': {'k1_1': {}, 'k1_2': {}}, 'k2': {'k2_1': {}, 'k2_2': {}}}");
        obj = (StatementList) queryParser(DetectRulesFeature.values(), fromString("return {'k1':{'k1_1':{},'k1_2':{}},'k2':{'k2_1':{},'k2_2':{}}}"));
        assert toString(obj, fmtOption1).equals("return {\n" +//
                                                "    'k1': {\n" + //
                                                "        'k1_1': {},\n" + //
                                                "        'k1_2': {}\n" +//
                                                "    },\n" + //
                                                "    'k2': {\n" +//
                                                "        'k2_1': {},\n" + //
                                                "        'k2_2': {}\n" +//
                                                "    }\n" + //
                                                "}");
    }

    @Test
    public void expressionTest_1() {
        Object obj = null;
        obj = queryParser(DetectRulesFeature.values(), fromString("return 1+1"));
        assert obj.toString().equals("return 1 + 1");
        obj = queryParser(DetectRulesFeature.values(), fromString("return 1 +1"));
        assert obj.toString().equals("return 1 + 1");
        obj = queryParser(DetectRulesFeature.values(), fromString("return 1+-1"));
        assert obj.toString().equals("return 1 + -1");
        obj = queryParser(DetectRulesFeature.values(), fromString("return 1+ -1"));
        assert obj.toString().equals("return 1 + -1");
        obj = queryParser(DetectRulesFeature.values(), fromString("return -1+-1"));
        assert obj.toString().equals("return -1 + -1");
        obj = queryParser(DetectRulesFeature.values(), fromString("return -+1+-+1"));
        assert obj.toString().equals("return - +1 + - +1");

        obj = queryParser(DetectRulesFeature.values(), fromString("return ++1"));
        assert obj.toString().equals("return ++1");
        obj = queryParser(DetectRulesFeature.values(), fromString("return ++(++1)"));
        assert obj.toString().equals("return ++(++1)");
        obj = queryParser(DetectRulesFeature.values(), fromString("return +++1"));
        assert obj.toString().equals("return ++ +1");
        obj = queryParser(DetectRulesFeature.values(), fromString("return ++(++ +1)"));
        assert obj.toString().equals("return ++(++ +1)");
        obj = queryParser(DetectRulesFeature.values(), fromString("return +1"));
        assert obj.toString().equals("return +1");
        obj = queryParser(DetectRulesFeature.values(), fromString("return +( + (+ (+ (+1))))"));
        assert obj.toString().equals("return +(+(+(+(+1))))");
        obj = queryParser(DetectRulesFeature.values(), fromString("return + +1"));
        assert obj.toString().equals("return + +1");
        obj = queryParser(DetectRulesFeature.values(), fromString("return (((--(--1))++)++)++"));
        assert obj.toString().equals("return (((--(--1))++)++)++");
        obj = queryParser(DetectRulesFeature.values(), fromString("return (((--(--1))--)--)--"));
        assert obj.toString().equals("return (((--(--1))--)--)--");
        obj = queryParser(DetectRulesFeature.values(), fromString("return ((( --  (-- 1)) ++ ) ++ ) ++"));
        assert obj.toString().equals("return (((--(--1))++)++)++");
        obj = queryParser(DetectRulesFeature.values(), fromString("return ((( -- ( -- 1)) -- ) -- ) -- "));
        assert obj.toString().equals("return (((--(--1))--)--)--");

        obj = queryParser(DetectRulesFeature.values(), fromString("return -1"));
        assert obj.toString().equals("return -1");
        obj = queryParser(DetectRulesFeature.values(), fromString("return !1"));
        assert obj.toString().equals("return !1");
        obj = queryParser(DetectRulesFeature.values(), fromString("return ~1"));
        assert obj.toString().equals("return ~1");
        obj = queryParser(DetectRulesFeature.values(), fromString("return !(!(!(!true)))"));
        assert obj.toString().equals("return !(!(!(!true)))");
        obj = queryParser(DetectRulesFeature.values(), fromString("return ~(!(~(!false)))"));
        assert obj.toString().equals("return ~(!(~(!false)))");

        obj = queryParser(DetectRulesFeature.values(), fromString("return 1+2"));
        assert obj.toString().equals("return 1 + 2");
        obj = queryParser(DetectRulesFeature.values(), fromString("return 1>>>2"));
        assert obj.toString().equals("return 1 >>> 2");
        obj = queryParser(DetectRulesFeature.values(), fromString("return 1in    2"));
        assert obj.toString().equals("return 1 in 2");
        obj = queryParser(DetectRulesFeature.values(), fromString("return 1in[1,2]"));
        assert obj.toString().equals("return 1 in [1, 2]");
        obj = queryParser(DetectRulesFeature.values(), fromString("return 1in{'k1':1}"));
        assert obj.toString().equals("return 1 in {'k1': 1}");
        obj = queryParser(DetectRulesFeature.values(), fromString("return 1in{}"));
        assert obj.toString().equals("return 1 in {}");
        obj = queryParser(DetectRulesFeature.values(), fromString("return 1in[]"));
        assert obj.toString().equals("return 1 in []");
        obj = queryParser(DetectRulesFeature.values(), fromString("return [1]in   1"));
        assert obj.toString().equals("return [1] in 1");
        obj = queryParser(DetectRulesFeature.values(), fromString("return {\"k1\":1}in{'k1':1}"));
        assert obj.toString().equals("return {\"k1\": 1} in {'k1': 1}");
        obj = queryParser(DetectRulesFeature.values(), fromString("return {\"k1\":1}in T'2023-05-11'"));
        assert obj.toString().equals("return {\"k1\": 1} in T'2023-05-11'");
        obj = queryParser(DetectRulesFeature.values(), fromString("return {\"k1\": 1} in T('yyyy-MM-dd')'2023-05-11'"));
        assert obj.toString().equals("return {\"k1\": 1} in T('yyyy-MM-dd')'2023-05-11'");
        obj = queryParser(DetectRulesFeature.values(), fromString("return 1not in 1"));
        assert obj.toString().equals("return 1 not in 1");
        obj = queryParser(DetectRulesFeature.values(), fromString("return {\"k1\":1}not in{'k1':1}"));
        assert obj.toString().equals("return {\"k1\": 1} not in {'k1': 1}");
        obj = queryParser(DetectRulesFeature.values(), fromString("return {\"k1\":1}not in T'2023-05-11'"));
        assert obj.toString().equals("return {\"k1\": 1} not in T'2023-05-11'");
        obj = queryParser(DetectRulesFeature.values(), fromString("return 1 || -1"));
        assert obj.toString().equals("return 1 || -1");
    }

    @Test
    public void expressionTest_2() {
        Object obj;
        obj = queryParser(DetectRulesFeature.values(), fromString("return 1+2*3"));
        assert obj.toString().equals("return 1 + 2 * 3");
        obj = queryParser(DetectRulesFeature.values(), fromString("return (1+2)*3"));
        assert obj.toString().equals("return (1 + 2) * 3");
        obj = queryParser(DetectRulesFeature.values(), fromString("return 1+++2"));
        assert obj.toString().equals("return 1 ++ +2");
        obj = queryParser(DetectRulesFeature.values(), fromString("return 1 + ++2 * 3 / 4 >> 5"));
        assert obj.toString().equals("return 1 + ++2 * 3 / 4 >> 5");
        obj = queryParser(DetectRulesFeature.values(), fromString("return 1+++2>>!3*4/5&6+7"));
        assert obj.toString().equals("return 1 ++ +2 >> !3 * 4 / 5 & 6 + 7");
        obj = queryParser(DetectRulesFeature.values(), fromString("return 1+(++2)>>!3*4/5&6+7"));
        assert obj.toString().equals("return 1 + (++2) >> !3 * 4 / 5 & 6 + 7");
        obj = queryParser(DetectRulesFeature.values(), fromString("return 1<=0or   1==1"));
        assert obj.toString().equals("return 1 <= 0 or 1 == 1");
        obj = queryParser(DetectRulesFeature.values(), fromString("return 1 <= 0 or 1 == 1 and 3 ne 4"));
        assert obj.toString().equals("return 1 <= 0 or 1 == 1 and 3 ne 4");

        obj = queryParser(DetectRulesFeature.values(), fromString("return 1 == 1 ? true : false"));
        assert obj.toString().equals("return 1 == 1 ? true : false");
        obj = queryParser(DetectRulesFeature.values(), fromString("return 1 == 1 ? 1+(++2)>>!3*4/5&6+7 : 1<=0or 1==1"));
        assert obj.toString().equals("return 1 == 1 ? 1 + (++2) >> !3 * 4 / 5 & 6 + 7 : 1 <= 0 or 1 == 1");

        obj = queryParser(DetectRulesFeature.values(), fromString("return (1 <= 2) == true"));
        assert obj.toString().equals("return (1 <= 2) == true");
    }

    @Test
    public void expressionTest_3() {
        Object obj;
        obj = queryParser(DetectRulesFeature.values(), fromString("return @username"));
        assert obj.toString().equals("return @username");
        obj = queryParser(DetectRulesFeature.values(), fromString("return @`username`"));
        assert obj.toString().equals("return @`username`");
        obj = queryParser(DetectRulesFeature.values(), fromString("return @user.abc"));
        assert obj.toString().equals("return @user.abc");
        obj = queryParser(DetectRulesFeature.values(), fromString("return @user.`username`"));
        assert obj.toString().equals("return @user.`username`");
        obj = queryParser(DetectRulesFeature.values(), fromString("return @userNames[1]"));
        assert obj.toString().equals("return @userNames[1]");
        obj = queryParser(DetectRulesFeature.values(), fromString("return @userNames[\"name\"]"));
        assert obj.toString().equals("return @userNames[\"name\"]");
        obj = queryParser(DetectRulesFeature.values(), fromString("return @userNames[1].name"));
        assert obj.toString().equals("return @userNames[1].name");
        obj = queryParser(DetectRulesFeature.values(), fromString("return @userNames[\"name\"][\"age\"][1].aaa.ccc['aa']"));
        assert obj.toString().equals("return @userNames[\"name\"][\"age\"][1].aaa.ccc['aa']");
        obj = queryParser(DetectRulesFeature.values(), fromString("return @userNames['name']['age']"));
        assert obj.toString().equals("return @userNames['name']['age']");
        obj = queryParser(DetectRulesFeature.values(), fromString("return @userNames['name'][@abc]"));
        assert obj.toString().equals("return @userNames['name'][@abc]");
        obj = queryParser(DetectRulesFeature.values(), fromString("return @userNames['name'].aaa['xx']"));
        assert obj.toString().equals("return @userNames['name'].aaa['xx']");
        obj = queryParser(DetectRulesFeature.values(), fromString("return @userNames[#{\"name\"}][#{aaaa}][@abc].aaa.ccc['aa']"));
        assert obj.toString().equals("return @userNames[#{\"name\"}][#{aaaa}][@abc].aaa.ccc['aa']");
        obj = queryParser(DetectRulesFeature.values(), fromString("return -@abc.acc && true || (1 + 2 - -3)"));
        assert obj.toString().equals("return -@abc.acc && true || (1 + 2 - -3)");
        obj = queryParser(DetectRulesFeature.values(), fromString("return -{'c':4} not in {'a':1, 'b':2, 'c':3}"));
        assert obj.toString().equals("return -{'c': 4} not in {'a': 1, 'b': 2, 'c': 3}");
    }

    @Test
    public void functionTest_1() {
        Object obj;
        obj = queryParser(DetectRulesFeature.values(), fromString("return @username()"));
        assert obj.toString().equals("return @username()");
        obj = queryParser(DetectRulesFeature.values(), fromString("return @`username`()"));
        assert obj.toString().equals("return @`username`()");
        obj = queryParser(DetectRulesFeature.values(), fromString("return @user.abc()"));
        assert obj.toString().equals("return @user.abc()");
        obj = queryParser(DetectRulesFeature.values(), fromString("return @user.`username`()"));
        assert obj.toString().equals("return @user.`username`()");
        obj = queryParser(DetectRulesFeature.values(), fromString("return @userNames[1]()"));
        assert obj.toString().equals("return @userNames[1]()");
        obj = queryParser(DetectRulesFeature.values(), fromString("return @userNames[\"name\"]()"));
        assert obj.toString().equals("return @userNames[\"name\"]()");
        obj = queryParser(DetectRulesFeature.values(), fromString("return @userNames[1].name()"));
        assert obj.toString().equals("return @userNames[1].name()");
        obj = queryParser(DetectRulesFeature.values(), fromString("return @userNames[\"name\"][\"age\"][1].aaa.ccc['aa']()"));
        assert obj.toString().equals("return @userNames[\"name\"][\"age\"][1].aaa.ccc['aa']()");
        obj = queryParser(DetectRulesFeature.values(), fromString("return @userNames['name']['age']()"));
        assert obj.toString().equals("return @userNames['name']['age']()");
        obj = queryParser(DetectRulesFeature.values(), fromString("return @userNames['name'][@abc]()"));
        assert obj.toString().equals("return @userNames['name'][@abc]()");
        obj = queryParser(DetectRulesFeature.values(), fromString("return @userNames['name'].aaa['xx']()"));
        assert obj.toString().equals("return @userNames['name'].aaa['xx']()");
        obj = queryParser(DetectRulesFeature.values(), fromString("return @userNames[#{\"name\"}][#{aaaa}][@abc].aaa.ccc['aa']()"));
        assert obj.toString().equals("return @userNames[#{\"name\"}][#{aaaa}][@abc].aaa.ccc['aa']()");
        obj = queryParser(DetectRulesFeature.values(), fromString("return -@abc.acc() && true || (1 + 2 - -3)"));
        assert obj.toString().equals("return -@abc.acc() && true || (1 + 2 - -3)");
    }

    @Test
    public void functionTest_2() {
        Object obj;
        obj = queryParser(DetectRulesFeature.values(), fromString("return @username(123,123)"));
        assert obj.toString().equals("return @username(123, 123)");
        obj = queryParser(DetectRulesFeature.values(), fromString("return @`username`(123,123)"));
        assert obj.toString().equals("return @`username`(123, 123)");
        obj = queryParser(DetectRulesFeature.values(), fromString("return @user.abc(123, 123).abc"));
        assert obj.toString().equals("return @user.abc(123, 123).abc");
        obj = queryParser(DetectRulesFeature.values(), fromString("return @func.string.isBlank(1)"));
        assert obj.toString().equals("return @func.string.isBlank(1)");
    }

    @Test
    public void typeCastTest_1() {
        Object obj;
        obj = queryParser(DetectRulesFeature.values(), fromString("return cast(1as bool)"));
        assert obj.toString().equals("return cast(1 as bool)");
        obj = queryParser(DetectRulesFeature.values(), fromString("return cast(1as float)"));
        assert obj.toString().equals("return cast(1 as float)");
        obj = queryParser(DetectRulesFeature.values(), fromString("return cast(1as float(3))"));
        assert obj.toString().equals("return cast(1 as float(3))");
        obj = queryParser(DetectRulesFeature.values(), fromString("return cast(1as float(  3  ))"));
        assert obj.toString().equals("return cast(1 as float(3))");
        obj = queryParser(DetectRulesFeature.values(), fromString("return cast(@user.abc(123, 123).abc as float(  3  ))"));
        assert obj.toString().equals("return cast(@user.abc(123, 123).abc as float(3))");

        obj = queryParser(DetectRulesFeature.values(), fromString("return cast(1as int(12,half_even))"));
        assert obj.toString().equals("return cast(1 as int(12, half_even))");
        obj = queryParser(DetectRulesFeature.values(), fromString("return cast(1as T('yyyy-mm-dd'))"));
        assert obj.toString().equals("return cast(1 as T('yyyy-mm-dd'))");
        obj = queryParser(DetectRulesFeature.values(), fromString("return cast(@user.abc(123, 123).abc as T('yyyy-mm-dd'))"));
        assert obj.toString().equals("return cast(@user.abc(123, 123).abc as T('yyyy-mm-dd'))");
    }

    @Test
    public void statementTest_1() {
        Object obj;
        obj = queryParser(DetectRulesFeature.values(), fromString("if (((decr (decr 1)) decr) decr) decr then return true end"));
        assert obj.toString().equals("if\n" + //
                                     "    (((decr(decr1))decr)decr)decr\n" + //
                                     "then\n" + //
                                     "    return true\n" + //
                                     "end");
        obj = queryParser(DetectRulesFeature.values(), fromString("if @abc.acc && true || (1 + 2 -3)  then return \"aaa\" elseif @abc.acc && true then return 'a12' end"));
        assert obj.toString().equals("if\n" + //
                                     "    @abc.acc && true || (1 + 2 - 3)\n" +//
                                     "then\n" +//
                                     "    return \"aaa\"\n" +//
                                     "elseif\n" +//
                                     "    @abc.acc && true\n" +//
                                     "then\n" +//
                                     "    return 'a12'\n" +//
                                     "end");
        obj = queryParser(DetectRulesFeature.values(), fromString("if 1 == 1 then return true else throw 'failed.' end"));
        assert obj.toString().equals("if\n" +//
                                     "    1 == 1\n" +//
                                     "then\n" + //
                                     "    return true\n" +//
                                     "else\n" +//
                                     "    throw 'failed.'\n" +//
                                     "end");
        obj = queryParser(DetectRulesFeature.values(), fromString("if @abc.acc && true then return \"aaa\" elseif @abc.acc && true then return #{abcdefg} else return 'a12' end"));
        assert obj.toString().equals("if\n" + //
                                     "    @abc.acc && true\n" +//
                                     "then\n" + //
                                     "    return \"aaa\"\n" +//
                                     "elseif\n" + //
                                     "    @abc.acc && true\n" + //
                                     "then\n" + //
                                     "    return #{abcdefg}\n" +//
                                     "else\n" +//
                                     "    return 'a12'\n" +//
                                     "end");
    }

    @Test
    public void statementTest_2() throws IOException {
        Map<String, String> fmtOption = new HashMap<>();
        fmtOption.put(DetectRuleFmtOptions.NEW_LINE_IF_AROUND_CONDITION.getKey(), "False");

        StatementList obj;
        obj = (StatementList) queryParser(DetectRulesFeature.values(), fromString("if (((decr (decr 1)) decr) decr) decr then return true end"));
        assert toString(obj, fmtOption).equals("if (((decr(decr1))decr)decr)decr then\n" +//
                                               "    return true\n" +//
                                               "end");
        obj = (StatementList) queryParser(DetectRulesFeature
            .values(), fromString("if @abc.acc && true || (1 + 2 -3)  then return \"aaa\" elseif @abc.acc && true then return 'a12' end"));
        assert toString(obj, fmtOption).equals("if @abc.acc && true || (1 + 2 - 3) then\n" +//
                                               "    return \"aaa\"\n" +//
                                               "elseif @abc.acc && true then\n" +//
                                               "    return 'a12'\n" +//
                                               "end");
        obj = (StatementList) queryParser(DetectRulesFeature.values(), fromString("if 1 == 1 then return true else throw 'failed.' end"));
        assert toString(obj, fmtOption).equals("if 1 == 1 then\n" + //
                                               "    return true\n" +//
                                               "else\n" +//
                                               "    throw 'failed.'\n" +//
                                               "end");
        obj = (StatementList) queryParser(DetectRulesFeature
            .values(), fromString("if @abc.acc && true then return \"aaa\" elseif @abc.acc && true then return #{abcdefg} else return 'a12' end"));
        assert toString(obj, fmtOption).equals("if @abc.acc && true then\n" + //
                                               "    return \"aaa\"\n" +//
                                               "elseif @abc.acc && true then\n" + //
                                               "    return #{abcdefg}\n" +//
                                               "else\n" +//
                                               "    return 'a12'\n" +//
                                               "end");
    }

    @Test
    public void statementTest_3() throws IOException {
        Map<String, String> fmtOption = new HashMap<>();
        fmtOption.put(DetectRuleFmtOptions.NEW_LINE_DEFINE_ENUMS_ELEMENT_EMPTY.getKey(), "True");
        fmtOption.put(DetectRuleFmtOptions.WRAPPING_DEFINE_ENUMS_ELEMENT_AS_NEW_LINE.getKey(), "True");

        StatementList obj;
        obj = (StatementList) queryParser(DetectRulesFeature.values(), fromString("#   define \"abc\""));
        assert obj.toString().equals("#define \"abc\"");
        obj = (StatementList) queryParser(DetectRulesFeature.values(), fromString("#   define \"abc\" as string"));
        assert obj.toString().equals("#define \"abc\" as string");
        obj = (StatementList) queryParser(DetectRulesFeature.values(), fromString("#define 'abc' as string default   'value is abc'"));
        assert obj.toString().equals("#define 'abc' as string default 'value is abc'");
        obj = (StatementList) queryParser(DetectRulesFeature.values(), fromString("#define \"abc\" as string default \"value is abc\" enum [ \"A\",\"B\",\"C\" ]"));
        assert obj.toString().equals("#define \"abc\" as string default \"value is abc\" enum [\"A\", \"B\", \"C\"]");
        obj = (StatementList) queryParser(DetectRulesFeature.values(), fromString("#define \"abc\" as string default \"value is abc\" enum [ \"A\",\"B\",\"C\" ] hint \"这是一个参数\""));
        assert obj.toString().equals("#define \"abc\" as string default \"value is abc\" enum [\"A\", \"B\", \"C\"] hint \"这是一个参数\"");
        obj = (StatementList) queryParser(DetectRulesFeature.values(), fromString("#define \"abc\" as string default \"value is abc\" enum [  ] hint \"这是一个参数\""));
        assert obj.toString().equals("#define \"abc\" as string default \"value is abc\" enum [] hint \"这是一个参数\"");

        obj = (StatementList) queryParser(DetectRulesFeature.values(), fromString("#define \"abc\" as string default \"value is abc\" enum [  ] hint \"这是一个参数\""));
        assert toString(obj, fmtOption).equals("#define \"abc\" as string default \"value is abc\" enum [\n" +//
                                               "] hint \"这是一个参数\"");
        obj = (StatementList) queryParser(DetectRulesFeature.values(), fromString("#define \"abc\" as string default \"value is abc\" enum [ \"A\",\"B\",\"C\" ] hint \"这是一个参数\""));
        assert toString(obj, fmtOption).equals("#define \"abc\" as string default \"value is abc\" enum [\n" + //
                                               "    \"A\",\n" + //
                                               "    \"B\",\n" + //
                                               "    \"C\"\n" + //
                                               "] hint \"这是一个参数\"");
    }

    private String toString(StatementList list, Map<String, String> fmtOption) throws IOException {
        Writer strWriter = new StringWriter();
        FmtWriter writer = new FmtWriter(strWriter, fmtOption);

        list.doFormat(writer);

        return strWriter.toString();
    }
}
