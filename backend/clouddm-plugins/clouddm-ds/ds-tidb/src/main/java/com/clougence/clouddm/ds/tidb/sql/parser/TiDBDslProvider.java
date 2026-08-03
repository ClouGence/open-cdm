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
package com.clougence.clouddm.ds.tidb.sql.parser;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.tidb.sql.TiSqlEngineSpi;
import com.clougence.clouddm.ds.tidb.sql.parser.antlr.TiDBLexer;
import com.clougence.clouddm.ds.tidb.sql.parser.antlr.TiDBParser;
import com.clougence.dslpaser.antlr.AntlerSyntaxException;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.ast.StatementSet;
import com.clougence.dslpaser.parse.AntlrStatementParser;
import com.clougence.dslpaser.parse.AstSplitScript;

public class TiDBDslProvider implements DslProvider {

    private final AntlrStatementParser TREE_PARSER = new TiDBStatementParser();
    private final TiDBParserConfig     config;
    private final boolean              noBackslashEscapesFallback;

    public TiDBDslProvider(TiDBParserConfig config){
        this(config, false);
    }

    private TiDBDslProvider(TiDBParserConfig config, boolean noBackslashEscapesFallback){
        this.config = config;
        this.noBackslashEscapesFallback = noBackslashEscapesFallback;
    }

    public TiDBVersion version() {
        return config.version();
    }

    public int exactVersion() {
        return config.exactVersion();
    }

    public TiDBParserConfig config() {
        return config;
    }

    @Override
    public String[] getDslName() { return new String[] { TiSqlEngineSpi.NAME }; }

    @Override
    public Lexer createLexer(CharStream charStream) {
        TiDBLexer lexer = new TiDBLexer(charStream);
        lexer.setConfig(config);
        ATN atn = lexer.getATN();
        lexer.setInterpreter(new LexerATNSimulator(lexer, atn, isolatedDecisionDfa(atn), new PredictionContextCache()));
        return lexer;
    }

    @Override
    public Parser createParser(Lexer lexer) {
        TiDBParser parser = new TiDBParser(new CommonTokenStream(lexer));
        parser.setConfig(config);
        ATN atn = parser.getATN();
        parser.setInterpreter(new ParserATNSimulator(parser, atn, isolatedDecisionDfa(atn), new PredictionContextCache()));
        return parser;
    }

    private static DFA[] isolatedDecisionDfa(ATN atn) {
        DFA[] decisionToDfa = new DFA[atn.getNumberOfDecisions()];
        for (int i = 0; i < decisionToDfa.length; i++) {
            decisionToDfa[i] = new DFA(atn.getDecisionState(i), i);
        }
        return decisionToDfa;
    }

    protected AntlrStatementParser treeParser() {
        return TREE_PARSER;
    }

    @Override
    public StatementSet doParser(Lexer lexer, Parser parser) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<AstSplitScript> doSplit(Lexer lexer, Parser parser) {
        try {
            return split(lexer, parser);
        } catch (AntlerSyntaxException firstFailure) {
            if (config.sqlModeKnown() || noBackslashEscapesFallback) {
                throw firstFailure;
            }
            try {
                return DslHelper.splitDsl(withNoBackslashEscapesFallback(), sourceText(lexer));
            } catch (RuntimeException fallbackFailure) {
                firstFailure.addSuppressed(fallbackFailure);
                throw firstFailure;
            }
        }
    }

    private List<AstSplitScript> split(Lexer lexer, Parser parser) {
        TokenStream tokenStream = parser.getTokenStream();
        List<ParseTree> astList = this.treeParser().statementList(lexer, parser);

        List<AstSplitScript> result = new ArrayList<>();
        ParseTree lastTree = null;
        for (ParseTree parseTree : astList) {
            ParserRuleContext context = (ParserRuleContext) parseTree;
            Token startToken = context.getStart();
            Token stopToken = context.getStop();

            result.add(AstSplitScript.builder()
                .script(this.treeParser().getTextKeepComment(tokenStream, lastTree, startToken, stopToken))
                .astTree(parseTree)
                .parser(parser)
                .lexer(lexer)
                .bodyStartCodeLine(startToken.getLine())
                .bodyStartCodeColumn(startToken.getCharPositionInLine())
                .build());
            lastTree = parseTree;
        }
        return result;
    }

    @Override
    public void doVisitor(Lexer lexer, Parser parser, AbstractParseTreeVisitor<?> visitor) {
        try {
            visit(lexer, parser, visitor);
        } catch (AntlerSyntaxException firstFailure) {
            if (config.sqlModeKnown() || noBackslashEscapesFallback) {
                throw firstFailure;
            }
            try {
                for (AstSplitScript script : DslHelper.splitDsl(withNoBackslashEscapesFallback(), sourceText(lexer))) {
                    visitor.visit(script.getAstTree());
                }
            } catch (RuntimeException fallbackFailure) {
                firstFailure.addSuppressed(fallbackFailure);
                throw firstFailure;
            }
        }
    }

    private void visit(Lexer lexer, Parser parser, AbstractParseTreeVisitor<?> visitor) {
        for (ParseTree astTree : this.treeParser().statementList(lexer, parser)) {
            visitor.visit(astTree);
        }
    }

    private TiDBDslProvider withNoBackslashEscapesFallback() {
        return new TiDBDslProvider(config.withFeature(TiDBParserFeature.NO_BACKSLASH_ESCAPES), true);
    }

    private static String sourceText(Lexer lexer) {
        CharStream input = lexer.getInputStream();
        return input.size() == 0 ? "" : input.getText(Interval.of(0, input.size() - 1));
    }
}
