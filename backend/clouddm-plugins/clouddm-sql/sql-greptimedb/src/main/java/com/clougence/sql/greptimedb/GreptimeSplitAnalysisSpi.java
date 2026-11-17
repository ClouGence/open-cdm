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

package com.clougence.sql.greptimedb;

import java.util.List;
import java.util.Set;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.parse.AntlrStatementParser;
import com.clougence.sql.common.parser.AbstractSplitAnalysisSpi;
import com.clougence.sql.greptimedb.antlr.GreptimeSqlLexer;
import com.clougence.sql.greptimedb.antlr.GreptimeSqlParser;

final class GreptimeSplitAnalysisSpi extends AbstractSplitAnalysisSpi {

    @Override
    protected DslProvider dslProvider() {
        return GreptimeDslProvider.INSTANCE;
    }

    @Override
    protected AntlrStatementParser statementParser() {
        return GreptimeDslProvider.INSTANCE;
    }

    @Override
    protected AbstractParseTreeVisitor<SplitQueryType> splitVisitor() {
        return GreptimeSplitVisitor.INSTANCE;
    }

    @Override
    protected void parseRoot(Parser parser) {
        ((GreptimeSqlParser) parser).script();
    }

    @Override
    protected boolean isStatementContext(ParserRuleContext context) {
        return context instanceof GreptimeSqlParser.StatementContext;
    }

    @Override
    protected boolean isStatementTerminated(Parser parser) {
        int next = parser.getTokenStream().LA(1);
        return next == GreptimeSqlLexer.SEMI || next == Token.EOF;
    }

    @Override
    protected SplitQueryType additionalType(ParseTree tree) {
        if (tree instanceof GreptimeSqlParser.InsertStatementContext insert && insert.query() != null) {
            return SplitQueryType.SELECT;
        }
        if (tree instanceof GreptimeSqlParser.AlterTableContext alter && alter.newName != null) {
            return SplitQueryType.RENAME_TABLE;
        }
        if (tree instanceof GreptimeSqlParser.AlterColumnContext alterColumn) {
            if (alterColumn.ADD() != null) {
                return SplitQueryType.ADD_COLUMN;
            }
            if (alterColumn.DROP() != null && alterColumn.MODIFY() == null) {
                return SplitQueryType.DROP_COLUMN;
            }
            return SplitQueryType.ALTER_COLUMN;
        }
        return null;
    }

    @Override
    protected List<SplitScript> collectChildren(ParserRuleContext context, CommonTokenStream tokens) {
        var statement = (GreptimeSqlParser.StatementContext) context;
        if (statement.createView() != null) {
            return List.of(createChild(statement.createView().query(), tokens, Set.of(SplitQueryType.SELECT), List.of()));
        }
        return List.of();
    }
}
