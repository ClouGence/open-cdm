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
package com.clougence.clouddm.ds.ads.analysis.ads4my;

import static com.clougence.clouddm.ds.ads.parser.ads4my.antlr.AdsMyParser.*;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;

import com.clougence.clouddm.ds.ads.parser.ads4my.antlr.AdsMyParserBaseVisitor;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public abstract class AbstractAdsMySplitVisitor extends AdsMyParserBaseVisitor<SecQueryType> {

    public AbstractAdsMySplitVisitor(){
    }

    @Override
    public SecQueryType visitCreateDatabase(CreateDatabaseContext ctx) {
        return SecQueryType.CREATE_SCHEMA;
    }

    @Override
    public SecQueryType visitCheckTable(CheckTableContext ctx) {
        return SecQueryType.CHECK_TABLE;
    }

    @Override
    public SecQueryType visitRepairTable(RepairTableContext ctx) {
        return SecQueryType.REPAIR;
    }

    @Override
    public SecQueryType visitCreateUdfFunction(CreateUdfFunctionContext ctx) {
        return SecQueryType.CREATE_UDF_FUNCTION;
    }

    @Override
    public SecQueryType visitUninstallPlugin(UninstallPluginContext ctx) {
        return SecQueryType.UNINSTALL_PLUGIN;
    }

    @Override
    public SecQueryType visitInstallPlugin(InstallPluginContext ctx) {
        return SecQueryType.INSTALL_PLUGIN;
    }

    @Override
    public SecQueryType visitSetPassword(SetPasswordContext ctx) {
        return SecQueryType.ALTER_USER;
    }

    @Override
    public SecQueryType visitChecksumTable(ChecksumTableContext ctx) {
        return SecQueryType.CHECK_TABLE;
    }

    @Override
    public SecQueryType visitOptimizeTable(OptimizeTableContext ctx) {
        return SecQueryType.OPTIMIZE;
    }

    @Override
    public SecQueryType visitCreateTablespaceInnodb(CreateTablespaceInnodbContext ctx) {
        return SecQueryType.CREATE_OBJECT;
    }

    @Override
    public SecQueryType visitCreateLogfileGroup(CreateLogfileGroupContext ctx) {
        return SecQueryType.CREATE_OBJECT;
    }

    @Override
    public SecQueryType visitAlterUserMysqlV56(AlterUserMysqlV56Context ctx) {
        return SecQueryType.ALTER_USER;
    }

    @Override
    public SecQueryType visitAlterUserMysqlV57(AlterUserMysqlV57Context ctx) {
        return SecQueryType.ALTER_USER;
    }

    @Override
    public SecQueryType visitDropTablespace(DropTablespaceContext ctx) {
        return SecQueryType.DROP_OBJECT;
    }

    @Override
    public SecQueryType visitDropLogfileGroup(DropLogfileGroupContext ctx) {
        return SecQueryType.DROP_OBJECT;
    }

    @Override
    public SecQueryType visitAlterTablespace(AlterTablespaceContext ctx) {
        return SecQueryType.ALTER_OBJECT;
    }

    @Override
    public SecQueryType visitAlterLogfileGroup(AlterLogfileGroupContext ctx) {
        return SecQueryType.ALTER_OBJECT;
    }

    @Override
    public SecQueryType visitCreateTablespaceNdb(CreateTablespaceNdbContext ctx) {
        return SecQueryType.CREATE_OBJECT;
    }

    @Override
    public SecQueryType visitAnalyzeTable(AnalyzeTableContext ctx) {
        return SecQueryType.ANALYZE;
    }

    @Override
    public SecQueryType visitWithSelectStatement(WithSelectStatementContext ctx) {
        return SecQueryType.SELECT;
    }

    @Override
    public SecQueryType visitPrepareStatement(PrepareStatementContext ctx) {
        return SecQueryType.PREPARE;
    }

    @Override
    public SecQueryType visitExecuteStatement(ExecuteStatementContext ctx) {
        return SecQueryType.EXECUTE;
    }

    @Override
    public SecQueryType visitDeallocatePrepare(DeallocatePrepareContext ctx) {
        return SecQueryType.DEALLOCATE;
    }

    @Override
    public SecQueryType visitSetTransaction(SetTransactionContext ctx) {
        return SecQueryType.TRANSACTION;
    }

    @Override
    public SecQueryType visitTransactionStatement(TransactionStatementContext ctx) {
        return SecQueryType.TRANSACTION;
    }

    @Override
    public SecQueryType visitDropProcedure(DropProcedureContext ctx) {
        return SecQueryType.DROP_PROCEDURE;
    }

    @Override
    public SecQueryType visitDropTrigger(DropTriggerContext ctx) {
        return SecQueryType.DROP_TRIGGER;
    }

    @Override
    public SecQueryType visitDropFunction(DropFunctionContext ctx) {
        return SecQueryType.DROP_FUNCTION;
    }

    @Override
    public SecQueryType visitDropRole(DropRoleContext ctx) {
        return SecQueryType.DROP_ROLE;
    }

    @Override
    public SecQueryType visitDropIndex(DropIndexContext ctx) {
        return SecQueryType.DROP_INDEX;
    }

    @Override
    public SecQueryType visitDropDatabase(DropDatabaseContext ctx) {
        return SecQueryType.DROP_SCHEMA;
    }

    @Override
    public SecQueryType visitAlterSimpleDatabase(AlterSimpleDatabaseContext ctx) {
        return SecQueryType.ALTER_SCHEMA;
    }

    @Override
    public SecQueryType visitAlterUpgradeName(AlterUpgradeNameContext ctx) {
        return SecQueryType.ALTER_SCHEMA;
    }

    @Override
    public SecQueryType visitTruncateTable(TruncateTableContext ctx) {
        return SecQueryType.TRUNCATE;
    }

    @Override
    public SecQueryType visitCopyCreateTable(CopyCreateTableContext ctx) {
        return SecQueryType.CREATE_TABLE_LIKE;
    }

    @Override
    public SecQueryType visitQueryCreateTable(QueryCreateTableContext ctx) {
        return SecQueryType.CREATE_TABLE_SELECT;
    }

    @Override
    public SecQueryType visitColumnCreateTable(ColumnCreateTableContext ctx) {
        return SecQueryType.CREATE_TABLE;
    }

    @Override
    public SecQueryType visitDropTable(DropTableContext ctx) {
        return SecQueryType.DROP_TABLE;
    }

    @Override
    public SecQueryType visitAlterTable(AlterTableContext ctx) {
        return SecQueryType.ALTER_TABLE;
    }

    @Override
    public SecQueryType visitRenameTable(RenameTableContext ctx) {
        return SecQueryType.RENAME_TABLE;
    }

    @Override
    public SecQueryType visitCreateTrigger(CreateTriggerContext ctx) {
        return SecQueryType.CREATE_TRIGGER;
    }

    @Override
    public SecQueryType visitCreateView(CreateViewContext ctx) {
        return SecQueryType.CREATE_VIEW;
    }

    @Override
    public SecQueryType visitAlterView(AlterViewContext ctx) {
        return SecQueryType.ALTER_VIEW;
    }

    @Override
    public SecQueryType visitDropView(DropViewContext ctx) {
        return SecQueryType.DROP_VIEW;
    }

    @Override
    public SecQueryType visitFullDescribeStatement(FullDescribeStatementContext ctx) {
        return SecQueryType.EXPLAIN;
    }

    @Override
    public SecQueryType visitCreateEvent(CreateEventContext ctx) {
        return SecQueryType.CREATE_EVENT;
    }

    @Override
    public SecQueryType visitDropEvent(DropEventContext ctx) {
        return SecQueryType.DROP_EVENT;
    }

    @Override
    public SecQueryType visitCreateIndex(CreateIndexContext ctx) {
        return SecQueryType.CREATE_INDEX;
    }

    @Override
    public SecQueryType visitAlterFunction(AlterFunctionContext ctx) {
        return SecQueryType.ALTER_FUNCTION;
    }

    @Override
    public SecQueryType visitCreateFunction(CreateFunctionContext ctx) {
        return SecQueryType.CREATE_FUNCTION;
    }

    @Override
    public SecQueryType visitCreateProcedure(CreateProcedureContext ctx) {
        return SecQueryType.CREATE_PROCEDURE;
    }

    @Override
    public SecQueryType visitAlterEvent(AlterEventContext ctx) {
        return SecQueryType.ALTER_EVENT;
    }

    @Override
    public SecQueryType visitSimpleSelect(SimpleSelectContext ctx) {
        return SecQueryType.SELECT;
    }

    @Override
    public SecQueryType visitParenthesisSelect(ParenthesisSelectContext ctx) {
        return SecQueryType.SELECT;
    }

    @Override
    public SecQueryType visitUnionSelect(UnionSelectContext ctx) {
        return SecQueryType.SELECT;
    }

    @Override
    public SecQueryType visitUnionParenthesisSelect(UnionParenthesisSelectContext ctx) {
        return SecQueryType.SELECT;
    }

    @Override
    public SecQueryType visitUpdateStatement(UpdateStatementContext ctx) {
        return SecQueryType.UPDATE;
    }

    @Override
    public SecQueryType visitInsertStatement(InsertStatementContext ctx) {
        return SecQueryType.INSERT;
    }

    @Override
    public SecQueryType visitReplaceStatement(ReplaceStatementContext ctx) {
        return SecQueryType.MYSQL_REPLACE_INTO;
    }

    @Override
    public SecQueryType visitDeleteStatement(DeleteStatementContext ctx) {
        return SecQueryType.DELETE;
    }

    @Override
    public SecQueryType visitCallStatement(CallStatementContext ctx) {
        return SecQueryType.CALL;
    }

    @Override
    public SecQueryType visitUseStatement(UseStatementContext ctx) {
        return SecQueryType.SWITCH_SCHEMA;
    }

    @Override
    public SecQueryType visitSimpleDescribeStatement(SimpleDescribeStatementContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitCreateUser(CreateUserContext ctx) {
        return SecQueryType.CREATE_USER;
    }

    @Override
    public SecQueryType visitDropUser(DropUserContext ctx) {
        return SecQueryType.DROP_USER;
    }

    @Override
    public SecQueryType visitRenameUser(RenameUserContext ctx) {
        return SecQueryType.RENAME_USER;
    }

    @Override
    public SecQueryType visitGrantProxy(GrantProxyContext ctx) {
        return SecQueryType.GRANT;
    }

    @Override
    public SecQueryType visitRevokeProxy(RevokeProxyContext ctx) {
        return SecQueryType.REVOKE;
    }

    @Override
    public SecQueryType visitGrantStatement(GrantStatementContext ctx) {
        return SecQueryType.GRANT;
    }

    @Override
    public SecQueryType visitRevokeStatement(RevokeStatementContext ctx) {
        return SecQueryType.REVOKE;
    }

    @Override
    public SecQueryType visitCreateRole(CreateRoleContext ctx) {
        return SecQueryType.CREATE_ROLE;
    }

    @Override
    public SecQueryType visitShowMasterLogs(ShowMasterLogsContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowCharset(ShowCharsetContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowLogEvents(ShowLogEventsContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowObjectFilter(ShowObjectFilterContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowColumns(ShowColumnsContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowTables(ShowTablesContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowCreateDb(ShowCreateDbContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowCreateFullIdObject(ShowCreateFullIdObjectContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowCreateUser(ShowCreateUserContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowEngine(ShowEngineContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowEngines(ShowEnginesContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowStatus(ShowStatusContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowPlugins(ShowPluginsContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowPrivileges(ShowPrivilegesContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowProcessList(ShowProcessListContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowProfiles(ShowProfilesContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowSlaveHosts(ShowSlaveHostsContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowAuthros(ShowAuthrosContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowContributors(ShowContributorsContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowErrors(ShowErrorsContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowCountErrors(ShowCountErrorsContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowSchemaFilter(ShowSchemaFilterContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowRoutine(ShowRoutineContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowGrants(ShowGrantsContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowIndexes(ShowIndexesContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowOpenTables(ShowOpenTablesContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowProfile(ShowProfileContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitResetMaster(ResetMasterContext ctx) {
        return SecQueryType.RESET;
    }

    @Override
    public SecQueryType visitResetSlave(ResetSlaveContext ctx) {
        return SecQueryType.RESET;
    }

    @Override
    public SecQueryType visitResetReplica(ResetReplicaContext ctx) {
        return SecQueryType.RESET;
    }

    @Override
    public SecQueryType visitFlushStatement(FlushStatementContext ctx) {
        return SecQueryType.MYSQL_FLUSH;
    }

    @Override
    public SecQueryType visitShowReplicaStatus(ShowReplicaStatusContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitKillStatement(KillStatementContext ctx) {
        return SecQueryType.KILL;
    }

    @Override
    public SecQueryType visitLoadIndexIntoCache(LoadIndexIntoCacheContext ctx) {
        return SecQueryType.LOAD_INDEX_INTO_CACHE;
    }

    @Override
    public SecQueryType visitPurgeBinaryLogs(PurgeBinaryLogsContext ctx) {
        return SecQueryType.PURGE;
    }

    @Override
    public SecQueryType visitShowSlaveStatus(ShowSlaveStatusContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitSetVariable(SetVariableContext ctx) {
        return SecQueryType.CONFIG_WRITE;
    }

    public SecQueryType visitChildren(RuleNode node) {

        int n = node.getChildCount();

        for (int i = 0; i < n; ++i) {
            ParseTree c = node.getChild(i);
            SecQueryType result = c.accept(this);
            if (result != null) {
                return result;
            }
        }

        return SecQueryType.UNKNOWN;
    }

}
