/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.spi.rewrite;

import com.clougence.clouddm.ds.TextCaseSupport;
import com.clougence.clouddm.ds.TextTestCase;

public final class RewriteTextCase extends TextTestCase {

    private String method;
    private String queryId;
    private String query;
    private String inputContextJson;
    private String expectedQuery;
    private String expectedContextJson;

    public RewriteTextCase(TextCaseSupport.CaseBlock block){
        super(block);
    }

    public String method() {
        return this.method;
    }

    public void setMethod(String method) { this.method = method; }

    public String queryId() {
        return this.queryId;
    }

    public void setQueryId(String queryId) { this.queryId = queryId; }

    public String query() {
        return this.query;
    }

    public void setQuery(String query) { this.query = query; }

    public String inputContextJson() {
        return this.inputContextJson;
    }

    public void setInputContextJson(String inputContextJson) { this.inputContextJson = inputContextJson; }

    public String expectedQuery() {
        return this.expectedQuery;
    }

    public void setExpectedQuery(String expectedQuery) { this.expectedQuery = expectedQuery; }

    public String expectedContextJson() {
        return this.expectedContextJson;
    }

    public void setExpectedContextJson(String expectedContextJson) { this.expectedContextJson = expectedContextJson; }
}
