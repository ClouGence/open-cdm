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

import com.clougence.clouddm.ds.cockroachdb.sql.parser.antlr.CrdbSqlParser;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.sql.postgres.parser.PgSplitVisitor;
import com.clougence.sql.postgres.parser.PostgresVersion;
import com.clougence.sql.postgres.parser.antlr.PgSqlParser;

public class CrdbSplitVisitor extends PgSplitVisitor {
    public CrdbSplitVisitor(PostgresVersion version){
        super(version);
    }

    @Override
    public SplitQueryType visitExtensionstmt(PgSqlParser.ExtensionstmtContext ctx) {
        return classify(CrdbStatementParser.parse(ctx));
    }

    public static SplitQueryType classify(CrdbSqlParser.StatementContext statement) {
        if (statement.database_stmt() != null) {
            if (statement.database_stmt().CREATE() != null) {
                return SplitQueryType.CREATE_CATALOG;
            }
            return SplitQueryType.DROP_CATALOG;
        }

        if (statement.privilege_stmt() != null) {
            if (statement.privilege_stmt().REVOKE() != null) {
                return SplitQueryType.REVOKE;
            }
            return SplitQueryType.GRANT;
        }

        if (statement.default_privileges_stmt() != null) {
            if (statement.default_privileges_stmt().REVOKE() != null) {
                return SplitQueryType.REVOKE;
            }
            return SplitQueryType.GRANT;
        }

        if (statement.role_stmt() != null) {
            var role = statement.role_stmt();
            if (role.CREATE() != null) {
                if (role.role_kind().USER() != null) {
                    return SplitQueryType.CREATE_USER;
                }
                return SplitQueryType.CREATE_ROLE;
            }

            if (role.DROP() != null) {
                if (role.role_kind().USER() != null) {
                    return SplitQueryType.DROP_USER;
                }
                return SplitQueryType.DROP_ROLE;
            }
            return SplitQueryType.ALTER_USER;
        }
        return SplitQueryType.METADATA;
    }
}
