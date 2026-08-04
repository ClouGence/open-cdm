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
package com.clougence.sql.db2.parser;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.dslpaser.parse.AntlrStatementParser;
import com.clougence.sql.db2.parser.antlr.Db2SqlLexer;
import com.clougence.sql.db2.parser.antlr.Db2SqlParser;

public class Db2AntlrStatementParser implements AntlrStatementParser {

    @Override
    public List<ParseTree> statementList(Lexer lexer, Parser parser) {
        List<ParseTree> result = new ArrayList<>();
        Db2SqlParser.Db2_fileContext file = ((Db2SqlParser) parser).db2_file();
        if (file.batch() == null) {
            return result;
        }
        for (Db2SqlParser.Sql_statementContext statement : file.batch().sql_statement()) {
            result.add(statement);
        }
        return result;
    }

    @Override
    public String getTextKeepComment(TokenStream tokens, ParseTree lastTree, Token startToken, Token endToken) {
        for (int i = startToken.getTokenIndex() - 1; i >= 0; i--) {
            Token start = tokens.get(i);
            if (start.getType() == Db2SqlLexer.WHITE_SPACE) {
                // ignore
            } else if (start.getType() == Db2SqlLexer.SEMI) {
                break;
            } else if (start.getType() == Db2SqlLexer.SQL_COMMENT || start.getType() == Db2SqlLexer.LINE_COMMENT) {
                startToken = start;
            } else {
                break;
            }
        }

        for (int i = endToken.getTokenIndex() + 1; i < tokens.size(); i++) {
            Token end = tokens.get(i);
            if (end.getType() == Db2SqlLexer.WHITE_SPACE) {
                //ignore
            } else if (end.getType() == Db2SqlLexer.SEMI) {
                endToken = end;
                break;
            } else if (end.getType() == Db2SqlLexer.SQL_COMMENT || end.getType() == Db2SqlLexer.LINE_COMMENT) {
                endToken = end;
            } else {
                break;
            }
        }

        return tokens.getText(startToken, endToken);
    }
}
