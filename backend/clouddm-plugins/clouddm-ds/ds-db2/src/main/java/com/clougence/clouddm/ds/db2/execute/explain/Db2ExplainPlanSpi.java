/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.ds.db2.execute.explain;

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

/** Parses DB2 explain operators and their input stream cardinalities. */
public class Db2ExplainPlanSpi implements ExplainPlanSpi {

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
                if (!"O".equalsIgnoreCase(value(row, indexes.get("source_type")))) {
                    continue;
                }
                ExplainPlanNode node = new ExplainPlanNode();
                node.setNodeId(value(row, indexes.get("operator_id")));
                node.setLogical(value(row, indexes.get("operator_type")));
                node.setPhysical(value(row, indexes.get("operator_type")));
                String schema = value(row, indexes.get("object_schema"));
                String object = value(row, indexes.get("object_name"));
                if (object != null) {
                    node.setObjectPath(schema == null ? object : schema.strip() + "." + object.strip());
                }
                node.setEstimatedRows(number(value(row, indexes.get("stream_count"))));
                node.setProperties(properties(row, columns));
                plan.getNodes().add(node);
            }
        }
        BehaviorRelation write = write(relations);
        if (write != null) {
            ExplainPlanNode target = plan.getNodes().stream().filter(node -> write.getAction().name().equalsIgnoreCase(node.getLogical())).findFirst().orElse(null);
            if (target == null && !plan.getNodes().isEmpty()) {
                target = plan.getNodes().get(0);
            }
            if (target == null) {
                target = new ExplainPlanNode();
                target.setNodeId("0");
                plan.getNodes().add(target);
            }
            if (target != null) {
                target.setLogical(write.getAction().name());
                if (write.getSubject() != null) {
                    target.setObjectPath(write.getSubject().getObjectPath());
                }
                if (write.getInsertRows() != null) {
                    target.setEstimatedRows(write.getInsertRows().doubleValue());
                }
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
