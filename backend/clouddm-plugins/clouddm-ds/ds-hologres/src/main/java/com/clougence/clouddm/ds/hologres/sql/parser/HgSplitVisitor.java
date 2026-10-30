/* Copyright 2026 杭州开云集致科技有限公司. Licensed under the Apache License, Version 2.0. */
package com.clougence.clouddm.ds.hologres.sql.parser;

import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.sql.postgres.parser.PgSplitVisitor;
import com.clougence.sql.postgres.parser.PostgresVersion;
import com.clougence.sql.postgres.parser.antlr.PgSqlParser;

public class HgSplitVisitor extends PgSplitVisitor {
    public HgSplitVisitor(PostgresVersion version){
        super(version);
    }

    @Override
    public SplitQueryType visitCallstmt(PgSqlParser.CallstmtContext ctx) {
        SplitQueryType type = HgPermissionCall.type(ctx);
        if (type != null) {
            return type;
        }
        return super.visitCallstmt(ctx);
    }
}
