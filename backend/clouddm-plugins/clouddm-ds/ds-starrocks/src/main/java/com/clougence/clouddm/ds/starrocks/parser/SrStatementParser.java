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
package com.clougence.clouddm.ds.starrocks.parser;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.starrocks.parser.antlr.StarRocksParser;
import com.clougence.dslpaser.parse.AntlrStatementParser;
import com.clougence.dslpaser.parse.SyntaxErrorListener;

public class SrStatementParser implements AntlrStatementParser {

    @Override
    public List<ParseTree> statementList(Lexer lexer, Parser parser) {
        parser.addErrorListener(SyntaxErrorListener.INSTANCE);
        List<ParseTree> result = new ArrayList<>();
        List<StarRocksParser.SingleStatementContext> singleStatementContexts = ((StarRocksParser) parser).sqlStatements().singleStatement();
        for (StarRocksParser.SingleStatementContext singleStatementContext : singleStatementContexts) {
            if (singleStatementContext.statement()!=null) {
                result.add(singleStatementContext.statement());
            }
        }

        return result;
    }

    @Override
    public String getTextKeepComment(TokenStream tokens, ParseTree lastTree, Token startToken, Token endToken) {
        for (int i = startToken.getTokenIndex() - 1; i >= 0; i--) {
            Token start = tokens.get(i);
            int type = start.getType();
            if (type == StarRocksParser.SEMICOLON) {
                break;
            } else if (type == StarRocksParser.SIMPLE_COMMENT || type == StarRocksParser.BRACKETED_COMMENT) {
                startToken = start;
            } else {
                break;
            }
        }

        for (int i = endToken.getTokenIndex() + 1; i < tokens.size(); i++) {
            Token end = tokens.get(i);
            int type = end.getType();
            if (type == StarRocksParser.SEMICOLON) {
                endToken = end;
                break;
            } else if (type == StarRocksParser.SIMPLE_COMMENT || type == StarRocksParser.BRACKETED_COMMENT) {
                endToken = end;
            } else {
                break;
            }
        }

        return tokens.getText(startToken, endToken);
    }
}
