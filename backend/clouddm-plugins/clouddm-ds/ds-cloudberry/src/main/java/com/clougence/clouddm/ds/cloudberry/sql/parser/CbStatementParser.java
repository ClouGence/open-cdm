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

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.Trees;

import com.clougence.clouddm.ds.cloudberry.sql.parser.antlr.CbSqlParser;
import com.clougence.dslpaser.parse.SyntaxErrorListener;
import com.clougence.sql.postgres.parser.PostgresVersion;
import com.clougence.sql.postgres.parser.antlr.PgSqlLexer;
import com.clougence.sql.postgres.parser.antlr.PgSqlParser;

public final class CbStatementParser {

    private CbStatementParser(){
    }

    public static CbSqlParser.StatementContext parse(String sql) {
        CommonTokenStream tokens = tokens(CharStreams.fromString(sql));
        return parse(tokens);
    }

    public static CbSqlParser.StatementContext parse(TokenStream source, ParserRuleContext context) {
        List<Token> statementTokens = new ArrayList<>();
        int start = context.getStart().getTokenIndex();
        int stop = context.getStop().getTokenIndex();
        for (int index = start; index <= stop; index++) {
            statementTokens.add(new CommonToken(source.get(index)));
        }
        return parse(new CommonTokenStream(new ListTokenSource(statementTokens)));
    }

    private static CbSqlParser.StatementContext parse(CommonTokenStream tokens) {
        CbSqlParser parser = parser(tokens);
        CbSqlParser.StatementContext statement = parser.root().statement();
        validateStatement(tokens, statement);
        return statement;
    }

    private static CommonTokenStream tokens(CharStream source) {
        PgSqlLexer lexer = new PgSqlLexer(source);
        lexer.setVersion(PostgresVersion.POSTGRES_14);
        lexer.removeErrorListeners();
        lexer.addErrorListener(SyntaxErrorListener.INSTANCE);
        return new CommonTokenStream(lexer);
    }

    private static CbSqlParser parser(TokenStream tokens) {
        CbSqlParser parser = new CbSqlParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(SyntaxErrorListener.INSTANCE);
        return parser;
    }

    private static void validateStatement(TokenStream tokens, CbSqlParser.StatementContext statement) {
        CbSqlParser.External_stmtContext external = statement.external_stmt();
        if (external != null && external.external_columns() != null && external.external_columns().qualified_name() == null) {
            int start = external.external_columns().getStart().getTokenIndex() + 1;
            int end = external.external_columns().getStop().getTokenIndex() - 1;
            validateColumns(tokens.getText(Interval.of(start, end)), false);
        } else if (external != null && external.external_column() != null) {
            validateColumns(tokens.getText(external.external_column()), true);
        }
    }

    public static boolean isExtensionStatement(TokenStream tokens) {
        String first = tokens.LT(1).getText();
        String second = tokens.LT(2).getText();
        if (!isExtensionPrefix(first, second)) {
            return false;
        }

        List<Token> statementTokens = new ArrayList<>();
        for (int offset = 1;; offset++) {
            Token token = tokens.LT(offset);
            if (token.getType() == Token.EOF) {
                break;
            }
            statementTokens.add(new CommonToken(token));
            if (token.getType() == PgSqlLexer.SEMI) {
                break;
            }
        }
        if (statementTokens.isEmpty()) {
            return false;
        }

        return tryParseExtension(new CommonTokenStream(new ListTokenSource(statementTokens))) != null;
    }

    private static CbSqlParser.StatementContext tryParseExtension(TokenStream tokens) {
        String first = tokens.LT(1).getText();
        String second = tokens.LT(2).getText();
        if (!isExtensionPrefix(first, second)) {
            return null;
        }

        CbSqlParser parser = new CbSqlParser(tokens);
        parser.removeErrorListeners();
        parser.setErrorHandler(new BailErrorStrategy());
        try {
            CbSqlParser.StatementContext statement = parser.root().statement();
            if (statement.role_stmt() == null) {
                return statement;
            }
            boolean cloudberry = Trees.getDescendants(statement.role_stmt()).stream().anyMatch(CbSqlParser.Cloudberry_role_optionContext.class::isInstance);
            return cloudberry ? statement : null;
        } catch (ParseCancellationException e) {
            return null;
        }
    }

    private static boolean isExtensionPrefix(String first, String second) {
        if ("RETRIEVE".equalsIgnoreCase(first) || "GRANT".equalsIgnoreCase(first) || "REVOKE".equalsIgnoreCase(first)) {
            return true;
        }
        if ("CREATE".equalsIgnoreCase(first)) {
            return "PROTOCOL".equalsIgnoreCase(second) || "TRUSTED".equalsIgnoreCase(second) || "EXTERNAL".equalsIgnoreCase(second) || "READABLE".equalsIgnoreCase(second)
                   || "WRITABLE".equalsIgnoreCase(second) || "RESOURCE".equalsIgnoreCase(second) || "ROLE".equalsIgnoreCase(second) || "USER".equalsIgnoreCase(second);
        }
        if ("ALTER".equalsIgnoreCase(first)) {
            return "PROTOCOL".equalsIgnoreCase(second) || "EXTERNAL".equalsIgnoreCase(second) || "RESOURCE".equalsIgnoreCase(second) || "ROLE".equalsIgnoreCase(second)
                   || "USER".equalsIgnoreCase(second);
        }
        return "DROP".equalsIgnoreCase(first) && ("PROTOCOL".equalsIgnoreCase(second) || "EXTERNAL".equalsIgnoreCase(second) || "RESOURCE".equalsIgnoreCase(second));
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
