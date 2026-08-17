/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.console.web.component.approval.model;

import com.clougence.clouddm.sdk.execute.session.QueryRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DmlExplainRequestMO {

    private long         index;
    private long         statementSizeBytes;
    private QueryRequest request;
    private boolean      nativeExplainSupported;
}
