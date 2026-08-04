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
package com.clougence.clouddm.ds.polardb.sql.porx.parser;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import com.clougence.clouddm.ds.polardb.sql.porx.parser.antlr.PolardbXParser;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.parse.AntlrStatementParser;
import com.clougence.sql.common.parser.AbstractSplitAnalysisSpi;
import com.clougence.sql.mysql.analysis.reference.MySqlResourceRegistry;

public class PorXSplitAnalysisSpi extends AbstractSplitAnalysisSpi {

    @Override
    protected DslProvider dslProvider() {
        return PolarXDslProvider.INSTANCE;
    }

    @Override
    protected AbstractParseTreeVisitor<SplitQueryType> splitVisitor() {
        return PorXSplitVisitor.INSTANCE;
    }

    @Override
    protected SplitQueryType additionalType(ParseTree tree) {
        if (!(tree instanceof PolardbXParser.UdfFunctionCallContext function)) {
            return null;
        }
        PolardbXParser.FullIdContext fullId = function.customFunctionName().fullId();
        String name = fullId.uid(fullId.uid().size() - 1).getText();
        return MySqlResourceRegistry.instance().isUserDefinedFunction(name, fullId.uid().size() > 1) ? SplitQueryType.CALL_PROG_OBJ : null;
    }

    @Override
    protected void parseRoot(Parser parser) {
        ((PolardbXParser) parser).root();
    }

    @Override
    protected boolean isStatementContext(ParserRuleContext context) {
        return context instanceof PolardbXParser.SqlStatementContext && context.getParent() instanceof PolardbXParser.SqlStatementsContext;
    }

    @Override
    protected AntlrStatementParser statementParser() {
        return ((PolarXDslProvider) PolarXDslProvider.INSTANCE).treeParser();
    }
}
