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
package com.clougence.sql.common.parser;

import java.util.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.*;

import com.clougence.clouddm.sdk.execute.session.QueryArg;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.sql.parser.SplitAnalysisSpi;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.ast.location.CodeLocation;
import com.clougence.dslpaser.parse.AntlrStatementParser;
import com.clougence.dslpaser.parse.SyntaxErrorListener;

public abstract class AbstractSplitAnalysisSpi implements SplitAnalysisSpi {

    protected abstract DslProvider dslProvider();

    protected abstract AbstractParseTreeVisitor<SecQueryType> splitVisitor();

    protected abstract void parseRoot(Parser parser);

    protected abstract boolean isStatementContext(ParserRuleContext context);

    protected abstract AntlrStatementParser statementParser();

    protected SecQueryType normalizeType(SecQueryType type, String script) {
        return type == null ? SecQueryType.UNKNOWN : type;
    }

    protected Set<SecQueryType> collectTypes(ParserRuleContext context, String script) {
        Set<SecQueryType> types = new LinkedHashSet<>();
        types.add(normalizeType(context.accept(splitVisitor()), script));
        collectAdditionalTypes(context, types);
        return types;
    }

    protected List<SplitScript> collectChildren(ParserRuleContext context, CommonTokenStream tokens) {
        return Collections.emptyList();
    }

    protected final SplitScript createChild(ParserRuleContext context, CommonTokenStream tokens, Set<SecQueryType> types, List<SplitScript> children) {
        String script = tokens.getText(context.getStart(), context.getStop());
        SplitScript split = new SplitScript();
        split.setScript(script);
        split.setType(types);
        split.setChildren(children);
        split.setBodyStartCodeLine(context.getStart().getLine());
        split.setBodyStartCodeColumn(context.getStart().getCharPositionInLine());

        int endLine = context.getStart().getLine();
        int endColumn = context.getStart().getCharPositionInLine();
        for (int i = 0; i < script.length(); i++) {
            if (script.charAt(i) == '\n') {
                endLine++;
                endColumn = 0;
            } else {
                endColumn++;
            }
        }
        split.setBodyEndCodeLine(endLine);
        split.setBodyEndCodeColumn(endColumn);
        return split;
    }

    protected SecQueryType additionalType(ParseTree tree) {
        return null;
    }

    private void collectAdditionalTypes(ParseTree tree, Set<SecQueryType> types) {
        SecQueryType type = additionalType(tree);
        if (type != null) {
            types.add(type);
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            collectAdditionalTypes(tree.getChild(i), types);
        }
    }

    @Override
    public List<SplitScript> splitScript(String script, List<QueryArg> args, int baseLine, int baseColumn) {
        return lightweightSplit(script, baseLine, baseColumn);
    }

    private List<SplitScript> lightweightSplit(String script, int baseLine, int baseColumn) {
        DslProvider provider = dslProvider();
        Lexer lexer = provider.createLexer(CharStreams.fromString(script));
        lexer.removeErrorListeners();
        lexer.addErrorListener(SyntaxErrorListener.INSTANCE);

        Parser parser = provider.createParser(lexer);
        parser.removeErrorListeners();
        parser.addErrorListener(SyntaxErrorListener.INSTANCE);
        parser.setBuildParseTree(true);
        isolatePredictionCaches(parser);

        if (!(parser.getTokenStream() instanceof CommonTokenStream tokens)) {
            throw new IllegalStateException("split requires CommonTokenStream");
        }
        tokens.fill();

        List<SplitScript> result = new ArrayList<>();
        parser.addParseListener(new SplitListener(tokens, new LocationCursor(script, new CodeLocation(baseLine, baseColumn)), result));
        parseRoot(parser);
        return result;
    }

    protected static void isolatePredictionCaches(Parser parser) {
        ATN atn = parser.getATN();
        DFA[] decisionToDfa = new DFA[atn.getNumberOfDecisions()];
        for (int i = 0; i < decisionToDfa.length; i++) {
            decisionToDfa[i] = new DFA(atn.getDecisionState(i), i);
        }
        parser.setInterpreter(new ParserATNSimulator(parser, atn, decisionToDfa, new PredictionContextCache()));
    }

    private final class SplitListener implements ParseTreeListener {

        private final CommonTokenStream tokens;
        private final LocationCursor    location;
        private final List<SplitScript> result;
        private ParserRuleContext       lastStatement;

        private SplitListener(CommonTokenStream tokens, LocationCursor location, List<SplitScript> result){
            this.tokens = tokens;
            this.location = location;
            this.result = result;
        }

        @Override
        public void visitTerminal(TerminalNode node) {
        }

        @Override
        public void visitErrorNode(ErrorNode node) {
        }

        @Override
        public void enterEveryRule(ParserRuleContext ctx) {
        }

        @Override
        public void exitEveryRule(ParserRuleContext ctx) {
            if (!isStatementContext(ctx)) {
                return;
            }

            Token startToken = ctx.getStart();
            Token stopToken = ctx.getStop();
            String script = statementParser().getTextKeepComment(this.tokens, this.lastStatement, startToken, stopToken);
            ScriptLocation scriptLocation = this.location.locate(script);

            SplitScript split = new SplitScript();
            split.setScript(script);
            split.setType(collectTypes(ctx, script));
            split.setChildren(collectChildren(ctx, this.tokens));
            split.setBodyStartCodeLine(startToken.getLine());
            split.setBodyStartCodeColumn(startToken.getCharPositionInLine());
            split.setBodyEndCodeLine(scriptLocation.endLine());
            split.setBodyEndCodeColumn(scriptLocation.endColumn());
            this.result.add(split);

            ParserRuleContext parent = ctx.getParent();
            if (parent != null && parent.children != null) {
                parent.children.remove(ctx);
            }
            this.lastStatement = ctx;
        }
    }

    private static final class LocationCursor {

        private final String source;
        private int          sourceOffset;
        private int          line;
        private int          column;

        private LocationCursor(String source, CodeLocation base){
            this.source = source;
            this.line = Math.max(1, base == null ? 1 : base.getLineNumber());
            this.column = Math.max(0, base == null ? 0 : base.getColumnNumber());
        }

        private ScriptLocation locate(String script) {
            int scriptOffset = this.source.indexOf(script, this.sourceOffset);
            if (scriptOffset < 0) {
                throw new IllegalStateException("Split script is not part of its source");
            }

            advance(this.source, this.sourceOffset, scriptOffset);
            advance(script, 0, script.length());
            this.sourceOffset = scriptOffset + script.length();
            return new ScriptLocation(this.line, this.column);
        }

        private void advance(String value, int start, int end) {
            for (int i = start; i < end; i++) {
                if (value.charAt(i) == '\n') {
                    this.line++;
                    this.column = 0;
                } else {
                    this.column++;
                }
            }
        }
    }

    private record ScriptLocation(int endLine, int endColumn) {
    }
}
