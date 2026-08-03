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

import java.util.*;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;

import com.clougence.clouddm.ds.tidb.sql.parser.antlr.TiDBParser;
import com.clougence.clouddm.ds.tidb.sql.parser.antlr.TiDBParserBaseVisitor;
import com.clougence.clouddm.ds.tidb.sql.parser.antlr.TiDBParser.*;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.clouddm.ds.tidb.sql.analysis.reference.TiDBObjectReference;
import com.clougence.clouddm.ds.tidb.sql.analysis.reference.TiDBResourceRegistry;
import com.clougence.clouddm.ds.tidb.sql.parser.TiDBVersion;
import com.clougence.utils.StringUtils;

public class TiDBObjectReferenceVisitor extends TiDBParserBaseVisitor<Void> {
    private final Parser                     parser;
    private final Map<UmiTypes, Object>      levelsParam;
    private final int                        baseLine;
    private final int                        baseColumn;
    private final TiDBVersion               version;
    private final int                        exactVersion;
    private final TiDBResourceRegistry      resources;
    private final List<TiDBObjectReference> references = new ArrayList<>();

    public TiDBObjectReferenceVisitor(Parser parser, Map<UmiTypes, Object> levelsParam, int baseLine, int baseColumn, TiDBVersion version, int exactVersion,
                                      TiDBResourceRegistry resources){
        this.parser = parser;
        this.levelsParam = levelsParam;
        this.baseLine = baseLine;
        this.baseColumn = baseColumn;
        this.version = version;
        this.exactVersion = exactVersion;
        this.resources = resources;
    }

    public List<TiDBObjectReference> references() {
        return references;
    }

    public void scan(ParseTree tree) {
        if (tree == null) {
            return;
        }
        tree.accept(this);
        for (int i = 0; i < tree.getChildCount(); i++) {
            scan(tree.getChild(i));
        }
    }

    /**
     * Resource extraction owns recursion explicitly through {@link #scan(ParseTree)}.
     * This makes every descendant context visible even when a statement-specific
     * visitor intentionally finishes after handling its own target.
     */
    @Override
    public Void visitChildren(RuleNode node) {
        return null;
    }

    public void addUnnamedFallback(SplitQueryType sqlType, TargetType targetType, ParserRuleContext ctx) {
        addUnnamedResource(sqlType, targetType, true, ctx);
    }

    protected final void addUnnamed(SplitQueryType sqlType, TargetType targetType, boolean require, Token token) {
        references.add(new TiDBObjectReference(sqlType, targetType, require, line(token), column(token), endLine(token), endColumn(token), List.of()));
    }

    protected final void addUnnamedAtCurrentSchema(SplitQueryType sqlType, TargetType targetType, boolean require, ParserRuleContext ctx) {
        List<String> nodes = new ArrayList<>();
        addPart(nodes, level(UmiTypes.Catalog));
        addPart(nodes, level(UmiTypes.Schema));
        if (nodes.isEmpty()) {
            addUnnamedResource(sqlType, targetType, require, ctx);
        } else {
            references.add(new TiDBObjectReference(sqlType, targetType, require, line(ctx), column(ctx), endLine(ctx), endColumn(ctx), nodes, null, false));
        }
    }

    @Override
    public Void visitQuerySpecificationSelect(QuerySpecificationSelectContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Void visitQueryExpressionSelect(QueryExpressionSelectContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Void visitSelectExpressionElement(SelectExpressionElementContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Void visitGenericFunctionCall(GenericFunctionCallContext ctx) {
        if (ctx.genericFunction().name instanceof CustomGenericFunctionNameContext custom) {
            FullIdContext fullId = custom.function.fullId();
            String qualifiedName = parser.getTokenStream().getText(fullId.getStart(), fullId.getStop());
            int separator = qualifiedName.lastIndexOf('.');
            String functionName = unquoteIdentifier(qualifiedName.substring(separator + 1).strip());
            if (resources.isUserDefinedFunction(functionName, fullId.DOT() != null, this.version)) {
                add(SplitQueryType.CALL_PROG_OBJ, TargetType.Function, fullId);
            }
        } else if (resources.isUserDefinedFunction(ctx.genericFunction().name.getText(), false, this.version)) {
            add(SplitQueryType.CALL_PROG_OBJ, TargetType.Function, ctx.genericFunction().name);
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitAggregateFunctionCall(AggregateFunctionCallContext ctx) {
        String functionName = ctx.aggregateFunction().getStart().getText();
        if (!resources.isBuiltInAggregateFunction(functionName, exactVersion)) {
            add(SplitQueryType.CALL_PROG_OBJ, TargetType.Function, ctx.aggregateFunction());
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitSpatialAggregateFunctionCall(SpatialAggregateFunctionCallContext ctx) {
        String functionName = ctx.customFunctionName().getStart().getText();
        if (!resources.isBuiltInAggregateFunction(functionName, exactVersion)) {
            add(SplitQueryType.CALL_PROG_OBJ, TargetType.Function, ctx.customFunctionName());
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitNonKeywordFunctionCall(NonKeywordFunctionCallContext ctx) {
        addFunction(ctx);
        return visitChildren(ctx);
    }

    @Override
    public Void visitSpecificFunctionCall(SpecificFunctionCallContext ctx) {
        addFunction(ctx);
        return visitChildren(ctx);
    }

    @Override
    public Void visitKeywordFunctionCall(KeywordFunctionCallContext ctx) {
        addFunction(ctx);
        return visitChildren(ctx);
    }

    @Override
    public Void visitPasswordFunctionCall(PasswordFunctionCallContext ctx) {
        addFunction(ctx);
        return visitChildren(ctx);
    }

    @Override
    public Void visitNonAggregateFunctionCall(NonAggregateFunctionCallContext ctx) {
        addFunction(ctx);
        return visitChildren(ctx);
    }

    @Override
    public Void visitJsonDualityObjectFunctionCall(JsonDualityObjectFunctionCallContext ctx) {
        addFunction(ctx);
        return visitChildren(ctx);
    }

    @Override
    public Void visitCreateUser(CreateUserContext ctx) {
        ctx.userAuthOption().forEach(option -> addDescendantAccounts(SplitQueryType.CREATE_USER, TargetType.User, false, option));
        ctx.createUserAuthOption().forEach(option -> addDescendantAccounts(SplitQueryType.CREATE_USER, TargetType.User, false, option));
        if (ctx.defaultRoleClause() != null) {
            descendants(ctx.defaultRoleClause(), RoleNameContext.class).forEach(role -> addAccount(SplitQueryType.CREATE_ROLE, TargetType.Role, true, role));
        }
        return null;
    }

    @Override
    public Void visitDropUser(DropUserContext ctx) {
        boolean require = ctx.ifExists() == null;
        ctx.accountTarget().forEach(target -> addDescendantAccounts(SplitQueryType.DROP_USER, TargetType.User, require, target));
        return null;
    }

    @Override
    public Void visitCreateRole(CreateRoleContext ctx) {
        ctx.roleName().forEach(role -> addAccount(SplitQueryType.CREATE_ROLE, TargetType.Role, false, role));
        return null;
    }

    @Override
    public Void visitDropRole(DropRoleContext ctx) {
        boolean require = ctx.ifExists() == null;
        ctx.roleName().forEach(role -> addAccount(SplitQueryType.DROP_ROLE, TargetType.Role, require, role));
        return null;
    }

    @Override
    public Void visitRenameUser(RenameUserContext ctx) {
        for (RenameUserClauseContext clause : ctx.renameUserClause()) {
            addDescendantAccounts(SplitQueryType.RENAME_USER, TargetType.User, true, clause.fromFirst);
            addAccount(SplitQueryType.RENAME_USER, TargetType.User, false, clause.toFirst);
        }
        return null;
    }

    @Override
    public Void visitGrantStatement(GrantStatementContext ctx) {
        addDescendantAccounts(SplitQueryType.GRANT, TargetType.UserOrRole, true, ctx);
        descendants(ctx, RoleNameContext.class).forEach(role -> addAccount(SplitQueryType.GRANT, TargetType.UserOrRole, true, role));
        addPrivilegeTarget(SplitQueryType.GRANT, ctx.privilegeObject, ctx.privilegeLevel());
        return null;
    }

    @Override
    public Void visitRevokeStatement(RevokeStatementContext ctx) {
        addDescendantAccounts(SplitQueryType.REVOKE, TargetType.UserOrRole, true, ctx);
        descendants(ctx, RoleNameContext.class).forEach(role -> addAccount(SplitQueryType.REVOKE, TargetType.UserOrRole, true, role));
        addPrivilegeTarget(SplitQueryType.REVOKE, ctx.privilegeObject, ctx.privilegeLevel());
        return null;
    }

    @Override
    public Void visitGrantProxy(GrantProxyContext ctx) {
        addDescendantAccounts(SplitQueryType.GRANT, TargetType.UserOrRole, true, ctx);
        return null;
    }

    @Override
    public Void visitRevokeProxy(RevokeProxyContext ctx) {
        addDescendantAccounts(SplitQueryType.REVOKE, TargetType.UserOrRole, true, ctx);
        return null;
    }

    @Override
    public Void visitFlushStatement(FlushStatementContext ctx) {
        FlushTablesOptionContext tableOption = ctx.flushTablesOption();
        if (tableOption != null) {
            FlushTableOptionContext modifier = tableOption.flushTableOption();
            SplitQueryType permission = modifier != null && modifier.EXPORT() != null ? SplitQueryType.DATA_EXPORT : SplitQueryType.ADMIN_TABLE;
            addTables(tableOption.tables(), permission);
            if (modifier != null && modifier.READ() != null) {
                addTables(tableOption.tables(), SplitQueryType.QUERY_LOCK);
            }
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitAlterUserMysqlV56(AlterUserMysqlV56Context ctx) {
        return addAlterUsers(ctx);
    }

    @Override
    public Void visitAlterUserMysqlV57(AlterUserMysqlV57Context ctx) {
        return addAlterUsers(ctx);
    }

    @Override
    public Void visitAlterUserCurrentUser(AlterUserCurrentUserContext ctx) {
        return addAlterUsers(ctx);
    }

    @Override
    public Void visitAlterUserCurrentUserDiscard(AlterUserCurrentUserDiscardContext ctx) {
        return addAlterUsers(ctx);
    }

    @Override
    public Void visitAlterUserDefaultRole(AlterUserDefaultRoleContext ctx) {
        return addAlterUsers(ctx);
    }

    @Override
    public Void visitAlterUserDiscardOldPassword(AlterUserDiscardOldPasswordContext ctx) {
        return addAlterUsers(ctx);
    }

    @Override
    public Void visitAlterUserMfa(AlterUserMfaContext ctx) {
        return addAlterUsers(ctx);
    }

    private void addFunction(ParserRuleContext ctx) {
        Token token = ctx.getStart();
        if (token == null || StringUtils.isBlank(token.getText()) || !resources.isUserDefinedFunction(token.getText(), false, this.version)) {
            return;
        }
        List<String> parts = new ArrayList<>();
        addPart(parts, unquoteIdentifier(token.getText()));
        List<String> nodes = resolveNodes(TargetType.Function, parts);
        addWithNodes(SplitQueryType.CALL_PROG_OBJ, TargetType.Function, true, token, nodes);
    }

    @Override
    public Void visitAtomTableItem(AtomTableItemContext ctx) {
        add(SplitQueryType.SELECT, TargetType.Table, ctx.tableName());
        return visitChildren(ctx);
    }

    @Override
    public Void visitColumnCreateTable(ColumnCreateTableContext ctx) {
        add(SplitQueryType.CREATE_TABLE, TargetType.Table, false, ctx.tableName());
        return null;
    }

    @Override
    public Void visitQueryCreateTable(QueryCreateTableContext ctx) {
        add(SplitQueryType.CREATE_TABLE, TargetType.Table, false, ctx.tableName());
        visit(ctx.createTableQueryExpression());
        return null;
    }

    @Override
    public Void visitCopyCreateTable(CopyCreateTableContext ctx) {
        if (!ctx.tableName().isEmpty()) {
            add(SplitQueryType.CREATE_TABLE, TargetType.Table, false, ctx.tableName(0));
        }
        if (ctx.tableName().size() > 1) {
            add(SplitQueryType.SELECT, TargetType.Table, ctx.tableName(1));
        } else if (ctx.parenthesisTable != null) {
            add(SplitQueryType.SELECT, TargetType.Table, ctx.parenthesisTable);
        }
        return null;
    }

    @Override
    public Void visitCreateView(CreateViewContext ctx) {
        if (ctx.MATERIALIZED() == null) {
            add(SplitQueryType.CREATE_VIEW, TargetType.View, false, ctx.fullId());
        } else {
            add(SplitQueryType.CREATE_VIEW, TargetType.Materialized, false, ctx.fullId());
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitAlterView(AlterViewContext ctx) {
        if (ctx.MATERIALIZED() == null) {
            add(SplitQueryType.ALTER_VIEW, TargetType.View, ctx.fullId());
        } else {
            add(SplitQueryType.ALTER_VIEW, TargetType.Materialized, ctx.fullId());
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitCreateProcedure(CreateProcedureContext ctx) {
        add(SplitQueryType.CREATE_PROG_OBJ, TargetType.Procedure, false, ctx.fullId());
        return visitChildren(ctx);
    }

    @Override
    public Void visitCreateFunction(CreateFunctionContext ctx) {
        add(SplitQueryType.CREATE_PROG_OBJ, TargetType.Function, false, ctx.fullId());
        return visitChildren(ctx);
    }

    @Override
    public Void visitCreateUdfFunction(CreateUdfFunctionContext ctx) {
        add(SplitQueryType.CREATE_PROG_OBJ, TargetType.Function, false, ctx.uid());
        addFile(SplitQueryType.CREATE_PROG_OBJ, true, ctx.textLiteralToken());
        return null;
    }

    @Override
    public Void visitAlterSimpleDatabase(AlterSimpleDatabaseContext ctx) {
        if (ctx.databaseName() != null) {
            add(SplitQueryType.ALTER_SCHEMA, TargetType.Schema, ctx.databaseName().uid());
        } else {
            List<String> nodes = new ArrayList<>();
            addPart(nodes, level(UmiTypes.Catalog));
            addPart(nodes, level(UmiTypes.Schema));
            addWithNodes(SplitQueryType.ALTER_SCHEMA, TargetType.Schema, true, ctx, nodes, false);
        }
        return null;
    }

    @Override
    public Void visitCreateLibrary(CreateLibraryContext ctx) {
        add(SplitQueryType.CREATE_LIBRARY, TargetType.Library, false, ctx.fullId());
        return null;
    }

    @Override
    public Void visitCreateMaskingPolicy(CreateMaskingPolicyContext ctx) {
        add(SplitQueryType.CREATE_POLICY, TargetType.MaskingPolicy, false, ctx.policyName);
        return null;
    }

    @Override
    public Void visitAlterProcedure(AlterProcedureContext ctx) {
        add(SplitQueryType.ALTER_PROG_OBJ, TargetType.Procedure, ctx.fullId());
        return null;
    }

    @Override
    public Void visitAlterFunction(AlterFunctionContext ctx) {
        add(SplitQueryType.ALTER_PROG_OBJ, TargetType.Function, ctx.fullId());
        return null;
    }

    @Override
    public Void visitAlterLibrary(AlterLibraryContext ctx) {
        add(SplitQueryType.ALTER_LIBRARY, TargetType.Library, ctx.fullId());
        return null;
    }

    @Override
    public Void visitDropProcedure(DropProcedureContext ctx) {
        add(SplitQueryType.DROP_PROG_OBJ, TargetType.Procedure, ctx.ifExists() == null, ctx.fullId());
        return null;
    }

    @Override
    public Void visitDropFunction(DropFunctionContext ctx) {
        add(SplitQueryType.DROP_PROG_OBJ, TargetType.Function, ctx.ifExists() == null, ctx.fullId());
        return null;
    }

    @Override
    public Void visitDropLibrary(DropLibraryContext ctx) {
        add(SplitQueryType.DROP_LIBRARY, TargetType.Library, ctx.ifExists() == null, ctx.fullId());
        return null;
    }

    @Override
    public Void visitDropMaskingPolicy(DropMaskingPolicyContext ctx) {
        add(SplitQueryType.DROP_POLICY, TargetType.MaskingPolicy, ctx.ifExists() == null, ctx.uid());
        return null;
    }

    @Override
    public Void visitCreateTablespaceInnodb(CreateTablespaceInnodbContext ctx) {
        add(SplitQueryType.CREATE_TABLESPACE, TargetType.Tablespace, false, ctx.uid());
        return null;
    }

    @Override
    public Void visitCreateTablespaceNdb(CreateTablespaceNdbContext ctx) {
        if (!ctx.uid().isEmpty()) {
            add(SplitQueryType.CREATE_TABLESPACE, TargetType.Tablespace, false, ctx.uid(0));
        }
        return null;
    }

    @Override
    public Void visitCreateUndoTablespace(CreateUndoTablespaceContext ctx) {
        add(SplitQueryType.CREATE_TABLESPACE, TargetType.Tablespace, false, ctx.uid());
        return null;
    }

    @Override
    public Void visitAlterTablespace(AlterTablespaceContext ctx) {
        for (UidContext uid : ctx.uid()) {
            add(SplitQueryType.ALTER_TABLESPACE, TargetType.Tablespace, uid);
        }
        return null;
    }

    @Override
    public Void visitAlterUndoTablespace(AlterUndoTablespaceContext ctx) {
        add(SplitQueryType.ALTER_TABLESPACE, TargetType.Tablespace, ctx.uid());
        return null;
    }

    @Override
    public Void visitDropTablespace(DropTablespaceContext ctx) {
        add(SplitQueryType.DROP_TABLESPACE, TargetType.Tablespace, ctx.uid());
        return null;
    }

    @Override
    public Void visitDropUndoTablespace(DropUndoTablespaceContext ctx) {
        add(SplitQueryType.DROP_TABLESPACE, TargetType.Tablespace, ctx.uid());
        return null;
    }

    @Override
    public Void visitCreateLogfileGroup(CreateLogfileGroupContext ctx) {
        add(SplitQueryType.CREATE_LOG, TargetType.Log, false, ctx.uid());
        return null;
    }

    @Override
    public Void visitAlterLogfileGroup(AlterLogfileGroupContext ctx) {
        add(SplitQueryType.ALTER_LOG, TargetType.Log, ctx.uid());
        return null;
    }

    @Override
    public Void visitDropLogfileGroup(DropLogfileGroupContext ctx) {
        add(SplitQueryType.DROP_LOG, TargetType.Log, ctx.uid());
        return null;
    }

    @Override
    public Void visitCreateResourceGroup(CreateResourceGroupContext ctx) {
        add(SplitQueryType.CREATE_RESOURCE_GROUP, TargetType.ResourceGroup, false, ctx.resourceGroupName());
        return null;
    }

    @Override
    public Void visitAlterResourceGroup(AlterResourceGroupContext ctx) {
        add(SplitQueryType.ALTER_RESOURCE_GROUP, TargetType.ResourceGroup, ctx.resourceGroupName());
        return null;
    }

    @Override
    public Void visitDropResourceGroup(DropResourceGroupContext ctx) {
        add(SplitQueryType.DROP_RESOURCE_GROUP, TargetType.ResourceGroup, ctx.resourceGroupName());
        return null;
    }

    @Override
    public Void visitSetResourceGroup(SetResourceGroupContext ctx) {
        add(SplitQueryType.ADMIN_RESOURCE_GROUP, TargetType.ResourceGroup, ctx.resourceGroupName());
        return null;
    }

    @Override
    public Void visitCreateServer(CreateServerContext ctx) {
        addQuotedResourceName(SplitQueryType.SYSTEM_SETTING_WRITE, TargetType.ConfigKey, false, ctx.serverObjectName());
        return null;
    }

    @Override
    public Void visitAlterServer(AlterServerContext ctx) {
        addQuotedResourceName(SplitQueryType.SYSTEM_SETTING_WRITE, TargetType.ConfigKey, true, ctx.serverObjectName());
        return null;
    }

    @Override
    public Void visitDropServer(DropServerContext ctx) {
        addQuotedResourceName(SplitQueryType.SYSTEM_SETTING_WRITE, TargetType.ConfigKey, true, ctx.serverObjectName());
        return null;
    }

    @Override
    public Void visitCreateSpatialReferenceSystem(CreateSpatialReferenceSystemContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, TargetType.ConfigKey, false, ctx.decimalLiteral());
        return null;
    }

    @Override
    public Void visitDropSpatialReferenceSystem(DropSpatialReferenceSystemContext ctx) {
        add(SplitQueryType.SYSTEM_SETTING_WRITE, TargetType.ConfigKey, ctx.decimalLiteral());
        return null;
    }

    @Override
    public Void visitChecksumTable(ChecksumTableContext ctx) {
        addAdminTables(ctx.tables());
        return null;
    }

    @Override
    public Void visitCheckTable(CheckTableContext ctx) {
        addAdminTables(ctx.tables());
        return null;
    }

    @Override
    public Void visitRepairTable(RepairTableContext ctx) {
        addAdminTables(ctx.tables());
        return null;
    }

    @Override
    public Void visitOptimizeTable(OptimizeTableContext ctx) {
        addAdminTables(ctx.tables());
        return null;
    }

    @Override
    public Void visitAnalyzeTable(AnalyzeTableContext ctx) {
        if (ctx.tableName() != null) {
            add(SplitQueryType.ADMIN_TABLE, TargetType.Table, ctx.tableName());
        } else {
            addAdminTables(ctx.tables());
        }
        return null;
    }

    @Override
    public Void visitSimpleDescribeStatement(SimpleDescribeStatementContext ctx) {
        add(SplitQueryType.METADATA, TargetType.Table, ctx.tableName());
        return null;
    }

    @Override
    public Void visitShowColumns(ShowColumnsContext ctx) {
        addMetadataTable(ctx.tableName(), ctx.uid());
        return null;
    }

    @Override
    public Void visitShowIndexes(ShowIndexesContext ctx) {
        addMetadataTable(ctx.tableName(), ctx.uid());
        return visitChildren(ctx);
    }

    @Override
    public Void visitShowTables(ShowTablesContext ctx) {
        if (!ctx.uid().isEmpty()) {
            add(SplitQueryType.METADATA, TargetType.Schema, ctx.uid(0));
        } else {
            addUnnamedResource(SplitQueryType.METADATA, TargetType.Schema, true, ctx);
        }
        return null;
    }

    @Override
    public Void visitShowCreateDb(ShowCreateDbContext ctx) {
        add(SplitQueryType.METADATA, TargetType.Schema, ctx.uid());
        return null;
    }

    @Override
    public Void visitShowCreateFullIdObject(ShowCreateFullIdObjectContext ctx) {
        TargetType type = switch (ctx.namedEntity.getText().toUpperCase()) {
            case "EVENT" -> TargetType.Event;
            case "FUNCTION" -> TargetType.Function;
            case "PROCEDURE" -> TargetType.Procedure;
            case "TRIGGER" -> TargetType.Trigger;
            case "VIEW" -> TargetType.View;
            case "LIBRARY" -> TargetType.Library;
            default -> TargetType.Table;
        };
        add(SplitQueryType.METADATA, type, ctx.fullId());
        return null;
    }

    @Override
    public Void visitShowCreateUser(ShowCreateUserContext ctx) {
        if (ctx.userName() == null) {
            addUnnamedResource(SplitQueryType.METADATA, TargetType.User, true, ctx);
        } else {
            addAccount(SplitQueryType.METADATA, TargetType.User, true, ctx.userName());
        }
        return null;
    }

    @Override
    public Void visitCacheIndexStatement(CacheIndexStatementContext ctx) {
        add(SplitQueryType.ADMIN_PERFORMANCE, TargetType.Index, ctx.tableName());
        for (TableIndexesContext tableIndexes : ctx.tableIndexes()) {
            add(SplitQueryType.ADMIN_PERFORMANCE, TargetType.Index, tableIndexes.tableName());
        }
        return null;
    }

    @Override
    public Void visitAlterInstance(AlterInstanceContext ctx) {
        addUnnamedResource(SplitQueryType.SYSTEM_SETTING_WRITE, TargetType.ConfigKey, true, ctx);
        return null;
    }

    @Override
    public Void visitCloneStatement(CloneStatementContext ctx) {
        addUnnamedResource(SplitQueryType.ADMIN, TargetType.Instance, true, ctx);
        return null;
    }

    @Override
    public Void visitBinlogStatement(BinlogStatementContext ctx) {
        addUnnamedResource(SplitQueryType.ADMIN_LOG, TargetType.Log, true, ctx);
        return null;
    }

    @Override
    public Void visitInstallComponent(InstallComponentContext ctx) {
        addUnnamedResource(SplitQueryType.CREATE_LIBRARY, TargetType.Library, false, ctx);
        return null;
    }

    @Override
    public Void visitInstallPlugin(InstallPluginContext ctx) {
        addUnnamedResource(SplitQueryType.CREATE_LIBRARY, TargetType.Library, false, ctx);
        return null;
    }

    @Override
    public Void visitUninstallComponent(UninstallComponentContext ctx) {
        addUnnamedResource(SplitQueryType.DROP_LIBRARY, TargetType.Library, true, ctx);
        return null;
    }

    @Override
    public Void visitUninstallPlugin(UninstallPluginContext ctx) {
        addUnnamedResource(SplitQueryType.DROP_LIBRARY, TargetType.Library, true, ctx);
        return null;
    }

    @Override
    public Void visitDoStatement(DoStatementContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Void visitSignalStatement(SignalStatementContext ctx) {
        return null;
    }

    @Override
    public Void visitResignalStatement(ResignalStatementContext ctx) {
        return null;
    }

    @Override
    public Void visitDiagnosticsStatement(DiagnosticsStatementContext ctx) {
        descendants(ctx, VariableClauseContext.class).stream()
            .filter(variable -> variable.LOCAL_ID() != null || variable.GLOBAL_ID() != null || variable.GLOBAL() != null || variable.SESSION() != null || variable.LOCAL() != null
                                || variable.persistScope() != null)
            .forEach(variable -> addConfigKey(SplitQueryType.SESSION_VARIABLE_RW, variable));
        return null;
    }

    @Override
    public Void visitTransactionStatement(TransactionStatementContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Void visitChangeMaster(ChangeMasterContext ctx) {
        addUnnamedResource(SplitQueryType.ALTER_REPLICATION, TargetType.Replication, true, ctx);
        return null;
    }

    @Override
    public Void visitChangeReplicationSource(ChangeReplicationSourceContext ctx) {
        addUnnamedResource(SplitQueryType.ALTER_REPLICATION, TargetType.Replication, true, ctx);
        return null;
    }

    @Override
    public Void visitChangeReplicationFilter(ChangeReplicationFilterContext ctx) {
        addUnnamedResource(SplitQueryType.ALTER_REPLICATION, TargetType.Replication, true, ctx);
        return null;
    }

    @Override
    public Void visitStartSlave(StartSlaveContext ctx) {
        addUnnamedResource(SplitQueryType.ADMIN_REPLICATION, TargetType.Replication, true, ctx);
        return null;
    }

    @Override
    public Void visitStartReplica(StartReplicaContext ctx) {
        addUnnamedResource(SplitQueryType.ADMIN_REPLICATION, TargetType.Replication, true, ctx);
        return null;
    }

    @Override
    public Void visitStartGroupReplication(StartGroupReplicationContext ctx) {
        addUnnamedResource(SplitQueryType.ADMIN_REPLICATION, TargetType.Replication, true, ctx);
        return null;
    }

    @Override
    public Void visitStopSlave(StopSlaveContext ctx) {
        addUnnamedResource(SplitQueryType.ADMIN_REPLICATION, TargetType.Replication, true, ctx);
        return null;
    }

    @Override
    public Void visitStopReplica(StopReplicaContext ctx) {
        addUnnamedResource(SplitQueryType.ADMIN_REPLICATION, TargetType.Replication, true, ctx);
        return null;
    }

    @Override
    public Void visitStopGroupReplication(StopGroupReplicationContext ctx) {
        addUnnamedResource(SplitQueryType.ADMIN_REPLICATION, TargetType.Replication, true, ctx);
        return null;
    }

    @Override
    public Void visitCreateTrigger(CreateTriggerContext ctx) {
        add(SplitQueryType.CREATE_TRIGGER, TargetType.Trigger, false, ctx.thisTrigger);
        add(SplitQueryType.ALTER_TABLE, TargetType.Table, ctx.tableName());
        return visitChildren(ctx);
    }

    @Override
    public Void visitDropTrigger(DropTriggerContext ctx) {
        add(SplitQueryType.DROP_TRIGGER, TargetType.Trigger, ctx.ifExists() == null, ctx.fullId());
        return null;
    }

    @Override
    public Void visitCreateEvent(CreateEventContext ctx) {
        add(SplitQueryType.CREATE_EVENT, TargetType.Event, false, ctx.fullId());
        return visitChildren(ctx);
    }

    @Override
    public Void visitAlterEvent(AlterEventContext ctx) {
        add(SplitQueryType.ALTER_EVENT, TargetType.Event, ctx.fullId(0));
        if (ctx.fullId().size() > 1) {
            add(SplitQueryType.ALTER_EVENT, TargetType.Event, false, ctx.fullId(1));
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitDropEvent(DropEventContext ctx) {
        add(SplitQueryType.DROP_EVENT, TargetType.Event, ctx.ifExists() == null, ctx.fullId());
        return null;
    }

    @Override
    public Void visitAlterTable(AlterTableContext ctx) {
        add(SplitQueryType.ALTER_TABLE, TargetType.Table, ctx.tableName());
        return visitChildren(ctx);
    }

    @Override
    public Void visitDropView(DropViewContext ctx) {
        for (FullIdContext fullId : ctx.fullId()) {
            add(SplitQueryType.DROP_VIEW, TargetType.View, ctx.ifExists() == null, fullId);
        }
        return null;
    }

    @Override
    public Void visitCreateIndex(CreateIndexContext ctx) {
        add(SplitQueryType.ADD_INDEX, TargetType.Index, false, ctx.indexName());
        add(SplitQueryType.ALTER_TABLE, TargetType.Table, ctx.tableName());
        return null;
    }

    @Override
    public Void visitDropIndex(DropIndexContext ctx) {
        add(SplitQueryType.DROP_INDEX, TargetType.Index, ctx.indexName());
        add(SplitQueryType.ALTER_TABLE, TargetType.Table, ctx.tableName());
        return null;
    }

    @Override
    public Void visitCreateDatabase(CreateDatabaseContext ctx) {
        add(SplitQueryType.CREATE_SCHEMA, TargetType.Schema, false, ctx.databaseName());
        return null;
    }

    @Override
    public Void visitDropDatabase(DropDatabaseContext ctx) {
        add(SplitQueryType.DROP_SCHEMA, TargetType.Schema, ctx.ifExists() == null, ctx.databaseName());
        return null;
    }

    @Override
    public Void visitDropTable(DropTableContext ctx) {
        for (TableNameContext tableName : ctx.tables().tableName()) {
            add(SplitQueryType.DROP_TABLE, TargetType.Table, ctx.ifExists() == null, tableName);
        }
        return null;
    }

    @Override
    public Void visitRenameTableClause(RenameTableClauseContext ctx) {
        add(SplitQueryType.RENAME_TABLE, TargetType.Table, ctx.tableName(0));
        add(SplitQueryType.RENAME_TABLE, TargetType.Table, false, ctx.tableName(1));
        return null;
    }

    @Override
    public Void visitTruncateTable(TruncateTableContext ctx) {
        add(SplitQueryType.TRUNCATE_TABLE, TargetType.Table, ctx.tableName());
        return null;
    }

    @Override
    public Void visitCallStatement(CallStatementContext ctx) {
        add(SplitQueryType.CALL_PROG_OBJ, TargetType.Procedure, ctx.procName());
        return null;
    }

    @Override
    public Void visitInsertStatement(InsertStatementContext ctx) {
        SplitQueryType type = ctx.duplicatedFirst == null ? SplitQueryType.INSERT : SplitQueryType.MERGE;
        add(type, TargetType.Table, ctx.tableName());
        if (ctx.insertStatementValue() != null) {
            visit(ctx.insertStatementValue());
        }
        return null;
    }

    @Override
    public Void visitReplaceStatement(ReplaceStatementContext ctx) {
        add(SplitQueryType.MERGE, TargetType.Table, ctx.tableName());
        if (ctx.replaceStatementValue() != null) {
            visit(ctx.replaceStatementValue());
        }
        return null;
    }

    @Override
    public Void visitLoadDataStatement(LoadDataStatementContext ctx) {
        add(SplitQueryType.DATA_IMPORT, TargetType.Table, ctx.tableName());
        addFile(SplitQueryType.DATA_IMPORT, true, ctx.loadSource().textLiteralToken());
        return visitChildren(ctx);
    }

    @Override
    public Void visitLoadXmlStatement(LoadXmlStatementContext ctx) {
        add(SplitQueryType.DATA_IMPORT, TargetType.Table, ctx.tableName());
        addFile(SplitQueryType.DATA_IMPORT, true, ctx.loadSource().textLiteralToken());
        return visitChildren(ctx);
    }

    @Override
    public Void visitSelectIntoDumpFile(SelectIntoDumpFileContext ctx) {
        addFile(SplitQueryType.DATA_EXPORT, false, ctx.textLiteralToken());
        return visitChildren(ctx);
    }

    @Override
    public Void visitSelectIntoTextFile(SelectIntoTextFileContext ctx) {
        addFile(SplitQueryType.DATA_EXPORT, false, ctx.filename);
        return visitChildren(ctx);
    }

    @Override
    public Void visitSelectIntoRemoteFile(SelectIntoRemoteFileContext ctx) {
        addFile(SplitQueryType.DATA_EXPORT, false, ctx.textLiteralToken());
        return visitChildren(ctx);
    }

    @Override
    public Void visitSelectIntoRemoteParameters(SelectIntoRemoteParametersContext ctx) {
        addFile(SplitQueryType.DATA_EXPORT, false, ctx.textLiteralToken());
        return visitChildren(ctx);
    }

    @Override
    public Void visitAssignmentField(AssignmentFieldContext ctx) {
        if (ctx.LOCAL_ID() != null) {
            String variable = ctx.LOCAL_ID().getText();
            while (variable.startsWith("@")) {
                variable = variable.substring(1);
            }
            addInstanceResource(SplitQueryType.SESSION_VARIABLE_RW, TargetType.ConfigKey, true, ctx, unquote(variable));
        }
        return null;
    }

    @Override
    public Void visitMysqlVariableExpressionAtom(MysqlVariableExpressionAtomContext ctx) {
        addConfigKey(ctx.mysqlVariable());
        return null;
    }

    @Override
    public Void visitSetVariable(SetVariableContext ctx) {
        for (SetVariableAssignmentContext assignment : ctx.setVariableAssignment()) {
            VariableClauseContext variable = assignment.variableClause();
            if (isRoutineLocalVariable(variable)) {
                continue;
            }
            if (variable == null) {
                String assignmentText = name(assignment);
                String key = assignmentText != null && assignmentText.toUpperCase(Locale.ROOT)
                    .startsWith("CHARACTER SET") ? "CHARACTER SET" : assignmentText == null ? null : assignmentText.split("\\s+", 2)[0];
                addInstanceResource(SplitQueryType.SESSION_SETTING_WRITE, TargetType.ConfigKey, true, assignment, key);
                continue;
            }
            String upper = name(variable).toUpperCase();
            SplitQueryType permission;
            if (upper.contains("GTID_") || upper.contains("SLAVE_") || upper.contains("REPLICA_")) {
                permission = SplitQueryType.ALTER_REPLICATION;
            } else if (!variable.LOCAL_ID().isEmpty()) {
                permission = SplitQueryType.SESSION_VARIABLE_RW;
            } else if (variable.GLOBAL() != null || variable.persistScope() != null || upper.startsWith("@@GLOBAL.") || upper.startsWith("@@PERSIST.")) {
                permission = SplitQueryType.SYSTEM_SETTING_WRITE;
            } else {
                permission = SplitQueryType.SESSION_SETTING_WRITE;
            }
            String key = variableName(variable);
            if (key.isEmpty()) {
                String assignmentText = name(assignment);
                int equals = assignmentText == null ? -1 : assignmentText.indexOf('=');
                key = normalizedVariableName(equals < 0 ? assignmentText : assignmentText.substring(0, equals));
            }
            if (key.isEmpty()) {
                addUnnamedResource(permission, TargetType.ConfigKey, true, variable);
            } else {
                addInstanceResource(permission, TargetType.ConfigKey, true, variable, key);
            }
            visit(assignment);
        }
        return null;
    }

    private boolean isRoutineLocalVariable(VariableClauseContext variable) {
        if (variable == null || !variable.LOCAL_ID().isEmpty() || variable.GLOBAL_ID() != null || variable.GLOBAL() != null || variable.SESSION() != null
            || variable.LOCAL() != null || variable.persistScope() != null) {
            return false;
        }

        ParserRuleContext owner = variable;
        while (owner != null && !(owner instanceof CreateProcedureContext) && !(owner instanceof CreateFunctionContext) && !(owner instanceof CreateTriggerContext)
               && !(owner instanceof CreateEventContext) && !(owner instanceof AlterEventContext)) {
            owner = owner.getParent();
        }
        if (owner == null) {
            return false;
        }

        Set<String> localNames = new LinkedHashSet<>();
        descendants(owner, ProcedureParameterContext.class).forEach(parameter -> localNames.add(normalizeIdentifier(name(parameter.uid()))));
        descendants(owner, FunctionParameterContext.class).forEach(parameter -> localNames.add(normalizeIdentifier(name(parameter.uid()))));
        descendants(owner, DeclareVariableContext.class).forEach(declaration -> declaration.uidList().uid().forEach(uid -> localNames.add(normalizeIdentifier(name(uid)))));
        return localNames.contains(normalizeIdentifier(name(variable)));
    }

    private static String normalizeIdentifier(String value) {
        if (value == null) {
            return "";
        }
        return unquoteIdentifier(value.trim()).toLowerCase(Locale.ROOT);
    }

    private static String removeLeading(String value, char character) {
        int offset = 0;
        while (offset < value.length() && value.charAt(offset) == character) {
            offset++;
        }
        return value.substring(offset);
    }

    private static String removeWhitespace(String value) {
        StringBuilder normalized = new StringBuilder(value.length());
        for (int i = 0; i < value.length(); i++) {
            char current = value.charAt(i);
            if (!Character.isWhitespace(current)) {
                normalized.append(current);
            }
        }
        return normalized.toString();
    }

    private static String stripVariableScope(String value) {
        String[] scopes = { "PERSIST_ONLY", "PERSIST", "GLOBAL", "SESSION", "LOCAL" };
        for (String scope : scopes) {
            if (!value.regionMatches(true, 0, scope, 0, scope.length())) {
                continue;
            }
            int offset = scope.length();
            if (offset < value.length() && (value.charAt(offset) == '.' || value.charAt(offset) == '=')) {
                offset++;
            }
            return value.substring(offset);
        }
        return value;
    }

    @Override
    public Void visitSetCharset(SetCharsetContext ctx) {
        addUnnamedResource(SplitQueryType.SESSION_SETTING_WRITE, TargetType.ConfigKey, true, ctx);
        return null;
    }

    @Override
    public Void visitSetNames(SetNamesContext ctx) {
        addUnnamedResource(SplitQueryType.SESSION_SETTING_WRITE, TargetType.ConfigKey, true, ctx);
        return null;
    }

    @Override
    public Void visitSetAutocommit(SetAutocommitContext ctx) {
        Token token = ctx.setAutocommitStatement().AUTOCOMMIT().getSymbol();
        addInstanceResource(SplitQueryType.SESSION_SETTING_WRITE, TargetType.ConfigKey, true, token, token.getText());
        return null;
    }

    @Override
    public Void visitResetPersist(ResetPersistContext ctx) {
        if (ctx.resetPersistVariable() == null) {
            addUnnamedResource(SplitQueryType.SYSTEM_SETTING_WRITE, TargetType.ConfigKey, true, ctx);
        } else {
            addInstanceResource(SplitQueryType.SYSTEM_SETTING_WRITE, TargetType.ConfigKey, ctx.EXISTS() == null, ctx.resetPersistVariable(), name(ctx.resetPersistVariable()));
        }
        return null;
    }

    @Override
    public Void visitPrepareStatement(PrepareStatementContext ctx) {
        addInstanceResource(SplitQueryType.UNSAFE, TargetType.PrepareStatement, false, ctx.uid(), name(ctx.uid()));
        return visitChildren(ctx);
    }

    @Override
    public Void visitExecuteStatement(ExecuteStatementContext ctx) {
        addInstanceResource(SplitQueryType.UNSAFE, TargetType.PrepareStatement, true, ctx.uid(), name(ctx.uid()));
        return visitChildren(ctx);
    }

    @Override
    public Void visitDeallocatePrepare(DeallocatePrepareContext ctx) {
        addInstanceResource(SplitQueryType.UNSAFE, TargetType.PrepareStatement, true, ctx.uid(), name(ctx.uid()));
        return null;
    }

    @Override
    public Void visitSingleUpdateStatement(SingleUpdateStatementContext ctx) {
        if (ctx.withClause() != null) {
            visit(ctx.withClause());
        }
        add(SplitQueryType.UPDATE, TargetType.Table, ctx.tableName());
        if (ctx.whereClause() != null) {
            visit(ctx.whereClause());
        }
        return null;
    }

    @Override
    public Void visitMultipleUpdateStatement(MultipleUpdateStatementContext ctx) {
        if (ctx.tableSources() != null) {
            visit(ctx.tableSources());
        }
        return null;
    }

    @Override
    public Void visitSingleDeleteStatement(SingleDeleteStatementContext ctx) {
        add(SplitQueryType.DELETE, TargetType.Table, ctx.tableName());
        if (ctx.whereClause() != null) {
            visit(ctx.whereClause());
        }
        return null;
    }

    @Override
    public Void visitMultipleDeleteStatement(MultipleDeleteStatementContext ctx) {
        for (TableNameContext tableName : ctx.tableName()) {
            add(SplitQueryType.DELETE, TargetType.Table, tableName);
        }
        if (ctx.tableSources() != null) {
            visit(ctx.tableSources());
        }
        return null;
    }

    private Void addAlterUsers(ParserRuleContext ctx) {
        List<UserNameContext> users = descendants(ctx, UserNameContext.class);
        if (users.isEmpty()) {
            addUnnamedResource(SplitQueryType.ALTER_USER, TargetType.User, true, ctx);
        } else {
            users.forEach(user -> addAccount(SplitQueryType.ALTER_USER, TargetType.User, true, user));
        }
        return null;
    }

    private void addAdminTables(TablesContext tables) {
        addTables(tables, SplitQueryType.ADMIN_TABLE);
    }

    private void addTables(TablesContext tables, SplitQueryType permission) {
        if (tables != null) {
            tables.tableName().forEach(table -> add(permission, TargetType.Table, table));
        }
    }

    private void addMetadataTable(TableNameContext table, UidContext explicitSchema) {
        if (table == null) {
            return;
        }
        if (explicitSchema == null || table.fullId().uid().size() > 1) {
            add(SplitQueryType.METADATA, TargetType.Table, table);
            return;
        }
        List<String> parts = new ArrayList<>();
        addPart(parts, name(explicitSchema));
        addPart(parts, name(table));
        add(SplitQueryType.METADATA, TargetType.Table, true, table, parts);
    }

    protected final void addPrivilegeTarget(SplitQueryType sqlType, PrivilegeObjectTypeContext objectType, PrivilegeLevelContext level) {
        if (level == null) {
            return;
        }
        TargetType targetType = TargetType.Table;
        if (objectType != null) {
            String object = name(objectType).toUpperCase();
            if ("FUNCTION".equals(object)) {
                targetType = TargetType.Function;
            } else if ("PROCEDURE".equals(object)) {
                targetType = TargetType.Procedure;
            } else if ("LIBRARY".equals(object)) {
                targetType = TargetType.Library;
            }
        }
        if (level instanceof GlobalPrivLevelContext) {
            addUnnamedResource(sqlType, TargetType.Instance, true, level);
        } else if (level instanceof CurrentSchemaPriviLevelContext) {
            List<String> nodes = new ArrayList<>();
            addPart(nodes, level(UmiTypes.Catalog));
            addPart(nodes, level(UmiTypes.Schema));
            addWithNodes(sqlType, TargetType.Schema, true, level, nodes, false);
        } else if (level instanceof DefiniteSchemaPrivLevelContext schema) {
            add(sqlType, TargetType.Schema, true, schema.uid());
        } else {
            List<String> parts = new ArrayList<>();
            descendants(level, UidContext.class).forEach(uid -> addPart(parts, name(uid)));
            descendants(level, DottedIdContext.class).forEach(id -> addPart(parts, removeLeading(name(id), '.')));
            add(sqlType, targetType, true, level, parts);
        }
    }

    protected final void addDescendantAccounts(SplitQueryType sqlType, TargetType targetType, boolean require, ParseTree tree) {
        descendants(tree, UserNameContext.class).forEach(user -> addAccount(sqlType, targetType, require, user));
    }

    protected final void addAccount(SplitQueryType sqlType, TargetType targetType, boolean require, ParserRuleContext ctx) {
        if (ctx == null) {
            return;
        }
        String account;
        if (ctx instanceof UserNameContext user) {
            account = unquote(name(user.user));
            if (user.host != null) {
                String host = user.host.getText();
                account += "@" + unquote(host.startsWith("@") ? host.substring(1) : host);
            }
        } else if (ctx instanceof RoleNameContext role && role.userName() != null) {
            UserNameContext user = role.userName();
            account = unquote(name(user.user));
            if (user.host != null) {
                String host = user.host.getText();
                account += "@" + unquote(host.startsWith("@") ? host.substring(1) : host);
            }
        } else {
            account = unquote(name(ctx));
        }
        addInstanceResource(sqlType, targetType, require, ctx, account);
    }

    private void addUnnamedResource(SplitQueryType sqlType, TargetType targetType, boolean require, ParserRuleContext ctx) {
        if (ctx == null) {
            return;
        }
        references.add(new TiDBObjectReference(sqlType, targetType, require, line(ctx), column(ctx), endLine(ctx), endColumn(ctx), List.of()));
    }

    private static <T extends ParseTree> List<T> descendants(ParseTree tree, Class<T> type) {
        List<T> result = new ArrayList<>();
        collectDescendants(tree, type, result);
        return result;
    }

    private static <T extends ParseTree> void collectDescendants(ParseTree tree, Class<T> type, List<T> result) {
        if (tree == null) {
            return;
        }
        if (type.isInstance(tree)) {
            result.add(type.cast(tree));
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            collectDescendants(tree.getChild(i), type, result);
        }
    }

    protected final void addConfigKey(MysqlVariableContext ctx) {
        String variable = name(ctx);
        while (variable.startsWith("@")) {
            variable = variable.substring(1);
        }
        int scopeSeparator = variable.indexOf('.');
        if (scopeSeparator >= 0 && scopeSeparator + 1 < variable.length()) {
            variable = variable.substring(scopeSeparator + 1);
        }
        addInstanceResource(SplitQueryType.SESSION_VARIABLE_RW, TargetType.ConfigKey, true, ctx, unquote(variable));
    }

    private void addConfigKey(SplitQueryType permission, VariableClauseContext ctx) {
        addInstanceResource(permission, TargetType.ConfigKey, true, ctx, variableName(ctx));
    }

    private String variableName(VariableClauseContext ctx) {
        if (!ctx.LOCAL_ID().isEmpty()) {
            return unquote(removeWhitespace(removeLeading(ctx.LOCAL_ID(0).getText(), '@')));
        }
        if (ctx.GLOBAL_ID() != null) {
            return unquote(stripVariableScope(removeWhitespace(removeLeading(ctx.GLOBAL_ID().getText(), '@'))));
        }
        if (ctx.uid() == null) {
            return normalizedVariableName(name(ctx));
        }
        int tokenType = ctx.uid().getStart().getType();
        if (tokenType == TiDBParser.GLOBAL || tokenType == TiDBParser.LOCAL || tokenType == TiDBParser.SESSION
            || tokenType == TiDBParser.ID && StringUtils.equalsIgnoreCase(ctx.uid().getText(), "PERSIST_ONLY")) {
            return normalizedVariableName(name(ctx));
        }
        return name(ctx.uid());
    }

    private static String normalizedVariableName(String rawName) {
        if (rawName == null) {
            return "";
        }
        String variable = removeWhitespace(rawName);
        while (variable.startsWith("@")) {
            variable = variable.substring(1);
        }
        variable = stripVariableScope(variable);
        return unquote(variable);
    }

    private void addFile(SplitQueryType sqlType, boolean require, ParserRuleContext ctx) {
        if (ctx == null) {
            return;
        }
        String file = unquote(name(ctx));
        List<String> nodes = new ArrayList<>();
        addPart(nodes, normalizePath(file));
        addWithNodes(sqlType, TargetType.File, require, ctx, nodes);
    }

    private void addQuotedResourceName(SplitQueryType sqlType, TargetType targetType, boolean require, ParserRuleContext ctx) {
        if (ctx == null) {
            return;
        }
        List<String> parts = new ArrayList<>();
        addPart(parts, unquote(name(ctx)));
        add(sqlType, targetType, require, ctx, parts);
    }

    private String normalizePath(String path) {
        if (StringUtils.isBlank(path)) {
            return path;
        }
        List<String> nodes = new ArrayList<>();
        int start = 0;
        for (int i = 0; i <= path.length(); i++) {
            if (i == path.length() || path.charAt(i) == '/') {
                String node = path.substring(start, i);
                if (StringUtils.isNotBlank(node)) {
                    nodes.add(node);
                }
                start = i + 1;
            }
        }
        return String.join("/", nodes);
    }

    protected final void addInstanceResource(SplitQueryType sqlType, TargetType targetType, boolean require, ParserRuleContext ctx, String name) {
        List<String> nodes = new ArrayList<>();
        addPart(nodes, name);
        addWithNodes(sqlType, targetType, require, ctx, nodes);
    }

    protected final void addInstanceResource(SplitQueryType sqlType, TargetType targetType, boolean require, Token token, String name) {
        List<String> nodes = new ArrayList<>();
        addPart(nodes, name);
        addWithNodes(sqlType, targetType, require, token, nodes);
    }

    protected final void addInstanceBehaviorResource(SplitQueryType sqlType, TargetType targetType, boolean require, Token token, String name, BehaviorAction action) {
        List<String> nodes = new ArrayList<>();
        addPart(nodes, name);
        if (token == null || nodes.isEmpty()) {
            return;
        }
        references.add(new TiDBObjectReference(sqlType, targetType, require, line(token), column(token), endLine(token), endColumn(token), nodes, action));
    }

    protected final void addBehaviorResource(SplitQueryType sqlType, TargetType targetType, boolean require, TableNameContext ctx, BehaviorAction action) {
        if (ctx == null) {
            return;
        }
        List<String> parts = ctx.delphiName == null ? qualifiedIdentifierParts(ctx) : List.of(name(ctx.delphiName));
        List<String> nodes = resolveNodes(targetType, parts);
        if (nodes.isEmpty()) {
            return;
        }
        references.add(new TiDBObjectReference(sqlType, targetType, require, line(ctx), column(ctx), endLine(ctx), endColumn(ctx), nodes, action));
    }

    protected final void addQualifiedResource(SplitQueryType sqlType, TargetType targetType, boolean require, Token start, Token stop, List<String> parts) {
        if (start == null || stop == null || parts.isEmpty()) {
            return;
        }
        List<String> nodes = resolveNodes(targetType, parts);
        references.add(new TiDBObjectReference(sqlType, targetType, require, line(start), column(start), endLine(stop), endColumn(stop), nodes));
    }

    private void addWithNodes(SplitQueryType sqlType, TargetType targetType, boolean require, ParserRuleContext ctx, List<String> nodes) {
        addWithNodes(sqlType, targetType, require, ctx, nodes, true);
    }

    private void addWithNodes(SplitQueryType sqlType, TargetType targetType, boolean require, ParserRuleContext ctx, List<String> nodes, boolean explicitName) {
        if (ctx == null || nodes.isEmpty()) {
            return;
        }
        references.add(new TiDBObjectReference(sqlType, targetType, require, line(ctx), column(ctx), endLine(ctx), endColumn(ctx), nodes, null, explicitName));
    }

    private void addWithNodes(SplitQueryType sqlType, TargetType targetType, boolean require, Token token, List<String> nodes) {
        if (token == null || nodes.isEmpty()) {
            return;
        }
        references.add(new TiDBObjectReference(sqlType, targetType, require, line(token), column(token), endLine(token), endColumn(token), nodes));
    }

    private static String unquote(String value) {
        if (value == null || value.length() < 2) {
            return value;
        }
        char first = value.charAt(0);
        char last = value.charAt(value.length() - 1);
        if ((first == '\'' || first == '"' || first == '`') && last == first) {
            String quote = String.valueOf(first);
            return value.substring(1, value.length() - 1).replace(quote + quote, quote);
        }
        return value;
    }

    protected static String unquoteIdentifier(String value) {
        if (value == null || value.length() < 2) {
            return value;
        }
        char quote = value.charAt(0);
        if ((quote == '`' || quote == '"') && value.charAt(value.length() - 1) == quote) {
            String delimiter = String.valueOf(quote);
            return value.substring(1, value.length() - 1).replace(delimiter + delimiter, delimiter);
        }
        return value;
    }

    protected final void add(SplitQueryType sqlType, TargetType targetType, TableNameContext ctx) {
        add(sqlType, targetType, true, ctx);
    }

    protected final void addSessionVariable(Token token) {
        if (token == null) {
            return;
        }
        String variable = token.getText();
        while (variable.startsWith("@")) {
            variable = variable.substring(1);
        }
        List<String> nodes = new ArrayList<>();
        addPart(nodes, unquote(variable));
        addWithNodes(SplitQueryType.SESSION_VARIABLE_RW, TargetType.ConfigKey, true, token, nodes);
    }

    protected final void addConfigKey(SplitQueryType sqlType, Token token, String variable) {
        if (token == null || StringUtils.isBlank(variable)) {
            return;
        }
        String normalized = stripVariableScope(removeLeading(variable, '@'));
        List<String> nodes = new ArrayList<>();
        addPart(nodes, unquote(normalized));
        addWithNodes(sqlType, TargetType.ConfigKey, true, token, nodes);
    }

    protected final void add(SplitQueryType sqlType, TargetType targetType, boolean require, TableNameContext ctx) {
        if (ctx == null) {
            return;
        }
        if (ctx.delphiName != null) {
            List<String> parts = new ArrayList<>();
            addPart(parts, name(ctx.delphiName));
            add(sqlType, targetType, require, ctx, parts);
        } else {
            List<String> parts = qualifiedIdentifierParts(ctx);
            add(sqlType, targetType, require, ctx, parts);
        }
    }

    private void add(SplitQueryType sqlType, TargetType targetType, ProcNameContext ctx) {
        add(sqlType, targetType, true, ctx);
    }

    private void add(SplitQueryType sqlType, TargetType targetType, boolean require, ProcNameContext ctx) {
        if (ctx != null) {
            add(sqlType, targetType, require, ctx.fullId());
        }
    }

    private void add(SplitQueryType sqlType, TargetType targetType, FullIdContext ctx) {
        add(sqlType, targetType, true, ctx);
    }

    protected final void add(SplitQueryType sqlType, TargetType targetType, boolean require, FullIdContext ctx) {
        if (ctx == null) {
            return;
        }
        List<String> parts = qualifiedIdentifierParts(ctx);
        add(sqlType, targetType, require, ctx, parts);
    }

    private List<String> qualifiedIdentifierParts(ParserRuleContext ctx) {
        String value = parser.getTokenStream().getText(ctx.getStart(), ctx.getStop());
        List<String> parts = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        char quote = 0;
        for (int i = 0; i < value.length(); i++) {
            char character = value.charAt(i);
            if (quote != 0) {
                current.append(character);
                if (character == quote) {
                    if (i + 1 < value.length() && value.charAt(i + 1) == quote) {
                        current.append(value.charAt(++i));
                    } else {
                        quote = 0;
                    }
                }
                continue;
            }
            if (character == '`' || character == '"') {
                quote = character;
                current.append(character);
            } else if (character == '.') {
                addPart(parts, unquoteIdentifier(current.toString().strip()));
                current.setLength(0);
            } else {
                current.append(character);
            }
        }
        addPart(parts, unquoteIdentifier(current.toString().strip()));
        return parts;
    }

    private void add(SplitQueryType sqlType, TargetType targetType, ParserRuleContext ctx) {
        add(sqlType, targetType, true, ctx);
    }

    protected final void add(SplitQueryType sqlType, TargetType targetType, boolean require, ParserRuleContext ctx) {
        if (ctx == null) {
            return;
        }
        List<String> parts = new ArrayList<>();
        addPart(parts, name(ctx));
        add(sqlType, targetType, require, ctx, parts);
    }

    protected final void add(SplitQueryType sqlType, TargetType targetType, boolean require, Token token) {
        if (token == null || StringUtils.isBlank(token.getText())) {
            return;
        }
        List<String> parts = new ArrayList<>();
        addPart(parts, unquoteIdentifier(token.getText()));
        List<String> nodes = resolveNodes(targetType, parts);
        addWithNodes(sqlType, targetType, require, token, nodes);
    }

    private void add(SplitQueryType sqlType, TargetType targetType, boolean require, ParserRuleContext ctx, List<String> parts) {
        if (parts.isEmpty()) {
            return;
        }
        List<String> nodes = resolveNodes(targetType, parts);
        addWithNodes(sqlType, targetType, require, ctx, nodes);
    }

    private List<String> resolveNodes(TargetType type, List<String> parts) {
        List<String> nodes = new ArrayList<>();
        String catalog = level(UmiTypes.Catalog);
        String schema = level(UmiTypes.Schema);

        if (type == TargetType.Catalog) {
            addPart(nodes, parts.get(parts.size() - 1));
            return nodes;
        }
        if (type == TargetType.Schema) {
            addPart(nodes, catalog);
            addPart(nodes, parts.get(parts.size() - 1));
            return nodes;
        }

        if (parts.size() >= 3) {
            addPart(nodes, parts.get(parts.size() - 3));
            addPart(nodes, parts.get(parts.size() - 2));
            addPart(nodes, parts.get(parts.size() - 1));
        } else if (parts.size() == 2) {
            addPart(nodes, catalog);
            addPart(nodes, parts.get(0));
            addPart(nodes, parts.get(1));
        } else {
            addPart(nodes, catalog);
            addPart(nodes, schema);
            addPart(nodes, parts.get(0));
        }
        return nodes;
    }

    private String level(UmiTypes type) {
        if (levelsParam == null || levelsParam.get(type) == null) {
            return null;
        }
        return StringUtils.toString(levelsParam.get(type));
    }

    private void addPart(List<String> parts, String part) {
        if (StringUtils.isNotBlank(part)) {
            parts.add(part);
        }
    }

    private String name(ParserRuleContext ctx) {
        if (ctx == null) {
            return null;
        }
        String text = parser.getTokenStream().getText(ctx.getStart(), ctx.getStop());
        if (StringUtils.isBlank(text)) {
            return text;
        }
        String value = text.trim();
        return unquoteIdentifier(value);
    }

    private int line(ParserRuleContext ctx) {
        return line(ctx.getStart());
    }

    private int line(Token token) {
        return token == null ? baseLine : Math.max(1, baseLine) + token.getLine() - 1;
    }

    private int column(ParserRuleContext ctx) {
        return column(ctx.getStart());
    }

    private int column(Token token) {
        if (token == null) {
            return baseColumn;
        }
        return token.getLine() == 1 ? Math.max(0, baseColumn) + token.getCharPositionInLine() : token.getCharPositionInLine();
    }

    private int endLine(ParserRuleContext ctx) {
        return endLine(ctx.getStop());
    }

    private int endLine(Token token) {
        if (token == null) {
            return baseLine;
        }
        String text = token.getText();
        int lineCount = 0;
        for (int i = 0; text != null && i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch == '\n') {
                lineCount++;
            } else if (ch == '\r' && (i + 1 == text.length() || text.charAt(i + 1) != '\n')) {
                lineCount++;
            }
        }
        return Math.max(1, baseLine) + token.getLine() - 1 + lineCount;
    }

    private int endColumn(ParserRuleContext ctx) {
        return endColumn(ctx.getStop());
    }

    private int endColumn(Token token) {
        if (token == null) {
            return baseColumn;
        }
        String text = token.getText();
        if (text == null) {
            return token.getCharPositionInLine();
        }
        int lastLineBreak = Math.max(text.lastIndexOf('\n'), text.lastIndexOf('\r'));
        if (lastLineBreak >= 0) {
            return text.length() - lastLineBreak - 1;
        }
        int column = token.getCharPositionInLine() + text.length();
        return token.getLine() == 1 ? Math.max(0, baseColumn) + column : column;
    }
}
