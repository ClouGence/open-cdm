/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.ds.lineage.mysql;

import java.util.*;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import com.clougence.clouddm.ds.TextCaseSupport;
import com.clougence.clouddm.ds.VirtualMetaService;
import com.clougence.clouddm.sdk.service.execute.MetaCol;
import com.clougence.clouddm.sdk.service.execute.MetaObj;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.lineage.ColumnLineage;
import com.clougence.clouddm.sdk.sql.analysis.lineage.LineageAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.lineage.LineageContext;
import com.clougence.clouddm.sdk.sql.analysis.security.column.QueryItem;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.RdbColumnDomain;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.RdbSelectDomain;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.RdbTableDomain;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.mysql.MySqlEngineSpi;
import com.clougence.sql.mysql.analysis.security.MySqlParserVisitor;
import com.clougence.sql.mysql.analysis.security.builder.MyBuilderFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Uses the split corpus as the canonical MySQL SQL source. The complete corpus
 * is retained under lineage/mysql and every SELECT case is executed.
 */
public abstract class MySqlLineageTextTest {

    private static final String LONG_DELIMITER  = "------------------------------------------------------------------------------------------";
    private static final String SHORT_DELIMITER = "----------";
    private static final ObjectMapper JSON      = new ObjectMapper();

    private final String        resourceDirectory;
    private final String        version;

    protected MySqlLineageTextTest(String directoryName, String version){
        this.resourceDirectory = "lineage/mysql/" + directoryName;
        this.version = version;
    }

    @TestFactory
    public Stream<DynamicTest> lineagePatterns() {
        CorpusMetaService metaService = new CorpusMetaService(version);
        LineageAnalysisSpi analysisSpi = new MySqlEngineSpi(metaService).lineageAnalysisSpi(SqlParserParameters.ofVersion(version));

        return fixtureResources().stream().flatMap(resourcePath -> loadLineageCases(resourcePath).stream())
            .map(testCase -> DynamicTest.dynamicTest(testCase.displayName(), () -> assertExpected(testCase, metaService, analysisSpi)));
    }

    protected List<String> fixtureResources() {
        return TextCaseSupport.resourceFiles(resourceDirectory, path -> {
            return !path.contains("/reject/") && //
                   !path.contains("/mode/") &&   //
                   !path.substring(resourceDirectory.length()).contains("/exact-");
        });
    }

    static List<CorpusCase> loadCases(String resourcePath) {
        String content = TextCaseSupport.readResource(resourcePath);
        int delimiter = content.indexOf(LONG_DELIMITER);
        if (delimiter < 0) {
            throw new IllegalArgumentException("Invalid split-derived lineage fixture: " + resourcePath);
        }

        List<CorpusCase> cases = new ArrayList<>();
        String expected = content.substring(delimiter + LONG_DELIMITER.length());
        int index = 0;
        for (String block : splitBlocks(expected)) {
            String normalized = block.strip();
            if (normalized.isEmpty()) {
                continue;
            }
            int typeEnd = normalized.indexOf(']');
            if (!normalized.startsWith("[") || typeEnd < 0) {
                throw new IllegalArgumentException("Invalid split-derived lineage case: " + resourcePath);
            }
            index++;
            String type = normalized.substring(1, typeEnd);
            int variantSeparator = type.indexOf('|');
            String primaryType = variantSeparator < 0 ? type : type.substring(0, variantSeparator);
            if (!"SELECT".equals(primaryType)) {
                continue;
            }
            String sql = normalized.substring(typeEnd + 1).strip();
            if (!isQuerySql(sql)) {
                continue;
            }
            cases.add(new CorpusCase(resourcePath, index, sql));
        }
        return cases;
    }

    private static List<LineageCase> loadLineageCases(String resourcePath) {
        List<LineageCase> cases = new ArrayList<>();
        TextCaseSupport.loadBlocks(resourcePath).forEach(block -> {
            String body = block.body();
            int sqlIndex = body.indexOf("sql:");
            int expectIndex = body.indexOf("expect:");
            if (sqlIndex < 0 || expectIndex <= sqlIndex) {
                throw new IllegalArgumentException("Invalid lineage fixture: " + resourcePath + "#" + block.index());
            }
            String sql = body.substring(sqlIndex + "sql:".length(), expectIndex).strip();
            String expectation = body.substring(expectIndex + "expect:".length()).strip();
            cases.add(new LineageCase(resourcePath, block.name(), sql, expectation));
        });
        return cases;
    }

    private static void assertExpected(LineageCase testCase, CorpusMetaService metaService, LineageAnalysisSpi analysisSpi) throws JsonProcessingException {
        JsonNode expected = JSON.readTree(testCase.expectation());
        try {
            List<ColumnLineage> columns = metaService.withSql(testCase.sql(), () -> analysisSpi.analyze(testCase.sql(), lineageContext()));
            if (expected.has("exception")) {
                Assert.fail("Expected " + expected.get("exception").asText() + " but analysis returned " + expectJson(columns));
            }
            Assert.assertEquals(expected, JSON.readTree(expectJson(columns)));
        } catch (RuntimeException e) {
            if (!expected.has("exception")) {
                throw e;
            }
            Assert.assertEquals(expected.get("exception").asText(), e.getClass().getSimpleName());
        }
    }

    private static List<String> splitBlocks(String content) {
        List<String> blocks = new ArrayList<>();
        int blockStart = 0;
        int lineStart = 0;
        for (int i = 0; i <= content.length(); i++) {
            if (i != content.length() && content.charAt(i) != '\n') {
                continue;
            }
            int lineEnd = i;
            if (lineEnd > lineStart && content.charAt(lineEnd - 1) == '\r') {
                lineEnd--;
            }
            if (content.substring(lineStart, lineEnd).trim().equals(SHORT_DELIMITER)) {
                blocks.add(content.substring(blockStart, lineStart));
                blockStart = i + 1;
            }
            lineStart = i + 1;
        }
        blocks.add(content.substring(blockStart));
        return blocks;
    }

    private static boolean isQuerySql(String sql) {
        String normalized = sql.stripLeading().toUpperCase(Locale.ROOT);
        return normalized.startsWith("SELECT") || normalized.startsWith("WITH") || normalized.startsWith("(");
    }

    static LineageContext lineageContext() {
        return LineageContext.builder().levelsParam(Map.of(UmiTypes.Catalog, "catalog1", UmiTypes.Schema, "schema1")).build();
    }

    static String expectJson(List<ColumnLineage> columns) {
        Map<String, List<String>> lineage = new LinkedHashMap<>();
        for (ColumnLineage column : columns) {
            List<String> sources = lineage.computeIfAbsent(column.column(), ignored -> new ArrayList<>());
            column.sources().forEach(source -> sources.add(source.toDsResPath()));
        }
        try {
            return JSON.writerWithDefaultPrettyPrinter().writeValueAsString(lineage);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize lineage expectation", e);
        }
    }

    static record CorpusCase(String resourcePath, int index, String sql) {

        String displayName() {
            String summary = collapseWhitespace(sql);
            if (summary.length() > 120) {
                summary = summary.substring(0, 117) + "...";
            }
            return resourcePath + "#" + String.format("%03d", index) + " " + summary;
        }

        private static String collapseWhitespace(String value) {
            StringBuilder normalized = new StringBuilder(value.length());
            boolean pendingSpace = false;
            for (int i = 0; i < value.length(); i++) {
                char current = value.charAt(i);
                if (Character.isWhitespace(current)) {
                    pendingSpace = normalized.length() > 0;
                } else {
                    if (pendingSpace) {
                        normalized.append(' ');
                        pendingSpace = false;
                    }
                    normalized.append(current);
                }
            }
            return normalized.toString();
        }
    }

    private record LineageCase(String resourcePath, String name, String sql, String expectation) {

        String displayName() {
            return resourcePath + "#" + name + " " + CorpusCase.collapseWhitespace(sql);
        }
    }

    static final class CorpusMetaService implements MetaService {

        private final MetaService               delegate      = new VirtualMetaService();
        private final DslProvider               provider;
        private final ThreadLocal<ParsedSchema> currentSchema = new ThreadLocal<>();

        CorpusMetaService(String version){
            this.provider = new MySqlEngineSpi(delegate).dslProvider(SqlParserParameters.ofVersion(version));
        }

        <T> T withSql(String sql, java.util.function.Supplier<T> action) {
            currentSchema.set(parseSchema(sql));
            try {
                return action.get();
            } catch (RuntimeException e) {
                throw new RuntimeException(e.getMessage() + ", parsedMeta=" + currentSchema.get(), e);
            } finally {
                currentSchema.remove();
            }
        }

        private ParsedSchema parseSchema(String sql) {
            MyBuilderFactory builder = new MyBuilderFactory(delegate);
            DslHelper.doVisitor(provider, sql, (lexer, parser) -> new MySqlParserVisitor(builder, parser));
            return ParsedSchema.from(builder.buildKeepOrigin());
        }

        @Override
        public List<MetaCol> fetchTableColumns(String uid, long dsId, Map<UmiTypes, Object> levelsParam, String tableName) {
            try {
                return delegate.fetchTableColumns(uid, dsId, levelsParam, tableName);
            } catch (IllegalStateException ignored) {
                ParsedSchema schemaInfo = Objects.requireNonNull(currentSchema.get(), "SQL context is missing");
                LinkedHashSet<String> columnNames = schemaInfo.columns(levelsParam, tableName);
                if (columnNames.isEmpty()) {
                    columnNames.add("id");
                }
                String schema = Objects.toString(levelsParam.get(UmiTypes.Schema), null);
                String catalog = Objects.toString(levelsParam.get(UmiTypes.Catalog), null);
                return columnNames.stream().map(columnName -> {
                    MetaCol column = new MetaCol();
                    column.setCatalog(catalog);
                    column.setSchema(schema);
                    column.setTable(tableName);
                    column.setColumn(columnName);
                    return column;
                }).toList();
            }
        }

        @Override
        public List<MetaObj> cachedObjectNames(String puid, String uid, long dsId, List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam) {
            return delegate.cachedObjectNames(puid, uid, dsId, levels, levelsParam);
        }
    }

    private static final class ParsedSchema {

        private final Map<TableKey, LinkedHashSet<String>> tableColumns = new LinkedHashMap<>();
        private final Map<RdbSelectDomain, Scope>          scopes       = new IdentityHashMap<>();
        private final Map<RdbTableDomain, Set<String>>     activeVirtualAssignments = new IdentityHashMap<>();

        static ParsedSchema from(List<RuleDomain> domains) {
            ParsedSchema schema = new ParsedSchema();
            for (RuleDomain domain : domains) {
                if (domain instanceof RdbSelectDomain selectDomain) {
                    schema.analyzeSelect(selectDomain, null);
                }
            }
            return schema;
        }

        private Scope analyzeSelect(RdbSelectDomain selectDomain, Scope outerScope) {
            Scope existing = scopes.get(selectDomain);
            if (existing != null) {
                return existing;
            }

            List<TableRef> tables = new ArrayList<>();
            for (RuleDomain child : safeChildren(selectDomain)) {
                if (child instanceof RdbTableDomain tableDomain) {
                    TableRef table = new TableRef(tableDomain);
                    tables.add(table);
                    if (!tableDomain.isVirtual()) {
                        tableColumns.computeIfAbsent(table.key(), ignored -> new LinkedHashSet<>());
                    }
                }
            }
            boolean naturalJoin = selectDomain.getOptions() != null
                    && Boolean.parseBoolean(selectDomain.getOptions().get("naturalJoin"));
            Scope scope = new Scope(tables, outerScope, naturalJoin, selectDomain.getJoinUsingColumns());
            scopes.put(selectDomain, scope);

            for (TableRef table : tables) {
                if (table.domain().isVirtual()) {
                    for (RuleDomain child : safeChildren(table.domain())) {
                        if (child instanceof RdbSelectDomain nestedSelect) {
                            analyzeSelect(nestedSelect, scope);
                        }
                    }
                }
            }
            for (QueryItem queryItem : selectDomain.getColumns()) {
                for (RuleDomain source : queryItem.getColumns()) {
                    analyzeSource(source, scope);
                }
            }
            for (RuleDomain whereDomain : selectDomain.getWhereDomains()) {
                analyzeSource(whereDomain, scope);
            }
            return scope;
        }

        private void analyzeSource(RuleDomain source, Scope scope) {
            if (source instanceof RdbColumnDomain columnDomain) {
                assignColumn(columnDomain.getTable(), columnDomain.getColumn(), scope);
            } else if (source instanceof RdbSelectDomain selectDomain) {
                analyzeSelect(selectDomain, scope);
            } else {
                for (RuleDomain child : safeChildren(source)) {
                    analyzeSource(child, scope);
                }
            }
        }

        private boolean assignColumn(String qualifier, String column, Scope scope) {
            if (scope == null || column == null || "*".equals(column)) {
                return false;
            }
            if (qualifier != null && !qualifier.isBlank()) {
                for (TableRef table : scope.tables()) {
                    if (table.hasQualifier(qualifier)) {
                        assignToTable(table, column);
                        return true;
                    }
                }
                return assignColumn(qualifier, column, scope.outer());
            }

            if (scope.naturalJoin() || scope.isUsingColumn(column)) {
                boolean assigned = false;
                for (TableRef table : scope.tables()) {
                    assignToTable(table, column);
                    assigned = true;
                }
                if (assigned) {
                    return true;
                }
            }

            List<TableRef> explicitVirtuals = scope.tables().stream().filter(table -> table.domain().isVirtual() && explicitlyProvides(table, column)).toList();
            if (explicitVirtuals.size() == 1) {
                assignToTable(explicitVirtuals.get(0), column);
                return true;
            }
            if (scope.tables().size() == 1) {
                assignToTable(scope.tables().get(0), column);
                return true;
            }

            List<TableRef> knownPhysicals = scope.tables().stream()
                .filter(table -> !table.domain().isVirtual() && tableColumns.getOrDefault(table.key(), new LinkedHashSet<>()).contains(column))
                .toList();
            if (knownPhysicals.size() == 1) {
                assignToTable(knownPhysicals.get(0), column);
                return true;
            }

            Optional<TableRef> firstPhysical = scope.tables().stream().filter(table -> !table.domain().isVirtual()).findFirst();
            if (firstPhysical.isPresent()) {
                assignToTable(firstPhysical.get(), column);
                return true;
            }
            return assignColumn(null, column, scope.outer());
        }

        private void assignToTable(TableRef table, String column) {
            if (!table.domain().isVirtual()) {
                tableColumns.computeIfAbsent(table.key(), ignored -> new LinkedHashSet<>()).add(column);
                return;
            }
            Set<String> activeColumns = activeVirtualAssignments.computeIfAbsent(table.domain(), ignored -> new HashSet<>());
            if (!activeColumns.add(column)) {
                return;
            }
            try {
                for (RuleDomain child : safeChildren(table.domain())) {
                    if (!(child instanceof RdbSelectDomain selectDomain)) {
                        continue;
                    }
                    Scope childScope = scopes.get(selectDomain);
                    boolean matched = false;
                    for (QueryItem queryItem : selectDomain.getColumns()) {
                        if (queryItem.isSelectAll()) {
                            matched = true;
                            assignColumn(queryItem.getTable(), column, childScope);
                            continue;
                        }
                        String outputName = queryItem.getItemAlias() == null ? queryItem.getColumn() : queryItem.getItemAlias();
                        if (Objects.equals(outputName, column)) {
                            matched = true;
                            for (RuleDomain source : queryItem.getColumns()) {
                                analyzeSource(source, childScope);
                            }
                        }
                    }
                    if (!matched && selectDomain.getColumns().stream().anyMatch(QueryItem::isSelectAll)) {
                        assignColumn(null, column, childScope);
                    }
                }
            } finally {
                activeColumns.remove(column);
                if (activeColumns.isEmpty()) {
                    activeVirtualAssignments.remove(table.domain());
                }
            }
        }

        private static boolean explicitlyProvides(TableRef table, String column) {
            for (RuleDomain child : safeChildren(table.domain())) {
                if (child instanceof RdbSelectDomain selectDomain) {
                    for (QueryItem queryItem : selectDomain.getColumns()) {
                        String outputName = queryItem.getItemAlias() == null ? queryItem.getColumn() : queryItem.getItemAlias();
                        if (!queryItem.isSelectAll() && Objects.equals(outputName, column)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        LinkedHashSet<String> columns(Map<UmiTypes, Object> levels, String tableName) {
            String catalog = normalizeName(Objects.toString(levels.get(UmiTypes.Catalog), null));
            String schema = normalizeName(Objects.toString(levels.get(UmiTypes.Schema), null));
            String table = normalizeName(tableName);
            return tableColumns.entrySet().stream()
                .filter(entry -> entry.getKey().matches(catalog, schema, table))
                .map(Map.Entry::getValue)
                .findFirst()
                .map(LinkedHashSet::new)
                .orElseGet(LinkedHashSet::new);
        }

        private static List<RuleDomain> safeChildren(RuleDomain domain) {
            return domain.getChildren() == null ? List.of() : domain.getChildren();
        }

        private static String normalizeName(String name) {
            if (name == null) {
                return "";
            }
            String cleaned = name.replace("`", "").replace("\"", "").trim();
            int dot = cleaned.lastIndexOf('.');
            return (dot < 0 ? cleaned : cleaned.substring(dot + 1)).toLowerCase(Locale.ROOT);
        }

        @Override
        public String toString() {
            return tableColumns.toString();
        }

        private record Scope(List<TableRef> tables, Scope outer, boolean naturalJoin, Set<String> usingColumns) {

            boolean isUsingColumn(String column) {
                return usingColumns != null && usingColumns.stream().anyMatch(name -> name != null && name.equalsIgnoreCase(column));
            }
        }

        private record TableRef(RdbTableDomain domain) {

            TableKey key() {
                return new TableKey(normalizeName(domain.getCatalog()), normalizeName(domain.getSchema()), normalizeName(domain.getTable()));
            }

            boolean hasQualifier(String qualifier) {
                String normalized = normalizeName(qualifier);
                return normalized.equals(key().table()) || normalized.equals(normalizeName(domain.getAlias()));
            }
        }

        private record TableKey(String catalog, String schema, String table) {

            boolean matches(String requestedCatalog, String requestedSchema, String requestedTable) {
                return Objects.equals(table, requestedTable)
                       && (catalog.isEmpty() || requestedCatalog.isEmpty() || Objects.equals(catalog, requestedCatalog))
                       && (schema.isEmpty() || requestedSchema.isEmpty() || Objects.equals(schema, requestedSchema));
            }
        }
    }
}
