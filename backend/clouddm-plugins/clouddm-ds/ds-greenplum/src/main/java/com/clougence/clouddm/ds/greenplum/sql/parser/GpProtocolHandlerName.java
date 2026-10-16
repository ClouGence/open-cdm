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

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

import com.clougence.dslpaser.parse.SyntaxErrorListener;
import com.clougence.sql.postgres.analysis.reference.PgResourceDialect;
import com.clougence.sql.postgres.parser.PostgresVersion;
import com.clougence.sql.postgres.parser.antlr.PgSqlLexer;
import com.clougence.sql.postgres.parser.antlr.PgSqlParser;

/** Parse a protocol handler's SQL string as a qualified PostgreSQL function name. */
public final class GpProtocolHandlerName {

    private GpProtocolHandlerName(){
    }

    public static List<String> parse(String literal) {
        String value = literal.substring(1, literal.length() - 1).replace("''", "'");
        PgSqlLexer lexer = new PgSqlLexer(CharStreams.fromString(value));
        lexer.setVersion(PostgresVersion.POSTGRES_12);
        lexer.removeErrorListeners();
        lexer.addErrorListener(SyntaxErrorListener.INSTANCE);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PgSqlParser parser = new PgSqlParser(tokens);
        parser.setVersion(PostgresVersion.POSTGRES_12);
        parser.removeErrorListeners();
        parser.addErrorListener(SyntaxErrorListener.INSTANCE);
        parser.func_name();
        if (tokens.LA(1) != Token.EOF) {
            throw new IllegalArgumentException("Invalid protocol handler name");
        }

        List<String> parts = new ArrayList<>();
        boolean expectName = true;
        for (Token token : tokens.getTokens()) {
            if (token.getChannel() != Token.DEFAULT_CHANNEL || token.getType() == Token.EOF) {
                continue;
            }
            if (expectName) {
                if (token.getType() == PgSqlLexer.DOT) {
                    throw new IllegalArgumentException("Invalid protocol handler name");
                }
                parts.add(PgResourceDialect.INSTANCE.normalizeIdentifier(token.getText()));
            } else if (token.getType() != PgSqlLexer.DOT) {
                throw new IllegalArgumentException("Invalid protocol handler name");
            }
            expectName = !expectName;
        }
        if (expectName || parts.isEmpty() || parts.size() > 3) {
            throw new IllegalArgumentException("Invalid protocol handler name");
        }
        return parts;
    }
}
