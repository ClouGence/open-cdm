/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.sdk.execute.explain;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import com.clougence.clouddm.sdk.Spi;
import com.clougence.clouddm.sdk.execute.resultset.echo.Result;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorRelation;

/** Builds a stable plan structure from datasource EXPLAIN output and statement behavior. */
public interface ExplainPlanSpi extends Spi {

    Set<BehaviorAction> ACTIONS = Collections.unmodifiableSet(EnumSet.of(//
            BehaviorAction.INSERT,  //
            BehaviorAction.UPDATE,  //
            BehaviorAction.DELETE,  //
            BehaviorAction.MERGE,   //
            BehaviorAction.REPLACE));

    ExplainPlan analyze(List<Result> results, List<BehaviorRelation> relations);
}
