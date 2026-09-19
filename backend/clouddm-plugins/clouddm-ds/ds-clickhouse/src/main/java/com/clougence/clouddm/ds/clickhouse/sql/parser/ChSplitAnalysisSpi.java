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
package com.clougence.clouddm.ds.clickhouse.sql.parser;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import com.clougence.clouddm.ds.clickhouse.sql.parser.antlr.ClickHouseParser;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.parse.AntlrStatementParser;
import com.clougence.sql.common.parser.AbstractSplitAnalysisSpi;

public class ChSplitAnalysisSpi extends AbstractSplitAnalysisSpi {

    private static final Set<String> KNOWN_USER_FUNCTIONS = Set.of("ads_version", "ss", "test", "test_func", "test_func1", "test_function", "test_function1");

    protected DslProvider dslProvider() {
        return ChSqlDslProvider.INSTANCE;
    }

    protected AbstractParseTreeVisitor<SplitQueryType> splitVisitor() {
        return ChSplitVisitor.INSTANCE;
    }

    @Override
    protected Set<SplitQueryType> collectTypes(ParserRuleContext context, String script) {
        if (context instanceof ClickHouseParser.QueryStmtExecuteAsContext) {
            // Executed child actions belong to the child, not the identity-switch node.
            return Set.of(SplitQueryType.SWITCH_USER);
        }
        return super.collectTypes(context, script);
    }

    @Override
    protected SplitQueryType additionalType(ParseTree tree) {
        if (tree instanceof ClickHouseParser.SystemShutdownStmtContext
            || tree instanceof ClickHouseParser.SystemSuspendStmtContext
            || tree instanceof ClickHouseParser.SystemReloadFunctionStmtContext
            || tree instanceof ClickHouseParser.SystemReloadFunctionsStmtContext) {
            return SplitQueryType.UNSAFE;
        }
        if (tree instanceof ClickHouseParser.SystemListenStmtContext listen && listen.STOP() != null) {
            ClickHouseParser.SystemListenTargetContext target = listen.systemListenTarget();
            // Closing all/default query endpoints can make the instance unavailable.
            if (target.EXCEPT() == null && (target.ALL() != null || target.DEFAULT() != null)) {
                return SplitQueryType.UNSAFE;
            }
        }
        if (tree instanceof ClickHouseParser.CreateUserStmtContext user) {
            // Explicit ROLE takes precedence over the implicit grant in named DEFAULT ROLE.
            ClickHouseParser.AccessRoleSetContext defaults = null;
            for (ClickHouseParser.CreateUserClauseContext clause : user.createUserClause()) {
                if (clause.userRoles() != null) {
                    if (clause.userRoles().accessRoleSet().members != null) {
                        return SplitQueryType.GRANT;
                    }
                    return null;
                }
                if (clause.userDefaultRoles() != null) {
                    defaults = clause.userDefaultRoles().accessRoleSet();
                }
            }
            if (defaults != null && defaults.members != null) {
                return SplitQueryType.GRANT;
            }
        }
        if (tree instanceof ClickHouseParser.ReplaceGrantOptionContext) {
            return SplitQueryType.REVOKE;
        }
        if (tree instanceof ClickHouseParser.AccessRenameContext rename) {
            if (rename.getParent() instanceof ClickHouseParser.AlterUserClauseContext) {
                return SplitQueryType.RENAME_USER;
            }
            if (rename.getParent() instanceof ClickHouseParser.AlterRoleClauseContext) {
                return SplitQueryType.RENAME_ROLE;
            }
        }
        if (tree instanceof ClickHouseParser.ColumnExprFunctionContext) {
            for (ParseTree parent = tree.getParent(); parent != null; parent = parent.getParent()) {
                if (parent instanceof ClickHouseParser.RowPolicyClauseContext) {
                    // A policy filter is stored for later queries, not evaluated by this DDL.
                    return null;
                }
            }
        }
        if (tree instanceof ClickHouseParser.QueryParameterContext) {
            return SplitQueryType.SESSION_VARIABLE_RW;
        }
        if (tree instanceof ClickHouseParser.SettingExprContext setting
            && setting.getParent().getParent() instanceof ClickHouseParser.SetStmtContext) {
            return setting.accept(splitVisitor());
        }
        if (tree instanceof ClickHouseParser.ColumnExprFunctionContext function) {
            String name = function.identifier().getText();
            if (name.equals("getSetting") || name.equals("getSettingOrDefault")) {
                return SplitQueryType.SESSION_VARIABLE_RW;
            }
        }
        if (tree instanceof ClickHouseParser.SelectUnionStmtContext && isExecutedDmlQuery(tree)) {
            return SplitQueryType.SELECT;
        }
        if (tree instanceof ClickHouseParser.AlterTableClauseAddColumnContext) {
            return SplitQueryType.ADD_COLUMN;
        }
        if (tree instanceof ClickHouseParser.AlterTableClauseDropColumnContext) {
            return SplitQueryType.DROP_COLUMN;
        }
        if (tree instanceof ClickHouseParser.AlterTableClauseCommentContext) {
            return SplitQueryType.COMMENT_COLUMN;
        }
        if (tree instanceof ClickHouseParser.AlterTableClauseRenameColumnContext) {
            return SplitQueryType.RENAME_COLUMN;
        }
        if (tree instanceof ClickHouseParser.AlterTableAlterColumnContext || tree instanceof ClickHouseParser.AlterTableModifyColumnContext
            || tree instanceof ClickHouseParser.AlterTableModifyColumnDefaultContext) {
            return SplitQueryType.ALTER_COLUMN;
        }
        if (tree instanceof ClickHouseParser.AlterTableClauseDropPartitionContext) {
            return SplitQueryType.DROP_PARTITION;
        }
        if (tree instanceof ClickHouseParser.AlterTableClauseAddIndexContext) {
            return SplitQueryType.ADD_INDEX;
        }
        if (tree instanceof ClickHouseParser.AlterTableClauseDropIndexContext) {
            return SplitQueryType.DROP_INDEX;
        }
        if (tree instanceof ClickHouseParser.AlterTableClauseClearColumnContext) {
            return SplitQueryType.TRUNCATE_COLUMN;
        }
        if (tree instanceof ClickHouseParser.AlterTableModifyCommentContext) {
            return SplitQueryType.COMMENT_TABLE;
        }
        if (tree instanceof ClickHouseParser.CommentClauseContext) {
            return commentType(tree);
        }
        if (tree instanceof ClickHouseParser.ColumnExprFunctionContext function && KNOWN_USER_FUNCTIONS.contains(function.identifier().getText().toLowerCase(Locale.ROOT))) {
            return SplitQueryType.CALL_PROG_OBJ;
        }
        return null;
    }

    @Override
    protected List<SplitScript> collectChildren(ParserRuleContext context, CommonTokenStream tokens) {
        if (context instanceof ClickHouseParser.QueryStmtExecuteAsContext execute) {
            ParserRuleContext body = execute.executeAsStmt().executeAsBody();
            if (body == null) {
                return Collections.emptyList();
            }
            return List.of(createChild(body, tokens, collectTypes(body, tokens.getText(body)), collectChildren(body, tokens)));
        }
        ParserRuleContext query = viewQuery(context);
        if (query == null) {
            return Collections.emptyList();
        }
        return List.of(createChild(query, tokens, collectTypes(query, tokens.getText(query)), Collections.emptyList()));
    }

    private boolean isExecutedDmlQuery(ParseTree tree) {
        for (ParseTree current = tree.getParent(); current != null; current = current.getParent()) {
            if (current instanceof ClickHouseParser.QueryStmtInsertContext || current instanceof ClickHouseParser.QueryStmtDeleteContext
                || current instanceof ClickHouseParser.QueryStmtUpdateContext || current instanceof ClickHouseParser.ExecuteAsBodyInsertContext
                || current instanceof ClickHouseParser.ExecuteAsBodyDeleteContext || current instanceof ClickHouseParser.ExecuteAsBodyUpdateContext) {
                return true;
            }
            if (current instanceof ClickHouseParser.CreateTableStmtContext || current instanceof ClickHouseParser.CreateViewStmtContext
                || current instanceof ClickHouseParser.CreateMaterializedViewStmtContext) {
                return false;
            }
        }
        return false;
    }

    private SplitQueryType commentType(ParseTree tree) {
        for (ParseTree current = tree.getParent(); current != null; current = current.getParent()) {
            if (current instanceof ClickHouseParser.AlterTableAlterColumnContext || current instanceof ClickHouseParser.AlterTableModifyColumnContext) {
                return SplitQueryType.COMMENT_COLUMN;
            }
            if (current instanceof ClickHouseParser.CreateTableStmtContext) {
                return SplitQueryType.COMMENT_TABLE;
            }
        }
        return null;
    }

    private ParserRuleContext viewQuery(ParseTree tree) {
        if (tree instanceof ClickHouseParser.CreateViewStmtContext view) {
            return view.subqueryClause().selectUnionStmt();
        }
        if (tree instanceof ClickHouseParser.CreateMaterializedViewStmtContext view) {
            return view.subqueryClause().selectUnionStmt();
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            ParserRuleContext result = viewQuery(tree.getChild(i));
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    @Override
    protected void parseRoot(Parser parser) {
        ((ClickHouseParser) parser).root();
    }

    @Override
    protected boolean isStatementContext(ParserRuleContext context) {
        return context instanceof ClickHouseParser.QueryStmtContext && context.getParent() instanceof ClickHouseParser.RootContext;
    }

    @Override
    protected AntlrStatementParser statementParser() {
        return new ChSqlAntlrStatementParser();
    }
}
