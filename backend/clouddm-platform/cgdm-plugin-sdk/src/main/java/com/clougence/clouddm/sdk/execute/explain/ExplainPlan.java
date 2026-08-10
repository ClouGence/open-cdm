/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.sdk.execute.explain;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExplainPlan {

    private ExplainPlanSource     source;
    private List<ExplainPlanNode> nodes      = new ArrayList<>();
    private Map<String, String>   properties = new LinkedHashMap<>();
}
