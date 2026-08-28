/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.console.web.component.approval.model;

import java.util.List;
import java.util.Set;

import com.clougence.clouddm.sdk.execute.explain.ExplainPlan;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DmlExplainResultMO {

    private long                index;
    private Integer             statementStartLine;
    private long                statementSizeBytes;
    private Set<BehaviorAction> actions;
    private List<String>        subjects;
    private DmlExplainStatus    status;
    private Long                estimatedAffectedRows;
    private ExplainPlan         explainPlan;
    private String              message;
}
