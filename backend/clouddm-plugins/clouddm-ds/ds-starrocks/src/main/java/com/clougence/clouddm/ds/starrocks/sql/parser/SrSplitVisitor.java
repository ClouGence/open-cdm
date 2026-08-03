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
package com.clougence.clouddm.ds.starrocks.sql.parser;

import com.clougence.clouddm.ds.starrocks.sql.parser.antlr.StarRocksBaseVisitor;
import com.clougence.clouddm.ds.starrocks.sql.parser.antlr.StarRocksParser.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;

public class SrSplitVisitor extends StarRocksBaseVisitor<SplitQueryType> {

    @Override
    public SplitQueryType visitShowCreateTableStatement(ShowCreateTableStatementContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowDeleteStatement(ShowDeleteStatementContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowTableStatement(ShowTableStatementContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitAlterMaterializedViewStatement(AlterMaterializedViewStatementContext ctx) {
        return SplitQueryType.ALTER_VIEW;
    }

    @Override
    public SplitQueryType visitShowDataStmt(ShowDataStmtContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitShowBrokerStatement(ShowBrokerStatementContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowComputeNodesStatement(ShowComputeNodesStatementContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowFrontendsStatement(ShowFrontendsStatementContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowRunningQueriesStatement(ShowRunningQueriesStatementContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitShowDatabasesStatement(ShowDatabasesStatementContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowWarningStatement(ShowWarningStatementContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowVariablesStatement(ShowVariablesStatementContext ctx) {
        if (ctx.varType() == null || ctx.varType().LOCAL() != null || ctx.varType().SESSION() != null) {
            return SplitQueryType.SESSION_VARIABLE_RW;
        }
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowAnalyzeStatement(ShowAnalyzeStatementContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitShowProcesslistStatement(ShowProcesslistStatementContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitShowCreateDbStatement(ShowCreateDbStatementContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowDictionaryStatement(ShowDictionaryStatementContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowFunctionsStatement(ShowFunctionsStatementContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitAlterViewStatement(AlterViewStatementContext ctx) {
        return SplitQueryType.ALTER_VIEW;
    }

    @Override
    public SplitQueryType visitShowMaterializedViewsStatement(ShowMaterializedViewsStatementContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowIndexStatement(ShowIndexStatementContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowColumnStatement(ShowColumnStatementContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowAlterStatement(ShowAlterStatementContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitAnalyzeStatement(AnalyzeStatementContext ctx) {
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitDescTableStatement(DescTableStatementContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    public static SrSplitVisitor INSTANCE = new SrSplitVisitor();

    @Override
    public SplitQueryType visitShowCatalogsStatement(ShowCatalogsStatementContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitQueryStatement(QueryStatementContext ctx) {
        return SplitQueryType.SELECT;
    }

    @Override
    public SplitQueryType visitUseDatabaseStatement(UseDatabaseStatementContext ctx) {
        return SplitQueryType.SWITCH_SCHEMA;
    }

    @Override
    public SplitQueryType visitInsertStatement(InsertStatementContext ctx) {
        return ctx.OVERWRITE() == null ? SplitQueryType.INSERT : SplitQueryType.MERGE;
    }

    @Override
    public SplitQueryType visitDeleteStatement(DeleteStatementContext ctx) {
        return SplitQueryType.DELETE;
    }

    @Override
    public SplitQueryType visitCreateExternalCatalogStatement(CreateExternalCatalogStatementContext ctx) {
        return SplitQueryType.CREATE_CATALOG;
    }

    @Override
    public SplitQueryType visitShowPartitionsStatement(ShowPartitionsStatementContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowCreateExternalCatalogStatement(ShowCreateExternalCatalogStatementContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitDropExternalCatalogStatement(DropExternalCatalogStatementContext ctx) {
        return SplitQueryType.DROP_CATALOG;
    }

    @Override
    public SplitQueryType visitAlterCatalogStatement(AlterCatalogStatementContext ctx) {
        return SplitQueryType.ALTER_CATALOG;
    }

    @Override
    public SplitQueryType visitCreateDbStatement(CreateDbStatementContext ctx) {
        return SplitQueryType.CREATE_SCHEMA;
    }

    @Override
    public SplitQueryType visitCreateTableStatement(CreateTableStatementContext ctx) {
        return SplitQueryType.CREATE_TABLE;
    }

    @Override
    public SplitQueryType visitCreateTableLikeStatement(CreateTableLikeStatementContext ctx) {
        return SplitQueryType.CREATE_TABLE;
    }

    @Override
    public SplitQueryType visitTruncateTableStatement(TruncateTableStatementContext ctx) {
        return SplitQueryType.TRUNCATE_TABLE;
    }

    @Override
    public SplitQueryType visitDropTableStatement(DropTableStatementContext ctx) {
        return SplitQueryType.DROP_TABLE;
    }

    @Override
    public SplitQueryType visitCancelAlterTableStatement(CancelAlterTableStatementContext ctx) {
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitDropDbStatement(DropDbStatementContext ctx) {
        return SplitQueryType.DROP_SCHEMA;
    }

    @Override
    public SplitQueryType visitCreateFunctionStatement(CreateFunctionStatementContext ctx) {
        return SplitQueryType.CREATE_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitCreateMaterializedViewStatement(CreateMaterializedViewStatementContext ctx) {
        return SplitQueryType.CREATE_VIEW;
    }

    @Override
    public SplitQueryType visitCreateUserStatement(CreateUserStatementContext ctx) {
        return SplitQueryType.CREATE_USER;
    }

    @Override
    public SplitQueryType visitCreateRoleStatement(CreateRoleStatementContext ctx) {
        return SplitQueryType.CREATE_ROLE;
    }

    @Override
    public SplitQueryType visitGrantOnTableBrief(GrantOnTableBriefContext ctx) {
        return SplitQueryType.GRANT;
    }

    @Override
    public SplitQueryType visitRevokeOnTableBrief(RevokeOnTableBriefContext ctx) {
        return SplitQueryType.REVOKE;
    }

    @Override
    public SplitQueryType visitShowRolesStatement(ShowRolesStatementContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowUserStatement(ShowUserStatementContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitShowGrantsStatement(ShowGrantsStatementContext ctx) {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitAlterDbQuotaStatement(AlterDbQuotaStatementContext ctx) {
        return SplitQueryType.ALTER_SCHEMA;
    }

    @Override
    public SplitQueryType visitUpdateStatement(UpdateStatementContext ctx) {
        return SplitQueryType.UPDATE;
    }

    @Override
    public SplitQueryType visitDropIndexStatement(DropIndexStatementContext ctx) {
        return SplitQueryType.DROP_INDEX;
    }

    @Override
    public SplitQueryType visitCreateIndexStatement(CreateIndexStatementContext ctx) {
        return SplitQueryType.ADD_INDEX;
    }

    @Override
    public SplitQueryType visitCreateTableAsSelectStatement(CreateTableAsSelectStatementContext ctx) {
        return SplitQueryType.CREATE_TABLE;
    }

    @Override
    public SplitQueryType visitAlterTableStatement(AlterTableStatementContext ctx) {
        return SplitQueryType.ALTER_TABLE;
    }

    @Override
    public SplitQueryType visitAlterDatabaseRenameStatement(AlterDatabaseRenameStatementContext ctx) {
        return SplitQueryType.RENAME_SCHEMA;
    }

    @Override
    public SplitQueryType visitSetUserVar(SetUserVarContext ctx) {
        return SplitQueryType.SESSION_VARIABLE_RW;
    }

    @Override
    public SplitQueryType visitSetSystemVar(SetSystemVarContext ctx) {
        VarTypeContext varType = ctx.varType();
        if (varType == null && ctx.systemVariable() != null) {
            varType = ctx.systemVariable().varType();
        }
        return varType != null && varType.GLOBAL() != null ? SplitQueryType.SYSTEM_SETTING_WRITE : SplitQueryType.SESSION_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitDropUserStatement(DropUserStatementContext ctx) {
        return SplitQueryType.DROP_USER;
    }

    @Override
    public SplitQueryType visitDropRoleStatement(DropRoleStatementContext ctx) {
        return SplitQueryType.DROP_ROLE;
    }

    @Override
    public SplitQueryType visitCreateViewStatement(CreateViewStatementContext ctx) {
        return SplitQueryType.CREATE_VIEW;
    }

    @Override
    public SplitQueryType visitDropFunctionStatement(DropFunctionStatementContext ctx) {
        return SplitQueryType.DROP_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitDropMaterializedViewStatement(DropMaterializedViewStatementContext ctx) {
        return SplitQueryType.DROP_VIEW;
    }

    @Override
    public SplitQueryType visitDropViewStatement(DropViewStatementContext ctx) {
        return SplitQueryType.DROP_VIEW;
    }
}
