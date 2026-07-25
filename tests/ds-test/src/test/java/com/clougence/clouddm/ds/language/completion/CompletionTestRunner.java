/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.ds.language.completion;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Constructor;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.*;

import org.junit.jupiter.api.DynamicTest;

import com.clougence.clouddm.sdk.language.DsLanguageSpi;
import com.clougence.clouddm.sdk.language.completion.CompletionItem;
import com.clougence.clouddm.sdk.language.completion.CompletionRequest;
import com.clougence.clouddm.sdk.language.completion.CompletionResult;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.sql.SqlEngineSpi;
import com.clougence.clouddm.ds.SqlTestSupport;
import com.clougence.clouddm.ds.TextCaseSupport;
import com.clougence.clouddm.ds.VirtualMetaService;
import com.clougence.schema.umi.struts.UmiTypes;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

final class CompletionTestRunner {

    private static final ObjectMapper JSON = new ObjectMapper();

    CompletionTestRunner(){
    }

    Collection<DynamicTest> tests() {
        return TextCaseSupport.resourceFiles("language/completion", path -> !path.contains("/reports/")).stream()
            .flatMap(path -> CompletionScriptParser.parse(path).stream())
            .map(testCase -> DynamicTest.dynamicTest(testCase.path() + "#" + testCase.name(), () -> run(testCase)))
            .toList();
    }

    private void run(CompletionScriptCase testCase) throws Exception {
        MetaService metaService = new VirtualMetaService(resource(testCase.meta()));
        DsLanguageSpi language = instantiate(testCase.languageClass(), MetaService.class, metaService);
        SqlEngineSpi sqlEngine = SqlTestSupport.sqlEngine(datasource(testCase.path()), metaService);

        CompletionRequest request = new CompletionRequest();
        request.setRequestId(testCase.name());
        request.setRequestVersion(1);
        request.setPrimaryUserId("puid");
        request.setCurrentUserId("uid");
        request.setDataSourceId(testCase.dataSourceId());
        request.setCatalog(testCase.catalog());
        request.setSchema(testCase.schema());
        request.setLevels(Arrays.asList(UmiTypes.values()));
        HashMap<UmiTypes, Object> levelsParam = new HashMap<>();
        if (testCase.catalog() != null && !testCase.catalog().isBlank()) {
            levelsParam.put(UmiTypes.Catalog, testCase.catalog());
        }
        if (testCase.schema() != null && !testCase.schema().isBlank()) {
            levelsParam.put(UmiTypes.Schema, testCase.schema());
        }
        request.setLevelsParam(levelsParam);
        request.setBasicCodeLine(1);
        request.setBasicCodeColumn(0);
        request.setSqlText(testCase.sqlText());
        request.setCursorLineNumber(testCase.cursorLineNumber());
        request.setCursorColNumber(testCase.cursorColNumber());
        request.setSqlEngine(sqlEngine);

        CompletionResult result = language.complete(request);
        JsonNode expected = expectedLabels(JSON.readTree(testCase.expectJson()));
        JsonNode actual = JSON.valueToTree(result.getItems().stream().map(CompletionItem::getLabel).toList());
        assertEquals(JSON.writeValueAsString(expected), JSON.writeValueAsString(actual));
    }

    private Path resource(String name) throws URISyntaxException {
        return Path.of(Thread.currentThread().getContextClassLoader().getResource(name).toURI());
    }

    private String datasource(String path) {
        String relative = path.substring("language/completion/".length());
        String[] parts = relative.split("/");
        if (parts.length < 2) {
            throw new IllegalStateException("Invalid completion resource path: " + path);
        }
        return SqlTestSupport.datasourceFromFilename(parts[0], parts[parts.length - 1]);
    }

    private static JsonNode expectedLabels(JsonNode expected) {
        if (expected.isArray()) {
            List<String> labels = new ArrayList<>();
            for (JsonNode node : expected) {
                labels.add(node.isTextual() ? node.asText() : node.path("label").asText());
            }
            return JSON.valueToTree(labels);
        }
        return expected;
    }

    private static <T> T instantiate(String className) throws Exception {
        Constructor<?> constructor = Class.forName(className).getDeclaredConstructor();
        constructor.setAccessible(true);
        return (T) constructor.newInstance();
    }

    private static <T> T instantiate(String className, Class<?> argType, Object arg) throws Exception {
        Constructor<?> constructor = Class.forName(className).getDeclaredConstructor(argType);
        constructor.setAccessible(true);
        return (T) constructor.newInstance(arg);
    }
}
