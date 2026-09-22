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
package com.clougence.clouddm.ds.greenplum.sql.parser;

import java.util.List;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.ds.greenplum.sql.GpSqlEngineSpi;
import com.clougence.dslpaser.parse.AstSplitScript;
import com.clougence.sql.postgres.analysis.security.base.PgSqlParserBase;
import com.clougence.sql.postgres.parser.PgDslProvider;
import com.clougence.sql.postgres.parser.PostgresVersion;
import com.clougence.sql.postgres.parser.antlr.PgSqlParser;

public class GpDslProvider extends PgDslProvider {

    public GpDslProvider(PostgresVersion version){
        super(version, GpSqlEngineSpi.NAME);
    }

    @Override
    public Parser createParser(Lexer lexer) {
        PgSqlParserBase parser = (PgSqlParserBase) super.createParser(lexer);
        parser.setExtensionPredicates(GpStatementParser::isExtensionStatement, GpDslProvider::isDistributionStart, GpDslProvider::isParallelRetrieve);
        return parser;
    }

    @Override
    public List<AstSplitScript> doSplit(Lexer lexer, Parser parser) {
        List<AstSplitScript> scripts = super.doSplit(lexer, parser);
        for (AstSplitScript script : scripts) {
            PgSqlParser.StmtContext statement = (PgSqlParser.StmtContext) script.getAstTree();
            if (statement.extensionstmt() != null) {
                GpStatementParser.parse(parser.getTokenStream(), statement.extensionstmt());
            }
        }
        return scripts;
    }

    @Override
    public void doVisitor(Lexer lexer, Parser parser, AbstractParseTreeVisitor<?> visitor) {
        for (AstSplitScript script : doSplit(lexer, parser)) {
            visitor.visit(script.getAstTree());
        }
    }

    private static boolean isDistributionStart(TokenStream tokens) {
        if (!"DISTRIBUTED".equalsIgnoreCase(tokens.LT(1).getText())) {
            return false;
        }
        String mode = tokens.LT(2).getText();
        return "BY".equalsIgnoreCase(mode) || "RANDOMLY".equalsIgnoreCase(mode) || "REPLICATED".equalsIgnoreCase(mode);
    }

    private static boolean isParallelRetrieve(TokenStream tokens) {
        return "PARALLEL".equalsIgnoreCase(tokens.LT(1).getText()) && "RETRIEVE".equalsIgnoreCase(tokens.LT(2).getText());
    }
}
