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

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.tidb.sql.parser.antlr.TiDBLexer;
import com.clougence.clouddm.ds.tidb.sql.parser.antlr.TiDBParser;
import com.clougence.dslpaser.parse.AntlrStatementParser;

public class TiDBStatementParser implements AntlrStatementParser {

    @Override
    public List<ParseTree> statementList(Lexer lexer, Parser parser) {
        List<ParseTree> result = new ArrayList<>();
        List<ParseTree> children = ((TiDBParser) parser).root().children;
        for (ParseTree child : children) {
            if (child instanceof TiDBParser.SqlStatementsContext) {
                for (ParseTree parseTree : ((TiDBParser.SqlStatementsContext) child).children) {
                    if (parseTree instanceof TiDBParser.SqlStatementContext) {
                        result.add(parseTree);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public String getTextKeepComment(TokenStream tokens, ParseTree lastTree, Token startToken, Token endToken) {
        // ANTLR fires the exit listener before every generated alternative has
        // assigned ParserRuleContext#stop. The token stream is already parked
        // on the next token, so the preceding token is the actual statement
        // end. This also avoids hiding a syntax error with an unrelated NPE.
        if (endToken == null) {
            int fallbackIndex = Math.max(startToken.getTokenIndex(), tokens.index() - 1);
            endToken = tokens.get(fallbackIndex);
        }
        for (int i = startToken.getTokenIndex() - 1; i >= 0; i--) {
            Token start = tokens.get(i);
            if (start.getType() == TiDBLexer.SPACE) {
                // ignore
            } else if (start.getType() == TiDBLexer.SEMI) {
                if (start.getChannel() == Token.DEFAULT_CHANNEL) {
                    break;
                }
                startToken = start;
            } else if (isComment(start)) {
                startToken = start;
            } else {
                break;
            }
        }

        for (int i = endToken.getTokenIndex() + 1; i < tokens.size(); i++) {
            Token end = tokens.get(i);
            if (end.getType() == TiDBLexer.SPACE) {
                //ignore
            } else if (end.getType() == TiDBLexer.SEMI) {
                endToken = end;
                if (end.getChannel() == Token.DEFAULT_CHANNEL) {
                    break;
                }
            } else if (isComment(end)) {
                endToken = end;
            } else {
                break;
            }
        }

        return tokens.getText(startToken, endToken);
    }

    private static boolean isComment(Token token) {
        int type = token.getType();
        return type == TiDBLexer.COMMENT_INPUT || type == TiDBLexer.LINE_COMMENT || type == TiDBLexer.EXEC_COMMENT_LEFT || type == TiDBLexer.EXEC_COMMENT_RIGHT;
    }
}
