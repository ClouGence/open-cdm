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

import java.util.Locale;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;

import com.clougence.clouddm.ds.tidb.sql.parser.antlr.TiDBParser;
import com.clougence.clouddm.ds.tidb.sql.parser.antlr.TiDBParserBaseVisitor;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;

public class TiSplitVisitor extends TiDBParserBaseVisitor<SplitQueryType> {
    public static final AbstractParseTreeVisitor<SplitQueryType> INSTANCE = new TiSplitVisitor();

    public TiSplitVisitor(){
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
        if (ctx.lockTables() != null || ctx.unlockTables() != null) {
            return SplitQueryType.SESSION_LOCK;
        }
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
        if (ctx.alterSpecification().size() == 1 && ctx.alterSpecification(0) instanceof AlterByCompactContext) {
            return SplitQueryType.ADMIN_TABLE;
        }
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
        if (ctx.REPLACE() != null) {
            return SplitQueryType.ALTER_VIEW;
        }
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
        if (ctx.analyze != null && !(ctx.describeObjectClause() instanceof DescribeDigestContext)
            && !(ctx.describeObjectClause() instanceof DescribeConnectionContext)) {
            return ctx.describeObjectClause().accept(this);
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
        if ("EXPLAIN".equalsIgnoreCase(ctx.command.getText())) {
            return SplitQueryType.PERFORMANCE;
        }
        return SplitQueryType.METADATA;
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
        return SplitQueryType.METADATA;
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
    public SplitQueryType visitShowCreateUser(ShowCreateUserContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowEngine(ShowEngineContext ctx) {
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
    public SplitQueryType visitShowAuthros(ShowAuthrosContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowContributors(ShowContributorsContext ctx) {
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
        for (FlushOptionContext option : ctx.flushOption()) {
            SplitQueryType type = flushType(option);
            if (type != SplitQueryType.SYSTEM_SETTING_WRITE) {
                return type;
            }
        }
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    static SplitQueryType flushType(FlushOptionContext option) {
        if (option instanceof TableFlushOptionContext table && table.flushTableOption() != null && table.flushTableOption().EXPORT() != null) {
            return SplitQueryType.DATA_EXPORT;
        }
        if (option instanceof TableFlushOptionContext || option instanceof SimpleFlushOptionContext simple && (simple.TABLES() != null || simple.TABLE() != null)) {
            return SplitQueryType.ADMIN_TABLE;
        }
        if (option instanceof ChannelFlushOptionContext || option instanceof SimpleFlushOptionContext simple && simple.LOGS() != null) {
            return SplitQueryType.MAINTAIN_LOG;
        }
        if (option instanceof SimpleFlushOptionContext simple && (simple.STATUS() != null || simple.CLIENT_ERRORS_SUMMARY() != null || simple.HOSTS() != null
                                                                  || simple.OPTIMIZER_COSTS() != null || simple.QUERY() != null || simple.USER_RESOURCES() != null)) {
            return SplitQueryType.ADMIN_PERFORMANCE;
        }
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitShowReplicaStatus(ShowReplicaStatusContext ctx) {
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
    public SplitQueryType visitShowSlaveStatus(ShowSlaveStatusContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitSetVariable(SetVariableContext ctx) {
        return variableType(ctx.variableClause(0));
    }

    static SplitQueryType variableType(VariableClauseContext variable) {
        if (TiRoutineAnalysis.isRoutineVariable(variable)) {
            return SplitQueryType.PROGRAM_CONTROL;
        }
        if (variable.LOCAL_ID() != null || variable.getText().equals("@")) {
            return SplitQueryType.SESSION_VARIABLE_RW;
        }
        if (variable.GLOBAL() != null || variable.PERSIST() != null || variable.getText().toUpperCase(Locale.ROOT).startsWith("@@GLOBAL.")) {
            return SplitQueryType.SYSTEM_SETTING_WRITE;
        }
        return SplitQueryType.SESSION_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitCreateSequence(TiDBParser.CreateSequenceContext ctx) {
        return SplitQueryType.CREATE_SEQUENCE;
    }

    @Override
    public SplitQueryType visitDropSequence(TiDBParser.DropSequenceContext ctx) {
        return SplitQueryType.DROP_SEQUENCE;
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

    @Override
    public SplitQueryType visitCreateBinding(CreateBindingContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitDropBinding(DropBindingContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitSetBinding(SetBindingContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitShowBindings(ShowBindingsContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowBindingCache(ShowBindingCacheContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitCreatePlacementPolicy(CreatePlacementPolicyContext ctx) {
        if (ctx.REPLACE() != null) {
            return SplitQueryType.ALTER_POLICY;
        }
        return SplitQueryType.CREATE_POLICY;
    }

    @Override
    public SplitQueryType visitAlterPlacementPolicy(AlterPlacementPolicyContext ctx) {
        return SplitQueryType.ALTER_POLICY;
    }

    @Override
    public SplitQueryType visitDropPlacementPolicy(DropPlacementPolicyContext ctx) {
        return SplitQueryType.DROP_POLICY;
    }

    @Override
    public SplitQueryType visitShowCreatePlacementPolicy(ShowCreatePlacementPolicyContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitCreateResourceGroup(CreateResourceGroupContext ctx) {
        return SplitQueryType.CREATE_RESOURCE_GROUP;
    }

    @Override
    public SplitQueryType visitAlterResourceGroup(AlterResourceGroupContext ctx) {
        return SplitQueryType.ALTER_RESOURCE_GROUP;
    }

    @Override
    public SplitQueryType visitDropResourceGroup(DropResourceGroupContext ctx) {
        return SplitQueryType.DROP_RESOURCE_GROUP;
    }

    @Override
    public SplitQueryType visitShowCreateResourceGroup(ShowCreateResourceGroupContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitSetResourceGroup(SetResourceGroupContext ctx) {
        return SplitQueryType.ADMIN_RESOURCE_GROUP;
    }

    @Override
    public SplitQueryType visitFlashbackTable(FlashbackTableContext ctx) {
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitFlashbackDatabase(FlashbackDatabaseContext ctx) {
        return SplitQueryType.ADMIN;
    }

    @Override
    public SplitQueryType visitFlashbackCluster(FlashbackClusterContext ctx) {
        return SplitQueryType.ADMIN;
    }

    @Override
    public SplitQueryType visitIndexAdviseStatement(IndexAdviseStatementContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitPlanReplayerDump(PlanReplayerDumpContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitPlanReplayerLoad(PlanReplayerLoadContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitPlanReplayerCapture(PlanReplayerCaptureContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitAdminShowDdl(AdminShowDdlContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitAdminShowNextRowId(AdminShowNextRowIdContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitAdminShowSlow(AdminShowSlowContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitImportIntoStatement(ImportIntoStatementContext ctx) {
        if (ctx.filename == null) {
            return SplitQueryType.INSERT;
        }
        return SplitQueryType.DATA_IMPORT;
    }

    @Override
    public SplitQueryType visitTableValueSelect(TableValueSelectContext ctx) {
        return SplitQueryType.SELECT;
    }

    @Override
    public SplitQueryType visitSplitRegionStatement(SplitRegionStatementContext ctx) {
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitBatchStatement(BatchStatementContext ctx) {
        if (ctx.DRY() != null) {
            return SplitQueryType.PERFORMANCE;
        }
        return visitChildren(ctx);
    }

    @Override
    public SplitQueryType visitCalibrateResource(CalibrateResourceContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitAddQueryWatch(AddQueryWatchContext ctx) {
        return SplitQueryType.ADMIN_RESOURCE_GROUP;
    }

    @Override
    public SplitQueryType visitRemoveQueryWatch(RemoveQueryWatchContext ctx) {
        return SplitQueryType.ADMIN_RESOURCE_GROUP;
    }

    @Override
    public SplitQueryType visitAlterSequence(AlterSequenceContext ctx) {
        return SplitQueryType.ALTER_SEQUENCE;
    }

    @Override
    public SplitQueryType visitAdminRepairStatement(AdminRepairStatementContext ctx) {
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitShowPlacement(ShowPlacementContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowTableRegions(ShowTableRegionsContext ctx) {
        return SplitQueryType.METADATA;
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
    public SplitQueryType visitTraceStatement(TraceStatementContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitBackupData(BackupDataContext ctx) {
        return SplitQueryType.DATA_EXPORT;
    }

    @Override
    public SplitQueryType visitRestoreData(RestoreDataContext ctx) {
        return SplitQueryType.DATA_IMPORT;
    }

    @Override
    public SplitQueryType visitShowBackupJobs(ShowBackupJobsContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowBackupJob(ShowBackupJobContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitCancelBackupJob(CancelBackupJobContext ctx) {
        return SplitQueryType.ADMIN_JOB;
    }

    @Override
    public SplitQueryType visitSetConfig(SetConfigContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitShowConfig(ShowConfigContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowStatistics(ShowStatisticsContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitCreateStatistics(CreateStatisticsContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitDropStatistics(DropStatisticsContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitDropStats(DropStatsContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitLoadStats(LoadStatsContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitRecoverTable(RecoverTableContext ctx) {
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitAdminCheckTable(AdminCheckTableContext ctx) {
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitAdminReload(AdminReloadContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitLockTables(LockTablesContext ctx) {
        return SplitQueryType.SESSION_LOCK;
    }

    @Override
    public SplitQueryType visitUnlockTables(UnlockTablesContext ctx) {
        return SplitQueryType.SESSION_LOCK;
    }

    @Override
    public SplitQueryType visitAdminCleanupIndex(AdminCleanupIndexContext ctx) {
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitAdminFlushPlanCache(AdminFlushPlanCacheContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitShowImportJobs(ShowImportJobsContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitCancelImportJob(CancelImportJobContext ctx) {
        return SplitQueryType.ADMIN_JOB;
    }

    @Override
    public SplitQueryType visitAlterDatabaseReplica(AlterDatabaseReplicaContext ctx) {
        return SplitQueryType.ALTER_SCHEMA;
    }

    @Override
    public SplitQueryType visitRecommendIndex(RecommendIndexContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
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
    public SplitQueryType visitSetNames(SetNamesContext ctx) {
        return SplitQueryType.SESSION_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitSetCharset(SetCharsetContext ctx) {
        return SplitQueryType.SESSION_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitSetAutocommit(SetAutocommitContext ctx) {
        return SplitQueryType.TRANSACTION;
    }

    @Override
    public SplitQueryType visitCreateLegacyImport(CreateLegacyImportContext ctx) {
        return SplitQueryType.DATA_IMPORT;
    }

    @Override
    public SplitQueryType visitStopLegacyImport(StopLegacyImportContext ctx) {
        return SplitQueryType.ADMIN_JOB;
    }

    @Override
    public SplitQueryType visitResumeLegacyImport(ResumeLegacyImportContext ctx) {
        return SplitQueryType.ADMIN_JOB;
    }

    @Override
    public SplitQueryType visitDropLegacyImport(DropLegacyImportContext ctx) {
        return SplitQueryType.DROP_JOB;
    }

    @Override
    public SplitQueryType visitShowCreateLegacyImport(ShowCreateLegacyImportContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShutdownStatement(ShutdownStatementContext ctx) {
        return SplitQueryType.UNSAFE;
    }

    @Override
    public SplitQueryType visitAdminCleanupTableLock(AdminCleanupTableLockContext ctx) {
        return SplitQueryType.SESSION_LOCK;
    }

    @Override
    public SplitQueryType visitAdminCheckIndex(AdminCheckIndexContext ctx) {
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitAdminEvolveBindings(AdminEvolveBindingsContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitLockStatistics(LockStatisticsContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitFlushStatsDelta(FlushStatsDeltaContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitAlterRange(AlterRangeContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitShowNextRowId(ShowNextRowIdContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitAlterInstance(AlterInstanceContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitShowBuiltins(ShowBuiltinsContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitAdminManageBindings(AdminManageBindingsContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitAdminManageDdlJobs(AdminManageDdlJobsContext ctx) {
        return SplitQueryType.ADMIN_JOB;
    }

    @Override
    public SplitQueryType visitAdminRecoverIndex(AdminRecoverIndexContext ctx) {
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitAdminPlugins(AdminPluginsContext ctx) {
        return SplitQueryType.ALTER_LIBRARY;
    }

    @Override
    public SplitQueryType visitFlushPlugins(FlushPluginsContext ctx) {
        return SplitQueryType.ALTER_LIBRARY;
    }

    @Override
    public SplitQueryType visitRestartInstance(RestartInstanceContext ctx) {
        return SplitQueryType.UNSAFE;
    }

    @Override
    public SplitQueryType visitSetMixed(SetMixedContext ctx) {
        return visit(ctx.mixedSetItem(0));
    }

    @Override
    public SplitQueryType visitMixedSetItem(MixedSetItemContext ctx) {
        VariableClauseContext variable = ctx.variableClause();
        if (variable == null) {
            return SplitQueryType.SESSION_SETTING_WRITE;
        }
        if (TiRoutineAnalysis.isRoutineVariable(variable)) {
            return SplitQueryType.PROGRAM_CONTROL;
        }
        if (variable.LOCAL_ID() != null || variable.getText().equals("@")) {
            return SplitQueryType.SESSION_VARIABLE_RW;
        }
        if (variable.GLOBAL() != null || variable.PERSIST() != null || variable.getText().toUpperCase(Locale.ROOT).startsWith("@@GLOBAL.")) {
            return SplitQueryType.SYSTEM_SETTING_WRITE;
        }
        return SplitQueryType.SESSION_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitAlterCurrentUser(AlterCurrentUserContext ctx) {
        return SplitQueryType.ALTER_USER;
    }

    @Override
    public SplitQueryType visitShowCreateCurrentUser(ShowCreateCurrentUserContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitAdminResetTelemetry(AdminResetTelemetryContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitBinlogStatement(BinlogStatementContext ctx) {
        return SplitQueryType.ADMIN_REPLICATION;
    }

    @Override
    public SplitQueryType visitAlterLegacyImport(AlterLegacyImportContext ctx) {
        return SplitQueryType.ALTER_JOB;
    }

    @Override
    public SplitQueryType visitShowLegacyImport(ShowLegacyImportContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowLegacyImports(ShowLegacyImportsContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitPurgeLegacyImport(PurgeLegacyImportContext ctx) {
        return SplitQueryType.ADMIN_JOB;
    }

    @Override
    public SplitQueryType visitShowTelemetry(ShowTelemetryContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitShowBinlogNodes(ShowBinlogNodesContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitChangeBinlogNode(ChangeBinlogNodeContext ctx) {
        return SplitQueryType.ALTER_REPLICATION;
    }

    @Override
    public SplitQueryType visitAdminAlterDdlJob(AdminAlterDdlJobContext ctx) {
        return SplitQueryType.ADMIN_JOB;
    }

    @Override
    public SplitQueryType visitCaptureTraffic(CaptureTrafficContext ctx) {
        return SplitQueryType.DATA_EXPORT;
    }

    @Override
    public SplitQueryType visitReplayTraffic(ReplayTrafficContext ctx) {
        return SplitQueryType.UNSAFE;
    }

    @Override
    public SplitQueryType visitShowTrafficJobs(ShowTrafficJobsContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitCancelTrafficJobs(CancelTrafficJobsContext ctx) {
        return SplitQueryType.ADMIN_JOB;
    }

    @Override
    public SplitQueryType visitSetSessionStates(SetSessionStatesContext ctx) {
        return SplitQueryType.SESSION_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitShowSessionStates(ShowSessionStatesContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitManageLoadJob(ManageLoadJobContext ctx) {
        return SplitQueryType.ADMIN_JOB;
    }

    @Override
    public SplitQueryType visitDistributeTable(DistributeTableContext ctx) {
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitShowDistributionJobs(ShowDistributionJobsContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitCancelDistributionJob(CancelDistributionJobContext ctx) {
        return SplitQueryType.ADMIN_JOB;
    }

    @Override
    public SplitQueryType visitShowAffinity(ShowAffinityContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitSetBdrRole(SetBdrRoleContext ctx) {
        return SplitQueryType.ALTER_REPLICATION;
    }

    @Override
    public SplitQueryType visitUnsetBdrRole(UnsetBdrRoleContext ctx) {
        return SplitQueryType.ALTER_REPLICATION;
    }

    @Override
    public SplitQueryType visitShowBdrRole(ShowBdrRoleContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowPlan(ShowPlanContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitCreateWorkloadSnapshot(CreateWorkloadSnapshotContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitRefreshStats(RefreshStatsContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitBackupLogs(BackupLogsContext ctx) {
        return SplitQueryType.DATA_EXPORT;
    }

    @Override
    public SplitQueryType visitManageBackupLogs(ManageBackupLogsContext ctx) {
        return SplitQueryType.ADMIN_JOB;
    }

    @Override
    public SplitQueryType visitPurgeBackupLogs(PurgeBackupLogsContext ctx) {
        return SplitQueryType.MAINTAIN_LOG;
    }

    @Override
    public SplitQueryType visitShowTableDistributions(ShowTableDistributionsContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitShowBackupLogs(ShowBackupLogsContext ctx) {
        if (ctx.filename != null) {
            return SplitQueryType.LOG_READ;
        }
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitHelpStatement(HelpStatementContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitDoStatement(DoStatementContext ctx) {
        return SplitQueryType.BLOCK;
    }

    @Override
    public SplitQueryType visitOpenCursor(OpenCursorContext ctx) {
        return SplitQueryType.PROGRAM_CONTROL;
    }

    @Override
    public SplitQueryType visitCloseCursor(CloseCursorContext ctx) {
        return SplitQueryType.PROGRAM_CONTROL;
    }

    @Override
    public SplitQueryType visitFetchCursor(FetchCursorContext ctx) {
        return SplitQueryType.PROGRAM_CONTROL;
    }
}
