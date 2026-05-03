package com.clougence.clouddm.ds.starrocks.analysis;

import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.ds.starrocks.parser.antlr.StarRocksBaseVisitor;
import com.clougence.clouddm.ds.starrocks.parser.antlr.StarRocksParser.*;

public class SrSplitVisitor extends StarRocksBaseVisitor<SecQueryType> {

    @Override
    public SecQueryType visitShowCreateTableStatement(ShowCreateTableStatementContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowDeleteStatement(ShowDeleteStatementContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowTableStatement(ShowTableStatementContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitAlterMaterializedViewStatement(AlterMaterializedViewStatementContext ctx) {
        return SecQueryType.ALTER_VIEW;
    }

    @Override
    public SecQueryType visitShowDataStmt(ShowDataStmtContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowBrokerStatement(ShowBrokerStatementContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowComputeNodesStatement(ShowComputeNodesStatementContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowFrontendsStatement(ShowFrontendsStatementContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowRunningQueriesStatement(ShowRunningQueriesStatementContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowDatabasesStatement(ShowDatabasesStatementContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowWarningStatement(ShowWarningStatementContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowVariablesStatement(ShowVariablesStatementContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowAnalyzeStatement(ShowAnalyzeStatementContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowProcesslistStatement(ShowProcesslistStatementContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowCreateDbStatement(ShowCreateDbStatementContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowDictionaryStatement(ShowDictionaryStatementContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowFunctionsStatement(ShowFunctionsStatementContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitAlterViewStatement(AlterViewStatementContext ctx) {
        return SecQueryType.ALTER_VIEW;
    }

    @Override
    public SecQueryType visitShowMaterializedViewsStatement(ShowMaterializedViewsStatementContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowIndexStatement(ShowIndexStatementContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowColumnStatement(ShowColumnStatementContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowAlterStatement(ShowAlterStatementContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitAnalyzeStatement(AnalyzeStatementContext ctx) {
        return SecQueryType.ANALYZE;
    }

    @Override
    public SecQueryType visitDescTableStatement(DescTableStatementContext ctx) {
        return SecQueryType.SHOW;
    }

    public static SrSplitVisitor INSTANCE = new SrSplitVisitor();

    @Override
    public SecQueryType visitShowCatalogsStatement(ShowCatalogsStatementContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitQueryStatement(QueryStatementContext ctx) {
        return SecQueryType.SELECT;
    }

    @Override
    public SecQueryType visitUseDatabaseStatement(UseDatabaseStatementContext ctx) {
        return SecQueryType.SWITCH_SCHEMA;
    }

    @Override
    public SecQueryType visitInsertStatement(InsertStatementContext ctx) {
        return SecQueryType.INSERT;
    }

    @Override
    public SecQueryType visitDeleteStatement(DeleteStatementContext ctx) {
        return SecQueryType.DELETE;
    }

    @Override
    public SecQueryType visitCreateExternalCatalogStatement(CreateExternalCatalogStatementContext ctx) {
        return SecQueryType.CREATE_CATALOG;
    }

    @Override
    public SecQueryType visitShowPartitionsStatement(ShowPartitionsStatementContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowCreateExternalCatalogStatement(ShowCreateExternalCatalogStatementContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitDropExternalCatalogStatement(DropExternalCatalogStatementContext ctx) {
        return SecQueryType.DROP_CATALOG;
    }

    @Override
    public SecQueryType visitAlterCatalogStatement(AlterCatalogStatementContext ctx) {
        return SecQueryType.ALTER_CATALOG;
    }

    @Override
    public SecQueryType visitCreateDbStatement(CreateDbStatementContext ctx) {
        return SecQueryType.CREATE_SCHEMA;
    }

    @Override
    public SecQueryType visitCreateTableStatement(CreateTableStatementContext ctx) {
        return SecQueryType.CREATE_TABLE;
    }

    @Override
    public SecQueryType visitCreateTableLikeStatement(CreateTableLikeStatementContext ctx) {
        return SecQueryType.CREATE_TABLE_LIKE;
    }

    @Override
    public SecQueryType visitTruncateTableStatement(TruncateTableStatementContext ctx) {
        return SecQueryType.TRUNCATE;
    }

    @Override
    public SecQueryType visitDropTableStatement(DropTableStatementContext ctx) {
        return SecQueryType.DROP_TABLE;
    }

    @Override
    public SecQueryType visitCancelAlterTableStatement(CancelAlterTableStatementContext ctx) {
        return SecQueryType.ALTER_TABLE;
    }

    @Override
    public SecQueryType visitDropDbStatement(DropDbStatementContext ctx) {
        return SecQueryType.DROP_SCHEMA;
    }

    @Override
    public SecQueryType visitCreateFunctionStatement(CreateFunctionStatementContext ctx) {
        return SecQueryType.CREATE_FUNCTION;
    }

    @Override
    public SecQueryType visitCreateMaterializedViewStatement(CreateMaterializedViewStatementContext ctx) {
        return SecQueryType.CREATE_VIEW;
    }

    @Override
    public SecQueryType visitCreateUserStatement(CreateUserStatementContext ctx) {
        return SecQueryType.CREATE_USER;
    }

    @Override
    public SecQueryType visitCreateRoleStatement(CreateRoleStatementContext ctx) {
        return SecQueryType.CREATE_ROLE;
    }

    @Override
    public SecQueryType visitGrantOnTableBrief(GrantOnTableBriefContext ctx) {
        return SecQueryType.GRANT;
    }

    @Override
    public SecQueryType visitRevokeOnTableBrief(RevokeOnTableBriefContext ctx) {
        return SecQueryType.REVOKE;
    }

    @Override
    public SecQueryType visitShowRolesStatement(ShowRolesStatementContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowUserStatement(ShowUserStatementContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowGrantsStatement(ShowGrantsStatementContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitAlterDbQuotaStatement(AlterDbQuotaStatementContext ctx) {
        return SecQueryType.ALTER_SCHEMA;
    }

    @Override
    public SecQueryType visitUpdateStatement(UpdateStatementContext ctx) {
        return SecQueryType.UPDATE;
    }

    @Override
    public SecQueryType visitDropIndexStatement(DropIndexStatementContext ctx) {
        return SecQueryType.DROP_INDEX;
    }

    @Override
    public SecQueryType visitCreateIndexStatement(CreateIndexStatementContext ctx) {
        return SecQueryType.CREATE_INDEX;
    }

    @Override
    public SecQueryType visitCreateTableAsSelectStatement(CreateTableAsSelectStatementContext ctx) {
        return SecQueryType.CREATE_TABLE_LIKE;
    }

    @Override
    public SecQueryType visitAlterTableStatement(AlterTableStatementContext ctx) {
        return SecQueryType.ALTER_TABLE;
    }

    @Override
    public SecQueryType visitAlterDatabaseRenameStatement(AlterDatabaseRenameStatementContext ctx) {
        return SecQueryType.RENAME_SCHEMA;
    }

    @Override
    public SecQueryType visitSetUserVar(SetUserVarContext ctx) {
        return SecQueryType.CONFIG_WRITE;
    }

    @Override
    public SecQueryType visitSetSystemVar(SetSystemVarContext ctx) {
        return SecQueryType.CONFIG_WRITE;
    }

    @Override
    public SecQueryType visitDropUserStatement(DropUserStatementContext ctx) {
        return SecQueryType.DROP_USER;
    }

    @Override
    public SecQueryType visitDropRoleStatement(DropRoleStatementContext ctx) {
        return SecQueryType.DROP_ROLE;
    }

    @Override
    public SecQueryType visitCreateViewStatement(CreateViewStatementContext ctx) {
        return SecQueryType.CREATE_VIEW;
    }

    @Override
    public SecQueryType visitDropFunctionStatement(DropFunctionStatementContext ctx) {
        return SecQueryType.DROP_FUNCTION;
    }

    @Override
    public SecQueryType visitDropMaterializedViewStatement(DropMaterializedViewStatementContext ctx) {
        return SecQueryType.DROP_VIEW;
    }

    @Override
    public SecQueryType visitDropViewStatement(DropViewStatementContext ctx) {
        return SecQueryType.DROP_VIEW;
    }
}
