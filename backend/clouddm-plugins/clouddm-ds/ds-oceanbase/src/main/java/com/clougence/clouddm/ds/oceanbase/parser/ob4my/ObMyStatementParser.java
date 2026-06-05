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
package com.clougence.clouddm.ds.oceanbase.parser.ob4my;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.oceanbase.parser.ob4my.antlr.ObForMySqlLexer;
import com.clougence.clouddm.ds.oceanbase.parser.ob4my.antlr.ObForMySqlParser;
import com.clougence.dslpaser.parse.AntlrStatementParser;

public class ObMyStatementParser implements AntlrStatementParser {

    @Override
    public List<ParseTree> statementList(Lexer lexer, Parser parser) {
        List<ParseTree> result = new ArrayList<>();
        List<ParseTree> children = ((ObForMySqlParser) parser).root().children;
        for (ParseTree child : children) {
            if (child instanceof ObForMySqlParser.SqlStatementsContext) {
                for (ParseTree parseTree : ((ObForMySqlParser.SqlStatementsContext) child).children) {
                    if (parseTree instanceof ObForMySqlParser.SqlStatementContext) {
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
            if (start.getType() == ObForMySqlLexer.SPACE) {
                // ignore
            } else if (start.getType() == ObForMySqlLexer.SEMI) {
                break;
            } else if (start.getType() == ObForMySqlLexer.COMMENT_INPUT || start.getType() == ObForMySqlLexer.LINE_COMMENT) {
                startToken = start;
            } else {
                startToken = start;
            }
        }

        for (int i = endToken.getTokenIndex() + 1; i < tokens.size(); i++) {
            Token end = tokens.get(i);
            if (end.getType() == ObForMySqlLexer.SPACE) {
                //ignore
            } else if (end.getType() == ObForMySqlLexer.SEMI) {
                endToken = end;
                break;
            } else if (end.getType() == ObForMySqlLexer.COMMENT_INPUT || end.getType() == ObForMySqlLexer.LINE_COMMENT) {
                endToken = end;
            } else {
                endToken = end;;
            }
        }

        return tokens.getText(startToken, endToken);
    }
}
