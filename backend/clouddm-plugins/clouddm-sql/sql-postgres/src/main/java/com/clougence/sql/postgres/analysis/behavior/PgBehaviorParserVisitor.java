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
package com.clougence.sql.postgres.analysis.behavior;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementBehavior;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.postgres.parser.PgSplitVisitor;
import com.clougence.sql.postgres.parser.PostgresVersion;
import com.clougence.sql.postgres.parser.antlr.PgSqlParser;

final class PgBehaviorParserVisitor extends AbstractParseTreeVisitor<Void> {

    private final Parser                                                        parser;
    private final PostgresVersion                                               version;
    private final Map<UmiTypes, Object>                                         levels;
    private final int                                                           baseLine;
    private final int                                                           baseColumn;
    private final Predicate<String>                                             dialectSystemFunction;
    private final Function<PostgresVersion, PgSplitVisitor>                     splitVisitorFactory;
    private final Function<PgSqlParser.ExtensionstmtContext, StatementBehavior> extensionBehavior;
    private final List<StatementBehavior>                                       behaviors = new ArrayList<>();

    PgBehaviorParserVisitor(Parser parser, PostgresVersion version, Map<UmiTypes, Object> levels, int baseLine, int baseColumn, Predicate<String> dialectSystemFunction,
                            Function<PostgresVersion, PgSplitVisitor> splitVisitorFactory, Function<PgSqlParser.ExtensionstmtContext, StatementBehavior> extensionBehavior){
        this.parser = parser;
        this.version = version;
        this.levels = levels;
        this.baseLine = baseLine;
        this.baseColumn = baseColumn;
        this.dialectSystemFunction = dialectSystemFunction;
        this.splitVisitorFactory = splitVisitorFactory;
        this.extensionBehavior = extensionBehavior;
    }

    List<StatementBehavior> behaviors() {
        return behaviors;
    }

    @Override
    public Void visit(ParseTree tree) {
        if (tree instanceof PgSqlParser.StmtContext statement && statement.extensionstmt() != null) {
            StatementBehavior behavior = extensionBehavior.apply(statement.extensionstmt());
            if (behavior != null) {
                behaviors.add(behavior);
                return null;
            }
        }
        SplitQueryType statementType = splitVisitorFactory.apply(version).visit(tree);
        PgStatementBehaviorVisitor visitor = new PgStatementBehaviorVisitor(parser, version, statementType, levels, baseLine, baseColumn, dialectSystemFunction);
        visitor.visit(tree);
        behaviors.add(visitor.behavior());
        return null;
    }
}
