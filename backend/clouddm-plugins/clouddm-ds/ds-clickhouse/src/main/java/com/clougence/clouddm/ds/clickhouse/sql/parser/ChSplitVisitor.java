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
package com.clougence.clouddm.ds.clickhouse.sql.parser;

import com.clougence.clouddm.ds.clickhouse.sql.parser.antlr.ClickHouseParserBaseVisitor;
import com.clougence.clouddm.ds.clickhouse.sql.parser.antlr.ClickHouseParser.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;

public class ChSplitVisitor extends ClickHouseParserBaseVisitor<SplitQueryType> {

    public static ChSplitVisitor INSTANCE = new ChSplitVisitor();

    @Override
    public SplitQueryType visitQueryStmtQuery(QueryStmtQueryContext ctx) {
        return ctx.query().accept(this);
    }

    @Override
    public SplitQueryType visitQueryStmtExecuteAs(QueryStmtExecuteAsContext ctx) {
        return SplitQueryType.SWITCH_USER;
    }

    @Override
    public SplitQueryType visitExecuteAsBodyQuery(ExecuteAsBodyQueryContext ctx) {
        return ctx.query().accept(this);
    }

    @Override
    public SplitQueryType visitExecuteAsBodyInsert(ExecuteAsBodyInsertContext ctx) {
        return SplitQueryType.INSERT;
    }

    @Override
    public SplitQueryType visitExecuteAsBodyDelete(ExecuteAsBodyDeleteContext ctx) {
        return SplitQueryType.DELETE;
    }

    @Override
    public SplitQueryType visitExecuteAsBodyUpdate(ExecuteAsBodyUpdateContext ctx) {
        return SplitQueryType.UPDATE;
    }

    @Override
    public SplitQueryType visitExplainStmt(ExplainStmtContext ctx) {
        if (ctx.ANALYZE() != null) {
            return SplitQueryType.SELECT;
        }
        if (ctx.CURRENT() != null) {
            return SplitQueryType.TRANSACTION;
        }
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitHypotheticalIndexStmt(HypotheticalIndexStmtContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitCreateFunctionStmt(CreateFunctionStmtContext ctx) {
        return SplitQueryType.CREATE_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitQueryStmtInsert(QueryStmtInsertContext ctx) {
        return SplitQueryType.INSERT;
    }

    @Override
    public SplitQueryType visitQueryStmtDelete(QueryStmtDeleteContext ctx) {
        return SplitQueryType.DELETE;
    }

    @Override
    public SplitQueryType visitShowTablesStmt(ShowTablesStmtContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitCreateViewStmt(CreateViewStmtContext ctx) {
        return SplitQueryType.CREATE_VIEW;
    }

    @Override
    public SplitQueryType visitCreateMaterializedViewStmt(CreateMaterializedViewStmtContext ctx) {
        return SplitQueryType.CREATE_VIEW;
    }

    @Override
    public SplitQueryType visitDropTableStmt(DropTableStmtContext ctx) {
        if (ctx.TABLE() != null) {
            return SplitQueryType.DROP_TABLE;
        } else if (ctx.VIEW() != null) {
            return SplitQueryType.DROP_VIEW;
        }

        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitDropDatabaseStmt(DropDatabaseStmtContext ctx) {
        return SplitQueryType.DROP_SCHEMA;
    }

    @Override
    public SplitQueryType visitCreateDatabaseStmt(CreateDatabaseStmtContext ctx) {
        return SplitQueryType.CREATE_SCHEMA;
    }

    @Override
    public SplitQueryType visitAlterTableStmt(AlterTableStmtContext ctx) {
        if (ctx.alterTableClause().size() > 1) {
            for (AlterTableClauseContext alterTableClauseContext : ctx.alterTableClause()) {
                if (alterTableClauseContext instanceof AlterTableClauseUpdateContext || alterTableClauseContext instanceof AlterTableClauseDeleteContext) {
                    return SplitQueryType.UNKNOWN;
                }
            }
        } else if (ctx.alterTableClause().get(0) instanceof AlterTableClauseDeleteContext) {
            return SplitQueryType.DELETE;
        } else if (ctx.alterTableClause().get(0) instanceof AlterTableClauseUpdateContext) {
            return SplitQueryType.UPDATE;
        }
        return SplitQueryType.ALTER_TABLE;
    }

    @Override
    public SplitQueryType visitShowCreateTableStmt(ShowCreateTableStmtContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowCreateDatabaseStmt(ShowCreateDatabaseStmtContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowEnginesStmt(ShowEnginesStmtContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowQuotasStmt(ShowQuotasStmtContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowQuotaStmt(ShowQuotaStmtContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowRolesStmt(ShowRolesStmtContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowSettingsStmt(ShowSettingsStmtContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowSettingStmt(ShowSettingStmtContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitTruncateStmt(TruncateStmtContext ctx) {
        return SplitQueryType.TRUNCATE_TABLE;
    }

    @Override
    public SplitQueryType visitUseStmt(UseStmtContext ctx) {
        return SplitQueryType.SWITCH_SCHEMA;
    }

    @Override
    public SplitQueryType visitSetStmt(SetStmtContext ctx) {
        return ctx.settingExprList().settingExpr(0).accept(this);
    }

    @Override
    public SplitQueryType visitSettingExpr(SettingExprContext ctx) {
        String name = ctx.identifier().getText();
        if (name.startsWith("`") || name.startsWith("\"")) {
            name = name.substring(1, name.length() - 1);
        }
        if (name.startsWith("param_")) {
            return SplitQueryType.SESSION_VARIABLE_RW;
        }
        return SplitQueryType.SESSION_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitSetTimeZoneStmt(SetTimeZoneStmtContext ctx) {
        return SplitQueryType.SESSION_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitSetRoleStmt(SetRoleStmtContext ctx) {
        return SplitQueryType.SWITCH_ROLE;
    }

    @Override
    public SplitQueryType visitShowUsersStmt(ShowUsersStmtContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowProfilesStmt(ShowProfilesStmtContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowPoliciesStmt(ShowPoliciesStmtContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowCreateQuotaStmt(ShowCreateQuotaStmtContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowAccessStmt(ShowAccessStmtContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowClusterStmt(ShowClusterStmtContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowClustersStmt(ShowClustersStmtContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowFilesystemCaches(ShowFilesystemCachesContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowFunctionsStmt(ShowFunctionsStmtContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowMergesStmt(ShowMergesStmtContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitShowPrivilegesStmt(ShowPrivilegesStmtContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitOptimizeStmt(OptimizeStmtContext ctx) {
        if (ctx.DRY() != null) {
            return SplitQueryType.PERFORMANCE;
        }
        if (ctx.partitionClause() != null) {
            return SplitQueryType.ADMIN_PARTITION;
        }
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitRenameEntityClause(RenameEntityClauseContext ctx) {
        if (ctx.TABLE() != null) {
            return SplitQueryType.RENAME_TABLE;
        } else if (ctx.DATABASE() != null) {
            return SplitQueryType.RENAME_SCHEMA;
        }
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitCreateTableStmt(CreateTableStmtContext ctx) {
        return SplitQueryType.CREATE_TABLE;
    }

    @Override
    public SplitQueryType visitQueryStmtUpdate(QueryStmtUpdateContext ctx) {
        return SplitQueryType.UPDATE;
    }

    @Override
    public SplitQueryType visitSelectUnionStmt(SelectUnionStmtContext ctx) {
        return SplitQueryType.SELECT;
    }

    @Override
    public SplitQueryType visitCreateUserStmt(CreateUserStmtContext ctx) {
        return SplitQueryType.CREATE_USER;
    }

    @Override
    public SplitQueryType visitDropUserStmt(DropUserStmtContext ctx) {
        return SplitQueryType.DROP_USER;
    }

    @Override
    public SplitQueryType visitCreateRoleStmt(CreateRoleStmtContext ctx) {
        return SplitQueryType.CREATE_ROLE;
    }

    @Override
    public SplitQueryType visitDropRoleStmt(DropRoleStmtContext ctx) {
        return SplitQueryType.DROP_ROLE;
    }

    @Override
    public SplitQueryType visitSetDefaultRoleStmt(SetDefaultRoleStmtContext ctx) {
        return SplitQueryType.ALTER_USER;
    }

    @Override
    public SplitQueryType visitGrantStmt(GrantStmtContext ctx) {
        return SplitQueryType.GRANT;
    }

    @Override
    public SplitQueryType visitRevokeStmt(RevokeStmtContext ctx) {
        return SplitQueryType.REVOKE;
    }

    @Override
    public SplitQueryType visitCheckGrantStmt(CheckGrantStmtContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitCreateRowPolicyStmt(CreateRowPolicyStmtContext ctx) {
        return SplitQueryType.CREATE_POLICY;
    }

    @Override
    public SplitQueryType visitAlterRowPolicyStmt(AlterRowPolicyStmtContext ctx) {
        return SplitQueryType.ALTER_POLICY;
    }

    @Override
    public SplitQueryType visitDropRowPolicyStmt(DropRowPolicyStmtContext ctx) {
        return SplitQueryType.DROP_POLICY;
    }

    @Override
    public SplitQueryType visitCreateSettingsProfileStmt(CreateSettingsProfileStmtContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitAlterSettingsProfileStmt(AlterSettingsProfileStmtContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitDropSettingsProfileStmt(DropSettingsProfileStmtContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitCreateQuotaStmt(CreateQuotaStmtContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitAlterQuotaStmt(AlterQuotaStmtContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitDropQuotaStmt(DropQuotaStmtContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitShowCreateUserStmt(ShowCreateUserStmtContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowCreateRoleStmt(ShowCreateRoleStmtContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowCreatePolicyStmt(ShowCreatePolicyStmtContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowCreateProfileStmt(ShowCreateProfileStmtContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowGrantsStmt(ShowGrantsStmtContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitAlterUserStmt(AlterUserStmtContext ctx) {
        boolean rename = false;
        for (AlterUserClauseContext clause : ctx.alterUserClause()) {
            if (clause.accessRename() != null) {
                rename = true;
            } else if (clause.clusterClause() == null && clause.accessStorage() == null) {
                return SplitQueryType.ALTER_USER;
            }
        }
        if (rename) {
            return SplitQueryType.RENAME_USER;
        }
        return SplitQueryType.ALTER_USER;
    }

    @Override
    public SplitQueryType visitAlterRoleStmt(AlterRoleStmtContext ctx) {
        boolean rename = false;
        for (AlterRoleClauseContext clause : ctx.alterRoleClause()) {
            if (clause.accessRename() != null) {
                rename = true;
            } else if (clause.alterAccessSettings() != null) {
                return SplitQueryType.ALTER_ROLE;
            }
        }
        if (rename) {
            return SplitQueryType.RENAME_ROLE;
        }
        return SplitQueryType.ALTER_ROLE;
    }

    @Override
    public SplitQueryType visitSystemConfigurationStmt(SystemConfigurationStmtContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitSystemReloadDictionariesStmt(SystemReloadDictionariesStmtContext ctx) {
        return SplitQueryType.ADMIN;
    }

    @Override
    public SplitQueryType visitSystemReloadDictionaryStmt(SystemReloadDictionaryStmtContext ctx) {
        return SplitQueryType.ADMIN;
    }

    @Override
    public SplitQueryType visitSystemUnloadDictionariesStmt(SystemUnloadDictionariesStmtContext ctx) {
        return SplitQueryType.ADMIN;
    }

    @Override
    public SplitQueryType visitSystemUnloadDictionaryStmt(SystemUnloadDictionaryStmtContext ctx) {
        return SplitQueryType.ADMIN;
    }

    @Override
    public SplitQueryType visitSystemReloadModelsStmt(SystemReloadModelsStmtContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitSystemReloadModelStmt(SystemReloadModelStmtContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitSystemReloadFunctionsStmt(SystemReloadFunctionsStmtContext ctx) {
        return SplitQueryType.ADMIN_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitSystemReloadFunctionStmt(SystemReloadFunctionStmtContext ctx) {
        return SplitQueryType.ADMIN_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitSystemCacheStmt(SystemCacheStmtContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitSystemReloadMetricsStmt(SystemReloadMetricsStmtContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitSystemJemallocPurgeStmt(SystemJemallocPurgeStmtContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitSystemFlushLogsStmt(SystemFlushLogsStmtContext ctx) {
        return SplitQueryType.MAINTAIN_LOG;
    }

    @Override
    public SplitQueryType visitSystemListenStmt(SystemListenStmtContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitSystemShutdownStmt(SystemShutdownStmtContext ctx) {
        return SplitQueryType.ADMIN;
    }

    @Override
    public SplitQueryType visitSystemSuspendStmt(SystemSuspendStmtContext ctx) {
        return SplitQueryType.ADMIN;
    }

    @Override
    public SplitQueryType visitSystemRestartDiskStmt(SystemRestartDiskStmtContext ctx) {
        return SplitQueryType.ADMIN;
    }

    @Override
    public SplitQueryType visitCreateNamedCollectionStmt(CreateNamedCollectionStmtContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitAlterNamedCollectionStmt(AlterNamedCollectionStmtContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitDropNamedCollectionStmt(DropNamedCollectionStmtContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitCreateResourceStmt(CreateResourceStmtContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitDropResourceStmt(DropResourceStmtContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitCreateWorkloadStmt(CreateWorkloadStmtContext ctx) {
        return SplitQueryType.CREATE_RESOURCE_GROUP;
    }

    @Override
    public SplitQueryType visitDropWorkloadStmt(DropWorkloadStmtContext ctx) {
        return SplitQueryType.DROP_RESOURCE_GROUP;
    }

    @Override
    public SplitQueryType visitShowProcessListStmt(ShowProcessListStmtContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitDescribeCacheStmt(DescribeCacheStmtContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitKillQueryStmt(KillQueryStmtContext ctx) {
        if (ctx.TEST() != null) {
            return SplitQueryType.PERFORMANCE;
        }
        return SplitQueryType.ADMIN;
    }

    @Override
    public SplitQueryType visitCheckStmt(CheckStmtContext ctx) {
        if (ctx.DATABASE() != null) {
            return SplitQueryType.ADMIN;
        }
        if (ctx.partitionClause() != null) {
            return SplitQueryType.ADMIN_PARTITION;
        }
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitUndropStmt(UndropStmtContext ctx) {
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitSystemFlushDistributedStmt(SystemFlushDistributedStmtContext ctx) {
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitSystemDistributedControlStmt(SystemDistributedControlStmtContext ctx) {
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitSystemTableControlStmt(SystemTableControlStmtContext ctx) {
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitSystemScheduleMergeStmt(SystemScheduleMergeStmtContext ctx) {
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitSystemSyncMergesStmt(SystemSyncMergesStmtContext ctx) {
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitSystemWaitLoadingPartsStmt(SystemWaitLoadingPartsStmtContext ctx) {
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitSystemFlushAsyncInsertStmt(SystemFlushAsyncInsertStmtContext ctx) {
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitSystemFlushObjectStorageQueueStmt(SystemFlushObjectStorageQueueStmtContext ctx) {
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitSystemPrewarmStmt(SystemPrewarmStmtContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitSystemPrimaryKeyStmt(SystemPrimaryKeyStmtContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitSystemReplicatedSendsStmt(SystemReplicatedSendsStmtContext ctx) {
        return SplitQueryType.ALTER_REPLICATION;
    }

    @Override
    public SplitQueryType visitSystemReplicationControlStmt(SystemReplicationControlStmtContext ctx) {
        return SplitQueryType.ALTER_REPLICATION;
    }

    @Override
    public SplitQueryType visitSystemSyncReplicaStmt(SystemSyncReplicaStmtContext ctx) {
        return SplitQueryType.ADMIN_REPLICATION;
    }

    @Override
    public SplitQueryType visitSystemSyncDatabaseReplicaStmt(SystemSyncDatabaseReplicaStmtContext ctx) {
        return SplitQueryType.ADMIN_REPLICATION;
    }

    @Override
    public SplitQueryType visitSystemRestartReplicaStmt(SystemRestartReplicaStmtContext ctx) {
        return SplitQueryType.ADMIN_REPLICATION;
    }

    @Override
    public SplitQueryType visitSystemRestartReplicasStmt(SystemRestartReplicasStmtContext ctx) {
        return SplitQueryType.ADMIN_REPLICATION;
    }

    @Override
    public SplitQueryType visitSystemRestoreReplicaStmt(SystemRestoreReplicaStmtContext ctx) {
        return SplitQueryType.ADMIN_REPLICATION;
    }

    @Override
    public SplitQueryType visitSystemRestoreDatabaseReplicaStmt(SystemRestoreDatabaseReplicaStmtContext ctx) {
        return SplitQueryType.ADMIN_REPLICATION;
    }

    @Override
    public SplitQueryType visitSystemReconnectZooKeeperStmt(SystemReconnectZooKeeperStmtContext ctx) {
        return SplitQueryType.ADMIN_REPLICATION;
    }

    @Override
    public SplitQueryType visitSystemDropReplicaStmt(SystemDropReplicaStmtContext ctx) {
        return SplitQueryType.DROP_REPLICATION;
    }

    @Override
    public SplitQueryType visitSystemDropDatabaseReplicaStmt(SystemDropDatabaseReplicaStmtContext ctx) {
        return SplitQueryType.DROP_REPLICATION;
    }

    @Override
    public SplitQueryType visitSystemUnfreezeStmt(SystemUnfreezeStmtContext ctx) {
        return SplitQueryType.ADMIN;
    }

    @Override
    public SplitQueryType visitSystemViewStmt(SystemViewStmtContext ctx) {
        return SplitQueryType.ADMIN;
    }

    @Override
    public SplitQueryType visitSystemViewsStmt(SystemViewsStmtContext ctx) {
        return SplitQueryType.ADMIN;
    }

    @Override
    public SplitQueryType visitSystemReplicatedViewStmt(SystemReplicatedViewStmtContext ctx) {
        return SplitQueryType.ADMIN;
    }

    @Override
    public SplitQueryType visitSystemBackgroundStmt(SystemBackgroundStmtContext ctx) {
        return SplitQueryType.ADMIN;
    }

    @Override
    public SplitQueryType visitSystemSyncFileCacheStmt(SystemSyncFileCacheStmtContext ctx) {
        return SplitQueryType.ADMIN;
    }

    @Override
    public SplitQueryType visitSystemWaitBlobsCleanupStmt(SystemWaitBlobsCleanupStmtContext ctx) {
        return SplitQueryType.ADMIN;
    }

    @Override
    public SplitQueryType visitKillTransactionStmt(KillTransactionStmtContext ctx) {
        return SplitQueryType.TRANSACTION;
    }

    @Override
    public SplitQueryType visitSystemSyncTransactionLogStmt(SystemSyncTransactionLogStmtContext ctx) {
        return SplitQueryType.TRANSACTION;
    }

    @Override
    public SplitQueryType visitBackupQuery(BackupQueryContext ctx) {
        return SplitQueryType.DATA_EXPORT;
    }

    @Override
    public SplitQueryType visitRestoreQuery(RestoreQueryContext ctx) {
        return SplitQueryType.DATA_IMPORT;
    }

    @Override
    public SplitQueryType visitKillMutationStmt(KillMutationStmtContext ctx) {
        if (ctx.TEST() != null) {
            return SplitQueryType.PERFORMANCE;
        }
        return SplitQueryType.ADMIN;
    }

    @Override
    public SplitQueryType visitKillPartMoveStmt(KillPartMoveStmtContext ctx) {
        if (ctx.TEST() != null) {
            return SplitQueryType.PERFORMANCE;
        }
        return SplitQueryType.ADMIN;
    }
}
