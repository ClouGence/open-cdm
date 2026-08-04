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
package com.clougence.sql.iso.sql92.parser;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import com.clougence.dslpaser.parse.AntlrStatementParser;
import com.clougence.sql.iso.sql92.parser.antlr.Sql92Parser;

public class Sql92StatementParser implements AntlrStatementParser {

    @Override
    public List<ParseTree> statementList(Lexer lexer, Parser parser) {
        List<ParseTree> result = new ArrayList<>();
        List<ParseTree> children = ((Sql92Parser) parser).root().children;
        if (children == null) {
            return result;
        }
        for (ParseTree child : children) {
            if (child instanceof TerminalNodeImpl) {
                continue;
            }
            // drill into sqlScript -> sqlStatement
            if (child instanceof Sql92Parser.SqlScriptContext) {
                for (ParseTree sc : ((Sql92Parser.SqlScriptContext) child).children) {
                    if (sc instanceof Sql92Parser.SqlStatementContext) {
                        result.add(sc);
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
            if (start.getType() == Sql92Parser.WS) {
                // ignore
            } else if (start.getType() == Sql92Parser.SEMI) {
                break;
            } else if (start.getType() == Sql92Parser.LINE_COMMENT || start.getType() == Sql92Parser.BLOCK_COMMENT) {
                startToken = start;
            } else {
                break;
            }
        }

        for (int i = endToken.getTokenIndex() + 1; i < tokens.size(); i++) {
            Token end = tokens.get(i);
            if (end.getType() == Sql92Parser.WS) {
                //ignore
            } else if (end.getType() == Sql92Parser.SEMI) {
                endToken = end;
                break;
            } else if (end.getType() == Sql92Parser.LINE_COMMENT || end.getType() == Sql92Parser.BLOCK_COMMENT) {
                endToken = end;
            } else {
                break;
            }
        }

        return tokens.getText(startToken, endToken);
    }
}
