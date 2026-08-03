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

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.TextCaseSupport;
import com.clougence.clouddm.ds.TextResourceShard;
import com.clougence.clouddm.ds.VirtualMetaService;
import com.clougence.clouddm.sdk.service.execute.MetaCol;
import com.clougence.clouddm.sdk.service.execute.MetaObj;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.lineage.LineageAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.lineage.LineageColumn;
import com.clougence.clouddm.sdk.sql.analysis.lineage.LineageContext;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.lineage.model.*;
import com.clougence.sql.mysql.MySqlEngineSpi;
import com.clougence.sql.mysql.analysis.lineage.antlr.MyLineageCstVisitor;
import com.clougence.sql.mysql.parser.antlr.MySqlParser.FullColumnNameContext;
import com.clougence.sql.mysql.parser.antlr.MySqlParserBaseVisitor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/** MySQL lineage fixtures are isolated by version directory. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Execution(ExecutionMode.CONCURRENT)
public abstract class BasicMySqlLineageTextTest {

    private static final String                          SHORT_DELIMITER = "----------";
    private static final ObjectMapper                    JSON            = new ObjectMapper();
    private static final String                          EXPECT_OUTPUT   = System.getenv("MYSQL_LINEAGE_EXPECT_OUTPUT_DIR");

    private final String                                 resourceDirectory;
    private final String                                 version;
    private final TextResourceShard                      fixtureShard;
    private final Map<String, List<RecordedCase>>         recordedCases = new ConcurrentHashMap<>();

    protected BasicMySqlLineageTextTest(String directoryName, String version, int shardCount, int shardId){
        this.resourceDirectory = "lineage/mysql/" + directoryName;
        this.version = version;
        this.fixtureShard = new TextResourceShard(resourceDirectory, shardCount, shardId);
    }

    @TestFactory
    public Stream<DynamicTest> lineagePatterns() {
        CorpusMetaService metaService = new CorpusMetaService(version);
        ThreadLocal<LineageAnalysisSpi> analysisSpi = ThreadLocal.withInitial(
                () -> new MySqlEngineSpi(metaService).lineageAnalysisSpi(SqlParserParameters.ofVersion(version)));

        return fixtureResources().stream()
            .flatMap(resourcePath -> loadLineageCases(resourcePath).stream())
            .map(testCase -> DynamicTest.dynamicTest(testCase.displayName(), () -> assertExpected(testCase, metaService, analysisSpi.get())));
    }

    protected final List<String> fixtureResources() {
        String resourceFilter = System.getenv("MYSQL_LINEAGE_RESOURCE_FILTER");
        return fixtureShard.resourceFiles(path -> {
            return !path.contains("/reject/") && //
                   !path.contains("/mode/") &&   //
                   !isNestedExactVersion(path) && //
                   (resourceFilter == null || resourceFilter.isBlank() || path.contains(resourceFilter));
        });
    }

    private boolean isNestedExactVersion(String path) {
        return path.substring(resourceDirectory.length()).contains("/exact-");
    }

    private static List<LineageCase> loadLineageCases(String resourcePath) {
        List<LineageCase> cases = new ArrayList<>();
        String caseFilter = System.getenv("MYSQL_LINEAGE_CASE_FILTER");
        TextCaseSupport.loadBlocks(resourcePath).forEach(block -> {
            if (caseFilter != null && !caseFilter.isBlank() && !block.name().contains(caseFilter)) {
                return;
            }
            String body = block.body();
            int sqlIndex = body.indexOf("sql:");
            int expectIndex = body.indexOf("expect:");
            if (sqlIndex < 0 || expectIndex <= sqlIndex) {
                throw new IllegalArgumentException("Invalid lineage fixture: " + resourcePath + "#" + block.index());
            }
            String sql = body.substring(sqlIndex + "sql:".length(), expectIndex).strip();
            String expectation = body.substring(expectIndex + "expect:".length()).strip();
            cases.add(new LineageCase(resourcePath, block.name(), sql, expectation, block.index()));
        });
        return cases;
    }

    private void assertExpected(LineageCase testCase, CorpusMetaService metaService, LineageAnalysisSpi analysisSpi) throws JsonProcessingException {
        JsonNode expected = JSON.readTree(testCase.expectation());
        try {
            List<LineageColumn> columns = metaService.withSql(testCase.sql(), () -> {
                try (StringReader reader = new StringReader(testCase.sql());
                        Stream<LineageColumn> stream = analysisSpi.analyzeStream(reader, lineageContext())) {
                    return stream.toList();
                }
            });
            if (recordingExpectations()) {
                record(testCase, expectJson(columns));
                return;
            }
            if (expected.has("exception")) {
                Assert.fail("Expected " + expected.get("exception").asText() + " but analysis returned " + expectJson(columns));
            }
            Assert.assertEquals(expected, JSON.readTree(expectJson(columns)));
        } catch (RuntimeException e) {
            Throwable root = rootCause(e);
            if (recordingExpectations()) {
                record(testCase, exceptionJson(root));
                return;
            }
            if (!expected.has("exception")) {
                throw e;
            }
            Assert.assertEquals(expected.get("exception").asText(), root.getClass().getSimpleName());
        }
    }

    @AfterAll
    void writeRecordedExpectations() throws IOException {
        if (!recordingExpectations()) {
            return;
        }
        Map<String, List<RecordedCase>> snapshot;
        synchronized (recordedCases) {
            snapshot = new LinkedHashMap<>(recordedCases);
            recordedCases.clear();
        }
        Path outputRoot = Path.of(EXPECT_OUTPUT);
        for (Map.Entry<String, List<RecordedCase>> entry : snapshot.entrySet()) {
            List<RecordedCase> cases = new ArrayList<>(entry.getValue());
            cases.sort(Comparator.comparingInt(RecordedCase::index));
            StringBuilder fixture = new StringBuilder();
            for (int index = 0; index < cases.size(); index++) {
                RecordedCase testCase = cases.get(index);
                if (index > 0) {
                    fixture.append(SHORT_DELIMITER).append(System.lineSeparator());
                }
                fixture.append('[')
                    .append(testCase.name())
                    .append(']')
                    .append(System.lineSeparator())
                    .append("sql:")
                    .append(System.lineSeparator())
                    .append(testCase.sql())
                    .append(System.lineSeparator())
                    .append("expect:")
                    .append(System.lineSeparator())
                    .append(testCase.actual())
                    .append(System.lineSeparator());
            }
            Path output = outputRoot.resolve(entry.getKey());
            Files.createDirectories(output.getParent());
            Files.writeString(output, fixture.toString(), StandardCharsets.UTF_8);
        }
    }

    private static boolean recordingExpectations() {
        return EXPECT_OUTPUT != null && !EXPECT_OUTPUT.isBlank();
    }

    private void record(LineageCase testCase, String actual) {
        recordedCases.computeIfAbsent(testCase.resourcePath(), ignored -> Collections.synchronizedList(new ArrayList<>()))
            .add(new RecordedCase(testCase.name(), testCase.sql(), actual, testCase.index()));
    }

    private static String exceptionJson(Throwable exception) throws JsonProcessingException {
        return JSON.writerWithDefaultPrettyPrinter().writeValueAsString(
                Map.of("exception", exception.getClass().getSimpleName()));
    }

    private static Throwable rootCause(Throwable throwable) {
        Throwable root = throwable;
        while (root.getCause() != null && root.getCause() != root) {
            root = root.getCause();
        }
        return root;
    }

    static LineageContext lineageContext() {
        return LineageContext.builder().levelsParam(Map.of(UmiTypes.Catalog, "catalog1", UmiTypes.Schema, "schema1")).build();
    }

    static String expectJson(List<LineageColumn> columns) {
        if (hasDuplicateColumnNames(columns)) {
            List<Map<String, Object>> lineage = new ArrayList<>();
            for (LineageColumn column : columns) {
                lineage.add(Map.of("column", column.column(), "sources", locatedSources(column)));
            }
            try {
                return JSON.writerWithDefaultPrettyPrinter().writeValueAsString(lineage);
            } catch (JsonProcessingException e) {
                throw new IllegalStateException("Failed to serialize lineage expectation", e);
            }
        }

        Map<String, List<String>> lineage = new LinkedHashMap<>();
        for (LineageColumn column : columns) {
            lineage.put(column.column(), locatedSources(column));
        }
        try {
            return JSON.writerWithDefaultPrettyPrinter().writeValueAsString(lineage);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize lineage expectation", e);
        }
    }

    private static boolean hasDuplicateColumnNames(List<LineageColumn> columns) {
        Set<String> names = new HashSet<>();
        for (LineageColumn column : columns) {
            if (!names.add(column.column())) {
                return true;
            }
        }
        return false;
    }

    private static List<String> locatedSources(LineageColumn column) {
        return column.sources().stream().map(source -> source.toLocatedDsResPath()).toList();
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

    private record LineageCase(String resourcePath, String name, String sql, String expectation, int index) {

        String displayName() {
            return resourcePath + "#" + name + " " + collapseWhitespace(sql);
        }
    }

    private record RecordedCase(String name, String sql, String actual, int index) {
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
            AtomicReference<CorpusCstVisitor> visitorRef = new AtomicReference<>();
            DslHelper.doVisitor(provider, new StringReader(sql), (lexer, parser) -> {
                CorpusCstVisitor visitor = new CorpusCstVisitor(parser);
                visitorRef.set(visitor);
                return visitor;
            });
            CorpusCstVisitor visitor = Objects.requireNonNull(visitorRef.get(), "MySQL corpus visitor is missing");
            return ParsedSchema.from(visitor.query(), visitor.hints());
        }

        @Override
        public List<MetaCol> fetchTableColumns(String uid, long dsId, Map<UmiTypes, Object> levelsParam, String tableName) {
            LinkedHashMap<String, MetaCol> columns = new LinkedHashMap<>();
            try {
                delegate.fetchTableColumns(uid, dsId, levelsParam, tableName).forEach(column -> columns.put(normalizeColumn(column.getColumn()), column));
            } catch (IllegalStateException ignored) {
                // The version corpus intentionally refers to tables outside the
                // small shared metadata fixture. Parsed hints complete that schema.
            }

            ParsedSchema schemaInfo = Objects.requireNonNull(currentSchema.get(), "SQL context is missing");
            LinkedHashSet<String> inferredColumns = schemaInfo.columns(levelsParam, tableName);
            if (columns.size() == 1 && columns.containsKey("id")
                    && !inferredColumns.isEmpty()
                    && inferredColumns.stream().noneMatch("id"::equalsIgnoreCase)) {
                columns.clear();
            }
            if (columns.isEmpty() && inferredColumns.isEmpty()) {
                inferredColumns.add("id");
            }
            String schema = Objects.toString(levelsParam.get(UmiTypes.Schema), null);
            String catalog = Objects.toString(levelsParam.get(UmiTypes.Catalog), null);
            inferredColumns.forEach(columnName -> {
                columns.computeIfAbsent(normalizeColumn(columnName), ignored -> {
                    MetaCol column = new MetaCol();
                    column.setCatalog(catalog);
                    column.setSchema(schema);
                    column.setTable(tableName);
                    column.setColumn(columnName);
                    return column;
                });
            });
            return List.copyOf(columns.values());
        }

        private static String normalizeColumn(String column) {
            return column == null ? "" : column.toLowerCase(Locale.ROOT);
        }

        @Override
        public List<MetaObj> cachedObjectNames(String puid, String uid, long dsId, List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam) {
            return delegate.cachedObjectNames(puid, uid, dsId, levels, levelsParam);
        }
    }

    private static final class ParsedSchema {

        private final Map<TableKey, LinkedHashSet<String>> tableColumns     = new LinkedHashMap<>();
        private final Map<String, LinkedHashSet<TableKey>> qualifierTargets = new LinkedHashMap<>();
        private final Map<String, Set<String>>             virtualColumns   = new LinkedHashMap<>();
        private boolean                                    guessUnqualified;

        static ParsedSchema from(LineageQuery query, List<ColumnHint> hints) {
            ParsedSchema schema = new ParsedSchema();
            schema.analyzeQuery(query, null, Map.of());
            hints.forEach(schema::applyHint);
            schema.guessUnqualified = true;
            schema.analyzeQuery(query, null, Map.of());
            schema.guessUnqualified = false;
            return schema;
        }

        private Set<TableKey> analyzeQuery(LineageQuery query, Scope outerScope, Map<String, Set<TableKey>> outerCtes) {
            Map<String, Set<TableKey>> visibleCtes = analyzeCtes(query.ctes(), outerScope, outerCtes);
            LinkedHashSet<TableKey> targets = new LinkedHashSet<>();
            for (LineageQueryBlock branch : query.branches()) {
                targets.addAll(analyzeBlock(branch, outerScope, visibleCtes));
            }
            return targets;
        }

        private Set<TableKey> analyzeBlock(LineageQueryBlock block, Scope outerScope, Map<String, Set<TableKey>> visibleCtes) {
            visibleCtes = analyzeCtes(block.ctes(), outerScope, visibleCtes);
            List<TableRef> tables = new ArrayList<>();
            List<RelationHint> relations = new ArrayList<>();
            List<MergeHint> mergeHints = new ArrayList<>();
            for (LineageRelation relation : block.relations()) {
                relations.add(analyzeRelation(relation, outerScope, visibleCtes, tables, mergeHints));
            }
            Scope scope = new Scope(tables, mergeHints,
                    relations.stream().anyMatch(RelationHint::virtual), outerScope);
            LinkedHashSet<TableKey> queryTargets = new LinkedHashSet<>();
            relations.forEach(relation -> queryTargets.addAll(relation.targets()));
            for (LineageSelectItem item : block.selectItems()) {
                if (item.wildcard()) {
                    for (RelationHint relation : relations) {
                        if (item.wildcardQualifier() == null || relation.hasQualifier(item.wildcardQualifier())) {
                            queryTargets.addAll(relation.targets());
                        }
                    }
                }
                for (LineageValue value : item.values()) {
                    analyzeValue(value, scope, visibleCtes);
                }
            }
            return queryTargets;
        }

        private Map<String, Set<TableKey>> analyzeCtes(List<LineageCte> ctes, Scope outerScope, Map<String, Set<TableKey>> outerCtes) {
            Map<String, Set<TableKey>> visibleCtes = new LinkedHashMap<>(outerCtes);
            ctes.forEach(cte -> visibleCtes.putIfAbsent(normalizeName(cte.name()), Set.of()));
            for (LineageCte cte : ctes) {
                Set<TableKey> previousTables = Set.copyOf(tableColumns.keySet());
                LinkedHashSet<TableKey> targets = new LinkedHashSet<>(
                        analyzeQuery(cte.query(), outerScope, visibleCtes));
                tableColumns.keySet().stream()
                    .filter(table -> !previousTables.contains(table))
                    .forEach(targets::add);
                if (cte.recursive() && cte.query().branches().size() > 1) {
                    cte.query().branches().subList(1, cte.query().branches().size()).stream()
                        .flatMap(branch -> branch.selectItems().stream())
                        .flatMap(item -> item.values().stream())
                        .filter(LineageColumnReference.class::isInstance)
                        .map(LineageColumnReference.class::cast)
                        .filter(reference -> reference.qualifier() == null)
                        .forEach(reference -> targets.forEach(target -> tableColumns
                            .computeIfAbsent(target, ignored -> new LinkedHashSet<>())
                            .add(reference.column())));
                }
                visibleCtes.put(normalizeName(cte.name()), Set.copyOf(targets));
                qualifierTargets.computeIfAbsent(normalizeName(cte.name()), ignored -> new LinkedHashSet<>())
                    .addAll(targets);
                registerVirtualColumns(cte.name(), cte.columnAliases(), cte.query());
            }
            return visibleCtes;
        }

        private RelationHint analyzeRelation(LineageRelation relation, Scope outerScope, Map<String, Set<TableKey>> visibleCtes, List<TableRef> tables,
                                             List<MergeHint> mergeHints) {
            if (relation instanceof LineageJoinRelation join) {
                RelationHint left = analyzeRelation(join.left(), outerScope, visibleCtes, tables, mergeHints);
                RelationHint right = analyzeRelation(join.right(), outerScope, visibleCtes, tables, mergeHints);
                LinkedHashSet<TableKey> targets = new LinkedHashSet<>(left.targets());
                targets.addAll(right.targets());
                if (join.natural() || !join.usingColumns().isEmpty()) {
                    mergeHints.add(new MergeHint(join.natural(), Set.copyOf(join.usingColumns()), targets));
                    for (String column : join.usingColumns()) {
                        targets.forEach(target -> tableColumns.computeIfAbsent(target, ignored -> new LinkedHashSet<>()).add(column));
                    }
                }
                return new RelationHint(null, null, targets, left.virtual() || right.virtual());
            }
            if (relation instanceof LineageNamedRelation named) {
                Set<TableKey> cteTargets = named.catalog() == null && named.schema() == null ? visibleCtes.get(normalizeName(named.name())) : null;
                if (cteTargets != null) {
                    RelationHint result = new RelationHint(named.alias(), named.name(), cteTargets, true);
                    if (named.alias() != null && !named.alias().isBlank()) {
                        virtualColumns.put(normalizeName(named.alias()),
                                virtualColumns.getOrDefault(normalizeName(named.name()), Set.of()));
                    }
                    registerQualifiers(result);
                    return result;
                }
                TableRef table = new TableRef(named);
                tables.add(table);
                tableColumns.computeIfAbsent(table.key(), ignored -> new LinkedHashSet<>());
                RelationHint result = new RelationHint(named.alias(), named.name(), Set.of(table.key()), false);
                registerQualifiers(result);
                return result;
            }
            if (relation instanceof LineageDerivedRelation derived) {
                Set<TableKey> targets = analyzeQuery(derived.query(), outerScope, visibleCtes);
                RelationHint result = new RelationHint(derived.alias(), null, targets, true);
                registerVirtualColumns(derived.alias(), derived.columnAliases(), derived.query());
                registerQualifiers(result);
                return result;
            }
            return new RelationHint(relation.alias(), null, Set.of(), true);
        }

        private void analyzeValue(LineageValue value, Scope scope, Map<String, Set<TableKey>> visibleCtes) {
            if (value instanceof LineageColumnReference reference) {
                assignColumn(reference.qualifier(), reference.column(), scope);
            } else if (value instanceof LineageSubqueryValue subquery) {
                analyzeQuery(subquery.query(), scope, visibleCtes);
            }
        }

        private void registerQualifiers(RelationHint relation) {
            relation.qualifiers().forEach(qualifier -> qualifierTargets.computeIfAbsent(qualifier, ignored -> new LinkedHashSet<>()).addAll(relation.targets()));
        }

        private void applyHint(ColumnHint hint) {
            if (hint.qualifier() == null) {
                return;
            }
            String qualifier = normalizeName(hint.qualifier());
            if (virtualColumns.getOrDefault(qualifier, Set.of()).stream()
                    .anyMatch(column -> column.equalsIgnoreCase(hint.column()))) {
                return;
            }
            Set<TableKey> targets = qualifierTargets.getOrDefault(qualifier, new LinkedHashSet<>());
            targets.forEach(target -> tableColumns.computeIfAbsent(target, ignored -> new LinkedHashSet<>()).add(hint.column()));
        }

        private void registerVirtualColumns(String qualifier, List<String> aliases,
                                            LineageQuery query) {
            if (qualifier == null || qualifier.isBlank()) {
                return;
            }
            LinkedHashSet<String> names = new LinkedHashSet<>();
            if (aliases != null && !aliases.isEmpty()) {
                names.addAll(aliases);
            } else if (!query.branches().isEmpty()) {
                query.branches().get(0).selectItems().stream()
                    .filter(item -> !item.wildcard())
                    .map(LineageSelectItem::outputName)
                    .filter(Objects::nonNull)
                    .forEach(names::add);
            }
            virtualColumns.put(normalizeName(qualifier), Set.copyOf(names));
        }

        private boolean assignColumn(String qualifier, String column, Scope scope) {
            if (scope == null || column == null) {
                return false;
            }
            if (qualifier != null && !qualifier.isBlank()) {
                for (TableRef table : scope.tables()) {
                    if (table.hasQualifier(qualifier)) {
                        tableColumns.computeIfAbsent(table.key(), ignored -> new LinkedHashSet<>()).add(column);
                        return true;
                    }
                }
                return assignColumn(qualifier, column, scope.outer());
            }
            for (MergeHint mergeHint : scope.mergeHints()) {
                if (mergeHint.natural() || mergeHint.columns().stream().anyMatch(column::equalsIgnoreCase)) {
                    mergeHint.targets().forEach(target -> tableColumns.computeIfAbsent(target, ignored -> new LinkedHashSet<>()).add(column));
                    return true;
                }
            }
            if (scope.tables().size() == 1 && !scope.hasVirtualRelation()) {
                tableColumns.computeIfAbsent(scope.tables().get(0).key(), ignored -> new LinkedHashSet<>()).add(column);
                return true;
            }

            List<TableRef> knownPhysicals = scope.tables().stream().filter(table -> tableColumns.getOrDefault(table.key(), new LinkedHashSet<>()).contains(column)).toList();
            if (knownPhysicals.size() == 1) {
                tableColumns.get(knownPhysicals.get(0).key()).add(column);
                return true;
            }
            if (guessUnqualified && !scope.hasVirtualRelation() && !scope.tables().isEmpty()
                    && scope.tables().stream().map(TableRef::key).distinct().count() == scope.tables().size()) {
                tableColumns.computeIfAbsent(scope.tables().get(0).key(), ignored -> new LinkedHashSet<>()).add(column);
                return true;
            }

            return assignColumn(null, column, scope.outer());
        }

        LinkedHashSet<String> columns(Map<UmiTypes, Object> levels, String tableName) {
            String catalog = normalizeName(Objects.toString(levels.get(UmiTypes.Catalog), null));
            String schema = normalizeName(Objects.toString(levels.get(UmiTypes.Schema), null));
            String table = normalizeName(tableName);
            LinkedHashSet<String> columns = tableColumns.entrySet()
                .stream()
                .filter(entry -> entry.getKey().matches(catalog, schema, table))
                .map(Map.Entry::getValue)
                .findFirst()
                .map(LinkedHashSet::new)
                .orElseGet(LinkedHashSet::new);
            int maxSyntheticColumn = columns.stream()
                .mapToInt(ParsedSchema::syntheticColumnIndex)
                .max()
                .orElse(0);
            if (maxSyntheticColumn > 0) {
                LinkedHashSet<String> expanded = new LinkedHashSet<>();
                for (int index = 1; index <= maxSyntheticColumn; index++) {
                    expanded.add("column" + index);
                }
                columns.stream().filter(column -> syntheticColumnIndex(column) == 0).forEach(expanded::add);
                return expanded;
            }
            return columns;
        }

        private static int syntheticColumnIndex(String column) {
            if (column == null || !column.toLowerCase(Locale.ROOT).startsWith("column")) {
                return 0;
            }
            try {
                return Integer.parseInt(column.substring("column".length()));
            } catch (NumberFormatException ignored) {
                return 0;
            }
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

        private record Scope(List<TableRef> tables, List<MergeHint> mergeHints,
                             boolean hasVirtualRelation, Scope outer) {
        }

        private record MergeHint(boolean natural, Set<String> columns, Set<TableKey> targets) {

            MergeHint{
                columns = Set.copyOf(columns);
                targets = Set.copyOf(targets);
            }
        }

        private record TableRef(LineageNamedRelation relation) {

            TableKey key() {
                return new TableKey(normalizeName(relation.catalog()), normalizeName(relation.schema()), normalizeName(relation.name()));
            }

            boolean hasQualifier(String qualifier) {
                String normalized = normalizeName(qualifier);
                return normalized.equals(key().table()) || normalized.equals(normalizeName(relation.alias()));
            }
        }

        private record RelationHint(String alias, String name, Set<TableKey> targets,
                                    boolean virtual) {

            RelationHint{
                targets = Set.copyOf(targets);
            }

            Set<String> qualifiers() {
                LinkedHashSet<String> result = new LinkedHashSet<>();
                if (alias != null && !alias.isBlank()) {
                    result.add(normalizeName(alias));
                }
                if (name != null && !name.isBlank()) {
                    result.add(normalizeName(name));
                }
                return result;
            }

            boolean hasQualifier(String qualifier) {
                return qualifiers().contains(normalizeName(qualifier));
            }
        }

        private record TableKey(String catalog, String schema, String table) {

            boolean matches(String requestedCatalog, String requestedSchema, String requestedTable) {
                return Objects.equals(table, requestedTable) && (catalog.isEmpty() || requestedCatalog.isEmpty() || Objects.equals(catalog, requestedCatalog))
                       && (schema.isEmpty() || requestedSchema.isEmpty() || Objects.equals(schema, requestedSchema));
            }
        }
    }

    private record ColumnHint(String qualifier, String column) {
    }

    /** Runs production-shape extraction and test-only metadata hints on one parse tree. */
    private static final class CorpusCstVisitor extends MySqlParserBaseVisitor<Void> {

        private final MyLineageCstVisitor lineageVisitor;
        private final ColumnHintVisitor   hintVisitor = new ColumnHintVisitor();

        CorpusCstVisitor(org.antlr.v4.runtime.Parser parser){
            this.lineageVisitor = new MyLineageCstVisitor(parser);
        }

        @Override
        public Void visit(ParseTree tree) {
            lineageVisitor.visit(tree);
            hintVisitor.visit(tree);
            return null;
        }

        LineageQuery query() {
            return lineageVisitor.query();
        }

        List<ColumnHint> hints() {
            return hintVisitor.hints();
        }
    }

    /**
     * Test-only metadata inference may inspect all clauses. Its output is used only
     * to synthesize a table schema for the version corpus and never participates in
     * production lineage resolution.
     */
    private static final class ColumnHintVisitor extends MySqlParserBaseVisitor<Void> {

        private final List<ColumnHint> hints = new ArrayList<>();

        List<ColumnHint> hints() {
            return List.copyOf(hints);
        }

        @Override
        public Void visitFullColumnName(FullColumnNameContext context) {
            List<String> parts = Arrays.stream(context.getText().split("\\.")).map(ParsedSchema::normalizeName).filter(part -> !part.isBlank()).toList();
            if (!parts.isEmpty()) {
                String column = parts.get(parts.size() - 1);
                String qualifier = parts.size() < 2 ? null : parts.get(parts.size() - 2);
                hints.add(new ColumnHint(qualifier, column));
            }
            return null;
        }
    }
}
