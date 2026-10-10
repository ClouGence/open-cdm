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
package com.clougence.sql.hana.parser;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.sql.hana.parser.antlr.HanaParser;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.parse.AntlrStatementParser;
import com.clougence.sql.common.parser.AbstractSplitAnalysisSpi;

public class HanaSplitAnalysisSpi extends AbstractSplitAnalysisSpi {

    @Override
    protected DslProvider dslProvider() {
        return HanaDslProvider.INSTANCE;
    }

    @Override
    protected AbstractParseTreeVisitor<SplitQueryType> splitVisitor() {
        return HanaSplitVisitor.INSTANCE;
    }

    @Override
    protected void parseRoot(Parser parser) {
        ((HanaParser) parser).splitRoot();
    }

    @Override
    protected boolean isStatementContext(ParserRuleContext context) {
        return context instanceof HanaParser.SplitStatementContext && context.getParent() instanceof HanaParser.SplitRootContext;
    }

    @Override
    protected boolean isStatementTerminated(Parser parser) {
        int next = parser.getTokenStream().LA(1);
        return next == HanaParser.SEMI || next == Token.EOF;
    }

    @Override
    protected AntlrStatementParser statementParser() {
        return new HanaStatementParser();
    }
}
