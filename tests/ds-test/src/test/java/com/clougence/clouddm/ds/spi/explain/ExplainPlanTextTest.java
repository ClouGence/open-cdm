/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.spi.explain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import com.clougence.clouddm.ds.TextCaseSupport;
import com.clougence.clouddm.ds.TextCaseSupport.CaseBlock;
import com.clougence.clouddm.ds.TextTestFramework;
import com.clougence.clouddm.sdk.execute.explain.ExplainPlan;
import com.clougence.clouddm.sdk.execute.explain.ExplainPlanSpi;
import com.clougence.clouddm.sdk.execute.resultset.echo.Result;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultSetMeta;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultSetRow;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultSetValue;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorRelation;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Execution(ExecutionMode.CONCURRENT)
public abstract class ExplainPlanTextTest {

    private static final ObjectMapper JSON = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

    private final String resourceDirectory;

    protected ExplainPlanTextTest(String resourceDirectory){
        this.resourceDirectory = resourceDirectory;
    }

    protected abstract ExplainPlanSpi explainPlanSpi();

    @TestFactory
    public Stream<DynamicTest> explainPlans() {
        return TextTestFramework.dynamicTests(TextCaseSupport.resourceFiles(this.resourceDirectory), ExplainPlanTextTest::loadCases, testCase -> {
            return DynamicTest.dynamicTest(testCase.caseId() + " " + summarize(testCase.sql()), () -> assertCase(testCase, this.explainPlanSpi()));
        });
    }

    static List<ExplainPlanTextCase> loadCases(String resourcePath) {
        return TextCaseSupport.loadBlocks(resourcePath).stream().map(ExplainPlanTextTest::parseCase).toList();
    }

    private static ExplainPlanTextCase parseCase(CaseBlock block) {
        ExplainPlanTextCase testCase = new ExplainPlanTextCase(block);
        String body = block.body();
        int sqlIndex = body.indexOf("sql:");
        int inputIndex = body.indexOf("input:");
        int relationsIndex = body.indexOf("relations:");
        int expectIndex = body.indexOf("expect:");
        if (sqlIndex < 0 || inputIndex <= sqlIndex || expectIndex <= inputIndex) {
            throw new IllegalArgumentException("Invalid EXPLAIN plan test case: " + testCase.name());
        }
        testCase.setSql(body.substring(sqlIndex + "sql:".length(), inputIndex).trim());
        int inputEnd = expectIndex;
        if (relationsIndex > inputIndex && relationsIndex < expectIndex) {
            inputEnd = relationsIndex;
        }
        testCase.setInputJson(body.substring(inputIndex + "input:".length(), inputEnd).trim());
        if (inputEnd == relationsIndex) {
            testCase.setRelationsJson(body.substring(relationsIndex + "relations:".length(), expectIndex).trim());
        }
        testCase.setExpectJson(body.substring(expectIndex + "expect:".length()).trim());
        return testCase;
    }

    private static void assertCase(ExplainPlanTextCase testCase, ExplainPlanSpi spi) throws Exception {
        List<BehaviorRelation> relations = List.of();
        if (testCase.relationsJson() != null) {
            relations = JSON.readerForListOf(BehaviorRelation.class).readValue(testCase.relationsJson());
        }
        ExplainPlan actual = spi.analyze(rawResults(testCase), relations);
        JsonNode expectedJson = JSON.readTree(testCase.expectJson());
        JsonNode actualJson = JSON.valueToTree(actual);
        Assert.assertEquals(testCase.caseId(), expectedJson, actualJson);
    }

    private static List<Result> rawResults(ExplainPlanTextCase testCase) throws Exception {
        JsonNode input = JSON.readTree(testCase.inputJson());
        if (!input.isArray()) {
            Assert.fail(testCase.caseId() + " input must be a JSON array");
        }
        if (input.isEmpty()) {
            return List.of();
        }

        List<String> columns = fieldNames(input.get(0));
        List<ResultSetRow> rows = new ArrayList<>();
        for (int rowIndex = 0; rowIndex < input.size(); rowIndex++) {
            JsonNode inputRow = input.get(rowIndex);
            if (!inputRow.isObject()) {
                Assert.fail(testCase.caseId() + " input[" + rowIndex + "] must be a JSON object");
            }
            List<String> rowColumns = fieldNames(inputRow);
            Assert.assertEquals(testCase.caseId() + " input rows must have identical ordered columns", columns, rowColumns);

            List<ResultSetValue> values = new ArrayList<>();
            for (String column : columns) {
                JsonNode valueNode = inputRow.get(column);
                String value = null;
                if (valueNode != null && !valueNode.isNull()) {
                    value = valueNode.asText();
                }
                long size = 0;
                if (value != null) {
                    size = value.length();
                }
                values.add(ResultSetValue.of(true, false, value, 0, size));
            }
            ResultSetRow row = new ResultSetRow();
            row.setData(values);
            rows.add(row);
        }

        String resultId = testCase.caseIndexId();
        ResultSetMeta meta = new ResultSetMeta();
        meta.setResultId(resultId);
        meta.setColumnList(columns);
        com.clougence.clouddm.sdk.execute.resultset.echo.ResultSet resultSet = new com.clougence.clouddm.sdk.execute.resultset.echo.ResultSet();
        resultSet.setResultId(resultId);
        resultSet.setRowSet(rows);
        return List.of(meta, resultSet);
    }

    private static List<String> fieldNames(JsonNode node) {
        List<String> fields = new ArrayList<>();
        Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();
        iterator.forEachRemaining(entry -> fields.add(entry.getKey()));
        return fields;
    }

    private static String summarize(String sql) {
        String oneLine = sql.replaceAll("\\s+", " ").strip();
        if (oneLine.length() <= 100) {
            return oneLine;
        }
        return oneLine.substring(0, 97) + "...";
    }
}
