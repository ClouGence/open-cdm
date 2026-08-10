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
package com.clougence.clouddm.ds.maxcompute.sql.analysis.behavior;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;

import com.clougence.clouddm.ds.maxcompute.sql.parser.antlr.McParserBaseVisitor;
import com.clougence.clouddm.ds.maxcompute.sql.parser.antlr.McParserParser;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementType;

public class McSplitVisitor extends McParserBaseVisitor<StatementType> {

    public static final AbstractParseTreeVisitor<StatementType> INSTANCE = new McSplitVisitor();

    public McSplitVisitor(){
    }

    @Override
    public StatementType visitInsert_stmt(McParserParser.Insert_stmtContext ctx) {
        return StatementType.INSERT;
    }

    @Override
    public StatementType visitCall_stmt(McParserParser.Call_stmtContext ctx) {
        return StatementType.CALL_PROG_OBJ;
    }

    @Override
    public StatementType visitShowTablePartitions(McParserParser.ShowTablePartitionsContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitDescribe_stmt(McParserParser.Describe_stmtContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitShowHistoryTables(McParserParser.ShowHistoryTablesContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitShowHistoryTable(McParserParser.ShowHistoryTableContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitShowTableColumnStatics(McParserParser.ShowTableColumnStaticsContext ctx) {
        return StatementType.PERFORMANCE;
    }

    @Override
    public StatementType visitShowCreateTable(McParserParser.ShowCreateTableContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitShowTables(McParserParser.ShowTablesContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitDropMView(McParserParser.DropMViewContext ctx) {
        return StatementType.DROP_VIEW;
    }

    @Override
    public StatementType visitAlter_materialized_view_stmt(McParserParser.Alter_materialized_view_stmtContext ctx) {
        return StatementType.ALTER_VIEW;
    }

    @Override
    public StatementType visitAssignment_stmt(McParserParser.Assignment_stmtContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitCreate_materialized_view_stmt(McParserParser.Create_materialized_view_stmtContext ctx) {
        return StatementType.CREATE_VIEW;
    }

    @Override
    public StatementType visitDropView(McParserParser.DropViewContext ctx) {
        return StatementType.DROP_VIEW;
    }

    @Override
    public StatementType visitCreate_view_stmt(McParserParser.Create_view_stmtContext ctx) {
        return StatementType.CREATE_VIEW;
    }

    @Override
    public StatementType visitTruncate_stmt(McParserParser.Truncate_stmtContext ctx) {
        return StatementType.TRUNCATE_TABLE;
    }

    @Override
    public StatementType visitAlter_table_stmt(McParserParser.Alter_table_stmtContext ctx) {
        return StatementType.ALTER_TABLE;
    }

    @Override
    public StatementType visitCreate_table_stmt(McParserParser.Create_table_stmtContext ctx) {
        if (ctx.create_table_definition() instanceof McParserParser.CreateTableColumnContext) {
            return StatementType.CREATE_TABLE;
        } else if (ctx.create_table_definition() instanceof McParserParser.CreateTableLikeContext) {
            return StatementType.CREATE_TABLE;
        } else {
            return StatementType.CREATE_TABLE;
        }
    }

    @Override
    public StatementType visitAnalyze_table_stmt(McParserParser.Analyze_table_stmtContext ctx) {
        return StatementType.ADMIN_TABLE;
    }

    @Override
    public StatementType visitDropTable(McParserParser.DropTableContext ctx) {
        return StatementType.DROP_TABLE;
    }

    @Override
    public StatementType visitSelect_stmt(McParserParser.Select_stmtContext ctx) {
        return StatementType.SELECT;
    }

    @Override
    public StatementType visitDelete_stmt(McParserParser.Delete_stmtContext ctx) {
        return StatementType.DELETE;
    }

    @Override
    public StatementType visitDropSchema(McParserParser.DropSchemaContext ctx) {
        return StatementType.DROP_SCHEMA;
    }

    @Override
    public StatementType visitCreate_database_stmt(McParserParser.Create_database_stmtContext ctx) {
        return StatementType.CREATE_SCHEMA;
    }

    @Override
    public StatementType visitUpdate_stmt(McParserParser.Update_stmtContext ctx) {
        return StatementType.UPDATE;
    }

    @Override
    public StatementType visitShowRoles(McParserParser.ShowRolesContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitShowUsers(McParserParser.ShowUsersContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitShowTrustProjects(McParserParser.ShowTrustProjectsContext ctx) {
        return StatementType.METADATA;
    }

    public StatementType visitChildren(RuleNode node) {
        int n = node.getChildCount();

        for (int i = 0; i < n; ++i) {
            ParseTree c = node.getChild(i);
            StatementType result = c.accept(this);
            if (result != null) {
                return result;
            }
        }

        return StatementType.UNKNOWN;
    }
}
