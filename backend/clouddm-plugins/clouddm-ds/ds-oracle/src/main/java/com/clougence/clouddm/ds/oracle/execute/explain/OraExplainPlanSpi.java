/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.ds.oracle.execute.explain;

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

/** Parses Oracle DBMS_XPLAN.DISPLAY output. */
public class OraExplainPlanSpi implements ExplainPlanSpi {

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
            if (!(result instanceof com.clougence.clouddm.sdk.execute.resultset.echo.ResultSet resultSet) || resultSet.getRowSet() == null) {
                continue;
            }
            List<String> columns = metas.get(resultSet.getResultId());
            String column = columns == null || columns.isEmpty() ? "PLAN_TABLE_OUTPUT" : columns.get(0);
            for (ResultSetRow row : resultSet.getRowSet()) {
                String line = value(row, 0);
                ExplainPlanNode node = planLine(line, column, plan.getNodes().size());
                if (node != null) {
                    plan.getNodes().add(node);
                }
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
            }
        }
        source(plan, results, relations);
        return plan;
    }

    private static ExplainPlanNode planLine(String line, String column, int index) {
        if (line == null || !line.startsWith("|") || line.contains("Operation")) {
            return null;
        }
        List<String> cells = cells(line);
        if (cells.size() < 8 || !unsignedInteger(cells.get(1).replace("*", "").trim())) {
            return null;
        }
        ExplainPlanNode node = new ExplainPlanNode();
        node.setNodeId(String.valueOf(index));
        node.setPhysical(cells.get(2).trim());
        node.setObjectPath(cells.get(3).trim().isEmpty() ? null : cells.get(3).trim());
        node.setEstimatedRows(number(cells.get(4).trim()));
        node.getProperties().put(column, line);
        return node;
    }

    private static List<String> cells(String line) {
        List<String> cells = new ArrayList<>();
        int start = 0;
        int separator;
        while ((separator = line.indexOf('|', start)) >= 0) {
            cells.add(line.substring(start, separator));
            start = separator + 1;
        }
        cells.add(line.substring(start));
        return cells;
    }

    private static boolean unsignedInteger(String value) {
        if (value.isEmpty()) {
            return false;
        }
        for (int index = 0; index < value.length(); index++) {
            if (!Character.isDigit(value.charAt(index))) {
                return false;
            }
        }
        return true;
    }

    private static BehaviorRelation write(List<BehaviorRelation> relations) {
        if (relations == null) {
            return null;
        }
        return relations.stream().filter(relation -> relation != null && ACTIONS.contains(relation.getAction())).findFirst().orElse(null);
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

    private static Double number(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            String normalized = value.toUpperCase(Locale.ROOT);
            double multiplier = 1D;
            if (normalized.endsWith("K")) {
                multiplier = 1_000D;
                normalized = normalized.substring(0, normalized.length() - 1);
            } else if (normalized.endsWith("M")) {
                multiplier = 1_000_000D;
                normalized = normalized.substring(0, normalized.length() - 1);
            } else if (normalized.endsWith("G")) {
                multiplier = 1_000_000_000D;
                normalized = normalized.substring(0, normalized.length() - 1);
            }
            return Double.valueOf(normalized) * multiplier;
        } catch (NumberFormatException ignored) {
            return null;
        }
    }
}
