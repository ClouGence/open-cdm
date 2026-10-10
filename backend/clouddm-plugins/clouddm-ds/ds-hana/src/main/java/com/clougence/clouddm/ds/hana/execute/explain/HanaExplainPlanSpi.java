/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.ds.hana.execute.explain;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
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

/** Parses SAP HANA EXPLAIN_PLAN_TABLE rows. */
public class HanaExplainPlanSpi implements ExplainPlanSpi {

    @Override
    public ExplainPlan analyze(List<Result> results, List<BehaviorRelation> relations) {
        ExplainPlan plan = new ExplainPlan();
        if (results != null) {
            parseNativeNodes(plan, results);
        }

        if (!plan.getNodes().isEmpty()) {
            // OUTPUT_SIZE describes this operator, not the statement's affected rows.
            plan.setSource(ExplainPlanSource.NATIVE);
            return plan;
        }

        if (relations == null) {
            return plan;
        }

        for (BehaviorRelation relation : relations) {
            if (relation == null || !AFFECTED_ROW_ACTIONS.contains(relation.getAction())) {
                continue;
            }
            ExplainPlanNode node = new ExplainPlanNode();
            node.setNodeId("statement-" + plan.getNodes().size());
            node.setLogical(relation.getAction().name());
            if (relation.getSubject() != null) {
                node.setObjectPath(relation.getSubject().getObjectPath());
            }
            if (relation.getInsertRows() != null) {
                node.setEstimatedRows(relation.getInsertRows().doubleValue());
            }
            plan.getNodes().add(node);
        }
        if (!plan.getNodes().isEmpty()) {
            plan.setSource(ExplainPlanSource.STATEMENT);
        }
        return plan;
    }

    private static void parseNativeNodes(ExplainPlan plan, List<Result> results) {
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
            if (columns == null || resultSet.getRowSet() == null) {
                continue;
            }
            Map<String, Integer> indexes = indexes(columns);
            for (ResultSetRow row : resultSet.getRowSet()) {
                String nodeId = value(row, indexes.get("operator_id"));
                String operator = value(row, indexes.get("operator_name"));
                if (nodeId == null || nodeId.isBlank() || operator == null || operator.isBlank()) {
                    continue;
                }

                ExplainPlanNode node = new ExplainPlanNode();
                node.setNodeId(nodeId);
                node.setParentNodeId(value(row, indexes.get("parent_operator_id")));
                node.setLogical(operator);
                node.setPhysical(value(row, indexes.get("execution_engine")));
                node.setObjectPath(value(row, indexes.get("table_name")));
                node.setEstimatedRows(number(value(row, indexes.get("output_size"))));
                node.setEstimatedSubtreeCost(number(value(row, indexes.get("subtree_cost"))));
                node.setDescription(value(row, indexes.get("operator_details")));
                node.setProperties(properties(row, columns));
                plan.getNodes().add(node);
            }
        }
    }

    private static Map<String, Integer> indexes(List<String> columns) {
        Map<String, Integer> indexes = new HashMap<>();
        for (int i = 0; i < columns.size(); i++) {
            indexes.put(columns.get(i).toLowerCase(Locale.ROOT), i);
        }
        return indexes;
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

    private static String value(ResultSetRow row, Integer index) {
        if (index == null || row.getData() == null || index >= row.getData().size()) {
            return null;
        }
        ResultSetValue value = row.getData().get(index);
        return value == null ? null : value.getValue();
    }

    private static Double number(String value) {
        try {
            if (value == null) {
                return null;
            }
            double number = Double.parseDouble(value);
            if (!Double.isFinite(number) || number < 0D) {
                return null;
            }
            return number;
        } catch (NumberFormatException ignored) {
            return null;
        }
    }
}
