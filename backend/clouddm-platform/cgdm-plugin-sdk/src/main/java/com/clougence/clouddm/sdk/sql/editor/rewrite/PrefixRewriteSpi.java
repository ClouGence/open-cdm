/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.sdk.sql.editor.rewrite;

public class PrefixRewriteSpi implements RewriteSpi {

    private final String prefix;

    public PrefixRewriteSpi(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String rewriteLimit(String queryId, String queryStr, RewriteContext context) {
        return withPrefix(queryStr, this.prefix);
    }

    @Override
    public String rewriteToExplain(String queryId, String queryStr, RewriteContext context) {
        return withPrefix(queryStr, this.prefix);
    }

    public static String withPrefix(String queryBody, String prefix) {
        int position = queryBody.length() - queryBody.stripLeading().length();
        return new StringBuilder(queryBody).insert(position, prefix).toString();
    }
}
