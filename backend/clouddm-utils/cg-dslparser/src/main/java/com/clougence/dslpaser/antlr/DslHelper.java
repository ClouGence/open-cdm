/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.dslpaser.antlr;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;

import com.clougence.dslpaser.ast.StatementSet;
import com.clougence.dslpaser.ast.location.CodeLocation;
import com.clougence.dslpaser.parse.AntlrParseTreeVisitorCreator;
import com.clougence.dslpaser.parse.AstSplitScript;
import com.clougence.dslpaser.parse.SyntaxErrorListener;
import com.clougence.utils.StringUtils;
import com.clougence.utils.ref.LinkedCaseInsensitiveMap;

public final class DslHelper {

    private static final Map<String, DslProvider> PROVIDER_MAP = new LinkedCaseInsensitiveMap<>();

    public static void register(DslProvider provider) {
        for (String dslName : provider.getDslName()) {
            PROVIDER_MAP.put(dslName, provider);
        }
    }

    public static void register(String dslName, DslProvider provider) {
        PROVIDER_MAP.put(dslName, provider);
    }

    public static DslProvider getProvider(String dslName) {
        return PROVIDER_MAP.get(dslName);
    }

    //

    public static StatementSet parserDsl(String dslName, String queryString) {
        if (!PROVIDER_MAP.containsKey(dslName)) {
            throw new UnsupportedOperationException("DSL '" + dslName + "' Unsupported.");
        }

        return parserDsl(PROVIDER_MAP.get(dslName), queryString);
    }

    public static StatementSet parserDsl(DslProvider provider, String queryString) {
        try {
            Lexer lexer = provider.createLexer(CharStreams.fromReader(new StringReader(queryString)));
            lexer.removeErrorListeners();
            lexer.addErrorListener(SyntaxErrorListener.INSTANCE);

            Parser parser = provider.createParser(lexer);
            parser.removeErrorListeners();
            parser.addErrorListener(SyntaxErrorListener.INSTANCE);
            return provider.doParser(lexer, parser);
        } catch (IOException e) {
            throw new SyntaxIoException(e);
        }
    }

    public static List<AstSplitScript> splitDsl(String dslName, String queryString) {
        if (!PROVIDER_MAP.containsKey(dslName)) {
            throw new UnsupportedOperationException("DSL '" + dslName + "' Unsupported.");
        }

        return splitDsl(PROVIDER_MAP.get(dslName), queryString);
    }

    public static List<AstSplitScript> splitDsl(DslProvider provider, String queryString) {
        try {
            Lexer lexer = provider.createLexer(CharStreams.fromReader(new StringReader(queryString)));
            lexer.removeErrorListeners();
            lexer.addErrorListener(SyntaxErrorListener.INSTANCE);

            Parser parser = provider.createParser(lexer);
            parser.removeErrorListeners();
            parser.addErrorListener(SyntaxErrorListener.INSTANCE);

            return provider.doSplit(lexer, parser);
        } catch (IOException e) {
            throw new SyntaxIoException(e);
        }
    }

    public static List<AstSplitScript> splitDsl(String dslName, String queryString, CodeLocation base) {
        if (!PROVIDER_MAP.containsKey(dslName)) {
            throw new UnsupportedOperationException("DSL '" + dslName + "' Unsupported.");
        }

        return splitDsl(PROVIDER_MAP.get(dslName), queryString, base);
    }

    public static List<AstSplitScript> splitDsl(DslProvider provider, String queryString, CodeLocation base) {
        // offset for line and column numbers
        int lineNumber = Math.max(1, base == null ? 1 : base.getLineNumber());
        int columnNumber = Math.max(0, base == null ? 0 : base.getColumnNumber());

        int curPoint = 0;
        int curLine = lineNumber;
        int curColumn = columnNumber;

        List<AstSplitScript> scripts = splitDsl(provider, queryString);
        for (AstSplitScript ass : scripts) {
            String script = ass.getScript();
            int startCodeLine = curLine;
            int startCodeColumn = curColumn;
            int endCodeLine = curLine;
            int endCodeColumn = curColumn;

            int scriptIdx = queryString.indexOf(script, curPoint);

            // before script
            String beforeScript = queryString.substring(curPoint, scriptIdx);
            if (!beforeScript.isEmpty()) {
                int lines = StringUtils.countMatches(beforeScript, "\n");
                if (lines > 0) {
                    startCodeLine += lines;
                    endCodeLine += lines;

                    int lastIndex = StringUtils.lastIndexOf(beforeScript, "\n");
                    String lastLine = beforeScript.substring(lastIndex + 1);
                    startCodeColumn = lastLine.length();
                } else {
                    startCodeColumn += beforeScript.length();
                    endCodeColumn += beforeScript.length();
                }
            }

            // line and column numbers
            int lines = StringUtils.countMatches(script, "\n");
            if (lines > 0) {
                endCodeLine += lines;

                int lastIndex = StringUtils.lastIndexOf(script, "\n");
                String lastLine = script.substring(lastIndex + 1);
                endCodeColumn = lastLine.length();
            } else {
                endCodeColumn += script.length();
            }

            // update current point
            curPoint += (beforeScript.length() + script.length());
            curLine = endCodeLine;
            curColumn = endCodeColumn;

            // result
            ass.setStartCodeLine(startCodeLine);
            ass.setStartCodeColumn(startCodeColumn);
            ass.setEndCodeLine(endCodeLine);
            ass.setEndCodeColumn(endCodeColumn);
        }
        return scripts;
    }

    public static void doVisitor(String dslName, String queryString, AntlrParseTreeVisitorCreator visitor) {
        if (!PROVIDER_MAP.containsKey(dslName)) {
            throw new UnsupportedOperationException("DSL '" + dslName + "' Unsupported.");
        }

        doVisitor(PROVIDER_MAP.get(dslName), queryString, visitor);
    }

    public static void doVisitor(DslProvider provider, String queryString, AntlrParseTreeVisitorCreator visitor) {
        try {
            Lexer lexer = provider.createLexer(CharStreams.fromReader(new StringReader(queryString)));
            lexer.removeErrorListeners();
            lexer.addErrorListener(SyntaxErrorListener.INSTANCE);

            Parser parser = provider.createParser(lexer);
            parser.removeErrorListeners();
            parser.addErrorListener(SyntaxErrorListener.INSTANCE);

            provider.doVisitor(lexer, parser, visitor.createVisitor(lexer, parser));
        } catch (IOException e) {
            throw new SyntaxIoException(e);
        }
    }

}
