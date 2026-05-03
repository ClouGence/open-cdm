package com.clougence.clouddm.ds.clickhouse.analysis;

import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.ds.clickhouse.parser.antlr.ClickHouseParserBaseVisitor;
import com.clougence.clouddm.ds.clickhouse.parser.antlr.ClickHouseParser.*;

public class ChSplitVisitor extends ClickHouseParserBaseVisitor<SecQueryType> {

    public static ChSplitVisitor INSTANCE = new ChSplitVisitor();

    @Override
    public SecQueryType visitQueryStmtInsert(QueryStmtInsertContext ctx) {
        return SecQueryType.INSERT;
    }

    @Override
    public SecQueryType visitQueryStmtDelete(QueryStmtDeleteContext ctx) {
        return SecQueryType.DELETE;
    }

    @Override
    public SecQueryType visitShowTablesStmt(ShowTablesStmtContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitCreateViewStmt(CreateViewStmtContext ctx) {
        return SecQueryType.CREATE_VIEW;
    }

    @Override
    public SecQueryType visitDropTableStmt(DropTableStmtContext ctx) {
        if (ctx.TABLE() != null) {
            return SecQueryType.DROP_TABLE;
        } else if (ctx.VIEW() != null) {
            return SecQueryType.DROP_VIEW;
        }

        return SecQueryType.UNKNOWN;
    }

    @Override
    public SecQueryType visitDropDatabaseStmt(DropDatabaseStmtContext ctx) {
        return SecQueryType.DROP_SCHEMA;
    }

    @Override
    public SecQueryType visitCreateDatabaseStmt(CreateDatabaseStmtContext ctx) {
        return SecQueryType.CREATE_SCHEMA;
    }

    @Override
    public SecQueryType visitAlterTableStmt(AlterTableStmtContext ctx) {
        if (ctx.alterTableClause().size() > 1) {
            for (AlterTableClauseContext alterTableClauseContext : ctx.alterTableClause()) {
                if (alterTableClauseContext instanceof AlterTableClauseUpdateContext || alterTableClauseContext instanceof AlterTableClauseDeleteContext) {
                    return SecQueryType.UNKNOWN;
                }
            }
        } else if (ctx.alterTableClause().get(0) instanceof AlterTableClauseDeleteContext) {
            return SecQueryType.DELETE;
        } else if (ctx.alterTableClause().get(0) instanceof AlterTableClauseUpdateContext) {
            return SecQueryType.UPDATE;
        }
        return SecQueryType.ALTER_TABLE;
    }

    @Override
    public SecQueryType visitShowCreateTableStmt(ShowCreateTableStmtContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowCreateDatabaseStmt(ShowCreateDatabaseStmtContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowEnginesStmt(ShowEnginesStmtContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowQuotasStmt(ShowQuotasStmtContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowQuotaStmt(ShowQuotaStmtContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowRolesStmt(ShowRolesStmtContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitTruncateStmt(TruncateStmtContext ctx) {
        return SecQueryType.TRUNCATE;
    }

    @Override
    public SecQueryType visitUseStmt(UseStmtContext ctx) {
        return SecQueryType.SWITCH_SCHEMA;
    }

    @Override
    public SecQueryType visitSetStmt(SetStmtContext ctx) {
        return SecQueryType.CONFIG_WRITE;
    }

    @Override
    public SecQueryType visitShowUsersStmt(ShowUsersStmtContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowProfilesStmt(ShowProfilesStmtContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowPoliciesStmt(ShowPoliciesStmtContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowCreateQuotaStmt(ShowCreateQuotaStmtContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowAccessStmt(ShowAccessStmtContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowClusterStmt(ShowClusterStmtContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowClustersStmt(ShowClustersStmtContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowFilesystemCaches(ShowFilesystemCachesContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowFunctionsStmt(ShowFunctionsStmtContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowMergesStmt(ShowMergesStmtContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowPrivilegesStmt(ShowPrivilegesStmtContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitOptimizeStmt(OptimizeStmtContext ctx) {
        return SecQueryType.ALTER_TABLE;
    }

    @Override
    public SecQueryType visitRenameEntityClause(RenameEntityClauseContext ctx) {
        if (ctx.TABLE() != null) {
            return SecQueryType.RENAME_TABLE;
        } else if (ctx.DATABASE() != null) {
            return SecQueryType.RENAME_SCHEMA;
        }
        return SecQueryType.UNKNOWN;
    }

    @Override
    public SecQueryType visitCreateTableStmt(CreateTableStmtContext ctx) {
        return SecQueryType.CREATE_TABLE;
    }

    @Override
    public SecQueryType visitQueryStmtUpdate(QueryStmtUpdateContext ctx) {
        return SecQueryType.UPDATE;
    }

    @Override
    public SecQueryType visitSelectUnionStmt(SelectUnionStmtContext ctx) {
        return SecQueryType.SELECT;
    }
}
