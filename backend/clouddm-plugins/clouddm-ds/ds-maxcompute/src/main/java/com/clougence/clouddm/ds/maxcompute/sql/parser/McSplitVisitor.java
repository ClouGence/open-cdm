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
package com.clougence.clouddm.ds.maxcompute.sql.parser;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;

import com.clougence.clouddm.ds.maxcompute.sql.parser.antlr.McParserBaseVisitor;
import com.clougence.clouddm.ds.maxcompute.sql.parser.antlr.McParserParser;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;

public class McSplitVisitor extends McParserBaseVisitor<SplitQueryType> {

    public static final AbstractParseTreeVisitor<SplitQueryType> INSTANCE = new McSplitVisitor();

    public McSplitVisitor(){
    }

    @Override
    public SplitQueryType visitInsert_stmt(McParserParser.Insert_stmtContext ctx) {
        return SplitQueryType.INSERT;
    }

    @Override
    public SplitQueryType visitCall_stmt(McParserParser.Call_stmtContext ctx) {
        return SplitQueryType.CALL_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitShowTablePartitions(McParserParser.ShowTablePartitionsContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitDescribe_stmt(McParserParser.Describe_stmtContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowHistoryTables(McParserParser.ShowHistoryTablesContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowHistoryTable(McParserParser.ShowHistoryTableContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowTableColumnStatics(McParserParser.ShowTableColumnStaticsContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitShowCreateTable(McParserParser.ShowCreateTableContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowTables(McParserParser.ShowTablesContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitDropMView(McParserParser.DropMViewContext ctx) {
        return SplitQueryType.DROP_VIEW;
    }

    @Override
    public SplitQueryType visitAlter_materialized_view_stmt(McParserParser.Alter_materialized_view_stmtContext ctx) {
        return SplitQueryType.ALTER_VIEW;
    }

    @Override
    public SplitQueryType visitAssignment_stmt(McParserParser.Assignment_stmtContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitCreate_materialized_view_stmt(McParserParser.Create_materialized_view_stmtContext ctx) {
        return SplitQueryType.CREATE_VIEW;
    }

    @Override
    public SplitQueryType visitDropView(McParserParser.DropViewContext ctx) {
        return SplitQueryType.DROP_VIEW;
    }

    @Override
    public SplitQueryType visitCreate_view_stmt(McParserParser.Create_view_stmtContext ctx) {
        return SplitQueryType.CREATE_VIEW;
    }

    @Override
    public SplitQueryType visitTruncate_stmt(McParserParser.Truncate_stmtContext ctx) {
        return SplitQueryType.TRUNCATE_TABLE;
    }

    @Override
    public SplitQueryType visitAlter_table_stmt(McParserParser.Alter_table_stmtContext ctx) {
        return SplitQueryType.ALTER_TABLE;
    }

    @Override
    public SplitQueryType visitCreate_table_stmt(McParserParser.Create_table_stmtContext ctx) {
        if (ctx.create_table_definition() instanceof McParserParser.CreateTableColumnContext) {
            return SplitQueryType.CREATE_TABLE;
        } else if (ctx.create_table_definition() instanceof McParserParser.CreateTableLikeContext) {
            return SplitQueryType.CREATE_TABLE;
        } else {
            return SplitQueryType.CREATE_TABLE;
        }
    }

    @Override
    public SplitQueryType visitAnalyze_table_stmt(McParserParser.Analyze_table_stmtContext ctx) {
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitDropTable(McParserParser.DropTableContext ctx) {
        return SplitQueryType.DROP_TABLE;
    }

    @Override
    public SplitQueryType visitSelect_stmt(McParserParser.Select_stmtContext ctx) {
        return SplitQueryType.SELECT;
    }

    @Override
    public SplitQueryType visitDelete_stmt(McParserParser.Delete_stmtContext ctx) {
        return SplitQueryType.DELETE;
    }

    @Override
    public SplitQueryType visitDropSchema(McParserParser.DropSchemaContext ctx) {
        return SplitQueryType.DROP_SCHEMA;
    }

    @Override
    public SplitQueryType visitCreate_database_stmt(McParserParser.Create_database_stmtContext ctx) {
        return SplitQueryType.CREATE_SCHEMA;
    }

    @Override
    public SplitQueryType visitUpdate_stmt(McParserParser.Update_stmtContext ctx) {
        return SplitQueryType.UPDATE;
    }

    @Override
    public SplitQueryType visitShowRoles(McParserParser.ShowRolesContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowUsers(McParserParser.ShowUsersContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowTrustProjects(McParserParser.ShowTrustProjectsContext ctx) {
        return SplitQueryType.METADATA;
    }

    public SplitQueryType visitChildren(RuleNode node) {
        int n = node.getChildCount();

        for (int i = 0; i < n; ++i) {
            ParseTree c = node.getChild(i);
            SplitQueryType result = c.accept(this);
            if (result != null) {
                return result;
            }
        }

        return SplitQueryType.UNKNOWN;
    }
}
