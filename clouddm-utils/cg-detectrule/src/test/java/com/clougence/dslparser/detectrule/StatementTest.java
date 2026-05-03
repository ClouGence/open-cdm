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

public class StatementTest {

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
    public void statementTest_3() throws IOException {
        Map<String, String> fmtOption = new HashMap<>();
        fmtOption.put(DetectRuleFmtOptions.NEW_LINE_DEFINE_ENUMS_ELEMENT_EMPTY.getKey(), "True");
        fmtOption.put(DetectRuleFmtOptions.WRAPPING_DEFINE_ENUMS_ELEMENT_AS_NEW_LINE.getKey(), "True");

        StatementList obj;
        obj = (StatementList) queryParser(DetectRulesFeature.values(), fromString("if\n" +//
                                                                                  "    @domain.sqlType not in ['CREATE_SCHEMA', 'ALERT_SCHEMA'] and\n" +//
                                                                                  "    @domain.ddlKind not in ['CREATE', 'ALERT']\n" + //
                                                                                  "then\n" +//
                                                                                  "    return true\n" +//
                                                                                  "end"));
        assert obj.toString()
            .equals("if\n" + "    @domain.sqlType not in ['CREATE_SCHEMA', 'ALERT_SCHEMA'] and @domain.ddlKind not in ['CREATE', 'ALERT']\n" + "then\n" + "    return true\n"
                    + "end");
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

        obj = (StatementList) queryParser(DetectRulesFeature.values(), fromString("abc = @domain.aac"));
        assert toString(obj, fmtOption).equals("abc = @domain.aac");
    }

    private String toString(StatementList list, Map<String, String> fmtOption) throws IOException {
        Writer strWriter = new StringWriter();
        FmtWriter writer = new FmtWriter(strWriter, fmtOption);

        list.doFormat(writer);

        return strWriter.toString();
    }
}
