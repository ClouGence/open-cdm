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
package com.clougence.clouddm.ds.ads.sql.ads4my.parser;

import static com.clougence.clouddm.ds.ads.sql.ads4my.parser.antlr.AdsMyParser.*;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;

import com.clougence.clouddm.ds.ads.sql.ads4my.parser.antlr.AdsMyParserBaseVisitor;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;

public class AdsMySplitVisitor extends AdsMyParserBaseVisitor<SplitQueryType> {

    public static final AbstractParseTreeVisitor<SplitQueryType> INSTANCE = new AdsMySplitVisitor();

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
    public SplitQueryType visitCreateUdfFunction(CreateUdfFunctionContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
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
    public SplitQueryType visitDropTablespace(DropTablespaceContext ctx) {
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
    public SplitQueryType visitAlterLogfileGroup(AlterLogfileGroupContext ctx) {
        return SplitQueryType.ALTER_LOG;
    }

    @Override
    public SplitQueryType visitCreateTablespaceNdb(CreateTablespaceNdbContext ctx) {
        return SplitQueryType.CREATE_TABLESPACE;
    }

    @Override
    public SplitQueryType visitAnalyzeTable(AnalyzeTableContext ctx) {
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitWithSelectStatement(WithSelectStatementContext ctx) {
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
    public SplitQueryType visitTransactionStatement(TransactionStatementContext ctx) {
        return SplitQueryType.TRANSACTION;
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
        return SplitQueryType.CREATE_VIEW;
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
    public SplitQueryType visitSimpleSelect(SimpleSelectContext ctx) {
        return SplitQueryType.SELECT;
    }

    @Override
    public SplitQueryType visitParenthesisSelect(ParenthesisSelectContext ctx) {
        return SplitQueryType.SELECT;
    }

    @Override
    public SplitQueryType visitUnionSelect(UnionSelectContext ctx) {
        return SplitQueryType.SELECT;
    }

    @Override
    public SplitQueryType visitUnionParenthesisSelect(UnionParenthesisSelectContext ctx) {
        return SplitQueryType.SELECT;
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
    public SplitQueryType visitSimpleDescribeStatement(SimpleDescribeStatementContext ctx) {
        return "EXPLAIN".equalsIgnoreCase(ctx.command.getText()) ? SplitQueryType.PERFORMANCE : SplitQueryType.UNKNOWN;
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
    public SplitQueryType visitShowCharset(ShowCharsetContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowLogEvents(ShowLogEventsContext ctx) {
        return SplitQueryType.LOG_READ;
    }

    @Override
    public SplitQueryType visitShowObjectFilter(ShowObjectFilterContext ctx) {
        String entity = ctx.showCommonEntity().getText();
        if (entity.equalsIgnoreCase("STATUS") || entity.equalsIgnoreCase("GLOBALSTATUS") || entity.equalsIgnoreCase("SESSIONSTATUS")) {
            return SplitQueryType.PERFORMANCE;
        }
        if (entity.equalsIgnoreCase("VARIABLES") || entity.equalsIgnoreCase("SESSIONVARIABLES")) {
            return SplitQueryType.SESSION_VARIABLE_RW;
        }
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowColumns(ShowColumnsContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowTables(ShowTablesContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowCreateDb(ShowCreateDbContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowCreateFullIdObject(ShowCreateFullIdObjectContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowCreateUser(ShowCreateUserContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowEngine(ShowEngineContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitShowEngines(ShowEnginesContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowStatus(ShowStatusContext ctx) {
        return SplitQueryType.LOG_READ;
    }

    @Override
    public SplitQueryType visitShowPlugins(ShowPluginsContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowPrivileges(ShowPrivilegesContext ctx) {
        return SplitQueryType.UNKNOWN;
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
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowAuthros(ShowAuthrosContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowContributors(ShowContributorsContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowErrors(ShowErrorsContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowCountErrors(ShowCountErrorsContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowSchemaFilter(ShowSchemaFilterContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowRoutine(ShowRoutineContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowGrants(ShowGrantsContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowIndexes(ShowIndexesContext ctx) {
        return SplitQueryType.UNKNOWN;
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
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitResetSlave(ResetSlaveContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitResetReplica(ResetReplicaContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitFlushStatement(FlushStatementContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitShowReplicaStatus(ShowReplicaStatusContext ctx) {
        return SplitQueryType.UNKNOWN;
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
    public SplitQueryType visitShowSlaveStatus(ShowSlaveStatusContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitSetVariable(SetVariableContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitAdbExternalTable(AdbExternalTableContext ctx) {
        return SplitQueryType.CREATE_TABLE;
    }

    @Override
    public SplitQueryType visitLoadDataStatement(LoadDataStatementContext ctx) {
        return SplitQueryType.DATA_IMPORT;
    }

    public SplitQueryType visitChildren(RuleNode node) {
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
