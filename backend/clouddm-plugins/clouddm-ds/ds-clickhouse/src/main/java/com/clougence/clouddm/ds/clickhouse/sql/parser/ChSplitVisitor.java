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
    public SplitQueryType visitExplainStmt(ExplainStmtContext ctx) {
        return SplitQueryType.SELECT;
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
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowQuotasStmt(ShowQuotasStmtContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowQuotaStmt(ShowQuotaStmtContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowRolesStmt(ShowRolesStmtContext ctx) {
        return SplitQueryType.UNKNOWN;
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
        return SplitQueryType.SESSION_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitShowUsersStmt(ShowUsersStmtContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowProfilesStmt(ShowProfilesStmtContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowPoliciesStmt(ShowPoliciesStmtContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowCreateQuotaStmt(ShowCreateQuotaStmtContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowAccessStmt(ShowAccessStmtContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowClusterStmt(ShowClusterStmtContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowClustersStmt(ShowClustersStmtContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowFilesystemCaches(ShowFilesystemCachesContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitShowFunctionsStmt(ShowFunctionsStmtContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowMergesStmt(ShowMergesStmtContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitShowPrivilegesStmt(ShowPrivilegesStmtContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitOptimizeStmt(OptimizeStmtContext ctx) {
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
}
