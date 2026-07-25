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
package com.clougence.sql.mysql.parser;

import static com.clougence.sql.mysql.parser.antlr.MySqlParser.*;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;

import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.sql.mysql.analysis.reference.MySqlResourceRegistry;
import com.clougence.sql.mysql.parser.antlr.MySqlParserBaseVisitor;

public class MySplitVisitor extends MySqlParserBaseVisitor<SplitQueryType> {

    private final Set<SplitQueryType>   types    = new LinkedHashSet<>();
    private final Set<String>           cteNames = new LinkedHashSet<>();
    private final MySqlVersion          version;
    private final MySqlResourceRegistry resources;
    private boolean                     currentNodeOnly;
    private boolean                     externalCodeLifecycleRisk;
    private boolean                     metadataTableRead;
    private boolean                     ordinaryTableRead;

    public MySplitVisitor(){
        this(MySqlVersion.LATEST, MySqlResourceRegistry.instance());
    }

    public MySplitVisitor(MySqlVersion version){
        this(version, MySqlResourceRegistry.instance());
    }

    public MySplitVisitor(MySqlVersion version, MySqlResourceRegistry resources){
        this.version = version == null ? MySqlVersion.LATEST : version;
        this.resources = Objects.requireNonNull(resources, "resources");
    }

    @Override
    public SplitQueryType visit(ParseTree tree) {
        collectTypes(tree);
        return this.types.stream().findFirst().orElse(null);
    }

    public Set<SplitQueryType> collectTypes(ParseTree tree) {
        this.types.clear();
        this.cteNames.clear();
        this.externalCodeLifecycleRisk = false;
        this.metadataTableRead = false;
        this.ordinaryTableRead = false;
        collectNode(tree);
        if (this.metadataTableRead) {
            this.types.add(SplitQueryType.METADATA);
            if (!this.ordinaryTableRead) {
                this.types.remove(SplitQueryType.SELECT);
            }
        }
        if (this.externalCodeLifecycleRisk) {
            this.types.add(SplitQueryType.UNSAFE);
        }
        return new LinkedHashSet<>(this.types);
    }

    private void collectNode(ParseTree tree) {
        if (isCreateTableDefinitionSubquery(tree)) {
            if (containsFunctionCall(tree)) {
                this.types.add(SplitQueryType.CALL_PROG_OBJ);
            }
            return;
        }
        SplitQueryType type;
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
        if (tree instanceof AlterTableContext ctx && ctx.partitionDefinitions() != null) {
            this.types.add(SplitQueryType.ALTER_PARTITION);
        }
    }

    private SplitQueryType firstNestedStatement(ParseTree tree, boolean selectOnly) {
        SplitQueryType type;
        boolean previous = this.currentNodeOnly;
        try {
            this.currentNodeOnly = true;
            type = tree.accept(this);
        } finally {
            this.currentNodeOnly = previous;
        }
        if (type == SplitQueryType.SELECT
            || !selectOnly && (type == SplitQueryType.INSERT || type == SplitQueryType.UPDATE || type == SplitQueryType.DELETE || type == SplitQueryType.MERGE)) {
            return type;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            SplitQueryType nestedType = firstNestedStatement(tree.getChild(i), selectOnly);
            if (nestedType != null) {
                return nestedType;
            }
        }
        return null;
    }

    private void collectDirectActions(ParseTree tree) {
        collectLockAction(tree);
        if (tree instanceof ColumnDeclarationContext ctx && hasConstraint(ctx.columnDefinition())) {
            this.types.add(SplitQueryType.ADD_CONSTRAINT);
        } else if (tree instanceof DeclareCursorContext || tree instanceof OpenCursorContext || tree instanceof FetchCursorContext || tree instanceof CloseCursorContext) {
            this.types.add(SplitQueryType.SELECT);
            this.types.add(SplitQueryType.PROGRAM_CONTROL);
        } else if (tree instanceof AlterByImportTablespaceContext || tree instanceof AlterByImportPartitionContext) {
            this.types.add(SplitQueryType.DATA_IMPORT);
        }
        if (tree instanceof WithSelectExprContext ctx && ctx.uid() != null) {
            this.cteNames.add(normalizeIdentifier(ctx.uid().getText()));
        } else if (tree instanceof AtomTableItemContext ctx) {
            collectTableRead(ctx);
        } else if (tree instanceof GenericFunctionCallContext ctx && isUserDefinedFunction(ctx)) {
            this.types.add(SplitQueryType.CALL_PROG_OBJ);
        }
        if (tree instanceof GenericFunctionCallContext ctx && functionAction(ctx) == SplitQueryType.DATA_IMPORT) {
            this.types.add(SplitQueryType.UNSAFE);
        }
        if (tree instanceof SelectStatementContext && containsDataExport(tree)) {
            this.types.add(SplitQueryType.SELECT);
            if (containsProcedureAnalyse(tree)) {
                this.types.add(SplitQueryType.PERFORMANCE);
            }
        } else if (tree instanceof FlushStatementContext ctx) {
            flushTypes(ctx).forEach(this.types::add);
        } else if (tree instanceof ResetOptionsContext ctx) {
            resetTypes(ctx).forEach(this.types::add);
        } else if (tree instanceof CloneStatementContext ctx && ctx.INSTANCE() != null && ctx.cloneDataDirectory() == null) {
            this.types.add(SplitQueryType.UNSAFE);
        } else if (tree instanceof FullDescribeStatementContext ctx && ctx.LOCAL_ID() != null) {
            this.types.add(SplitQueryType.SESSION_VARIABLE_RW);
        } else if (tree instanceof DiagnosticsStatementContext) {
            this.types.add(SplitQueryType.SESSION_VARIABLE_RW);
        } else if (tree instanceof SetTransactionContext ctx) {
            if (ctx.setTransactionStatement().GLOBAL() != null) {
                this.types.add(SplitQueryType.SYSTEM_SETTING_WRITE);
            } else if (ctx.setTransactionStatement().SESSION() != null) {
                this.types.add(SplitQueryType.SESSION_SETTING_WRITE);
            }
        } else if (tree instanceof CreateProcedureContext ctx && ctx.routineOption().stream().anyMatch(option -> option instanceof RoutineCommentContext)) {
            this.types.add(SplitQueryType.COMMENT_PROG_OBJ);
        } else if (tree instanceof CreateFunctionContext ctx && ctx.routineOption().stream().anyMatch(option -> option instanceof RoutineCommentContext)) {
            this.types.add(SplitQueryType.COMMENT_PROG_OBJ);
        } else if (tree instanceof AlterProcedureContext ctx && ctx.alterRoutineOption().stream().anyMatch(option -> option.COMMENT() != null)) {
            this.types.add(SplitQueryType.COMMENT_PROG_OBJ);
        } else if (tree instanceof AlterFunctionContext ctx && ctx.alterRoutineOption().stream().anyMatch(option -> option.COMMENT() != null)) {
            this.types.add(SplitQueryType.COMMENT_PROG_OBJ);
        } else if (tree instanceof CreateEventContext ctx && ctx.COMMENT() != null) {
            this.types.add(SplitQueryType.COMMENT_EVENT);
        } else if (tree instanceof AlterEventContext ctx) {
            if (ctx.RENAME() != null) {
                this.types.add(SplitQueryType.RENAME_EVENT);
            }
            if (ctx.COMMENT() != null) {
                this.types.add(SplitQueryType.COMMENT_EVENT);
            }
        } else if (tree instanceof CreateLibraryContext ctx && ctx.libraryCharacteristic().stream().anyMatch(item -> item.COMMENT() != null)) {
            this.types.add(SplitQueryType.COMMENT_LIBRARY);
        } else if (tree instanceof AlterLibraryContext) {
            this.types.add(SplitQueryType.COMMENT_LIBRARY);
        } else if (tree instanceof CreateUserContext ctx && ctx.accountAttributeOption() != null && ctx.accountAttributeOption().COMMENT() != null) {
            this.types.add(SplitQueryType.COMMENT_USER);
        } else if (tree instanceof AlterUserMysqlV57Context ctx && ctx.accountAttributeOption() != null && ctx.accountAttributeOption().COMMENT() != null) {
            this.types.add(SplitQueryType.COMMENT_USER);
        } else if (tree instanceof AlterTablespaceContext ctx && ctx.RENAME() != null) {
            this.types.add(SplitQueryType.RENAME_TABLESPACE);
        } else if (tree instanceof AlterByChangeColumnContext ctx && !ctx.oldColumn.getText().equals(ctx.columnDefinition().uid().getText())) {
            this.types.add(SplitQueryType.RENAME_COLUMN);
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
            this.types.add(SplitQueryType.QUERY_LOCK);
        } else if (tree instanceof GenericFunctionCallContext ctx && isSessionLockFunction(ctx)) {
            this.types.add(SplitQueryType.SESSION_LOCK);
        }
    }

    private boolean isSessionLockFunction(GenericFunctionCallContext ctx) {
        return functionAction(ctx) == SplitQueryType.SESSION_LOCK;
    }

    private static Set<SplitQueryType> flushTypes(FlushStatementContext ctx) {
        Set<SplitQueryType> result = new LinkedHashSet<>();
        if (ctx.flushTablesOption() != null) {
            FlushTablesOptionContext tablesOption = ctx.flushTablesOption();
            if (tablesOption.flushTableOption() != null && tablesOption.flushTableOption().EXPORT() != null) {
                result.add(SplitQueryType.DATA_EXPORT);
                result.add(SplitQueryType.SESSION_LOCK);
            } else {
                result.add(SplitQueryType.ADMIN_TABLE);
                if (tablesOption.WITH() != null || tablesOption.flushTableOption() != null && tablesOption.flushTableOption().WITH() != null) {
                    result.add(SplitQueryType.SESSION_LOCK);
                }
            }
            return result;
        }

        ctx.flushOption().stream().map(MySplitVisitor::flushOptionType).forEach(result::add);
        if (result.isEmpty()) {
            result.add(SplitQueryType.SYSTEM_SETTING_WRITE);
        }
        return result;
    }

    private static SplitQueryType flushOptionType(FlushOptionContext option) {
        if (option.LOGS() != null) {
            return SplitQueryType.MAINTAIN_LOG;
        }
        if (option.HOSTS() != null || option.OPTIMIZER_COSTS() != null || option.QUERY() != null || option.STATUS() != null || option.USER_RESOURCES() != null) {
            return SplitQueryType.ADMIN_PERFORMANCE;
        }
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    private static Set<SplitQueryType> resetTypes(ResetOptionsContext ctx) {
        Set<SplitQueryType> result = new LinkedHashSet<>();
        for (ResetOptionContext option : ctx.resetOption()) {
            if (option.SLAVE() != null || option.REPLICA() != null) {
                result.add(SplitQueryType.ALTER_REPLICATION);
            } else if (option.MASTER() != null || option.BINARY() != null && option.LOGS() != null) {
                result.add(SplitQueryType.MAINTAIN_LOG);
            } else if (option.QUERY() != null && option.CACHE() != null) {
                result.add(SplitQueryType.ADMIN_PERFORMANCE);
            } else {
                result.add(SplitQueryType.SYSTEM_SETTING_WRITE);
            }
        }
        return result;
    }

    private SplitQueryType selectType(ParseTree tree) {
        if (containsDataExport(tree)) {
            return SplitQueryType.DATA_EXPORT;
        }
        SplitQueryType functionAction = preferredFunctionAction(tree);
        if (isManagementFunctionAction(functionAction)) {
            return SplitQueryType.SELECT;
        }
        if (functionAction == SplitQueryType.PERFORMANCE) {
            return SplitQueryType.SELECT;
        }
        if (functionAction == SplitQueryType.LOG_READ) {
            return SplitQueryType.LOG_READ;
        }
        return SplitQueryType.SELECT;
    }

    private SplitQueryType preferredFunctionAction(ParseTree tree) {
        SplitQueryType preferred = null;
        if (tree instanceof GenericFunctionCallContext ctx) {
            preferred = functionAction(ctx);
            if (isManagementFunctionAction(preferred)) {
                return preferred;
            }
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            SplitQueryType action = preferredFunctionAction(tree.getChild(i));
            if (isManagementFunctionAction(action)) {
                return action;
            }
            if (preferred == null && action != null) {
                preferred = action;
            }
        }
        return preferred;
    }

    private static boolean isManagementFunctionAction(SplitQueryType type) {
        return type != null && switch (type) {
            case SYSTEM_SETTING_WRITE, SESSION_SETTING_WRITE, ALTER_REPLICATION, ALTER_POLICY, DROP_POLICY, ADMIN_REPLICATION, ADMIN_LOG, MAINTAIN_LOG, ADMIN_PERFORMANCE -> true;
            default -> false;
        };
    }

    private SplitQueryType functionAction(GenericFunctionCallContext ctx) {
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

    private static boolean shouldDescend(ParseTree tree, SplitQueryType type) {
        if (type == SplitQueryType.PERFORMANCE) {
            return tree instanceof GenericFunctionCallContext ctx && isBenchmarkFunction(ctx) || tree instanceof QuerySpecificationSelectContext && containsBenchmarkFunction(tree);
        }
        return type == null || switch (type) {
            case CREATE_TABLE, ALTER_TABLE, CREATE_TABLESPACE, ADD_COLUMN, ALTER_COLUMN, ADD_INDEX, INSERT, UPDATE, DELETE, MERGE, ADMIN, BLOCK, DATA_IMPORT, DATA_EXPORT,
                    SESSION_VARIABLE_RW, SESSION_SETTING_WRITE, SYSTEM_SETTING_WRITE, ALTER_REPLICATION, ALTER_POLICY, DROP_POLICY, ADMIN_LOG, LOG_READ, ADMIN_REPLICATION,
                    MAINTAIN_LOG, ADMIN_PERFORMANCE, SELECT, CALL_PROG_OBJ ->
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
    public SplitQueryType visitCreateDatabase(CreateDatabaseContext ctx) {
        return SplitQueryType.CREATE_SCHEMA;
    }

    @Override
    public SplitQueryType visitCheckTable(CheckTableContext ctx) {
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitRepairTable(RepairTableContext ctx) {
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitCloneStatement(CloneStatementContext ctx) {
        return ctx.LOCAL() != null ? SplitQueryType.DATA_EXPORT : SplitQueryType.DATA_IMPORT;
    }

    @Override
    public SplitQueryType visitRestartStatement(RestartStatementContext ctx) {
        return SplitQueryType.UNSAFE;
    }

    @Override
    public SplitQueryType visitShutdownStatement(ShutdownStatementContext ctx) {
        return SplitQueryType.UNSAFE;
    }

    @Override
    public SplitQueryType visitBinlogStatement(BinlogStatementContext ctx) {
        return SplitQueryType.ADMIN_REPLICATION;
    }

    @Override
    public SplitQueryType visitCacheIndexStatement(CacheIndexStatementContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitCreateUdfFunction(CreateUdfFunctionContext ctx) {
        return SplitQueryType.CREATE_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitUninstallPlugin(UninstallPluginContext ctx) {
        return SplitQueryType.DROP_LIBRARY;
    }

    @Override
    public SplitQueryType visitInstallPlugin(InstallPluginContext ctx) {
        return SplitQueryType.CREATE_LIBRARY;
    }

    @Override
    public SplitQueryType visitInstallComponent(InstallComponentContext ctx) {
        return SplitQueryType.CREATE_LIBRARY;
    }

    @Override
    public SplitQueryType visitUninstallComponent(UninstallComponentContext ctx) {
        return SplitQueryType.DROP_LIBRARY;
    }

    @Override
    public SplitQueryType visitSetPassword(SetPasswordContext ctx) {
        return SplitQueryType.ALTER_USER;
    }

    @Override
    public SplitQueryType visitChecksumTable(ChecksumTableContext ctx) {
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitOptimizeTable(OptimizeTableContext ctx) {
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitCreateTablespaceInnodb(CreateTablespaceInnodbContext ctx) {
        return SplitQueryType.CREATE_TABLESPACE;
    }

    @Override
    public SplitQueryType visitCreateUndoTablespace(CreateUndoTablespaceContext ctx) {
        return SplitQueryType.CREATE_TABLESPACE;
    }

    @Override
    public SplitQueryType visitCreateLogfileGroup(CreateLogfileGroupContext ctx) {
        return SplitQueryType.CREATE_LOG;
    }

    @Override
    public SplitQueryType visitAlterUserMysqlV56(AlterUserMysqlV56Context ctx) {
        return SplitQueryType.ALTER_USER;
    }

    @Override
    public SplitQueryType visitAlterUserMysqlV57(AlterUserMysqlV57Context ctx) {
        return SplitQueryType.ALTER_USER;
    }

    @Override
    public SplitQueryType visitAlterUserCurrentUser(AlterUserCurrentUserContext ctx) {
        return SplitQueryType.ALTER_USER;
    }

    @Override
    public SplitQueryType visitAlterUserCurrentUserDiscard(AlterUserCurrentUserDiscardContext ctx) {
        return SplitQueryType.ALTER_USER;
    }

    @Override
    public SplitQueryType visitAlterUserDefaultRole(AlterUserDefaultRoleContext ctx) {
        return SplitQueryType.ALTER_USER;
    }

    @Override
    public SplitQueryType visitAlterUserDiscardOldPassword(AlterUserDiscardOldPasswordContext ctx) {
        return SplitQueryType.ALTER_USER;
    }

    @Override
    public SplitQueryType visitAlterUserMfa(AlterUserMfaContext ctx) {
        return SplitQueryType.ALTER_USER;
    }

    @Override
    public SplitQueryType visitDropTablespace(DropTablespaceContext ctx) {
        return SplitQueryType.DROP_TABLESPACE;
    }

    @Override
    public SplitQueryType visitDropUndoTablespace(DropUndoTablespaceContext ctx) {
        return SplitQueryType.DROP_TABLESPACE;
    }

    @Override
    public SplitQueryType visitDropLogfileGroup(DropLogfileGroupContext ctx) {
        return SplitQueryType.DROP_LOG;
    }

    @Override
    public SplitQueryType visitAlterTablespace(AlterTablespaceContext ctx) {
        return SplitQueryType.ALTER_TABLESPACE;
    }

    @Override
    public SplitQueryType visitAlterUndoTablespace(AlterUndoTablespaceContext ctx) {
        return SplitQueryType.ALTER_TABLESPACE;
    }

    @Override
    public SplitQueryType visitAlterLogfileGroup(AlterLogfileGroupContext ctx) {
        return SplitQueryType.ALTER_LOG;
    }

    @Override
    public SplitQueryType visitAlterInstance(AlterInstanceContext ctx) {
        AlterInstanceActionContext action = ctx.alterInstanceAction();
        if (action.REDO_LOG() != null || action.BINLOG() != null) {
            return SplitQueryType.ADMIN_LOG;
        }
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitCreateTablespaceNdb(CreateTablespaceNdbContext ctx) {
        return SplitQueryType.CREATE_TABLESPACE;
    }

    @Override
    public SplitQueryType visitCreateResourceGroup(CreateResourceGroupContext ctx) {
        return SplitQueryType.CREATE_RESOURCE_GROUP;
    }

    @Override
    public SplitQueryType visitCreateServer(CreateServerContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitCreateSpatialReferenceSystem(CreateSpatialReferenceSystemContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitCreateLibrary(CreateLibraryContext ctx) {
        return SplitQueryType.CREATE_LIBRARY;
    }

    @Override
    public SplitQueryType visitCreateMaskingPolicy(CreateMaskingPolicyContext ctx) {
        return SplitQueryType.CREATE_POLICY;
    }

    @Override
    public SplitQueryType visitAlterResourceGroup(AlterResourceGroupContext ctx) {
        return SplitQueryType.ALTER_RESOURCE_GROUP;
    }

    @Override
    public SplitQueryType visitAlterLibrary(AlterLibraryContext ctx) {
        return SplitQueryType.ALTER_LIBRARY;
    }

    @Override
    public SplitQueryType visitAlterServer(AlterServerContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitDropResourceGroup(DropResourceGroupContext ctx) {
        return SplitQueryType.DROP_RESOURCE_GROUP;
    }

    @Override
    public SplitQueryType visitDropServer(DropServerContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitDropSpatialReferenceSystem(DropSpatialReferenceSystemContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitDropLibrary(DropLibraryContext ctx) {
        return SplitQueryType.DROP_LIBRARY;
    }

    @Override
    public SplitQueryType visitDropMaskingPolicy(DropMaskingPolicyContext ctx) {
        return SplitQueryType.DROP_POLICY;
    }

    @Override
    public SplitQueryType visitSetResourceGroup(SetResourceGroupContext ctx) {
        return SplitQueryType.ADMIN_RESOURCE_GROUP;
    }

    @Override
    public SplitQueryType visitSignalStatement(SignalStatementContext ctx) {
        return SplitQueryType.PROGRAM_CONTROL;
    }

    @Override
    public SplitQueryType visitResignalStatement(ResignalStatementContext ctx) {
        return SplitQueryType.PROGRAM_CONTROL;
    }

    @Override
    public SplitQueryType visitDiagnosticsStatement(DiagnosticsStatementContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitAnalyzeTable(AnalyzeTableContext ctx) {
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitWithSelectStatement(WithSelectStatementContext ctx) {
        return selectType(ctx);
    }

    @Override
    public SplitQueryType visitTableStatement(TableStatementContext ctx) {
        return SplitQueryType.SELECT;
    }

    @Override
    public SplitQueryType visitValuesStatement(ValuesStatementContext ctx) {
        return SplitQueryType.SELECT;
    }

    @Override
    public SplitQueryType visitLoadDataStatement(LoadDataStatementContext ctx) {
        return SplitQueryType.DATA_IMPORT;
    }

    @Override
    public SplitQueryType visitLoadXmlStatement(LoadXmlStatementContext ctx) {
        return SplitQueryType.DATA_IMPORT;
    }

    @Override
    public SplitQueryType visitImportTableStatement(ImportTableStatementContext ctx) {
        return SplitQueryType.DATA_IMPORT;
    }

    @Override
    public SplitQueryType visitDoStatement(DoStatementContext ctx) {
        return SplitQueryType.BLOCK;
    }

    @Override
    public SplitQueryType visitHandlerStatement(HandlerStatementContext ctx) {
        return SplitQueryType.SELECT;
    }

    @Override
    public SplitQueryType visitPrepareStatement(PrepareStatementContext ctx) {
        return SplitQueryType.UNSAFE;
    }

    @Override
    public SplitQueryType visitExecuteStatement(ExecuteStatementContext ctx) {
        return SplitQueryType.UNSAFE;
    }

    @Override
    public SplitQueryType visitDeallocatePrepare(DeallocatePrepareContext ctx) {
        return SplitQueryType.UNSAFE;
    }

    @Override
    public SplitQueryType visitSetTransaction(SetTransactionContext ctx) {
        return SplitQueryType.TRANSACTION;
    }

    @Override
    public SplitQueryType visitSetAutocommit(SetAutocommitContext ctx) {
        return SplitQueryType.SESSION_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitTransactionStatement(TransactionStatementContext ctx) {
        if (ctx.lockInstance() != null || ctx.unlockInstance() != null) {
            return SplitQueryType.SESSION_LOCK;
        }
        if (ctx.lockTables() != null || ctx.unlockTables() != null) {
            return SplitQueryType.SESSION_LOCK;
        }
        return SplitQueryType.TRANSACTION;
    }

    @Override
    public SplitQueryType visitLockInstance(LockInstanceContext ctx) {
        return SplitQueryType.SESSION_LOCK;
    }

    @Override
    public SplitQueryType visitUnlockInstance(UnlockInstanceContext ctx) {
        return SplitQueryType.SESSION_LOCK;
    }

    @Override
    public SplitQueryType visitDropProcedure(DropProcedureContext ctx) {
        return SplitQueryType.DROP_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitDropTrigger(DropTriggerContext ctx) {
        return SplitQueryType.DROP_TRIGGER;
    }

    @Override
    public SplitQueryType visitDropFunction(DropFunctionContext ctx) {
        return SplitQueryType.DROP_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitDropRole(DropRoleContext ctx) {
        return SplitQueryType.DROP_ROLE;
    }

    @Override
    public SplitQueryType visitDropIndex(DropIndexContext ctx) {
        return SplitQueryType.DROP_INDEX;
    }

    @Override
    public SplitQueryType visitDropDatabase(DropDatabaseContext ctx) {
        return SplitQueryType.DROP_SCHEMA;
    }

    @Override
    public SplitQueryType visitAlterSimpleDatabase(AlterSimpleDatabaseContext ctx) {
        return SplitQueryType.ALTER_SCHEMA;
    }

    @Override
    public SplitQueryType visitAlterUpgradeName(AlterUpgradeNameContext ctx) {
        return SplitQueryType.ALTER_SCHEMA;
    }

    @Override
    public SplitQueryType visitTruncateTable(TruncateTableContext ctx) {
        return SplitQueryType.TRUNCATE_TABLE;
    }

    @Override
    public SplitQueryType visitCopyCreateTable(CopyCreateTableContext ctx) {
        return SplitQueryType.CREATE_TABLE;
    }

    @Override
    public SplitQueryType visitQueryCreateTable(QueryCreateTableContext ctx) {
        return SplitQueryType.CREATE_TABLE;
    }

    @Override
    public SplitQueryType visitColumnCreateTable(ColumnCreateTableContext ctx) {
        return SplitQueryType.CREATE_TABLE;
    }

    @Override
    public SplitQueryType visitColumnDeclaration(ColumnDeclarationContext ctx) {
        return SplitQueryType.ADD_COLUMN;
    }

    @Override
    public SplitQueryType visitConstraintDeclaration(ConstraintDeclarationContext ctx) {
        return SplitQueryType.ADD_CONSTRAINT;
    }

    @Override
    public SplitQueryType visitIndexDeclaration(IndexDeclarationContext ctx) {
        return SplitQueryType.ADD_INDEX;
    }

    @Override
    public SplitQueryType visitDropTable(DropTableContext ctx) {
        return SplitQueryType.DROP_TABLE;
    }

    @Override
    public SplitQueryType visitAlterTable(AlterTableContext ctx) {
        return SplitQueryType.ALTER_TABLE;
    }

    @Override
    public SplitQueryType visitRenameTable(RenameTableContext ctx) {
        return SplitQueryType.RENAME_TABLE;
    }

    @Override
    public SplitQueryType visitCreateTrigger(CreateTriggerContext ctx) {
        return SplitQueryType.CREATE_TRIGGER;
    }

    @Override
    public SplitQueryType visitCreateView(CreateViewContext ctx) {
        return ctx.REPLACE() == null ? SplitQueryType.CREATE_VIEW : SplitQueryType.ALTER_VIEW;
    }

    @Override
    public SplitQueryType visitAlterView(AlterViewContext ctx) {
        return SplitQueryType.ALTER_VIEW;
    }

    @Override
    public SplitQueryType visitDropView(DropViewContext ctx) {
        return SplitQueryType.DROP_VIEW;
    }

    @Override
    public SplitQueryType visitFullDescribeStatement(FullDescribeStatementContext ctx) {
        if (ctx.analyze != null) {
            return firstNestedStatement(ctx.describeObjectClause(), false);
        }
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitCreateEvent(CreateEventContext ctx) {
        return SplitQueryType.CREATE_EVENT;
    }

    @Override
    public SplitQueryType visitDropEvent(DropEventContext ctx) {
        return SplitQueryType.DROP_EVENT;
    }

    @Override
    public SplitQueryType visitCreateIndex(CreateIndexContext ctx) {
        return SplitQueryType.ADD_INDEX;
    }

    @Override
    public SplitQueryType visitAlterFunction(AlterFunctionContext ctx) {
        return SplitQueryType.ALTER_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitAlterProcedure(AlterProcedureContext ctx) {
        return SplitQueryType.ALTER_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitCreateFunction(CreateFunctionContext ctx) {
        return SplitQueryType.CREATE_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitCreateProcedure(CreateProcedureContext ctx) {
        return SplitQueryType.CREATE_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitAlterEvent(AlterEventContext ctx) {
        return SplitQueryType.ALTER_EVENT;
    }

    @Override
    public SplitQueryType visitQuerySpecificationSelect(QuerySpecificationSelectContext ctx) {
        return selectType(ctx);
    }

    @Override
    public SplitQueryType visitQueryExpressionSelect(QueryExpressionSelectContext ctx) {
        return selectType(ctx);
    }

    @Override
    public SplitQueryType visitUnionTableValueSelect(UnionTableValueSelectContext ctx) {
        return selectType(ctx);
    }

    @Override
    public SplitQueryType visitProcedureAnalyseClause(ProcedureAnalyseClauseContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitGenericFunctionCall(GenericFunctionCallContext ctx) {
        return functionAction(ctx);
    }

    @Override
    public SplitQueryType visitMysqlVariable(MysqlVariableContext ctx) {
        if (ctx.LOCAL_ID() != null) {
            return SplitQueryType.SESSION_VARIABLE_RW;
        }
        String variable = ctx.GLOBAL_ID().getText().toUpperCase(Locale.ROOT);
        if (variable.startsWith("@@GLOBAL.") || variable.startsWith("@@PERSIST.") || variable.startsWith("@@PERSIST_ONLY.")) {
            return null;
        }
        return SplitQueryType.SESSION_VARIABLE_RW;
    }

    @Override
    public SplitQueryType visitSelectIntoVariables(SelectIntoVariablesContext ctx) {
        return ctx.assignmentField().stream().anyMatch(field -> field.LOCAL_ID() != null) ? SplitQueryType.SESSION_VARIABLE_RW : null;
    }

    @Override
    public SplitQueryType visitAssignmentField(AssignmentFieldContext ctx) {
        return ctx.LOCAL_ID() == null ? null : SplitQueryType.SESSION_VARIABLE_RW;
    }

    @Override
    public SplitQueryType visitSelectExpressionElement(SelectExpressionElementContext ctx) {
        return ctx.LOCAL_ID() != null && ctx.VAR_ASSIGN() != null ? SplitQueryType.SESSION_VARIABLE_RW : null;
    }

    @Override
    public SplitQueryType visitVariableAssignmentExpression(VariableAssignmentExpressionContext ctx) {
        return SplitQueryType.SESSION_VARIABLE_RW;
    }

    @Override
    public SplitQueryType visitNestedVariableAssignmentExpression(NestedVariableAssignmentExpressionContext ctx) {
        return SplitQueryType.SESSION_VARIABLE_RW;
    }

    @Override
    public SplitQueryType visitUpdateStatement(UpdateStatementContext ctx) {
        return SplitQueryType.UPDATE;
    }

    @Override
    public SplitQueryType visitInsertStatement(InsertStatementContext ctx) {
        return ctx.duplicatedFirst == null ? SplitQueryType.INSERT : SplitQueryType.MERGE;
    }

    @Override
    public SplitQueryType visitReplaceStatement(ReplaceStatementContext ctx) {
        return SplitQueryType.MERGE;
    }

    @Override
    public SplitQueryType visitDeleteStatement(DeleteStatementContext ctx) {
        return SplitQueryType.DELETE;
    }

    @Override
    public SplitQueryType visitCallStatement(CallStatementContext ctx) {
        return SplitQueryType.CALL_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitUseStatement(UseStatementContext ctx) {
        return SplitQueryType.SWITCH_SCHEMA;
    }

    @Override
    public SplitQueryType visitHelpStatement(HelpStatementContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitSimpleDescribeStatement(SimpleDescribeStatementContext ctx) {
        return "EXPLAIN".equalsIgnoreCase(ctx.command.getText()) ? SplitQueryType.PERFORMANCE : SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitCreateUser(CreateUserContext ctx) {
        return SplitQueryType.CREATE_USER;
    }

    @Override
    public SplitQueryType visitDropUser(DropUserContext ctx) {
        return SplitQueryType.DROP_USER;
    }

    @Override
    public SplitQueryType visitRenameUser(RenameUserContext ctx) {
        return SplitQueryType.RENAME_USER;
    }

    @Override
    public SplitQueryType visitGrantProxy(GrantProxyContext ctx) {
        return SplitQueryType.GRANT;
    }

    @Override
    public SplitQueryType visitRevokeProxy(RevokeProxyContext ctx) {
        return SplitQueryType.REVOKE;
    }

    @Override
    public SplitQueryType visitGrantStatement(GrantStatementContext ctx) {
        return SplitQueryType.GRANT;
    }

    @Override
    public SplitQueryType visitRevokeStatement(RevokeStatementContext ctx) {
        return SplitQueryType.REVOKE;
    }

    @Override
    public SplitQueryType visitCreateRole(CreateRoleContext ctx) {
        return SplitQueryType.CREATE_ROLE;
    }

    @Override
    public SplitQueryType visitShowMasterLogs(ShowMasterLogsContext ctx) {
        return SplitQueryType.LOG_READ;
    }

    @Override
    public SplitQueryType visitShowBinaryLogStatus(ShowBinaryLogStatusContext ctx) {
        return SplitQueryType.LOG_READ;
    }

    @Override
    public SplitQueryType visitShowCharset(ShowCharsetContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowBinlogEvents(ShowBinlogEventsContext ctx) {
        return SplitQueryType.LOG_READ;
    }

    @Override
    public SplitQueryType visitShowRelayLogEvents(ShowRelayLogEventsContext ctx) {
        return SplitQueryType.LOG_READ;
    }

    @Override
    public SplitQueryType visitShowObjectFilter(ShowObjectFilterContext ctx) {
        String entity = ctx.showCommonEntity().getText();
        if (entity.equalsIgnoreCase("STATUS") || entity.equalsIgnoreCase("GLOBALSTATUS") || entity.equalsIgnoreCase("SESSIONSTATUS") || entity.equalsIgnoreCase("LOCALSTATUS")) {
            return SplitQueryType.PERFORMANCE;
        }
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowColumns(ShowColumnsContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowTables(ShowTablesContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowCreateDb(ShowCreateDbContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowCreateFullIdObject(ShowCreateFullIdObjectContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowCreateMaskingPolicy(ShowCreateMaskingPolicyContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowCreateUser(ShowCreateUserContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowEngine(ShowEngineContext ctx) {
        if (ctx.engineOption.getType() == LOGS) {
            return SplitQueryType.LOG_READ;
        }
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitShowEngines(ShowEnginesContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowStatus(ShowStatusContext ctx) {
        return SplitQueryType.LOG_READ;
    }

    @Override
    public SplitQueryType visitShowPlugins(ShowPluginsContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowPrivileges(ShowPrivilegesContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowProcessList(ShowProcessListContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitShowProfiles(ShowProfilesContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitShowSlaveHosts(ShowSlaveHostsContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowErrors(ShowErrorsContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitShowCountErrors(ShowCountErrorsContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitShowSchemaFilter(ShowSchemaFilterContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowRoutine(ShowRoutineContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowLibraryStatus(ShowLibraryStatusContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowGrants(ShowGrantsContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowIndexes(ShowIndexesContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowOpenTables(ShowOpenTablesContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitShowProfile(ShowProfileContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitResetMaster(ResetMasterContext ctx) {
        return SplitQueryType.MAINTAIN_LOG;
    }

    @Override
    public SplitQueryType visitResetBinaryLogsAndGtids(ResetBinaryLogsAndGtidsContext ctx) {
        return SplitQueryType.MAINTAIN_LOG;
    }

    @Override
    public SplitQueryType visitResetSlave(ResetSlaveContext ctx) {
        return SplitQueryType.ALTER_REPLICATION;
    }

    @Override
    public SplitQueryType visitResetReplica(ResetReplicaContext ctx) {
        return SplitQueryType.ALTER_REPLICATION;
    }

    @Override
    public SplitQueryType visitResetQueryCache(ResetQueryCacheContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitResetOptions(ResetOptionsContext ctx) {
        return resetTypes(ctx).stream().findFirst().orElse(SplitQueryType.SYSTEM_SETTING_WRITE);
    }

    @Override
    public SplitQueryType visitResetPersist(ResetPersistContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitFlushStatement(FlushStatementContext ctx) {
        return flushTypes(ctx).stream().findFirst().orElse(SplitQueryType.SYSTEM_SETTING_WRITE);
    }

    @Override
    public SplitQueryType visitShowReplicaStatus(ShowReplicaStatusContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowParseTree(ShowParseTreeContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitShowReplicas(ShowReplicasContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitKillStatement(KillStatementContext ctx) {
        return SplitQueryType.ADMIN;
    }

    @Override
    public SplitQueryType visitLoadIndexIntoCache(LoadIndexIntoCacheContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitPurgeBinaryLogs(PurgeBinaryLogsContext ctx) {
        return SplitQueryType.MAINTAIN_LOG;
    }

    @Override
    public SplitQueryType visitChangeMaster(ChangeMasterContext ctx) {
        return SplitQueryType.ALTER_REPLICATION;
    }

    @Override
    public SplitQueryType visitChangeReplicationSource(ChangeReplicationSourceContext ctx) {
        return SplitQueryType.ALTER_REPLICATION;
    }

    @Override
    public SplitQueryType visitChangeReplicationFilter(ChangeReplicationFilterContext ctx) {
        return SplitQueryType.ALTER_REPLICATION;
    }

    @Override
    public SplitQueryType visitStartSlave(StartSlaveContext ctx) {
        return SplitQueryType.ALTER_REPLICATION;
    }

    @Override
    public SplitQueryType visitStartReplica(StartReplicaContext ctx) {
        return SplitQueryType.ALTER_REPLICATION;
    }

    @Override
    public SplitQueryType visitStopSlave(StopSlaveContext ctx) {
        return SplitQueryType.ALTER_REPLICATION;
    }

    @Override
    public SplitQueryType visitStopReplica(StopReplicaContext ctx) {
        return SplitQueryType.ALTER_REPLICATION;
    }

    @Override
    public SplitQueryType visitStartGroupReplication(StartGroupReplicationContext ctx) {
        return SplitQueryType.ALTER_REPLICATION;
    }

    @Override
    public SplitQueryType visitStopGroupReplication(StopGroupReplicationContext ctx) {
        return SplitQueryType.ALTER_REPLICATION;
    }

    @Override
    public SplitQueryType visitXaStartTransaction(XaStartTransactionContext ctx) {
        return SplitQueryType.TRANSACTION;
    }

    @Override
    public SplitQueryType visitXaEndTransaction(XaEndTransactionContext ctx) {
        return SplitQueryType.TRANSACTION;
    }

    @Override
    public SplitQueryType visitXaPrepareStatement(XaPrepareStatementContext ctx) {
        return SplitQueryType.TRANSACTION;
    }

    @Override
    public SplitQueryType visitXaCommitWork(XaCommitWorkContext ctx) {
        return SplitQueryType.TRANSACTION;
    }

    @Override
    public SplitQueryType visitXaRollbackWork(XaRollbackWorkContext ctx) {
        return SplitQueryType.TRANSACTION;
    }

    @Override
    public SplitQueryType visitXaRecoverWork(XaRecoverWorkContext ctx) {
        return SplitQueryType.TRANSACTION;
    }

    @Override
    public SplitQueryType visitShowSlaveStatus(ShowSlaveStatusContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitSetVariable(SetVariableContext ctx) {
        boolean onlyUserVariables = ctx.setVariableAssignment().stream().allMatch(assignment -> assignment.variableClause().LOCAL_ID() != null);
        if (onlyUserVariables) {
            return SplitQueryType.SESSION_VARIABLE_RW;
        }
        boolean replicationSetting = ctx.setVariableAssignment().stream().anyMatch(assignment -> {
            String variable = assignment.variableClause().getText().toUpperCase();
            return variable.contains("GTID_") || variable.contains("SLAVE_") || variable.contains("REPLICA_");
        });
        if (replicationSetting) {
            return SplitQueryType.ALTER_REPLICATION;
        }
        boolean systemSetting = ctx.setVariableAssignment().stream().anyMatch(assignment -> {
            VariableClauseContext variable = assignment.variableClause();
            String text = variable.getText().toUpperCase();
            return text.startsWith("@@GLOBAL.") || text.startsWith("@@PERSIST.") || text.startsWith("@@PERSIST_ONLY.") || variable.GLOBAL() != null
                   || variable.persistScope() != null;
        });
        return systemSetting ? SplitQueryType.SYSTEM_SETTING_WRITE : SplitQueryType.SESSION_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitSetCharset(SetCharsetContext ctx) {
        return SplitQueryType.SESSION_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitSetNames(SetNamesContext ctx) {
        return SplitQueryType.SESSION_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitSetRole(SetRoleContext ctx) {
        return SplitQueryType.SWITCH_ROLE;
    }

    @Override
    public SplitQueryType visitSetDefaultRole(SetDefaultRoleContext ctx) {
        return SplitQueryType.ALTER_USER;
    }

    @Override
    public SplitQueryType visitAlterByAddColumn(AlterByAddColumnContext ctx) {
        return SplitQueryType.ADD_COLUMN;
    }

    @Override
    public SplitQueryType visitAlterByAddColumns(AlterByAddColumnsContext ctx) {
        return SplitQueryType.ADD_COLUMN;
    }

    @Override
    public SplitQueryType visitAlterByAddIndex(AlterByAddIndexContext ctx) {
        return SplitQueryType.ADD_INDEX;
    }

    @Override
    public SplitQueryType visitAlterByAddUniqueKey(AlterByAddUniqueKeyContext ctx) {
        return ctx.CONSTRAINT() == null ? SplitQueryType.ADD_INDEX : SplitQueryType.ADD_CONSTRAINT;
    }

    @Override
    public SplitQueryType visitAlterByAddSpecialIndex(AlterByAddSpecialIndexContext ctx) {
        return SplitQueryType.ADD_INDEX;
    }

    @Override
    public SplitQueryType visitAlterByAddPrimaryKey(AlterByAddPrimaryKeyContext ctx) {
        return SplitQueryType.ADD_CONSTRAINT;
    }

    @Override
    public SplitQueryType visitAlterByAddForeignKey(AlterByAddForeignKeyContext ctx) {
        return SplitQueryType.ADD_CONSTRAINT;
    }

    @Override
    public SplitQueryType visitAlterByAddCheckTableConstraint(AlterByAddCheckTableConstraintContext ctx) {
        return SplitQueryType.ADD_CONSTRAINT;
    }

    @Override
    public SplitQueryType visitAlterBySetMaskingPolicy(AlterBySetMaskingPolicyContext ctx) {
        return SplitQueryType.ALTER_COLUMN;
    }

    @Override
    public SplitQueryType visitAlterByDropMaskingPolicy(AlterByDropMaskingPolicyContext ctx) {
        return SplitQueryType.ALTER_COLUMN;
    }

    @Override
    public SplitQueryType visitAlterByChangeDefault(AlterByChangeDefaultContext ctx) {
        return SplitQueryType.ALTER_COLUMN;
    }

    @Override
    public SplitQueryType visitAlterByChangeColumn(AlterByChangeColumnContext ctx) {
        return SplitQueryType.ALTER_COLUMN;
    }

    @Override
    public SplitQueryType visitAlterByModifyColumn(AlterByModifyColumnContext ctx) {
        return SplitQueryType.ALTER_COLUMN;
    }

    @Override
    public SplitQueryType visitAlterByRenameColumn(AlterByRenameColumnContext ctx) {
        return SplitQueryType.RENAME_COLUMN;
    }

    @Override
    public SplitQueryType visitAlterByDropColumn(AlterByDropColumnContext ctx) {
        return SplitQueryType.DROP_COLUMN;
    }

    @Override
    public SplitQueryType visitAlterByAlterConstraintEnforcement(AlterByAlterConstraintEnforcementContext ctx) {
        return SplitQueryType.ALTER_CONSTRAINT;
    }

    @Override
    public SplitQueryType visitAlterByDropConstraintCheck(AlterByDropConstraintCheckContext ctx) {
        return SplitQueryType.DROP_CONSTRAINT;
    }

    @Override
    public SplitQueryType visitAlterByDropPrimaryKey(AlterByDropPrimaryKeyContext ctx) {
        return SplitQueryType.DROP_CONSTRAINT;
    }

    @Override
    public SplitQueryType visitAlterByDropForeignKey(AlterByDropForeignKeyContext ctx) {
        return SplitQueryType.DROP_CONSTRAINT;
    }

    @Override
    public SplitQueryType visitAlterByDropIndex(AlterByDropIndexContext ctx) {
        return SplitQueryType.DROP_INDEX;
    }

    @Override
    public SplitQueryType visitAlterByRenameIndex(AlterByRenameIndexContext ctx) {
        return SplitQueryType.RENAME_INDEX;
    }

    @Override
    public SplitQueryType visitAlterByAlterIndexVisibility(AlterByAlterIndexVisibilityContext ctx) {
        return SplitQueryType.ALTER_INDEX;
    }

    @Override
    public SplitQueryType visitAlterByRename(AlterByRenameContext ctx) {
        return SplitQueryType.RENAME_TABLE;
    }

    @Override
    public SplitQueryType visitAlterByDiscardTablespace(AlterByDiscardTablespaceContext ctx) {
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitAlterByImportTablespace(AlterByImportTablespaceContext ctx) {
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitAlterByDisableKeys(AlterByDisableKeysContext ctx) {
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitAlterByEnableKeys(AlterByEnableKeysContext ctx) {
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitAlterByOrder(AlterByOrderContext ctx) {
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitAlterByForce(AlterByForceContext ctx) {
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitAlterByAddPartition(AlterByAddPartitionContext ctx) {
        return SplitQueryType.ADD_PARTITION;
    }

    @Override
    public SplitQueryType visitAlterByDropPartition(AlterByDropPartitionContext ctx) {
        return SplitQueryType.DROP_PARTITION;
    }

    @Override
    public SplitQueryType visitAlterByTruncatePartition(AlterByTruncatePartitionContext ctx) {
        return SplitQueryType.TRUNCATE_PARTITION;
    }

    @Override
    public SplitQueryType visitAlterByCoalescePartition(AlterByCoalescePartitionContext ctx) {
        return SplitQueryType.ALTER_PARTITION;
    }

    @Override
    public SplitQueryType visitAlterByReorganizePartition(AlterByReorganizePartitionContext ctx) {
        return SplitQueryType.ALTER_PARTITION;
    }

    @Override
    public SplitQueryType visitAlterByExchangePartition(AlterByExchangePartitionContext ctx) {
        return SplitQueryType.ALTER_PARTITION;
    }

    @Override
    public SplitQueryType visitAlterByRemovePartitioning(AlterByRemovePartitioningContext ctx) {
        return SplitQueryType.ALTER_PARTITION;
    }

    @Override
    public SplitQueryType visitAlterByUpgradePartitioning(AlterByUpgradePartitioningContext ctx) {
        return SplitQueryType.ALTER_PARTITION;
    }

    @Override
    public SplitQueryType visitAlterByAnalyzePartition(AlterByAnalyzePartitionContext ctx) {
        return SplitQueryType.ADMIN_PARTITION;
    }

    @Override
    public SplitQueryType visitAlterByCheckPartition(AlterByCheckPartitionContext ctx) {
        return SplitQueryType.ADMIN_PARTITION;
    }

    @Override
    public SplitQueryType visitAlterByOptimizePartition(AlterByOptimizePartitionContext ctx) {
        return SplitQueryType.ADMIN_PARTITION;
    }

    @Override
    public SplitQueryType visitAlterByDiscardPartition(AlterByDiscardPartitionContext ctx) {
        return SplitQueryType.ADMIN_PARTITION;
    }

    @Override
    public SplitQueryType visitAlterByImportPartition(AlterByImportPartitionContext ctx) {
        return SplitQueryType.ADMIN_PARTITION;
    }

    @Override
    public SplitQueryType visitAlterByRebuildPartition(AlterByRebuildPartitionContext ctx) {
        return SplitQueryType.ADMIN_PARTITION;
    }

    @Override
    public SplitQueryType visitAlterByRepairPartition(AlterByRepairPartitionContext ctx) {
        return SplitQueryType.ADMIN_PARTITION;
    }

    @Override
    public SplitQueryType visitAlterBySecondaryLoad(AlterBySecondaryLoadContext ctx) {
        return SplitQueryType.ADMIN_PARTITION;
    }

    @Override
    public SplitQueryType visitAlterBySecondaryUnload(AlterBySecondaryUnloadContext ctx) {
        return SplitQueryType.ADMIN_PARTITION;
    }

    @Override
    public SplitQueryType visitTableOptionComment(TableOptionCommentContext ctx) {
        return SplitQueryType.COMMENT_TABLE;
    }

    @Override
    public SplitQueryType visitCommentColumnConstraint(CommentColumnConstraintContext ctx) {
        return SplitQueryType.COMMENT_COLUMN;
    }

    @Override
    public SplitQueryType visitCommonIndexOption(CommonIndexOptionContext ctx) {
        return ctx.COMMENT() == null ? null : SplitQueryType.COMMENT_INDEX;
    }

    @Override
    public SplitQueryType visitPartitionOptionComment(PartitionOptionCommentContext ctx) {
        return SplitQueryType.COMMENT_PARTITION;
    }

    @Override
    public SplitQueryType visitTablespaceOption(TablespaceOptionContext ctx) {
        return ctx.COMMENT() == null ? null : SplitQueryType.COMMENT_TABLESPACE;
    }

    @Override
    public SplitQueryType visitLogfileGroupOption(LogfileGroupOptionContext ctx) {
        return null;
    }

    public SplitQueryType visitChildren(RuleNode node) {
        if (this.currentNodeOnly) {
            return null;
        }

        int n = node.getChildCount();

        for (int i = 0; i < n; ++i) {
            ParseTree c = node.getChild(i);
            SplitQueryType result = c.accept(this);
            if (result != null) {
                return result;
            }
        }

        return null;
    }

}
