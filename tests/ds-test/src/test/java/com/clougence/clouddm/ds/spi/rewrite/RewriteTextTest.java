/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.spi.rewrite;

import java.util.List;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import com.clougence.clouddm.ds.TextCaseSupport;
import com.clougence.clouddm.ds.TextCaseSupport.CaseBlock;
import com.clougence.clouddm.ds.TextTestFramework;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteContext;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Execution(ExecutionMode.CONCURRENT)
public abstract class RewriteTextTest {

    private static final String       NULL_QUERY = "<null>";
    private static final ObjectMapper JSON       = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL).enable(DeserializationFeature.USE_LONG_FOR_INTS);

    private final String              resourceDirectory;

    protected RewriteTextTest(String resourceDirectory){
        this.resourceDirectory = resourceDirectory;
    }

    static List<RewriteTextCase> loadCases(String resourcePath) {
        return TextCaseSupport.loadBlocks(resourcePath).stream().map(RewriteTextTest::parseCase).toList();
    }

    private static RewriteTextCase parseCase(CaseBlock block) {
        RewriteTextCase testCase = new RewriteTextCase(block);
        String body = block.body();
        testCase.setMethod(TextCaseSupport.readRequiredLine(body, "method:"));
        testCase.setQueryId(TextCaseSupport.readOptionalLine(body, "query-id:"));
        testCase.setQuery(TextCaseSupport.section(body, "query:", "input-context:").strip());
        testCase.setInputContextJson(TextCaseSupport.section(body, "input-context:", "expect-query:").strip());
        String expectedQuery = TextCaseSupport.section(body, "expect-query:", "expect-context:").strip();
        if (!NULL_QUERY.equals(expectedQuery)) {
            testCase.setExpectedQuery(expectedQuery);
        }
        testCase.setExpectedContextJson(TextCaseSupport.section(body, "expect-context:", null).strip());
        return testCase;
    }

    private static void assertCase(RewriteTextCase testCase, RewriteSpi spi) throws Exception {
        RewriteContext context = JSON.readValue(testCase.inputContextJson(), RewriteContext.class);
        String actualQuery;
        if ("rewriteLimit".equals(testCase.method())) {
            actualQuery = spi.rewriteLimit(testCase.query(), context);
        } else if ("rewriteDmlToQuery".equals(testCase.method())) {
            if (testCase.queryId() == null || testCase.queryId().isBlank()) {
                Assert.fail(testCase.caseId() + " requires query-id");
            }
            actualQuery = spi.rewriteDmlToQuery(testCase.queryId(), testCase.query(), context);
        } else {
            Assert.fail(testCase.caseId() + " has unsupported method: " + testCase.method());
            return;
        }

        Assert.assertEquals(testCase.caseId() + " query", testCase.expectedQuery(), actualQuery);
        JsonNode expectedContext = JSON.readTree(testCase.expectedContextJson());
        JsonNode actualContext = JSON.valueToTree(context);
        Assert.assertEquals(testCase.caseId() + " context", expectedContext, actualContext);
    }

    protected abstract RewriteSpi rewriteSpi(RewriteTextCase testCase);

    @TestFactory
    public Stream<DynamicTest> rewrites() {
        return TextTestFramework.dynamicTests(TextCaseSupport.resourceFiles(this.resourceDirectory), RewriteTextTest::loadCases, testCase -> {
            return DynamicTest.dynamicTest(testCase.caseId(), () -> assertCase(testCase, this.rewriteSpi(testCase)));
        });
    }
}
