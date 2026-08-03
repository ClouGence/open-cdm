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
package com.clougence.sql.iso.sql99.parser;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.parse.AntlrStatementParser;
import com.clougence.sql.common.parser.AbstractSplitAnalysisSpi;
import com.clougence.sql.iso.sql99.parser.antlr.Sql99Parser;

public class Sql99SplitAnalysisSpi extends AbstractSplitAnalysisSpi {

    protected DslProvider dslProvider() {
        return Sql99DslProvider.INSTANCE;
    }

    protected AbstractParseTreeVisitor<SplitQueryType> splitVisitor() {
        return Sql99SplitVisitor.INSTANCE;
    }

    @Override
    protected void parseRoot(Parser parser) {
        ((Sql99Parser) parser).root();
    }

    @Override
    protected boolean isStatementContext(ParserRuleContext context) {
        return context instanceof Sql99Parser.SqlStatementContext && context.getParent() instanceof Sql99Parser.SqlScriptContext;
    }

    @Override
    protected AntlrStatementParser statementParser() {
        return new Sql99StatementParser();
    }
}
