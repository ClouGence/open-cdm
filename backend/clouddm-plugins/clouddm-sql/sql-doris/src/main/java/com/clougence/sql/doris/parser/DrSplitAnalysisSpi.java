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
package com.clougence.sql.doris.parser;

import java.util.*;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.parse.AntlrStatementParser;
import com.clougence.sql.common.parser.AbstractSplitAnalysisSpi;
import com.clougence.sql.doris.parser.antlr.DorisParser;

public class DrSplitAnalysisSpi extends AbstractSplitAnalysisSpi {

    private static final Set<String> KNOWN_USER_FUNCTIONS     = Set.of("ads_version", "test", "test_func", "test_func1", "test_function");
    private static final Set<String> EXTERNAL_TABLE_FUNCTIONS = Set.of("azure", "gcs", "hdfs", "http_stream", "jdbc", "local", "s3");
    private final DrDslProvider      provider;

    public DrSplitAnalysisSpi(DrDslProvider provider){
        this.provider = provider;
    }

    protected DslProvider dslProvider() {
        return this.provider;
    }

    protected AbstractParseTreeVisitor<SplitQueryType> splitVisitor() {
        return DrSplitVisitor.INSTANCE;
    }

    @Override
    protected Set<SplitQueryType> collectTypes(ParserRuleContext context, String script) {
        DorisParser.CreateTableContext createTable = findContext(context, DorisParser.CreateTableContext.class);
        if (createTable == null) {
            return super.collectTypes(context, script);
        }

        Set<SplitQueryType> types = new LinkedHashSet<>();
        types.add(normalizeType(context.accept(splitVisitor()), script));
        if (createTable.columnDefs() != null) {
            types.add(SplitQueryType.ADD_COLUMN);
            for (DorisParser.ColumnDefContext column : createTable.columnDefs().columnDef()) {
                if (column.comment != null) {
                    types.add(SplitQueryType.COMMENT_COLUMN);
                }
            }
        } else if (createTable.ctasCols != null) {
            types.add(SplitQueryType.ADD_COLUMN);
        }
        if (createTable.indexDefs() != null) {
            types.add(SplitQueryType.ADD_INDEX);
            for (DorisParser.IndexDefContext index : createTable.indexDefs().indexDef()) {
                if (index.comment != null) {
                    types.add(SplitQueryType.COMMENT_INDEX);
                }
            }
        }
        if (createTable.COMMENT() != null) {
            types.add(SplitQueryType.COMMENT_TABLE);
        }
        return types;
    }

    @Override
    protected SplitQueryType additionalType(ParseTree tree) {
        if (tree instanceof DorisParser.QuerySpecificationContext && isExecutedDmlQuery(tree)) {
            return SplitQueryType.SELECT;
        }
        if (tree instanceof DorisParser.InsertIntoTVFContext) {
            return SplitQueryType.DATA_EXPORT;
        }
        if (tree instanceof DorisParser.OutFileClauseContext) {
            return SplitQueryType.DATA_EXPORT;
        }
        if (tree instanceof DorisParser.TableValuedFunctionContext function && isExecutedTableFunction(function)
            && EXTERNAL_TABLE_FUNCTIONS.contains(function.tvfName.getText().toLowerCase(Locale.ROOT))) {
            return SplitQueryType.DATA_IMPORT;
        }
        if (tree instanceof DorisParser.AddColumnClauseContext || tree instanceof DorisParser.AddColumnsClauseContext) {
            return SplitQueryType.ADD_COLUMN;
        }
        if (tree instanceof DorisParser.DropColumnClauseContext) {
            return SplitQueryType.DROP_COLUMN;
        }
        if (tree instanceof DorisParser.ModifyColumnClauseContext || tree instanceof DorisParser.ReorderColumnsClauseContext) {
            return SplitQueryType.ALTER_COLUMN;
        }
        if (tree instanceof DorisParser.RenameColumnClauseContext) {
            return SplitQueryType.RENAME_COLUMN;
        }
        if (tree instanceof DorisParser.AddPartitionClauseContext || tree instanceof DorisParser.AlterMultiPartitionClauseContext) {
            return SplitQueryType.ADD_PARTITION;
        }
        if (tree instanceof DorisParser.DropPartitionClauseContext) {
            return SplitQueryType.DROP_PARTITION;
        }
        if (tree instanceof DorisParser.ModifyPartitionClauseContext || tree instanceof DorisParser.ReplacePartitionClauseContext
            || tree instanceof DorisParser.RenamePartitionClauseContext) {
            return SplitQueryType.ALTER_PARTITION;
        }
        if (tree instanceof DorisParser.AddIndexClauseContext || tree instanceof DorisParser.AlterTableAddRollupContext) {
            return SplitQueryType.ADD_INDEX;
        }
        if (tree instanceof DorisParser.DropIndexClauseContext || tree instanceof DorisParser.AlterTableDropRollupContext) {
            return SplitQueryType.DROP_INDEX;
        }
        if (tree instanceof DorisParser.RenameClauseContext) {
            return SplitQueryType.RENAME_TABLE;
        }
        if (tree instanceof DorisParser.ModifyTableCommentClauseContext) {
            return SplitQueryType.COMMENT_TABLE;
        }
        if (tree instanceof DorisParser.ModifyColumnCommentClauseContext) {
            return SplitQueryType.COMMENT_COLUMN;
        }
        if (tree instanceof DorisParser.AddConstraintContext) {
            return SplitQueryType.ADD_CONSTRAINT;
        }
        if (tree instanceof DorisParser.DropConstraintContext) {
            return SplitQueryType.DROP_CONSTRAINT;
        }
        if (tree instanceof DorisParser.CreateTableContext ctx && ctx.COMMENT() != null) {
            return SplitQueryType.COMMENT_TABLE;
        }
        if (tree instanceof DorisParser.ColumnDefContext ctx && ctx.comment != null) {
            return SplitQueryType.COMMENT_COLUMN;
        }
        if (tree instanceof DorisParser.IndexDefContext ctx && ctx.comment != null) {
            return SplitQueryType.COMMENT_INDEX;
        }
        if (tree instanceof DorisParser.CreateIndexContext ctx && ctx.STRING_LITERAL() != null) {
            return SplitQueryType.COMMENT_INDEX;
        }
        if (tree instanceof DorisParser.FunctionCallExpressionContext ctx && isExecutedFunction(ctx)) {
            return functionType(ctx);
        }
        return null;
    }

    @Override
    protected List<SplitScript> collectChildren(ParserRuleContext context, CommonTokenStream tokens) {
        ParserRuleContext body = definitionBody(context);
        if (body == null) {
            return Collections.emptyList();
        }
        return List.of(createChild(body, tokens, collectBodyTypes(body, tokens.getText(body)), Collections.emptyList()));
    }

    private boolean isExecutedDmlQuery(ParseTree tree) {
        if (hasAncestor(tree, DorisParser.CreateScheduledJobContext.class)) {
            return false;
        }
        for (ParseTree current = tree.getParent(); current != null; current = current.getParent()) {
            if (current instanceof DorisParser.InsertTableContext || current instanceof DorisParser.InsertIntoTVFContext || current instanceof DorisParser.UpdateContext
                || current instanceof DorisParser.DeleteContext || current instanceof DorisParser.MergeIntoContext) {
                return true;
            }
            if (current instanceof DorisParser.CreateTableContext || current instanceof DorisParser.CreateViewContext || current instanceof DorisParser.CreateMTMVContext
                || current instanceof DorisParser.AlterViewContext || current instanceof DorisParser.CreateScheduledJobContext) {
                return false;
            }
        }
        return false;
    }

    private boolean isExecutedFunction(ParseTree tree) {
        if (hasAncestor(tree, DorisParser.CreateScheduledJobContext.class)) {
            return false;
        }
        for (ParseTree current = tree.getParent(); current != null; current = current.getParent()) {
            if (current instanceof DorisParser.StatementDefaultContext || current instanceof DorisParser.InsertTableContext || current instanceof DorisParser.InsertIntoTVFContext
                || current instanceof DorisParser.UpdateContext || current instanceof DorisParser.DeleteContext || current instanceof DorisParser.MergeIntoContext) {
                return true;
            }
            if (current instanceof DorisParser.CreateTableContext || current instanceof DorisParser.CreateAliasFunctionContext
                || current instanceof DorisParser.CreateUserDefineFunctionContext) {
                return false;
            }
        }
        return false;
    }

    private boolean isExecutedTableFunction(ParseTree tree) {
        if (hasAncestor(tree, DorisParser.CreateScheduledJobContext.class)) {
            return false;
        }
        for (ParseTree current = tree.getParent(); current != null; current = current.getParent()) {
            if (current instanceof DorisParser.CreateTableContext || current instanceof DorisParser.CreateViewContext || current instanceof DorisParser.CreateMTMVContext
                || current instanceof DorisParser.AlterViewContext || current instanceof DorisParser.CreateScheduledJobContext) {
                return false;
            }
            if (current instanceof DorisParser.StatementDefaultContext || current instanceof DorisParser.InsertTableContext || current instanceof DorisParser.InsertIntoTVFContext
                || current instanceof DorisParser.UpdateContext || current instanceof DorisParser.DeleteContext || current instanceof DorisParser.MergeIntoContext) {
                return true;
            }
        }
        return false;
    }

    private Set<SplitQueryType> collectBodyTypes(ParserRuleContext body, String script) {
        Set<SplitQueryType> types = new LinkedHashSet<>();
        types.add(body instanceof DorisParser.QueryContext ? SplitQueryType.SELECT : normalizeType(body.accept(splitVisitor()), script));
        collectBodyAdditionalTypes(body, types);
        return types;
    }

    private void collectBodyAdditionalTypes(ParseTree tree, Set<SplitQueryType> types) {
        if (tree instanceof DorisParser.QuerySpecificationContext) {
            types.add(SplitQueryType.SELECT);
        } else if (tree instanceof DorisParser.InsertIntoTVFContext) {
            types.add(SplitQueryType.DATA_EXPORT);
        } else if (tree instanceof DorisParser.OutFileClauseContext) {
            types.add(SplitQueryType.DATA_EXPORT);
        } else if (tree instanceof DorisParser.TableValuedFunctionContext function && EXTERNAL_TABLE_FUNCTIONS.contains(function.tvfName.getText().toLowerCase(Locale.ROOT))) {
            types.add(SplitQueryType.DATA_IMPORT);
        } else if (tree instanceof DorisParser.FunctionCallExpressionContext function) {
            SplitQueryType type = functionType(function);
            if (type != null) {
                types.add(type);
            }
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            collectBodyAdditionalTypes(tree.getChild(i), types);
        }
    }

    private SplitQueryType functionType(DorisParser.FunctionCallExpressionContext function) {
        String name = function.functionIdentifier().functionNameIdentifier().getText().toLowerCase(Locale.ROOT);
        return function.functionIdentifier().dbName != null || KNOWN_USER_FUNCTIONS.contains(name) ? SplitQueryType.CALL_PROG_OBJ : null;
    }

    private boolean hasAncestor(ParseTree tree, Class<? extends ParseTree> type) {
        for (ParseTree current = tree.getParent(); current != null; current = current.getParent()) {
            if (type.isInstance(current)) {
                return true;
            }
        }
        return false;
    }

    private ParserRuleContext definitionBody(ParseTree tree) {
        DorisParser.CreateViewContext view = findContext(tree, DorisParser.CreateViewContext.class);
        if (view != null) {
            return view.query();
        }
        DorisParser.CreateMTMVContext materializedView = findContext(tree, DorisParser.CreateMTMVContext.class);
        if (materializedView != null) {
            return materializedView.query();
        }
        DorisParser.AlterViewContext alteredView = findContext(tree, DorisParser.AlterViewContext.class);
        if (alteredView != null && alteredView.query() != null) {
            return alteredView.query();
        }
        DorisParser.CreateScheduledJobContext job = findContext(tree, DorisParser.CreateScheduledJobContext.class);
        return job == null ? null : job.supportedDmlStatement();
    }

    private <T extends ParserRuleContext> T findContext(ParseTree tree, Class<T> type) {
        if (type.isInstance(tree)) {
            return type.cast(tree);
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            T result = findContext(tree.getChild(i), type);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    @Override
    protected void parseRoot(Parser parser) {
        ((DorisParser) parser).multiStatements();
    }

    @Override
    protected boolean isStatementContext(ParserRuleContext context) {
        return context instanceof DorisParser.StatementContext && context.getParent() instanceof DorisParser.MultiStatementsContext;
    }

    @Override
    protected AntlrStatementParser statementParser() {
        return new DrStatementParser();
    }
}
