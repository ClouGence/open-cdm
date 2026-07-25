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
package com.clougence.sql.iso.sql2003.parser;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.parse.AntlrStatementParser;
import com.clougence.sql.common.parser.AbstractSplitAnalysisSpi;
import com.clougence.sql.iso.sql2003.parser.antlr.Sql2003Parser;

public class Sql2003SplitAnalysisSpi extends AbstractSplitAnalysisSpi {

    protected DslProvider dslProvider() {
        return Sql2003DslProvider.INSTANCE;
    }

    protected AbstractParseTreeVisitor<SecQueryType> splitVisitor() {
        return Sql2003SplitVisitor.INSTANCE;
    }

    @Override
    protected void parseRoot(Parser parser) {
        ((Sql2003Parser) parser).root();
    }

    @Override
    protected boolean isStatementContext(ParserRuleContext context) {
        return context instanceof Sql2003Parser.SqlStatementContext && context.getParent() instanceof Sql2003Parser.SqlScriptContext;
    }

    @Override
    protected AntlrStatementParser statementParser() {
        return new Sql2003StatementParser();
    }
}
