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
package com.clougence.sql.hana.parser;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.sql.hana.HanaSqlEngineSpi;
import com.clougence.sql.hana.parser.antlr.HanaLexer;
import com.clougence.sql.hana.parser.antlr.HanaParser;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.ast.StatementSet;
import com.clougence.dslpaser.parse.AntlrStatementParser;
import com.clougence.dslpaser.parse.AstSplitScript;
import com.clougence.dslpaser.parse.CaseInsensitiveStream;
import com.clougence.dslpaser.parse.SyntaxErrorListener;

public final class HanaDslProvider implements DslProvider {

    public static final DslProvider    INSTANCE    = new HanaDslProvider();
    private static final AntlrStatementParser TREE_PARSER = new HanaStatementParser();

    private HanaDslProvider() {}

    @Override
    public String[] getDslName() { return new String[] { HanaSqlEngineSpi.NAME }; }

    @Override
    public Lexer createLexer(CharStream charStream) {
        Lexer lexer = new HanaLexer(new CaseInsensitiveStream(charStream));
        lexer.removeErrorListeners();
        lexer.addErrorListener(SyntaxErrorListener.INSTANCE);
        return lexer;
    }

    @Override
    public Parser createParser(Lexer lexer) {
        Parser parser = new HanaParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.addErrorListener(SyntaxErrorListener.INSTANCE);
        return parser;
    }

    @Override
    public StatementSet doParser(Lexer lexer, Parser parser) {
        throw new UnsupportedOperationException("HANA semantic AST is not supported by the structural grammar.");
    }

    @Override
    public List<AstSplitScript> doSplit(Lexer lexer, Parser parser) {
        TokenStream tokenStream = parser.getTokenStream();
        List<ParseTree> astList = TREE_PARSER.statementList(lexer, parser);

        List<AstSplitScript> result = new ArrayList<>();
        ParseTree lastTree = null;
        for (ParseTree parseTree : astList) {
            ParserRuleContext context = (ParserRuleContext) parseTree;
            Token startToken = context.getStart();
            Token stopToken = context.getStop();

            result.add(AstSplitScript.builder()
                .script(TREE_PARSER.getTextKeepComment(tokenStream, lastTree, startToken, stopToken))
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
        List<ParseTree> astList = TREE_PARSER.statementList(lexer, parser);
        for (ParseTree astTree : astList) {
            visitor.visit(astTree);
        }
    }
}
