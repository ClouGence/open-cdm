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
package com.clougence.sql.db2.parser;

import java.util.Collections;
import java.util.List;
import java.util.Set;

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
import com.clougence.sql.db2.parser.antlr.Db2SqlParser;

public class Db2SplitAnalysisSpi extends AbstractSplitAnalysisSpi {

    protected DslProvider dslProvider() {
        return Db2DslProvider.INSTANCE;
    }

    protected AbstractParseTreeVisitor<SplitQueryType> splitVisitor() {
        return Db2SplitVisitor.INSTANCE;
    }

    @Override
    protected SplitQueryType additionalType(ParseTree tree) {
        if (tree instanceof Db2SqlParser.Insert_statementContext ctx && !ctx.fullselect().isEmpty()) {
            return SplitQueryType.SELECT;
        }
        if (!(tree instanceof Db2SqlParser.Alter_table_optsContext ctx)) {
            return null;
        }
        if (ctx.ADD() != null) {
            if (ctx.column_definition() != null) {
                return SplitQueryType.ADD_COLUMN;
            }
            if (ctx.index_name() != null) {
                return SplitQueryType.ADD_INDEX;
            }
            if (ctx.unique_constraint() != null || ctx.referential_constraint() != null || ctx.check_constraint() != null) {
                return SplitQueryType.ADD_CONSTRAINT;
            }
        }
        if (ctx.ALTER() != null && ctx.column_alteration() != null) {
            return SplitQueryType.ALTER_COLUMN;
        }
        if (ctx.RENAME() != null) {
            return ctx.s != null && ctx.t != null ? SplitQueryType.RENAME_COLUMN : SplitQueryType.RENAME_TABLE;
        }
        if (!ctx.DROP().isEmpty()) {
            if (ctx.PRIMARY() != null || ctx.FOREIGN() != null || ctx.UNIQUE() != null || ctx.CHECK() != null || ctx.CONSTRAINT() != null) {
                return SplitQueryType.DROP_CONSTRAINT;
            }
            if (!ctx.column_name().isEmpty()) {
                return SplitQueryType.DROP_COLUMN;
            }
        }
        return null;
    }

    @Override
    protected List<SplitScript> collectChildren(ParserRuleContext context, CommonTokenStream tokens) {
        Db2SqlParser.Create_view_statementContext view = findContext(context, Db2SqlParser.Create_view_statementContext.class);
        if (view == null || view.fullselect() == null) {
            return Collections.emptyList();
        }
        return List.of(createChild(view.fullselect(), tokens, Set.of(SplitQueryType.SELECT), Collections.emptyList()));
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
        ((Db2SqlParser) parser).db2_file();
    }

    @Override
    protected boolean isStatementContext(ParserRuleContext context) {
        return context instanceof Db2SqlParser.Sql_statementContext && context.getParent() instanceof Db2SqlParser.BatchContext;
    }

    @Override
    protected AntlrStatementParser statementParser() {
        return new Db2AntlrStatementParser();
    }
}
