package com.clougence.clouddm.ds.maxcompute.analysis;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;

import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.ds.maxcompute.parser.antlr.McParserBaseVisitor;
import com.clougence.clouddm.ds.maxcompute.parser.antlr.McParserParser;

public class McSplitVisitor extends McParserBaseVisitor<SecQueryType> {

    public static final AbstractParseTreeVisitor<SecQueryType> INSTANCE = new McSplitVisitor();

    public McSplitVisitor(){
    }

    @Override
    public SecQueryType visitInsert_stmt(McParserParser.Insert_stmtContext ctx) {
        return SecQueryType.INSERT;
    }

    @Override
    public SecQueryType visitShowTablePartitions(McParserParser.ShowTablePartitionsContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitDescribe_stmt(McParserParser.Describe_stmtContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowHistoryTables(McParserParser.ShowHistoryTablesContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowHistoryTable(McParserParser.ShowHistoryTableContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowTableColumnStatics(McParserParser.ShowTableColumnStaticsContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowCreateTable(McParserParser.ShowCreateTableContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowTables(McParserParser.ShowTablesContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitDropMView(McParserParser.DropMViewContext ctx) {
        return SecQueryType.DROP_VIEW;
    }

    @Override
    public SecQueryType visitAlter_materialized_view_stmt(McParserParser.Alter_materialized_view_stmtContext ctx) {
        return SecQueryType.ALTER_VIEW;
    }

    @Override
    public SecQueryType visitAssignment_stmt(McParserParser.Assignment_stmtContext ctx) {
        return SecQueryType.CONFIG_WRITE;
    }

    @Override
    public SecQueryType visitCreate_materialized_view_stmt(McParserParser.Create_materialized_view_stmtContext ctx) {
        return SecQueryType.CREATE_VIEW;
    }

    @Override
    public SecQueryType visitDropView(McParserParser.DropViewContext ctx) {
        return SecQueryType.DROP_VIEW;
    }

    @Override
    public SecQueryType visitCreate_view_stmt(McParserParser.Create_view_stmtContext ctx) {
        return SecQueryType.CREATE_VIEW;
    }

    @Override
    public SecQueryType visitTruncate_stmt(McParserParser.Truncate_stmtContext ctx) {
        return SecQueryType.TRUNCATE;
    }

    @Override
    public SecQueryType visitAlter_table_stmt(McParserParser.Alter_table_stmtContext ctx) {
        return SecQueryType.ALTER_TABLE;
    }

    @Override
    public SecQueryType visitCreate_table_stmt(McParserParser.Create_table_stmtContext ctx) {
        if (ctx.create_table_definition() instanceof McParserParser.CreateTableColumnContext) {
            return SecQueryType.CREATE_TABLE;
        } else if (ctx.create_table_definition() instanceof McParserParser.CreateTableLikeContext) {
            return SecQueryType.CREATE_TABLE_LIKE;
        } else {
            return SecQueryType.CREATE_TABLE_SELECT;
        }
    }

    @Override
    public SecQueryType visitAnalyze_table_stmt(McParserParser.Analyze_table_stmtContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitDropTable(McParserParser.DropTableContext ctx) {
        return SecQueryType.DROP_TABLE;
    }

    @Override
    public SecQueryType visitSelect_stmt(McParserParser.Select_stmtContext ctx) {
        return SecQueryType.SELECT;
    }

    @Override
    public SecQueryType visitDelete_stmt(McParserParser.Delete_stmtContext ctx) {
        return SecQueryType.DELETE;
    }

    @Override
    public SecQueryType visitDropSchema(McParserParser.DropSchemaContext ctx) {
        return SecQueryType.DROP_SCHEMA;
    }

    @Override
    public SecQueryType visitCreate_database_stmt(McParserParser.Create_database_stmtContext ctx) {
        return SecQueryType.CREATE_SCHEMA;
    }

    @Override
    public SecQueryType visitUpdate_stmt(McParserParser.Update_stmtContext ctx) {
        return SecQueryType.UPDATE;
    }

    @Override
    public SecQueryType visitShowRoles(McParserParser.ShowRolesContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowUsers(McParserParser.ShowUsersContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitShowTrustProjects(McParserParser.ShowTrustProjectsContext ctx) {
        return SecQueryType.SHOW;
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
