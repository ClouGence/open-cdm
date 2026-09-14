package com.clougence.sql.mysql;

import static org.junit.jupiter.api.Assertions.*;

import java.io.StringReader;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.dslpaser.antlr.AntlerSyntaxException;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.mysql.parser.MyDslProvider;
import com.clougence.sql.mysql.parser.MySqlVersion;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

class MariaDBDalTest {

    private static final Map<UmiTypes, Object> LEVELS = Map.of(UmiTypes.Instance, "1", UmiTypes.Schema, "schema1");

    @TestFactory
    Stream<DynamicTest> officialDalCommandsMatchVersionAndSqlMode() throws Exception {
        JsonNode cases;
        try (var input = getClass().getResourceAsStream("/mariadb_dal_cases.json")) {
            cases = new ObjectMapper().readTree(input);
        }
        Stream.Builder<DynamicTest> tests = Stream.builder();
        for (JsonNode test : cases) {
            for (String version : List.of("10.11.14", "11.4.8", "11.8.3", "12.3.0")) {
                boolean supported = MySqlVersion.parseExactVersion(version) >= MySqlVersion.parseExactVersion(test.get("min").asText());
                String sql = test.get("sql").asText();
                tests.add(DynamicTest
                    .dynamicTest(testName("MariaDB", version, "unknown", supported ? "accepted" : "rejected", sql), () -> verifyDalCommand(test, version, supported, false)));
                tests.add(DynamicTest
                    .dynamicTest(testName("MariaDB", version, "known", supported ? "accepted" : "rejected", sql), () -> verifyDalCommand(test, version, supported, true)));
            }
        }
        return tests.build();
    }

    private static void verifyDalCommand(JsonNode test, String version, boolean supported, boolean knownMode) {
        MySqlEngineSpi engine = new MySqlEngineSpi(null);
        SqlParserParameters parameters = new SqlParserParameters(Map.of(SqlParserParameters.VERSION, version + "-MariaDB"));
        if (knownMode) {
            parameters = parameters.put(SqlParserParameters.SQL_MODE, "");
        }
        String sql = test.get("sql").asText();
        if (!supported) {
            var rejectedParameters = parameters;
            assertThrows(AntlerSyntaxException.class, () -> {
                try (var stream = engine.splitAnalysisSpi(rejectedParameters).splitScriptStream(new StringReader(sql), List.of(), 0, 0)) {
                    stream.toList();
                }
            });
            return;
        }
        Set<SplitQueryType> expected = new HashSet<>();
        test.get("types").forEach(type -> expected.add(SplitQueryType.valueOf(type.asText())));
        try (var stream = engine.splitAnalysisSpi(parameters).splitScriptStream(new StringReader(sql), List.of(), 0, 0)) {
            var scripts = stream.toList();
            assertEquals(1, scripts.size());
            assertEquals(expected, scripts.get(0).getType());
            assertEquals(sql, scripts.get(0).getScript());
        }
        try (var stream = engine.behaviorAnalysisSpi(parameters).analysisBehaviorStream(new StringReader(sql), LEVELS, 0, 0)) {
            var behaviors = stream.toList();
            assertEquals(1, behaviors.size());
            assertEquals(SplitQueryType.valueOf(test.get("behavior").asText()), behaviors.get(0).getStatementType());
            assertFalse(behaviors.get(0).getRelations().isEmpty());
            var actions = behaviors.get(0).getRelations().stream().map(relation -> relation.getAction()).toList();
            if (expected.contains(SplitQueryType.UNSAFE)) {
                assertTrue(actions.contains(BehaviorAction.UNSAFE), "missing unsafe action");
            }
            if (sql.startsWith("KILL ")) {
                assertTrue(actions.contains(BehaviorAction.TERMINATE));
            }
            if (sql.startsWith("START ")) {
                assertTrue(actions.contains(BehaviorAction.START));
            }
            if (sql.startsWith("STOP ")) {
                assertTrue(actions.contains(BehaviorAction.STOP));
            }
            if (sql.startsWith("BACKUP ")) {
                BehaviorAction action = BehaviorAction.LOCK;
                if (sql.equals("BACKUP UNLOCK;") || sql.equals("BACKUP STAGE END;")) {
                    action = BehaviorAction.UNLOCK;
                }
                assertTrue(actions.contains(action));
            }
            if (sql.startsWith("INSTALL SONAME") || sql.startsWith("UNINSTALL SONAME")) {
                assertFalse(actions.contains(BehaviorAction.UNSAFE));
                BehaviorAction action = BehaviorAction.CREATE;
                if (sql.startsWith("UNINSTALL")) {
                    action = BehaviorAction.DROP;
                }
                assertTrue(actions.contains(action));
            }
            if (sql.startsWith("FLUSH ")) {
                assertTrue(actions.contains(BehaviorAction.FLUSH));
            }
            if (sql.startsWith("SHOW CREATE SEQUENCE")) {
                assertEquals(TargetType.Sequence, behaviors.get(0).getRelations().get(0).getSubject().getObjectType());
            }
            if (sql.startsWith("SHOW CREATE SERVER")) {
                assertEquals(TargetType.ConfigKey, behaviors.get(0).getRelations().get(0).getSubject().getObjectType());
            }
            if (sql.contains("DELETE FROM")) {
                assertTrue(behaviors.get(0).getRelations().stream().anyMatch(relation -> relation.getAction() == BehaviorAction.DELETE), "missing DELETE action");
            }
            if (sql.contains("UPDATE schema1")) {
                assertTrue(behaviors.get(0).getRelations().stream().anyMatch(relation -> relation.getAction() == BehaviorAction.UPDATE), "missing UPDATE action");
            }
            if (sql.contains("SELECT * FROM schema1.orders")) {
                assertTrue(behaviors.get(0)
                    .getRelations()
                    .stream()
                    .anyMatch(relation -> relation.getAction() == BehaviorAction.READ && relation.getSubject().getObjectType() == TargetType.Table
                                          && relation.getSubject().getObjectName() != null
                                          && "orders".equals(relation.getSubject().getObjectName().getObjectName())), "missing table READ action");
            }
            if (sql.startsWith("SET STATEMENT") && sql.contains("max_statement_time")) {
                assertTrue(behaviors.get(0)
                    .getRelations()
                    .stream()
                    .anyMatch(relation -> relation.getAction() == BehaviorAction.CONFIGURE && relation.getSubject().getObjectType() == TargetType.ConfigKey
                                          && "/1/max_statement_time/".equals(relation.getSubject().getObjectPath())), "statement setting must be instance scoped");
            }
        }
    }

    @Test
    void engineCacheSeparatesMariaDBDialect() {
        MySqlEngineSpi engine = new MySqlEngineSpi(null);
        var parameters = new SqlParserParameters(Map.of(SqlParserParameters.VERSION, "8.0.36"));
        var mysql = (MyDslProvider) engine.dslProvider(parameters);
        var maria = (MyDslProvider) engine.dslProvider(parameters.put(SqlParserParameters.GRAMMAR_VERSION, "MariaDB"));
        assertNotSame(mysql, maria);
        assertFalse(mysql.config().isMariaDb());
        assertTrue(maria.config().isMariaDb());
        assertEquals(mysql.version(), maria.version());
        assertEquals(mysql.exactVersion(), maria.exactVersion());
        assertNotEquals(mysql.config(), maria.config());
        assertSame(mysql, engine.dslProvider(parameters));
        assertSame(maria, engine.dslProvider(parameters.put(SqlParserParameters.GRAMMAR_VERSION, "MariaDB")));
        var unknown = (MyDslProvider) engine.dslProvider(new SqlParserParameters(Map.of(SqlParserParameters.GRAMMAR_VERSION, "MariaDB")));
        assertTrue(unknown.config().isMariaDb());
        assertEquals(0, unknown.exactVersion());
        var unknownMysql = (MyDslProvider) engine.dslProvider(new SqlParserParameters(Map.of()));
        assertNotSame(unknownMysql, unknown);
        assertFalse(unknownMysql.config().isMariaDb());
    }

    @TestFactory
    Stream<DynamicTest> officialDalCommandsRespectVersionBoundaries() throws Exception {
        JsonNode cases = new ObjectMapper()
            .readTree("""
                    [
                      {"sql":"CHANGE MASTER TO MASTER_DEMOTE_TO_SLAVE=1;", "types":["ALTER_REPLICATION"], "behavior":"ALTER_REPLICATION", "before":"10.9.8", "since":"10.10.0"},
                      {"sql":"SHOW EXPLAIN FORMAT=JSON FOR 123;", "types":["PERFORMANCE"], "behavior":"PERFORMANCE", "before":"10.8.8", "since":"10.9.0", "legacy":"SHOW EXPLAIN FOR 123;"},
                      {"sql":"CHANGE MASTER TO MASTER_USE_GTID=REPLICA_POS;", "types":["ALTER_REPLICATION"], "behavior":"ALTER_REPLICATION", "before":"10.5.0", "since":"10.5.1", "legacy":"CHANGE MASTER TO MASTER_USE_GTID=SLAVE_POS;"},
                      {"sql":"UNINSTALL SONAME IF EXISTS 'ha_archive';", "types":["DROP_LIBRARY"], "behavior":"DROP_LIBRARY", "before":"10.3.39", "since":"10.4.0", "legacy":"UNINSTALL SONAME 'ha_archive';"},
                      {"sql":"INSTALL PLUGIN IF NOT EXISTS archive SONAME 'ha_archive';", "types":["CREATE_LIBRARY"], "behavior":"CREATE_LIBRARY", "before":"10.3.39", "since":"10.4.0", "legacy":"INSTALL PLUGIN archive SONAME 'ha_archive';"},
                      {"sql":"UNINSTALL PLUGIN IF EXISTS archive;", "types":["DROP_LIBRARY"], "behavior":"DROP_LIBRARY", "before":"10.3.39", "since":"10.4.0", "legacy":"UNINSTALL PLUGIN archive;"},
                      {"sql":"RESET QUERY CACHE, REPLICA ALL;", "types":["ADMIN_PERFORMANCE","ALTER_REPLICATION","UNSAFE"], "behavior":"ADMIN_PERFORMANCE", "before":"10.5.0", "since":"10.5.1"},
                      {"sql":"RESET QUERY CACHE, REPLICA 'c1' ALL;", "types":["ADMIN_PERFORMANCE","ALTER_REPLICATION","UNSAFE"], "behavior":"ADMIN_PERFORMANCE", "before":"10.5.0", "since":"10.5.1"},
                      {"sql":"CREATE TABLE t (v VECTOR(3));", "types":["CREATE_TABLE","ADD_COLUMN"], "behavior":"CREATE_TABLE", "before":"11.7.0", "since":"11.7.1"}
                    ]
                    """);
        Stream.Builder<DynamicTest> tests = Stream.builder();
        for (JsonNode test : cases) {
            for (boolean knownMode : List.of(false, true)) {
                String mode = knownMode ? "known" : "unknown";
                tests.add(DynamicTest.dynamicTest(testName("MariaDB", test.get("before").asText(), mode, "rejected-before-minimum", test.get("sql")
                    .asText()), () -> verifyDalCommand(test, test.get("before").asText(), false, knownMode)));
                tests.add(DynamicTest.dynamicTest(testName("MariaDB", test.get("since").asText(), mode, "accepted-at-minimum", test.get("sql")
                    .asText()), () -> verifyDalCommand(test, test.get("since").asText(), true, knownMode)));
                if (test.has("legacy")) {
                    var legacy = (com.fasterxml.jackson.databind.node.ObjectNode) test.deepCopy();
                    legacy.put("sql", test.get("legacy").asText());
                    tests.add(DynamicTest.dynamicTest(testName("MariaDB", test.get("before").asText(), mode, "accepted-legacy", legacy.get("sql")
                        .asText()), () -> verifyDalCommand(legacy, test.get("before").asText(), true, knownMode)));
                }
            }
        }
        return tests.build();
    }

    @TestFactory
    Stream<DynamicTest> mixedFlushCommandsRetainEveryTarget() {
        return List
            .of("FLUSH SSL, BINARY LOGS", "FLUSH BINARY LOGS, SSL", "FLUSH USER_VARIABLES, BINARY LOGS", "FLUSH BINARY LOGS, USER_VARIABLES", "FLUSH SSL, BINARY LOGS, USER_STATISTICS", "FLUSH USER_STATISTICS, BINARY LOGS, SSL")
            .stream()
            .map(sql -> DynamicTest.dynamicTest(testName("MariaDB", "11.4.8", "default", "all-targets-retained", sql), () -> {
                var engine = new MySqlEngineSpi(null);
                var parameters = new SqlParserParameters(Map.of(SqlParserParameters.VERSION, "11.4.8-MariaDB"));
                try (var stream = engine.behaviorAnalysisSpi(parameters).analysisBehaviorStream(new StringReader(sql), LEVELS, 0, 0)) {
                    var behaviors = stream.toList();
                    assertEquals(1, behaviors.size());
                    var relations = behaviors.get(0).getRelations();
                    Set<TargetType> expected = new HashSet<>(Set.of(TargetType.ConfigKey, TargetType.Log));
                    if (sql.contains("USER_STATISTICS")) {
                        expected.add(TargetType.Instance);
                    }
                    assertEquals(expected, new HashSet<>(relations.stream().map(relation -> relation.getSubject().getObjectType()).toList()));
                    assertEquals(expected.size(), relations.size());
                    assertTrue(relations.stream().allMatch(relation -> relation.getAction() == BehaviorAction.FLUSH));
                }
            }));
    }

    @TestFactory
    Stream<DynamicTest> multiOptionResetCommandsRetainEveryTarget() {
        return List.of("RESET SLAVE 'c1' ALL, QUERY CACHE", "RESET QUERY CACHE, REPLICA 'c1' ALL", "RESET MASTER, QUERY CACHE", "RESET QUERY CACHE, MASTER")
            .stream()
            .map(sql -> DynamicTest.dynamicTest(testName("MariaDB", "11.4.8", "default", "all-targets-retained", sql), () -> {
                var engine = new MySqlEngineSpi(null);
                var parameters = new SqlParserParameters(Map.of(SqlParserParameters.VERSION, "11.4.8-MariaDB"));
                try (var stream = engine.behaviorAnalysisSpi(parameters).analysisBehaviorStream(new StringReader(sql), LEVELS, 0, 0)) {
                    var relations = stream.toList().get(0).getRelations();
                    TargetType resetTarget = sql.contains("MASTER") ? TargetType.Log : TargetType.Replication;
                    Set<TargetType> expected = Set.of(resetTarget, TargetType.Instance);
                    assertEquals(expected, new HashSet<>(relations.stream().map(relation -> relation.getSubject().getObjectType()).toList()));
                    assertEquals(expected.size(), relations.size());
                    assertTrue(relations.stream().allMatch(relation -> relation.getAction() == BehaviorAction.UNSAFE));
                }
            }));
    }

    @TestFactory
    Stream<DynamicTest> quotedAllReplicationConnectionsUseResetAction() {
        return List.of("RESET SLAVE 'ALL'", "RESET REPLICA 'ALL'")
            .stream()
            .map(sql -> DynamicTest.dynamicTest(testName("MariaDB", "11.4.8", "default", "quoted-all-is-connection-name", sql), () -> {
                var engine = new MySqlEngineSpi(null);
                var parameters = new SqlParserParameters(Map.of(SqlParserParameters.VERSION, "11.4.8-MariaDB"));
                try (var stream = engine.behaviorAnalysisSpi(parameters).analysisBehaviorStream(new StringReader(sql), LEVELS, 0, 0)) {
                    var relations = stream.toList().get(0).getRelations();
                    assertEquals(1, relations.size());
                    assertEquals(TargetType.Replication, relations.get(0).getSubject().getObjectType());
                    assertEquals(BehaviorAction.RESET, relations.get(0).getAction());
                }
            }));
    }

    @TestFactory
    Stream<DynamicTest> libraryLifecycleCommandsDoNotAddUnsafeAction() {
        return List
            .of("INSTALL SONAME 'ha_archive'", "UNINSTALL SONAME IF EXISTS 'ha_archive'", "INSTALL PLUGIN archive SONAME 'ha_archive'", "INSTALL PLUGIN IF NOT EXISTS archive SONAME 'ha_archive'", "UNINSTALL PLUGIN archive", "UNINSTALL PLUGIN IF EXISTS archive", "SET STATEMENT sql_log_bin=0 FOR INSTALL SONAME 'ha_archive'")
            .stream()
            .map(sql -> DynamicTest.dynamicTest(testName("MariaDB", "11.4.8", "default", "library-action-without-unsafe", sql), () -> {
                var engine = new MySqlEngineSpi(null);
                var parameters = new SqlParserParameters(Map.of(SqlParserParameters.VERSION, "11.4.8-MariaDB"));
                try (var stream = engine.behaviorAnalysisSpi(parameters).analysisBehaviorStream(new StringReader(sql), LEVELS, 0, 0)) {
                    var relations = stream.toList().get(0).getRelations();
                    assertFalse(relations.stream().anyMatch(relation -> relation.getAction() == BehaviorAction.UNSAFE));
                    BehaviorAction action = BehaviorAction.CREATE;
                    if (sql.startsWith("UNINSTALL")) {
                        action = BehaviorAction.DROP;
                    }
                    var libraryAction = action;
                    assertTrue(relations.stream().anyMatch(relation -> relation.getSubject().getObjectType() == TargetType.Library && relation.getAction() == libraryAction));
                }
            }));
    }

    @TestFactory
    Stream<DynamicTest> incompatibleAndMalformedCommandsAreRejected() {
        Stream.Builder<DynamicTest> tests = Stream.builder();
        for (String version : List.of("5.7.44", "8.0.36", "8.4.10", "9.7.0")) {
            for (String sql : List
                .of("BACKUP STAGE START", "SET STATEMENT max_statement_time=1 FOR SELECT 1", "KILL SOFT QUERY 1", "KILL QUERY ID 1", "KILL USER 'alice'", "SHOW CREATE SEQUENCE s", "SHOW ALL SLAVES STATUS", "SHOW ANALYZE FOR 1", "ANALYZE SELECT 1", "ANALYZE TABLE t PERSISTENT FOR ALL", "INSTALL SONAME 'a'", "UNINSTALL SONAME IF EXISTS 'a'", "INSTALL PLUGIN IF NOT EXISTS p SONAME 'p.so'", "UNINSTALL PLUGIN IF EXISTS p", "CHANGE MASTER TO MASTER_USE_GTID=REPLICA_POS", "FLUSH SSL", "REPAIR TABLE t FORCE", "RESET SLAVE 'c1' ALL, QUERY CACHE", "RESET QUERY CACHE, REPLICA 'c1' ALL")) {
                tests.add(rejected("MySQL", version, sql));
            }
        }
        for (String sql : List
            .of("BACKUP STAGE INVALID", "SET STATEMENT max_statement_time=1 FOR", "KILL SOFT QUERY ID", "SHOW NONEXISTENT", "CHANGE MASTER TO MASTER_USE_GTID=INVALID", "SHOW PLUGINS SONAME", "ANALYZE TABLE t PERSISTENT FOR COLUMNS (id)", "SHOW BINARY LOG STATUS", "RESET BINARY LOGS AND GTIDS", "CLONE LOCAL DATA DIRECTORY='/tmp/data'", "INSTALL COMPONENT 'x'", "CHANGE REPLICATION SOURCE TO SOURCE_HOST='db'", "RESTART")) {
            tests.add(rejected("MariaDB", "11.8.3-MariaDB", sql));
        }
        tests.add(rejected("MariaDB", "10.8.0-MariaDB", "SHOW ANALYZE FOR 1"));
        tests.add(rejected("MariaDB", "10.3.0-MariaDB", "BACKUP STAGE START"));
        tests.add(rejected("MariaDB", "10.4.3-MariaDB", "SHUTDOWN WAIT FOR ALL SLAVES"));
        tests.add(rejected("MariaDB", "10.6.0-MariaDB", "SHOW SLAVE STATUS FOR CHANNEL 'c'"));
        tests.add(rejected("MariaDB", "10.11.14-MariaDB", "START SLAVE UNTIL SQL_BEFORE_GTIDS='0-1-100'"));
        tests.add(rejected("MariaDB", "11.8.3-MariaDB", "CHANGE MASTER TO MASTER_AUTO_POSITION=1"));
        tests.add(rejected("MariaDB", "11.8.3-MariaDB", "CHANGE MASTER TO MASTER_BIND='eth0'"));
        tests.add(rejected("MariaDB", "11.8.3-MariaDB", "SHOW REPLICAS"));
        tests.add(rejected("MariaDB", "11.8.3-MariaDB", "FLUSH OPTIMIZER_COSTS"));
        tests.add(rejected("MariaDB", "11.8.3-MariaDB", "START GROUP_REPLICATION"));
        tests.add(rejected("MariaDB", "11.8.3-MariaDB", "STOP GROUP_REPLICATION"));
        return tests.build();
    }

    @TestFactory
    Stream<DynamicTest> mysqlDalCommandsRemainCompatible() {
        Stream.Builder<DynamicTest> tests = Stream.builder();
        for (String version : List.of("5.6.51", "5.7.44", "8.0.46", "8.4.10", "9.7.1")) {
            for (String sql : List
                .of("SHOW DATABASES", "SHOW FULL TABLES", "SHOW COLUMNS FROM t", "SHOW GLOBAL STATUS", "SHOW VARIABLES", "SHOW BINARY LOGS", "SHOW BINLOG EVENTS", "SHOW PLUGINS", "SHOW PROCESSLIST", "SET SESSION autocommit=1", "KILL QUERY 123", "FLUSH PRIVILEGES", "FLUSH STATUS", "FLUSH BINARY LOGS", "FLUSH TABLES t WITH READ LOCK", "ANALYZE TABLE t", "CHECK TABLE t", "OPTIMIZE TABLE t", "REPAIR TABLE t QUICK", "INSTALL PLUGIN p SONAME 'p.so'", "UNINSTALL PLUGIN p", "CACHE INDEX t IN cache1", "LOAD INDEX INTO CACHE t")) {
                tests.add(DynamicTest.dynamicTest(testName("MySQL", version, "default", "accepted-compatible", sql), () -> {
                    var engine = new MySqlEngineSpi(null);
                    var parameters = new SqlParserParameters(Map.of(SqlParserParameters.VERSION, version));
                    try (var stream = engine.splitAnalysisSpi(parameters).splitScriptStream(new StringReader(sql), List.of(), 0, 0)) {
                        assertFalse(stream.toList().get(0).getType().contains(SplitQueryType.UNKNOWN));
                    }
                    try (var stream = engine.behaviorAnalysisSpi(parameters).analysisBehaviorStream(new StringReader(sql), LEVELS, 0, 0)) {
                        var result = stream.toList().get(0);
                        assertNotEquals(SplitQueryType.UNKNOWN, result.getStatementType());
                        assertFalse(result.getRelations().isEmpty());
                    }
                }));
            }
        }
        return tests.build();
    }

    @TestFactory
    Stream<DynamicTest> sharedMariaDBGrammarRemainsSupported() {
        return List.of("SELECT 1 INTERSECT SELECT 1", "SELECT 1 EXCEPT SELECT 2", "SELECT JSON_VALUE('{\"a\":1}', '$.a')", "CREATE PROCEDURE IF NOT EXISTS p() SELECT 1")
            .stream()
            .map(sql -> DynamicTest.dynamicTest(testName("MariaDB", "11.4.8", "default", "accepted-shared-grammar", sql), () -> {
                var engine = new MySqlEngineSpi(null);
                var parameters = new SqlParserParameters(Map.of(SqlParserParameters.VERSION, "11.4.8-MariaDB"));
                try (var stream = engine.splitAnalysisSpi(parameters).splitScriptStream(new StringReader(sql), List.of(), 0, 0)) {
                    assertFalse(stream.toList().get(0).getType().contains(SplitQueryType.UNKNOWN));
                }
            }));
    }

    private static DynamicTest rejected(String dialect, String version, String sql) {
        return DynamicTest.dynamicTest(testName(dialect, version, "default", "rejected", sql), () -> {
            MySqlEngineSpi engine = new MySqlEngineSpi(null);
            var parameters = new SqlParserParameters(Map.of(SqlParserParameters.VERSION, version));
            assertThrows(AntlerSyntaxException.class, () -> {
                try (var stream = engine.splitAnalysisSpi(parameters).splitScriptStream(new StringReader(sql), List.of(), 0, 0)) {
                    stream.toList();
                }
            });
        });
    }

    private static String testName(String dialect, String version, String sqlMode, String expected, String sql) {
        return "[dialect=" + dialect + "][version=" + version + "][sqlMode=" + sqlMode + "][expected=" + expected + "] " + sql;
    }
}
