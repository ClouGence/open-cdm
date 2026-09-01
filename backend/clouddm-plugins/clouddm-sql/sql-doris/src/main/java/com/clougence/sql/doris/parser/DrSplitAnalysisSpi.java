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
package com.clougence.sql.doris.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;

import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.parse.CaseInsensitiveStream;
import com.clougence.sql.common.parser.AbstractSplitAnalysisSpi;
import com.clougence.sql.common.parser.LexerSplitPolicy;
import com.clougence.sql.common.parser.SplitLexerFastPath;
import com.clougence.sql.common.parser.SplitLexerFastPath.CommentSyntax;
import com.clougence.sql.doris.parser.antlr.DorisLexer;

/** Doris lexer-only statement splitter. */
public class DrSplitAnalysisSpi extends AbstractSplitAnalysisSpi {

    private final DrDslProvider provider;

    public DrSplitAnalysisSpi(){
        this(DrDslProvider.INSTANCE);
    }

    public DrSplitAnalysisSpi(DrDslProvider provider){
        this.provider = provider;
    }

    @Override
    protected DslProvider dslProvider() {
        return this.provider;
    }

    @Override
    protected LexerSplitPolicy createSplitPolicy() {
        return new DrLexerSplitPolicy();
    }

    @Override
    protected Lexer createLexer(CharStream source) {
        DorisLexer lexer = new SplitLexer(new CaseInsensitiveStream(source));
        lexer.setConfig(this.provider.config());
        return lexer;
    }

    private static final class SplitLexer extends DorisLexer {
        private SplitLexer(CharStream input){
            super(input);
        }

        @Override
        public Token nextToken() {
            Token token = SplitLexerFastPath.nextToken(this, DorisLexer.IDENTIFIER, DorisLexer.WS, CommentSyntax.STANDARD);
            return token != null ? token : super.nextToken();
        }
    }
}
