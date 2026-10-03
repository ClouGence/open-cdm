/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.hana.analysis;

import java.util.ArrayList;
import java.util.List;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementBehavior;
import lombok.Getter;

@Getter
public final class HanaAnalysisResult {
    private final StatementBehavior behavior = new StatementBehavior();
    private final List<RuleDomain> domains = new ArrayList<>();
}
