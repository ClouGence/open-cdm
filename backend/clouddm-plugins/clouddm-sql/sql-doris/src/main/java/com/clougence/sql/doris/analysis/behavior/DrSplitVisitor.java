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
package com.clougence.sql.doris.analysis.behavior;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementType;
import com.clougence.sql.doris.parser.antlr.DorisParserBaseVisitor;
import com.clougence.sql.doris.parser.antlr.DorisParser.*;

public class DrSplitVisitor extends DorisParserBaseVisitor<StatementType> {

    public static final AbstractParseTreeVisitor<StatementType> INSTANCE = new DrSplitVisitor();

    @Override
    public StatementType visitStatementDefault(StatementDefaultContext ctx) {
        return ctx.explain() == null ? StatementType.SELECT : StatementType.PERFORMANCE;
    }

    @Override
    public StatementType visitCancelAlterTable(CancelAlterTableContext ctx) {
        return StatementType.ADMIN_TABLE;
    }

    public StatementType visitSetVariableWithType(SetVariableWithTypeContext ctx) {
        return ctx.statementScope().GLOBAL() == null ? StatementType.SESSION_SETTING_WRITE : StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitSetSystemVariable(SetSystemVariableContext ctx) {
        return ctx.statementScope() != null && ctx.statementScope().GLOBAL() != null ? StatementType.SYSTEM_SETTING_WRITE : StatementType.SESSION_SETTING_WRITE;
    }

    public StatementType visitSetUserVariable(SetUserVariableContext ctx) {
        return StatementType.SESSION_VARIABLE_RW;
    }

    @Override
    public StatementType visitTruncateTable(TruncateTableContext ctx) {
        return StatementType.TRUNCATE_TABLE;
    }

    @Override
    public StatementType visitShowColumnHistogramStats(ShowColumnHistogramStatsContext ctx) {
        return StatementType.PERFORMANCE;
    }

    @Override
    public StatementType visitAlterColumnStats(AlterColumnStatsContext ctx) {
        return StatementType.ADMIN_PERFORMANCE;
    }

    @Override
    public StatementType visitCreateRepository(CreateRepositoryContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitCreateResource(CreateResourceContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitCreateStoragePolicy(CreateStoragePolicyContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitShowConfig(ShowConfigContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitSupportedUnsetStatementAlias(SupportedUnsetStatementAliasContext ctx) {
        SupportedUnsetStatementContext unset = ctx.supportedUnsetStatement();
        if (unset.DEFAULT() != null) {
            return StatementType.SYSTEM_SETTING_WRITE;
        }
        return unset.statementScope() != null && unset.statementScope().GLOBAL() != null ? StatementType.SYSTEM_SETTING_WRITE : StatementType.SESSION_SETTING_WRITE;
    }

    @Override
    public StatementType visitMysqlLoad(MysqlLoadContext ctx) {
        return StatementType.DATA_IMPORT;
    }

    @Override
    public StatementType visitCreateRoutineLoadAlias(CreateRoutineLoadAliasContext ctx) {
        return StatementType.DATA_IMPORT;
    }

    @Override
    public StatementType visitShowCreateRoutineLoad(ShowCreateRoutineLoadContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitResumeRoutineLoad(ResumeRoutineLoadContext ctx) {
        return StatementType.DATA_IMPORT;
    }

    @Override
    public StatementType visitLoad(LoadContext ctx) {
        return StatementType.DATA_IMPORT;
    }

    @Override
    public StatementType visitCreateRowPolicy(CreateRowPolicyContext ctx) {
        return StatementType.CREATE_POLICY;
    }

    @Override
    public StatementType visitCreateWorkloadPolicy(CreateWorkloadPolicyContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitCreateEncryptkey(CreateEncryptkeyContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitCreateSqlBlockRule(CreateSqlBlockRuleContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitCreateWorkloadGroup(CreateWorkloadGroupContext ctx) {
        return StatementType.CREATE_RESOURCE_GROUP;
    }

    @Override
    public StatementType visitCreateStorageVault(CreateStorageVaultContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitAlterSystem(AlterSystemContext ctx) {
        return StatementType.ADMIN;
    }

    @Override
    public StatementType visitAlterMTMV(AlterMTMVContext ctx) {
        return StatementType.ALTER_VIEW;
    }

    @Override
    public StatementType visitAlterView(AlterViewContext ctx) {
        return StatementType.ALTER_VIEW;
    }

    @Override
    public StatementType visitAlterRole(AlterRoleContext ctx) {
        return StatementType.ALTER_ROLE;
    }

    @Override
    public StatementType visitAlterWorkloadPolicy(AlterWorkloadPolicyContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitAlterStoragePolicy(AlterStoragePolicyContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitAlterSqlBlockRule(AlterSqlBlockRuleContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitAlterCatalogProperties(AlterCatalogPropertiesContext ctx) {
        return StatementType.ALTER_CATALOG;
    }

    @Override
    public StatementType visitAlterStorageVault(AlterStorageVaultContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitRefreshMTMV(RefreshMTMVContext ctx) {
        return StatementType.ADMIN;
    }

    @Override
    public StatementType visitSupportedRecoverStatementAlias(SupportedRecoverStatementAliasContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public StatementType visitRecoverDatabase(RecoverDatabaseContext ctx) {
        return StatementType.ADMIN;
    }

    @Override
    public StatementType visitRecoverTable(RecoverTableContext ctx) {
        return StatementType.ADMIN_TABLE;
    }

    @Override
    public StatementType visitRecoverPartition(RecoverPartitionContext ctx) {
        return StatementType.ADMIN_PARTITION;
    }

    @Override
    public StatementType visitSupportedKillStatementAlias(SupportedKillStatementAliasContext ctx) {
        return StatementType.ADMIN;
    }

    @Override
    public StatementType visitShowRoutineLoad(ShowRoutineLoadContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitShowRoutineLoadTask(ShowRoutineLoadTaskContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitStopRoutineLoad(StopRoutineLoadContext ctx) {
        return StatementType.DATA_IMPORT;
    }

    @Override
    public StatementType visitResumeAllRoutineLoad(ResumeAllRoutineLoadContext ctx) {
        return StatementType.DATA_IMPORT;
    }

    @Override
    public StatementType visitPauseAllRoutineLoad(PauseAllRoutineLoadContext ctx) {
        return StatementType.DATA_IMPORT;
    }

    @Override
    public StatementType visitPauseRoutineLoad(PauseRoutineLoadContext ctx) {
        return StatementType.DATA_IMPORT;
    }

    @Override
    public StatementType visitRefreshCatalog(RefreshCatalogContext ctx) {
        return StatementType.ADMIN;
    }

    @Override
    public StatementType visitRefreshDatabase(RefreshDatabaseContext ctx) {
        return StatementType.ADMIN;
    }

    @Override
    public StatementType visitSync(SyncContext ctx) {
        return StatementType.ADMIN;
    }

    @Override
    public StatementType visitRefreshLdap(RefreshLdapContext ctx) {
        return StatementType.ADMIN;
    }

    @Override
    public StatementType visitRefreshTable(RefreshTableContext ctx) {
        return StatementType.ADMIN_TABLE;
    }

    @Override
    public StatementType visitSupportedCleanStatementAlias(SupportedCleanStatementAliasContext ctx) {
        SupportedCleanStatementContext clean = ctx.supportedCleanStatement();
        if (clean instanceof CleanAllProfileContext || clean instanceof CleanQueryStatsContext || clean instanceof CleanAllQueryStatsContext) {
            return StatementType.ADMIN_PERFORMANCE;
        }
        return StatementType.ADMIN;
    }

    @Override
    public StatementType visitExport(ExportContext ctx) {
        return StatementType.DATA_EXPORT;
    }

    @Override
    public StatementType visitShowTableStats(ShowTableStatsContext ctx) {
        return StatementType.PERFORMANCE;
    }

    @Override
    public StatementType visitShowColumnStats(ShowColumnStatsContext ctx) {
        return StatementType.PERFORMANCE;
    }

    @Override
    public StatementType visitShowIndexStats(ShowIndexStatsContext ctx) {
        return StatementType.PERFORMANCE;
    }

    @Override
    public StatementType visitShowAnalyze(ShowAnalyzeContext ctx) {
        return StatementType.PERFORMANCE;
    }

    @Override
    public StatementType visitShowQueuedAnalyzeJobs(ShowQueuedAnalyzeJobsContext ctx) {
        return StatementType.PERFORMANCE;
    }

    @Override
    public StatementType visitShowAnalyzeTask(ShowAnalyzeTaskContext ctx) {
        return StatementType.PERFORMANCE;
    }

    @Override
    public StatementType visitAlterTable(AlterTableContext ctx) {
        return StatementType.ALTER_TABLE;
    }

    @Override
    public StatementType visitAddConstraint(AddConstraintContext ctx) {
        return StatementType.ALTER_TABLE;
    }

    @Override
    public StatementType visitDropConstraint(DropConstraintContext ctx) {
        return StatementType.ALTER_TABLE;
    }

    @Override
    public StatementType visitShowConstraint(ShowConstraintContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitAlterDatabaseSetQuota(AlterDatabaseSetQuotaContext ctx) {
        return StatementType.ALTER_SCHEMA;
    }

    @Override
    public StatementType visitAlterRoutineLoad(AlterRoutineLoadContext ctx) {
        return StatementType.DATA_IMPORT;
    }

    @Override
    public StatementType visitAlterResource(AlterResourceContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitAlterRepository(AlterRepositoryContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitAlterWorkloadGroup(AlterWorkloadGroupContext ctx) {
        return StatementType.ALTER_RESOURCE_GROUP;
    }

    @Override
    public StatementType visitAlterComputeGroup(AlterComputeGroupContext ctx) {
        return StatementType.ALTER_RESOURCE_GROUP;
    }

    @Override
    public StatementType visitAlterTableExecute(AlterTableExecuteContext ctx) {
        return StatementType.ADMIN_TABLE;
    }

    @Override
    public StatementType visitAlterUser(AlterUserContext ctx) {
        return StatementType.ALTER_USER;
    }

    @Override
    public StatementType visitAlterJob(AlterJobContext ctx) {
        return StatementType.ALTER_JOB;
    }

    @Override
    public StatementType visitCancelJobTask(CancelJobTaskContext ctx) {
        return StatementType.ADMIN_JOB;
    }

    @Override
    public StatementType visitDropSqlBlockRule(DropSqlBlockRuleContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitDropFile(DropFileContext ctx) {
        return StatementType.ADMIN;
    }

    @Override
    public StatementType visitDropWorkloadGroup(DropWorkloadGroupContext ctx) {
        return StatementType.DROP_RESOURCE_GROUP;
    }

    @Override
    public StatementType visitDropWorkloadPolicy(DropWorkloadPolicyContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitDropStoragePolicy(DropStoragePolicyContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitDropEncryptkey(DropEncryptkeyContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitDropCatalogRecycleBin(DropCatalogRecycleBinContext ctx) {
        return StatementType.ADMIN;
    }

    @Override
    public StatementType visitAlterColocateGroup(AlterColocateGroupContext ctx) {
        return StatementType.ADMIN;
    }

    @Override
    public StatementType visitAlterTableAddRollup(AlterTableAddRollupContext ctx) {
        return StatementType.ALTER_TABLE;
    }

    @Override
    public StatementType visitAlterTableDropRollup(AlterTableDropRollupContext ctx) {
        return StatementType.ALTER_TABLE;
    }

    @Override
    public StatementType visitSwitchCatalog(SwitchCatalogContext ctx) {
        return StatementType.SWITCH_CATALOG;
    }

    @Override
    public StatementType visitCreateUserDefineFunction(CreateUserDefineFunctionContext ctx) {
        return StatementType.CREATE_PROG_OBJ;
    }

    @Override
    public StatementType visitCreateAliasFunction(CreateAliasFunctionContext ctx) {
        return StatementType.CREATE_PROG_OBJ;
    }

    @Override
    public StatementType visitCreateMTMV(CreateMTMVContext ctx) {
        return StatementType.CREATE_VIEW;
    }

    @Override
    public StatementType visitCreateIndex(CreateIndexContext ctx) {
        return StatementType.ADD_INDEX;
    }

    @Override
    public StatementType visitBuildIndex(BuildIndexContext ctx) {
        return StatementType.ADMIN;
    }

    @Override
    public StatementType visitCreateView(CreateViewContext ctx) {
        return StatementType.CREATE_VIEW;
    }

    @Override
    public StatementType visitCreateFile(CreateFileContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitDropResource(DropResourceContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitDropRepository(DropRepositoryContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitCreateDictionary(CreateDictionaryContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitDropDictionary(DropDictionaryContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitRefreshDictionary(RefreshDictionaryContext ctx) {
        return StatementType.ADMIN;
    }

    @Override
    public StatementType visitCreateAuthenticationIntegration(CreateAuthenticationIntegrationContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitAlterAuthenticationIntegrationProperties(AlterAuthenticationIntegrationPropertiesContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitAlterAuthenticationIntegrationUnsetProperties(AlterAuthenticationIntegrationUnsetPropertiesContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitAlterAuthenticationIntegrationComment(AlterAuthenticationIntegrationCommentContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitDropAuthenticationIntegration(DropAuthenticationIntegrationContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitCreateRoleMapping(CreateRoleMappingContext ctx) {
        return StatementType.GRANT;
    }

    @Override
    public StatementType visitDropRoleMapping(DropRoleMappingContext ctx) {
        return StatementType.REVOKE;
    }

    @Override
    public StatementType visitCreateStage(CreateStageContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitDropStage(DropStageContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitCreateIndexAnalyzer(CreateIndexAnalyzerContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitCreateIndexTokenizer(CreateIndexTokenizerContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitCreateIndexTokenFilter(CreateIndexTokenFilterContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitCreateIndexCharFilter(CreateIndexCharFilterContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitCreateIndexNormalizer(CreateIndexNormalizerContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitDropIndexAnalyzer(DropIndexAnalyzerContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitDropIndexTokenizer(DropIndexTokenizerContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitDropIndexTokenFilter(DropIndexTokenFilterContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitDropIndexCharFilter(DropIndexCharFilterContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitDropIndexNormalizer(DropIndexNormalizerContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitDropView(DropViewContext ctx) {
        return StatementType.DROP_VIEW;
    }

    @Override
    public StatementType visitDropFunction(DropFunctionContext ctx) {
        return StatementType.DROP_PROG_OBJ;
    }

    @Override
    public StatementType visitSupportedTransactionStatementAlias(SupportedTransactionStatementAliasContext ctx) {
        return StatementType.TRANSACTION;
    }

    @Override
    public StatementType visitCreateScheduledJob(CreateScheduledJobContext ctx) {
        return StatementType.CREATE_JOB;
    }

    @Override
    public StatementType visitPauseJob(PauseJobContext ctx) {
        return StatementType.ADMIN_JOB;
    }

    @Override
    public StatementType visitDropJob(DropJobContext ctx) {
        return StatementType.DROP_JOB;
    }

    @Override
    public StatementType visitAnalyzeTable(AnalyzeTableContext ctx) {
        return StatementType.ADMIN_TABLE;
    }

    @Override
    public StatementType visitResumeJob(ResumeJobContext ctx) {
        return StatementType.ADMIN_JOB;
    }

    @Override
    public StatementType visitDescribeTable(DescribeTableContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitSupportedDescribeStatementAlias(SupportedDescribeStatementAliasContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitSupportedAdminStatementAlias(SupportedAdminStatementAliasContext ctx) {
        return StatementType.ADMIN;
    }

    @Override
    public StatementType visitAnalyzeDatabase(AnalyzeDatabaseContext ctx) {
        return StatementType.ADMIN_PERFORMANCE;
    }

    @Override
    public StatementType visitAlterTableStats(AlterTableStatsContext ctx) {
        return StatementType.ADMIN_PERFORMANCE;
    }

    @Override
    public StatementType visitDropStats(DropStatsContext ctx) {
        return StatementType.ADMIN_PERFORMANCE;
    }

    @Override
    public StatementType visitDropCachedStats(DropCachedStatsContext ctx) {
        return StatementType.ADMIN_PERFORMANCE;
    }

    @Override
    public StatementType visitDropExpiredStats(DropExpiredStatsContext ctx) {
        return StatementType.ADMIN_PERFORMANCE;
    }

    @Override
    public StatementType visitKillAnalyzeJob(KillAnalyzeJobContext ctx) {
        return StatementType.ADMIN_PERFORMANCE;
    }

    @Override
    public StatementType visitDropAnalyzeJob(DropAnalyzeJobContext ctx) {
        return StatementType.ADMIN_PERFORMANCE;
    }

    @Override
    public StatementType visitSetTransaction(SetTransactionContext ctx) {
        return StatementType.TRANSACTION;
    }

    @Override
    public StatementType visitDropMV(DropMVContext ctx) {
        return StatementType.DROP_VIEW;
    }

    @Override
    public StatementType visitCopyInto(CopyIntoContext ctx) {
        return StatementType.DATA_IMPORT;
    }

    @Override
    public StatementType visitDropIndex(DropIndexContext ctx) {
        return StatementType.DROP_INDEX;
    }

    @Override
    public StatementType visitUseDatabase(UseDatabaseContext ctx) {
        return StatementType.SWITCH_SCHEMA;
    }

    @Override
    public StatementType visitAlterTableProperties(AlterTablePropertiesContext ctx) {
        return StatementType.ALTER_TABLE;
    }

    @Override
    public StatementType visitCreateCatalog(CreateCatalogContext ctx) {
        return StatementType.CREATE_CATALOG;
    }

    @Override
    public StatementType visitDropCatalog(DropCatalogContext ctx) {
        return StatementType.DROP_CATALOG;
    }

    @Override
    public StatementType visitCreateRole(CreateRoleContext ctx) {
        return StatementType.CREATE_ROLE;
    }

    @Override
    public StatementType visitDropUser(DropUserContext ctx) {
        return StatementType.DROP_USER;
    }

    @Override
    public StatementType visitGrantTablePrivilege(GrantTablePrivilegeContext ctx) {
        return StatementType.GRANT;
    }

    @Override
    public StatementType visitGrantResourcePrivilege(GrantResourcePrivilegeContext ctx) {
        return StatementType.GRANT;
    }

    @Override
    public StatementType visitGrantRole(GrantRoleContext ctx) {
        return StatementType.GRANT;
    }

    @Override
    public StatementType visitRevokeTablePrivilege(RevokeTablePrivilegeContext ctx) {
        return StatementType.REVOKE;
    }

    @Override
    public StatementType visitRevokeRole(RevokeRoleContext ctx) {
        return StatementType.REVOKE;
    }

    @Override
    public StatementType visitRevokeResourcePrivilege(RevokeResourcePrivilegeContext ctx) {
        return StatementType.REVOKE;
    }

    @Override
    public StatementType visitDropRole(DropRoleContext ctx) {
        return StatementType.DROP_ROLE;
    }

    @Override
    public StatementType visitCreateUser(CreateUserContext ctx) {
        return StatementType.CREATE_USER;
    }

    @Override
    public StatementType visitAlterCatalogRename(AlterCatalogRenameContext ctx) {
        return StatementType.RENAME_CATALOG;
    }

    @Override
    public StatementType visitAlterCatalogComment(AlterCatalogCommentContext ctx) {
        return StatementType.COMMENT_CATALOG;
    }

    @Override
    public StatementType visitSupportedShowStatementAlias(SupportedShowStatementAliasContext ctx) {
        SupportedShowStatementContext show = ctx.supportedShowStatement();
        if (show instanceof ShowBackupContext || show instanceof ShowRestoreContext || show instanceof ShowSnapshotContext) {
            return StatementType.MAINTAIN_BACKUP;
        }
        if (show instanceof ShowProcessListContext || show instanceof ShowWarningErrorsContext || show instanceof ShowWarningErrorCountContext || show instanceof ShowStatusContext
            || show instanceof ShowOpenTablesContext || show instanceof ShowLoadProfileContext || show instanceof ShowQueryProfileContext
            || show instanceof ShowDiagnoseTabletContext || show instanceof ShowQueryStatsContext || show instanceof ShowDataSkewContext) {
            return StatementType.PERFORMANCE;
        }
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitShowCreateMTMV(ShowCreateMTMVContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitShowCreateLoad(ShowCreateLoadContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitShowIndexAnalyzer(ShowIndexAnalyzerContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitShowIndexTokenizer(ShowIndexTokenizerContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitShowIndexTokenFilter(ShowIndexTokenFilterContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitShowIndexCharFilter(ShowIndexCharFilterContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitShowIndexNormalizer(ShowIndexNormalizerContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitHelp(HelpContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitBackup(BackupContext ctx) {
        return StatementType.BACKUP;
    }

    @Override
    public StatementType visitRestore(RestoreContext ctx) {
        return StatementType.RESTORE;
    }

    @Override
    public StatementType visitWarmUpCluster(WarmUpClusterContext ctx) {
        return StatementType.ADMIN_PERFORMANCE;
    }

    @Override
    public StatementType visitWarmUpSelect(WarmUpSelectContext ctx) {
        return StatementType.ADMIN_PERFORMANCE;
    }

    @Override
    public StatementType visitLockTables(LockTablesContext ctx) {
        return StatementType.SESSION_LOCK;
    }

    @Override
    public StatementType visitUnlockTables(UnlockTablesContext ctx) {
        return StatementType.SESSION_LOCK;
    }

    @Override
    public StatementType visitInstallPlugin(InstallPluginContext ctx) {
        return StatementType.CREATE_LIBRARY;
    }

    @Override
    public StatementType visitUninstallPlugin(UninstallPluginContext ctx) {
        return StatementType.DROP_LIBRARY;
    }

    @Override
    public StatementType visitCreateDatabase(CreateDatabaseContext ctx) {
        return StatementType.CREATE_SCHEMA;
    }

    @Override
    public StatementType visitDropDatabase(DropDatabaseContext ctx) {
        return StatementType.DROP_SCHEMA;
    }

    @Override
    public StatementType visitAlterDatabaseRename(AlterDatabaseRenameContext ctx) {
        return StatementType.RENAME_SCHEMA;
    }

    @Override
    public StatementType visitAlterDatabaseProperties(AlterDatabasePropertiesContext ctx) {
        return StatementType.ALTER_SCHEMA;
    }

    @Override
    public StatementType visitInsertTable(InsertTableContext ctx) {
        return ctx.OVERWRITE() == null ? StatementType.INSERT : StatementType.MERGE;
    }

    @Override
    public StatementType visitInsertIntoTVF(InsertIntoTVFContext ctx) {
        return StatementType.INSERT;
    }

    @Override
    public StatementType visitMergeInto(MergeIntoContext ctx) {
        return StatementType.MERGE;
    }

    @Override
    public StatementType visitReplay(ReplayContext ctx) {
        return StatementType.PERFORMANCE;
    }

    @Override
    public StatementType visitUpdate(UpdateContext ctx) {
        return StatementType.UPDATE;
    }

    @Override
    public StatementType visitCallProcedure(CallProcedureContext ctx) {
        return StatementType.CALL_PROG_OBJ;
    }

    @Override
    public StatementType visitCreateTable(CreateTableContext ctx) {
        return StatementType.CREATE_TABLE;
    }

    @Override
    public StatementType visitCreateTableLike(CreateTableLikeContext ctx) {
        return StatementType.CREATE_TABLE;
    }

    @Override
    public StatementType visitDelete(DeleteContext ctx) {
        return StatementType.DELETE;
    }

    @Override
    public StatementType visitSetUserProperties(SetUserPropertiesContext ctx) {
        return StatementType.ALTER_USER;
    }

    @Override
    public StatementType visitSetDefaultStorageVault(SetDefaultStorageVaultContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitSetLdapAdminPassword(SetLdapAdminPasswordContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitUseCloudCluster(UseCloudClusterContext ctx) {
        return StatementType.ADMIN_RESOURCE_GROUP;
    }

    @Override
    public StatementType visitCancelLoad(CancelLoadContext ctx) {
        return StatementType.DATA_IMPORT;
    }

    @Override
    public StatementType visitCancelExport(CancelExportContext ctx) {
        return StatementType.DATA_EXPORT;
    }

    @Override
    public StatementType visitCancelWarmUpJob(CancelWarmUpJobContext ctx) {
        return StatementType.ADMIN_PERFORMANCE;
    }

    @Override
    public StatementType visitCancelDecommisionBackend(CancelDecommisionBackendContext ctx) {
        return StatementType.ADMIN;
    }

    @Override
    public StatementType visitCancelBackup(CancelBackupContext ctx) {
        return StatementType.MAINTAIN_BACKUP;
    }

    @Override
    public StatementType visitCancelRestore(CancelRestoreContext ctx) {
        return StatementType.MAINTAIN_BACKUP;
    }

    @Override
    public StatementType visitCancelBuildIndex(CancelBuildIndexContext ctx) {
        return StatementType.ADMIN;
    }

    @Override
    public StatementType visitDropRowPolicy(DropRowPolicyContext ctx) {
        return StatementType.DROP_POLICY;
    }

    @Override
    public StatementType visitPauseMTMV(PauseMTMVContext ctx) {
        return StatementType.ADMIN_JOB;
    }

    @Override
    public StatementType visitResumeMTMV(ResumeMTMVContext ctx) {
        return StatementType.ADMIN_JOB;
    }

    @Override
    public StatementType visitCancelMTMVTask(CancelMTMVTaskContext ctx) {
        return StatementType.ADMIN_JOB;
    }

    @Override
    public StatementType visitDropTable(DropTableContext ctx) {
        return StatementType.DROP_TABLE;
    }
}
