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
package com.clougence.sql.mysql.parser;

import org.antlr.v4.runtime.*;

import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.sql.common.parser.*;
import com.clougence.sql.mysql.parser.antlr.MySqlLexer;

/** Lexer-only MySQL statement splitter. */
public class MySplitAnalysisSpi extends AbstractSplitAnalysisSpi {

    private final MyDslProvider provider;

    public MySplitAnalysisSpi(MyDslProvider provider){
        this.provider = provider;
    }

    @Override
    protected DslProvider dslProvider() {
        return this.provider;
    }

    @Override
    protected LexerSplitPolicy createSplitPolicy() {
        return new MyLexerSplitPolicy();
    }

    @Override
    protected Lexer createLexer(CharStream source) {
        MySqlLexer lexer = new SplitLexer(source);
        lexer.setConfig(this.provider.config());
        return lexer;
    }

    private static final class SplitLexer extends MySqlLexer {

        private SplitLexer(CharStream input){
            super(input);
        }
    }

}
