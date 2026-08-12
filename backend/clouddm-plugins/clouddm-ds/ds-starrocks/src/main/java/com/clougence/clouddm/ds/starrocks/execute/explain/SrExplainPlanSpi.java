/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.ds.starrocks.execute.explain;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.sdk.execute.explain.ExplainPlan;
import com.clougence.clouddm.sdk.execute.explain.ExplainPlanNode;
import com.clougence.clouddm.sdk.execute.explain.ExplainPlanSource;
import com.clougence.clouddm.sdk.execute.explain.ExplainPlanSpi;
import com.clougence.clouddm.sdk.execute.resultset.echo.Result;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultSetRow;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultSetValue;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorRelation;

/** Parses StarRocks' text fragment plan. */
public class SrExplainPlanSpi implements ExplainPlanSpi {

    @Override
    public ExplainPlan analyze(List<Result> results, List<BehaviorRelation> relations) {
        ExplainPlan plan = new ExplainPlan();
        ExplainPlanNode scan = null;
        for (String line : lines(results)) {
            if (line.contains(":OlapScanNode")) {
                scan = new ExplainPlanNode();
                scan.setNodeId(String.valueOf(plan.getNodes().size()));
                scan.setPhysical("OlapScanNode");
                plan.getNodes().add(scan);
            } else if (scan != null && line.stripLeading().startsWith("TABLE:")) {
                scan.setObjectPath(line.substring(line.indexOf(':') + 1).trim());
            } else if (scan != null) {
                Double rows = cardinality(line);
                if (rows != null) {
                    scan.setEstimatedRows(rows > 0D ? rows : null);
                }
            }
        }
        mergeStatement(plan, relations);
        source(plan, results, relations);
        return plan;
    }

    private static Double cardinality(String line) {
        String normalized = line.stripLeading();
        String prefix = "cardinality=";
        if (!normalized.startsWith(prefix)) {
            return null;
        }
        int end = normalized.indexOf(',', prefix.length());
        if (end < 0) {
            end = normalized.length();
        }
        try {
            return Double.valueOf(normalized.substring(prefix.length(), end).trim());
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private static List<String> lines(List<Result> results) {
        List<String> lines = new ArrayList<>();
        for (Result result : results) {
            if (!(result instanceof com.clougence.clouddm.sdk.execute.resultset.echo.ResultSet resultSet) || resultSet.getRowSet() == null) {
                continue;
            }
            for (ResultSetRow row : resultSet.getRowSet()) {
                String value = value(row);
                if (value != null) {
                    lines.addAll(value.lines().toList());
                }
            }
        }
        return lines;
    }

    private static void mergeStatement(ExplainPlan plan, List<BehaviorRelation> relations) {
        if (relations == null) {
            return;
        }
        BehaviorRelation write = relations.stream().filter(relation -> relation != null && ACTIONS.contains(relation.getAction())).findFirst().orElse(null);
        if (write == null) {
            return;
        }
        ExplainPlanNode target = plan.getNodes().isEmpty() ? new ExplainPlanNode() : plan.getNodes().get(0);
        if (plan.getNodes().isEmpty()) {
            target.setNodeId("0");
            plan.getNodes().add(target);
        }
        target.setLogical(write.getAction().name());
        if (write.getSubject() != null) {
            target.setObjectPath(write.getSubject().getObjectPath());
        }
        if (write.getInsertRows() != null) {
            target.setEstimatedRows(write.getInsertRows().doubleValue());
        }
    }

    private static void source(ExplainPlan plan, List<Result> results, List<BehaviorRelation> relations) {
        boolean nativePlan = results != null && !results.isEmpty();
        boolean statement = relations != null && !relations.isEmpty();
        if (nativePlan && statement) {
            plan.setSource(ExplainPlanSource.MERGE);
        } else if (nativePlan) {
            plan.setSource(ExplainPlanSource.NATIVE);
        } else if (statement) {
            plan.setSource(ExplainPlanSource.STATEMENT);
        }
    }

    private static String value(ResultSetRow row) {
        if (row.getData() == null || row.getData().isEmpty()) {
            return null;
        }
        ResultSetValue value = row.getData().get(0);
        return value == null ? null : value.getValue();
    }
}
