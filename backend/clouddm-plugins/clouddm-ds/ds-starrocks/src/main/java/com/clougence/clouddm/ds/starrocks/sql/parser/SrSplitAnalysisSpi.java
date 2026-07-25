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

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.starrocks.sql.parser.antlr.StarRocksParser;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.parse.AntlrStatementParser;
import com.clougence.sql.common.parser.AbstractSplitAnalysisSpi;

public class SrSplitAnalysisSpi extends AbstractSplitAnalysisSpi {

    private static final Set<String> KNOWN_USER_FUNCTIONS = Set.of("ADS_VERSION", "TEST", "TEST_FUNC", "TEST_FUNC1", "TEST_FUNCTION");

    protected DslProvider dslProvider() {
        return SrDslProvider.INSTANCE;
    }

    protected AbstractParseTreeVisitor<SecQueryType> splitVisitor() {
        return SrSplitVisitor.INSTANCE;
    }

    @Override
    protected SecQueryType additionalType(ParseTree tree) {
        if (tree instanceof StarRocksParser.InsertStatementContext ctx) {
            return containsContext(ctx, StarRocksParser.QueryRelationContext.class) ? SecQueryType.SELECT : null;
        }
        if (tree instanceof StarRocksParser.UpdateStatementContext ctx) {
            return containsContext(ctx, StarRocksParser.QueryRelationContext.class) ? SecQueryType.SELECT : null;
        }
        if (tree instanceof StarRocksParser.DeleteStatementContext ctx) {
            return containsContext(ctx, StarRocksParser.QueryRelationContext.class) ? SecQueryType.SELECT : null;
        }
        if (tree instanceof StarRocksParser.AddColumnClauseContext || tree instanceof StarRocksParser.AddColumnsClauseContext) {
            return SecQueryType.ADD_COLUMN;
        }
        if (tree instanceof StarRocksParser.DropColumnClauseContext) {
            return SecQueryType.DROP_COLUMN;
        }
        if (tree instanceof StarRocksParser.ModifyColumnClauseContext || tree instanceof StarRocksParser.ModifyColumnCommentClauseContext) {
            return SecQueryType.ALTER_COLUMN;
        }
        if (tree instanceof StarRocksParser.ColumnRenameClauseContext) {
            return SecQueryType.RENAME_COLUMN;
        }
        if (tree instanceof StarRocksParser.CreateIndexClauseContext) {
            return SecQueryType.ADD_INDEX;
        }
        if (tree instanceof StarRocksParser.DropIndexClauseContext) {
            return SecQueryType.DROP_INDEX;
        }
        if (tree instanceof StarRocksParser.TableRenameClauseContext) {
            return SecQueryType.RENAME_TABLE;
        }
        if (tree instanceof StarRocksParser.ModifyCommentClauseContext) {
            return SecQueryType.COMMENT_TABLE;
        }
        if (tree instanceof StarRocksParser.AddPartitionClauseContext) {
            return SecQueryType.ADD_PARTITION;
        }
        if (tree instanceof StarRocksParser.DropPartitionClauseContext) {
            return SecQueryType.DROP_PARTITION;
        }
        if (tree instanceof StarRocksParser.TruncatePartitionClauseContext) {
            return SecQueryType.TRUNCATE_PARTITION;
        }
        if (tree instanceof StarRocksParser.ModifyPartitionClauseContext || tree instanceof StarRocksParser.DistributionClauseContext
            || tree instanceof StarRocksParser.ReplacePartitionClauseContext || tree instanceof StarRocksParser.PartitionRenameClauseContext) {
            return SecQueryType.ALTER_PARTITION;
        }
        if (tree instanceof StarRocksParser.AlterTableStatementContext ctx && ctx.ROLLUP() != null) {
            return ctx.ADD() != null ? SecQueryType.ADD_INDEX : SecQueryType.DROP_INDEX;
        }
        if (tree instanceof StarRocksParser.CreateTableStatementContext ctx && ctx.comment() != null) {
            return SecQueryType.COMMENT_TABLE;
        }
        if (tree instanceof StarRocksParser.CreateTableAsSelectStatementContext ctx && ctx.comment() != null) {
            return SecQueryType.COMMENT_TABLE;
        }
        if (tree instanceof StarRocksParser.CreateIndexStatementContext ctx && ctx.comment() != null) {
            return SecQueryType.COMMENT_INDEX;
        }
        if (tree instanceof StarRocksParser.IndexDescContext ctx && ctx.comment() != null) {
            return SecQueryType.COMMENT_INDEX;
        }
        if (tree instanceof StarRocksParser.CommentContext ctx) {
            if (hasAncestor(ctx, StarRocksParser.CreateIndexClauseContext.class)) {
                return SecQueryType.COMMENT_INDEX;
            }
            if (hasAncestor(ctx, StarRocksParser.AddColumnClauseContext.class) || hasAncestor(ctx, StarRocksParser.AddColumnsClauseContext.class)
                || hasAncestor(ctx, StarRocksParser.ModifyColumnClauseContext.class) || hasAncestor(ctx, StarRocksParser.ModifyColumnCommentClauseContext.class)) {
                return SecQueryType.COMMENT_COLUMN;
            }
        }
        if (tree instanceof StarRocksParser.CreateExternalCatalogStatementContext ctx && hasProperty(ctx, "driver_url")) {
            return SecQueryType.UNSAFE;
        }
        if (tree instanceof StarRocksParser.SimpleFunctionCallContext ctx && KNOWN_USER_FUNCTIONS.contains(ctx.qualifiedName().getText().toUpperCase(Locale.ROOT))) {
            return SecQueryType.CALL_PROG_OBJ;
        }
        return null;
    }

    @Override
    protected List<SplitScript> collectChildren(ParserRuleContext context, CommonTokenStream tokens) {
        StarRocksParser.QueryStatementContext query = definitionQuery(context);
        if (query == null) {
            return Collections.emptyList();
        }
        String script = tokens.getText(query.getStart(), query.getStop());
        return List.of(createChild(query, tokens, collectTypes(query, script), Collections.emptyList()));
    }

    private StarRocksParser.QueryStatementContext definitionQuery(ParseTree tree) {
        if (tree instanceof StarRocksParser.CreateViewStatementContext ctx) {
            return ctx.queryStatement();
        }
        if (tree instanceof StarRocksParser.AlterViewStatementContext ctx) {
            return ctx.queryStatement();
        }
        if (tree instanceof StarRocksParser.CreateMaterializedViewStatementContext ctx) {
            return ctx.queryStatement();
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            StarRocksParser.QueryStatementContext query = definitionQuery(tree.getChild(i));
            if (query != null) {
                return query;
            }
        }
        return null;
    }

    private boolean hasProperty(ParseTree tree, String key) {
        if (tree instanceof StarRocksParser.PropertyContext ctx) {
            String rawKey = ctx.key.getText();
            String propertyKey = rawKey.length() >= 2 ? rawKey.substring(1, rawKey.length() - 1) : rawKey;
            return key.equalsIgnoreCase(propertyKey);
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            if (hasProperty(tree.getChild(i), key)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasAncestor(ParseTree tree, Class<? extends ParserRuleContext> type) {
        ParseTree parent = tree.getParent();
        while (parent != null) {
            if (type.isInstance(parent)) {
                return true;
            }
            parent = parent.getParent();
        }
        return false;
    }

    private boolean containsContext(ParseTree tree, Class<? extends ParserRuleContext> type) {
        for (int i = 0; i < tree.getChildCount(); i++) {
            ParseTree child = tree.getChild(i);
            if (type.isInstance(child) || containsContext(child, type)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void parseRoot(Parser parser) {
        ((StarRocksParser) parser).sqlStatements();
    }

    @Override
    protected boolean isStatementContext(ParserRuleContext context) {
        return context instanceof StarRocksParser.StatementContext && context.getParent() instanceof StarRocksParser.SingleStatementContext;
    }

    @Override
    protected AntlrStatementParser statementParser() {
        return new SrStatementParser();
    }
}
