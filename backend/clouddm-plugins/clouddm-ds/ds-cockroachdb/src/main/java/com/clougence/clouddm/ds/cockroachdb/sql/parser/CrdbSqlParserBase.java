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

import java.util.Locale;
import java.util.Set;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;

import com.clougence.sql.postgres.parser.antlr.PgSqlLexer;

public abstract class CrdbSqlParserBase extends Parser {
    // CockroachDB v24.3 sql.y reserved_keyword; PostgreSQL reserves a different keyword set.
    private static final Set<String> RESERVED   = Set
        .of("ALL", "ANALYSE", "ANALYZE", "AND", "ANY", "ARRAY", "AS", "ASC", "ASYMMETRIC", "BOTH", "CASE", "CAST", "CHECK", "COLLATE", "COLUMN", "CONCURRENTLY", "CONSTRAINT", "CREATE", "CURRENT_CATALOG", "CURRENT_DATE", "CURRENT_ROLE", "CURRENT_SCHEMA", "CURRENT_TIME", "CURRENT_TIMESTAMP", "CURRENT_USER", "DEFAULT", "DEFERRABLE", "DESC", "DISTINCT", "DO", "ELSE", "END", "EXCEPT", "FALSE", "FETCH", "FOR", "FOREIGN", "FROM", "GRANT", "GROUP", "HAVING", "IN", "INITIALLY", "INTERSECT", "INTO", "LATERAL", "LEADING", "LIMIT", "LOCALTIME", "LOCALTIMESTAMP", "NOT", "NULL", "OFFSET", "ON", "ONLY", "OR", "ORDER", "PLACING", "PRIMARY", "REFERENCES", "RETURNING", "SELECT", "SESSION_USER", "SOME", "SYMMETRIC", "TABLE", "THEN", "TO", "TRAILING", "TRUE", "UNION", "UNIQUE", "USER", "USING", "VARIADIC", "WHEN", "WHERE", "WINDOW", "WITH");

    private static final Set<String> ROLE_FLAGS = Set
        .of("CREATEROLE", "NOCREATEROLE", "LOGIN", "NOLOGIN", "CONTROLJOB", "NOCONTROLJOB", "CONTROLCHANGEFEED", "NOCONTROLCHANGEFEED", "CREATEDB", "NOCREATEDB", "CREATELOGIN", "NOCREATELOGIN", "VIEWACTIVITY", "NOVIEWACTIVITY", "VIEWACTIVITYREDACTED", "NOVIEWACTIVITYREDACTED", "CANCELQUERY", "NOCANCELQUERY", "MODIFYCLUSTERSETTING", "NOMODIFYCLUSTERSETTING", "SQLLOGIN", "NOSQLLOGIN", "VIEWCLUSTERSETTING", "NOVIEWCLUSTERSETTING", "REPLICATION", "NOREPLICATION");

    protected CrdbSqlParserBase(TokenStream input){
        super(input);
    }

    protected final boolean keyword(String word) {
        return word.equalsIgnoreCase(getTokenStream().LT(1).getText());
    }

    protected final boolean identifierToken() {
        Token token = getTokenStream().LT(1);
        if (token.getType() == PgSqlLexer.Identifier || token.getType() == PgSqlLexer.QuotedIdentifier) {
            return true;
        }

        String text = token.getText();
        return token.getType() != Token.EOF && text.chars().allMatch(c -> Character.isLetter(c) || c == '_') && !RESERVED.contains(text.toUpperCase(Locale.ROOT));
    }

    protected final boolean roleFlag() {
        return ROLE_FLAGS.contains(getTokenStream().LT(1).getText().toUpperCase(Locale.ROOT));
    }
}
