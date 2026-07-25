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

import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.sql.mysql.parser.antlr.MySqlParserBaseVisitor;
import com.clougence.sql.mysql.analysis.reference.MySqlResourceRegistry;

public class MySplitVisitor extends MySqlParserBaseVisitor<SecQueryType> {

    private final Set<SecQueryType>     types    = new LinkedHashSet<>();
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
    public SecQueryType visit(ParseTree tree) {
        collectTypes(tree);
        return this.types.stream().findFirst().orElse(null);
    }

    public Set<SecQueryType> collectTypes(ParseTree tree) {
        this.types.clear();
        this.cteNames.clear();
        this.externalCodeLifecycleRisk = false;
        this.metadataTableRead = false;
        this.ordinaryTableRead = false;
        collectNode(tree);
        if (this.metadataTableRead) {
            this.types.add(SecQueryType.METADATA);
            if (!this.ordinaryTableRead) {
                this.types.remove(SecQueryType.SELECT);
            }
        }
        if (this.externalCodeLifecycleRisk) {
            this.types.add(SecQueryType.UNSAFE);
        }
        return new LinkedHashSet<>(this.types);
    }

    private void collectNode(ParseTree tree) {
        if (isCreateTableDefinitionSubquery(tree)) {
            if (containsFunctionCall(tree)) {
                this.types.add(SecQueryType.CALL_PROG_OBJ);
            }
            return;
        }
        SecQueryType type;
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
            this.types.add(SecQueryType.ALTER_PARTITION);
        }
    }

    private SecQueryType firstNestedStatement(ParseTree tree, boolean selectOnly) {
        SecQueryType type;
        boolean previous = this.currentNodeOnly;
        try {
            this.currentNodeOnly = true;
            type = tree.accept(this);
        } finally {
            this.currentNodeOnly = previous;
        }
        if (type == SecQueryType.SELECT
            || !selectOnly && (type == SecQueryType.INSERT || type == SecQueryType.UPDATE || type == SecQueryType.DELETE || type == SecQueryType.MERGE)) {
            return type;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            SecQueryType nestedType = firstNestedStatement(tree.getChild(i), selectOnly);
            if (nestedType != null) {
                return nestedType;
            }
        }
        return null;
    }

    private void collectDirectActions(ParseTree tree) {
        collectLockAction(tree);
        if (tree instanceof ColumnDeclarationContext ctx && hasConstraint(ctx.columnDefinition())) {
            this.types.add(SecQueryType.ADD_CONSTRAINT);
        } else if (tree instanceof DeclareCursorContext || tree instanceof OpenCursorContext || tree instanceof FetchCursorContext || tree instanceof CloseCursorContext) {
            this.types.add(SecQueryType.SELECT);
            this.types.add(SecQueryType.PROGRAM_CONTROL);
        } else if (tree instanceof AlterByImportTablespaceContext || tree instanceof AlterByImportPartitionContext) {
            this.types.add(SecQueryType.DATA_IMPORT);
        }
        if (tree instanceof WithSelectExprContext ctx && ctx.uid() != null) {
            this.cteNames.add(normalizeIdentifier(ctx.uid().getText()));
        } else if (tree instanceof AtomTableItemContext ctx) {
            collectTableRead(ctx);
        } else if (tree instanceof GenericFunctionCallContext ctx && isUserDefinedFunction(ctx)) {
            this.types.add(SecQueryType.CALL_PROG_OBJ);
        }
        if (tree instanceof GenericFunctionCallContext ctx && functionAction(ctx) == SecQueryType.DATA_IMPORT) {
            this.types.add(SecQueryType.UNSAFE);
        }
        if (tree instanceof SelectStatementContext && containsDataExport(tree)) {
            this.types.add(SecQueryType.SELECT);
            if (containsProcedureAnalyse(tree)) {
                this.types.add(SecQueryType.PERFORMANCE);
            }
        } else if (tree instanceof FlushStatementContext ctx) {
            flushTypes(ctx).forEach(this.types::add);
        } else if (tree instanceof ResetOptionsContext ctx) {
            resetTypes(ctx).forEach(this.types::add);
        } else if (tree instanceof CloneStatementContext ctx && ctx.INSTANCE() != null && ctx.cloneDataDirectory() == null) {
            this.types.add(SecQueryType.UNSAFE);
        } else if (tree instanceof FullDescribeStatementContext ctx && ctx.LOCAL_ID() != null) {
            this.types.add(SecQueryType.SESSION_VARIABLE_RW);
        } else if (tree instanceof DiagnosticsStatementContext) {
            this.types.add(SecQueryType.SESSION_VARIABLE_RW);
        } else if (tree instanceof SetTransactionContext ctx) {
            if (ctx.setTransactionStatement().GLOBAL() != null) {
                this.types.add(SecQueryType.SYSTEM_SETTING_WRITE);
            } else if (ctx.setTransactionStatement().SESSION() != null) {
                this.types.add(SecQueryType.SESSION_SETTING_WRITE);
            }
        } else if (tree instanceof CreateProcedureContext ctx && ctx.routineOption().stream().anyMatch(option -> option instanceof RoutineCommentContext)) {
            this.types.add(SecQueryType.COMMENT_PROG_OBJ);
        } else if (tree instanceof CreateFunctionContext ctx && ctx.routineOption().stream().anyMatch(option -> option instanceof RoutineCommentContext)) {
            this.types.add(SecQueryType.COMMENT_PROG_OBJ);
        } else if (tree instanceof AlterProcedureContext ctx && ctx.alterRoutineOption().stream().anyMatch(option -> option.COMMENT() != null)) {
            this.types.add(SecQueryType.COMMENT_PROG_OBJ);
        } else if (tree instanceof AlterFunctionContext ctx && ctx.alterRoutineOption().stream().anyMatch(option -> option.COMMENT() != null)) {
            this.types.add(SecQueryType.COMMENT_PROG_OBJ);
        } else if (tree instanceof CreateEventContext ctx && ctx.COMMENT() != null) {
            this.types.add(SecQueryType.COMMENT_EVENT);
        } else if (tree instanceof AlterEventContext ctx) {
            if (ctx.RENAME() != null) {
                this.types.add(SecQueryType.RENAME_EVENT);
            }
            if (ctx.COMMENT() != null) {
                this.types.add(SecQueryType.COMMENT_EVENT);
            }
        } else if (tree instanceof CreateLibraryContext ctx && ctx.libraryCharacteristic().stream().anyMatch(item -> item.COMMENT() != null)) {
            this.types.add(SecQueryType.COMMENT_LIBRARY);
        } else if (tree instanceof AlterLibraryContext) {
            this.types.add(SecQueryType.COMMENT_LIBRARY);
        } else if (tree instanceof CreateUserContext ctx && ctx.accountAttributeOption() != null && ctx.accountAttributeOption().COMMENT() != null) {
            this.types.add(SecQueryType.COMMENT_USER);
        } else if (tree instanceof AlterUserMysqlV57Context ctx && ctx.accountAttributeOption() != null && ctx.accountAttributeOption().COMMENT() != null) {
            this.types.add(SecQueryType.COMMENT_USER);
        } else if (tree instanceof AlterTablespaceContext ctx && ctx.RENAME() != null) {
            this.types.add(SecQueryType.RENAME_TABLESPACE);
        } else if (tree instanceof AlterByChangeColumnContext ctx && !ctx.oldColumn.getText().equals(ctx.columnDefinition().uid().getText())) {
            this.types.add(SecQueryType.RENAME_COLUMN);
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
            this.types.add(SecQueryType.QUERY_LOCK);
        } else if (tree instanceof GenericFunctionCallContext ctx && isSessionLockFunction(ctx)) {
            this.types.add(SecQueryType.SESSION_LOCK);
        }
    }

    private boolean isSessionLockFunction(GenericFunctionCallContext ctx) {
        return functionAction(ctx) == SecQueryType.SESSION_LOCK;
    }

    private static Set<SecQueryType> flushTypes(FlushStatementContext ctx) {
        Set<SecQueryType> result = new LinkedHashSet<>();
        if (ctx.flushTablesOption() != null) {
            FlushTablesOptionContext tablesOption = ctx.flushTablesOption();
            if (tablesOption.flushTableOption() != null && tablesOption.flushTableOption().EXPORT() != null) {
                result.add(SecQueryType.DATA_EXPORT);
                result.add(SecQueryType.SESSION_LOCK);
            } else {
                result.add(SecQueryType.ADMIN_TABLE);
                if (tablesOption.WITH() != null || tablesOption.flushTableOption() != null && tablesOption.flushTableOption().WITH() != null) {
                    result.add(SecQueryType.SESSION_LOCK);
                }
            }
            return result;
        }

        ctx.flushOption().stream().map(MySplitVisitor::flushOptionType).forEach(result::add);
        if (result.isEmpty()) {
            result.add(SecQueryType.SYSTEM_SETTING_WRITE);
        }
        return result;
    }

    private static SecQueryType flushOptionType(FlushOptionContext option) {
        if (option.LOGS() != null) {
            return SecQueryType.MAINTAIN_LOG;
        }
        if (option.HOSTS() != null || option.OPTIMIZER_COSTS() != null || option.QUERY() != null || option.STATUS() != null || option.USER_RESOURCES() != null) {
            return SecQueryType.ADMIN_PERFORMANCE;
        }
        return SecQueryType.SYSTEM_SETTING_WRITE;
    }

    private static Set<SecQueryType> resetTypes(ResetOptionsContext ctx) {
        Set<SecQueryType> result = new LinkedHashSet<>();
        for (ResetOptionContext option : ctx.resetOption()) {
            if (option.SLAVE() != null || option.REPLICA() != null) {
                result.add(SecQueryType.ALTER_REPLICATION);
            } else if (option.MASTER() != null || option.BINARY() != null && option.LOGS() != null) {
                result.add(SecQueryType.MAINTAIN_LOG);
            } else if (option.QUERY() != null && option.CACHE() != null) {
                result.add(SecQueryType.ADMIN_PERFORMANCE);
            } else {
                result.add(SecQueryType.SYSTEM_SETTING_WRITE);
            }
        }
        return result;
    }

    private SecQueryType selectType(ParseTree tree) {
        if (containsDataExport(tree)) {
            return SecQueryType.DATA_EXPORT;
        }
        SecQueryType functionAction = preferredFunctionAction(tree);
        if (isManagementFunctionAction(functionAction)) {
            return SecQueryType.SELECT;
        }
        if (functionAction == SecQueryType.PERFORMANCE) {
            return SecQueryType.SELECT;
        }
        if (functionAction == SecQueryType.LOG_READ) {
            return SecQueryType.LOG_READ;
        }
        return SecQueryType.SELECT;
    }

    private SecQueryType preferredFunctionAction(ParseTree tree) {
        SecQueryType preferred = null;
        if (tree instanceof GenericFunctionCallContext ctx) {
            preferred = functionAction(ctx);
            if (isManagementFunctionAction(preferred)) {
                return preferred;
            }
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            SecQueryType action = preferredFunctionAction(tree.getChild(i));
            if (isManagementFunctionAction(action)) {
                return action;
            }
            if (preferred == null && action != null) {
                preferred = action;
            }
        }
        return preferred;
    }

    private static boolean isManagementFunctionAction(SecQueryType type) {
        return type != null && switch (type) {
            case SYSTEM_SETTING_WRITE, SESSION_SETTING_WRITE, ALTER_REPLICATION, ALTER_POLICY, DROP_POLICY, ADMIN_REPLICATION, ADMIN_LOG, MAINTAIN_LOG, ADMIN_PERFORMANCE -> true;
            default -> false;
        };
    }

    private SecQueryType functionAction(GenericFunctionCallContext ctx) {
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

    private static boolean shouldDescend(ParseTree tree, SecQueryType type) {
        if (type == SecQueryType.PERFORMANCE) {
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
    public SecQueryType visitCreateDatabase(CreateDatabaseContext ctx) {
        return SecQueryType.CREATE_SCHEMA;
    }

    @Override
    public SecQueryType visitCheckTable(CheckTableContext ctx) {
        return SecQueryType.ADMIN_TABLE;
    }

    @Override
    public SecQueryType visitRepairTable(RepairTableContext ctx) {
        return SecQueryType.ADMIN_TABLE;
    }

    @Override
    public SecQueryType visitCloneStatement(CloneStatementContext ctx) {
        return ctx.LOCAL() != null ? SecQueryType.DATA_EXPORT : SecQueryType.DATA_IMPORT;
    }

    @Override
    public SecQueryType visitRestartStatement(RestartStatementContext ctx) {
        return SecQueryType.UNSAFE;
    }

    @Override
    public SecQueryType visitShutdownStatement(ShutdownStatementContext ctx) {
        return SecQueryType.UNSAFE;
    }

    @Override
    public SecQueryType visitBinlogStatement(BinlogStatementContext ctx) {
        return SecQueryType.ADMIN_REPLICATION;
    }

    @Override
    public SecQueryType visitCacheIndexStatement(CacheIndexStatementContext ctx) {
        return SecQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SecQueryType visitCreateUdfFunction(CreateUdfFunctionContext ctx) {
        return SecQueryType.CREATE_PROG_OBJ;
    }

    @Override
    public SecQueryType visitUninstallPlugin(UninstallPluginContext ctx) {
        return SecQueryType.DROP_LIBRARY;
    }

    @Override
    public SecQueryType visitInstallPlugin(InstallPluginContext ctx) {
        return SecQueryType.CREATE_LIBRARY;
    }

    @Override
    public SecQueryType visitInstallComponent(InstallComponentContext ctx) {
        return SecQueryType.CREATE_LIBRARY;
    }

    @Override
    public SecQueryType visitUninstallComponent(UninstallComponentContext ctx) {
        return SecQueryType.DROP_LIBRARY;
    }

    @Override
    public SecQueryType visitSetPassword(SetPasswordContext ctx) {
        return SecQueryType.ALTER_USER;
    }

    @Override
    public SecQueryType visitChecksumTable(ChecksumTableContext ctx) {
        return SecQueryType.ADMIN_TABLE;
    }

    @Override
    public SecQueryType visitOptimizeTable(OptimizeTableContext ctx) {
        return SecQueryType.ADMIN_TABLE;
    }

    @Override
    public SecQueryType visitCreateTablespaceInnodb(CreateTablespaceInnodbContext ctx) {
        return SecQueryType.CREATE_TABLESPACE;
    }

    @Override
    public SecQueryType visitCreateUndoTablespace(CreateUndoTablespaceContext ctx) {
        return SecQueryType.CREATE_TABLESPACE;
    }

    @Override
    public SecQueryType visitCreateLogfileGroup(CreateLogfileGroupContext ctx) {
        return SecQueryType.CREATE_LOG;
    }

    @Override
    public SecQueryType visitAlterUserMysqlV56(AlterUserMysqlV56Context ctx) {
        return SecQueryType.ALTER_USER;
    }

    @Override
    public SecQueryType visitAlterUserMysqlV57(AlterUserMysqlV57Context ctx) {
        return SecQueryType.ALTER_USER;
    }

    @Override
    public SecQueryType visitAlterUserCurrentUser(AlterUserCurrentUserContext ctx) {
        return SecQueryType.ALTER_USER;
    }

    @Override
    public SecQueryType visitAlterUserCurrentUserDiscard(AlterUserCurrentUserDiscardContext ctx) {
        return SecQueryType.ALTER_USER;
    }

    @Override
    public SecQueryType visitAlterUserDefaultRole(AlterUserDefaultRoleContext ctx) {
        return SecQueryType.ALTER_USER;
    }

    @Override
    public SecQueryType visitAlterUserDiscardOldPassword(AlterUserDiscardOldPasswordContext ctx) {
        return SecQueryType.ALTER_USER;
    }

    @Override
    public SecQueryType visitAlterUserMfa(AlterUserMfaContext ctx) {
        return SecQueryType.ALTER_USER;
    }

    @Override
    public SecQueryType visitDropTablespace(DropTablespaceContext ctx) {
        return SecQueryType.DROP_TABLESPACE;
    }

    @Override
    public SecQueryType visitDropUndoTablespace(DropUndoTablespaceContext ctx) {
        return SecQueryType.DROP_TABLESPACE;
    }

    @Override
    public SecQueryType visitDropLogfileGroup(DropLogfileGroupContext ctx) {
        return SecQueryType.DROP_LOG;
    }

    @Override
    public SecQueryType visitAlterTablespace(AlterTablespaceContext ctx) {
        return SecQueryType.ALTER_TABLESPACE;
    }

    @Override
    public SecQueryType visitAlterUndoTablespace(AlterUndoTablespaceContext ctx) {
        return SecQueryType.ALTER_TABLESPACE;
    }

    @Override
    public SecQueryType visitAlterLogfileGroup(AlterLogfileGroupContext ctx) {
        return SecQueryType.ALTER_LOG;
    }

    @Override
    public SecQueryType visitAlterInstance(AlterInstanceContext ctx) {
        AlterInstanceActionContext action = ctx.alterInstanceAction();
        if (action.REDO_LOG() != null || action.BINLOG() != null) {
            return SecQueryType.ADMIN_LOG;
        }
        return SecQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SecQueryType visitCreateTablespaceNdb(CreateTablespaceNdbContext ctx) {
        return SecQueryType.CREATE_TABLESPACE;
    }

    @Override
    public SecQueryType visitCreateResourceGroup(CreateResourceGroupContext ctx) {
        return SecQueryType.CREATE_RESOURCE_GROUP;
    }

    @Override
    public SecQueryType visitCreateServer(CreateServerContext ctx) {
        return SecQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SecQueryType visitCreateSpatialReferenceSystem(CreateSpatialReferenceSystemContext ctx) {
        return SecQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SecQueryType visitCreateLibrary(CreateLibraryContext ctx) {
        return SecQueryType.CREATE_LIBRARY;
    }

    @Override
    public SecQueryType visitCreateMaskingPolicy(CreateMaskingPolicyContext ctx) {
        return SecQueryType.CREATE_POLICY;
    }

    @Override
    public SecQueryType visitAlterResourceGroup(AlterResourceGroupContext ctx) {
        return SecQueryType.ALTER_RESOURCE_GROUP;
    }

    @Override
    public SecQueryType visitAlterLibrary(AlterLibraryContext ctx) {
        return SecQueryType.ALTER_LIBRARY;
    }

    @Override
    public SecQueryType visitAlterServer(AlterServerContext ctx) {
        return SecQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SecQueryType visitDropResourceGroup(DropResourceGroupContext ctx) {
        return SecQueryType.DROP_RESOURCE_GROUP;
    }

    @Override
    public SecQueryType visitDropServer(DropServerContext ctx) {
        return SecQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SecQueryType visitDropSpatialReferenceSystem(DropSpatialReferenceSystemContext ctx) {
        return SecQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SecQueryType visitDropLibrary(DropLibraryContext ctx) {
        return SecQueryType.DROP_LIBRARY;
    }

    @Override
    public SecQueryType visitDropMaskingPolicy(DropMaskingPolicyContext ctx) {
        return SecQueryType.DROP_POLICY;
    }

    @Override
    public SecQueryType visitSetResourceGroup(SetResourceGroupContext ctx) {
        return SecQueryType.ADMIN_RESOURCE_GROUP;
    }

    @Override
    public SecQueryType visitSignalStatement(SignalStatementContext ctx) {
        return SecQueryType.PROGRAM_CONTROL;
    }

    @Override
    public SecQueryType visitResignalStatement(ResignalStatementContext ctx) {
        return SecQueryType.PROGRAM_CONTROL;
    }

    @Override
    public SecQueryType visitDiagnosticsStatement(DiagnosticsStatementContext ctx) {
        return SecQueryType.PERFORMANCE;
    }

    @Override
    public SecQueryType visitAnalyzeTable(AnalyzeTableContext ctx) {
        return SecQueryType.ADMIN_TABLE;
    }

    @Override
    public SecQueryType visitWithSelectStatement(WithSelectStatementContext ctx) {
        return selectType(ctx);
    }

    @Override
    public SecQueryType visitTableStatement(TableStatementContext ctx) {
        return SecQueryType.SELECT;
    }

    @Override
    public SecQueryType visitValuesStatement(ValuesStatementContext ctx) {
        return SecQueryType.SELECT;
    }

    @Override
    public SecQueryType visitLoadDataStatement(LoadDataStatementContext ctx) {
        return SecQueryType.DATA_IMPORT;
    }

    @Override
    public SecQueryType visitLoadXmlStatement(LoadXmlStatementContext ctx) {
        return SecQueryType.DATA_IMPORT;
    }

    @Override
    public SecQueryType visitImportTableStatement(ImportTableStatementContext ctx) {
        return SecQueryType.DATA_IMPORT;
    }

    @Override
    public SecQueryType visitDoStatement(DoStatementContext ctx) {
        return SecQueryType.BLOCK;
    }

    @Override
    public SecQueryType visitHandlerStatement(HandlerStatementContext ctx) {
        return SecQueryType.SELECT;
    }

    @Override
    public SecQueryType visitPrepareStatement(PrepareStatementContext ctx) {
        return SecQueryType.UNSAFE;
    }

    @Override
    public SecQueryType visitExecuteStatement(ExecuteStatementContext ctx) {
        return SecQueryType.UNSAFE;
    }

    @Override
    public SecQueryType visitDeallocatePrepare(DeallocatePrepareContext ctx) {
        return SecQueryType.UNSAFE;
    }

    @Override
    public SecQueryType visitSetTransaction(SetTransactionContext ctx) {
        return SecQueryType.TRANSACTION;
    }

    @Override
    public SecQueryType visitSetAutocommit(SetAutocommitContext ctx) {
        return SecQueryType.SESSION_SETTING_WRITE;
    }

    @Override
    public SecQueryType visitTransactionStatement(TransactionStatementContext ctx) {
        if (ctx.lockInstance() != null || ctx.unlockInstance() != null) {
            return SecQueryType.SESSION_LOCK;
        }
        if (ctx.lockTables() != null || ctx.unlockTables() != null) {
            return SecQueryType.SESSION_LOCK;
        }
        return SecQueryType.TRANSACTION;
    }

    @Override
    public SecQueryType visitLockInstance(LockInstanceContext ctx) {
        return SecQueryType.SESSION_LOCK;
    }

    @Override
    public SecQueryType visitUnlockInstance(UnlockInstanceContext ctx) {
        return SecQueryType.SESSION_LOCK;
    }

    @Override
    public SecQueryType visitDropProcedure(DropProcedureContext ctx) {
        return SecQueryType.DROP_PROG_OBJ;
    }

    @Override
    public SecQueryType visitDropTrigger(DropTriggerContext ctx) {
        return SecQueryType.DROP_TRIGGER;
    }

    @Override
    public SecQueryType visitDropFunction(DropFunctionContext ctx) {
        return SecQueryType.DROP_PROG_OBJ;
    }

    @Override
    public SecQueryType visitDropRole(DropRoleContext ctx) {
        return SecQueryType.DROP_ROLE;
    }

    @Override
    public SecQueryType visitDropIndex(DropIndexContext ctx) {
        return SecQueryType.DROP_INDEX;
    }

    @Override
    public SecQueryType visitDropDatabase(DropDatabaseContext ctx) {
        return SecQueryType.DROP_SCHEMA;
    }

    @Override
    public SecQueryType visitAlterSimpleDatabase(AlterSimpleDatabaseContext ctx) {
        return SecQueryType.ALTER_SCHEMA;
    }

    @Override
    public SecQueryType visitAlterUpgradeName(AlterUpgradeNameContext ctx) {
        return SecQueryType.ALTER_SCHEMA;
    }

    @Override
    public SecQueryType visitTruncateTable(TruncateTableContext ctx) {
        return SecQueryType.TRUNCATE_TABLE;
    }

    @Override
    public SecQueryType visitCopyCreateTable(CopyCreateTableContext ctx) {
        return SecQueryType.CREATE_TABLE;
    }

    @Override
    public SecQueryType visitQueryCreateTable(QueryCreateTableContext ctx) {
        return SecQueryType.CREATE_TABLE;
    }

    @Override
    public SecQueryType visitColumnCreateTable(ColumnCreateTableContext ctx) {
        return SecQueryType.CREATE_TABLE;
    }

    @Override
    public SecQueryType visitColumnDeclaration(ColumnDeclarationContext ctx) {
        return SecQueryType.ADD_COLUMN;
    }

    @Override
    public SecQueryType visitConstraintDeclaration(ConstraintDeclarationContext ctx) {
        return SecQueryType.ADD_CONSTRAINT;
    }

    @Override
    public SecQueryType visitIndexDeclaration(IndexDeclarationContext ctx) {
        return SecQueryType.ADD_INDEX;
    }

    @Override
    public SecQueryType visitDropTable(DropTableContext ctx) {
        return SecQueryType.DROP_TABLE;
    }

    @Override
    public SecQueryType visitAlterTable(AlterTableContext ctx) {
        return SecQueryType.ALTER_TABLE;
    }

    @Override
    public SecQueryType visitRenameTable(RenameTableContext ctx) {
        return SecQueryType.RENAME_TABLE;
    }

    @Override
    public SecQueryType visitCreateTrigger(CreateTriggerContext ctx) {
        return SecQueryType.CREATE_TRIGGER;
    }

    @Override
    public SecQueryType visitCreateView(CreateViewContext ctx) {
        return ctx.REPLACE() == null ? SecQueryType.CREATE_VIEW : SecQueryType.ALTER_VIEW;
    }

    @Override
    public SecQueryType visitAlterView(AlterViewContext ctx) {
        return SecQueryType.ALTER_VIEW;
    }

    @Override
    public SecQueryType visitDropView(DropViewContext ctx) {
        return SecQueryType.DROP_VIEW;
    }

    @Override
    public SecQueryType visitFullDescribeStatement(FullDescribeStatementContext ctx) {
        if (ctx.analyze != null) {
            return firstNestedStatement(ctx.describeObjectClause(), false);
        }
        return SecQueryType.PERFORMANCE;
    }

    @Override
    public SecQueryType visitCreateEvent(CreateEventContext ctx) {
        return SecQueryType.CREATE_EVENT;
    }

    @Override
    public SecQueryType visitDropEvent(DropEventContext ctx) {
        return SecQueryType.DROP_EVENT;
    }

    @Override
    public SecQueryType visitCreateIndex(CreateIndexContext ctx) {
        return SecQueryType.ADD_INDEX;
    }

    @Override
    public SecQueryType visitAlterFunction(AlterFunctionContext ctx) {
        return SecQueryType.ALTER_PROG_OBJ;
    }

    @Override
    public SecQueryType visitAlterProcedure(AlterProcedureContext ctx) {
        return SecQueryType.ALTER_PROG_OBJ;
    }

    @Override
    public SecQueryType visitCreateFunction(CreateFunctionContext ctx) {
        return SecQueryType.CREATE_PROG_OBJ;
    }

    @Override
    public SecQueryType visitCreateProcedure(CreateProcedureContext ctx) {
        return SecQueryType.CREATE_PROG_OBJ;
    }

    @Override
    public SecQueryType visitAlterEvent(AlterEventContext ctx) {
        return SecQueryType.ALTER_EVENT;
    }

    @Override
    public SecQueryType visitQuerySpecificationSelect(QuerySpecificationSelectContext ctx) {
        return selectType(ctx);
    }

    @Override
    public SecQueryType visitQueryExpressionSelect(QueryExpressionSelectContext ctx) {
        return selectType(ctx);
    }

    @Override
    public SecQueryType visitUnionTableValueSelect(UnionTableValueSelectContext ctx) {
        return selectType(ctx);
    }

    @Override
    public SecQueryType visitProcedureAnalyseClause(ProcedureAnalyseClauseContext ctx) {
        return SecQueryType.PERFORMANCE;
    }

    @Override
    public SecQueryType visitGenericFunctionCall(GenericFunctionCallContext ctx) {
        return functionAction(ctx);
    }

    @Override
    public SecQueryType visitMysqlVariable(MysqlVariableContext ctx) {
        if (ctx.LOCAL_ID() != null) {
            return SecQueryType.SESSION_VARIABLE_RW;
        }
        String variable = ctx.GLOBAL_ID().getText().toUpperCase(Locale.ROOT);
        if (variable.startsWith("@@GLOBAL.") || variable.startsWith("@@PERSIST.") || variable.startsWith("@@PERSIST_ONLY.")) {
            return null;
        }
        return SecQueryType.SESSION_VARIABLE_RW;
    }

    @Override
    public SecQueryType visitSelectIntoVariables(SelectIntoVariablesContext ctx) {
        return ctx.assignmentField().stream().anyMatch(field -> field.LOCAL_ID() != null) ? SecQueryType.SESSION_VARIABLE_RW : null;
    }

    @Override
    public SecQueryType visitAssignmentField(AssignmentFieldContext ctx) {
        return ctx.LOCAL_ID() == null ? null : SecQueryType.SESSION_VARIABLE_RW;
    }

    @Override
    public SecQueryType visitSelectExpressionElement(SelectExpressionElementContext ctx) {
        return ctx.LOCAL_ID() != null && ctx.VAR_ASSIGN() != null ? SecQueryType.SESSION_VARIABLE_RW : null;
    }

    @Override
    public SecQueryType visitVariableAssignmentExpression(VariableAssignmentExpressionContext ctx) {
        return SecQueryType.SESSION_VARIABLE_RW;
    }

    @Override
    public SecQueryType visitNestedVariableAssignmentExpression(NestedVariableAssignmentExpressionContext ctx) {
        return SecQueryType.SESSION_VARIABLE_RW;
    }

    @Override
    public SecQueryType visitUpdateStatement(UpdateStatementContext ctx) {
        return SecQueryType.UPDATE;
    }

    @Override
    public SecQueryType visitInsertStatement(InsertStatementContext ctx) {
        return ctx.duplicatedFirst == null ? SecQueryType.INSERT : SecQueryType.MERGE;
    }

    @Override
    public SecQueryType visitReplaceStatement(ReplaceStatementContext ctx) {
        return SecQueryType.MERGE;
    }

    @Override
    public SecQueryType visitDeleteStatement(DeleteStatementContext ctx) {
        return SecQueryType.DELETE;
    }

    @Override
    public SecQueryType visitCallStatement(CallStatementContext ctx) {
        return SecQueryType.CALL_PROG_OBJ;
    }

    @Override
    public SecQueryType visitUseStatement(UseStatementContext ctx) {
        return SecQueryType.SWITCH_SCHEMA;
    }

    @Override
    public SecQueryType visitHelpStatement(HelpStatementContext ctx) {
        return SecQueryType.METADATA;
    }

    @Override
    public SecQueryType visitSimpleDescribeStatement(SimpleDescribeStatementContext ctx) {
        return "EXPLAIN".equalsIgnoreCase(ctx.command.getText()) ? SecQueryType.PERFORMANCE : SecQueryType.METADATA;
    }

    @Override
    public SecQueryType visitCreateUser(CreateUserContext ctx) {
        return SecQueryType.CREATE_USER;
    }

    @Override
    public SecQueryType visitDropUser(DropUserContext ctx) {
        return SecQueryType.DROP_USER;
    }

    @Override
    public SecQueryType visitRenameUser(RenameUserContext ctx) {
        return SecQueryType.RENAME_USER;
    }

    @Override
    public SecQueryType visitGrantProxy(GrantProxyContext ctx) {
        return SecQueryType.GRANT;
    }

    @Override
    public SecQueryType visitRevokeProxy(RevokeProxyContext ctx) {
        return SecQueryType.REVOKE;
    }

    @Override
    public SecQueryType visitGrantStatement(GrantStatementContext ctx) {
        return SecQueryType.GRANT;
    }

    @Override
    public SecQueryType visitRevokeStatement(RevokeStatementContext ctx) {
        return SecQueryType.REVOKE;
    }

    @Override
    public SecQueryType visitCreateRole(CreateRoleContext ctx) {
        return SecQueryType.CREATE_ROLE;
    }

    @Override
    public SecQueryType visitShowMasterLogs(ShowMasterLogsContext ctx) {
        return SecQueryType.LOG_READ;
    }

    @Override
    public SecQueryType visitShowBinaryLogStatus(ShowBinaryLogStatusContext ctx) {
        return SecQueryType.LOG_READ;
    }

    @Override
    public SecQueryType visitShowCharset(ShowCharsetContext ctx) {
        return SecQueryType.METADATA;
    }

    @Override
    public SecQueryType visitShowBinlogEvents(ShowBinlogEventsContext ctx) {
        return SecQueryType.LOG_READ;
    }

    @Override
    public SecQueryType visitShowRelayLogEvents(ShowRelayLogEventsContext ctx) {
        return SecQueryType.LOG_READ;
    }

    @Override
    public SecQueryType visitShowObjectFilter(ShowObjectFilterContext ctx) {
        String entity = ctx.showCommonEntity().getText();
        if (entity.equalsIgnoreCase("STATUS") || entity.equalsIgnoreCase("GLOBALSTATUS") || entity.equalsIgnoreCase("SESSIONSTATUS") || entity.equalsIgnoreCase("LOCALSTATUS")) {
            return SecQueryType.PERFORMANCE;
        }
        return SecQueryType.METADATA;
    }

    @Override
    public SecQueryType visitShowColumns(ShowColumnsContext ctx) {
        return SecQueryType.METADATA;
    }

    @Override
    public SecQueryType visitShowTables(ShowTablesContext ctx) {
        return SecQueryType.METADATA;
    }

    @Override
    public SecQueryType visitShowCreateDb(ShowCreateDbContext ctx) {
        return SecQueryType.METADATA;
    }

    @Override
    public SecQueryType visitShowCreateFullIdObject(ShowCreateFullIdObjectContext ctx) {
        return SecQueryType.METADATA;
    }

    @Override
    public SecQueryType visitShowCreateMaskingPolicy(ShowCreateMaskingPolicyContext ctx) {
        return SecQueryType.METADATA;
    }

    @Override
    public SecQueryType visitShowCreateUser(ShowCreateUserContext ctx) {
        return SecQueryType.METADATA;
    }

    @Override
    public SecQueryType visitShowEngine(ShowEngineContext ctx) {
        if (ctx.engineOption.getType() == LOGS) {
            return SecQueryType.LOG_READ;
        }
        return SecQueryType.PERFORMANCE;
    }

    @Override
    public SecQueryType visitShowEngines(ShowEnginesContext ctx) {
        return SecQueryType.METADATA;
    }

    @Override
    public SecQueryType visitShowStatus(ShowStatusContext ctx) {
        return SecQueryType.LOG_READ;
    }

    @Override
    public SecQueryType visitShowPlugins(ShowPluginsContext ctx) {
        return SecQueryType.METADATA;
    }

    @Override
    public SecQueryType visitShowPrivileges(ShowPrivilegesContext ctx) {
        return SecQueryType.METADATA;
    }

    @Override
    public SecQueryType visitShowProcessList(ShowProcessListContext ctx) {
        return SecQueryType.PERFORMANCE;
    }

    @Override
    public SecQueryType visitShowProfiles(ShowProfilesContext ctx) {
        return SecQueryType.PERFORMANCE;
    }

    @Override
    public SecQueryType visitShowSlaveHosts(ShowSlaveHostsContext ctx) {
        return SecQueryType.METADATA;
    }

    @Override
    public SecQueryType visitShowErrors(ShowErrorsContext ctx) {
        return SecQueryType.PERFORMANCE;
    }

    @Override
    public SecQueryType visitShowCountErrors(ShowCountErrorsContext ctx) {
        return SecQueryType.PERFORMANCE;
    }

    @Override
    public SecQueryType visitShowSchemaFilter(ShowSchemaFilterContext ctx) {
        return SecQueryType.METADATA;
    }

    @Override
    public SecQueryType visitShowRoutine(ShowRoutineContext ctx) {
        return SecQueryType.METADATA;
    }

    @Override
    public SecQueryType visitShowLibraryStatus(ShowLibraryStatusContext ctx) {
        return SecQueryType.METADATA;
    }

    @Override
    public SecQueryType visitShowGrants(ShowGrantsContext ctx) {
        return SecQueryType.METADATA;
    }

    @Override
    public SecQueryType visitShowIndexes(ShowIndexesContext ctx) {
        return SecQueryType.METADATA;
    }

    @Override
    public SecQueryType visitShowOpenTables(ShowOpenTablesContext ctx) {
        return SecQueryType.PERFORMANCE;
    }

    @Override
    public SecQueryType visitShowProfile(ShowProfileContext ctx) {
        return SecQueryType.PERFORMANCE;
    }

    @Override
    public SecQueryType visitResetMaster(ResetMasterContext ctx) {
        return SecQueryType.MAINTAIN_LOG;
    }

    @Override
    public SecQueryType visitResetBinaryLogsAndGtids(ResetBinaryLogsAndGtidsContext ctx) {
        return SecQueryType.MAINTAIN_LOG;
    }

    @Override
    public SecQueryType visitResetSlave(ResetSlaveContext ctx) {
        return SecQueryType.ALTER_REPLICATION;
    }

    @Override
    public SecQueryType visitResetReplica(ResetReplicaContext ctx) {
        return SecQueryType.ALTER_REPLICATION;
    }

    @Override
    public SecQueryType visitResetQueryCache(ResetQueryCacheContext ctx) {
        return SecQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SecQueryType visitResetOptions(ResetOptionsContext ctx) {
        return resetTypes(ctx).stream().findFirst().orElse(SecQueryType.SYSTEM_SETTING_WRITE);
    }

    @Override
    public SecQueryType visitResetPersist(ResetPersistContext ctx) {
        return SecQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SecQueryType visitFlushStatement(FlushStatementContext ctx) {
        return flushTypes(ctx).stream().findFirst().orElse(SecQueryType.SYSTEM_SETTING_WRITE);
    }

    @Override
    public SecQueryType visitShowReplicaStatus(ShowReplicaStatusContext ctx) {
        return SecQueryType.METADATA;
    }

    @Override
    public SecQueryType visitShowParseTree(ShowParseTreeContext ctx) {
        return SecQueryType.PERFORMANCE;
    }

    @Override
    public SecQueryType visitShowReplicas(ShowReplicasContext ctx) {
        return SecQueryType.METADATA;
    }

    @Override
    public SecQueryType visitKillStatement(KillStatementContext ctx) {
        return SecQueryType.ADMIN;
    }

    @Override
    public SecQueryType visitLoadIndexIntoCache(LoadIndexIntoCacheContext ctx) {
        return SecQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SecQueryType visitPurgeBinaryLogs(PurgeBinaryLogsContext ctx) {
        return SecQueryType.MAINTAIN_LOG;
    }

    @Override
    public SecQueryType visitChangeMaster(ChangeMasterContext ctx) {
        return SecQueryType.ALTER_REPLICATION;
    }

    @Override
    public SecQueryType visitChangeReplicationSource(ChangeReplicationSourceContext ctx) {
        return SecQueryType.ALTER_REPLICATION;
    }

    @Override
    public SecQueryType visitChangeReplicationFilter(ChangeReplicationFilterContext ctx) {
        return SecQueryType.ALTER_REPLICATION;
    }

    @Override
    public SecQueryType visitStartSlave(StartSlaveContext ctx) {
        return SecQueryType.ALTER_REPLICATION;
    }

    @Override
    public SecQueryType visitStartReplica(StartReplicaContext ctx) {
        return SecQueryType.ALTER_REPLICATION;
    }

    @Override
    public SecQueryType visitStopSlave(StopSlaveContext ctx) {
        return SecQueryType.ALTER_REPLICATION;
    }

    @Override
    public SecQueryType visitStopReplica(StopReplicaContext ctx) {
        return SecQueryType.ALTER_REPLICATION;
    }

    @Override
    public SecQueryType visitStartGroupReplication(StartGroupReplicationContext ctx) {
        return SecQueryType.ALTER_REPLICATION;
    }

    @Override
    public SecQueryType visitStopGroupReplication(StopGroupReplicationContext ctx) {
        return SecQueryType.ALTER_REPLICATION;
    }

    @Override
    public SecQueryType visitXaStartTransaction(XaStartTransactionContext ctx) {
        return SecQueryType.TRANSACTION;
    }

    @Override
    public SecQueryType visitXaEndTransaction(XaEndTransactionContext ctx) {
        return SecQueryType.TRANSACTION;
    }

    @Override
    public SecQueryType visitXaPrepareStatement(XaPrepareStatementContext ctx) {
        return SecQueryType.TRANSACTION;
    }

    @Override
    public SecQueryType visitXaCommitWork(XaCommitWorkContext ctx) {
        return SecQueryType.TRANSACTION;
    }

    @Override
    public SecQueryType visitXaRollbackWork(XaRollbackWorkContext ctx) {
        return SecQueryType.TRANSACTION;
    }

    @Override
    public SecQueryType visitXaRecoverWork(XaRecoverWorkContext ctx) {
        return SecQueryType.TRANSACTION;
    }

    @Override
    public SecQueryType visitShowSlaveStatus(ShowSlaveStatusContext ctx) {
        return SecQueryType.METADATA;
    }

    @Override
    public SecQueryType visitSetVariable(SetVariableContext ctx) {
        boolean onlyUserVariables = ctx.setVariableAssignment().stream().allMatch(assignment -> assignment.variableClause().LOCAL_ID() != null);
        if (onlyUserVariables) {
            return SecQueryType.SESSION_VARIABLE_RW;
        }
        boolean replicationSetting = ctx.setVariableAssignment().stream().anyMatch(assignment -> {
            String variable = assignment.variableClause().getText().toUpperCase();
            return variable.contains("GTID_") || variable.contains("SLAVE_") || variable.contains("REPLICA_");
        });
        if (replicationSetting) {
            return SecQueryType.ALTER_REPLICATION;
        }
        boolean systemSetting = ctx.setVariableAssignment().stream().anyMatch(assignment -> {
            VariableClauseContext variable = assignment.variableClause();
            String text = variable.getText().toUpperCase();
            return text.startsWith("@@GLOBAL.") || text.startsWith("@@PERSIST.") || text.startsWith("@@PERSIST_ONLY.") || variable.GLOBAL() != null
                   || variable.persistScope() != null;
        });
        return systemSetting ? SecQueryType.SYSTEM_SETTING_WRITE : SecQueryType.SESSION_SETTING_WRITE;
    }

    @Override
    public SecQueryType visitSetCharset(SetCharsetContext ctx) {
        return SecQueryType.SESSION_SETTING_WRITE;
    }

    @Override
    public SecQueryType visitSetNames(SetNamesContext ctx) {
        return SecQueryType.SESSION_SETTING_WRITE;
    }

    @Override
    public SecQueryType visitSetRole(SetRoleContext ctx) {
        return SecQueryType.SWITCH_ROLE;
    }

    @Override
    public SecQueryType visitSetDefaultRole(SetDefaultRoleContext ctx) {
        return SecQueryType.ALTER_USER;
    }

    @Override
    public SecQueryType visitAlterByAddColumn(AlterByAddColumnContext ctx) {
        return SecQueryType.ADD_COLUMN;
    }

    @Override
    public SecQueryType visitAlterByAddColumns(AlterByAddColumnsContext ctx) {
        return SecQueryType.ADD_COLUMN;
    }

    @Override
    public SecQueryType visitAlterByAddIndex(AlterByAddIndexContext ctx) {
        return SecQueryType.ADD_INDEX;
    }

    @Override
    public SecQueryType visitAlterByAddUniqueKey(AlterByAddUniqueKeyContext ctx) {
        return ctx.CONSTRAINT() == null ? SecQueryType.ADD_INDEX : SecQueryType.ADD_CONSTRAINT;
    }

    @Override
    public SecQueryType visitAlterByAddSpecialIndex(AlterByAddSpecialIndexContext ctx) {
        return SecQueryType.ADD_INDEX;
    }

    @Override
    public SecQueryType visitAlterByAddPrimaryKey(AlterByAddPrimaryKeyContext ctx) {
        return SecQueryType.ADD_CONSTRAINT;
    }

    @Override
    public SecQueryType visitAlterByAddForeignKey(AlterByAddForeignKeyContext ctx) {
        return SecQueryType.ADD_CONSTRAINT;
    }

    @Override
    public SecQueryType visitAlterByAddCheckTableConstraint(AlterByAddCheckTableConstraintContext ctx) {
        return SecQueryType.ADD_CONSTRAINT;
    }

    @Override
    public SecQueryType visitAlterBySetMaskingPolicy(AlterBySetMaskingPolicyContext ctx) {
        return SecQueryType.ALTER_COLUMN;
    }

    @Override
    public SecQueryType visitAlterByDropMaskingPolicy(AlterByDropMaskingPolicyContext ctx) {
        return SecQueryType.ALTER_COLUMN;
    }

    @Override
    public SecQueryType visitAlterByChangeDefault(AlterByChangeDefaultContext ctx) {
        return SecQueryType.ALTER_COLUMN;
    }

    @Override
    public SecQueryType visitAlterByChangeColumn(AlterByChangeColumnContext ctx) {
        return SecQueryType.ALTER_COLUMN;
    }

    @Override
    public SecQueryType visitAlterByModifyColumn(AlterByModifyColumnContext ctx) {
        return SecQueryType.ALTER_COLUMN;
    }

    @Override
    public SecQueryType visitAlterByRenameColumn(AlterByRenameColumnContext ctx) {
        return SecQueryType.RENAME_COLUMN;
    }

    @Override
    public SecQueryType visitAlterByDropColumn(AlterByDropColumnContext ctx) {
        return SecQueryType.DROP_COLUMN;
    }

    @Override
    public SecQueryType visitAlterByAlterConstraintEnforcement(AlterByAlterConstraintEnforcementContext ctx) {
        return SecQueryType.ALTER_CONSTRAINT;
    }

    @Override
    public SecQueryType visitAlterByDropConstraintCheck(AlterByDropConstraintCheckContext ctx) {
        return SecQueryType.DROP_CONSTRAINT;
    }

    @Override
    public SecQueryType visitAlterByDropPrimaryKey(AlterByDropPrimaryKeyContext ctx) {
        return SecQueryType.DROP_CONSTRAINT;
    }

    @Override
    public SecQueryType visitAlterByDropForeignKey(AlterByDropForeignKeyContext ctx) {
        return SecQueryType.DROP_CONSTRAINT;
    }

    @Override
    public SecQueryType visitAlterByDropIndex(AlterByDropIndexContext ctx) {
        return SecQueryType.DROP_INDEX;
    }

    @Override
    public SecQueryType visitAlterByRenameIndex(AlterByRenameIndexContext ctx) {
        return SecQueryType.RENAME_INDEX;
    }

    @Override
    public SecQueryType visitAlterByAlterIndexVisibility(AlterByAlterIndexVisibilityContext ctx) {
        return SecQueryType.ALTER_INDEX;
    }

    @Override
    public SecQueryType visitAlterByRename(AlterByRenameContext ctx) {
        return SecQueryType.RENAME_TABLE;
    }

    @Override
    public SecQueryType visitAlterByDiscardTablespace(AlterByDiscardTablespaceContext ctx) {
        return SecQueryType.ADMIN_TABLE;
    }

    @Override
    public SecQueryType visitAlterByImportTablespace(AlterByImportTablespaceContext ctx) {
        return SecQueryType.ADMIN_TABLE;
    }

    @Override
    public SecQueryType visitAlterByDisableKeys(AlterByDisableKeysContext ctx) {
        return SecQueryType.ADMIN_TABLE;
    }

    @Override
    public SecQueryType visitAlterByEnableKeys(AlterByEnableKeysContext ctx) {
        return SecQueryType.ADMIN_TABLE;
    }

    @Override
    public SecQueryType visitAlterByOrder(AlterByOrderContext ctx) {
        return SecQueryType.ADMIN_TABLE;
    }

    @Override
    public SecQueryType visitAlterByForce(AlterByForceContext ctx) {
        return SecQueryType.ADMIN_TABLE;
    }

    @Override
    public SecQueryType visitAlterByAddPartition(AlterByAddPartitionContext ctx) {
        return SecQueryType.ADD_PARTITION;
    }

    @Override
    public SecQueryType visitAlterByDropPartition(AlterByDropPartitionContext ctx) {
        return SecQueryType.DROP_PARTITION;
    }

    @Override
    public SecQueryType visitAlterByTruncatePartition(AlterByTruncatePartitionContext ctx) {
        return SecQueryType.TRUNCATE_PARTITION;
    }

    @Override
    public SecQueryType visitAlterByCoalescePartition(AlterByCoalescePartitionContext ctx) {
        return SecQueryType.ALTER_PARTITION;
    }

    @Override
    public SecQueryType visitAlterByReorganizePartition(AlterByReorganizePartitionContext ctx) {
        return SecQueryType.ALTER_PARTITION;
    }

    @Override
    public SecQueryType visitAlterByExchangePartition(AlterByExchangePartitionContext ctx) {
        return SecQueryType.ALTER_PARTITION;
    }

    @Override
    public SecQueryType visitAlterByRemovePartitioning(AlterByRemovePartitioningContext ctx) {
        return SecQueryType.ALTER_PARTITION;
    }

    @Override
    public SecQueryType visitAlterByUpgradePartitioning(AlterByUpgradePartitioningContext ctx) {
        return SecQueryType.ALTER_PARTITION;
    }

    @Override
    public SecQueryType visitAlterByAnalyzePartition(AlterByAnalyzePartitionContext ctx) {
        return SecQueryType.ADMIN_PARTITION;
    }

    @Override
    public SecQueryType visitAlterByCheckPartition(AlterByCheckPartitionContext ctx) {
        return SecQueryType.ADMIN_PARTITION;
    }

    @Override
    public SecQueryType visitAlterByOptimizePartition(AlterByOptimizePartitionContext ctx) {
        return SecQueryType.ADMIN_PARTITION;
    }

    @Override
    public SecQueryType visitAlterByDiscardPartition(AlterByDiscardPartitionContext ctx) {
        return SecQueryType.ADMIN_PARTITION;
    }

    @Override
    public SecQueryType visitAlterByImportPartition(AlterByImportPartitionContext ctx) {
        return SecQueryType.ADMIN_PARTITION;
    }

    @Override
    public SecQueryType visitAlterByRebuildPartition(AlterByRebuildPartitionContext ctx) {
        return SecQueryType.ADMIN_PARTITION;
    }

    @Override
    public SecQueryType visitAlterByRepairPartition(AlterByRepairPartitionContext ctx) {
        return SecQueryType.ADMIN_PARTITION;
    }

    @Override
    public SecQueryType visitAlterBySecondaryLoad(AlterBySecondaryLoadContext ctx) {
        return SecQueryType.ADMIN_PARTITION;
    }

    @Override
    public SecQueryType visitAlterBySecondaryUnload(AlterBySecondaryUnloadContext ctx) {
        return SecQueryType.ADMIN_PARTITION;
    }

    @Override
    public SecQueryType visitTableOptionComment(TableOptionCommentContext ctx) {
        return SecQueryType.COMMENT_TABLE;
    }

    @Override
    public SecQueryType visitCommentColumnConstraint(CommentColumnConstraintContext ctx) {
        return SecQueryType.COMMENT_COLUMN;
    }

    @Override
    public SecQueryType visitCommonIndexOption(CommonIndexOptionContext ctx) {
        return ctx.COMMENT() == null ? null : SecQueryType.COMMENT_INDEX;
    }

    @Override
    public SecQueryType visitPartitionOptionComment(PartitionOptionCommentContext ctx) {
        return SecQueryType.COMMENT_PARTITION;
    }

    @Override
    public SecQueryType visitTablespaceOption(TablespaceOptionContext ctx) {
        return ctx.COMMENT() == null ? null : SecQueryType.COMMENT_TABLESPACE;
    }

    @Override
    public SecQueryType visitLogfileGroupOption(LogfileGroupOptionContext ctx) {
        return null;
    }

    public SecQueryType visitChildren(RuleNode node) {
        if (this.currentNodeOnly) {
            return null;
        }

        int n = node.getChildCount();

        for (int i = 0; i < n; ++i) {
            ParseTree c = node.getChild(i);
            SecQueryType result = c.accept(this);
            if (result != null) {
                return result;
            }
        }

        return null;
    }

}
