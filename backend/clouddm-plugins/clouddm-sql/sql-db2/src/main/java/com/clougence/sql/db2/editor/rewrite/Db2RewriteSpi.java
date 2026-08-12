/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.db2.editor.rewrite;

import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteContext;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;
import com.clougence.utils.HashUtils;

public class Db2RewriteSpi implements RewriteSpi {

    @Override
    public String rewriteLimit(String query, RewriteContext context) {
        return query;
    }

    @Override
    public String rewriteDmlToQuery(String queryId, String queryStr, RewriteContext context) {
        int queryNo = HashUtils.fnvHash(queryId);
        return "EXPLAIN PLAN SET QUERYNO = " + queryNo + " FOR " + queryStr;
    }
}
