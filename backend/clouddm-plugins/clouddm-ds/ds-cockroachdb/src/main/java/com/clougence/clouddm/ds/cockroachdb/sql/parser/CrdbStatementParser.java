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
import java.util.Set;
import java.util.Locale;
import org.antlr.v4.runtime.*;
import com.clougence.dslpaser.parse.SyntaxErrorListener;
import com.clougence.clouddm.ds.cockroachdb.sql.parser.antlr.CrdbSqlParser;

public final class CrdbStatementParser {
    private static final Set<String> SHOW_OBJECTS = Set.of("COLUMNS", "CONSTRAINTS", "INDEX", "INDEXES", "CREATE", "TABLES", "SCHEMAS", "SEQUENCES", "TYPES", "ENUMS", "DATABASES", "ROLES", "USERS");
    private CrdbStatementParser() {}

    public static boolean isExtension(TokenStream tokens) {
        return "SHOW".equalsIgnoreCase(tokens.LT(1).getText()) && SHOW_OBJECTS.contains(tokens.LT(2).getText().toUpperCase(Locale.ROOT));
    }

    public static CrdbSqlParser.StatementContext parse(TokenStream source, ParserRuleContext context) {
        List<Token> statement = new ArrayList<>();
        for (int i = context.getStart().getTokenIndex(); i <= context.getStop().getTokenIndex(); i++) {
            statement.add(new CommonToken(source.get(i)));
        }
        CrdbSqlParser parser = new CrdbSqlParser(new CommonTokenStream(new ListTokenSource(statement)));
        parser.removeErrorListeners();
        parser.addErrorListener(SyntaxErrorListener.INSTANCE);
        return parser.root().statement();
    }

    public static List<String> names(CrdbSqlParser.Qualified_nameContext context) {
        return context.identifier().stream().map(id -> {
            String name = id.getText();
            if (name.startsWith("\"")) {
                return name.substring(1, name.length() - 1).replace("\"\"", "\"");
            }
            return name.toLowerCase(Locale.ROOT);
        }).toList();
    }
}
