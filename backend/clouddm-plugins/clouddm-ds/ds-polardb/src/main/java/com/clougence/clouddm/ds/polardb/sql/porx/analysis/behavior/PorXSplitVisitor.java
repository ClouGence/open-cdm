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
package com.clougence.clouddm.ds.polardb.sql.porx.analysis.behavior;

import static com.clougence.clouddm.ds.polardb.sql.porx.parser.antlr.PolardbXParser.*;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;

import com.clougence.clouddm.ds.polardb.sql.porx.parser.antlr.PolardbXParserBaseVisitor;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementType;

public class PorXSplitVisitor extends PolardbXParserBaseVisitor<StatementType> {
    public static final AbstractParseTreeVisitor<StatementType> INSTANCE = new PorXSplitVisitor();

    public PorXSplitVisitor(){
    }

    @Override
    public StatementType visitCreateDatabase(CreateDatabaseContext ctx) {
        return StatementType.CREATE_SCHEMA;
    }

    @Override
    public StatementType visitCheckTable(CheckTableContext ctx) {
        return StatementType.ADMIN_TABLE;
    }

    @Override
    public StatementType visitRepairTable(RepairTableContext ctx) {
        return StatementType.ADMIN_TABLE;
    }

    @Override
    public StatementType visitCreateUdfFunction(CreateUdfFunctionContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
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
    public StatementType visitDropTablespace(DropTablespaceContext ctx) {
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
    public StatementType visitAlterLogfileGroup(AlterLogfileGroupContext ctx) {
        return StatementType.ALTER_LOG;
    }

    @Override
    public StatementType visitCreateTablespaceNdb(CreateTablespaceNdbContext ctx) {
        return StatementType.CREATE_TABLESPACE;
    }

    @Override
    public StatementType visitAnalyzeTable(AnalyzeTableContext ctx) {
        return StatementType.ADMIN_TABLE;
    }

    @Override
    public StatementType visitWithSelectStatement(WithSelectStatementContext ctx) {
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
    public StatementType visitTransactionStatement(TransactionStatementContext ctx) {
        return StatementType.TRANSACTION;
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
    public StatementType visitQueryCreateTable(QueryCreateTableContext ctx) {
        return StatementType.CREATE_TABLE;
    }

    @Override
    public StatementType visitColumnCreateTable(ColumnCreateTableContext ctx) {
        return StatementType.CREATE_TABLE;
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
        return StatementType.CREATE_VIEW;
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
        return StatementType.PERFORMANCE;
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
    public StatementType visitSimpleSelect(SimpleSelectContext ctx) {
        return StatementType.SELECT;
    }

    @Override
    public StatementType visitParenthesisSelect(ParenthesisSelectContext ctx) {
        return StatementType.SELECT;
    }

    @Override
    public StatementType visitUnionSelect(UnionSelectContext ctx) {
        return StatementType.SELECT;
    }

    @Override
    public StatementType visitUnionParenthesisSelect(UnionParenthesisSelectContext ctx) {
        return StatementType.SELECT;
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
    public StatementType visitSimpleDescribeStatement(SimpleDescribeStatementContext ctx) {
        return "EXPLAIN".equalsIgnoreCase(ctx.command.getText()) ? StatementType.PERFORMANCE : StatementType.UNKNOWN;
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
    public StatementType visitShowCharset(ShowCharsetContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowLogEvents(ShowLogEventsContext ctx) {
        return StatementType.LOG_READ;
    }

    @Override
    public StatementType visitShowObjectFilter(ShowObjectFilterContext ctx) {
        String entity = ctx.showCommonEntity().getText();
        if (entity.equalsIgnoreCase("STATUS") || entity.equalsIgnoreCase("GLOBALSTATUS") || entity.equalsIgnoreCase("SESSIONSTATUS")) {
            return StatementType.PERFORMANCE;
        }
        if (entity.equalsIgnoreCase("VARIABLES") || entity.equalsIgnoreCase("SESSIONVARIABLES")) {
            return StatementType.SESSION_VARIABLE_RW;
        }
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowColumns(ShowColumnsContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowTables(ShowTablesContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowCreateDb(ShowCreateDbContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowCreateFullIdObject(ShowCreateFullIdObjectContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowCreateUser(ShowCreateUserContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowEngine(ShowEngineContext ctx) {
        return StatementType.PERFORMANCE;
    }

    @Override
    public StatementType visitShowEngines(ShowEnginesContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowStatus(ShowStatusContext ctx) {
        return StatementType.LOG_READ;
    }

    @Override
    public StatementType visitShowPlugins(ShowPluginsContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowPrivileges(ShowPrivilegesContext ctx) {
        return StatementType.UNKNOWN;
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
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowAuthros(ShowAuthrosContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowContributors(ShowContributorsContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowErrors(ShowErrorsContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowCountErrors(ShowCountErrorsContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowSchemaFilter(ShowSchemaFilterContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowRoutine(ShowRoutineContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowGrants(ShowGrantsContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowIndexes(ShowIndexesContext ctx) {
        return StatementType.UNKNOWN;
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
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitResetSlave(ResetSlaveContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitResetReplica(ResetReplicaContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitFlushStatement(FlushStatementContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitShowReplicaStatus(ShowReplicaStatusContext ctx) {
        return StatementType.UNKNOWN;
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
    public StatementType visitShowSlaveStatus(ShowSlaveStatusContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitSetVariable(SetVariableContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    public StatementType visitChildren(RuleNode node) {

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
