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
package com.clougence.clouddm.dsfamily.db2.parser;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.dsfamily.db2.parser.antlr.Db2SqlLexer;
import com.clougence.clouddm.dsfamily.db2.parser.antlr.Db2SqlParser;
import com.clougence.dslpaser.parse.AntlrStatementParser;

public class Db2AntlrStatementParser implements AntlrStatementParser {

    @Override
    public List<ParseTree> statementList(Lexer lexer, Parser parser) {
        List<ParseTree> result = new ArrayList<>();
        List<ParseTree> children = ((Db2SqlParser) parser).root().children;
        for (ParseTree child : children) {
            if (child instanceof Db2SqlParser.SqlStatementsContext) {
                for (ParseTree parseTree : ((Db2SqlParser.SqlStatementsContext) child).children) {
                    if (parseTree instanceof Db2SqlParser.SqlStatementContext) {
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
            if (start.getType() == Db2SqlLexer.SPACE) {
                // ignore
            } else if (start.getType() == Db2SqlLexer.SEMI) {
                break;
            } else if (start.getType() == Db2SqlLexer.SPEC_MYSQL_COMMENT || start.getType() == Db2SqlLexer.COMMENT_INPUT || start.getType() == Db2SqlLexer.LINE_COMMENT) {
                startToken = start;
            } else {
                break;
            }
        }

        for (int i = endToken.getTokenIndex() + 1; i < tokens.size(); i++) {
            Token end = tokens.get(i);
            if (end.getType() == Db2SqlLexer.SPACE) {
                //ignore
            } else if (end.getType() == Db2SqlLexer.SEMI) {
                endToken = end;
                break;
            } else if (end.getType() == Db2SqlLexer.SPEC_MYSQL_COMMENT || end.getType() == Db2SqlLexer.COMMENT_INPUT || end.getType() == Db2SqlLexer.LINE_COMMENT) {
                endToken = end;
            } else {
                break;
            }
        }

        return tokens.getText(startToken, endToken);
    }
}
