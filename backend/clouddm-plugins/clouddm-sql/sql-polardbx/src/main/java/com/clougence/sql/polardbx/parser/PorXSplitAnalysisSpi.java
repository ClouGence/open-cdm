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
package com.clougence.sql.polardbx.parser;

import java.util.Locale;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import com.clougence.sql.polardbx.parser.antlr.PolardbXParser;
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
        if (!(tree instanceof PolardbXParser.VariableClauseContext || tree instanceof PolardbXParser.MysqlVariableContext
            || tree instanceof PolardbXParser.SetTransactionStatementContext || tree instanceof PolardbXParser.SlowSqlCclContext
            || tree instanceof PolardbXParser.UdfFunctionCallContext || tree instanceof PolardbXParser.LockClauseContext
            || tree instanceof PolardbXParser.SelectInsertValueContext || tree instanceof PolardbXParser.AtomTableItemContext)) {
            return null;
        }
        if (!isExecuted(tree)) {
            return null;
        }
        if (tree instanceof PolardbXParser.LockClauseContext) {
            return SplitQueryType.QUERY_LOCK;
        }
        if (tree instanceof PolardbXParser.SelectInsertValueContext) {
            return SplitQueryType.SELECT;
        }
        if (tree instanceof PolardbXParser.AtomTableItemContext table) {
            return tableReadType(table);
        }
        if (tree instanceof PolardbXParser.VariableClauseContext variable && variable.getParent() instanceof PolardbXParser.SetVariableContext) {
            return PorXSplitVisitor.variableWriteType(variable);
        }
        if (tree instanceof PolardbXParser.MysqlVariableContext variable) {
            String name = variable.getText().toUpperCase(Locale.ROOT);
            if (name.startsWith("@@GLOBAL.") || name.startsWith("@@PERSIST.") || name.startsWith("@@PERSIST_ONLY.")) {
                return null;
            }
            return SplitQueryType.SESSION_VARIABLE_RW;
        }
        if (tree instanceof PolardbXParser.SetTransactionStatementContext transaction) {
            if (transaction.GLOBAL() != null) {
                return SplitQueryType.SYSTEM_SETTING_WRITE;
            }
            if (transaction.SESSION() != null) {
                return SplitQueryType.SESSION_SETTING_WRITE;
            }
        }
        if (tree instanceof PolardbXParser.SlowSqlCclContext ccl && ccl.GO() != null) {
            // GO also terminates excess running queries when enabling/updating throttling.
            return SplitQueryType.ADMIN;
        }
        if (!(tree instanceof PolardbXParser.UdfFunctionCallContext function)) {
            return null;
        }
        PolardbXParser.FullIdContext fullId = function.customFunctionName().fullId();
        String name = fullId.uid(fullId.uid().size() - 1).getText();
        return MySqlResourceRegistry.instance().isUserDefinedFunction(name, fullId.uid().size() > 1) ? SplitQueryType.CALL_PROG_OBJ : null;
    }

    private boolean isExecuted(ParseTree tree) {
        for (ParseTree parent = tree.getParent(); parent != null; parent = parent.getParent()) {
            // ANALYZE and TRACE have separate contexts and execute their SQL bodies.
            if (parent instanceof PolardbXParser.FullDescribeStatementContext
                || parent instanceof PolardbXParser.PolardbxExplainStatementContext
                || parent instanceof PolardbXParser.BaselineStatementContext
                || parent instanceof PolardbXParser.RoutineBodyContext || parent instanceof PolardbXParser.CreateFunctionContext
                || parent instanceof PolardbXParser.CreateViewContext || parent instanceof PolardbXParser.AlterViewContext) {
                return false;
            }
        }
        return true;
    }

    private SplitQueryType tableReadType(PolardbXParser.AtomTableItemContext table) {
        PolardbXParser.FullIdContext name = table.tableName().fullId();
        if (name.uid().size() != 2) {
            return null;
        }
        String schema = name.uid(0).getText();
        String object = name.uid(1).getText();
        if (!(schema.equalsIgnoreCase("information_schema") || schema.equalsIgnoreCase("`information_schema`"))
            || !(object.equalsIgnoreCase("PLAN_CACHE") || object.equalsIgnoreCase("`PLAN_CACHE`")
                || object.equalsIgnoreCase("SPM") || object.equalsIgnoreCase("`SPM`"))) {
            return null;
        }
        // AtomTableItem also occurs in UPDATE/DELETE targets; only query sources qualify.
        for (ParseTree parent = table.getParent(); parent != null; parent = parent.getParent()) {
            if (parent instanceof PolardbXParser.QuerySpecificationContext || parent instanceof PolardbXParser.QuerySpecificationNointoContext) {
                return SplitQueryType.PERFORMANCE;
            }
        }
        return null;
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
