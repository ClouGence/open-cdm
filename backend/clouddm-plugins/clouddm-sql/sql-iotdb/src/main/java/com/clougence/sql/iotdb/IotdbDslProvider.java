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
package com.clougence.sql.iotdb;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.ast.StatementSet;
import com.clougence.dslpaser.parse.AntlrStatementParser;
import com.clougence.dslpaser.parse.AstSplitScript;
import com.clougence.dslpaser.parse.CaseInsensitiveStream;
import com.clougence.dslpaser.parse.SyntaxErrorListener;
import com.clougence.sql.iotdb.antlr.IotdbLexer;
import com.clougence.sql.iotdb.antlr.IotdbParser;

final class IotdbDslProvider implements DslProvider, AntlrStatementParser {

    static final IotdbDslProvider TREE  = new IotdbDslProvider(true);
    static final IotdbDslProvider TABLE = new IotdbDslProvider(false);

    private final boolean         treeModel;

    private IotdbDslProvider(boolean treeModel){
        this.treeModel = treeModel;
    }

    @Override
    public String[] getDslName() { return new String[] { IotdbSqlEngineSpi.NAME }; }

    @Override
    public Lexer createLexer(CharStream charStream) {
        IotdbLexer lexer = new IotdbLexer(new CaseInsensitiveStream(charStream));
        lexer.setTreeModel(treeModel);
        lexer.removeErrorListeners();
        lexer.addErrorListener(SyntaxErrorListener.INSTANCE);
        return lexer;
    }

    @Override
    public Parser createParser(Lexer lexer) {
        Parser parser = new IotdbParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.addErrorListener(SyntaxErrorListener.INSTANCE);
        return parser;
    }

    List<? extends ParserRuleContext> statements(IotdbParser parser) {
        if (treeModel) {
            return parser.treeScript().treeStatement();
        }
        return parser.tableScript().tableStatement();
    }

    @Override
    public List<ParseTree> statementList(Lexer lexer, Parser parser) {
        return new ArrayList<>(statements((IotdbParser) parser));
    }

    @Override
    public StatementSet doParser(Lexer lexer, Parser parser) {
        throw new UnsupportedOperationException("IoTDB semantic AST formatting is outside the supported SQL scope");
    }

    @Override
    public List<AstSplitScript> doSplit(Lexer lexer, Parser parser) {
        List<AstSplitScript> result = new ArrayList<>();
        TokenStream tokens = parser.getTokenStream();
        for (ParserRuleContext statement : statements((IotdbParser) parser)) {
            Token start = scriptStart(tokens, statement.getStart());
            Token stop = scriptStop(tokens, statement.getStop());
            String script = tokens.getText(start, stop);
            String[] lines = script.split("\\n", -1);
            int endLine = start.getLine() + lines.length - 1;
            int endColumn = lines[lines.length - 1].length();
            if (lines.length == 1) {
                endColumn += start.getCharPositionInLine();
            }
            result.add(AstSplitScript.builder()
                .script(script)
                .astTree(statement)
                .parser(parser)
                .lexer(lexer)
                .startCodeLine(start.getLine())
                .startCodeColumn(start.getCharPositionInLine())
                .bodyStartCodeLine(start.getLine())
                .bodyStartCodeColumn(start.getCharPositionInLine())
                .endCodeLine(endLine)
                .endCodeColumn(endColumn)
                .build());
        }
        return result;
    }

    @Override
    public void doVisitor(Lexer lexer, Parser parser, AbstractParseTreeVisitor<?> visitor) {
        statements((IotdbParser) parser).forEach(visitor::visit);
    }

    @Override
    public String getTextKeepComment(TokenStream tokens, ParseTree lastTree, Token start, Token stop) {
        return tokens.getText(scriptStart(tokens, start), scriptStop(tokens, stop));
    }

    private Token scriptStart(TokenStream tokens, Token start) {
        for (int i = start.getTokenIndex() - 1; i >= 0; i--) {
            Token token = tokens.get(i);
            if (token.getChannel() == Token.DEFAULT_CHANNEL) {
                break;
            }
            if (token.getType() != IotdbLexer.WS) {
                start = token;
            }
        }
        return start;
    }

    private Token scriptStop(TokenStream tokens, Token stop) {
        for (int i = stop.getTokenIndex() + 1; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            if (token.getType() == IotdbLexer.SEMI) {
                return token;
            }
            if (token.getChannel() == Token.DEFAULT_CHANNEL) {
                break;
            }
            if (token.getType() != IotdbLexer.WS) {
                stop = token;
            }
        }
        return stop;
    }
}
