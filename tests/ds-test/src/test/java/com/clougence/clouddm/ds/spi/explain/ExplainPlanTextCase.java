/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.spi.explain;

import com.clougence.clouddm.ds.TextCaseSupport;
import com.clougence.clouddm.ds.TextTestCase;

public final class ExplainPlanTextCase extends TextTestCase {

    private String sql;
    private String inputJson;
    private String relationsJson;
    private String expectJson;

    public ExplainPlanTextCase(TextCaseSupport.CaseBlock block){
        super(block);
    }

    public String sql() {
        return this.sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String inputJson() {
        return this.inputJson;
    }

    public void setInputJson(String inputJson) {
        this.inputJson = inputJson;
    }

    public String expectJson() {
        return this.expectJson;
    }

    public void setExpectJson(String expectJson) {
        this.expectJson = expectJson;
    }

    public String relationsJson() {
        return this.relationsJson;
    }

    public void setRelationsJson(String relationsJson) {
        this.relationsJson = relationsJson;
    }
}
