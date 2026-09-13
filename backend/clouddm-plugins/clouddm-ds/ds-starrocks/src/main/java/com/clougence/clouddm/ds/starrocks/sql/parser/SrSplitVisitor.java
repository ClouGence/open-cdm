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
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitShowVariablesStatement(ShowVariablesStatementContext ctx) {
        return SplitQueryType.METADATA;
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
        if (ctx.explainDesc() != null) {
            return explainType(ctx.explainDesc());
        }
        return SplitQueryType.SELECT;
    }

    @Override
    public SplitQueryType visitUseDatabaseStatement(UseDatabaseStatementContext ctx) {
        return SplitQueryType.SWITCH_SCHEMA;
    }

    @Override
    public SplitQueryType visitSetCatalogStatement(SetCatalogStatementContext ctx) {
        return SplitQueryType.SWITCH_CATALOG;
    }

    @Override
    public SplitQueryType visitSetRoleStatement(SetRoleStatementContext ctx) {
        return SplitQueryType.SWITCH_ROLE;
    }

    @Override
    public SplitQueryType visitExecuteAsStatement(ExecuteAsStatementContext ctx) {
        return SplitQueryType.SWITCH_USER;
    }

    @Override
    public SplitQueryType visitSetStatement(SetStatementContext ctx) {
        // The first assignment determines the primary action; the SPI collects the rest in order.
        return ctx.setVar(0).accept(this);
    }

    @Override
    public SplitQueryType visitSetNames(SetNamesContext ctx) {
        return SplitQueryType.SESSION_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitShowStatusStatement(ShowStatusStatementContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitShowProcStatement(ShowProcStatementContext ctx) {
        String path = ctx.path.getText();
        path = path.substring(1, path.length() - 1);
        if ("/current_queries".equals(path) || "/global_current_queries".equals(path)) {
            return SplitQueryType.PERFORMANCE;
        }
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitKillStatement(KillStatementContext ctx) {
        return SplitQueryType.ADMIN;
    }

    @Override
    public SplitQueryType visitInsertStatement(InsertStatementContext ctx) {
        if (ctx.explainDesc() != null) {
            return explainType(ctx.explainDesc());
        }
        return ctx.OVERWRITE() == null ? SplitQueryType.INSERT : SplitQueryType.MERGE;
    }

    @Override
    public SplitQueryType visitDeleteStatement(DeleteStatementContext ctx) {
        if (ctx.explainDesc() != null) {
            return explainType(ctx.explainDesc());
        }
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
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowUserStatement(ShowUserStatementContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowGrantsStatement(ShowGrantsStatementContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitAlterUserStatement(AlterUserStatementContext ctx) {
        return SplitQueryType.ALTER_USER;
    }

    @Override
    public SplitQueryType visitSetPassword(SetPasswordContext ctx) {
        return SplitQueryType.ALTER_USER;
    }

    @Override
    public SplitQueryType visitSetUserPropertyStatement(SetUserPropertyStatementContext ctx) {
        return SplitQueryType.ALTER_USER;
    }

    @Override
    public SplitQueryType visitSetDefaultRoleStatement(SetDefaultRoleStatementContext ctx) {
        return SplitQueryType.ALTER_USER;
    }

    @Override
    public SplitQueryType visitAlterRoleStatement(AlterRoleStatementContext ctx) {
        return SplitQueryType.COMMENT_ROLE;
    }

    @Override
    public SplitQueryType visitGrantRoleToUser(GrantRoleToUserContext ctx) {
        return SplitQueryType.GRANT;
    }

    @Override
    public SplitQueryType visitGrantRoleToRole(GrantRoleToRoleContext ctx) {
        return SplitQueryType.GRANT;
    }

    @Override
    public SplitQueryType visitGrantOnUser(GrantOnUserContext ctx) {
        return SplitQueryType.GRANT;
    }

    @Override
    public SplitQueryType visitGrantOnSystem(GrantOnSystemContext ctx) {
        return SplitQueryType.GRANT;
    }

    @Override
    public SplitQueryType visitGrantOnFunc(GrantOnFuncContext ctx) {
        return SplitQueryType.GRANT;
    }

    @Override
    public SplitQueryType visitGrantOnPrimaryObj(GrantOnPrimaryObjContext ctx) {
        return SplitQueryType.GRANT;
    }

    @Override
    public SplitQueryType visitGrantOnAll(GrantOnAllContext ctx) {
        return SplitQueryType.GRANT;
    }

    @Override
    public SplitQueryType visitRevokeRoleFromUser(RevokeRoleFromUserContext ctx) {
        return SplitQueryType.REVOKE;
    }

    @Override
    public SplitQueryType visitRevokeRoleFromRole(RevokeRoleFromRoleContext ctx) {
        return SplitQueryType.REVOKE;
    }

    @Override
    public SplitQueryType visitRevokeOnUser(RevokeOnUserContext ctx) {
        return SplitQueryType.REVOKE;
    }

    @Override
    public SplitQueryType visitRevokeOnSystem(RevokeOnSystemContext ctx) {
        return SplitQueryType.REVOKE;
    }

    @Override
    public SplitQueryType visitRevokeOnFunc(RevokeOnFuncContext ctx) {
        return SplitQueryType.REVOKE;
    }

    @Override
    public SplitQueryType visitRevokeOnPrimaryObj(RevokeOnPrimaryObjContext ctx) {
        return SplitQueryType.REVOKE;
    }

    @Override
    public SplitQueryType visitRevokeOnAll(RevokeOnAllContext ctx) {
        return SplitQueryType.REVOKE;
    }

    @Override
    public SplitQueryType visitShowAllAuthentication(ShowAllAuthenticationContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowAuthenticationForUser(ShowAuthenticationForUserContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowUserPropertyStatement(ShowUserPropertyStatementContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowPrivilegesStatement(ShowPrivilegesStatementContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowSecurityIntegrationStatement(ShowSecurityIntegrationStatementContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowCreateSecurityIntegrationStatement(ShowCreateSecurityIntegrationStatementContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowGroupProvidersStatement(ShowGroupProvidersStatementContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowCreateGroupProviderStatement(ShowCreateGroupProviderStatementContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitCreateSecurityIntegrationStatement(CreateSecurityIntegrationStatementContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitAlterSecurityIntegrationStatement(AlterSecurityIntegrationStatementContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitDropSecurityIntegrationStatement(DropSecurityIntegrationStatementContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitCreateGroupProviderStatement(CreateGroupProviderStatementContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitDropGroupProviderStatement(DropGroupProviderStatementContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitAlterDbQuotaStatement(AlterDbQuotaStatementContext ctx) {
        return SplitQueryType.ALTER_SCHEMA;
    }

    @Override
    public SplitQueryType visitUpdateStatement(UpdateStatementContext ctx) {
        if (ctx.explainDesc() != null) {
            return explainType(ctx.explainDesc());
        }
        return SplitQueryType.UPDATE;
    }

    private static SplitQueryType explainType(ExplainDescContext ctx) {
        if (ctx.ANALYZE() != null) {
            return SplitQueryType.UNSAFE;
        }
        return SplitQueryType.SELECT;
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
