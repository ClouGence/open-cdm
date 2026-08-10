/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.sdk.execute.explain;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExplainPlanNode {

    private String              nodeId;
    private String              parentNodeId;

    private String              logical;
    private String              physical;
    private String              objectPath;
    private Boolean             parallel;
    private String              description;

    private Double              estimatedRows;
    private Double              estimatedExecutions;
    private Long                estimatedRowSize;
    private Double              estimatedIoCost;
    private Double              estimatedCpuCost;
    private Double              estimatedSubtreeCost;

    private Map<String, String> properties = new LinkedHashMap<>();
}
