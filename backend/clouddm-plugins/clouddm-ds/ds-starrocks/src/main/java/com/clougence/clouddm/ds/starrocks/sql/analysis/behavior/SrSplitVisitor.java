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
package com.clougence.clouddm.ds.starrocks.sql.analysis.behavior;

import com.clougence.clouddm.ds.starrocks.sql.parser.antlr.StarRocksBaseVisitor;
import com.clougence.clouddm.ds.starrocks.sql.parser.antlr.StarRocksParser.*;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementType;

public class SrSplitVisitor extends StarRocksBaseVisitor<StatementType> {

    @Override
    public StatementType visitShowCreateTableStatement(ShowCreateTableStatementContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowDeleteStatement(ShowDeleteStatementContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowTableStatement(ShowTableStatementContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitAlterMaterializedViewStatement(AlterMaterializedViewStatementContext ctx) {
        return StatementType.ALTER_VIEW;
    }

    @Override
    public StatementType visitShowDataStmt(ShowDataStmtContext ctx) {
        return StatementType.PERFORMANCE;
    }

    @Override
    public StatementType visitShowBrokerStatement(ShowBrokerStatementContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowComputeNodesStatement(ShowComputeNodesStatementContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowFrontendsStatement(ShowFrontendsStatementContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowRunningQueriesStatement(ShowRunningQueriesStatementContext ctx) {
        return StatementType.PERFORMANCE;
    }

    @Override
    public StatementType visitShowDatabasesStatement(ShowDatabasesStatementContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowWarningStatement(ShowWarningStatementContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowVariablesStatement(ShowVariablesStatementContext ctx) {
        if (ctx.varType() == null || ctx.varType().LOCAL() != null || ctx.varType().SESSION() != null) {
            return StatementType.SESSION_VARIABLE_RW;
        }
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowAnalyzeStatement(ShowAnalyzeStatementContext ctx) {
        return StatementType.PERFORMANCE;
    }

    @Override
    public StatementType visitShowProcesslistStatement(ShowProcesslistStatementContext ctx) {
        return StatementType.PERFORMANCE;
    }

    @Override
    public StatementType visitShowCreateDbStatement(ShowCreateDbStatementContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowDictionaryStatement(ShowDictionaryStatementContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowFunctionsStatement(ShowFunctionsStatementContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitAlterViewStatement(AlterViewStatementContext ctx) {
        return StatementType.ALTER_VIEW;
    }

    @Override
    public StatementType visitShowMaterializedViewsStatement(ShowMaterializedViewsStatementContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowIndexStatement(ShowIndexStatementContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowColumnStatement(ShowColumnStatementContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowAlterStatement(ShowAlterStatementContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitAnalyzeStatement(AnalyzeStatementContext ctx) {
        return StatementType.ADMIN_TABLE;
    }

    @Override
    public StatementType visitDescTableStatement(DescTableStatementContext ctx) {
        return StatementType.UNKNOWN;
    }

    public static SrSplitVisitor INSTANCE = new SrSplitVisitor();

    @Override
    public StatementType visitShowCatalogsStatement(ShowCatalogsStatementContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitQueryStatement(QueryStatementContext ctx) {
        if (ctx.explainDesc() != null) {
            return explainType(ctx.explainDesc());
        }
        return StatementType.SELECT;
    }

    @Override
    public StatementType visitUseDatabaseStatement(UseDatabaseStatementContext ctx) {
        return StatementType.SWITCH_SCHEMA;
    }

    @Override
    public StatementType visitInsertStatement(InsertStatementContext ctx) {
        if (ctx.explainDesc() != null) {
            return explainType(ctx.explainDesc());
        }
        return ctx.OVERWRITE() == null ? StatementType.INSERT : StatementType.MERGE;
    }

    @Override
    public StatementType visitDeleteStatement(DeleteStatementContext ctx) {
        if (ctx.explainDesc() != null) {
            return explainType(ctx.explainDesc());
        }
        return StatementType.DELETE;
    }

    @Override
    public StatementType visitCreateExternalCatalogStatement(CreateExternalCatalogStatementContext ctx) {
        return StatementType.CREATE_CATALOG;
    }

    @Override
    public StatementType visitShowPartitionsStatement(ShowPartitionsStatementContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowCreateExternalCatalogStatement(ShowCreateExternalCatalogStatementContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitDropExternalCatalogStatement(DropExternalCatalogStatementContext ctx) {
        return StatementType.DROP_CATALOG;
    }

    @Override
    public StatementType visitAlterCatalogStatement(AlterCatalogStatementContext ctx) {
        return StatementType.ALTER_CATALOG;
    }

    @Override
    public StatementType visitCreateDbStatement(CreateDbStatementContext ctx) {
        return StatementType.CREATE_SCHEMA;
    }

    @Override
    public StatementType visitCreateTableStatement(CreateTableStatementContext ctx) {
        return StatementType.CREATE_TABLE;
    }

    @Override
    public StatementType visitCreateTableLikeStatement(CreateTableLikeStatementContext ctx) {
        return StatementType.CREATE_TABLE;
    }

    @Override
    public StatementType visitTruncateTableStatement(TruncateTableStatementContext ctx) {
        return StatementType.TRUNCATE_TABLE;
    }

    @Override
    public StatementType visitDropTableStatement(DropTableStatementContext ctx) {
        return StatementType.DROP_TABLE;
    }

    @Override
    public StatementType visitCancelAlterTableStatement(CancelAlterTableStatementContext ctx) {
        return StatementType.ADMIN_TABLE;
    }

    @Override
    public StatementType visitDropDbStatement(DropDbStatementContext ctx) {
        return StatementType.DROP_SCHEMA;
    }

    @Override
    public StatementType visitCreateFunctionStatement(CreateFunctionStatementContext ctx) {
        return StatementType.CREATE_PROG_OBJ;
    }

    @Override
    public StatementType visitCreateMaterializedViewStatement(CreateMaterializedViewStatementContext ctx) {
        return StatementType.CREATE_VIEW;
    }

    @Override
    public StatementType visitCreateUserStatement(CreateUserStatementContext ctx) {
        return StatementType.CREATE_USER;
    }

    @Override
    public StatementType visitCreateRoleStatement(CreateRoleStatementContext ctx) {
        return StatementType.CREATE_ROLE;
    }

    @Override
    public StatementType visitGrantOnTableBrief(GrantOnTableBriefContext ctx) {
        return StatementType.GRANT;
    }

    @Override
    public StatementType visitRevokeOnTableBrief(RevokeOnTableBriefContext ctx) {
        return StatementType.REVOKE;
    }

    @Override
    public StatementType visitShowRolesStatement(ShowRolesStatementContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowUserStatement(ShowUserStatementContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitShowGrantsStatement(ShowGrantsStatementContext ctx) {
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitAlterDbQuotaStatement(AlterDbQuotaStatementContext ctx) {
        return StatementType.ALTER_SCHEMA;
    }

    @Override
    public StatementType visitUpdateStatement(UpdateStatementContext ctx) {
        if (ctx.explainDesc() != null) {
            return explainType(ctx.explainDesc());
        }
        return StatementType.UPDATE;
    }

    private static StatementType explainType(ExplainDescContext ctx) {
        if (ctx.ANALYZE() != null) {
            return StatementType.UNSAFE;
        }
        return StatementType.SELECT;
    }

    @Override
    public StatementType visitDropIndexStatement(DropIndexStatementContext ctx) {
        return StatementType.DROP_INDEX;
    }

    @Override
    public StatementType visitCreateIndexStatement(CreateIndexStatementContext ctx) {
        return StatementType.ADD_INDEX;
    }

    @Override
    public StatementType visitCreateTableAsSelectStatement(CreateTableAsSelectStatementContext ctx) {
        return StatementType.CREATE_TABLE;
    }

    @Override
    public StatementType visitAlterTableStatement(AlterTableStatementContext ctx) {
        return StatementType.ALTER_TABLE;
    }

    @Override
    public StatementType visitAlterDatabaseRenameStatement(AlterDatabaseRenameStatementContext ctx) {
        return StatementType.RENAME_SCHEMA;
    }

    @Override
    public StatementType visitSetUserVar(SetUserVarContext ctx) {
        return StatementType.SESSION_VARIABLE_RW;
    }

    @Override
    public StatementType visitSetSystemVar(SetSystemVarContext ctx) {
        VarTypeContext varType = ctx.varType();
        if (varType == null && ctx.systemVariable() != null) {
            varType = ctx.systemVariable().varType();
        }
        return varType != null && varType.GLOBAL() != null ? StatementType.SYSTEM_SETTING_WRITE : StatementType.SESSION_SETTING_WRITE;
    }

    @Override
    public StatementType visitDropUserStatement(DropUserStatementContext ctx) {
        return StatementType.DROP_USER;
    }

    @Override
    public StatementType visitDropRoleStatement(DropRoleStatementContext ctx) {
        return StatementType.DROP_ROLE;
    }

    @Override
    public StatementType visitCreateViewStatement(CreateViewStatementContext ctx) {
        return StatementType.CREATE_VIEW;
    }

    @Override
    public StatementType visitDropFunctionStatement(DropFunctionStatementContext ctx) {
        return StatementType.DROP_PROG_OBJ;
    }

    @Override
    public StatementType visitDropMaterializedViewStatement(DropMaterializedViewStatementContext ctx) {
        return StatementType.DROP_VIEW;
    }

    @Override
    public StatementType visitDropViewStatement(DropViewStatementContext ctx) {
        return StatementType.DROP_VIEW;
    }
}
