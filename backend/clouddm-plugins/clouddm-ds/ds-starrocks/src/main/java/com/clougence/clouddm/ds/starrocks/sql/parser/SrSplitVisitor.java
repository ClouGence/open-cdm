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
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public class SrSplitVisitor extends StarRocksBaseVisitor<SecQueryType> {

    @Override
    public SecQueryType visitShowCreateTableStatement(ShowCreateTableStatementContext ctx) {
        return SecQueryType.UNKNOWN;
    }

    @Override
    public SecQueryType visitShowDeleteStatement(ShowDeleteStatementContext ctx) {
        return SecQueryType.UNKNOWN;
    }

    @Override
    public SecQueryType visitShowTableStatement(ShowTableStatementContext ctx) {
        return SecQueryType.UNKNOWN;
    }

    @Override
    public SecQueryType visitAlterMaterializedViewStatement(AlterMaterializedViewStatementContext ctx) {
        return SecQueryType.ALTER_VIEW;
    }

    @Override
    public SecQueryType visitShowDataStmt(ShowDataStmtContext ctx) {
        return SecQueryType.PERFORMANCE;
    }

    @Override
    public SecQueryType visitShowBrokerStatement(ShowBrokerStatementContext ctx) {
        return SecQueryType.UNKNOWN;
    }

    @Override
    public SecQueryType visitShowComputeNodesStatement(ShowComputeNodesStatementContext ctx) {
        return SecQueryType.UNKNOWN;
    }

    @Override
    public SecQueryType visitShowFrontendsStatement(ShowFrontendsStatementContext ctx) {
        return SecQueryType.UNKNOWN;
    }

    @Override
    public SecQueryType visitShowRunningQueriesStatement(ShowRunningQueriesStatementContext ctx) {
        return SecQueryType.PERFORMANCE;
    }

    @Override
    public SecQueryType visitShowDatabasesStatement(ShowDatabasesStatementContext ctx) {
        return SecQueryType.UNKNOWN;
    }

    @Override
    public SecQueryType visitShowWarningStatement(ShowWarningStatementContext ctx) {
        return SecQueryType.UNKNOWN;
    }

    @Override
    public SecQueryType visitShowVariablesStatement(ShowVariablesStatementContext ctx) {
        if (ctx.varType() == null || ctx.varType().LOCAL() != null || ctx.varType().SESSION() != null) {
            return SecQueryType.SESSION_VARIABLE_RW;
        }
        return SecQueryType.UNKNOWN;
    }

    @Override
    public SecQueryType visitShowAnalyzeStatement(ShowAnalyzeStatementContext ctx) {
        return SecQueryType.PERFORMANCE;
    }

    @Override
    public SecQueryType visitShowProcesslistStatement(ShowProcesslistStatementContext ctx) {
        return SecQueryType.PERFORMANCE;
    }

    @Override
    public SecQueryType visitShowCreateDbStatement(ShowCreateDbStatementContext ctx) {
        return SecQueryType.UNKNOWN;
    }

    @Override
    public SecQueryType visitShowDictionaryStatement(ShowDictionaryStatementContext ctx) {
        return SecQueryType.UNKNOWN;
    }

    @Override
    public SecQueryType visitShowFunctionsStatement(ShowFunctionsStatementContext ctx) {
        return SecQueryType.UNKNOWN;
    }

    @Override
    public SecQueryType visitAlterViewStatement(AlterViewStatementContext ctx) {
        return SecQueryType.ALTER_VIEW;
    }

    @Override
    public SecQueryType visitShowMaterializedViewsStatement(ShowMaterializedViewsStatementContext ctx) {
        return SecQueryType.UNKNOWN;
    }

    @Override
    public SecQueryType visitShowIndexStatement(ShowIndexStatementContext ctx) {
        return SecQueryType.UNKNOWN;
    }

    @Override
    public SecQueryType visitShowColumnStatement(ShowColumnStatementContext ctx) {
        return SecQueryType.UNKNOWN;
    }

    @Override
    public SecQueryType visitShowAlterStatement(ShowAlterStatementContext ctx) {
        return SecQueryType.UNKNOWN;
    }

    @Override
    public SecQueryType visitAnalyzeStatement(AnalyzeStatementContext ctx) {
        return SecQueryType.ADMIN_TABLE;
    }

    @Override
    public SecQueryType visitDescTableStatement(DescTableStatementContext ctx) {
        return SecQueryType.UNKNOWN;
    }

    public static SrSplitVisitor INSTANCE = new SrSplitVisitor();

    @Override
    public SecQueryType visitShowCatalogsStatement(ShowCatalogsStatementContext ctx) {
        return SecQueryType.UNKNOWN;
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
        return ctx.OVERWRITE() == null ? SecQueryType.INSERT : SecQueryType.MERGE;
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
        return SecQueryType.UNKNOWN;
    }

    @Override
    public SecQueryType visitShowCreateExternalCatalogStatement(ShowCreateExternalCatalogStatementContext ctx) {
        return SecQueryType.UNKNOWN;
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
        return SecQueryType.CREATE_TABLE;
    }

    @Override
    public SecQueryType visitTruncateTableStatement(TruncateTableStatementContext ctx) {
        return SecQueryType.TRUNCATE_TABLE;
    }

    @Override
    public SecQueryType visitDropTableStatement(DropTableStatementContext ctx) {
        return SecQueryType.DROP_TABLE;
    }

    @Override
    public SecQueryType visitCancelAlterTableStatement(CancelAlterTableStatementContext ctx) {
        return SecQueryType.ADMIN_TABLE;
    }

    @Override
    public SecQueryType visitDropDbStatement(DropDbStatementContext ctx) {
        return SecQueryType.DROP_SCHEMA;
    }

    @Override
    public SecQueryType visitCreateFunctionStatement(CreateFunctionStatementContext ctx) {
        return SecQueryType.CREATE_PROG_OBJ;
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
        return SecQueryType.UNKNOWN;
    }

    @Override
    public SecQueryType visitShowUserStatement(ShowUserStatementContext ctx) {
        return SecQueryType.UNKNOWN;
    }

    @Override
    public SecQueryType visitShowGrantsStatement(ShowGrantsStatementContext ctx) {
        return SecQueryType.UNKNOWN;
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
        return SecQueryType.ADD_INDEX;
    }

    @Override
    public SecQueryType visitCreateTableAsSelectStatement(CreateTableAsSelectStatementContext ctx) {
        return SecQueryType.CREATE_TABLE;
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
        return SecQueryType.SESSION_VARIABLE_RW;
    }

    @Override
    public SecQueryType visitSetSystemVar(SetSystemVarContext ctx) {
        VarTypeContext varType = ctx.varType();
        if (varType == null && ctx.systemVariable() != null) {
            varType = ctx.systemVariable().varType();
        }
        return varType != null && varType.GLOBAL() != null ? SecQueryType.SYSTEM_SETTING_WRITE : SecQueryType.SESSION_SETTING_WRITE;
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
        return SecQueryType.DROP_PROG_OBJ;
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
