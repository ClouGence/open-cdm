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
package com.clougence.sql.oracle.parser;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.sql.oracle.parser.antlr.PlSqlParserBaseVisitor;
import com.clougence.sql.oracle.parser.antlr.PlSqlParser.*;

public class OraSplitVisitor extends PlSqlParserBaseVisitor<SplitQueryType> {

    public static final AbstractParseTreeVisitor<SplitQueryType> INSTANCE = new OraSplitVisitor();

    public OraSplitVisitor(){
    }

    @Override
    public SplitQueryType visitCreate_table(Create_tableContext ctx) {
        return SplitQueryType.CREATE_TABLE;
    }

    @Override
    public SplitQueryType visitRename_object(Rename_objectContext ctx) {
        return SplitQueryType.RENAME_TABLE;
    }

    @Override
    public SplitQueryType visitDrop_table(Drop_tableContext ctx) {
        return SplitQueryType.DROP_TABLE;
    }

    @Override
    public SplitQueryType visitDrop_trigger(Drop_triggerContext ctx) {
        return SplitQueryType.DROP_TRIGGER;
    }

    @Override
    public SplitQueryType visitAlter_trigger(Alter_triggerContext ctx) {
        return SplitQueryType.ALTER_TRIGGER;
    }

    @Override
    public SplitQueryType visitCreate_materialized_view_log(Create_materialized_view_logContext ctx) {
        return SplitQueryType.CREATE_LOG;
    }

    @Override
    public SplitQueryType visitDrop_sequence(Drop_sequenceContext ctx) {
        return SplitQueryType.DROP_SEQUENCE;
    }

    @Override
    public SplitQueryType visitAlter_table(Alter_tableContext ctx) {
        return SplitQueryType.ALTER_TABLE;
    }

    @Override
    public SplitQueryType visitComment_on_table(Comment_on_tableContext ctx) {
        return SplitQueryType.COMMENT_TABLE;
    }

    @Override
    public SplitQueryType visitComment_on_column(Comment_on_columnContext ctx) {
        return SplitQueryType.COMMENT_COLUMN;
    }

    @Override
    public SplitQueryType visitCreate_trigger(Create_triggerContext ctx) {
        return SplitQueryType.CREATE_TRIGGER;
    }

    @Override
    public SplitQueryType visitCreate_materialized_view(Create_materialized_viewContext ctx) {
        return SplitQueryType.CREATE_VIEW;
    }

    @Override
    public SplitQueryType visitTruncate_table(Truncate_tableContext ctx) {
        return SplitQueryType.TRUNCATE_TABLE;
    }

    @Override
    public SplitQueryType visitCreate_view(Create_viewContext ctx) {
        return SplitQueryType.CREATE_VIEW;
    }

    @Override
    public SplitQueryType visitAlter_view(Alter_viewContext ctx) {
        return SplitQueryType.ALTER_VIEW;
    }

    @Override
    public SplitQueryType visitDrop_view(Drop_viewContext ctx) {
        return SplitQueryType.DROP_VIEW;
    }

    @Override
    public SplitQueryType visitCreate_index(Create_indexContext ctx) {
        return SplitQueryType.ADD_INDEX;
    }

    @Override
    public SplitQueryType visitDrop_index(Drop_indexContext ctx) {
        return SplitQueryType.DROP_INDEX;
    }

    @Override
    public SplitQueryType visitAlter_index(Alter_indexContext ctx) {
        return SplitQueryType.ALTER_INDEX;
    }

    @Override
    public SplitQueryType visitCreate_function_body(Create_function_bodyContext ctx) {
        return SplitQueryType.CREATE_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitAlter_function(Alter_functionContext ctx) {
        return SplitQueryType.ALTER_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitCreate_procedure_body(Create_procedure_bodyContext ctx) {
        return SplitQueryType.CREATE_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitAlter_procedure(Alter_procedureContext ctx) {
        return SplitQueryType.ALTER_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitSelect_statement(Select_statementContext ctx) {
        return SplitQueryType.SELECT;
    }

    @Override
    public SplitQueryType visitUpdate_statement(Update_statementContext ctx) {
        return SplitQueryType.UPDATE;
    }

    @Override
    public SplitQueryType visitInsert_statement(Insert_statementContext ctx) {
        return SplitQueryType.INSERT;
    }

    @Override
    public SplitQueryType visitDelete_statement(Delete_statementContext ctx) {
        return SplitQueryType.DELETE;
    }

    @Override
    public SplitQueryType visitMerge_statement(Merge_statementContext ctx) {
        return SplitQueryType.MERGE;
    }

    @Override
    public SplitQueryType visitCall_statement(Call_statementContext ctx) {
        return SplitQueryType.CALL_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitCreate_user(Create_userContext ctx) {
        return SplitQueryType.CREATE_USER;
    }

    @Override
    public SplitQueryType visitDrop_user(Drop_userContext ctx) {
        return SplitQueryType.DROP_USER;
    }

    @Override
    public SplitQueryType visitAlter_user(Alter_userContext ctx) {
        return SplitQueryType.ALTER_USER;
    }

    @Override
    public SplitQueryType visitGrant_statement(Grant_statementContext ctx) {
        return SplitQueryType.GRANT;
    }

    @Override
    public SplitQueryType visitRevoke_statement(Revoke_statementContext ctx) {
        return SplitQueryType.REVOKE;
    }

    @Override
    public SplitQueryType visitCreate_role(Create_roleContext ctx) {
        return SplitQueryType.CREATE_ROLE;
    }

    @Override
    public SplitQueryType visitDrop_role(Drop_roleContext ctx) {
        return SplitQueryType.DROP_ROLE;
    }

    @Override
    public SplitQueryType visitAlter_session(Alter_sessionContext ctx) {
        return SplitQueryType.SWITCH_SCHEMA;
    }

    @Override
    public SplitQueryType visitCreate_sequence(Create_sequenceContext ctx) {
        return SplitQueryType.CREATE_SEQUENCE;
    }

    @Override
    public SplitQueryType visitAlter_sequence(Alter_sequenceContext ctx) {
        return SplitQueryType.ALTER_SEQUENCE;
    }

    @Override
    public SplitQueryType visitCreate_synonym(Create_synonymContext ctx) {
        return SplitQueryType.CREATE_SYNONYM;
    }

    @Override
    public SplitQueryType visitDrop_synonym(Drop_synonymContext ctx) {
        return SplitQueryType.DROP_SYNONYM;
    }

    @Override
    public SplitQueryType visitGeneral_element_part(General_element_partContext ctx) {
        return SplitQueryType.CALL_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitAnonymous_block(Anonymous_blockContext ctx) {
        return SplitQueryType.BLOCK;
    }

    @Override
    public SplitQueryType visitDrop_function(Drop_functionContext ctx) {
        return SplitQueryType.DROP_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitDrop_procedure(Drop_procedureContext ctx) {
        return SplitQueryType.DROP_PROG_OBJ;
    }
}
