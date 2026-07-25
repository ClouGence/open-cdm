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

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.maxcompute.sql.parser.antlr.McParserParser;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.parse.AntlrStatementParser;
import com.clougence.sql.common.parser.AbstractSplitAnalysisSpi;

public class McSplitAnalysisSpi extends AbstractSplitAnalysisSpi {

    protected DslProvider dslProvider() {
        return McSqlDslProvider.INSTANCE;
    }

    protected AbstractParseTreeVisitor<SplitQueryType> splitVisitor() {
        return McSplitVisitor.INSTANCE;
    }

    @Override
    protected SplitQueryType additionalType(ParseTree tree) {
        if (tree instanceof McParserParser.Select_stmtContext && hasDmlOwner(tree)) {
            return SplitQueryType.SELECT;
        }
        if (tree instanceof McParserParser.AlterTableAddColumnsContext) {
            return SplitQueryType.ADD_COLUMN;
        }
        if (tree instanceof McParserParser.AlterTableDropColumnsContext) {
            return SplitQueryType.DROP_COLUMN;
        }
        if (tree instanceof McParserParser.AlterTableChangeColumnNameContext) {
            return SplitQueryType.RENAME_COLUMN;
        }
        if (tree instanceof McParserParser.AlterTableChangeColumnContext || tree instanceof McParserParser.AlterTableChangeColumnNullContext) {
            return SplitQueryType.ALTER_COLUMN;
        }
        if (tree instanceof McParserParser.AlterTableChangeColumnCommentContext) {
            return SplitQueryType.COMMENT_COLUMN;
        }
        if (tree instanceof McParserParser.AlterTableReanmeContext) {
            return SplitQueryType.RENAME_TABLE;
        }
        if (tree instanceof McParserParser.AlterTableCommentContext) {
            return SplitQueryType.COMMENT_TABLE;
        }
        if (tree instanceof McParserParser.Comment_clauseContext comment && comment.getParent() instanceof McParserParser.CreateTableColumnContext) {
            return SplitQueryType.COMMENT_TABLE;
        }
        if (tree instanceof McParserParser.Comment_clauseContext comment && comment.getParent() instanceof McParserParser.AlterTableChangeColumnContext) {
            return SplitQueryType.COMMENT_COLUMN;
        }
        if (tree instanceof McParserParser.Create_table_columns_itemContext column && column.T_COMMENT() != null) {
            return SplitQueryType.COMMENT_COLUMN;
        }
        if (tree instanceof McParserParser.AlterTablePartitionContext partition) {
            if (partition.T_ADD2() != null) {
                return SplitQueryType.ADD_PARTITION;
            }
            if (partition.T_DROP() != null) {
                return SplitQueryType.DROP_PARTITION;
            }
            return SplitQueryType.ALTER_PARTITION;
        }
        return null;
    }

    @Override
    protected List<SplitScript> collectChildren(ParserRuleContext context, CommonTokenStream tokens) {
        McParserParser.Select_stmtContext query = viewQuery(context);
        return query == null ? Collections.emptyList() : List.of(createChild(query, tokens, Set.of(SplitQueryType.SELECT), Collections.emptyList()));
    }

    private boolean hasDmlOwner(ParseTree tree) {
        boolean dml = false;
        for (ParseTree parent = tree.getParent(); parent != null; parent = parent.getParent()) {
            if (parent instanceof McParserParser.Insert_stmtContext || parent instanceof McParserParser.Update_stmtContext || parent instanceof McParserParser.Delete_stmtContext) {
                dml = true;
            }
            if (parent instanceof McParserParser.Create_table_stmtContext || parent instanceof McParserParser.Create_view_stmtContext
                || parent instanceof McParserParser.Create_materialized_view_stmtContext) {
                return false;
            }
        }
        return dml;
    }

    private McParserParser.Select_stmtContext viewQuery(ParseTree tree) {
        if (tree instanceof McParserParser.Create_view_stmtContext view) {
            return view.select_stmt();
        }
        if (tree instanceof McParserParser.Create_materialized_view_stmtContext view) {
            return view.select_stmt();
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            McParserParser.Select_stmtContext result = viewQuery(tree.getChild(i));
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    @Override
    protected void parseRoot(Parser parser) {
        ((McParserParser) parser).block();
    }

    @Override
    protected boolean isStatementContext(ParserRuleContext context) {
        return context instanceof McParserParser.StmtContext && context.getParent() instanceof McParserParser.BlockContext;
    }

    @Override
    protected AntlrStatementParser statementParser() {
        return new McSqlAntlrStatementParser();
    }
}
