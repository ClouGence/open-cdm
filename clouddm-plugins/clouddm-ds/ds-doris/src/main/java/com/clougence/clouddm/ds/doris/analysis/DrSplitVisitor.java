package com.clougence.clouddm.ds.doris.analysis;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.ds.doris.parser.antlr.DorisParserBaseVisitor;
import com.clougence.clouddm.ds.doris.parser.antlr.DorisParser.*;

public class DrSplitVisitor extends DorisParserBaseVisitor<SecQueryType> {

    public static final AbstractParseTreeVisitor<SecQueryType> INSTANCE = new DrSplitVisitor();

    @Override
    public SecQueryType visitStatementDefault(StatementDefaultContext ctx) {
        return SecQueryType.SELECT;
    }

    @Override
    public SecQueryType visitCancelAlterTable(CancelAlterTableContext ctx) {
        return SecQueryType.ALTER_TABLE;
    }

    public SecQueryType visitSetVariableWithType(SetVariableWithTypeContext ctx) {
        return SecQueryType.CONFIG_WRITE;
    }

    @Override
    public SecQueryType visitSetSystemVariable(SetSystemVariableContext ctx) {
        return SecQueryType.CONFIG_WRITE;
    }

    public SecQueryType visitSetUserVariable(SetUserVariableContext ctx) {
        return SecQueryType.CONFIG_WRITE;
    }

    @Override
    public SecQueryType visitTruncateTable(TruncateTableContext ctx) {
        return SecQueryType.TRUNCATE;
    }

    @Override
    public SecQueryType visitShowColumnHistogramStats(ShowColumnHistogramStatsContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitAlterColumnStats(AlterColumnStatsContext ctx) {
        return SecQueryType.ALTER_TABLE;
    }

    @Override
    public SecQueryType visitCreateRepository(CreateRepositoryContext ctx) {
        return SecQueryType.ADMIN;
    }

    @Override
    public SecQueryType visitCreateResource(CreateResourceContext ctx) {
        return SecQueryType.ADMIN;
    }

    @Override
    public SecQueryType visitCreateStoragePolicy(CreateStoragePolicyContext ctx) {
        return SecQueryType.ADMIN;
    }

    @Override
    public SecQueryType visitShowConfig(ShowConfigContext ctx) {
        return SecQueryType.ADMIN;
    }

    @Override
    public SecQueryType visitSupportedUnsetStatementAlias(SupportedUnsetStatementAliasContext ctx) {
        return SecQueryType.ADMIN;
    }

    @Override
    public SecQueryType visitMysqlLoad(MysqlLoadContext ctx) {
        return SecQueryType.LOAD;
    }

    @Override
    public SecQueryType visitCreateRoutineLoadAlias(CreateRoutineLoadAliasContext ctx) {
        return SecQueryType.CREATE_OBJECT;
    }

    @Override
    public SecQueryType visitShowCreateRoutineLoad(ShowCreateRoutineLoadContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitResumeRoutineLoad(ResumeRoutineLoadContext ctx) {
        return SecQueryType.ALTER_OBJECT;
    }

    @Override
    public SecQueryType visitLoad(LoadContext ctx) {
        return SecQueryType.LOAD;
    }

    @Override
    public SecQueryType visitCreateRowPolicy(CreateRowPolicyContext ctx) {
        return SecQueryType.ADMIN;
    }

    @Override
    public SecQueryType visitCreateWorkloadPolicy(CreateWorkloadPolicyContext ctx) {
        return SecQueryType.ADMIN;
    }

    @Override
    public SecQueryType visitCreateEncryptkey(CreateEncryptkeyContext ctx) {
        return SecQueryType.ADMIN;
    }

    @Override
    public SecQueryType visitCreateSqlBlockRule(CreateSqlBlockRuleContext ctx) {
        return SecQueryType.ADMIN;
    }

    @Override
    public SecQueryType visitCreateWorkloadGroup(CreateWorkloadGroupContext ctx) {
        return SecQueryType.CREATE_OBJECT;
    }

    @Override
    public SecQueryType visitCreateStorageVault(CreateStorageVaultContext ctx) {
        return SecQueryType.CREATE_OBJECT;
    }

    @Override
    public SecQueryType visitAlterSystem(AlterSystemContext ctx) {
        return SecQueryType.ADMIN;
    }

    @Override
    public SecQueryType visitAlterMTMV(AlterMTMVContext ctx) {
        return SecQueryType.ALTER_VIEW;
    }

    @Override
    public SecQueryType visitAlterView(AlterViewContext ctx) {
        return SecQueryType.ALTER_VIEW;
    }

    @Override
    public SecQueryType visitAlterRole(AlterRoleContext ctx) {
        return SecQueryType.ADMIN;
    }

    @Override
    public SecQueryType visitAlterWorkloadPolicy(AlterWorkloadPolicyContext ctx) {
        return SecQueryType.ADMIN;
    }

    @Override
    public SecQueryType visitAlterStoragePolicy(AlterStoragePolicyContext ctx) {
        return SecQueryType.ADMIN;
    }

    @Override
    public SecQueryType visitAlterSqlBlockRule(AlterSqlBlockRuleContext ctx) {
        return SecQueryType.ADMIN;
    }

    @Override
    public SecQueryType visitAlterCatalogProperties(AlterCatalogPropertiesContext ctx) {
        return SecQueryType.ALTER_CATALOG;
    }

    @Override
    public SecQueryType visitAlterStorageVault(AlterStorageVaultContext ctx) {
        return SecQueryType.ADMIN;
    }

    @Override
    public SecQueryType visitRefreshMTMV(RefreshMTMVContext ctx) {
        return SecQueryType.REFRESH_VIEW;
    }

    @Override
    public SecQueryType visitSupportedRecoverStatementAlias(SupportedRecoverStatementAliasContext ctx) {
        return SecQueryType.RECOVER;
    }

    @Override
    public SecQueryType visitSupportedKillStatementAlias(SupportedKillStatementAliasContext ctx) {
        return SecQueryType.KILL;
    }

    @Override
    public SecQueryType visitShowRoutineLoad(ShowRoutineLoadContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowRoutineLoadTask(ShowRoutineLoadTaskContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitStopRoutineLoad(StopRoutineLoadContext ctx) {
        return SecQueryType.ALTER_OBJECT;
    }

    @Override
    public SecQueryType visitResumeAllRoutineLoad(ResumeAllRoutineLoadContext ctx) {
        return SecQueryType.ALTER_OBJECT;
    }

    @Override
    public SecQueryType visitPauseAllRoutineLoad(PauseAllRoutineLoadContext ctx) {
        return SecQueryType.ALTER_OBJECT;
    }

    @Override
    public SecQueryType visitPauseRoutineLoad(PauseRoutineLoadContext ctx) {
        return SecQueryType.ALTER_OBJECT;
    }

    @Override
    public SecQueryType visitRefreshCatalog(RefreshCatalogContext ctx) {
        return SecQueryType.REFRESH_CATALOG;
    }

    @Override
    public SecQueryType visitRefreshDatabase(RefreshDatabaseContext ctx) {
        return SecQueryType.REFRESH_SCHEMA;
    }

    @Override
    public SecQueryType visitSync(SyncContext ctx) {
        return SecQueryType.SYNC;
    }

    @Override
    public SecQueryType visitRefreshLdap(RefreshLdapContext ctx) {
        return SecQueryType.ADMIN;
    }

    @Override
    public SecQueryType visitRefreshTable(RefreshTableContext ctx) {
        return SecQueryType.REFRESH_TABLE;
    }

    @Override
    public SecQueryType visitSupportedCleanStatementAlias(SupportedCleanStatementAliasContext ctx) {
        return SecQueryType.ADMIN;
    }

    @Override
    public SecQueryType visitShowProcedureStatus(ShowProcedureStatusContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitExport(ExportContext ctx) {
        return SecQueryType.EXPORT;
    }

    @Override
    public SecQueryType visitShowTableStats(ShowTableStatsContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitAlterTable(AlterTableContext ctx) {
        return SecQueryType.ALTER_TABLE;
    }

    @Override
    public SecQueryType visitAlterRoutineLoad(AlterRoutineLoadContext ctx) {
        return SecQueryType.ALTER_OBJECT;
    }

    @Override
    public SecQueryType visitAlterResource(AlterResourceContext ctx) {
        return SecQueryType.ALTER_OBJECT;
    }

    @Override
    public SecQueryType visitDropSqlBlockRule(DropSqlBlockRuleContext ctx) {
        return SecQueryType.ADMIN;
    }

    @Override
    public SecQueryType visitDropFile(DropFileContext ctx) {
        return SecQueryType.ADMIN;
    }

    @Override
    public SecQueryType visitDropWorkloadGroup(DropWorkloadGroupContext ctx) {
        return SecQueryType.ADMIN;
    }

    @Override
    public SecQueryType visitDropWorkloadPolicy(DropWorkloadPolicyContext ctx) {
        return SecQueryType.ADMIN;
    }

    @Override
    public SecQueryType visitDropStoragePolicy(DropStoragePolicyContext ctx) {
        return SecQueryType.ADMIN;
    }

    @Override
    public SecQueryType visitDropEncryptkey(DropEncryptkeyContext ctx) {
        return SecQueryType.ADMIN;
    }

    @Override
    public SecQueryType visitDropCatalogRecycleBin(DropCatalogRecycleBinContext ctx) {
        return SecQueryType.ADMIN;
    }

    @Override
    public SecQueryType visitAlterColocateGroup(AlterColocateGroupContext ctx) {
        return SecQueryType.ADMIN;
    }

    @Override
    public SecQueryType visitAlterTableAddRollup(AlterTableAddRollupContext ctx) {
        return SecQueryType.ALTER_TABLE;
    }

    @Override
    public SecQueryType visitAlterTableDropRollup(AlterTableDropRollupContext ctx) {
        return SecQueryType.ALTER_TABLE;
    }

    @Override
    public SecQueryType visitSwitchCatalog(SwitchCatalogContext ctx) {
        return SecQueryType.SWITCH_CATALOG;
    }

    @Override
    public SecQueryType visitCreateUserDefineFunction(CreateUserDefineFunctionContext ctx) {
        return SecQueryType.CREATE_FUNCTION;
    }

    @Override
    public SecQueryType visitCreateAliasFunction(CreateAliasFunctionContext ctx) {
        return SecQueryType.CREATE_FUNCTION;
    }

    @Override
    public SecQueryType visitCreateMTMV(CreateMTMVContext ctx) {
        return SecQueryType.CREATE_VIEW;
    }

    @Override
    public SecQueryType visitCreateIndex(CreateIndexContext ctx) {
        return SecQueryType.CREATE_INDEX;
    }

    @Override
    public SecQueryType visitCreateView(CreateViewContext ctx) {
        return SecQueryType.CREATE_VIEW;
    }

    @Override
    public SecQueryType visitCreateFile(CreateFileContext ctx) {
        return SecQueryType.CREATE_OBJECT;
    }

    @Override
    public SecQueryType visitDropView(DropViewContext ctx) {
        return SecQueryType.DROP_VIEW;
    }

    @Override
    public SecQueryType visitSupportedTransactionStatementAlias(SupportedTransactionStatementAliasContext ctx) {
        return SecQueryType.TRANSACTION;
    }

    @Override
    public SecQueryType visitCreateScheduledJob(CreateScheduledJobContext ctx) {
        return SecQueryType.CREATE_OBJECT;
    }

    @Override
    public SecQueryType visitPauseJob(PauseJobContext ctx) {
        return SecQueryType.ALTER_OBJECT;
    }

    @Override
    public SecQueryType visitDropJob(DropJobContext ctx) {
        return SecQueryType.DROP_OBJECT;
    }

    @Override
    public SecQueryType visitAnalyzeTable(AnalyzeTableContext ctx) {
        return SecQueryType.ANALYZE;
    }

    @Override
    public SecQueryType visitResumeJob(ResumeJobContext ctx) {
        return SecQueryType.ALTER_OBJECT;
    }

    @Override
    public SecQueryType visitDescribeTable(DescribeTableContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitSupportedAdminStatementAlias(SupportedAdminStatementAliasContext ctx) {
        return SecQueryType.ADMIN;
    }

    @Override
    public SecQueryType visitSetTransaction(SetTransactionContext ctx) {
        return SecQueryType.TRANSACTION;
    }

    @Override
    public SecQueryType visitDropMV(DropMVContext ctx) {
        return SecQueryType.DROP_VIEW;
    }

    @Override
    public SecQueryType visitCopyInto(CopyIntoContext ctx) {
        return SecQueryType.COPY_INTO;
    }

    @Override
    public SecQueryType visitDropIndex(DropIndexContext ctx) {
        return SecQueryType.DROP_INDEX;
    }

    @Override
    public SecQueryType visitUseDatabase(UseDatabaseContext ctx) {
        return SecQueryType.SWITCH_SCHEMA;
    }

    @Override
    public SecQueryType visitAlterTableProperties(AlterTablePropertiesContext ctx) {
        return SecQueryType.ALTER_TABLE;
    }

    @Override
    public SecQueryType visitCreateCatalog(CreateCatalogContext ctx) {
        return SecQueryType.CREATE_CATALOG;
    }

    @Override
    public SecQueryType visitDropCatalog(DropCatalogContext ctx) {
        return SecQueryType.DROP_CATALOG;
    }

    @Override
    public SecQueryType visitCreateRole(CreateRoleContext ctx) {
        return SecQueryType.CREATE_ROLE;
    }

    @Override
    public SecQueryType visitDropUser(DropUserContext ctx) {
        return SecQueryType.DROP_USER;
    }

    @Override
    public SecQueryType visitGrantTablePrivilege(GrantTablePrivilegeContext ctx) {
        return SecQueryType.GRANT;
    }

    @Override
    public SecQueryType visitGrantResourcePrivilege(GrantResourcePrivilegeContext ctx) {
        return SecQueryType.GRANT;
    }

    @Override
    public SecQueryType visitGrantRole(GrantRoleContext ctx) {
        return SecQueryType.GRANT;
    }

    @Override
    public SecQueryType visitRevokeTablePrivilege(RevokeTablePrivilegeContext ctx) {
        return SecQueryType.REVOKE;
    }

    @Override
    public SecQueryType visitRevokeRole(RevokeRoleContext ctx) {
        return SecQueryType.REVOKE;
    }

    @Override
    public SecQueryType visitRevokeResourcePrivilege(RevokeResourcePrivilegeContext ctx) {
        return SecQueryType.REVOKE;
    }

    @Override
    public SecQueryType visitDropRole(DropRoleContext ctx) {
        return SecQueryType.DROP_ROLE;
    }

    @Override
    public SecQueryType visitCreateUser(CreateUserContext ctx) {
        return SecQueryType.CREATE_USER;
    }

    @Override
    public SecQueryType visitAlterCatalogRename(AlterCatalogRenameContext ctx) {
        return SecQueryType.RENAME_CATALOG;
    }

    @Override
    public SecQueryType visitAlterCatalogComment(AlterCatalogCommentContext ctx) {
        return SecQueryType.ALTER_CATALOG;
    }

    @Override
    public SecQueryType visitSupportedShowStatementAlias(SupportedShowStatementAliasContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitCreateDatabase(CreateDatabaseContext ctx) {
        return SecQueryType.CREATE_SCHEMA;
    }

    @Override
    public SecQueryType visitDropDatabase(DropDatabaseContext ctx) {
        return SecQueryType.DROP_SCHEMA;
    }

    @Override
    public SecQueryType visitAlterDatabaseRename(AlterDatabaseRenameContext ctx) {
        return SecQueryType.RENAME_SCHEMA;
    }

    @Override
    public SecQueryType visitAlterDatabaseProperties(AlterDatabasePropertiesContext ctx) {
        return SecQueryType.ALTER_SCHEMA;
    }

    @Override
    public SecQueryType visitInsertTable(InsertTableContext ctx) {
        return SecQueryType.INSERT;
    }

    @Override
    public SecQueryType visitUpdate(UpdateContext ctx) {
        return SecQueryType.UPDATE;
    }

    @Override
    public SecQueryType visitCallProcedure(CallProcedureContext ctx) {
        return SecQueryType.CALL;
    }

    @Override
    public SecQueryType visitCreateTable(CreateTableContext ctx) {
        return SecQueryType.CREATE_TABLE;
    }

    @Override
    public SecQueryType visitCreateTableLike(CreateTableLikeContext ctx) {
        return SecQueryType.CREATE_TABLE_LIKE;
    }

    @Override
    public SecQueryType visitDelete(DeleteContext ctx) {
        return SecQueryType.DELETE;
    }

    @Override
    public SecQueryType visitDropTable(DropTableContext ctx) {
        return SecQueryType.DROP_TABLE;
    }
}
