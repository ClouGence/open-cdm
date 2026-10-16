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
package com.clougence.sql.es;

import com.clougence.dslpaser.parse.AntlrStatementParser;
import com.clougence.sql.es.antlr.EsLexer;
import com.clougence.sql.es.antlr.EsParser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;

final class EsAntlrStatementParser implements AntlrStatementParser {
    @Override
    public List<ParseTree> statementList(Lexer lexer, Parser parser) {
        EsSyntaxErrorListener.install(parser);
        return new ArrayList<>(((EsParser) parser).script().request());
    }

    @Override
    public String getTextKeepComment(TokenStream tokens, ParseTree lastTree, Token startToken, Token endToken) {
        return tokens.getText(scriptStart(tokens, lastTree, startToken), endToken);
    }

    static Token scriptStart(TokenStream tokens, ParseTree lastTree, Token startToken) {
        int index = 0;
        if (lastTree != null) {
            index = ((ParserRuleContext) lastTree).getStop().getTokenIndex() + 1;
        }
        while (index < startToken.getTokenIndex()) {
            int type = tokens.get(index).getType();
            if (type != EsLexer.WS && type != EsLexer.EOL) {
                break;
            }
            index++;
        }
        return tokens.get(index);
    }
}
