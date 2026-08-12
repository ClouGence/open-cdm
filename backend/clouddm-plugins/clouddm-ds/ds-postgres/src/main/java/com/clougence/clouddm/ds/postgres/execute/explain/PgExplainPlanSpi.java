/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.ds.postgres.execute.explain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.execute.explain.ExplainPlan;
import com.clougence.clouddm.sdk.execute.explain.ExplainPlanNode;
import com.clougence.clouddm.sdk.execute.explain.ExplainPlanSource;
import com.clougence.clouddm.sdk.execute.explain.ExplainPlanSpi;
import com.clougence.clouddm.sdk.execute.resultset.echo.Result;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultSetMeta;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultSetRow;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultSetValue;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorRelation;

/** Parses PostgreSQL's text QUERY PLAN rows. */
public class PgExplainPlanSpi implements ExplainPlanSpi {

    @Override
    public ExplainPlan analyze(List<Result> results, List<BehaviorRelation> relations) {
        ExplainPlan plan = new ExplainPlan();
        Map<String, List<String>> metas = new HashMap<>();
        for (Result result : results) {
            if (result instanceof ResultSetMeta meta) {
                metas.put(meta.getResultId(), meta.getColumnList());
            }
        }
        for (Result result : results) {
            if (!(result instanceof com.clougence.clouddm.sdk.execute.resultset.echo.ResultSet resultSet)) {
                continue;
            }
            List<String> columns = metas.get(resultSet.getResultId());
            if (columns == null || columns.isEmpty() || resultSet.getRowSet() == null) {
                continue;
            }
            for (ResultSetRow row : resultSet.getRowSet()) {
                String line = value(row, 0);
                ExplainPlanNode node = planLine(line, columns.get(0), plan.getNodes().size());
                if (node == null) {
                    continue;
                }
                plan.getNodes().add(node);
            }
        }
        BehaviorRelation write = write(relations);
        if (write != null) {
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
            } else if (target.getEstimatedRows() == null || target.getEstimatedRows() == 0D) {
                target.setEstimatedRows(plan.getNodes().stream().map(ExplainPlanNode::getEstimatedRows).filter(rows -> rows != null && rows > 0D).findFirst().orElse(null));
            }
        }
        source(plan, results, relations);
        return plan;
    }

    private static ExplainPlanNode planLine(String line, String column, int index) {
        if (line == null) {
            return null;
        }
        String normalized = line.stripLeading();
        if (normalized.startsWith("->")) {
            normalized = normalized.substring(2).stripLeading();
        }
        int costStart = normalized.indexOf("(cost=");
        if (costStart < 1) {
            return null;
        }
        int rowsStart = normalized.indexOf("rows=", costStart);
        if (rowsStart < 0) {
            return null;
        }
        rowsStart += "rows=".length();
        int rowsEnd = rowsStart;
        while (rowsEnd < normalized.length()) {
            char current = normalized.charAt(rowsEnd);
            if (!Character.isDigit(current) && current != '.') {
                break;
            }
            rowsEnd++;
        }
        if (rowsEnd == rowsStart) {
            return null;
        }
        ExplainPlanNode node = new ExplainPlanNode();
        node.setNodeId(String.valueOf(index));
        node.setPhysical(normalized.substring(0, costStart).trim());
        node.setEstimatedRows(Double.valueOf(normalized.substring(rowsStart, rowsEnd)));
        node.getProperties().put(column, line);
        return node;
    }

    private static BehaviorRelation write(List<BehaviorRelation> relations) {
        if (relations == null) {
            return null;
        }
        return relations.stream().filter(relation -> {
            return relation != null && AFFECTED_ROW_ACTIONS.contains(relation.getAction());
        }).findFirst().orElse(null);
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

    private static String value(ResultSetRow row, int index) {
        if (row.getData() == null || row.getData().size() <= index) {
            return null;
        }
        ResultSetValue value = row.getData().get(index);
        return value == null ? null : value.getValue();
    }
}
