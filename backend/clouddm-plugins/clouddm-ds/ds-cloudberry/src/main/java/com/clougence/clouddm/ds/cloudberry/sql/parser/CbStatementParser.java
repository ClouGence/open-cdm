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

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

import com.clougence.clouddm.ds.cloudberry.sql.parser.antlr.CbSqlParser;
import com.clougence.dslpaser.parse.SyntaxErrorListener;
import com.clougence.sql.postgres.parser.PostgresVersion;
import com.clougence.sql.postgres.parser.antlr.PgSqlLexer;
import com.clougence.sql.postgres.parser.antlr.PgSqlParser;

public final class CbStatementParser {

    private CbStatementParser(){
    }

    public static CbSqlParser.StatementContext parse(String sql) {
        PgSqlLexer lexer = new PgSqlLexer(CharStreams.fromString(sql));
        lexer.setVersion(PostgresVersion.POSTGRES_14);
        lexer.removeErrorListeners();
        lexer.addErrorListener(SyntaxErrorListener.INSTANCE);
        CbSqlParser parser = new CbSqlParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.addErrorListener(SyntaxErrorListener.INSTANCE);
        CbSqlParser.StatementContext statement = parser.root().statement();
        CbSqlParser.External_stmtContext external = statement.external_stmt();
        if (external != null && external.external_columns() != null && external.external_columns().qualified_name() == null) {
            int start = external.external_columns().getStart().getStartIndex() + 1;
            int end = external.external_columns().getStop().getStopIndex();
            validateColumns(sql.substring(start, end), false);
        } else if (external != null && external.external_column() != null) {
            int start = external.external_column().getStart().getStartIndex();
            int end = external.external_column().getStop().getStopIndex() + 1;
            validateColumns(sql.substring(start, end), true);
        }
        return statement;
    }

    private static void validateColumns(String columns, boolean single) {
        PgSqlLexer lexer = new PgSqlLexer(CharStreams.fromString(columns));
        lexer.setVersion(PostgresVersion.POSTGRES_14);
        lexer.removeErrorListeners();
        lexer.addErrorListener(SyntaxErrorListener.INSTANCE);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PgSqlParser parser = new PgSqlParser(tokens);
        parser.setVersion(PostgresVersion.POSTGRES_14);
        parser.removeErrorListeners();
        parser.addErrorListener(SyntaxErrorListener.INSTANCE);
        PgSqlParser.TableelementlistContext list = parser.tableelementlist();
        if (tokens.LA(1) != Token.EOF || single && list.tableelement().size() != 1) {
            throw new IllegalArgumentException("Invalid external table columns near " + tokens.LT(1).getText());
        }
        for (PgSqlParser.TableelementContext element : list.tableelement()) {
            if (element.columnDef() == null || !element.columnDef().colquallist().colconstraint().isEmpty()) {
                throw new IllegalArgumentException("Unsupported external table column option");
            }
        }
    }
}
