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
package com.clougence.clouddm.ds.oceanbase.sql.ob4my.parser;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import com.clougence.clouddm.ds.oceanbase.sql.parser.antlr.ObForMySqlParser;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.parse.AntlrStatementParser;
import com.clougence.sql.common.parser.AbstractSplitAnalysisSpi;
import com.clougence.sql.mysql.analysis.reference.MySqlResourceRegistry;

public class ObSplitAnalysisSpi extends AbstractSplitAnalysisSpi {

    @Override
    protected DslProvider dslProvider() {
        return ObMyDslProvider.INSTANCE;
    }

    @Override
    protected AbstractParseTreeVisitor<SplitQueryType> splitVisitor() {
        return ObSplitVisitor.INSTANCE;
    }

    @Override
    protected SplitQueryType additionalType(ParseTree tree) {
        if (tree instanceof ObForMySqlParser.UdfFunctionCallContext function) {
            ObForMySqlParser.FullIdContext fullId = function.customFunctionName().fullId();
            String name = fullId.uid(fullId.uid().size() - 1).getText();
            return MySqlResourceRegistry.instance().isUserDefinedFunction(name, fullId.uid().size() > 1) ? SplitQueryType.CALL_PROG_OBJ : null;
        }
        if (tree instanceof ObForMySqlParser.SelectInsertValueContext) {
            return SplitQueryType.SELECT;
        }
        if (tree instanceof ObForMySqlParser.AlterByAddColumnContext || tree instanceof ObForMySqlParser.AlterByAddColumnsContext) {
            return SplitQueryType.ADD_COLUMN;
        }
        if (tree instanceof ObForMySqlParser.AlterByModifyColumnContext || tree instanceof ObForMySqlParser.AlterByChangeColumnContext
            || tree instanceof ObForMySqlParser.AlterByChangeDefaultContext) {
            return SplitQueryType.ALTER_COLUMN;
        }
        if (tree instanceof ObForMySqlParser.AlterByRenameColumnContext) {
            return SplitQueryType.RENAME_COLUMN;
        }
        if (tree instanceof ObForMySqlParser.AlterByDropColumnContext) {
            return SplitQueryType.DROP_COLUMN;
        }
        if (tree instanceof ObForMySqlParser.AlterByAddPrimaryKeyContext || tree instanceof ObForMySqlParser.AlterByAddForeignKeyContext
            || tree instanceof ObForMySqlParser.AlterByAddCheckTableConstraintContext) {
            return SplitQueryType.ADD_CONSTRAINT;
        }
        if (tree instanceof ObForMySqlParser.AlterByAddUniqueKeyContext unique) {
            return unique.CONSTRAINT() == null ? SplitQueryType.ADD_INDEX : SplitQueryType.ADD_CONSTRAINT;
        }
        if (tree instanceof ObForMySqlParser.AlterByDropPrimaryKeyContext || tree instanceof ObForMySqlParser.AlterByDropForeignKeyContext
            || tree instanceof ObForMySqlParser.AlterByDropConstraintCheckContext) {
            return SplitQueryType.DROP_CONSTRAINT;
        }
        if (tree instanceof ObForMySqlParser.AlterByAddIndexContext || tree instanceof ObForMySqlParser.AlterByAddSpecialIndexContext) {
            return SplitQueryType.ADD_INDEX;
        }
        if (tree instanceof ObForMySqlParser.AlterByDropIndexContext) {
            return SplitQueryType.DROP_INDEX;
        }
        if (tree instanceof ObForMySqlParser.AlterByRenameIndexContext) {
            return SplitQueryType.RENAME_INDEX;
        }
        if (tree instanceof ObForMySqlParser.AlterByAlterIndexVisibilityContext) {
            return SplitQueryType.ALTER_INDEX;
        }
        if (tree instanceof ObForMySqlParser.AlterByRenameContext) {
            return SplitQueryType.RENAME_TABLE;
        }
        if (tree instanceof ObForMySqlParser.AlterByAddPartitionContext) {
            return SplitQueryType.ADD_PARTITION;
        }
        if (tree instanceof ObForMySqlParser.AlterByDropPartitionContext) {
            return SplitQueryType.DROP_PARTITION;
        }
        if (tree instanceof ObForMySqlParser.AlterByTruncatePartitionContext) {
            return SplitQueryType.TRUNCATE_PARTITION;
        }
        if (tree instanceof ObForMySqlParser.AlterByCoalescePartitionContext || tree instanceof ObForMySqlParser.AlterByReorganizePartitionContext
            || tree instanceof ObForMySqlParser.AlterByExchangePartitionContext || tree instanceof ObForMySqlParser.AlterByRemovePartitioningContext
            || tree instanceof ObForMySqlParser.AlterByUpgradePartitioningContext) {
            return SplitQueryType.ALTER_PARTITION;
        }
        if (tree instanceof ObForMySqlParser.AlterByAnalyzePartitionContext || tree instanceof ObForMySqlParser.AlterByCheckPartitionContext
            || tree instanceof ObForMySqlParser.AlterByOptimizePartitionContext || tree instanceof ObForMySqlParser.AlterByRepairPartitionContext
            || tree instanceof ObForMySqlParser.AlterByRebuildPartitionContext || tree instanceof ObForMySqlParser.AlterByDiscardPartitionContext
            || tree instanceof ObForMySqlParser.AlterByImportPartitionContext) {
            return SplitQueryType.ADMIN_PARTITION;
        }
        if (tree instanceof ObForMySqlParser.AlterByDiscardTablespaceContext || tree instanceof ObForMySqlParser.AlterByImportTablespaceContext) {
            return SplitQueryType.ADMIN_TABLE;
        }
        if (tree instanceof ObForMySqlParser.TableOptionCommentContext) {
            return SplitQueryType.COMMENT_TABLE;
        }
        if (tree instanceof ObForMySqlParser.IndexOptionContext option && option.COMMENT() != null) {
            return SplitQueryType.COMMENT_INDEX;
        }
        return null;
    }

    @Override
    protected Set<SplitQueryType> collectTypes(ParserRuleContext context, String script) {
        ObForMySqlParser.SetVariableContext setVariable = findContext(context, ObForMySqlParser.SetVariableContext.class);
        if (setVariable != null) {
            Set<SplitQueryType> types = new LinkedHashSet<>();
            for (ObForMySqlParser.VariableClauseContext variable : setVariable.variableClause()) {
                if (variable.LOCAL_ID() != null) {
                    types.add(SplitQueryType.SESSION_VARIABLE_RW);
                } else if (variable.GLOBAL_ID() != null || variable.GLOBAL() != null || variable.PERSIST() != null) {
                    types.add(SplitQueryType.SYSTEM_SETTING_WRITE);
                } else {
                    types.add(SplitQueryType.SESSION_SETTING_WRITE);
                }
            }
            return types.isEmpty() ? Collections.singleton(SplitQueryType.UNKNOWN) : types;
        }

        Set<SplitQueryType> types = new LinkedHashSet<>(super.collectTypes(context, script));
        collectSecondaryTypes(context, types);
        return types;
    }

    private void collectSecondaryTypes(ParseTree tree, Set<SplitQueryType> types) {
        if (tree instanceof ObForMySqlParser.AlterByChangeColumnContext change && !change.oldColumn.getText().equals(change.columnDefinition().uid().getText())) {
            types.add(SplitQueryType.RENAME_COLUMN);
        }
        if (tree instanceof ObForMySqlParser.AlterByImportTablespaceContext || tree instanceof ObForMySqlParser.AlterByImportPartitionContext) {
            types.add(SplitQueryType.DATA_IMPORT);
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            collectSecondaryTypes(tree.getChild(i), types);
        }
    }

    @Override
    protected List<SplitScript> collectChildren(ParserRuleContext context, CommonTokenStream tokens) {
        ObForMySqlParser.CreateViewContext createView = findContext(context, ObForMySqlParser.CreateViewContext.class);
        if (createView != null) {
            return List.of(createChild(createView.selectStatement(), tokens, Collections.singleton(SplitQueryType.SELECT), Collections.emptyList()));
        }
        ObForMySqlParser.AlterViewContext alterView = findContext(context, ObForMySqlParser.AlterViewContext.class);
        if (alterView != null) {
            return List.of(createChild(alterView.selectStatement(), tokens, Collections.singleton(SplitQueryType.SELECT), Collections.emptyList()));
        }
        return Collections.emptyList();
    }

    private <T extends ParserRuleContext> T findContext(ParseTree tree, Class<T> type) {
        if (type.isInstance(tree)) {
            return type.cast(tree);
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            T found = findContext(tree.getChild(i), type);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    @Override
    protected void parseRoot(Parser parser) {
        ((ObForMySqlParser) parser).root();
    }

    @Override
    protected boolean isStatementContext(ParserRuleContext context) {
        return context instanceof ObForMySqlParser.SqlStatementContext && context.getParent() instanceof ObForMySqlParser.SqlStatementsContext;
    }

    @Override
    protected AntlrStatementParser statementParser() {
        return ((ObMyDslProvider) ObMyDslProvider.INSTANCE).treeParser();
    }
}
