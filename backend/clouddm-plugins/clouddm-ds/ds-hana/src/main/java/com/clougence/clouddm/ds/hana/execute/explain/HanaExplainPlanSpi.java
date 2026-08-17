/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.ds.hana.execute.explain;

import java.util.*;

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
                ExplainPlanNode node = new ExplainPlanNode();
                node.setNodeId(value(row, indexes.get("operator_id")));
                node.setParentNodeId(value(row, indexes.get("parent_operator_id")));
                node.setLogical(value(row, indexes.get("operator_name")));
                node.setPhysical(value(row, indexes.get("execution_engine")));
                node.setObjectPath(value(row, indexes.get("table_name")));
                node.setEstimatedRows(number(value(row, indexes.get("output_size"))));
                node.setProperties(properties(row, columns));
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
            return value == null ? null : Double.valueOf(value);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }
}
