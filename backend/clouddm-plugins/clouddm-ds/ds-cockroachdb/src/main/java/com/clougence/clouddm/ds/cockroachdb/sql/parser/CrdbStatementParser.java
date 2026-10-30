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
package com.clougence.clouddm.ds.cockroachdb.sql.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.clougence.clouddm.ds.cockroachdb.sql.parser.antlr.CrdbSqlParser;
import com.clougence.dslpaser.parse.SyntaxErrorListener;
import com.clougence.sql.postgres.parser.antlr.PgSqlParser;

public final class CrdbStatementParser {
    private static final Set<String> SHOW_OBJECTS = Set
        .of("COLUMNS", "CONSTRAINTS", "INDEX", "INDEXES", "CREATE", "TABLES", "SCHEMAS", "SEQUENCES", "TYPES", "ENUMS", "DATABASES", "ROLES", "USERS");

    private CrdbStatementParser(){
    }

    public static boolean isExtension(TokenStream tokens) {
        String first = tokens.LT(1).getText().toUpperCase(Locale.ROOT);
        String second = tokens.LT(2).getText().toUpperCase(Locale.ROOT);
        if (first.equals("GRANT") || first.equals("REVOKE")) {
            return true;
        }

        if (second.equals("DATABASE")) {
            return first.equals("DROP") || first.equals("CREATE") && tokens.LT(3).getText().equalsIgnoreCase("IF");
        }

        if (first.equals("CREATE") || first.equals("ALTER") || first.equals("DROP")) {
            return second.equals("ROLE") || second.equals("USER") || second.equals("GROUP") || first.equals("ALTER") && second.equals("DEFAULT");
        }
        return first.equals("SHOW") && SHOW_OBJECTS.contains(second);
    }

    public static CrdbSqlParser.StatementContext parse(PgSqlParser.ExtensionstmtContext context) {
        List<Token> statement = new ArrayList<>();
        for (var child : context.children) {
            // extensionstmt consists of terminals; retain token locations without re-lexing the SQL text.
            TerminalNode node = (TerminalNode) child;
            statement.add(new CommonToken(node.getSymbol()));
        }

        CrdbSqlParser parser = new CrdbSqlParser(new CommonTokenStream(new ListTokenSource(statement)));
        parser.removeErrorListeners();
        parser.addErrorListener(SyntaxErrorListener.INSTANCE);
        return parser.root().statement();
    }

    public static String name(String value) {
        if (value.startsWith("\"")) {
            return value.substring(1, value.length() - 1).replace("\"\"", "\"");
        }
        return value.toLowerCase(Locale.ROOT);
    }

    public static List<String> names(CrdbSqlParser.Qualified_nameContext context) {
        return context.identifier().stream().map(id -> name(id.getText())).toList();
    }
}
