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
package com.clougence.clouddm.ds.oracle.analysis;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.ds.oracle.parser.antlr.PlSqlParser.*;
import com.clougence.clouddm.ds.oracle.parser.antlr.PlSqlParserBaseVisitor;

public class OraSplitVisitor extends PlSqlParserBaseVisitor<SecQueryType> {

    public static final AbstractParseTreeVisitor<SecQueryType> INSTANCE = new OraSplitVisitor();

    public OraSplitVisitor(){
    }

    @Override
    public SecQueryType visitCreate_table(Create_tableContext ctx) {
        return SecQueryType.CREATE_TABLE;
    }

    @Override
    public SecQueryType visitRename_object(Rename_objectContext ctx) {
        return SecQueryType.RENAME_TABLE;
    }

    @Override
    public SecQueryType visitDrop_table(Drop_tableContext ctx) {
        return SecQueryType.DROP_TABLE;
    }

    @Override
    public SecQueryType visitDrop_trigger(Drop_triggerContext ctx) {
        return SecQueryType.DROP_TRIGGER;
    }

    @Override
    public SecQueryType visitAlter_trigger(Alter_triggerContext ctx) {
        return SecQueryType.ALTER_TRIGGER;
    }

    @Override
    public SecQueryType visitCreate_materialized_view_log(Create_materialized_view_logContext ctx) {
        return SecQueryType.CREATE_OBJECT;
    }

    @Override
    public SecQueryType visitDrop_sequence(Drop_sequenceContext ctx) {
        return SecQueryType.DROP_SEQUENCE;
    }

    @Override
    public SecQueryType visitAlter_table(Alter_tableContext ctx) {
        return SecQueryType.ALTER_TABLE;
    }

    @Override
    public SecQueryType visitComment_on_table(Comment_on_tableContext ctx) {
        return SecQueryType.COMMENT_TABLE;
    }

    @Override
    public SecQueryType visitComment_on_column(Comment_on_columnContext ctx) {
        return SecQueryType.COMMENT_COLUMN;
    }

    @Override
    public SecQueryType visitCreate_trigger(Create_triggerContext ctx) {
        return SecQueryType.CREATE_TRIGGER;
    }

    @Override
    public SecQueryType visitCreate_materialized_view(Create_materialized_viewContext ctx) {
        return SecQueryType.CREATE_MATERIALIZED_VIEW;
    }

    @Override
    public SecQueryType visitTruncate_table(Truncate_tableContext ctx) {
        return SecQueryType.TRUNCATE;
    }

    @Override
    public SecQueryType visitCreate_view(Create_viewContext ctx) {
        return SecQueryType.CREATE_VIEW;
    }

    @Override
    public SecQueryType visitCreate_index(Create_indexContext ctx) {
        return SecQueryType.CREATE_INDEX;
    }

    @Override
    public SecQueryType visitDrop_index(Drop_indexContext ctx) {
        return SecQueryType.DROP_INDEX;
    }

    @Override
    public SecQueryType visitAlter_index(Alter_indexContext ctx) {
        return SecQueryType.ALTER_INDEX;
    }

    @Override
    public SecQueryType visitCreate_function_body(Create_function_bodyContext ctx) {
        return SecQueryType.CREATE_FUNCTION;
    }

    @Override
    public SecQueryType visitCreate_procedure_body(Create_procedure_bodyContext ctx) {
        return SecQueryType.CREATE_PROCEDURE;
    }

    @Override
    public SecQueryType visitSelect_statement(Select_statementContext ctx) {
        return SecQueryType.SELECT;
    }

    @Override
    public SecQueryType visitUpdate_statement(Update_statementContext ctx) {
        return SecQueryType.UPDATE;
    }

    @Override
    public SecQueryType visitInsert_statement(Insert_statementContext ctx) {
        return SecQueryType.INSERT;
    }

    @Override
    public SecQueryType visitDelete_statement(Delete_statementContext ctx) {
        return SecQueryType.DELETE;
    }

    @Override
    public SecQueryType visitCall_statement(Call_statementContext ctx) {
        return SecQueryType.CALL;
    }

    @Override
    public SecQueryType visitCreate_user(Create_userContext ctx) {
        return SecQueryType.CREATE_USER;
    }

    @Override
    public SecQueryType visitDrop_user(Drop_userContext ctx) {
        return SecQueryType.DROP_USER;
    }

    @Override
    public SecQueryType visitGrant_statement(Grant_statementContext ctx) {
        return SecQueryType.GRANT;
    }

    @Override
    public SecQueryType visitRevoke_statement(Revoke_statementContext ctx) {
        return SecQueryType.REVOKE;
    }

    @Override
    public SecQueryType visitCreate_role(Create_roleContext ctx) {
        return SecQueryType.CREATE_ROLE;
    }

    @Override
    public SecQueryType visitDrop_role(Drop_roleContext ctx) {
        return SecQueryType.DROP_ROLE;
    }

    @Override
    public SecQueryType visitAlter_session(Alter_sessionContext ctx) {
        return SecQueryType.SWITCH_SCHEMA;
    }

    @Override
    public SecQueryType visitCreate_sequence(Create_sequenceContext ctx) {
        return SecQueryType.CREATE_SEQUENCE;
    }

    @Override
    public SecQueryType visitCreate_synonym(Create_synonymContext ctx) {
        return SecQueryType.CREATE_SYNONYM;
    }

    @Override
    public SecQueryType visitGeneral_element_part(General_element_partContext ctx) {
        return SecQueryType.CALL;
    }

    @Override
    public SecQueryType visitAnonymous_block(Anonymous_blockContext ctx) {
        return SecQueryType.SQL_BLOCK;
    }

    @Override
    public SecQueryType visitDrop_function(Drop_functionContext ctx) {
        return SecQueryType.DROP_FUNCTION;
    }

    @Override
    public SecQueryType visitDrop_procedure(Drop_procedureContext ctx) {
        return SecQueryType.DROP_PROCEDURE;
    }
}
