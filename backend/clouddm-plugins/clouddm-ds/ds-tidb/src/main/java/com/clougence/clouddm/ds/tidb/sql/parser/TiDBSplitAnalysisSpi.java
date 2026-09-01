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
package com.clougence.clouddm.ds.tidb.sql.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;

import com.clougence.clouddm.ds.tidb.sql.parser.antlr.TiDBLexer;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.sql.common.parser.AbstractSplitAnalysisSpi;
import com.clougence.sql.common.parser.LexerSplitPolicy;
import com.clougence.sql.common.parser.SplitLexerFastPath;
import com.clougence.sql.common.parser.SplitLexerFastPath.CommentSyntax;

/**
 * TiDB lexer-only statement splitter.
 *
 * <p>This SPI deliberately does not construct a TiDB parser or classify statements. Semantic
 * analysis is owned by the behavior analysis pipeline.</p>
 */
public class TiDBSplitAnalysisSpi extends AbstractSplitAnalysisSpi {

    private final TiDBDslProvider provider;

    public TiDBSplitAnalysisSpi(TiDBDslProvider provider){
        this.provider = provider;
    }

    @Override
    protected DslProvider dslProvider() {
        return this.provider;
    }

    @Override
    protected LexerSplitPolicy createSplitPolicy() {
        return new TiDBLexerSplitPolicy();
    }

    @Override
    protected Lexer createLexer(CharStream source) {
        TiDBLexer lexer = new SplitLexer(source);
        lexer.setConfig(this.provider.config());
        return lexer;
    }

    private static final class SplitLexer extends TiDBLexer {
        private SplitLexer(CharStream input){
            super(input);
        }

        @Override
        public Token nextToken() {
            Token token = SplitLexerFastPath.nextToken(this, TiDBLexer.ID, TiDBLexer.SPACE, CommentSyntax.MYSQL);
            return token != null ? token : super.nextToken();
        }
    }
}
