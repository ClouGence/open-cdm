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
package com.clougence.sql.sqlserver.parser;

import static com.clougence.clouddm.sdk.sql.parser.SplitQueryType.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.sql.sqlserver.parser.antlr.SqlServerParser;
import com.clougence.sql.sqlserver.parser.antlr.SqlServerParserBaseVisitor;

public class MsSplitVisitor extends SqlServerParserBaseVisitor<SplitQueryType> {

    public static final AbstractParseTreeVisitor<SplitQueryType> INSTANCE = new MsSplitVisitor();

    @Override
    public SplitQueryType visitTransaction_statement(SqlServerParser.Transaction_statementContext ctx) {
        return TRANSACTION;
    }

    @Override
    public SplitQueryType visitRevert_statement(SqlServerParser.Revert_statementContext ctx) {
        return SWITCH_USER;
    }

    @Override
    public SplitQueryType visitSetuser_statement(SqlServerParser.Setuser_statementContext ctx) {
        return SWITCH_USER;
    }

    @Override
    public SplitQueryType visitDbcc_freeproccache(SqlServerParser.Dbcc_freeproccacheContext ctx) {
        return ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitDbcc_freesystemcache(SqlServerParser.Dbcc_freesystemcacheContext ctx) {
        return ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitDbcc_freesessioncache(SqlServerParser.Dbcc_freesessioncacheContext ctx) {
        return ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitDbcc_dropcleanbuffers(SqlServerParser.Dbcc_dropcleanbuffersContext ctx) {
        return ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitDbcc_inputbuffer(SqlServerParser.Dbcc_inputbufferContext ctx) {
        return PERFORMANCE;
    }

    @Override
    public SplitQueryType visitDbcc_outputbuffer(SqlServerParser.Dbcc_outputbufferContext ctx) {
        return PERFORMANCE;
    }

    @Override
    public SplitQueryType visitDbcc_opentran(SqlServerParser.Dbcc_opentranContext ctx) {
        return PERFORMANCE;
    }

    @Override
    public SplitQueryType visitDbcc_proccache(SqlServerParser.Dbcc_proccacheContext ctx) {
        return PERFORMANCE;
    }

    @Override
    public SplitQueryType visitDbcc_memorystatus(SqlServerParser.Dbcc_memorystatusContext ctx) {
        return PERFORMANCE;
    }

    @Override
    public SplitQueryType visitDbcc_useroptions(SqlServerParser.Dbcc_useroptionsContext ctx) {
        return METADATA;
    }

    @Override
    public SplitQueryType visitDbcc_show_statistics(SqlServerParser.Dbcc_show_statisticsContext ctx) {
        return PERFORMANCE;
    }

    @Override
    public SplitQueryType visitDbcc_showcontig(SqlServerParser.Dbcc_showcontigContext ctx) {
        return PERFORMANCE;
    }

    @Override
    public SplitQueryType visitDbcc_tracestatus(SqlServerParser.Dbcc_tracestatusContext ctx) {
        return PERFORMANCE;
    }

    @Override
    public SplitQueryType visitDbcc_sqlperf(SqlServerParser.Dbcc_sqlperfContext ctx) {
        return ctx.CLEAR() == null ? PERFORMANCE : ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitDbcc_trace_control(SqlServerParser.Dbcc_trace_controlContext ctx) {
        for (SqlServerParser.Dbcc_trace_flagContext flag : ctx.dbcc_trace_flag()) {
            if ("-1".equals(flag.getText())) {
                return SYSTEM_SETTING_WRITE;
            }
        }
        return SESSION_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitCreate_server_audit(SqlServerParser.Create_server_auditContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitAlter_server_audit(SqlServerParser.Alter_server_auditContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitDrop_server_audit(SqlServerParser.Drop_server_auditContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitCreate_server_audit_specification(SqlServerParser.Create_server_audit_specificationContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitAlter_server_audit_specification(SqlServerParser.Alter_server_audit_specificationContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitDrop_server_audit_specification(SqlServerParser.Drop_server_audit_specificationContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitCreate_database_audit_specification(SqlServerParser.Create_database_audit_specificationContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitAlter_database_audit_specification(SqlServerParser.Alter_database_audit_specificationContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitDrop_database_audit_specification(SqlServerParser.Drop_database_audit_specificationContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitCreate_availability_group(SqlServerParser.Create_availability_groupContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitAlter_availability_group(SqlServerParser.Alter_availability_groupContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitDrop_availability_group(SqlServerParser.Drop_availability_groupContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitCreate_endpoint(SqlServerParser.Create_endpointContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitAlter_endpoint(SqlServerParser.Alter_endpointContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitDrop_endpoint(SqlServerParser.Drop_endpointContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitMessage_statement(SqlServerParser.Message_statementContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitAlter_message_type(SqlServerParser.Alter_message_typeContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitDrop_message_type(SqlServerParser.Drop_message_typeContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitCreate_contract(SqlServerParser.Create_contractContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitDrop_contract(SqlServerParser.Drop_contractContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitCreate_queue(SqlServerParser.Create_queueContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitAlter_queue(SqlServerParser.Alter_queueContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitDrop_queue(SqlServerParser.Drop_queueContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitCreate_service(SqlServerParser.Create_serviceContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitAlter_service(SqlServerParser.Alter_serviceContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitDrop_service(SqlServerParser.Drop_serviceContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitCreate_route(SqlServerParser.Create_routeContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitAlter_route(SqlServerParser.Alter_routeContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitDrop_route(SqlServerParser.Drop_routeContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitCreate_remote_service_binding(SqlServerParser.Create_remote_service_bindingContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitAlter_remote_service_binding(SqlServerParser.Alter_remote_service_bindingContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitDrop_remote_service_binding(SqlServerParser.Drop_remote_service_bindingContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitCreate_or_alter_broker_priority(SqlServerParser.Create_or_alter_broker_priorityContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitDrop_broker_priority(SqlServerParser.Drop_broker_priorityContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitCreate_event_notification(SqlServerParser.Create_event_notificationContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitDrop_event_notifications(SqlServerParser.Drop_event_notificationsContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitEnd_conversation(SqlServerParser.End_conversationContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitBegin_conversation_timer(SqlServerParser.Begin_conversation_timerContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitMove_conversation(SqlServerParser.Move_conversationContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitCreate_or_alter_event_session(SqlServerParser.Create_or_alter_event_sessionContext ctx) {
        return ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitDrop_event_session(SqlServerParser.Drop_event_sessionContext ctx) {
        return ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitCreate_user(SqlServerParser.Create_userContext ctx) {
        return CREATE_USER;
    }

    @Override
    public SplitQueryType visitDrop_user(SqlServerParser.Drop_userContext ctx) {
        return DROP_USER;
    }

    @Override
    public SplitQueryType visitDrop_login(SqlServerParser.Drop_loginContext ctx) {
        return DROP_USER;
    }

    @Override
    public SplitQueryType visitCreate_db_role(SqlServerParser.Create_db_roleContext ctx) {
        return CREATE_ROLE;
    }

    @Override
    public SplitQueryType visitCreate_server_role(SqlServerParser.Create_server_roleContext ctx) {
        return CREATE_ROLE;
    }

    @Override
    public SplitQueryType visitCreate_application_role(SqlServerParser.Create_application_roleContext ctx) {
        return CREATE_ROLE;
    }

    @Override
    public SplitQueryType visitDrop_db_role(SqlServerParser.Drop_db_roleContext ctx) {
        return DROP_ROLE;
    }

    @Override
    public SplitQueryType visitDrop_server_role(SqlServerParser.Drop_server_roleContext ctx) {
        return DROP_ROLE;
    }

    @Override
    public SplitQueryType visitDrop_application_role(SqlServerParser.Drop_application_roleContext ctx) {
        return DROP_ROLE;
    }

    @Override
    public SplitQueryType visitGrant_statement(SqlServerParser.Grant_statementContext ctx) {
        return GRANT;
    }

    @Override
    public SplitQueryType visitRevoke_statement(SqlServerParser.Revoke_statementContext ctx) {
        return REVOKE;
    }

    @Override
    public SplitQueryType visitAlter_authorization(SqlServerParser.Alter_authorizationContext ctx) {
        return TRANSFER_PRIVILEGE;
    }

    @Override
    public SplitQueryType visitAlter_authorization_for_sql_database(SqlServerParser.Alter_authorization_for_sql_databaseContext ctx) {
        return TRANSFER_PRIVILEGE;
    }

    @Override
    public SplitQueryType visitAlter_authorization_for_azure_dw(SqlServerParser.Alter_authorization_for_azure_dwContext ctx) {
        return TRANSFER_PRIVILEGE;
    }

    @Override
    public SplitQueryType visitAlter_authorization_for_parallel_dw(SqlServerParser.Alter_authorization_for_parallel_dwContext ctx) {
        return TRANSFER_PRIVILEGE;
    }

    @Override
    public SplitQueryType visitAlter_security_policy(SqlServerParser.Alter_security_policyContext ctx) {
        return ALTER_POLICY;
    }

    @Override
    public SplitQueryType visitAdd_signature(SqlServerParser.Add_signatureContext ctx) {
        return ADMIN_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitDrop_signature(SqlServerParser.Drop_signatureContext ctx) {
        return ADMIN_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitDeny_statement(SqlServerParser.Deny_statementContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitCreate_certificate(SqlServerParser.Create_certificateContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitAlter_certificate(SqlServerParser.Alter_certificateContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitDrop_certificate(SqlServerParser.Drop_certificateContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitBackup_certificate(SqlServerParser.Backup_certificateContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitCreate_asymmetric_key(SqlServerParser.Create_asymmetric_keyContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitAlter_asymmetric_key(SqlServerParser.Alter_asymmetric_keyContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitDrop_asymmetric_key(SqlServerParser.Drop_asymmetric_keyContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitCreate_key(SqlServerParser.Create_keyContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitAlter_symmetric_key(SqlServerParser.Alter_symmetric_keyContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitDrop_symmetric_key(SqlServerParser.Drop_symmetric_keyContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitOpen_key(SqlServerParser.Open_keyContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitClose_key(SqlServerParser.Close_keyContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitCreate_master_key_sql_server(SqlServerParser.Create_master_key_sql_serverContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitAlter_master_key_sql_server(SqlServerParser.Alter_master_key_sql_serverContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitDrop_master_key(SqlServerParser.Drop_master_keyContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitBackup_master_key(SqlServerParser.Backup_master_keyContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitRestore_master_key(SqlServerParser.Restore_master_keyContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitAlter_service_master_key(SqlServerParser.Alter_service_master_keyContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitBackup_service_master_key(SqlServerParser.Backup_service_master_keyContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitRestore_service_master_key(SqlServerParser.Restore_service_master_keyContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitCreate_database_encryption_key(SqlServerParser.Create_database_encryption_keyContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitAlter_database_encryption_key(SqlServerParser.Alter_database_encryption_keyContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitDrop_database_encryption_key(SqlServerParser.Drop_database_encryption_keyContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitCreate_column_master_key(SqlServerParser.Create_column_master_keyContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitDrop_column_master_key(SqlServerParser.Drop_column_master_keyContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitCreate_column_encryption_key(SqlServerParser.Create_column_encryption_keyContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitAlter_column_encryption_key(SqlServerParser.Alter_column_encryption_keyContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitDrop_column_encryption_key(SqlServerParser.Drop_column_encryption_keyContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitCreate_credential(SqlServerParser.Create_credentialContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitAlter_credential(SqlServerParser.Alter_credentialContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitDrop_credential(SqlServerParser.Drop_credentialContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitCreate_database_scoped_credential(SqlServerParser.Create_database_scoped_credentialContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitAlter_database_scoped_credential(SqlServerParser.Alter_database_scoped_credentialContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitDrop_database_scoped_credential(SqlServerParser.Drop_database_scoped_credentialContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitAlter_login_sql_server(SqlServerParser.Alter_login_sql_serverContext ctx) {
        if (ctx.alter_login_option().size() == 1 && ctx.alter_login_option(0).new_name != null) {
            return RENAME_USER;
        }
        return ALTER_USER;
    }

    @Override
    public SplitQueryType visitAlter_user(SqlServerParser.Alter_userContext ctx) {
        if (ctx.alter_user_option().size() == 1 && ctx.alter_user_option(0).new_name != null) {
            return RENAME_USER;
        }
        return ALTER_USER;
    }

    @Override
    public SplitQueryType visitAlter_application_role(SqlServerParser.Alter_application_roleContext ctx) {
        if (ctx.application_role_option().size() == 1 && ctx.application_role_option(0).new_name != null) {
            return RENAME_ROLE;
        }
        return ALTER_ROLE;
    }

    @Override
    public SplitQueryType visitAlter_db_role(SqlServerParser.Alter_db_roleContext ctx) {
        if (ctx.new_role_name != null) {
            return RENAME_ROLE;
        }
        return ALTER_ROLE;
    }

    @Override
    public SplitQueryType visitAlter_server_role(SqlServerParser.Alter_server_roleContext ctx) {
        if (ctx.new_server_role_name != null) {
            return RENAME_ROLE;
        }
        return ALTER_ROLE;
    }

    @Override
    public SplitQueryType visitCreate_table(SqlServerParser.Create_tableContext ctx) {
        return SplitQueryType.CREATE_TABLE;
    }

    @Override
    public SplitQueryType visitCreate_security_policy(SqlServerParser.Create_security_policyContext ctx) {
        return SplitQueryType.CREATE_POLICY;
    }

    @Override
    public SplitQueryType visitDrop_security_policy(SqlServerParser.Drop_security_policyContext ctx) {
        return SplitQueryType.DROP_POLICY;
    }

    @Override
    public SplitQueryType visitExecute_statement(SqlServerParser.Execute_statementContext ctx) {
        if (ctx.execute_body().execute_as_context() != null) {
            return SWITCH_USER;
        }
        SplitQueryType renameType = trySpRenameType(ctx);
        if (renameType != null) {
            return renameType;
        }
        return CALL_PROG_OBJ;
    }

    private SplitQueryType trySpRenameType(SqlServerParser.Execute_statementContext ctx) {
        SqlServerParser.Execute_bodyContext body = ctx.execute_body();
        if (body == null || body.func_proc_name_server_database_schema() == null) {
            return null;
        }
        List<String> procNames = names(body.func_proc_name_server_database_schema());
        if (procNames.isEmpty() || !"sp_rename".equalsIgnoreCase(procNames.get(procNames.size() - 1))) {
            return null;
        }
        List<String> args = executeArgs(body);
        if (args.size() < 3) {
            return null;
        }
        String target = stripQuote(args.get(2)).toUpperCase(Locale.ROOT);
        if (target.contains("COLUMN")) {
            return RENAME_COLUMN;
        }
        if (target.contains("OBJECT")) {
            return RENAME_TABLE;
        }
        return null;
    }

    private List<String> names(ParseTree tree) {
        List<String> names = new ArrayList<>();
        collectNames(tree, names);
        return names;
    }

    private void collectNames(ParseTree tree, List<String> names) {
        if (tree instanceof SqlServerParser.Id_Context) {
            names.add(stripQuote(tree.getText()));
            return;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            collectNames(tree.getChild(i), names);
        }
    }

    private List<String> executeArgs(SqlServerParser.Execute_bodyContext body) {
        List<String> args = new ArrayList<>();
        collectExecuteArgs(body.execute_statement_arg(), args);
        return args;
    }

    private void collectExecuteArgs(ParseTree tree, List<String> args) {
        if (tree == null) {
            return;
        }
        if (tree instanceof SqlServerParser.Execute_parameterContext parameter) {
            args.add(stripQuote(parameter.getText()));
            return;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            collectExecuteArgs(tree.getChild(i), args);
        }
    }

    private String stripQuote(String text) {
        if (text == null) {
            return "";
        }
        String trimmed = text.trim();
        if ((trimmed.startsWith("[") && trimmed.endsWith("]")) || (trimmed.startsWith("\"") && trimmed.endsWith("\"")) || (trimmed.startsWith("'") && trimmed.endsWith("'"))) {
            return trimmed.substring(1, trimmed.length() - 1);
        }
        return trimmed;
    }

    @Override
    public SplitQueryType visitDrop_table(SqlServerParser.Drop_tableContext ctx) {
        return SplitQueryType.DROP_TABLE;
    }

    @Override
    public SplitQueryType visitTruncate_table(SqlServerParser.Truncate_tableContext ctx) {
        return SplitQueryType.TRUNCATE_TABLE;
    }

    @Override
    public SplitQueryType visitDrop_aggregate(SqlServerParser.Drop_aggregateContext ctx) {
        return SplitQueryType.DROP_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitAlter_table(SqlServerParser.Alter_tableContext ctx) {
        return SplitQueryType.ALTER_TABLE;
    }

    @Override
    public SplitQueryType visitBlock_statement(SqlServerParser.Block_statementContext ctx) {
        return BLOCK;
    }

    @Override
    public SplitQueryType visitDeclare_statement(SqlServerParser.Declare_statementContext ctx) {
        return PROGRAM_CONTROL;
    }

    @Override
    public SplitQueryType visitReturn_statement(SqlServerParser.Return_statementContext ctx) {
        return PROGRAM_CONTROL;
    }

    @Override
    public SplitQueryType visitIf_statement(SqlServerParser.If_statementContext ctx) {
        return PROGRAM_CONTROL;
    }

    @Override
    public SplitQueryType visitTry_catch_statement(SqlServerParser.Try_catch_statementContext ctx) {
        return PROGRAM_CONTROL;
    }

    @Override
    public SplitQueryType visitWhile_statement(SqlServerParser.While_statementContext ctx) {
        return PROGRAM_CONTROL;
    }

    @Override
    public SplitQueryType visitSet_statement(SqlServerParser.Set_statementContext ctx) {
        return ctx.set_special() == null ? SESSION_VARIABLE_RW : SESSION_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitCreate_schema(SqlServerParser.Create_schemaContext ctx) {
        return SplitQueryType.CREATE_SCHEMA;
    }

    @Override
    public SplitQueryType visitDrop_schema(SqlServerParser.Drop_schemaContext ctx) {
        return SplitQueryType.DROP_SCHEMA;
    }

    @Override
    public SplitQueryType visitDrop_database(SqlServerParser.Drop_databaseContext ctx) {
        return SplitQueryType.DROP_CATALOG;
    }

    @Override
    public SplitQueryType visitCreate_database(SqlServerParser.Create_databaseContext ctx) {
        return SplitQueryType.CREATE_CATALOG;
    }

    @Override
    public SplitQueryType visitDrop_view(SqlServerParser.Drop_viewContext ctx) {
        return SplitQueryType.DROP_VIEW;
    }

    @Override
    public SplitQueryType visitCreate_index(SqlServerParser.Create_indexContext ctx) {
        return SplitQueryType.ADD_INDEX;
    }

    @Override
    public SplitQueryType visitDrop_index(SqlServerParser.Drop_indexContext ctx) {
        return SplitQueryType.DROP_INDEX;
    }

    @Override
    public SplitQueryType visitAlter_index(SqlServerParser.Alter_indexContext ctx) {
        return SplitQueryType.ALTER_INDEX;
    }

    @Override
    public SplitQueryType visitCreate_view(SqlServerParser.Create_viewContext ctx) {
        if (hasDirectToken(ctx, SqlServerParser.OR)) {
            return UNKNOWN;
        }
        return startsWith(ctx, "alter") ? ALTER_VIEW : CREATE_VIEW;
    }

    @Override
    public SplitQueryType visitCreate_or_alter_dml_trigger(SqlServerParser.Create_or_alter_dml_triggerContext ctx) {
        if (hasDirectToken(ctx, SqlServerParser.OR)) {
            return UNKNOWN;
        }
        return startsWith(ctx, "alter") ? ALTER_TRIGGER : CREATE_TRIGGER;
    }

    @Override
    public SplitQueryType visitCreate_or_alter_ddl_trigger(SqlServerParser.Create_or_alter_ddl_triggerContext ctx) {
        if (hasDirectToken(ctx, SqlServerParser.OR)) {
            return UNKNOWN;
        }
        return startsWith(ctx, "alter") ? ALTER_TRIGGER : CREATE_TRIGGER;
    }

    @Override
    public SplitQueryType visitDelete_statement(SqlServerParser.Delete_statementContext ctx) {
        return DELETE;
    }

    @Override
    public SplitQueryType visitInsert_statement(SqlServerParser.Insert_statementContext ctx) {
        return INSERT;
    }

    @Override
    public SplitQueryType visitUpdate_statement(SqlServerParser.Update_statementContext ctx) {
        return UPDATE;
    }

    @Override
    public SplitQueryType visitCreate_synonym(SqlServerParser.Create_synonymContext ctx) {
        return CREATE_SYNONYM;
    }

    @Override
    public SplitQueryType visitCreate_sequence(SqlServerParser.Create_sequenceContext ctx) {
        return CREATE_SEQUENCE;
    }

    @Override
    public SplitQueryType visitCreate_or_alter_function(SqlServerParser.Create_or_alter_functionContext ctx) {
        if (hasDirectToken(ctx, SqlServerParser.OR)) {
            return UNKNOWN;
        }
        return startsWith(ctx, "alter") ? ALTER_PROG_OBJ : CREATE_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitCreate_or_alter_procedure(SqlServerParser.Create_or_alter_procedureContext ctx) {
        if (hasDirectToken(ctx, SqlServerParser.OR)) {
            return UNKNOWN;
        }
        return startsWith(ctx, "alter") ? ALTER_PROG_OBJ : CREATE_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitCreate_login_sql_server(SqlServerParser.Create_login_sql_serverContext ctx) {
        return CREATE_USER;
    }

    @Override
    public SplitQueryType visitSelect_statement(SqlServerParser.Select_statementContext ctx) {
        return SELECT;
    }

    @Override
    public SplitQueryType visitSelect_statement_standalone(SqlServerParser.Select_statement_standaloneContext ctx) {
        return SELECT;
    }

    @Override
    public SplitQueryType visitUse_statement(SqlServerParser.Use_statementContext ctx) {
        return SWITCH_CATALOG;
    }

    @Override
    public SplitQueryType visitAlter_database_scoped_configuration(SqlServerParser.Alter_database_scoped_configurationContext ctx) {
        return ALTER_CATALOG;
    }

    @Override
    public SplitQueryType visitAlter_server_configuration(SqlServerParser.Alter_server_configurationContext ctx) {
        return SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitReconfigure_statement(SqlServerParser.Reconfigure_statementContext ctx) {
        return SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitShutdown_statement(SqlServerParser.Shutdown_statementContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitCheckpoint_statement(SqlServerParser.Checkpoint_statementContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitBackup_database(SqlServerParser.Backup_databaseContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitBackup_log(SqlServerParser.Backup_logContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitBackup_snapshot(SqlServerParser.Backup_snapshotContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitRestore_database(SqlServerParser.Restore_databaseContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitRestore_log(SqlServerParser.Restore_logContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitRestore_metadata(SqlServerParser.Restore_metadataContext ctx) {
        return METADATA;
    }

    @Override
    public SplitQueryType visitRestore_verify(SqlServerParser.Restore_verifyContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitCreate_statistics(SqlServerParser.Create_statisticsContext ctx) {
        return ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitUpdate_statistics(SqlServerParser.Update_statisticsContext ctx) {
        return ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitDrop_statistics(SqlServerParser.Drop_statisticsContext ctx) {
        return ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitCreate_resource_pool(SqlServerParser.Create_resource_poolContext ctx) {
        return CREATE_RESOURCE_GROUP;
    }

    @Override
    public SplitQueryType visitCreate_external_resource_pool(SqlServerParser.Create_external_resource_poolContext ctx) {
        return CREATE_RESOURCE_GROUP;
    }

    @Override
    public SplitQueryType visitCreate_workload_group(SqlServerParser.Create_workload_groupContext ctx) {
        return CREATE_RESOURCE_GROUP;
    }

    @Override
    public SplitQueryType visitAlter_resource_pool(SqlServerParser.Alter_resource_poolContext ctx) {
        return ALTER_RESOURCE_GROUP;
    }

    @Override
    public SplitQueryType visitAlter_external_resource_pool(SqlServerParser.Alter_external_resource_poolContext ctx) {
        return ALTER_RESOURCE_GROUP;
    }

    @Override
    public SplitQueryType visitAlter_workload_group(SqlServerParser.Alter_workload_groupContext ctx) {
        return ALTER_RESOURCE_GROUP;
    }

    @Override
    public SplitQueryType visitDrop_resource_pool(SqlServerParser.Drop_resource_poolContext ctx) {
        return DROP_RESOURCE_GROUP;
    }

    @Override
    public SplitQueryType visitDrop_external_resource_pool(SqlServerParser.Drop_external_resource_poolContext ctx) {
        return DROP_RESOURCE_GROUP;
    }

    @Override
    public SplitQueryType visitDrop_workload_group(SqlServerParser.Drop_workload_groupContext ctx) {
        return DROP_RESOURCE_GROUP;
    }

    @Override
    public SplitQueryType visitAlter_resource_governor(SqlServerParser.Alter_resource_governorContext ctx) {
        return ADMIN_RESOURCE_GROUP;
    }

    @Override
    public SplitQueryType visitDbcc_checkdb(SqlServerParser.Dbcc_checkdbContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitDbcc_checkalloc(SqlServerParser.Dbcc_checkallocContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitDbcc_checkcatalog(SqlServerParser.Dbcc_checkcatalogContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitDbcc_checkfilegroup(SqlServerParser.Dbcc_checkfilegroupContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitDbcc_checktable(SqlServerParser.Dbcc_checktableContext ctx) {
        return ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitDbcc_checkident(SqlServerParser.Dbcc_checkidentContext ctx) {
        return ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitDbcc_cleantable(SqlServerParser.Dbcc_cleantableContext ctx) {
        return ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitDbcc_dbreindex(SqlServerParser.Dbcc_dbreindexContext ctx) {
        return ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitDbcc_indexdefrag(SqlServerParser.Dbcc_indexdefragContext ctx) {
        return ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitDbcc_shrinkdatabase(SqlServerParser.Dbcc_shrinkdatabaseContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitDbcc_shrinkfile(SqlServerParser.Dbcc_shrinkfileContext ctx) {
        return ADMIN;
    }

    @Override
    public SplitQueryType visitAlter_database(SqlServerParser.Alter_databaseContext ctx) {
        if (ctx.new_name != null) {
            return RENAME_CATALOG;
        }
        return ALTER_CATALOG;
    }

    @Override
    public SplitQueryType visitKill_statement(SqlServerParser.Kill_statementContext ctx) {
        if (ctx.kill_process() != null && ctx.kill_process().STATUSONLY() != null) {
            return PERFORMANCE;
        }
        return ADMIN;
    }

    @Override
    public SplitQueryType visitDbcc_checkconstraints(SqlServerParser.Dbcc_checkconstraintsContext ctx) {
        if (ctx.table_or_constraint != null || ctx.table_or_constraint_name != null) {
            return ADMIN_TABLE;
        }
        return ADMIN;
    }

    @Override
    public SplitQueryType visitDbcc_updateusage(SqlServerParser.Dbcc_updateusageContext ctx) {
        if (ctx.table_or_view != null) {
            return ADMIN_TABLE;
        }
        return ADMIN;
    }

    private boolean startsWith(ParserRuleContext ctx, String keyword) {
        return ctx.getChildCount() > 0 && keyword.equalsIgnoreCase(ctx.getChild(0).getText());
    }

    private boolean hasDirectToken(ParserRuleContext ctx, int tokenType) {
        for (ParseTree child : ctx.children) {
            if (child instanceof TerminalNodeImpl terminal && terminal.getSymbol().getType() == tokenType) {
                return true;
            }
        }
        return false;
    }
}
