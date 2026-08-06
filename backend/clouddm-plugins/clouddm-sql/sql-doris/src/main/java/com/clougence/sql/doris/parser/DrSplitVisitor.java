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
package com.clougence.sql.doris.parser;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.sql.doris.parser.antlr.DorisParserBaseVisitor;
import com.clougence.sql.doris.parser.antlr.DorisParser.*;

public class DrSplitVisitor extends DorisParserBaseVisitor<SplitQueryType> {

    public static final AbstractParseTreeVisitor<SplitQueryType> INSTANCE = new DrSplitVisitor();

    @Override
    public SplitQueryType visitStatementDefault(StatementDefaultContext ctx) {
        return ctx.explain() == null ? SplitQueryType.SELECT : SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitCancelAlterTable(CancelAlterTableContext ctx) {
        return SplitQueryType.ADMIN_TABLE;
    }

    public SplitQueryType visitSetVariableWithType(SetVariableWithTypeContext ctx) {
        return ctx.statementScope().GLOBAL() == null ? SplitQueryType.SESSION_SETTING_WRITE : SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitSetSystemVariable(SetSystemVariableContext ctx) {
        return ctx.statementScope() != null && ctx.statementScope().GLOBAL() != null ? SplitQueryType.SYSTEM_SETTING_WRITE : SplitQueryType.SESSION_SETTING_WRITE;
    }

    public SplitQueryType visitSetUserVariable(SetUserVariableContext ctx) {
        return SplitQueryType.SESSION_VARIABLE_RW;
    }

    @Override
    public SplitQueryType visitTruncateTable(TruncateTableContext ctx) {
        return SplitQueryType.TRUNCATE_TABLE;
    }

    @Override
    public SplitQueryType visitShowColumnHistogramStats(ShowColumnHistogramStatsContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitAlterColumnStats(AlterColumnStatsContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitCreateRepository(CreateRepositoryContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitCreateResource(CreateResourceContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitCreateStoragePolicy(CreateStoragePolicyContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitShowConfig(ShowConfigContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitSupportedUnsetStatementAlias(SupportedUnsetStatementAliasContext ctx) {
        SupportedUnsetStatementContext unset = ctx.supportedUnsetStatement();
        if (unset.DEFAULT() != null) {
            return SplitQueryType.SYSTEM_SETTING_WRITE;
        }
        return unset.statementScope() != null && unset.statementScope().GLOBAL() != null ? SplitQueryType.SYSTEM_SETTING_WRITE : SplitQueryType.SESSION_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitMysqlLoad(MysqlLoadContext ctx) {
        return SplitQueryType.DATA_IMPORT;
    }

    @Override
    public SplitQueryType visitCreateRoutineLoadAlias(CreateRoutineLoadAliasContext ctx) {
        return SplitQueryType.DATA_IMPORT;
    }

    @Override
    public SplitQueryType visitShowCreateRoutineLoad(ShowCreateRoutineLoadContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitResumeRoutineLoad(ResumeRoutineLoadContext ctx) {
        return SplitQueryType.DATA_IMPORT;
    }

    @Override
    public SplitQueryType visitLoad(LoadContext ctx) {
        return SplitQueryType.DATA_IMPORT;
    }

    @Override
    public SplitQueryType visitCreateRowPolicy(CreateRowPolicyContext ctx) {
        return SplitQueryType.CREATE_POLICY;
    }

    @Override
    public SplitQueryType visitCreateWorkloadPolicy(CreateWorkloadPolicyContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitCreateEncryptkey(CreateEncryptkeyContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitCreateSqlBlockRule(CreateSqlBlockRuleContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitCreateWorkloadGroup(CreateWorkloadGroupContext ctx) {
        return SplitQueryType.CREATE_RESOURCE_GROUP;
    }

    @Override
    public SplitQueryType visitCreateStorageVault(CreateStorageVaultContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitAlterSystem(AlterSystemContext ctx) {
        return SplitQueryType.ADMIN;
    }

    @Override
    public SplitQueryType visitAlterMTMV(AlterMTMVContext ctx) {
        return SplitQueryType.ALTER_VIEW;
    }

    @Override
    public SplitQueryType visitAlterView(AlterViewContext ctx) {
        return SplitQueryType.ALTER_VIEW;
    }

    @Override
    public SplitQueryType visitAlterRole(AlterRoleContext ctx) {
        return SplitQueryType.ALTER_ROLE;
    }

    @Override
    public SplitQueryType visitAlterWorkloadPolicy(AlterWorkloadPolicyContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitAlterStoragePolicy(AlterStoragePolicyContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitAlterSqlBlockRule(AlterSqlBlockRuleContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitAlterCatalogProperties(AlterCatalogPropertiesContext ctx) {
        return SplitQueryType.ALTER_CATALOG;
    }

    @Override
    public SplitQueryType visitAlterStorageVault(AlterStorageVaultContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitRefreshMTMV(RefreshMTMVContext ctx) {
        return SplitQueryType.ADMIN;
    }

    @Override
    public SplitQueryType visitSupportedRecoverStatementAlias(SupportedRecoverStatementAliasContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public SplitQueryType visitRecoverDatabase(RecoverDatabaseContext ctx) {
        return SplitQueryType.ADMIN;
    }

    @Override
    public SplitQueryType visitRecoverTable(RecoverTableContext ctx) {
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitRecoverPartition(RecoverPartitionContext ctx) {
        return SplitQueryType.ADMIN_PARTITION;
    }

    @Override
    public SplitQueryType visitSupportedKillStatementAlias(SupportedKillStatementAliasContext ctx) {
        return SplitQueryType.ADMIN;
    }

    @Override
    public SplitQueryType visitShowRoutineLoad(ShowRoutineLoadContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowRoutineLoadTask(ShowRoutineLoadTaskContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitStopRoutineLoad(StopRoutineLoadContext ctx) {
        return SplitQueryType.DATA_IMPORT;
    }

    @Override
    public SplitQueryType visitResumeAllRoutineLoad(ResumeAllRoutineLoadContext ctx) {
        return SplitQueryType.DATA_IMPORT;
    }

    @Override
    public SplitQueryType visitPauseAllRoutineLoad(PauseAllRoutineLoadContext ctx) {
        return SplitQueryType.DATA_IMPORT;
    }

    @Override
    public SplitQueryType visitPauseRoutineLoad(PauseRoutineLoadContext ctx) {
        return SplitQueryType.DATA_IMPORT;
    }

    @Override
    public SplitQueryType visitRefreshCatalog(RefreshCatalogContext ctx) {
        return SplitQueryType.ADMIN;
    }

    @Override
    public SplitQueryType visitRefreshDatabase(RefreshDatabaseContext ctx) {
        return SplitQueryType.ADMIN;
    }

    @Override
    public SplitQueryType visitSync(SyncContext ctx) {
        return SplitQueryType.ADMIN;
    }

    @Override
    public SplitQueryType visitRefreshLdap(RefreshLdapContext ctx) {
        return SplitQueryType.ADMIN;
    }

    @Override
    public SplitQueryType visitRefreshTable(RefreshTableContext ctx) {
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitSupportedCleanStatementAlias(SupportedCleanStatementAliasContext ctx) {
        SupportedCleanStatementContext clean = ctx.supportedCleanStatement();
        if (clean instanceof CleanAllProfileContext || clean instanceof CleanQueryStatsContext || clean instanceof CleanAllQueryStatsContext) {
            return SplitQueryType.ADMIN_PERFORMANCE;
        }
        return SplitQueryType.ADMIN;
    }

    @Override
    public SplitQueryType visitExport(ExportContext ctx) {
        return SplitQueryType.DATA_EXPORT;
    }

    @Override
    public SplitQueryType visitShowTableStats(ShowTableStatsContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitShowColumnStats(ShowColumnStatsContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitShowIndexStats(ShowIndexStatsContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitShowAnalyze(ShowAnalyzeContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitShowQueuedAnalyzeJobs(ShowQueuedAnalyzeJobsContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitShowAnalyzeTask(ShowAnalyzeTaskContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitAlterTable(AlterTableContext ctx) {
        return SplitQueryType.ALTER_TABLE;
    }

    @Override
    public SplitQueryType visitAddConstraint(AddConstraintContext ctx) {
        return SplitQueryType.ALTER_TABLE;
    }

    @Override
    public SplitQueryType visitDropConstraint(DropConstraintContext ctx) {
        return SplitQueryType.ALTER_TABLE;
    }

    @Override
    public SplitQueryType visitShowConstraint(ShowConstraintContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitAlterDatabaseSetQuota(AlterDatabaseSetQuotaContext ctx) {
        return SplitQueryType.ALTER_SCHEMA;
    }

    @Override
    public SplitQueryType visitAlterRoutineLoad(AlterRoutineLoadContext ctx) {
        return SplitQueryType.DATA_IMPORT;
    }

    @Override
    public SplitQueryType visitAlterResource(AlterResourceContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitAlterRepository(AlterRepositoryContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitAlterWorkloadGroup(AlterWorkloadGroupContext ctx) {
        return SplitQueryType.ALTER_RESOURCE_GROUP;
    }

    @Override
    public SplitQueryType visitAlterComputeGroup(AlterComputeGroupContext ctx) {
        return SplitQueryType.ALTER_RESOURCE_GROUP;
    }

    @Override
    public SplitQueryType visitAlterTableExecute(AlterTableExecuteContext ctx) {
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitAlterUser(AlterUserContext ctx) {
        return SplitQueryType.ALTER_USER;
    }

    @Override
    public SplitQueryType visitAlterJob(AlterJobContext ctx) {
        return SplitQueryType.ALTER_JOB;
    }

    @Override
    public SplitQueryType visitCancelJobTask(CancelJobTaskContext ctx) {
        return SplitQueryType.ADMIN_JOB;
    }

    @Override
    public SplitQueryType visitDropSqlBlockRule(DropSqlBlockRuleContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitDropFile(DropFileContext ctx) {
        return SplitQueryType.ADMIN;
    }

    @Override
    public SplitQueryType visitDropWorkloadGroup(DropWorkloadGroupContext ctx) {
        return SplitQueryType.DROP_RESOURCE_GROUP;
    }

    @Override
    public SplitQueryType visitDropWorkloadPolicy(DropWorkloadPolicyContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitDropStoragePolicy(DropStoragePolicyContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitDropEncryptkey(DropEncryptkeyContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitDropCatalogRecycleBin(DropCatalogRecycleBinContext ctx) {
        return SplitQueryType.ADMIN;
    }

    @Override
    public SplitQueryType visitAlterColocateGroup(AlterColocateGroupContext ctx) {
        return SplitQueryType.ADMIN;
    }

    @Override
    public SplitQueryType visitAlterTableAddRollup(AlterTableAddRollupContext ctx) {
        return SplitQueryType.ALTER_TABLE;
    }

    @Override
    public SplitQueryType visitAlterTableDropRollup(AlterTableDropRollupContext ctx) {
        return SplitQueryType.ALTER_TABLE;
    }

    @Override
    public SplitQueryType visitSwitchCatalog(SwitchCatalogContext ctx) {
        return SplitQueryType.SWITCH_CATALOG;
    }

    @Override
    public SplitQueryType visitCreateUserDefineFunction(CreateUserDefineFunctionContext ctx) {
        return SplitQueryType.CREATE_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitCreateAliasFunction(CreateAliasFunctionContext ctx) {
        return SplitQueryType.CREATE_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitCreateMTMV(CreateMTMVContext ctx) {
        return SplitQueryType.CREATE_VIEW;
    }

    @Override
    public SplitQueryType visitCreateIndex(CreateIndexContext ctx) {
        return SplitQueryType.ADD_INDEX;
    }

    @Override
    public SplitQueryType visitBuildIndex(BuildIndexContext ctx) {
        return SplitQueryType.ADMIN;
    }

    @Override
    public SplitQueryType visitCreateView(CreateViewContext ctx) {
        return SplitQueryType.CREATE_VIEW;
    }

    @Override
    public SplitQueryType visitCreateFile(CreateFileContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitDropResource(DropResourceContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitDropRepository(DropRepositoryContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitCreateDictionary(CreateDictionaryContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitDropDictionary(DropDictionaryContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitRefreshDictionary(RefreshDictionaryContext ctx) {
        return SplitQueryType.ADMIN;
    }

    @Override
    public SplitQueryType visitCreateAuthenticationIntegration(CreateAuthenticationIntegrationContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitAlterAuthenticationIntegrationProperties(AlterAuthenticationIntegrationPropertiesContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitAlterAuthenticationIntegrationUnsetProperties(AlterAuthenticationIntegrationUnsetPropertiesContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitAlterAuthenticationIntegrationComment(AlterAuthenticationIntegrationCommentContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitDropAuthenticationIntegration(DropAuthenticationIntegrationContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitCreateRoleMapping(CreateRoleMappingContext ctx) {
        return SplitQueryType.GRANT;
    }

    @Override
    public SplitQueryType visitDropRoleMapping(DropRoleMappingContext ctx) {
        return SplitQueryType.REVOKE;
    }

    @Override
    public SplitQueryType visitCreateStage(CreateStageContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitDropStage(DropStageContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitCreateIndexAnalyzer(CreateIndexAnalyzerContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitCreateIndexTokenizer(CreateIndexTokenizerContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitCreateIndexTokenFilter(CreateIndexTokenFilterContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitCreateIndexCharFilter(CreateIndexCharFilterContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitCreateIndexNormalizer(CreateIndexNormalizerContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitDropIndexAnalyzer(DropIndexAnalyzerContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitDropIndexTokenizer(DropIndexTokenizerContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitDropIndexTokenFilter(DropIndexTokenFilterContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitDropIndexCharFilter(DropIndexCharFilterContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitDropIndexNormalizer(DropIndexNormalizerContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitDropView(DropViewContext ctx) {
        return SplitQueryType.DROP_VIEW;
    }

    @Override
    public SplitQueryType visitDropFunction(DropFunctionContext ctx) {
        return SplitQueryType.DROP_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitSupportedTransactionStatementAlias(SupportedTransactionStatementAliasContext ctx) {
        return SplitQueryType.TRANSACTION;
    }

    @Override
    public SplitQueryType visitCreateScheduledJob(CreateScheduledJobContext ctx) {
        return SplitQueryType.CREATE_JOB;
    }

    @Override
    public SplitQueryType visitPauseJob(PauseJobContext ctx) {
        return SplitQueryType.ADMIN_JOB;
    }

    @Override
    public SplitQueryType visitDropJob(DropJobContext ctx) {
        return SplitQueryType.DROP_JOB;
    }

    @Override
    public SplitQueryType visitAnalyzeTable(AnalyzeTableContext ctx) {
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitResumeJob(ResumeJobContext ctx) {
        return SplitQueryType.ADMIN_JOB;
    }

    @Override
    public SplitQueryType visitDescribeTable(DescribeTableContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitSupportedDescribeStatementAlias(SupportedDescribeStatementAliasContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitSupportedAdminStatementAlias(SupportedAdminStatementAliasContext ctx) {
        return SplitQueryType.ADMIN;
    }

    @Override
    public SplitQueryType visitAnalyzeDatabase(AnalyzeDatabaseContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitAlterTableStats(AlterTableStatsContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitDropStats(DropStatsContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitDropCachedStats(DropCachedStatsContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitDropExpiredStats(DropExpiredStatsContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitKillAnalyzeJob(KillAnalyzeJobContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitDropAnalyzeJob(DropAnalyzeJobContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitSetTransaction(SetTransactionContext ctx) {
        return SplitQueryType.TRANSACTION;
    }

    @Override
    public SplitQueryType visitDropMV(DropMVContext ctx) {
        return SplitQueryType.DROP_VIEW;
    }

    @Override
    public SplitQueryType visitCopyInto(CopyIntoContext ctx) {
        return SplitQueryType.DATA_IMPORT;
    }

    @Override
    public SplitQueryType visitDropIndex(DropIndexContext ctx) {
        return SplitQueryType.DROP_INDEX;
    }

    @Override
    public SplitQueryType visitUseDatabase(UseDatabaseContext ctx) {
        return SplitQueryType.SWITCH_SCHEMA;
    }

    @Override
    public SplitQueryType visitAlterTableProperties(AlterTablePropertiesContext ctx) {
        return SplitQueryType.ALTER_TABLE;
    }

    @Override
    public SplitQueryType visitCreateCatalog(CreateCatalogContext ctx) {
        return SplitQueryType.CREATE_CATALOG;
    }

    @Override
    public SplitQueryType visitDropCatalog(DropCatalogContext ctx) {
        return SplitQueryType.DROP_CATALOG;
    }

    @Override
    public SplitQueryType visitCreateRole(CreateRoleContext ctx) {
        return SplitQueryType.CREATE_ROLE;
    }

    @Override
    public SplitQueryType visitDropUser(DropUserContext ctx) {
        return SplitQueryType.DROP_USER;
    }

    @Override
    public SplitQueryType visitGrantTablePrivilege(GrantTablePrivilegeContext ctx) {
        return SplitQueryType.GRANT;
    }

    @Override
    public SplitQueryType visitGrantResourcePrivilege(GrantResourcePrivilegeContext ctx) {
        return SplitQueryType.GRANT;
    }

    @Override
    public SplitQueryType visitGrantRole(GrantRoleContext ctx) {
        return SplitQueryType.GRANT;
    }

    @Override
    public SplitQueryType visitRevokeTablePrivilege(RevokeTablePrivilegeContext ctx) {
        return SplitQueryType.REVOKE;
    }

    @Override
    public SplitQueryType visitRevokeRole(RevokeRoleContext ctx) {
        return SplitQueryType.REVOKE;
    }

    @Override
    public SplitQueryType visitRevokeResourcePrivilege(RevokeResourcePrivilegeContext ctx) {
        return SplitQueryType.REVOKE;
    }

    @Override
    public SplitQueryType visitDropRole(DropRoleContext ctx) {
        return SplitQueryType.DROP_ROLE;
    }

    @Override
    public SplitQueryType visitCreateUser(CreateUserContext ctx) {
        return SplitQueryType.CREATE_USER;
    }

    @Override
    public SplitQueryType visitAlterCatalogRename(AlterCatalogRenameContext ctx) {
        return SplitQueryType.RENAME_CATALOG;
    }

    @Override
    public SplitQueryType visitAlterCatalogComment(AlterCatalogCommentContext ctx) {
        return SplitQueryType.COMMENT_CATALOG;
    }

    @Override
    public SplitQueryType visitSupportedShowStatementAlias(SupportedShowStatementAliasContext ctx) {
        SupportedShowStatementContext show = ctx.supportedShowStatement();
        if (show instanceof ShowBackupContext || show instanceof ShowRestoreContext || show instanceof ShowSnapshotContext) {
            return SplitQueryType.MAINTAIN_BACKUP;
        }
        if (show instanceof ShowProcessListContext || show instanceof ShowWarningErrorsContext || show instanceof ShowWarningErrorCountContext || show instanceof ShowStatusContext
            || show instanceof ShowOpenTablesContext || show instanceof ShowLoadProfileContext || show instanceof ShowQueryProfileContext
            || show instanceof ShowDiagnoseTabletContext || show instanceof ShowQueryStatsContext || show instanceof ShowDataSkewContext) {
            return SplitQueryType.PERFORMANCE;
        }
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowCreateMTMV(ShowCreateMTMVContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowCreateLoad(ShowCreateLoadContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowIndexAnalyzer(ShowIndexAnalyzerContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowIndexTokenizer(ShowIndexTokenizerContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowIndexTokenFilter(ShowIndexTokenFilterContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowIndexCharFilter(ShowIndexCharFilterContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowIndexNormalizer(ShowIndexNormalizerContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitHelp(HelpContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitBackup(BackupContext ctx) {
        return SplitQueryType.BACKUP;
    }

    @Override
    public SplitQueryType visitRestore(RestoreContext ctx) {
        return SplitQueryType.RESTORE;
    }

    @Override
    public SplitQueryType visitWarmUpCluster(WarmUpClusterContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitWarmUpSelect(WarmUpSelectContext ctx) {
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
    public SplitQueryType visitInstallPlugin(InstallPluginContext ctx) {
        return SplitQueryType.CREATE_LIBRARY;
    }

    @Override
    public SplitQueryType visitUninstallPlugin(UninstallPluginContext ctx) {
        return SplitQueryType.DROP_LIBRARY;
    }

    @Override
    public SplitQueryType visitCreateDatabase(CreateDatabaseContext ctx) {
        return SplitQueryType.CREATE_SCHEMA;
    }

    @Override
    public SplitQueryType visitDropDatabase(DropDatabaseContext ctx) {
        return SplitQueryType.DROP_SCHEMA;
    }

    @Override
    public SplitQueryType visitAlterDatabaseRename(AlterDatabaseRenameContext ctx) {
        return SplitQueryType.RENAME_SCHEMA;
    }

    @Override
    public SplitQueryType visitAlterDatabaseProperties(AlterDatabasePropertiesContext ctx) {
        return SplitQueryType.ALTER_SCHEMA;
    }

    @Override
    public SplitQueryType visitInsertTable(InsertTableContext ctx) {
        return ctx.OVERWRITE() == null ? SplitQueryType.INSERT : SplitQueryType.MERGE;
    }

    @Override
    public SplitQueryType visitInsertIntoTVF(InsertIntoTVFContext ctx) {
        return SplitQueryType.INSERT;
    }

    @Override
    public SplitQueryType visitMergeInto(MergeIntoContext ctx) {
        return SplitQueryType.MERGE;
    }

    @Override
    public SplitQueryType visitReplay(ReplayContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitUpdate(UpdateContext ctx) {
        return SplitQueryType.UPDATE;
    }

    @Override
    public SplitQueryType visitCallProcedure(CallProcedureContext ctx) {
        return SplitQueryType.CALL_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitCreateTable(CreateTableContext ctx) {
        return SplitQueryType.CREATE_TABLE;
    }

    @Override
    public SplitQueryType visitCreateTableLike(CreateTableLikeContext ctx) {
        return SplitQueryType.CREATE_TABLE;
    }

    @Override
    public SplitQueryType visitDelete(DeleteContext ctx) {
        return SplitQueryType.DELETE;
    }

    @Override
    public SplitQueryType visitSetUserProperties(SetUserPropertiesContext ctx) {
        return SplitQueryType.ALTER_USER;
    }

    @Override
    public SplitQueryType visitSetDefaultStorageVault(SetDefaultStorageVaultContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitSetLdapAdminPassword(SetLdapAdminPasswordContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitUseCloudCluster(UseCloudClusterContext ctx) {
        return SplitQueryType.ADMIN_RESOURCE_GROUP;
    }

    @Override
    public SplitQueryType visitCancelLoad(CancelLoadContext ctx) {
        return SplitQueryType.DATA_IMPORT;
    }

    @Override
    public SplitQueryType visitCancelExport(CancelExportContext ctx) {
        return SplitQueryType.DATA_EXPORT;
    }

    @Override
    public SplitQueryType visitCancelWarmUpJob(CancelWarmUpJobContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitCancelDecommisionBackend(CancelDecommisionBackendContext ctx) {
        return SplitQueryType.ADMIN;
    }

    @Override
    public SplitQueryType visitCancelBackup(CancelBackupContext ctx) {
        return SplitQueryType.MAINTAIN_BACKUP;
    }

    @Override
    public SplitQueryType visitCancelRestore(CancelRestoreContext ctx) {
        return SplitQueryType.MAINTAIN_BACKUP;
    }

    @Override
    public SplitQueryType visitCancelBuildIndex(CancelBuildIndexContext ctx) {
        return SplitQueryType.ADMIN;
    }

    @Override
    public SplitQueryType visitDropRowPolicy(DropRowPolicyContext ctx) {
        return SplitQueryType.DROP_POLICY;
    }

    @Override
    public SplitQueryType visitPauseMTMV(PauseMTMVContext ctx) {
        return SplitQueryType.ADMIN_JOB;
    }

    @Override
    public SplitQueryType visitResumeMTMV(ResumeMTMVContext ctx) {
        return SplitQueryType.ADMIN_JOB;
    }

    @Override
    public SplitQueryType visitCancelMTMVTask(CancelMTMVTaskContext ctx) {
        return SplitQueryType.ADMIN_JOB;
    }

    @Override
    public SplitQueryType visitDropTable(DropTableContext ctx) {
        return SplitQueryType.DROP_TABLE;
    }
}
