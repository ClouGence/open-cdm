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
package com.clougence.clouddm.ds.dameng.sql.parser;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import com.clougence.clouddm.ds.dameng.sql.parser.antlr.DmSqlParser;
import com.clougence.dslpaser.parse.AntlrStatementParser;

public class DmStatementParser implements AntlrStatementParser {

    @Override
    public List<ParseTree> statementList(Lexer lexer, Parser parser) {
        List<ParseTree> result = new ArrayList<>();
        DmSqlParser.SqlScriptContext script = ((DmSqlParser) parser).sqlScript();
        if (script.children == null) {
            return result;
        }
        for (ParseTree child : script.children) {
            if (child instanceof TerminalNodeImpl) {
                continue;
            }
            if (child instanceof DmSqlParser.StatementBlockContext block) {
                if (block.statement() != null) {
                    result.add(block.statement());
                }
            }
        }
        return result;
    }

    @Override
    public String getTextKeepComment(TokenStream tokens, ParseTree lastTree, Token startToken, Token endToken) {
        for (int i = startToken.getTokenIndex() - 1; i >= 0; i--) {
            Token start = tokens.get(i);
            if (start.getType() == DmSqlParser.WS) {
                // ignore whitespace
            } else if (start.getType() == DmSqlParser.SEMI || start.getType() == DmSqlParser.SLASH) {
                break;
            } else if (start.getType() == DmSqlParser.LINE_COMMENT || start.getType() == DmSqlParser.C_LINE_COMMENT || start.getType() == DmSqlParser.BLOCK_COMMENT) {
                startToken = start;
            } else {
                break;
            }
        }

        for (int i = endToken.getTokenIndex() + 1; i < tokens.size(); i++) {
            Token end = tokens.get(i);
            if (end.getType() == DmSqlParser.WS) {
                // ignore whitespace
            } else if (end.getType() == DmSqlParser.SEMI) {
                endToken = end;
                Token slash = followingSlashTerminator(tokens, i + 1);
                if (slash != null) {
                    endToken = slash;
                }
                break;
            } else if (end.getType() == DmSqlParser.SLASH) {
                endToken = end;
                break;
            } else if (end.getType() == DmSqlParser.LINE_COMMENT || end.getType() == DmSqlParser.C_LINE_COMMENT || end.getType() == DmSqlParser.BLOCK_COMMENT) {
                endToken = end;
            } else {
                break;
            }
        }
        return tokens.getText(startToken, endToken);
    }

    private Token followingSlashTerminator(TokenStream tokens, int startIndex) {
        for (int i = startIndex; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            if (token.getType() == DmSqlParser.WS) {
                continue;
            }
            if (token.getType() == DmSqlParser.SLASH) {
                return token;
            }
            return null;
        }
        return null;
    }
}
