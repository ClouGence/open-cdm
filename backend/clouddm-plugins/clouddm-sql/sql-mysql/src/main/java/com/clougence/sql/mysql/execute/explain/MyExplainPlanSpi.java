/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.mysql.execute.explain;

import java.util.*;

import com.clougence.clouddm.sdk.execute.explain.ExplainPlan;
import com.clougence.clouddm.sdk.execute.explain.ExplainPlanNode;
import com.clougence.clouddm.sdk.execute.explain.ExplainPlanSource;
import com.clougence.clouddm.sdk.execute.explain.ExplainPlanSpi;
import com.clougence.clouddm.sdk.execute.resultset.echo.Result;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultSetMeta;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultSetRow;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultSetValue;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorRelation;
import com.clougence.sql.mysql.MySqlEngineSpi;

/** Converts MySQL's tabular EXPLAIN output into the common plan model. */
public class MyExplainPlanSpi implements ExplainPlanSpi {

    @Override
    public String name() {
        return MySqlEngineSpi.NAME;
    }

    @Override
    public ExplainPlan analyze(List<Result> results, List<BehaviorRelation> relations) {
        ExplainPlan plan = new ExplainPlan();
        if (results != null && !results.isEmpty()) {
            parseNative(results, plan);
        }
        if (relations != null && !relations.isEmpty()) {
            enrichWithBehavior(plan, relations);
        }

        boolean hasNative = results != null && !results.isEmpty();
        boolean hasBehavior = relations != null && !relations.isEmpty();
        if (hasNative && hasBehavior) {
            plan.setSource(ExplainPlanSource.MERGE);
        } else if (hasNative) {
            plan.setSource(ExplainPlanSource.NATIVE);
        } else if (hasBehavior) {
            plan.setSource(ExplainPlanSource.STATEMENT);
        }
        return plan;
    }

    private static void parseNative(List<Result> results, ExplainPlan plan) {
        Map<String, List<String>> columnsByResult = new HashMap<>();
        for (Result result : results) {
            if (result instanceof ResultSetMeta meta) {
                columnsByResult.put(meta.getResultId(), meta.getColumnList());
            }
        }

        int nodeIndex = 0;
        for (Result result : results) {
            if (!(result instanceof com.clougence.clouddm.sdk.execute.resultset.echo.ResultSet resultSet)) {
                continue;
            }
            List<String> columns = columnsByResult.get(resultSet.getResultId());
            if (columns == null || resultSet.getRowSet() == null) {
                continue;
            }
            Map<String, Integer> columnIndexes = columnIndexes(columns);
            for (ResultSetRow row : resultSet.getRowSet()) {
                ExplainPlanNode node = new ExplainPlanNode();
                node.setNodeId(String.valueOf(nodeIndex++));
                node.setObjectPath(value(row, columnIndexes.get("table")));
                node.setLogical(value(row, columnIndexes.get("select_type")));
                node.setPhysical(value(row, columnIndexes.get("type")));
                node.setEstimatedRows(doubleValue(value(row, columnIndexes.get("rows"))));
                node.setProperties(properties(row, columns));
                plan.getNodes().add(node);
            }
        }
    }

    private static void enrichWithBehavior(ExplainPlan plan, List<BehaviorRelation> relations) {
        for (BehaviorRelation relation : relations) {
            if (!isWrite(relation) || relation.getSubject() == null) {
                continue;
            }
            ExplainPlanNode target = targetNode(plan, relation);
            if (target == null && isInsert(relation)) {
                target = new ExplainPlanNode();
                target.setNodeId(String.valueOf(plan.getNodes().size()));
                target.setLogical(relation.getAction().name());
                target.setObjectPath(relation.getSubject().getObjectPath());
                plan.getNodes().add(target);
            }
            if (target == null) {
                continue;
            }

            target.setObjectPath(relation.getSubject().getObjectPath());
            if (relation.getInsertRows() != null) {
                target.setEstimatedRows(relation.getInsertRows().doubleValue());
            } else if (isInsert(relation) && target.getEstimatedRows() == null) {
                target.setEstimatedRows(selectOutputRows(plan, target));
            }
        }
    }

    private static ExplainPlanNode targetNode(ExplainPlan plan, BehaviorRelation relation) {
        String action = relation.getAction().name();
        for (ExplainPlanNode node : plan.getNodes()) {
            if (node.getLogical() == null) {
                continue;
            }
            String logical = node.getLogical().toUpperCase(Locale.ROOT);
            boolean mergeTarget = relation.getAction() == BehaviorAction.MERGE && (logical.equals("INSERT") || logical.equals("REPLACE"));
            if (logical.equals(action) || mergeTarget) {
                return node;
            }
        }

        String objectName = objectName(relation.getSubject().getObjectPath());
        for (ExplainPlanNode node : plan.getNodes()) {
            if (objectName.equalsIgnoreCase(objectName(node.getObjectPath()))) {
                return node;
            }
        }
        if (plan.getNodes().size() == 1 && !isInsert(relation)) {
            return plan.getNodes().get(0);
        }
        return null;
    }

    private static Double selectOutputRows(ExplainPlan plan, ExplainPlanNode target) {
        List<ExplainPlanNode> sources = plan.getNodes()
            .stream()
            .filter(node -> node != target)
            .filter(node -> node.getEstimatedRows() != null)
            .filter(node -> node.getObjectPath() == null || !node.getObjectPath().startsWith("<"))
            .toList();
        if (sources.size() != 1) {
            return null;
        }
        ExplainPlanNode source = sources.get(0);
        Double rows = source.getEstimatedRows();
        Double filtered = doubleValue(source.getProperties().get("filtered"));
        if (filtered != null) {
            rows = rows * filtered / 100D;
        }
        return rows;
    }

    private static boolean isWrite(BehaviorRelation relation) {
        if (relation == null) {
            return false;
        }
        return ExplainPlanSpi.ACTIONS.contains(relation.getAction());
    }

    private static boolean isInsert(BehaviorRelation relation) {
        BehaviorAction action = relation.getAction();
        return action == BehaviorAction.INSERT || action == BehaviorAction.MERGE || action == BehaviorAction.REPLACE;
    }

    private static String objectName(String objectPath) {
        if (objectPath == null) {
            return "";
        }
        String normalized = objectPath;
        while (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        int separator = normalized.lastIndexOf('/');
        if (separator >= 0) {
            return normalized.substring(separator + 1);
        }
        return normalized;
    }

    private static Map<String, Integer> columnIndexes(List<String> columns) {
        Map<String, Integer> indexes = new HashMap<>();
        for (int i = 0; i < columns.size(); i++) {
            indexes.put(columns.get(i).toLowerCase(Locale.ROOT), i);
        }
        return indexes;
    }

    private static String value(ResultSetRow row, Integer index) {
        if (index == null || row.getData() == null || index >= row.getData().size()) {
            return null;
        }
        ResultSetValue value = row.getData().get(index);
        return value == null ? null : value.getValue();
    }

    private static Map<String, String> properties(ResultSetRow row, List<String> columns) {
        Map<String, String> properties = new LinkedHashMap<>();
        for (int i = 0; i < columns.size(); i++) {
            String value = value(row, i);
            if (value != null) {
                properties.put(columns.get(i), value);
            }
        }
        return properties;
    }

    private static Double doubleValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Double.valueOf(value);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

}
