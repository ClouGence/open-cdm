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
package com.clougence.clouddm.ds.clickhouse.sql.analysis.behavior;

import com.clougence.clouddm.ds.clickhouse.sql.parser.antlr.ClickHouseParserBaseVisitor;
import com.clougence.clouddm.ds.clickhouse.sql.parser.antlr.ClickHouseParser.*;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementType;

public class ChSplitVisitor extends ClickHouseParserBaseVisitor<StatementType> {

    public static ChSplitVisitor INSTANCE = new ChSplitVisitor();

    @Override
    public StatementType visitQueryStmtInsert(QueryStmtInsertContext ctx) {
        return StatementType.INSERT;
    }

    @Override
    public StatementType visitQueryStmtDelete(QueryStmtDeleteContext ctx) {
        return StatementType.DELETE;
    }

    @Override
    public StatementType visitShowTablesStmt(ShowTablesStmtContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitCreateViewStmt(CreateViewStmtContext ctx) {
        return StatementType.CREATE_VIEW;
    }

    @Override
    public StatementType visitCreateMaterializedViewStmt(CreateMaterializedViewStmtContext ctx) {
        return StatementType.CREATE_VIEW;
    }

    @Override
    public StatementType visitDropTableStmt(DropTableStmtContext ctx) {
        if (ctx.TABLE() != null) {
            return StatementType.DROP_TABLE;
        } else if (ctx.VIEW() != null) {
            return StatementType.DROP_VIEW;
        }

        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitDropDatabaseStmt(DropDatabaseStmtContext ctx) {
        return StatementType.DROP_SCHEMA;
    }

    @Override
    public StatementType visitCreateDatabaseStmt(CreateDatabaseStmtContext ctx) {
        return StatementType.CREATE_SCHEMA;
    }

    @Override
    public StatementType visitAlterTableStmt(AlterTableStmtContext ctx) {
        if (ctx.alterTableClause().size() > 1) {
            for (AlterTableClauseContext alterTableClauseContext : ctx.alterTableClause()) {
                if (alterTableClauseContext instanceof AlterTableClauseUpdateContext || alterTableClauseContext instanceof AlterTableClauseDeleteContext) {
                    return StatementType.UNKNOWN;
                }
            }
        } else if (ctx.alterTableClause().get(0) instanceof AlterTableClauseDeleteContext) {
            return StatementType.DELETE;
        } else if (ctx.alterTableClause().get(0) instanceof AlterTableClauseUpdateContext) {
            return StatementType.UPDATE;
        }
        return StatementType.ALTER_TABLE;
    }

    @Override
    public StatementType visitShowCreateTableStmt(ShowCreateTableStmtContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowCreateDatabaseStmt(ShowCreateDatabaseStmtContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowEnginesStmt(ShowEnginesStmtContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowQuotasStmt(ShowQuotasStmtContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowQuotaStmt(ShowQuotaStmtContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowRolesStmt(ShowRolesStmtContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitTruncateStmt(TruncateStmtContext ctx) {
        return StatementType.TRUNCATE_TABLE;
    }

    @Override
    public StatementType visitUseStmt(UseStmtContext ctx) {
        return StatementType.SWITCH_SCHEMA;
    }

    @Override
    public StatementType visitSetStmt(SetStmtContext ctx) {
        return StatementType.SESSION_SETTING_WRITE;
    }

    @Override
    public StatementType visitShowUsersStmt(ShowUsersStmtContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowProfilesStmt(ShowProfilesStmtContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowPoliciesStmt(ShowPoliciesStmtContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowCreateQuotaStmt(ShowCreateQuotaStmtContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowAccessStmt(ShowAccessStmtContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowClusterStmt(ShowClusterStmtContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowClustersStmt(ShowClustersStmtContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowFilesystemCaches(ShowFilesystemCachesContext ctx) {
        return StatementType.PERFORMANCE;
    }

    @Override
    public StatementType visitShowFunctionsStmt(ShowFunctionsStmtContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowMergesStmt(ShowMergesStmtContext ctx) {
        return StatementType.PERFORMANCE;
    }

    @Override
    public StatementType visitShowPrivilegesStmt(ShowPrivilegesStmtContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitOptimizeStmt(OptimizeStmtContext ctx) {
        return StatementType.ADMIN_TABLE;
    }

    @Override
    public StatementType visitRenameEntityClause(RenameEntityClauseContext ctx) {
        if (ctx.TABLE() != null) {
            return StatementType.RENAME_TABLE;
        } else if (ctx.DATABASE() != null) {
            return StatementType.RENAME_SCHEMA;
        }
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitCreateTableStmt(CreateTableStmtContext ctx) {
        return StatementType.CREATE_TABLE;
    }

    @Override
    public StatementType visitQueryStmtUpdate(QueryStmtUpdateContext ctx) {
        return StatementType.UPDATE;
    }

    @Override
    public StatementType visitSelectUnionStmt(SelectUnionStmtContext ctx) {
        return StatementType.SELECT;
    }
}
