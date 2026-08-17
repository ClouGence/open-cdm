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
package com.clougence.clouddm.ds.tidb.sql.analysis.behavior;

import static com.clougence.clouddm.ds.tidb.sql.parser.antlr.TiDBParser.*;

import java.util.*;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;

import com.clougence.clouddm.ds.tidb.sql.analysis.sysobj.TiDBResourceRegistry;
import com.clougence.clouddm.ds.tidb.sql.parser.TiDBVersion;
import com.clougence.clouddm.ds.tidb.sql.parser.antlr.TiDBParserBaseVisitor;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementType;

public class TiDBSplitVisitor extends TiDBParserBaseVisitor<StatementType> {

    private final Set<StatementType>   types    = new LinkedHashSet<>();
    private final Set<String>          cteNames = new LinkedHashSet<>();
    private final TiDBVersion          version;
    private final TiDBResourceRegistry resources;
    private boolean                    currentNodeOnly;
    private boolean                    externalCodeLifecycleRisk;
    private boolean                    metadataTableRead;
    private boolean                    ordinaryTableRead;

    public TiDBSplitVisitor(){
        this(TiDBVersion.LATEST, TiDBResourceRegistry.instance());
    }

    public TiDBSplitVisitor(TiDBVersion version){
        this(version, TiDBResourceRegistry.instance());
    }

    public TiDBSplitVisitor(TiDBVersion version, TiDBResourceRegistry resources){
        this.version = version == null ? TiDBVersion.LATEST : version;
        this.resources = Objects.requireNonNull(resources, "resources");
    }

    @Override
    public StatementType visit(ParseTree tree) {
        collectTypes(tree);
        return this.types.stream().findFirst().orElse(null);
    }

    public Set<StatementType> collectTypes(ParseTree tree) {
        this.types.clear();
        this.cteNames.clear();
        this.externalCodeLifecycleRisk = false;
        this.metadataTableRead = false;
        this.ordinaryTableRead = false;
        collectNode(tree);
        if (this.metadataTableRead) {
            this.types.add(StatementType.METADATA);
            if (!this.ordinaryTableRead) {
                this.types.remove(StatementType.SELECT);
            }
        }
        if (this.externalCodeLifecycleRisk) {
            this.types.add(StatementType.UNSAFE);
        }
        return new LinkedHashSet<>(this.types);
    }

    private void collectNode(ParseTree tree) {
        if (isCreateTableDefinitionSubquery(tree)) {
            if (containsFunctionCall(tree)) {
                this.types.add(StatementType.CALL_PROG_OBJ);
            }
            return;
        }
        StatementType type;
        boolean previous = this.currentNodeOnly;
        try {
            this.currentNodeOnly = true;
            type = tree.accept(this);
        } finally {
            this.currentNodeOnly = previous;
        }
        if (type != null) {
            this.types.add(type);
        }
        collectDirectActions(tree);
        if (tree instanceof CreateTableQueryExpressionContext) {
            collectNestedLockActions(tree);
            collectNestedTableReads(tree);
            return;
        }
        if (!shouldDescend(tree, type)) {
            return;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            collectNode(tree.getChild(i));
        }
        if (tree instanceof AlterTableContext ctx) {
            if (ctx.partitionDefinitions() != null) {
                this.types.add(StatementType.ALTER_PARTITION);
            }
            if (ctx.updateIndexesClause() != null) {
                this.types.add(StatementType.ALTER_PARTITION);
                this.types.add(StatementType.ALTER_INDEX);
            }
        }
    }

    private StatementType firstNestedStatement(ParseTree tree, boolean selectOnly) {
        StatementType type;
        boolean previous = this.currentNodeOnly;
        try {
            this.currentNodeOnly = true;
            type = tree.accept(this);
        } finally {
            this.currentNodeOnly = previous;
        }
        if (type == StatementType.SELECT
            || !selectOnly && (type == StatementType.INSERT || type == StatementType.UPDATE || type == StatementType.DELETE || type == StatementType.MERGE)) {
            return type;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            StatementType nestedType = firstNestedStatement(tree.getChild(i), selectOnly);
            if (nestedType != null) {
                return nestedType;
            }
        }
        return null;
    }

    private void collectDirectActions(ParseTree tree) {
        collectLockAction(tree);
        if (tree instanceof BrieBackupContext) {
            this.types.add(StatementType.DATA_EXPORT);
        } else if (tree instanceof BrieRestoreContext) {
            this.types.add(StatementType.DATA_IMPORT);
        } else if (tree instanceof BrieAdminContext ctx && ctx.PURGE() != null) {
            this.types.add(StatementType.UNSAFE);
        }
        if (tree instanceof SetBindingContext ctx && !ctx.tidbLooseArgument().isEmpty() && ctx.tidbLooseArgument(0).getStart().getType() == SELECT) {
            this.types.add(StatementType.SELECT);
        }
        if (tree instanceof ColumnDeclarationContext ctx && hasConstraint(ctx.columnDefinition())) {
            this.types.add(StatementType.ADD_CONSTRAINT);
        } else if (tree instanceof DeclareCursorContext || tree instanceof OpenCursorContext || tree instanceof FetchCursorContext || tree instanceof CloseCursorContext) {
            this.types.add(StatementType.SELECT);
            this.types.add(StatementType.PROGRAM_CONTROL);
        } else if (tree instanceof AlterByImportTablespaceContext || tree instanceof AlterByImportPartitionContext) {
            this.types.add(StatementType.DATA_IMPORT);
        }
        if (tree instanceof WithSelectExprContext ctx && ctx.uid() != null) {
            this.cteNames.add(normalizeIdentifier(ctx.uid().getText()));
        } else if (tree instanceof AtomTableItemContext ctx) {
            collectTableRead(ctx);
        } else if (tree instanceof GenericFunctionCallContext ctx && isUserDefinedFunction(ctx)) {
            this.types.add(StatementType.CALL_PROG_OBJ);
        }
        if (tree instanceof SetVariableContext ctx && ctx.setVariableAssignment().stream().anyMatch(assignment -> assignment.variableClause() == null)) {
            this.types.add(StatementType.SESSION_SETTING_WRITE);
        }
        if (tree instanceof GenericFunctionCallContext ctx && functionAction(ctx) == StatementType.DATA_IMPORT) {
            this.types.add(StatementType.UNSAFE);
        }
        if (tree instanceof SelectStatementContext && containsDataExport(tree)) {
            this.types.add(StatementType.SELECT);
            if (containsProcedureAnalyse(tree)) {
                this.types.add(StatementType.PERFORMANCE);
            }
        } else if (tree instanceof FlushStatementContext ctx) {
            flushTypes(ctx).forEach(this.types::add);
        } else if (tree instanceof AnalyzeTableContext ctx && ctx.PARTITION() != null) {
            this.types.add(StatementType.ADMIN_PARTITION);
        } else if (tree instanceof ResetOptionsContext ctx) {
            resetTypes(ctx).forEach(this.types::add);
        } else if (tree instanceof CloneStatementContext ctx && ctx.INSTANCE() != null && ctx.cloneDataDirectory() == null) {
            this.types.add(StatementType.UNSAFE);
        } else if (tree instanceof FullDescribeStatementContext ctx && ctx.LOCAL_ID() != null) {
            this.types.add(StatementType.SESSION_VARIABLE_RW);
        } else if (tree instanceof DiagnosticsStatementContext) {
            this.types.add(StatementType.SESSION_VARIABLE_RW);
        } else if (tree instanceof SetTransactionContext ctx) {
            if (ctx.setTransactionStatement().GLOBAL() != null) {
                this.types.add(StatementType.SYSTEM_SETTING_WRITE);
            } else if (ctx.setTransactionStatement().SESSION() != null) {
                this.types.add(StatementType.SESSION_SETTING_WRITE);
            }
        } else if (tree instanceof CreateProcedureContext ctx && ctx.routineOption().stream().anyMatch(option -> option instanceof RoutineCommentContext)) {
            this.types.add(StatementType.COMMENT_PROG_OBJ);
        } else if (tree instanceof CreateFunctionContext ctx && ctx.routineOption().stream().anyMatch(option -> option instanceof RoutineCommentContext)) {
            this.types.add(StatementType.COMMENT_PROG_OBJ);
        } else if (tree instanceof AlterProcedureContext ctx && ctx.alterRoutineOption().stream().anyMatch(option -> option.COMMENT() != null)) {
            this.types.add(StatementType.COMMENT_PROG_OBJ);
        } else if (tree instanceof AlterFunctionContext ctx && ctx.alterRoutineOption().stream().anyMatch(option -> option.COMMENT() != null)) {
            this.types.add(StatementType.COMMENT_PROG_OBJ);
        } else if (tree instanceof CreateEventContext ctx && ctx.COMMENT() != null) {
            this.types.add(StatementType.COMMENT_EVENT);
        } else if (tree instanceof AlterEventContext ctx) {
            if (ctx.RENAME() != null) {
                this.types.add(StatementType.RENAME_EVENT);
            }
            if (ctx.COMMENT() != null) {
                this.types.add(StatementType.COMMENT_EVENT);
            }
        } else if (tree instanceof CreateLibraryContext ctx && ctx.libraryCharacteristic().stream().anyMatch(item -> item.COMMENT() != null)) {
            this.types.add(StatementType.COMMENT_LIBRARY);
        } else if (tree instanceof AlterLibraryContext) {
            this.types.add(StatementType.COMMENT_LIBRARY);
        } else if (tree instanceof CreateUserContext ctx && ctx.accountAttributeOption() != null && ctx.accountAttributeOption().COMMENT() != null) {
            this.types.add(StatementType.COMMENT_USER);
        } else if (tree instanceof AlterUserMysqlV57Context ctx && ctx.accountAttributeOption() != null && ctx.accountAttributeOption().COMMENT() != null) {
            this.types.add(StatementType.COMMENT_USER);
        } else if (tree instanceof AlterTablespaceContext ctx && ctx.RENAME() != null) {
            this.types.add(StatementType.RENAME_TABLESPACE);
        } else if (tree instanceof AlterByChangeColumnContext ctx && !ctx.oldColumn.getText().equals(ctx.columnDefinition().uid().getText())) {
            this.types.add(StatementType.RENAME_COLUMN);
        }
        collectExternalCodeLifecycleRisk(tree);
    }

    private static boolean hasConstraint(ColumnDefinitionContext context) {
        return context.columnConstraint()
            .stream()
            .anyMatch(constraint -> constraint instanceof PrimaryKeyColumnConstraintContext || constraint instanceof UniqueKeyColumnConstraintContext
                                    || constraint instanceof ReferenceColumnConstraintContext || constraint instanceof CheckColumnConstraintContext);
    }

    private void collectExternalCodeLifecycleRisk(ParseTree tree) {
        if (tree instanceof CreateUdfFunctionContext || tree instanceof CreateFunctionContext ctx && usesExternalCode(ctx)
            || tree instanceof CreateProcedureContext ctx && usesExternalCode(ctx) || tree instanceof AlterFunctionContext ctx && usesExternalCode(ctx)
            || tree instanceof AlterProcedureContext ctx && usesExternalCode(ctx)) {
            this.externalCodeLifecycleRisk = true;
        }
    }

    private static boolean usesExternalCode(CreateFunctionContext ctx) {
        return ctx.routineUsingClause() != null || ctx.routineOption().stream().anyMatch(RoutineExternalLanguageContext.class::isInstance);
    }

    private static boolean usesExternalCode(CreateProcedureContext ctx) {
        return ctx.routineUsingClause() != null || ctx.routineOption().stream().anyMatch(RoutineExternalLanguageContext.class::isInstance);
    }

    private static boolean usesExternalCode(AlterFunctionContext ctx) {
        return ctx.alterRoutineUsingClause() != null || ctx.alterRoutineOption().stream().anyMatch(option -> option.LANGUAGE() != null && option.SQL() == null);
    }

    private static boolean usesExternalCode(AlterProcedureContext ctx) {
        return ctx.alterRoutineUsingClause() != null || ctx.alterRoutineOption().stream().anyMatch(option -> option.LANGUAGE() != null && option.SQL() == null);
    }

    private void collectTableRead(AtomTableItemContext ctx) {
        FullIdContext fullId = ctx.tableName().fullId();
        if (fullId == null || fullId.uid().isEmpty()) {
            this.ordinaryTableRead = true;
            return;
        }
        if (fullId.DOT() == null) {
            if (!this.cteNames.contains(normalizeIdentifier(fullId.uid(0).getText()))) {
                this.ordinaryTableRead = true;
            }
            return;
        }
        String object = fullId.identifierAfterDot != null ? fullId.identifierAfterDot.getText() : fullId.uid(fullId.uid().size() - 1).getText();
        if (resources.isMetadataTable(fullId.uid(0).getText(), object, this.version)) {
            this.metadataTableRead = true;
        } else {
            this.ordinaryTableRead = true;
        }
    }

    private static String normalizeIdentifier(String name) {
        if (name == null) {
            return "";
        }
        String normalized = name.trim();
        if (normalized.length() >= 2) {
            char quote = normalized.charAt(0);
            if ((quote == '`' || quote == '"') && normalized.charAt(normalized.length() - 1) == quote) {
                normalized = normalized.substring(1, normalized.length() - 1);
            }
        }
        return normalized.toLowerCase(Locale.ROOT);
    }

    private void collectNestedLockActions(ParseTree tree) {
        for (int i = 0; i < tree.getChildCount(); i++) {
            ParseTree child = tree.getChild(i);
            collectLockAction(child);
            collectNestedLockActions(child);
        }
    }

    private void collectNestedTableReads(ParseTree tree) {
        for (int i = 0; i < tree.getChildCount(); i++) {
            ParseTree child = tree.getChild(i);
            if (child instanceof AtomTableItemContext ctx) {
                collectTableRead(ctx);
            }
            collectNestedTableReads(child);
        }
    }

    private void collectLockAction(ParseTree tree) {
        if (tree instanceof LockClauseContext) {
            this.types.add(StatementType.QUERY_LOCK);
        } else if (tree instanceof GenericFunctionCallContext ctx && isSessionLockFunction(ctx)) {
            this.types.add(StatementType.SESSION_LOCK);
        }
    }

    private boolean isSessionLockFunction(GenericFunctionCallContext ctx) {
        return functionAction(ctx) == StatementType.SESSION_LOCK;
    }

    private static Set<StatementType> flushTypes(FlushStatementContext ctx) {
        Set<StatementType> result = new LinkedHashSet<>();
        if (ctx.flushTablesOption() != null) {
            FlushTablesOptionContext tablesOption = ctx.flushTablesOption();
            if (tablesOption.flushTableOption() != null && tablesOption.flushTableOption().EXPORT() != null) {
                result.add(StatementType.DATA_EXPORT);
                result.add(StatementType.SESSION_LOCK);
            } else {
                result.add(StatementType.ADMIN_TABLE);
                if (tablesOption.WITH() != null || tablesOption.flushTableOption() != null && tablesOption.flushTableOption().WITH() != null) {
                    result.add(StatementType.SESSION_LOCK);
                }
            }
            return result;
        }

        ctx.flushOption().stream().map(TiDBSplitVisitor::flushOptionType).forEach(result::add);
        if (result.isEmpty()) {
            result.add(StatementType.SYSTEM_SETTING_WRITE);
        }
        return result;
    }

    private static StatementType flushOptionType(FlushOptionContext option) {
        if (option.LOGS() != null) {
            return StatementType.MAINTAIN_LOG;
        }
        if (option.CLIENT_ERRORS_SUMMARY() != null || option.HOSTS() != null || option.OPTIMIZER_COSTS() != null || option.QUERY() != null || option.STATUS() != null
            || option.USER_RESOURCES() != null) {
            return StatementType.ADMIN_PERFORMANCE;
        }
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    private static Set<StatementType> resetTypes(ResetOptionsContext ctx) {
        Set<StatementType> result = new LinkedHashSet<>();
        for (ResetOptionContext option : ctx.resetOption()) {
            if (option.SLAVE() != null || option.REPLICA() != null) {
                result.add(StatementType.ALTER_REPLICATION);
            } else if (option.MASTER() != null || option.BINARY() != null && option.LOGS() != null) {
                result.add(StatementType.MAINTAIN_LOG);
            } else if (option.QUERY() != null && option.CACHE() != null) {
                result.add(StatementType.ADMIN_PERFORMANCE);
            } else {
                result.add(StatementType.SYSTEM_SETTING_WRITE);
            }
        }
        return result;
    }

    private StatementType selectType(ParseTree tree) {
        // SELECT is the outer action. Export, log, performance, locking and
        // function actions are collected while descending the parse tree and
        // must follow it in the ordered classification set.
        return StatementType.SELECT;
    }

    private StatementType preferredFunctionAction(ParseTree tree) {
        StatementType preferred = null;
        if (tree instanceof GenericFunctionCallContext ctx) {
            preferred = functionAction(ctx);
            if (isManagementFunctionAction(preferred)) {
                return preferred;
            }
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            StatementType action = preferredFunctionAction(tree.getChild(i));
            if (isManagementFunctionAction(action)) {
                return action;
            }
            if (preferred == null && action != null) {
                preferred = action;
            }
        }
        return preferred;
    }

    private static boolean isManagementFunctionAction(StatementType type) {
        return type != null && switch (type) {
            case SYSTEM_SETTING_WRITE, SESSION_SETTING_WRITE, ALTER_REPLICATION, ALTER_POLICY, DROP_POLICY, ADMIN_REPLICATION, ADMIN_LOG, MAINTAIN_LOG, ADMIN_PERFORMANCE -> true;
            default -> false;
        };
    }

    private StatementType functionAction(GenericFunctionCallContext ctx) {
        return resources.functionStatementType(ctx.genericFunction().name.getText(), this.version, ctx.genericFunction().args != null);
    }

    private boolean isUserDefinedFunction(GenericFunctionCallContext ctx) {
        if (!(ctx.genericFunction().name instanceof CustomGenericFunctionNameContext custom) || functionAction(ctx) != null) {
            return false;
        }
        FullIdContext fullId = custom.function.fullId();
        String functionName = fullId.identifierAfterDot != null ? fullId.identifierAfterDot.getText() : fullId.uid(fullId.uid().size() - 1).getText();
        return resources.isUserDefinedFunction(functionName, fullId.DOT() != null, this.version);
    }

    private static boolean containsProcedureAnalyse(ParseTree tree) {
        if (tree instanceof ProcedureAnalyseClauseContext) {
            return true;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            if (containsProcedureAnalyse(tree.getChild(i))) {
                return true;
            }
        }
        return false;
    }

    private static boolean containsDataExport(ParseTree tree) {
        if (tree instanceof SelectIntoDumpFileContext || tree instanceof SelectIntoRemoteFileContext || tree instanceof SelectIntoRemoteParametersContext
            || tree instanceof SelectIntoTextFileContext) {
            return true;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            if (containsDataExport(tree.getChild(i))) {
                return true;
            }
        }
        return false;
    }

    private static boolean isCreateTableDefinitionSubquery(ParseTree tree) {
        if (!(tree instanceof SubqueryStatementContext)) {
            return false;
        }
        ParseTree parent = tree.getParent();
        while (parent != null) {
            if (parent instanceof ColumnCreateTableContext) {
                return true;
            }
            parent = parent.getParent();
        }
        return false;
    }

    private static boolean containsFunctionCall(ParseTree tree) {
        if (tree instanceof FunctionCallContext) {
            return true;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            if (containsFunctionCall(tree.getChild(i))) {
                return true;
            }
        }
        return false;
    }

    private static boolean shouldDescend(ParseTree tree, StatementType type) {
        if (type == StatementType.PERFORMANCE) {
            return tree instanceof GenericFunctionCallContext ctx && isBenchmarkFunction(ctx) || tree instanceof QuerySpecificationSelectContext && containsBenchmarkFunction(tree);
        }
        return type == null || switch (type) {
            case CREATE_TABLE, ALTER_TABLE, CREATE_TABLESPACE, ADD_COLUMN, ALTER_COLUMN, ADD_INDEX, INSERT, UPDATE, DELETE, MERGE, ADMIN, ADMIN_TABLE, BLOCK, DATA_IMPORT,
                    DATA_EXPORT, SESSION_VARIABLE_RW, SESSION_SETTING_WRITE, SYSTEM_SETTING_WRITE, ALTER_REPLICATION, ALTER_POLICY, DROP_POLICY, ADMIN_LOG, LOG_READ,
                    ADMIN_REPLICATION, MAINTAIN_LOG, ADMIN_PERFORMANCE, SELECT, CALL_PROG_OBJ ->
                true;
            default -> false;
        };
    }

    private static boolean containsBenchmarkFunction(ParseTree tree) {
        if (tree instanceof GenericFunctionCallContext ctx && isBenchmarkFunction(ctx)) {
            return true;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            if (containsBenchmarkFunction(tree.getChild(i))) {
                return true;
            }
        }
        return false;
    }

    private static boolean isBenchmarkFunction(GenericFunctionCallContext ctx) {
        return "BENCHMARK".equalsIgnoreCase(ctx.genericFunction().name.getText());
    }

    @Override
    public StatementType visitCreateDatabase(CreateDatabaseContext ctx) {
        return StatementType.CREATE_SCHEMA;
    }

    @Override
    public StatementType visitCreatePlacementPolicy(CreatePlacementPolicyContext ctx) {
        return StatementType.CREATE_POLICY;
    }

    @Override
    public StatementType visitAlterPlacementPolicy(AlterPlacementPolicyContext ctx) {
        return StatementType.ALTER_POLICY;
    }

    @Override
    public StatementType visitDropPlacementPolicy(DropPlacementPolicyContext ctx) {
        return StatementType.DROP_POLICY;
    }

    @Override
    public StatementType visitCreateSequence(CreateSequenceContext ctx) {
        return StatementType.CREATE_SEQUENCE;
    }

    @Override
    public StatementType visitAlterSequence(AlterSequenceContext ctx) {
        return StatementType.ALTER_SEQUENCE;
    }

    @Override
    public StatementType visitDropSequence(DropSequenceContext ctx) {
        return StatementType.DROP_SEQUENCE;
    }

    @Override
    public StatementType visitCheckTable(CheckTableContext ctx) {
        return StatementType.ADMIN_TABLE;
    }

    @Override
    public StatementType visitSplitRegionStatement(SplitRegionStatementContext ctx) {
        return StatementType.ADMIN_TABLE;
    }

    @Override
    public StatementType visitTidbAdminStatement(TidbAdminStatementContext ctx) {
        if (ctx.CHECK() != null || ctx.RECOVER() != null || ctx.REPAIR() != null || ctx.CLEANUP() != null || ctx.CHECKSUM() != null) {
            return StatementType.ADMIN_TABLE;
        }
        return StatementType.ADMIN;
    }

    @Override
    public StatementType visitTidbBindingStatement(TidbBindingStatementContext ctx) {
        return StatementType.ADMIN_PERFORMANCE;
    }

    @Override
    public StatementType visitSetBinding(SetBindingContext ctx) {
        return StatementType.ADMIN_PERFORMANCE;
    }

    @Override
    public StatementType visitQueryWatchStatement(QueryWatchStatementContext ctx) {
        return StatementType.ADMIN_RESOURCE_GROUP;
    }

    @Override
    public StatementType visitPlanReplayerStatement(PlanReplayerStatementContext ctx) {
        return StatementType.ADMIN_PERFORMANCE;
    }

    @Override
    public StatementType visitTidbStatsStatement(TidbStatsStatementContext ctx) {
        return StatementType.ADMIN_PERFORMANCE;
    }

    @Override
    public StatementType visitRecoverTableStatement(RecoverTableStatementContext ctx) {
        return StatementType.ADMIN_TABLE;
    }

    @Override
    public StatementType visitFlashbackStatement(FlashbackStatementContext ctx) {
        return ctx.TABLE() == null ? StatementType.ADMIN : StatementType.ADMIN_TABLE;
    }

    @Override
    public StatementType visitTraceStatement(TraceStatementContext ctx) {
        return StatementType.PERFORMANCE;
    }

    @Override
    public StatementType visitCancelDistributionJobStatement(CancelDistributionJobStatementContext ctx) {
        return StatementType.ADMIN_JOB;
    }

    @Override
    public StatementType visitDistributeTableStatement(DistributeTableStatementContext ctx) {
        return StatementType.ADMIN_TABLE;
    }

    @Override
    public StatementType visitCompactTableStatement(CompactTableStatementContext ctx) {
        return StatementType.ADMIN_TABLE;
    }

    @Override
    public StatementType visitAlterRangeStatement(AlterRangeStatementContext ctx) {
        return StatementType.ALTER_POLICY;
    }

    @Override
    public StatementType visitTidbShowStatement(TidbShowStatementContext ctx) {
        TidbShowCommandContext command = ctx.tidbShowCommand();
        return command.BACKUP() != null || command.BACKUPS() != null || command.BR() != null ? StatementType.MAINTAIN_BACKUP : StatementType.METADATA;
    }

    @Override
    public StatementType visitSplitSyntaxOption(SplitSyntaxOptionContext ctx) {
        return ctx.PARTITION() == null ? null : StatementType.ADMIN_PARTITION;
    }

    @Override
    public StatementType visitSplitPartitionNames(SplitPartitionNamesContext ctx) {
        return StatementType.ADMIN_PARTITION;
    }

    @Override
    public StatementType visitBrieBackup(BrieBackupContext ctx) {
        return StatementType.BACKUP;
    }

    @Override
    public StatementType visitBrieRestore(BrieRestoreContext ctx) {
        return StatementType.RESTORE;
    }

    @Override
    public StatementType visitBrieAdmin(BrieAdminContext ctx) {
        return StatementType.MAINTAIN_BACKUP;
    }

    @Override
    public StatementType visitImportIntoPath(ImportIntoPathContext ctx) {
        return StatementType.DATA_IMPORT;
    }

    @Override
    public StatementType visitImportIntoSelect(ImportIntoSelectContext ctx) {
        return StatementType.INSERT;
    }

    @Override
    public StatementType visitCancelImportJobStatement(CancelImportJobStatementContext ctx) {
        return StatementType.ADMIN_JOB;
    }

    @Override
    public StatementType visitDropLoadDataJob(DropLoadDataJobContext ctx) {
        return StatementType.DROP_JOB;
    }

    @Override
    public StatementType visitAdminLoadDataJob(AdminLoadDataJobContext ctx) {
        return StatementType.ADMIN_JOB;
    }

    @Override
    public StatementType visitLegacyCreateImportStatement(LegacyCreateImportStatementContext ctx) {
        return StatementType.DATA_IMPORT;
    }

    @Override
    public StatementType visitLegacyAlterImportStatement(LegacyAlterImportStatementContext ctx) {
        return StatementType.ADMIN;
    }

    @Override
    public StatementType visitLegacyDropImportStatement(LegacyDropImportStatementContext ctx) {
        return StatementType.ADMIN;
    }

    @Override
    public StatementType visitLegacyResumeImportStatement(LegacyResumeImportStatementContext ctx) {
        return StatementType.ADMIN;
    }

    @Override
    public StatementType visitLegacyStopImportStatement(LegacyStopImportStatementContext ctx) {
        return StatementType.ADMIN;
    }

    @Override
    public StatementType visitLegacyPurgeImportStatement(LegacyPurgeImportStatementContext ctx) {
        return StatementType.ADMIN;
    }

    @Override
    public StatementType visitLegacyShowImportStatement(LegacyShowImportStatementContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitLegacyChangePumpStatement(LegacyChangePumpStatementContext ctx) {
        return StatementType.ALTER_REPLICATION;
    }

    @Override
    public StatementType visitLegacyIndexAdviseStatement(LegacyIndexAdviseStatementContext ctx) {
        return ctx.getText().toUpperCase(Locale.ROOT).startsWith("INDEXADVISELOCALINFILE") ? StatementType.UNSAFE : StatementType.ADMIN_PERFORMANCE;
    }

    @Override
    public StatementType visitNonTransactionalDmlStatement(NonTransactionalDmlStatementContext ctx) {
        return ctx.nonTransactionalDryRun() == null ? null : StatementType.PERFORMANCE;
    }

    @Override
    public StatementType visitTrafficCapture(TrafficCaptureContext ctx) {
        return StatementType.DATA_EXPORT;
    }

    @Override
    public StatementType visitTrafficReplay(TrafficReplayContext ctx) {
        return StatementType.DATA_IMPORT;
    }

    @Override
    public StatementType visitTrafficReplaySource(TrafficReplaySourceContext ctx) {
        return StatementType.UNSAFE;
    }

    @Override
    public StatementType visitTrafficShowJobs(TrafficShowJobsContext ctx) {
        return StatementType.ADMIN;
    }

    @Override
    public StatementType visitTrafficCancelJobs(TrafficCancelJobsContext ctx) {
        return StatementType.ADMIN_JOB;
    }

    @Override
    public StatementType visitRepairTable(RepairTableContext ctx) {
        return StatementType.ADMIN_TABLE;
    }

    @Override
    public StatementType visitCloneStatement(CloneStatementContext ctx) {
        return ctx.LOCAL() != null ? StatementType.DATA_EXPORT : StatementType.DATA_IMPORT;
    }

    @Override
    public StatementType visitRestartStatement(RestartStatementContext ctx) {
        return StatementType.UNSAFE;
    }

    @Override
    public StatementType visitShutdownStatement(ShutdownStatementContext ctx) {
        return StatementType.UNSAFE;
    }

    @Override
    public StatementType visitBinlogStatement(BinlogStatementContext ctx) {
        return StatementType.ADMIN_REPLICATION;
    }

    @Override
    public StatementType visitCacheIndexStatement(CacheIndexStatementContext ctx) {
        return StatementType.ADMIN_PERFORMANCE;
    }

    @Override
    public StatementType visitCreateUdfFunction(CreateUdfFunctionContext ctx) {
        return StatementType.CREATE_PROG_OBJ;
    }

    @Override
    public StatementType visitUninstallPlugin(UninstallPluginContext ctx) {
        return StatementType.DROP_LIBRARY;
    }

    @Override
    public StatementType visitInstallPlugin(InstallPluginContext ctx) {
        return StatementType.CREATE_LIBRARY;
    }

    @Override
    public StatementType visitInstallComponent(InstallComponentContext ctx) {
        return StatementType.CREATE_LIBRARY;
    }

    @Override
    public StatementType visitUninstallComponent(UninstallComponentContext ctx) {
        return StatementType.DROP_LIBRARY;
    }

    @Override
    public StatementType visitSetPassword(SetPasswordContext ctx) {
        return StatementType.ALTER_USER;
    }

    @Override
    public StatementType visitChecksumTable(ChecksumTableContext ctx) {
        return StatementType.ADMIN_TABLE;
    }

    @Override
    public StatementType visitOptimizeTable(OptimizeTableContext ctx) {
        return StatementType.ADMIN_TABLE;
    }

    @Override
    public StatementType visitCreateTablespaceInnodb(CreateTablespaceInnodbContext ctx) {
        return StatementType.CREATE_TABLESPACE;
    }

    @Override
    public StatementType visitCreateUndoTablespace(CreateUndoTablespaceContext ctx) {
        return StatementType.CREATE_TABLESPACE;
    }

    @Override
    public StatementType visitCreateLogfileGroup(CreateLogfileGroupContext ctx) {
        return StatementType.CREATE_LOG;
    }

    @Override
    public StatementType visitAlterUserMysqlV56(AlterUserMysqlV56Context ctx) {
        return StatementType.ALTER_USER;
    }

    @Override
    public StatementType visitAlterUserMysqlV57(AlterUserMysqlV57Context ctx) {
        return StatementType.ALTER_USER;
    }

    @Override
    public StatementType visitAlterUserCurrentUser(AlterUserCurrentUserContext ctx) {
        return StatementType.ALTER_USER;
    }

    @Override
    public StatementType visitAlterUserCurrentUserDiscard(AlterUserCurrentUserDiscardContext ctx) {
        return StatementType.ALTER_USER;
    }

    @Override
    public StatementType visitAlterUserDefaultRole(AlterUserDefaultRoleContext ctx) {
        return StatementType.ALTER_USER;
    }

    @Override
    public StatementType visitAlterUserDiscardOldPassword(AlterUserDiscardOldPasswordContext ctx) {
        return StatementType.ALTER_USER;
    }

    @Override
    public StatementType visitAlterUserMfa(AlterUserMfaContext ctx) {
        return StatementType.ALTER_USER;
    }

    @Override
    public StatementType visitDropTablespace(DropTablespaceContext ctx) {
        return StatementType.DROP_TABLESPACE;
    }

    @Override
    public StatementType visitDropUndoTablespace(DropUndoTablespaceContext ctx) {
        return StatementType.DROP_TABLESPACE;
    }

    @Override
    public StatementType visitDropLogfileGroup(DropLogfileGroupContext ctx) {
        return StatementType.DROP_LOG;
    }

    @Override
    public StatementType visitAlterTablespace(AlterTablespaceContext ctx) {
        return StatementType.ALTER_TABLESPACE;
    }

    @Override
    public StatementType visitAlterUndoTablespace(AlterUndoTablespaceContext ctx) {
        return StatementType.ALTER_TABLESPACE;
    }

    @Override
    public StatementType visitAlterLogfileGroup(AlterLogfileGroupContext ctx) {
        return StatementType.ALTER_LOG;
    }

    @Override
    public StatementType visitAlterInstance(AlterInstanceContext ctx) {
        AlterInstanceActionContext action = ctx.alterInstanceAction();
        if (action.REDO_LOG() != null || action.BINLOG() != null) {
            return StatementType.ADMIN_LOG;
        }
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitCreateTablespaceNdb(CreateTablespaceNdbContext ctx) {
        return StatementType.CREATE_TABLESPACE;
    }

    @Override
    public StatementType visitCreateResourceGroup(CreateResourceGroupContext ctx) {
        return StatementType.CREATE_RESOURCE_GROUP;
    }

    @Override
    public StatementType visitCreateServer(CreateServerContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitCreateSpatialReferenceSystem(CreateSpatialReferenceSystemContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitCreateLibrary(CreateLibraryContext ctx) {
        return StatementType.CREATE_LIBRARY;
    }

    @Override
    public StatementType visitCreateMaskingPolicy(CreateMaskingPolicyContext ctx) {
        return StatementType.CREATE_POLICY;
    }

    @Override
    public StatementType visitAlterResourceGroup(AlterResourceGroupContext ctx) {
        return StatementType.ALTER_RESOURCE_GROUP;
    }

    @Override
    public StatementType visitAlterLibrary(AlterLibraryContext ctx) {
        return StatementType.ALTER_LIBRARY;
    }

    @Override
    public StatementType visitAlterServer(AlterServerContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitDropResourceGroup(DropResourceGroupContext ctx) {
        return StatementType.DROP_RESOURCE_GROUP;
    }

    @Override
    public StatementType visitDropServer(DropServerContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitDropSpatialReferenceSystem(DropSpatialReferenceSystemContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitDropLibrary(DropLibraryContext ctx) {
        return StatementType.DROP_LIBRARY;
    }

    @Override
    public StatementType visitDropMaskingPolicy(DropMaskingPolicyContext ctx) {
        return StatementType.DROP_POLICY;
    }

    @Override
    public StatementType visitSetResourceGroup(SetResourceGroupContext ctx) {
        return StatementType.ADMIN_RESOURCE_GROUP;
    }

    @Override
    public StatementType visitCalibrateResourceStatement(CalibrateResourceStatementContext ctx) {
        return StatementType.ADMIN_RESOURCE_GROUP;
    }

    @Override
    public StatementType visitCreateStatisticsStatement(CreateStatisticsStatementContext ctx) {
        return StatementType.ADMIN_PERFORMANCE;
    }

    @Override
    public StatementType visitDropStatisticsStatement(DropStatisticsStatementContext ctx) {
        return StatementType.ADMIN_PERFORMANCE;
    }

    @Override
    public StatementType visitRecommendIndexStatement(RecommendIndexStatementContext ctx) {
        return StatementType.ADMIN_PERFORMANCE;
    }

    @Override
    public StatementType visitFlushStatsDeltaStatement(FlushStatsDeltaStatementContext ctx) {
        return StatementType.ADMIN_PERFORMANCE;
    }

    @Override
    public StatementType visitSetConfig(SetConfigContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitSignalStatement(SignalStatementContext ctx) {
        return StatementType.PROGRAM_CONTROL;
    }

    @Override
    public StatementType visitResignalStatement(ResignalStatementContext ctx) {
        return StatementType.PROGRAM_CONTROL;
    }

    @Override
    public StatementType visitDiagnosticsStatement(DiagnosticsStatementContext ctx) {
        return StatementType.PERFORMANCE;
    }

    @Override
    public StatementType visitAnalyzeTable(AnalyzeTableContext ctx) {
        return StatementType.ADMIN_TABLE;
    }

    @Override
    public StatementType visitWithSelectStatement(WithSelectStatementContext ctx) {
        return selectType(ctx);
    }

    @Override
    public StatementType visitTableStatement(TableStatementContext ctx) {
        return StatementType.SELECT;
    }

    @Override
    public StatementType visitValuesStatement(ValuesStatementContext ctx) {
        return StatementType.SELECT;
    }

    @Override
    public StatementType visitLoadDataStatement(LoadDataStatementContext ctx) {
        return StatementType.DATA_IMPORT;
    }

    @Override
    public StatementType visitLoadXmlStatement(LoadXmlStatementContext ctx) {
        return StatementType.DATA_IMPORT;
    }

    @Override
    public StatementType visitImportTableStatement(ImportTableStatementContext ctx) {
        return StatementType.DATA_IMPORT;
    }

    @Override
    public StatementType visitDoStatement(DoStatementContext ctx) {
        return StatementType.BLOCK;
    }

    @Override
    public StatementType visitHandlerStatement(HandlerStatementContext ctx) {
        return StatementType.SELECT;
    }

    @Override
    public StatementType visitPrepareStatement(PrepareStatementContext ctx) {
        return StatementType.UNSAFE;
    }

    @Override
    public StatementType visitExecuteStatement(ExecuteStatementContext ctx) {
        return StatementType.UNSAFE;
    }

    @Override
    public StatementType visitDeallocatePrepare(DeallocatePrepareContext ctx) {
        return StatementType.UNSAFE;
    }

    @Override
    public StatementType visitSetTransaction(SetTransactionContext ctx) {
        return StatementType.TRANSACTION;
    }

    @Override
    public StatementType visitSetAutocommit(SetAutocommitContext ctx) {
        return StatementType.SESSION_SETTING_WRITE;
    }

    @Override
    public StatementType visitTransactionStatement(TransactionStatementContext ctx) {
        if (ctx.lockInstance() != null || ctx.unlockInstance() != null) {
            return StatementType.SESSION_LOCK;
        }
        if (ctx.lockTables() != null || ctx.unlockTables() != null) {
            return StatementType.SESSION_LOCK;
        }
        return StatementType.TRANSACTION;
    }

    @Override
    public StatementType visitLockInstance(LockInstanceContext ctx) {
        return StatementType.SESSION_LOCK;
    }

    @Override
    public StatementType visitUnlockInstance(UnlockInstanceContext ctx) {
        return StatementType.SESSION_LOCK;
    }

    @Override
    public StatementType visitDropProcedure(DropProcedureContext ctx) {
        return StatementType.DROP_PROG_OBJ;
    }

    @Override
    public StatementType visitDropTrigger(DropTriggerContext ctx) {
        return StatementType.DROP_TRIGGER;
    }

    @Override
    public StatementType visitDropFunction(DropFunctionContext ctx) {
        return StatementType.DROP_PROG_OBJ;
    }

    @Override
    public StatementType visitDropRole(DropRoleContext ctx) {
        return StatementType.DROP_ROLE;
    }

    @Override
    public StatementType visitDropIndex(DropIndexContext ctx) {
        return StatementType.DROP_INDEX;
    }

    @Override
    public StatementType visitDropDatabase(DropDatabaseContext ctx) {
        return StatementType.DROP_SCHEMA;
    }

    @Override
    public StatementType visitAlterSimpleDatabase(AlterSimpleDatabaseContext ctx) {
        return StatementType.ALTER_SCHEMA;
    }

    @Override
    public StatementType visitAlterUpgradeName(AlterUpgradeNameContext ctx) {
        return StatementType.ALTER_SCHEMA;
    }

    @Override
    public StatementType visitTruncateTable(TruncateTableContext ctx) {
        return StatementType.TRUNCATE_TABLE;
    }

    @Override
    public StatementType visitCopyCreateTable(CopyCreateTableContext ctx) {
        return StatementType.CREATE_TABLE;
    }

    @Override
    public StatementType visitEmptyCreateTable(EmptyCreateTableContext ctx) {
        return StatementType.CREATE_TABLE;
    }

    @Override
    public StatementType visitQueryCreateTable(QueryCreateTableContext ctx) {
        return StatementType.CREATE_TABLE;
    }

    @Override
    public StatementType visitColumnCreateTable(ColumnCreateTableContext ctx) {
        return StatementType.CREATE_TABLE;
    }

    @Override
    public StatementType visitColumnDeclaration(ColumnDeclarationContext ctx) {
        return StatementType.ADD_COLUMN;
    }

    @Override
    public StatementType visitConstraintDeclaration(ConstraintDeclarationContext ctx) {
        return StatementType.ADD_CONSTRAINT;
    }

    @Override
    public StatementType visitIndexDeclaration(IndexDeclarationContext ctx) {
        return StatementType.ADD_INDEX;
    }

    @Override
    public StatementType visitDropTable(DropTableContext ctx) {
        return StatementType.DROP_TABLE;
    }

    @Override
    public StatementType visitAlterTable(AlterTableContext ctx) {
        return StatementType.ALTER_TABLE;
    }

    @Override
    public StatementType visitRenameTable(RenameTableContext ctx) {
        return StatementType.RENAME_TABLE;
    }

    @Override
    public StatementType visitCreateTrigger(CreateTriggerContext ctx) {
        return StatementType.CREATE_TRIGGER;
    }

    @Override
    public StatementType visitCreateView(CreateViewContext ctx) {
        return ctx.REPLACE() == null ? StatementType.CREATE_VIEW : StatementType.ALTER_VIEW;
    }

    @Override
    public StatementType visitAlterView(AlterViewContext ctx) {
        return StatementType.ALTER_VIEW;
    }

    @Override
    public StatementType visitDropView(DropViewContext ctx) {
        return StatementType.DROP_VIEW;
    }

    @Override
    public StatementType visitFullDescribeStatement(FullDescribeStatementContext ctx) {
        if (ctx.analyze != null) {
            return StatementType.UNSAFE;
        }
        return StatementType.SELECT;
    }

    @Override
    public StatementType visitCreateEvent(CreateEventContext ctx) {
        return StatementType.CREATE_EVENT;
    }

    @Override
    public StatementType visitDropEvent(DropEventContext ctx) {
        return StatementType.DROP_EVENT;
    }

    @Override
    public StatementType visitCreateIndex(CreateIndexContext ctx) {
        return StatementType.ADD_INDEX;
    }

    @Override
    public StatementType visitAlterFunction(AlterFunctionContext ctx) {
        return StatementType.ALTER_PROG_OBJ;
    }

    @Override
    public StatementType visitAlterProcedure(AlterProcedureContext ctx) {
        return StatementType.ALTER_PROG_OBJ;
    }

    @Override
    public StatementType visitCreateFunction(CreateFunctionContext ctx) {
        return StatementType.CREATE_PROG_OBJ;
    }

    @Override
    public StatementType visitCreateProcedure(CreateProcedureContext ctx) {
        return StatementType.CREATE_PROG_OBJ;
    }

    @Override
    public StatementType visitAlterEvent(AlterEventContext ctx) {
        return StatementType.ALTER_EVENT;
    }

    @Override
    public StatementType visitQuerySpecificationSelect(QuerySpecificationSelectContext ctx) {
        return selectType(ctx);
    }

    @Override
    public StatementType visitQueryExpressionSelect(QueryExpressionSelectContext ctx) {
        return selectType(ctx);
    }

    @Override
    public StatementType visitUnionTableValueSelect(UnionTableValueSelectContext ctx) {
        return selectType(ctx);
    }

    @Override
    public StatementType visitProcedureAnalyseClause(ProcedureAnalyseClauseContext ctx) {
        return StatementType.PERFORMANCE;
    }

    @Override
    public StatementType visitGenericFunctionCall(GenericFunctionCallContext ctx) {
        return functionAction(ctx);
    }

    @Override
    public StatementType visitMysqlVariable(MysqlVariableContext ctx) {
        if (ctx.LOCAL_ID() != null) {
            return StatementType.SESSION_VARIABLE_RW;
        }
        String variable = ctx.GLOBAL_ID().getText().toUpperCase(Locale.ROOT);
        if (variable.startsWith("@@GLOBAL.") || variable.startsWith("@@PERSIST.") || variable.startsWith("@@PERSIST_ONLY.")) {
            return null;
        }
        return StatementType.SESSION_VARIABLE_RW;
    }

    @Override
    public StatementType visitSelectIntoVariables(SelectIntoVariablesContext ctx) {
        return ctx.assignmentField().stream().anyMatch(field -> field.LOCAL_ID() != null) ? StatementType.SESSION_VARIABLE_RW : null;
    }

    @Override
    public StatementType visitSelectIntoDumpFile(SelectIntoDumpFileContext ctx) {
        return StatementType.DATA_EXPORT;
    }

    @Override
    public StatementType visitSelectIntoTextFile(SelectIntoTextFileContext ctx) {
        return StatementType.DATA_EXPORT;
    }

    @Override
    public StatementType visitSelectIntoRemoteFile(SelectIntoRemoteFileContext ctx) {
        return StatementType.DATA_EXPORT;
    }

    @Override
    public StatementType visitSelectIntoRemoteParameters(SelectIntoRemoteParametersContext ctx) {
        return StatementType.DATA_EXPORT;
    }

    @Override
    public StatementType visitAssignmentField(AssignmentFieldContext ctx) {
        return ctx.LOCAL_ID() == null ? null : StatementType.SESSION_VARIABLE_RW;
    }

    @Override
    public StatementType visitSelectExpressionElement(SelectExpressionElementContext ctx) {
        return ctx.LOCAL_ID() != null && ctx.VAR_ASSIGN() != null ? StatementType.SESSION_VARIABLE_RW : null;
    }

    @Override
    public StatementType visitVariableAssignmentExpression(VariableAssignmentExpressionContext ctx) {
        return StatementType.SESSION_VARIABLE_RW;
    }

    @Override
    public StatementType visitNestedVariableAssignmentExpression(NestedVariableAssignmentExpressionContext ctx) {
        return StatementType.SESSION_VARIABLE_RW;
    }

    @Override
    public StatementType visitUpdateStatement(UpdateStatementContext ctx) {
        return StatementType.UPDATE;
    }

    @Override
    public StatementType visitInsertStatement(InsertStatementContext ctx) {
        return ctx.duplicatedFirst == null ? StatementType.INSERT : StatementType.MERGE;
    }

    @Override
    public StatementType visitReplaceStatement(ReplaceStatementContext ctx) {
        return StatementType.MERGE;
    }

    @Override
    public StatementType visitDeleteStatement(DeleteStatementContext ctx) {
        return StatementType.DELETE;
    }

    @Override
    public StatementType visitCallStatement(CallStatementContext ctx) {
        return StatementType.CALL_PROG_OBJ;
    }

    @Override
    public StatementType visitUseStatement(UseStatementContext ctx) {
        return StatementType.SWITCH_SCHEMA;
    }

    @Override
    public StatementType visitHelpStatement(HelpStatementContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitSimpleDescribeStatement(SimpleDescribeStatementContext ctx) {
        return "EXPLAIN".equalsIgnoreCase(ctx.command.getText()) ? StatementType.SELECT : StatementType.METADATA;
    }

    @Override
    public StatementType visitCreateUser(CreateUserContext ctx) {
        return StatementType.CREATE_USER;
    }

    @Override
    public StatementType visitDropUser(DropUserContext ctx) {
        return StatementType.DROP_USER;
    }

    @Override
    public StatementType visitRenameUser(RenameUserContext ctx) {
        return StatementType.RENAME_USER;
    }

    @Override
    public StatementType visitGrantProxy(GrantProxyContext ctx) {
        return StatementType.GRANT;
    }

    @Override
    public StatementType visitRevokeProxy(RevokeProxyContext ctx) {
        return StatementType.REVOKE;
    }

    @Override
    public StatementType visitGrantStatement(GrantStatementContext ctx) {
        return StatementType.GRANT;
    }

    @Override
    public StatementType visitRevokeStatement(RevokeStatementContext ctx) {
        return StatementType.REVOKE;
    }

    @Override
    public StatementType visitCreateRole(CreateRoleContext ctx) {
        return StatementType.CREATE_ROLE;
    }

    @Override
    public StatementType visitShowMasterLogs(ShowMasterLogsContext ctx) {
        return StatementType.LOG_READ;
    }

    @Override
    public StatementType visitShowBinaryLogStatus(ShowBinaryLogStatusContext ctx) {
        return StatementType.LOG_READ;
    }

    @Override
    public StatementType visitShowSessionStates(ShowSessionStatesContext ctx) {
        return StatementType.PERFORMANCE;
    }

    @Override
    public StatementType visitShowRuntimeStatistics(ShowRuntimeStatisticsContext ctx) {
        return ctx.RESTORES() != null ? StatementType.MAINTAIN_BACKUP : StatementType.PERFORMANCE;
    }

    @Override
    public StatementType visitShowSessionBindings(ShowSessionBindingsContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitShowCharset(ShowCharsetContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitShowBinlogEvents(ShowBinlogEventsContext ctx) {
        return StatementType.LOG_READ;
    }

    @Override
    public StatementType visitShowRelayLogEvents(ShowRelayLogEventsContext ctx) {
        return StatementType.LOG_READ;
    }

    @Override
    public StatementType visitShowObjectFilter(ShowObjectFilterContext ctx) {
        String entity = ctx.showCommonEntity().getText();
        if (entity.equalsIgnoreCase("STATUS") || entity.equalsIgnoreCase("GLOBALSTATUS") || entity.equalsIgnoreCase("SESSIONSTATUS") || entity.equalsIgnoreCase("LOCALSTATUS")) {
            return StatementType.PERFORMANCE;
        }
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitShowColumns(ShowColumnsContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitShowTables(ShowTablesContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitShowCreateDb(ShowCreateDbContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitShowCreateFullIdObject(ShowCreateFullIdObjectContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitShowCreateMaskingPolicy(ShowCreateMaskingPolicyContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitShowCreateUser(ShowCreateUserContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitShowEngine(ShowEngineContext ctx) {
        if (ctx.engineOption.getType() == LOGS) {
            return StatementType.LOG_READ;
        }
        return StatementType.PERFORMANCE;
    }

    @Override
    public StatementType visitShowEngines(ShowEnginesContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitShowStatus(ShowStatusContext ctx) {
        return StatementType.LOG_READ;
    }

    @Override
    public StatementType visitShowPlugins(ShowPluginsContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitShowPrivileges(ShowPrivilegesContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitShowProcessList(ShowProcessListContext ctx) {
        return StatementType.PERFORMANCE;
    }

    @Override
    public StatementType visitShowProfiles(ShowProfilesContext ctx) {
        return StatementType.PERFORMANCE;
    }

    @Override
    public StatementType visitShowSlaveHosts(ShowSlaveHostsContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitShowErrors(ShowErrorsContext ctx) {
        return StatementType.PERFORMANCE;
    }

    @Override
    public StatementType visitShowCountErrors(ShowCountErrorsContext ctx) {
        return StatementType.PERFORMANCE;
    }

    @Override
    public StatementType visitShowSchemaFilter(ShowSchemaFilterContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitShowRoutine(ShowRoutineContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitShowLibraryStatus(ShowLibraryStatusContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitShowGrants(ShowGrantsContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitShowIndexes(ShowIndexesContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitShowOpenTables(ShowOpenTablesContext ctx) {
        return StatementType.PERFORMANCE;
    }

    @Override
    public StatementType visitShowProfile(ShowProfileContext ctx) {
        return StatementType.PERFORMANCE;
    }

    @Override
    public StatementType visitResetMaster(ResetMasterContext ctx) {
        return StatementType.MAINTAIN_LOG;
    }

    @Override
    public StatementType visitResetBinaryLogsAndGtids(ResetBinaryLogsAndGtidsContext ctx) {
        return StatementType.MAINTAIN_LOG;
    }

    @Override
    public StatementType visitResetSlave(ResetSlaveContext ctx) {
        return StatementType.ALTER_REPLICATION;
    }

    @Override
    public StatementType visitResetReplica(ResetReplicaContext ctx) {
        return StatementType.ALTER_REPLICATION;
    }

    @Override
    public StatementType visitResetQueryCache(ResetQueryCacheContext ctx) {
        return StatementType.ADMIN_PERFORMANCE;
    }

    @Override
    public StatementType visitResetOptions(ResetOptionsContext ctx) {
        return resetTypes(ctx).stream().findFirst().orElse(StatementType.SYSTEM_SETTING_WRITE);
    }

    @Override
    public StatementType visitResetPersist(ResetPersistContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitFlushStatement(FlushStatementContext ctx) {
        return flushTypes(ctx).stream().findFirst().orElse(StatementType.SYSTEM_SETTING_WRITE);
    }

    @Override
    public StatementType visitShowReplicaStatus(ShowReplicaStatusContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitShowParseTree(ShowParseTreeContext ctx) {
        return StatementType.PERFORMANCE;
    }

    @Override
    public StatementType visitShowReplicas(ShowReplicasContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitKillStatement(KillStatementContext ctx) {
        return StatementType.ADMIN;
    }

    @Override
    public StatementType visitLoadIndexIntoCache(LoadIndexIntoCacheContext ctx) {
        return StatementType.ADMIN_PERFORMANCE;
    }

    @Override
    public StatementType visitPurgeBinaryLogs(PurgeBinaryLogsContext ctx) {
        return StatementType.MAINTAIN_LOG;
    }

    @Override
    public StatementType visitChangeMaster(ChangeMasterContext ctx) {
        return StatementType.ALTER_REPLICATION;
    }

    @Override
    public StatementType visitChangeReplicationSource(ChangeReplicationSourceContext ctx) {
        return StatementType.ALTER_REPLICATION;
    }

    @Override
    public StatementType visitChangeReplicationFilter(ChangeReplicationFilterContext ctx) {
        return StatementType.ALTER_REPLICATION;
    }

    @Override
    public StatementType visitStartSlave(StartSlaveContext ctx) {
        return StatementType.ALTER_REPLICATION;
    }

    @Override
    public StatementType visitStartReplica(StartReplicaContext ctx) {
        return StatementType.ALTER_REPLICATION;
    }

    @Override
    public StatementType visitStopSlave(StopSlaveContext ctx) {
        return StatementType.ALTER_REPLICATION;
    }

    @Override
    public StatementType visitStopReplica(StopReplicaContext ctx) {
        return StatementType.ALTER_REPLICATION;
    }

    @Override
    public StatementType visitStartGroupReplication(StartGroupReplicationContext ctx) {
        return StatementType.ALTER_REPLICATION;
    }

    @Override
    public StatementType visitStopGroupReplication(StopGroupReplicationContext ctx) {
        return StatementType.ALTER_REPLICATION;
    }

    @Override
    public StatementType visitXaStartTransaction(XaStartTransactionContext ctx) {
        return StatementType.TRANSACTION;
    }

    @Override
    public StatementType visitXaEndTransaction(XaEndTransactionContext ctx) {
        return StatementType.TRANSACTION;
    }

    @Override
    public StatementType visitXaPrepareStatement(XaPrepareStatementContext ctx) {
        return StatementType.TRANSACTION;
    }

    @Override
    public StatementType visitXaCommitWork(XaCommitWorkContext ctx) {
        return StatementType.TRANSACTION;
    }

    @Override
    public StatementType visitXaRollbackWork(XaRollbackWorkContext ctx) {
        return StatementType.TRANSACTION;
    }

    @Override
    public StatementType visitXaRecoverWork(XaRecoverWorkContext ctx) {
        return StatementType.TRANSACTION;
    }

    @Override
    public StatementType visitShowSlaveStatus(ShowSlaveStatusContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitSetVariable(SetVariableContext ctx) {
        List<SetVariableAssignmentContext> variables = ctx.setVariableAssignment().stream().filter(assignment -> assignment.variableClause() != null).toList();
        if (variables.isEmpty()) {
            return StatementType.SESSION_SETTING_WRITE;
        }
        boolean onlyUserVariables = variables.stream().allMatch(assignment -> {
            String variable = assignment.variableClause().getText();
            return variable.startsWith("@") && !variable.startsWith("@@");
        });
        if (onlyUserVariables) {
            return StatementType.SESSION_VARIABLE_RW;
        }
        boolean replicationSetting = variables.stream().anyMatch(assignment -> {
            String variable = assignment.variableClause().getText().toUpperCase();
            return variable.contains("GTID_") || variable.contains("SLAVE_") || variable.contains("REPLICA_");
        });
        if (replicationSetting) {
            return StatementType.ALTER_REPLICATION;
        }
        boolean systemSetting = variables.stream().anyMatch(assignment -> {
            VariableClauseContext variable = assignment.variableClause();
            String text = variable.getText().toUpperCase();
            return text.startsWith("@@GLOBAL.") || text.startsWith("@@PERSIST.") || text.startsWith("@@PERSIST_ONLY.") || variable.GLOBAL() != null
                   || variable.persistScope() != null;
        });
        return systemSetting ? StatementType.SYSTEM_SETTING_WRITE : StatementType.SESSION_SETTING_WRITE;
    }

    @Override
    public StatementType visitSetSessionStates(SetSessionStatesContext ctx) {
        return StatementType.SESSION_SETTING_WRITE;
    }

    @Override
    public StatementType visitSetCharset(SetCharsetContext ctx) {
        return StatementType.SESSION_SETTING_WRITE;
    }

    @Override
    public StatementType visitSetNames(SetNamesContext ctx) {
        return StatementType.SESSION_SETTING_WRITE;
    }

    @Override
    public StatementType visitSetRole(SetRoleContext ctx) {
        return StatementType.SWITCH_ROLE;
    }

    @Override
    public StatementType visitSetDefaultRole(SetDefaultRoleContext ctx) {
        return StatementType.ALTER_USER;
    }

    @Override
    public StatementType visitAlterByAddColumn(AlterByAddColumnContext ctx) {
        return StatementType.ADD_COLUMN;
    }

    @Override
    public StatementType visitAlterByAddColumns(AlterByAddColumnsContext ctx) {
        return StatementType.ADD_COLUMN;
    }

    @Override
    public StatementType visitAlterByAddIndex(AlterByAddIndexContext ctx) {
        return StatementType.ADD_INDEX;
    }

    @Override
    public StatementType visitAlterByAddUniqueKey(AlterByAddUniqueKeyContext ctx) {
        return ctx.CONSTRAINT() == null ? StatementType.ADD_INDEX : StatementType.ADD_CONSTRAINT;
    }

    @Override
    public StatementType visitAlterByAddSpecialIndex(AlterByAddSpecialIndexContext ctx) {
        return StatementType.ADD_INDEX;
    }

    @Override
    public StatementType visitAlterByAddPrimaryKey(AlterByAddPrimaryKeyContext ctx) {
        return StatementType.ADD_CONSTRAINT;
    }

    @Override
    public StatementType visitAlterByAddForeignKey(AlterByAddForeignKeyContext ctx) {
        return StatementType.ADD_CONSTRAINT;
    }

    @Override
    public StatementType visitAlterByAddCheckTableConstraint(AlterByAddCheckTableConstraintContext ctx) {
        return StatementType.ADD_CONSTRAINT;
    }

    @Override
    public StatementType visitAlterByStatsExtended(AlterByStatsExtendedContext ctx) {
        return StatementType.ADMIN_PERFORMANCE;
    }

    @Override
    public StatementType visitAlterBySetMaskingPolicy(AlterBySetMaskingPolicyContext ctx) {
        return StatementType.ALTER_COLUMN;
    }

    @Override
    public StatementType visitAlterByDropMaskingPolicy(AlterByDropMaskingPolicyContext ctx) {
        return StatementType.ALTER_COLUMN;
    }

    @Override
    public StatementType visitAlterByChangeDefault(AlterByChangeDefaultContext ctx) {
        return StatementType.ALTER_COLUMN;
    }

    @Override
    public StatementType visitAlterByChangeColumn(AlterByChangeColumnContext ctx) {
        return StatementType.ALTER_COLUMN;
    }

    @Override
    public StatementType visitAlterByModifyColumn(AlterByModifyColumnContext ctx) {
        return StatementType.ALTER_COLUMN;
    }

    @Override
    public StatementType visitAlterByRenameColumn(AlterByRenameColumnContext ctx) {
        return StatementType.RENAME_COLUMN;
    }

    @Override
    public StatementType visitAlterByDropColumn(AlterByDropColumnContext ctx) {
        return StatementType.DROP_COLUMN;
    }

    @Override
    public StatementType visitAlterByAlterConstraintEnforcement(AlterByAlterConstraintEnforcementContext ctx) {
        return StatementType.ALTER_CONSTRAINT;
    }

    @Override
    public StatementType visitAlterByDropConstraintCheck(AlterByDropConstraintCheckContext ctx) {
        return StatementType.DROP_CONSTRAINT;
    }

    @Override
    public StatementType visitAlterByDropPrimaryKey(AlterByDropPrimaryKeyContext ctx) {
        return StatementType.DROP_CONSTRAINT;
    }

    @Override
    public StatementType visitAlterByDropForeignKey(AlterByDropForeignKeyContext ctx) {
        return StatementType.DROP_CONSTRAINT;
    }

    @Override
    public StatementType visitAlterByDropIndex(AlterByDropIndexContext ctx) {
        return StatementType.DROP_INDEX;
    }

    @Override
    public StatementType visitAlterByRenameIndex(AlterByRenameIndexContext ctx) {
        return StatementType.RENAME_INDEX;
    }

    @Override
    public StatementType visitAlterByAlterIndexVisibility(AlterByAlterIndexVisibilityContext ctx) {
        return StatementType.ALTER_INDEX;
    }

    @Override
    public StatementType visitAlterByRename(AlterByRenameContext ctx) {
        return StatementType.RENAME_TABLE;
    }

    @Override
    public StatementType visitAlterByDiscardTablespace(AlterByDiscardTablespaceContext ctx) {
        return StatementType.ADMIN_TABLE;
    }

    @Override
    public StatementType visitAlterByImportTablespace(AlterByImportTablespaceContext ctx) {
        return StatementType.ADMIN_TABLE;
    }

    @Override
    public StatementType visitAlterByDisableKeys(AlterByDisableKeysContext ctx) {
        return StatementType.ADMIN_TABLE;
    }

    @Override
    public StatementType visitAlterByEnableKeys(AlterByEnableKeysContext ctx) {
        return StatementType.ADMIN_TABLE;
    }

    @Override
    public StatementType visitAlterByOrder(AlterByOrderContext ctx) {
        return StatementType.ADMIN_TABLE;
    }

    @Override
    public StatementType visitAlterByForce(AlterByForceContext ctx) {
        return StatementType.ADMIN_TABLE;
    }

    @Override
    public StatementType visitAlterByAddPartition(AlterByAddPartitionContext ctx) {
        return StatementType.ADD_PARTITION;
    }

    @Override
    public StatementType visitAlterByDropPartition(AlterByDropPartitionContext ctx) {
        return StatementType.DROP_PARTITION;
    }

    @Override
    public StatementType visitAlterByTruncatePartition(AlterByTruncatePartitionContext ctx) {
        return StatementType.TRUNCATE_PARTITION;
    }

    @Override
    public StatementType visitAlterByCoalescePartition(AlterByCoalescePartitionContext ctx) {
        return StatementType.ALTER_PARTITION;
    }

    @Override
    public StatementType visitAlterByReorganizePartition(AlterByReorganizePartitionContext ctx) {
        return StatementType.ALTER_PARTITION;
    }

    @Override
    public StatementType visitAlterByExchangePartition(AlterByExchangePartitionContext ctx) {
        return StatementType.ALTER_PARTITION;
    }

    @Override
    public StatementType visitAlterByRemovePartitioning(AlterByRemovePartitioningContext ctx) {
        return StatementType.ALTER_PARTITION;
    }

    @Override
    public StatementType visitAlterByUpgradePartitioning(AlterByUpgradePartitioningContext ctx) {
        return StatementType.ALTER_PARTITION;
    }

    @Override
    public StatementType visitAlterByAnalyzePartition(AlterByAnalyzePartitionContext ctx) {
        return StatementType.ADMIN_PARTITION;
    }

    @Override
    public StatementType visitAlterByCheckPartition(AlterByCheckPartitionContext ctx) {
        return StatementType.ADMIN_PARTITION;
    }

    @Override
    public StatementType visitAlterByOptimizePartition(AlterByOptimizePartitionContext ctx) {
        return StatementType.ADMIN_PARTITION;
    }

    @Override
    public StatementType visitAlterByDiscardPartition(AlterByDiscardPartitionContext ctx) {
        return StatementType.ADMIN_PARTITION;
    }

    @Override
    public StatementType visitAlterByImportPartition(AlterByImportPartitionContext ctx) {
        return StatementType.ADMIN_PARTITION;
    }

    @Override
    public StatementType visitAlterByRebuildPartition(AlterByRebuildPartitionContext ctx) {
        return StatementType.ADMIN_PARTITION;
    }

    @Override
    public StatementType visitAlterByRepairPartition(AlterByRepairPartitionContext ctx) {
        return StatementType.ADMIN_PARTITION;
    }

    @Override
    public StatementType visitAlterBySecondaryLoad(AlterBySecondaryLoadContext ctx) {
        return StatementType.ADMIN_PARTITION;
    }

    @Override
    public StatementType visitAlterBySecondaryUnload(AlterBySecondaryUnloadContext ctx) {
        return StatementType.ADMIN_PARTITION;
    }

    @Override
    public StatementType visitTableOptionComment(TableOptionCommentContext ctx) {
        return StatementType.COMMENT_TABLE;
    }

    @Override
    public StatementType visitCommentColumnConstraint(CommentColumnConstraintContext ctx) {
        return StatementType.COMMENT_COLUMN;
    }

    @Override
    public StatementType visitCommonIndexOption(CommonIndexOptionContext ctx) {
        return ctx.COMMENT() == null ? null : StatementType.COMMENT_INDEX;
    }

    @Override
    public StatementType visitPartitionOptionComment(PartitionOptionCommentContext ctx) {
        return StatementType.COMMENT_PARTITION;
    }

    @Override
    public StatementType visitTablespaceOption(TablespaceOptionContext ctx) {
        return ctx.COMMENT() == null ? null : StatementType.COMMENT_TABLESPACE;
    }

    @Override
    public StatementType visitLogfileGroupOption(LogfileGroupOptionContext ctx) {
        return null;
    }

    public StatementType visitChildren(RuleNode node) {
        if (this.currentNodeOnly) {
            return null;
        }

        int n = node.getChildCount();

        for (int i = 0; i < n; ++i) {
            ParseTree c = node.getChild(i);
            StatementType result = c.accept(this);
            if (result != null) {
                return result;
            }
        }

        return null;
    }

}
