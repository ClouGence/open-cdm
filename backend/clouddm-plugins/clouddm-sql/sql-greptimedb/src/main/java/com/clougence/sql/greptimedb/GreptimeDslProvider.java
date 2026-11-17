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

package com.clougence.sql.greptimedb;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.ast.StatementSet;
import com.clougence.dslpaser.parse.AntlrStatementParser;
import com.clougence.dslpaser.parse.AstSplitScript;
import com.clougence.dslpaser.parse.CaseInsensitiveStream;
import com.clougence.sql.greptimedb.antlr.GreptimeSqlLexer;
import com.clougence.sql.greptimedb.antlr.GreptimeSqlParser;

final class GreptimeDslProvider implements DslProvider, AntlrStatementParser {

    static final GreptimeDslProvider INSTANCE = new GreptimeDslProvider();

    private GreptimeDslProvider(){
    }

    static String identifier(ParserRuleContext context) {
        Token token = context.getStart();
        String text = token.getText();
        if (token.getType() == GreptimeSqlLexer.QUOTED_IDENTIFIER) {
            return text.substring(1, text.length() - 1).replace("``", "`");
        }
        if (token.getType() != GreptimeSqlLexer.STRING_LITERAL && token.getType() != GreptimeSqlLexer.DOUBLE_QUOTED_STRING) {
            return text.toLowerCase(Locale.ROOT);
        }

        char quote = text.charAt(0);
        StringBuilder name = new StringBuilder(text.length() - 2);
        for (int i = 1; i < text.length() - 1; i++) {
            char ch = text.charAt(i);
            if (ch == quote) {
                i++;
            } else if (ch == '\\') {
                ch = text.charAt(++i);
                if (ch == '%' || ch == '_') {
                    name.append('\\');
                }
                ch = switch (ch) {
                    case '0' -> '\0';
                    case 'a' -> '\u0007';
                    case 'b' -> '\b';
                    case 'f' -> '\f';
                    case 'n' -> '\n';
                    case 'r' -> '\r';
                    case 't' -> '\t';
                    case 'Z' -> '\u001a';
                    default -> ch;
                };
            }
            name.append(ch);
        }
        return name.toString();
    }

    @Override
    public String[] getDslName() { return new String[] { GreptimeSqlEngineSpi.NAME }; }

    @Override
    public Lexer createLexer(CharStream source) {
        return new GreptimeSqlLexer(new CaseInsensitiveStream(source));
    }

    @Override
    public Parser createParser(Lexer lexer) {
        var parser = new GreptimeSqlParser(new CommonTokenStream(lexer));
        parser.addParseListener(new GreptimeCreateTableValidator(parser));
        return parser;
    }

    @Override
    public List<ParseTree> statementList(Lexer lexer, Parser parser) {
        return new ArrayList<>(((GreptimeSqlParser) parser).script().statement());
    }

    @Override
    public StatementSet doParser(Lexer lexer, Parser parser) {
        throw new UnsupportedOperationException("GreptimeDB SQL semantic AST is unsupported");
    }

    @Override
    public List<AstSplitScript> doSplit(Lexer lexer, Parser parser) {
        List<AstSplitScript> result = new ArrayList<>();
        TokenStream tokens = parser.getTokenStream();
        for (var statement : ((GreptimeSqlParser) parser).script().statement()) {
            Token start = statement.getStart();
            result.add(AstSplitScript.builder()
                .script(getTextKeepComment(tokens, null, start, statement.getStop()))
                .astTree(statement)
                .parser(parser)
                .lexer(lexer)
                .bodyStartCodeLine(start.getLine())
                .bodyStartCodeColumn(start.getCharPositionInLine())
                .build());
        }
        return result;
    }

    @Override
    public void doVisitor(Lexer lexer, Parser parser, AbstractParseTreeVisitor<?> visitor) {
        ((GreptimeSqlParser) parser).script().statement().forEach(visitor::visit);
    }

    @Override
    public String getTextKeepComment(TokenStream tokens, ParseTree lastTree, Token start, Token stop) {
        for (int i = start.getTokenIndex() - 1; i >= 0; i--) {
            Token token = tokens.get(i);
            if (token.getChannel() == Token.DEFAULT_CHANNEL) {
                break;
            }
            if (token.getType() != GreptimeSqlLexer.WS) {
                start = token;
            }
        }

        for (int i = stop.getTokenIndex() + 1; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            if (token.getType() == GreptimeSqlLexer.SEMI) {
                stop = token;
                break;
            }
            if (token.getChannel() == Token.DEFAULT_CHANNEL) {
                break;
            }
            if (token.getType() != GreptimeSqlLexer.WS) {
                stop = token;
            }
        }
        return tokens.getText(start, stop);
    }
}
