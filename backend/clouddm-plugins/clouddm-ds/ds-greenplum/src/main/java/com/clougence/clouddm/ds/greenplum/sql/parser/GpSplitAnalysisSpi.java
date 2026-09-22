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

import java.util.Set;

import org.antlr.v4.runtime.ParserRuleContext;

import com.clougence.clouddm.ds.greenplum.sql.parser.antlr.GpSqlParser;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.sql.postgres.parser.PgSplitAnalysisSpi;
import com.clougence.sql.postgres.parser.PgSplitVisitor;
import com.clougence.sql.postgres.parser.PostgresVersion;
import com.clougence.sql.postgres.parser.antlr.PgSqlParser;

public class GpSplitAnalysisSpi extends PgSplitAnalysisSpi {

    private final GpDslProvider provider;

    public GpSplitAnalysisSpi(PostgresVersion version){
        super(version);
        this.provider = new GpDslProvider(version);
    }

    @Override
    protected DslProvider dslProvider() {
        return provider;
    }

    @Override
    protected PgSplitVisitor newSplitVisitor() {
        return new GpSplitVisitor(version());
    }

    @Override
    protected Set<SplitQueryType> collectTypes(ParserRuleContext context, String script) {
        PgSqlParser.StmtContext statement = (PgSqlParser.StmtContext) context;
        if (statement.extensionstmt() != null) {
            return Set.of(classify(GpStatementParser.parse(script)));
        }
        return super.collectTypes(context, script);
    }

    public static SplitQueryType classify(GpSqlParser.StatementContext statement) {
        if (statement.protocol_privilege_stmt() != null) {
            if (statement.protocol_privilege_stmt().REVOKE() != null) {
                return SplitQueryType.REVOKE;
            }
            return SplitQueryType.GRANT;
        }
        if (statement.role_stmt() != null) {
            if (statement.role_stmt().CREATE() != null) {
                if (statement.role_stmt().role_keyword().USER() != null) {
                    return SplitQueryType.CREATE_USER;
                }
                return SplitQueryType.CREATE_ROLE;
            }
            return SplitQueryType.ALTER_USER;
        }
        if (statement.protocol_stmt() != null) {
            if (statement.protocol_stmt().CREATE() != null) {
                return SplitQueryType.CREATE_PROG_OBJ;
            }
            if (statement.protocol_stmt().ALTER() != null) {
                return SplitQueryType.ALTER_PROG_OBJ;
            }
            return SplitQueryType.DROP_PROG_OBJ;
        }
        if (statement.external_stmt() != null) {
            if (statement.external_stmt().CREATE() != null) {
                return SplitQueryType.CREATE_TABLE;
            }
            if (statement.external_stmt().ALTER() != null) {
                GpSqlParser.External_alter_actionContext action = statement.external_stmt().external_alter_action();
                if (action.ADD_P() != null) {
                    return SplitQueryType.ADD_COLUMN;
                }
                if (action.DROP() != null) {
                    return SplitQueryType.DROP_COLUMN;
                }
                if (action.ALTER() != null) {
                    return SplitQueryType.ALTER_COLUMN;
                }
                return SplitQueryType.ALTER_TABLE;
            }
            return SplitQueryType.DROP_TABLE;
        }
        if (statement.resource_stmt() != null) {
            return SplitQueryType.SYSTEM_SETTING_WRITE;
        }
        return SplitQueryType.SELECT;
    }
}
