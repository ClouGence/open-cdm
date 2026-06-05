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
package com.clougence.clouddm.ds.redis.parser;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.redis.parser.antlr.RedisParser;
import com.clougence.dslpaser.parse.AntlrStatementParser;

public class RedisAntlrStatementParser implements AntlrStatementParser {

    @Override
    public List<ParseTree> statementList(Lexer lexer, Parser parser) {
        List<ParseTree> result = new ArrayList<>();
        recursionAddText(result, ((RedisParser) parser).rootInstSet());
        return result;
    }

    private void recursionAddText(List<ParseTree> result, ParseTree tree) {
        for (int i = 0; i < tree.getChildCount(); i++) {
            ParseTree child = tree.getChild(i);
            if (child instanceof RedisParser.CmdInstContext) {
                result.add(child);
            } else {
                recursionAddText(result, child);
            }
        }
    }

    @Override
    public String getTextKeepComment(TokenStream tokens, ParseTree lastTree, Token startToken, Token endToken) {
        int deleteChars = 0;
        if (lastTree != null) {
            startToken = ((ParserRuleContext) lastTree).getStop();
            deleteChars = startToken.getText().length();
        }

        String text = tokens.getText(startToken, endToken).trim();
        if (deleteChars > 0) {
            text = text.substring(deleteChars).trim();
        }
        return text;
    }
}
