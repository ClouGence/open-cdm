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

import static com.clougence.sql.postgres.parser.antlr.PgSqlLexer.LANGUAGE;

import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.sql.postgres.parser.PgSplitVisitor;
import com.clougence.sql.postgres.parser.PostgresVersion;
import com.clougence.sql.postgres.parser.antlr.PgSqlParser.CreateplangstmtContext;
import com.clougence.sql.postgres.parser.antlr.PgSqlParser.CreatetransformstmtContext;
import com.clougence.sql.postgres.parser.antlr.PgSqlParser.DropstmtContext;
import com.clougence.sql.postgres.parser.antlr.PgSqlParser.DroptransformstmtContext;
import com.clougence.sql.postgres.parser.antlr.PgSqlParser.RenamestmtContext;

/** Cloudberry management classification that must not affect PostgreSQL. */
public class CbSplitVisitor extends PgSplitVisitor {

    public CbSplitVisitor(PostgresVersion version){
        super(version);
    }

    @Override
    public SplitQueryType visitCreateplangstmt(CreateplangstmtContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitCreatetransformstmt(CreatetransformstmtContext ctx) {
        return SplitQueryType.CREATE_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitDroptransformstmt(DroptransformstmtContext ctx) {
        return SplitQueryType.DROP_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitDropstmt(DropstmtContext ctx) {
        if (hasToken(ctx, LANGUAGE)) {
            return SplitQueryType.SYSTEM_SETTING_WRITE;
        }
        return super.visitDropstmt(ctx);
    }

    @Override
    public SplitQueryType visitRenamestmt(RenamestmtContext ctx) {
        if (hasToken(ctx, LANGUAGE)) {
            return SplitQueryType.SYSTEM_SETTING_WRITE;
        }
        return super.visitRenamestmt(ctx);
    }
}
