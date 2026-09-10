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

import static com.clougence.clouddm.ds.tidb.sql.parser.antlr.TiDBParser.*;

import java.util.*;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.tidb.sql.analysis.reference.TiFunctionRegistry;
import com.clougence.clouddm.ds.tidb.sql.parser.antlr.TiDBParser;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.parse.AntlrStatementParser;
import com.clougence.sql.common.parser.AbstractSplitAnalysisSpi;

public class TiSplitAnalysisSpi extends AbstractSplitAnalysisSpi {

    protected DslProvider dslProvider() {
        return TiDBDslProvider.INSTANCE;
    }

    @Override
    protected AbstractParseTreeVisitor<SplitQueryType> splitVisitor() {
        return TiSplitVisitor.INSTANCE;
    }

    @Override
    protected Set<SplitQueryType> collectTypes(ParserRuleContext context, String script) {
        Set<SplitQueryType> types = new LinkedHashSet<>();
        SplitQueryType primary = normalizeType(context.accept(splitVisitor()));
        if (primary == SplitQueryType.SELECT && TiQueryAnalysis.isMetadataOnly(context)) {
            primary = SplitQueryType.METADATA;
        }
        types.add(primary);
        collectActions(context, context, types);
        if ((primary == SplitQueryType.SELECT || primary == SplitQueryType.METADATA) && types.contains(SplitQueryType.DATA_EXPORT)) {
            Set<SplitQueryType> exportTypes = new LinkedHashSet<>();
            exportTypes.add(SplitQueryType.DATA_EXPORT);
            exportTypes.addAll(types);
            return exportTypes;
        }
        return types;
    }

    private void collectActions(ParseTree tree, ParseTree root, Set<SplitQueryType> types) {
        if (tree instanceof FullDescribeStatementContext explain && explain.analyze == null) {
            return;
        }
        if (tree != root && (tree instanceof RoutineBodyContext || tree instanceof ReturnStatementContext)) {
            return;
        }
        if (tree instanceof PlanReplayerDumpContext replay && replay.ANALYZE() == null || tree instanceof AdminRepairStatementContext || tree instanceof BindingStatementContext) {
            return;
        }
        if (tree instanceof CreateViewContext || tree instanceof AlterViewContext) {
            return;
        }
        if (tree instanceof SetTransactionStatementContext transaction && transaction.transactionContext != null) {
            if (transaction.SESSION() != null) {
                types.add(SplitQueryType.SESSION_SETTING_WRITE);
            } else {
                types.add(SplitQueryType.SYSTEM_SETTING_WRITE);
            }
        }
        if (tree instanceof SetAutocommitStatementContext) {
            types.add(SplitQueryType.SESSION_SETTING_WRITE);
        }
        if (tree instanceof TableNameContext table && types.contains(SplitQueryType.SELECT) && TiQueryAnalysis.isMetadataTable(table)) {
            types.add(SplitQueryType.METADATA);
        }
        if (tree instanceof MixedSetItemContext item) {
            types.add(new TiSplitVisitor().visitMixedSetItem(item));
        }
        if (tree instanceof AlterByImportTablespaceContext || tree instanceof AlterByDiscardTablespaceContext || tree instanceof AlterSecondaryLoadContext) {
            types.add(SplitQueryType.ADMIN_TABLE);
        }
        if (tree instanceof AssignmentFieldContext assignment && assignment.LOCAL_ID() != null) {
            types.add(SplitQueryType.SESSION_VARIABLE_RW);
        }
        if (tree instanceof ScalarFunctionCallContext function) {
            String name = function.getStart().getText();
            if (name.equalsIgnoreCase("LOAD_FILE")) {
                types.add(SplitQueryType.DATA_IMPORT);
                types.add(SplitQueryType.UNSAFE);
            } else if (name.equalsIgnoreCase("LAST_INSERT_ID") && function.functionArgs() != null) {
                types.add(SplitQueryType.SESSION_SETTING_WRITE);
            }
        }
        if (tree instanceof BinlogStatementContext || tree instanceof CaptureTrafficContext || tree instanceof BackupLogsContext || tree instanceof PurgeBackupLogsContext
            || tree instanceof ShowBackupLogsContext backup && backup.filename != null) {
            types.add(SplitQueryType.UNSAFE);
        }
        if (tree instanceof AlterPartitionAttributesContext || tree instanceof AlterPartitionBoundaryContext) {
            types.add(SplitQueryType.ALTER_PARTITION);
        } else if (tree instanceof AlterByAddStatisticsContext || tree instanceof AlterByDropStatisticsContext) {
            types.add(SplitQueryType.ADMIN_PERFORMANCE);
        } else if (tree instanceof AlterConstraintEnforcementContext) {
            types.add(SplitQueryType.ALTER_CONSTRAINT);
        } else if (tree instanceof AlterByOrderContext || tree instanceof AlterByForceContext || tree instanceof AlterByEnableKeysContext
                   || tree instanceof AlterByDisableKeysContext) {
            types.add(SplitQueryType.ADMIN_TABLE);
        } else if (tree instanceof UserAttributeContext attribute && attribute.COMMENT() != null) {
            types.add(SplitQueryType.COMMENT_USER);
        } else if (tree instanceof SplitRegionStatementContext split && !split.PARTITION().isEmpty()) {
            types.add(SplitQueryType.ADMIN_PARTITION);
        }
        if (tree instanceof LockClauseContext && !types.contains(SplitQueryType.PERFORMANCE)) {
            types.add(SplitQueryType.QUERY_LOCK);
        } else if (tree instanceof AlterByAddPartitionContext) {
            types.add(SplitQueryType.ADD_PARTITION);
        } else if (tree instanceof PartitionDefinitionsContext && types.contains(SplitQueryType.ALTER_TABLE)) {
            types.add(SplitQueryType.ALTER_PARTITION);
        } else if (tree instanceof AlterByDropPartitionContext) {
            types.add(SplitQueryType.DROP_PARTITION);
        } else if (tree instanceof AlterByTruncatePartitionContext) {
            types.add(SplitQueryType.TRUNCATE_PARTITION);
        } else if (tree instanceof AlterByCoalescePartitionContext || tree instanceof AlterByReorganizePartitionContext || tree instanceof AlterByExchangePartitionContext
                   || tree instanceof AlterByRemovePartitioningContext || tree instanceof AlterByUpgradePartitioningContext) {
            types.add(SplitQueryType.ALTER_PARTITION);
        } else if (tree instanceof AlterByAnalyzePartitionContext || tree instanceof AlterByCheckPartitionContext || tree instanceof AlterByOptimizePartitionContext
                   || tree instanceof AlterByRebuildPartitionContext || tree instanceof AlterByRepairPartitionContext || tree instanceof AlterByDiscardPartitionContext
                   || tree instanceof AlterByImportPartitionContext || tree instanceof AnalyzeTableContext analyze && analyze.PARTITION() != null) {
            types.add(SplitQueryType.ADMIN_PARTITION);
        }
        if (tree instanceof SimpleFlushOptionContext flush && flush.LOCK() != null
            || tree instanceof FlushTableOptionContext flushTable && (flushTable.LOCK() != null || flushTable.EXPORT() != null)) {
            types.add(SplitQueryType.SESSION_LOCK);
        }
        if (tree instanceof PartitionOptionCommentContext) {
            types.add(SplitQueryType.COMMENT_PARTITION);
        }
        if (tree instanceof ColumnDeclarationContext || tree instanceof AlterByAddColumnContext || tree instanceof AlterByAddColumnsContext) {
            types.add(SplitQueryType.ADD_COLUMN);
        } else if (tree instanceof IndexDeclarationContext || tree instanceof AlterByAddIndexContext || tree instanceof AlterByAddSpecialIndexContext) {
            types.add(SplitQueryType.ADD_INDEX);
        } else if (tree instanceof ConstraintDeclarationContext || tree instanceof PrimaryKeyColumnConstraintContext || tree instanceof UniqueKeyColumnConstraintContext
                   || tree instanceof ReferenceColumnConstraintContext || tree instanceof CheckColumnConstraintContext || tree instanceof AlterByAddPrimaryKeyContext
                   || tree instanceof AlterByAddUniqueKeyContext || tree instanceof AlterByAddForeignKeyContext || tree instanceof AlterByAddCheckTableConstraintContext) {
            types.add(SplitQueryType.ADD_CONSTRAINT);
        } else if (tree instanceof AlterByDropColumnContext) {
            types.add(SplitQueryType.DROP_COLUMN);
        } else if (tree instanceof AlterByModifyColumnContext || tree instanceof AlterByChangeDefaultContext || tree instanceof AlterByChangeColumnContext) {
            types.add(SplitQueryType.ALTER_COLUMN);
            if (tree instanceof AlterByChangeColumnContext change && !change.oldColumn.getText().equals(change.columnDefinition().uid().getText())) {
                types.add(SplitQueryType.RENAME_COLUMN);
            }
        } else if (tree instanceof AlterByRenameColumnContext) {
            types.add(SplitQueryType.RENAME_COLUMN);
        } else if (tree instanceof AlterByDropConstraintCheckContext || tree instanceof AlterByDropPrimaryKeyContext || tree instanceof AlterByDropForeignKeyContext) {
            types.add(SplitQueryType.DROP_CONSTRAINT);
        } else if (tree instanceof AlterByDropIndexContext) {
            types.add(SplitQueryType.DROP_INDEX);
        } else if (tree instanceof AlterByAlterIndexVisibilityContext) {
            types.add(SplitQueryType.ALTER_INDEX);
        } else if (tree instanceof AlterByRenameIndexContext) {
            types.add(SplitQueryType.RENAME_INDEX);
        } else if (tree instanceof AlterByRenameContext) {
            types.add(SplitQueryType.RENAME_TABLE);
        } else if (tree instanceof CommentColumnConstraintContext) {
            types.add(SplitQueryType.COMMENT_COLUMN);
        } else if (tree instanceof TableOptionCommentContext) {
            types.add(SplitQueryType.COMMENT_TABLE);
        } else if (tree instanceof IndexOptionContext option && option.COMMENT() != null) {
            types.add(SplitQueryType.COMMENT_INDEX);
        } else if (tree instanceof RoutineCommentContext) {
            types.add(SplitQueryType.COMMENT_PROG_OBJ);
        } else if (tree instanceof SelectIntoTextFileContext || tree instanceof SelectIntoDumpFileContext) {
            types.add(SplitQueryType.DATA_EXPORT);
        } else if (tree instanceof SelectExpressionElementContext select && select.LOCAL_ID() != null) {
            types.add(SplitQueryType.SESSION_VARIABLE_RW);
        } else if (tree instanceof MysqlVariableContext variable) {
            if (!variable.getText().toUpperCase(Locale.ROOT).startsWith("@@GLOBAL.")) {
                types.add(SplitQueryType.SESSION_VARIABLE_RW);
            }
        } else if (tree instanceof SelectStatementContext && !types.contains(SplitQueryType.CREATE_TABLE) && !types.contains(SplitQueryType.PERFORMANCE)
                   && !types.contains(SplitQueryType.METADATA) && !types.contains(SplitQueryType.CREATE_PROG_OBJ)) {
            types.add(SplitQueryType.SELECT);
        }
        if (tree instanceof AlterByImportPartitionContext || tree instanceof AlterByImportTablespaceContext) {
            types.add(SplitQueryType.DATA_IMPORT);
        }
        SplitQueryType functionType = additionalType(tree);
        if (functionType != null) {
            types.add(functionType);
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            collectActions(tree.getChild(i), root, types);
        }
        if (tree instanceof AlterTableContext indexed && indexed.INDEXES() != null) {
            types.add(SplitQueryType.ALTER_INDEX);
        }
        if (tree instanceof AlterTableContext alter && (alter.REMOVE() != null || alter.PARTITION() != null)) {
            types.add(SplitQueryType.ALTER_PARTITION);
        }
    }

    @Override
    protected List<SplitScript> collectChildren(ParserRuleContext context, CommonTokenStream tokens) {
        ParserRuleContext body = definitionBody(context);
        if (body == null || body == context) {
            return List.of();
        }
        return List.of(programNode(body, tokens));
    }

    private ParserRuleContext definitionBody(ParseTree tree) {
        if (tree instanceof CreateViewContext view) {
            return view.selectStatement();
        }
        if (tree instanceof AlterViewContext view) {
            return view.selectStatement();
        }
        if (tree instanceof CreateProcedureContext procedure) {
            return procedure.routineBody();
        }
        if (tree instanceof CreateFunctionContext function) {
            return function.routineBody();
        }
        if (tree instanceof CreateTriggerContext trigger) {
            return trigger.routineBody();
        }
        if (tree instanceof CreateEventContext event) {
            return event.routineBody();
        }
        if (tree instanceof AlterEventContext event) {
            return event.routineBody();
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            ParserRuleContext body = definitionBody(tree.getChild(i));
            if (body != null) {
                return body;
            }
        }
        return null;
    }

    private SplitScript programNode(ParserRuleContext context, CommonTokenStream tokens) {
        if (context instanceof RoutineBodyContext || context instanceof CompoundStatementContext || context instanceof ProcedureSqlStatementContext) {
            for (int i = 0; i < context.getChildCount(); i++) {
                if (context.getChild(i) instanceof ParserRuleContext child) {
                    return programNode(child, tokens);
                }
            }
        }
        Set<SplitQueryType> types = new LinkedHashSet<>();
        List<SplitScript> children = new ArrayList<>();
        if (context instanceof BlockStatementContext) {
            types.add(SplitQueryType.BLOCK);
            collectProgramChildren(context, children, tokens);
        } else if (isProgramControl(context)) {
            types.add(SplitQueryType.PROGRAM_CONTROL);
            collectProgramChildren(context, children, tokens);
        } else if (context instanceof DeclareVariableContext || context instanceof DeclareConditionContext || context instanceof DeclareCursorContext
                   || context instanceof DeclareHandlerContext) {
            if (context instanceof DeclareCursorContext) {
                types.add(SplitQueryType.SELECT);
            }
            types.add(SplitQueryType.PROGRAM_CONTROL);
            if (context instanceof DeclareHandlerContext) {
                collectProgramChildren(context, children, tokens);
            }
        } else {
            types.addAll(collectTypes(context, tokens.getText(context)));
            children.addAll(collectChildren(context, tokens));
        }
        return createChild(context, tokens, types, children);
    }

    private void collectProgramChildren(ParseTree tree, List<SplitScript> children, CommonTokenStream tokens) {
        for (int i = 0; i < tree.getChildCount(); i++) {
            ParseTree child = tree.getChild(i);
            if (child instanceof ParserRuleContext context
                && (context instanceof ProcedureSqlStatementContext || context instanceof SqlStatementContext || context instanceof BlockStatementContext
                    || context instanceof DeclareVariableContext || context instanceof DeclareConditionContext || context instanceof DeclareCursorContext
                    || context instanceof DeclareHandlerContext || isProgramControl(context))) {
                children.add(programNode(context, tokens));
            } else {
                collectProgramChildren(child, children, tokens);
            }
        }
    }

    private boolean isProgramControl(ParseTree tree) {
        return tree instanceof CaseStatementContext || tree instanceof IfStatementContext || tree instanceof LoopStatementContext || tree instanceof RepeatStatementContext
               || tree instanceof WhileStatementContext || tree instanceof IterateStatementContext || tree instanceof LeaveStatementContext
               || tree instanceof ReturnStatementContext;
    }

    @Override
    protected SplitQueryType additionalType(ParseTree tree) {
        if (!(tree instanceof TiDBParser.UdfFunctionCallContext function)) {
            if (tree instanceof ScalarFunctionCallContext scalar) {
                return TiFunctionRegistry.nativeStatementType(scalar.getStart().getText());
            }
            return null;
        }
        TiDBParser.FullIdContext fullId = function.customFunctionName().fullId();
        String name = fullId.uid(fullId.uid().size() - 1).getText();
        return TiFunctionRegistry.statementType(name, fullId.uid().size() > 1);
    }

    @Override
    protected void parseRoot(Parser parser) {
        ((TiDBParser) parser).root();
    }

    @Override
    protected boolean isStatementContext(ParserRuleContext context) {
        return context instanceof TiDBParser.SqlStatementContext && context.getParent() instanceof TiDBParser.SqlStatementsContext;
    }

    @Override
    protected AntlrStatementParser statementParser() {
        return ((TiDBDslProvider) TiDBDslProvider.INSTANCE).treeParser();
    }
}
