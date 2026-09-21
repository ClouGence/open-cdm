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
package com.clougence.clouddm.ds.cloudberry.sql.parser;

import java.util.List;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.ds.cloudberry.sql.CbSqlEngineSpi;
import com.clougence.dslpaser.parse.AstSplitScript;
import com.clougence.sql.postgres.analysis.security.base.PgSqlParserBase;
import com.clougence.sql.postgres.parser.PgDslProvider;
import com.clougence.sql.postgres.parser.PostgresVersion;
import com.clougence.sql.postgres.parser.antlr.PgSqlParser;

public class CbDslProvider extends PgDslProvider {

    public CbDslProvider(PostgresVersion version){
        super(version, CbSqlEngineSpi.NAME);
    }

    @Override
    public Parser createParser(Lexer lexer) {
        PgSqlParserBase parser = (PgSqlParserBase) super.createParser(lexer);
        parser.setExtensionPredicates(CbSyntax::isExtensionStatement, CbSyntax::isDistributionStart, CbSyntax::isParallelRetrieve);
        return parser;
    }

    @Override
    public List<AstSplitScript> doSplit(Lexer lexer, Parser parser) {
        List<AstSplitScript> scripts = super.doSplit(lexer, parser);
        for (AstSplitScript script : scripts) {
            PgSqlParser.StmtContext statement = (PgSqlParser.StmtContext) script.getAstTree();
            if (statement.extensionstmt() != null) {
                CbStatementParser.parse(script.getScript());
                continue;
            }
            PgSqlParser.Pg_stmtContext pg = statement.pg_stmt();
            if (pg.createstmt() != null && pg.createstmt().extension_clause() != null) {
                CbSyntax.validateDistribution(pg.createstmt().extension_clause());
            }
            if (pg.createasstmt() != null && pg.createasstmt().extension_clause() != null) {
                CbSyntax.validateDistribution(pg.createasstmt().extension_clause());
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
}
