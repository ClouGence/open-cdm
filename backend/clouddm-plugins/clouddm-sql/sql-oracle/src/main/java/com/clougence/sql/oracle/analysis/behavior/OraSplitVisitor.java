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
package com.clougence.sql.oracle.analysis.behavior;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementType;
import com.clougence.sql.oracle.parser.antlr.PlSqlParserBaseVisitor;
import com.clougence.sql.oracle.parser.antlr.PlSqlParser.*;

public class OraSplitVisitor extends PlSqlParserBaseVisitor<StatementType> {

    public static final AbstractParseTreeVisitor<StatementType> INSTANCE = new OraSplitVisitor();

    public OraSplitVisitor(){
    }

    @Override
    public StatementType visitExplain_statement(Explain_statementContext ctx) {
        return StatementType.SELECT;
    }

    @Override
    public StatementType visitCreate_table(Create_tableContext ctx) {
        return StatementType.CREATE_TABLE;
    }

    @Override
    public StatementType visitRename_object(Rename_objectContext ctx) {
        return StatementType.RENAME_TABLE;
    }

    @Override
    public StatementType visitDrop_table(Drop_tableContext ctx) {
        return StatementType.DROP_TABLE;
    }

    @Override
    public StatementType visitDrop_trigger(Drop_triggerContext ctx) {
        return StatementType.DROP_TRIGGER;
    }

    @Override
    public StatementType visitAlter_trigger(Alter_triggerContext ctx) {
        return StatementType.ALTER_TRIGGER;
    }

    @Override
    public StatementType visitCreate_materialized_view_log(Create_materialized_view_logContext ctx) {
        return StatementType.CREATE_LOG;
    }

    @Override
    public StatementType visitDrop_sequence(Drop_sequenceContext ctx) {
        return StatementType.DROP_SEQUENCE;
    }

    @Override
    public StatementType visitAlter_table(Alter_tableContext ctx) {
        return StatementType.ALTER_TABLE;
    }

    @Override
    public StatementType visitComment_on_table(Comment_on_tableContext ctx) {
        return StatementType.COMMENT_TABLE;
    }

    @Override
    public StatementType visitComment_on_column(Comment_on_columnContext ctx) {
        return StatementType.COMMENT_COLUMN;
    }

    @Override
    public StatementType visitCreate_trigger(Create_triggerContext ctx) {
        return StatementType.CREATE_TRIGGER;
    }

    @Override
    public StatementType visitCreate_materialized_view(Create_materialized_viewContext ctx) {
        return StatementType.CREATE_VIEW;
    }

    @Override
    public StatementType visitTruncate_table(Truncate_tableContext ctx) {
        return StatementType.TRUNCATE_TABLE;
    }

    @Override
    public StatementType visitCreate_view(Create_viewContext ctx) {
        return StatementType.CREATE_VIEW;
    }

    @Override
    public StatementType visitAlter_view(Alter_viewContext ctx) {
        return StatementType.ALTER_VIEW;
    }

    @Override
    public StatementType visitDrop_view(Drop_viewContext ctx) {
        return StatementType.DROP_VIEW;
    }

    @Override
    public StatementType visitCreate_index(Create_indexContext ctx) {
        return StatementType.ADD_INDEX;
    }

    @Override
    public StatementType visitDrop_index(Drop_indexContext ctx) {
        return StatementType.DROP_INDEX;
    }

    @Override
    public StatementType visitAlter_index(Alter_indexContext ctx) {
        return StatementType.ALTER_INDEX;
    }

    @Override
    public StatementType visitCreate_function_body(Create_function_bodyContext ctx) {
        return StatementType.CREATE_PROG_OBJ;
    }

    @Override
    public StatementType visitAlter_function(Alter_functionContext ctx) {
        return StatementType.ALTER_PROG_OBJ;
    }

    @Override
    public StatementType visitCreate_procedure_body(Create_procedure_bodyContext ctx) {
        return StatementType.CREATE_PROG_OBJ;
    }

    @Override
    public StatementType visitAlter_procedure(Alter_procedureContext ctx) {
        return StatementType.ALTER_PROG_OBJ;
    }

    @Override
    public StatementType visitSelect_statement(Select_statementContext ctx) {
        return StatementType.SELECT;
    }

    @Override
    public StatementType visitUpdate_statement(Update_statementContext ctx) {
        return StatementType.UPDATE;
    }

    @Override
    public StatementType visitInsert_statement(Insert_statementContext ctx) {
        return StatementType.INSERT;
    }

    @Override
    public StatementType visitDelete_statement(Delete_statementContext ctx) {
        return StatementType.DELETE;
    }

    @Override
    public StatementType visitMerge_statement(Merge_statementContext ctx) {
        return StatementType.MERGE;
    }

    @Override
    public StatementType visitCall_statement(Call_statementContext ctx) {
        return StatementType.CALL_PROG_OBJ;
    }

    @Override
    public StatementType visitCreate_user(Create_userContext ctx) {
        return StatementType.CREATE_USER;
    }

    @Override
    public StatementType visitDrop_user(Drop_userContext ctx) {
        return StatementType.DROP_USER;
    }

    @Override
    public StatementType visitAlter_user(Alter_userContext ctx) {
        return StatementType.ALTER_USER;
    }

    @Override
    public StatementType visitGrant_statement(Grant_statementContext ctx) {
        return StatementType.GRANT;
    }

    @Override
    public StatementType visitRevoke_statement(Revoke_statementContext ctx) {
        return StatementType.REVOKE;
    }

    @Override
    public StatementType visitCreate_role(Create_roleContext ctx) {
        return StatementType.CREATE_ROLE;
    }

    @Override
    public StatementType visitDrop_role(Drop_roleContext ctx) {
        return StatementType.DROP_ROLE;
    }

    @Override
    public StatementType visitAlter_session(Alter_sessionContext ctx) {
        return StatementType.SWITCH_SCHEMA;
    }

    @Override
    public StatementType visitCreate_sequence(Create_sequenceContext ctx) {
        return StatementType.CREATE_SEQUENCE;
    }

    @Override
    public StatementType visitAlter_sequence(Alter_sequenceContext ctx) {
        return StatementType.ALTER_SEQUENCE;
    }

    @Override
    public StatementType visitCreate_synonym(Create_synonymContext ctx) {
        return StatementType.CREATE_SYNONYM;
    }

    @Override
    public StatementType visitDrop_synonym(Drop_synonymContext ctx) {
        return StatementType.DROP_SYNONYM;
    }

    @Override
    public StatementType visitGeneral_element_part(General_element_partContext ctx) {
        return StatementType.CALL_PROG_OBJ;
    }

    @Override
    public StatementType visitAnonymous_block(Anonymous_blockContext ctx) {
        return StatementType.BLOCK;
    }

    @Override
    public StatementType visitDrop_function(Drop_functionContext ctx) {
        return StatementType.DROP_PROG_OBJ;
    }

    @Override
    public StatementType visitDrop_procedure(Drop_procedureContext ctx) {
        return StatementType.DROP_PROG_OBJ;
    }
}
