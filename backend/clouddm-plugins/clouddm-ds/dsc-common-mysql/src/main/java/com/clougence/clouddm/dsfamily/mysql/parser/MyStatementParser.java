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
package com.clougence.clouddm.dsfamily.mysql.parser;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.dsfamily.mysql.parser.antlr.MySqlLexer;
import com.clougence.clouddm.dsfamily.mysql.parser.antlr.MySqlParser;
import com.clougence.dslpaser.parse.AntlrStatementParser;

public class MyStatementParser implements AntlrStatementParser {

    @Override
    public List<ParseTree> statementList(Lexer lexer, Parser parser) {
        List<ParseTree> result = new ArrayList<>();
        List<ParseTree> children = ((MySqlParser) parser).root().children;
        for (ParseTree child : children) {
            if (child instanceof MySqlParser.SqlStatementsContext) {
                for (ParseTree parseTree : ((MySqlParser.SqlStatementsContext) child).children) {
                    if (parseTree instanceof MySqlParser.SqlStatementContext) {
                        result.add(parseTree);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public String getTextKeepComment(TokenStream tokens, ParseTree lastTree, Token startToken, Token endToken) {
        for (int i = startToken.getTokenIndex() - 1; i >= 0; i--) {
            Token start = tokens.get(i);
            if (start.getType() == MySqlLexer.SPACE) {
                // ignore
            } else if (start.getType() == MySqlLexer.SEMI) {
                break;
            } else if (start.getType() == MySqlLexer.COMMENT_INPUT || start.getType() == MySqlLexer.LINE_COMMENT) {
                startToken = start;
            } else {
                break;
            }
        }

        for (int i = endToken.getTokenIndex() + 1; i < tokens.size(); i++) {
            Token end = tokens.get(i);
            if (end.getType() == MySqlLexer.SPACE) {
                //ignore
            } else if (end.getType() == MySqlLexer.SEMI) {
                endToken = end;
                break;
            } else if (end.getType() == MySqlLexer.COMMENT_INPUT || end.getType() == MySqlLexer.LINE_COMMENT) {
                endToken = end;
            } else {
                break;
            }
        }

        return tokens.getText(startToken, endToken);
    }
}
