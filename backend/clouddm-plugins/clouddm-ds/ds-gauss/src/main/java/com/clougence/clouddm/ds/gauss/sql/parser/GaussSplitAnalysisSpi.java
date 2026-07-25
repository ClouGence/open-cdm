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
package com.clougence.clouddm.ds.gauss.sql.parser;

import java.util.Collections;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.ds.gauss.sql.parser.antlr.GaussSqlParser;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.parse.AntlrStatementParser;
import com.clougence.sql.postgres.parser.PgSplitAnalysisSpi;
import com.clougence.sql.postgres.parser.PostgresVersion;

public class GaussSplitAnalysisSpi extends PgSplitAnalysisSpi {

    public GaussSplitAnalysisSpi(){
        super(PostgresVersion.LATEST);
    }

    @Override
    protected DslProvider dslProvider() {
        return GaussDslProvider.INSTANCE;
    }

    @Override
    protected AbstractParseTreeVisitor<SecQueryType> splitVisitor() {
        return new GaussSplitVisitor();
    }

    @Override
    protected java.util.Set<SecQueryType> collectTypes(ParserRuleContext context, String script) {
        SecQueryType type = context.accept(new GaussSplitVisitor());
        return Collections.singleton(type == null ? SecQueryType.UNKNOWN : type);
    }

    @Override
    protected void parseRoot(Parser parser) {
        ((GaussSqlParser) parser).root();
    }

    @Override
    protected boolean isStatementContext(ParserRuleContext context) {
        return context.getParent() instanceof GaussSqlParser.StmtmultiContext;
    }

    @Override
    protected AntlrStatementParser statementParser() {
        return new GaussAntlrStatementParser();
    }

}
