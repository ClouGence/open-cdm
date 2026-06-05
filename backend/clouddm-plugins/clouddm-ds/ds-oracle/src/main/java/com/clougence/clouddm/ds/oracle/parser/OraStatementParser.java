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
package com.clougence.clouddm.ds.oracle.parser;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import com.clougence.clouddm.ds.oracle.parser.antlr.PlSqlParser;
import com.clougence.dslpaser.parse.AntlrStatementParser;

public class OraStatementParser implements AntlrStatementParser {

    @Override
    public List<ParseTree> statementList(Lexer lexer, Parser parser) {
        List<ParseTree> result = new ArrayList<>();
        List<ParseTree> children = ((PlSqlParser) parser).sql_script().children;
        for (ParseTree child : children) {
            if (child instanceof TerminalNodeImpl) {
                continue;
            } else {
                result.add(child);
            }
        }
        return result;
    }

    @Override
    public String getTextKeepComment(TokenStream tokens, ParseTree lastTree, Token startToken, Token endToken) {
        for (int i = startToken.getTokenIndex() - 1; i >= 0; i--) {
            Token start = tokens.get(i);
            if (start.getType() == PlSqlParser.SPACES) {
                // ignore
            } else if (start.getType() == PlSqlParser.SEMICOLON) {
                break;
            } else if (start.getType() == PlSqlParser.SINGLE_LINE_COMMENT || start.getType() == PlSqlParser.MULTI_LINE_COMMENT || start.getType() == PlSqlParser.REMARK_COMMENT) {
                startToken = start;
            } else {
                break;
            }
        }

        for (int i = endToken.getTokenIndex() + 1; i < tokens.size(); i++) {
            Token end = tokens.get(i);
            if (end.getType() == PlSqlParser.SPACES) {
                //ignore
            } else if (end.getType() == PlSqlParser.SEMICOLON) {
                endToken = end;
                break;
            } else if (end.getType() == PlSqlParser.SINGLE_LINE_COMMENT || end.getType() == PlSqlParser.MULTI_LINE_COMMENT || end.getType() == PlSqlParser.REMARK_COMMENT) {
                endToken = end;
            } else {
                break;
            }
        }

        return tokens.getText(startToken, endToken);
    }
}
