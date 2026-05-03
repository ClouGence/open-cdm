package com.clougence.dslparser.detectrule;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;

import com.clougence.detectrule.parser.DetectRuleHelper;
import com.clougence.detectrule.parser.DetectRulesFeature;
import com.clougence.detectrule.parser.antlr.DefaultDetectRulesVisitor;
import com.clougence.detectrule.parser.antlr.DetectRulesLexer;
import com.clougence.detectrule.parser.antlr.DetectRulesParser;
import com.clougence.detectrule.parser.ast.statement.StatementList;
import com.clougence.dslpaser.antlr.ThrowingListener;
import com.clougence.utils.ResourcesUtils;

public class DetectRulesFeatureTest {

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

    public static DetectRulesParser.RootContext queryParserAst(DetectRulesFeature[] featureArrays, CharStream charStream) {
        List<DetectRulesFeature> features = Arrays.asList(featureArrays);

        DetectRulesLexer lexer = new DetectRulesLexer(charStream, features);
        lexer.removeErrorListeners();
        lexer.addErrorListener(ThrowingListener.INSTANCE);

        DetectRulesParser qlParser = new DetectRulesParser(new CommonTokenStream(lexer), features);
        qlParser.removeErrorListeners();
        qlParser.addErrorListener(ThrowingListener.INSTANCE);

        return qlParser.root();
    }

    public static CharStream fromString(String script) {
        return CharStreams.fromString(script);
    }

    public static CharStream fromResource(String script) throws IOException {
        return CharStreams.fromStream(ResourcesUtils.getResourceAsStream(script));
    }

    @Test
    public void fullFeaturedScriptTest() throws IOException {
        queryParser(DetectRulesFeature.values(), fromResource("full-featured/data_representation.txt"));
        queryParser(DetectRulesFeature.values(), fromResource("full-featured/expression_1.txt"));
        queryParser(DetectRulesFeature.values(), fromResource("full-featured/expression_2_lowercase.txt"));
        queryParser(DetectRulesFeature.values(), fromResource("full-featured/expression_2_uppercase.txt"));
        queryParser(DetectRulesFeature.values(), fromResource("full-featured/expression_3.txt"));
        queryParser(DetectRulesFeature.values(), fromResource("full-featured/expression_cast.txt"));
        queryParser(DetectRulesFeature.values(), fromResource("full-featured/statement.txt"));
    }

    @Test
    public void simplestFeaturedScriptTest() throws IOException {
        DetectRulesFeature[] features = new DetectRulesFeature[0];
        Object obj1 = queryParser(features, fromResource("simplest/data_representation.txt"));
        Object obj2 = queryParser(features, fromResource("simplest/expression_1.txt"));
        Object obj3 = queryParser(features, fromResource("simplest/expression_3.txt"));
        //queryParser(features, fromResource("simplest/expression_cast.txt"));
        Object obj4 = queryParser(features, fromResource("simplest/statement.txt"));

        try {
            queryParser(features, fromString("return minus1plus minus1"));
            assert false;
        } catch (Exception e) {
            assert true;
        }
    }

    @Test
    public void continuousUnarySymbolTest() throws IOException {
        try {
            queryParser(DetectRulesFeature.values(), fromString("return ++ ++1"));
            assert false;
        } catch (Exception e) {
            assert true;
        }

        try {
            queryParser(DetectRulesFeature.values(), fromString("return 1-- --"));
            assert false;
        } catch (Exception e) {
            assert true;
        }

        try {
            queryParser(DetectRulesFeature.values(), fromString("return ++ ++1-- --"));
            assert false;
        } catch (Exception e) {
            assert true;
        }

        try {
            queryParser(DetectRulesFeature.values(), fromString("return ((-- (-- 1) ++) ++) ++"));
            assert false;
        } catch (Exception e) {
            queryParser(DetectRulesFeature.values(), fromString("return (((-- (-- 1)) ++) ++) ++"));
            assert true;
        }

        try {
            queryParser(DetectRulesFeature.values(), fromString("return + + + + +1"));
            assert false;
        } catch (Exception e) {
            queryParser(DetectRulesFeature.values(), fromString("return +( + (+ (+ +1)))"));
            assert true;
        }

        try {
            queryParser(DetectRulesFeature.values(), fromString("return !!!!true"));
            assert false;
        } catch (Exception e) {
            queryParser(DetectRulesFeature.values(), fromString("return !(!(!(!true)))"));
            assert true;
        }
    }

    @Test
    public void extractParametersTest() {
        Object obj;
        obj = queryParser(DetectRulesFeature.values(), fromString("#define 'abc' as string default 'value is abc' enum [ '1.','2' ] hint '这是一个参数'"));
        assert DetectRuleHelper.extractParameters((StatementList) obj).size() == 1;

        obj = queryParser(DetectRulesFeature.values(), fromString("return #{abc} + #{bcd}"));
        assert DetectRuleHelper.extractParameters((StatementList) obj).size() == 2;

        obj = queryParser(DetectRulesFeature.values(), fromString("return #{abc} + #{abc}"));
        assert DetectRuleHelper.extractParameters((StatementList) obj).size() == 1;

        obj = queryParser(DetectRulesFeature.values(), fromString("#define 'abc' as string default 'value is abc' enum [ '1.','2' ] hint '这是一个参数' \nreturn #{abc} + #{abc}"));
        assert DetectRuleHelper.extractParameters((StatementList) obj).size() == 1;
    }

    @Test
    public void main() throws IOException {
        Object obj1 = queryParser(DetectRulesFeature.values(), fromString("return @isNotBlank[12].aa"));
        Object obj2 = queryParser(DetectRulesFeature.values(), fromString("return @isNotBlank(cast(@userName as string))"));
        Object obj3 = queryParser(DetectRulesFeature.values(), fromString("return @abc.acc && true || (1 + 2 - 3)"));
        Object obj4 = queryParser(DetectRulesFeature.values(), fromString("assert 1 > 2, \"assert failed.\""));
        Object obj5 = queryParser(DetectRulesFeature.values(), fromString("return cast(1 as T('yyyy-mm-dd'))"));
        Object obj6 = queryParser(DetectRulesFeature.values(), fromString("return cast(1as float(1))"));
        Object obj7 = queryParser(DetectRulesFeature.values(), fromString("return not true"));
    }
}
