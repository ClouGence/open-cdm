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

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import com.clougence.sql.postgres.parser.PgDslProvider;
import com.clougence.sql.postgres.parser.PostgresVersion;
import com.clougence.sql.postgres.analysis.security.base.PgSqlParserBase;

public class CrdbDslProvider extends PgDslProvider {
    public CrdbDslProvider(PostgresVersion version) {
        super(version, "CockroachDB SQL");
    }

    @Override
    public Parser createParser(Lexer lexer) {
        PgSqlParserBase parser = (PgSqlParserBase) super.createParser(lexer);
        parser.setUpsertKeyword(tokens -> "UPSERT".equalsIgnoreCase(tokens.LT(1).getText()));
        parser.setSnapshotClause(tokens -> "AS".equalsIgnoreCase(tokens.LT(1).getText()) && "OF".equalsIgnoreCase(tokens.LT(2).getText()));
        parser.setExtensionPredicates(CrdbStatementParser::isExtension, null, null);
        parser.addParseListener(new CrdbStatementListener(parser));
        return parser;
    }
}
