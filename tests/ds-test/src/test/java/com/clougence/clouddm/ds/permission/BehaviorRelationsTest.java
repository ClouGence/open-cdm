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
package com.clougence.clouddm.ds.permission;

import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import com.clougence.clouddm.console.web.component.analysis.BehaviorRelations;
import com.clougence.clouddm.console.web.component.analysis.BehaviorRequest;
import com.clougence.clouddm.ds.SqlTestSupport;
import com.clougence.clouddm.ds.ads.sql.ads4my.AdsMySysObjectRegistrySpi;
import com.clougence.clouddm.ds.ads.sql.ads4my.parser.AdsMyVersion;
import com.clougence.clouddm.ds.clickhouse.sql.ChSysObjectRegistrySpi;
import com.clougence.clouddm.ds.clickhouse.sql.parser.ClickHouseVersion;
import com.clougence.clouddm.ds.cloudberry.sql.analysis.sysobj.CbSysObjectRegistrySpi;
import com.clougence.clouddm.ds.cloudberry.sql.parser.CloudberryVersion;
import com.clougence.clouddm.ds.cockroachdb.sql.analysis.CrdbSysObjectRegistrySpi;
import com.clougence.clouddm.ds.cockroachdb.sql.analysis.reference.CrdbResourceRegistry;
import com.clougence.clouddm.ds.cockroachdb.sql.parser.CockroachDbVersion;
import com.clougence.clouddm.ds.dameng.sql.analysis.sysobj.DmSysObjectRegistrySpi;
import com.clougence.clouddm.ds.gauss.sql.GaussSysObjectRegistrySpi;
import com.clougence.clouddm.ds.gauss.sql.parser.GaussVersion;
import com.clougence.clouddm.ds.greenplum.sql.analysis.sysobj.GpSysObjectRegistrySpi;
import com.clougence.clouddm.ds.greenplum.sql.parser.GreenplumVersion;
import com.clougence.clouddm.ds.hologres.sql.analysis.sysobj.HgSysObjectRegistrySpi;
import com.clougence.clouddm.ds.hologres.sql.parser.HologresVersion;
import com.clougence.clouddm.ds.oceanbase.sql.ob4my.ObMySysObjectRegistrySpi;
import com.clougence.clouddm.ds.oceanbase.sql.ob4my.analysis.reference.ObMyResourceRegistry;
import com.clougence.clouddm.ds.oceanbase.sql.ob4ora.analysis.reference.ObOraResourceRegistry;
import com.clougence.clouddm.ds.oceanbase.sql.ob4ora.analysis.sysobj.ObOraSysObjectRegistrySpi;
import com.clougence.clouddm.ds.oceanbase.sql.parser.OceanBaseVersion;
import com.clougence.clouddm.ds.starrocks.sql.SrSysObjectRegistrySpi;
import com.clougence.clouddm.ds.starrocks.sql.parser.StarRocksVersion;
import com.clougence.clouddm.ds.tidb.sql.analysis.sysobj.TiSysObjectRegistrySpi;
import com.clougence.clouddm.ds.yashandb.sql.YashanDBOracleSysObjectRegistrySpi;
import com.clougence.clouddm.ds.yashandb.sql.parser.YashanDbVersion;
import com.clougence.clouddm.sdk.security.auth.SecDataAuthKind;
import com.clougence.clouddm.sdk.service.secrules.RuleQueryType;
import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.analysis.sysobj.SysObjectRegistrySpi;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.registry.*;
import com.clougence.sql.db2.Db2SysObjectRegistrySpi;
import com.clougence.sql.db2.parser.Db2Version;
import com.clougence.sql.doris.analysis.sysobj.DrSysObjectRegistrySpi;
import com.clougence.sql.doris.parser.DorisVersion;
import com.clougence.sql.hana.analysis.sysobj.HanaSysObjectRegistrySpi;
import com.clougence.sql.hana.parser.HanaVersion;
import com.clougence.sql.mysql.analysis.sysobj.MySysObjectRegistrySpi;
import com.clougence.sql.mysql.parser.MariaDbVersion;
import com.clougence.sql.oracle.analysis.sysobj.OraSysObjectRegistrySpi;
import com.clougence.sql.oracle.parser.OracleVersion;
import com.clougence.sql.polardbx.PorXSysObjectRegistrySpi;
import com.clougence.sql.polardbx.parser.PolarDbXVersion;
import com.clougence.sql.postgres.analysis.reference.PgResourceRegistry;
import com.clougence.sql.postgres.analysis.sysobj.PgSysObjectRegistrySpi;
import com.clougence.sql.sqlserver.analysis.sysobj.MsSysObjectRegistrySpi;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class BehaviorRelationsTest {

    private static final String FUNCTION_RESOURCES_ROOT                      = "/META-INF/clougence/";
    private static final String GREENPLUM_BUILT_IN_FUNCTIONS_RESOURCE        = "/META-INF/clougence/greenplum-built-in-functions.json";
    private static final String CLOUDBERRY_BUILT_IN_FUNCTIONS_RESOURCE       = "/META-INF/clougence/cloudberry-built-in-functions.json";
    private static final String OCEANBASE_ORACLE_BUILT_IN_FUNCTIONS_RESOURCE = "/META-INF/clougence/oceanbase-oracle-built-in-functions.json";

    @Test
    public void analysisResourceGroupsMergeWithoutLosingPermissionFacts() {
        VersionedResourceRegistry<FunctionBehavior> registry = new VersionedResourceRegistry<>(String::toLowerCase);
        VersionedFunctionCatalogLoader.loadAnalysisResources(PgResourceRegistry.class, "/META-INF/clougence/postgres-analysis-resources.json", registry, Integer::parseInt);
        FunctionBehavior file = registry.find(RegisteredResourceType.FUNCTION, 14, "pg_read_file").orElseThrow();
        Assert.assertEquals(BehaviorAction.CALL, file.action());
        Assert.assertEquals(TargetType.File, file.targetType());
        Assert.assertEquals(BehaviorAction.READ, file.targetAction());
        Assert.assertTrue(file.unsafe());
        Assert.assertTrue(file.privileged());
        Assert.assertTrue(file.statementTypes().containsAll(List.of(SplitQueryType.DATA_IMPORT, SplitQueryType.UNSAFE)));
        FunctionBehavior metadata = registry.find(RegisteredResourceType.FUNCTION, 14, "pg_database_size").orElseThrow();
        Assert.assertTrue(metadata.metadata());
        Assert.assertTrue(metadata.privileged());
        Assert.assertTrue(metadata.statementTypes().contains(SplitQueryType.METADATA));
    }

    @Test
    public void functionAndSessionLocksRetainAuthorization() {
        for (BehaviorAction action : List.of(BehaviorAction.LOCK, BehaviorAction.UNLOCK)) {
            Assert.assertEquals(SecDataAuthKind.MANAGE, flatten(action, TargetType.Session).authKind());
            Assert.assertEquals(SecDataAuthKind.MANAGE, flatten(action, TargetType.Instance).authKind());
            Assert.assertEquals(SecDataAuthKind.PROGRAM, flatten(action, TargetType.Function).authKind());
            Assert.assertNull(flatten(action, TargetType.Table).authKind());
            Assert.assertNull(flatten(action, TargetType.Transaction).authKind());
        }
    }

    @TestFactory
    public Stream<DynamicTest> quotedNativeAdministrativeCallsKeepSafetyPermissions() {
        Map<String, List<String>> cases = new LinkedHashMap<>();
        cases.put("postgres", List.of("\"pg_read_file\"('/tmp/classification')", "UNSAFE"));
        cases.put("greenplum", List.of("\"gp_truncate_error_log\"('t')", "UNSAFE"));
        cases.put("cloudberry", List.of("\"gp_truncate_error_log\"('t')", "UNSAFE"));
        cases.put("cockroachdb", List.of("crdb_internal.\"force_retry\"('1s'::INTERVAL)", "UNSAFE"));
        cases.put("hologres", List.of("\"apply_privileges\"('a','b')", "MANAGE"));
        cases.put("oracle", List.of("\"DBMS_LOB\".\"FILEEXISTS\"(NULL)", "UNSAFE"));
        cases.put("yashandb_oracle", List.of("\"UTL_HTTP\".\"REQUEST\"('http://example.invalid')", "UNSAFE"));
        cases.put("ob4ora", List.of("\"DBMS_LOCK\".\"REQUEST\"(1)", "MANAGE"));
        cases.put("db2", List.of("SELECT demo_table.* FROM demo_table, TABLE(\"SYSPROC\".\"MON_GET_TABLE\"(NULL,NULL,-1)) AS codex_monitor", "READ"));
        cases.put("gauss", List.of("\"pg_catalog\".\"set_config\"('statement_timeout','1s',false)", "MANAGE"));
        return cases.entrySet().stream().map(entry -> DynamicTest.dynamicTest(entry.getKey(), () -> {
            List<BehaviorRequest> requests = functionRequests(entry.getKey(), entry.getValue().get(0));
            Assert.assertTrue(requests.toString(), requests.stream().anyMatch(request -> request.authKind() == SecDataAuthKind.valueOf(entry.getValue().get(1))));
        }));
    }

    @TestFactory
    public Stream<DynamicTest> planningDoesNotExecuteFunctionEffects() {
        Map<String, String> queries = new LinkedHashMap<>();
        queries.put("mysql", "EXPLAIN SELECT GET_LOCK('x',0) FROM demo_table");
        queries.put("mariadb", "EXPLAIN SELECT GET_LOCK('x',0) FROM demo_table");
        queries.put("tidb", "EXPLAIN SELECT GET_LOCK('x',0) FROM demo_table");
        queries.put("por4x", "EXPLAIN SELECT GET_LOCK('x',0) FROM demo_table");
        queries.put("ob4my", "EXPLAIN SELECT GET_LOCK('x',0) FROM demo_table");
        queries.put("postgres", "EXPLAIN SELECT set_config('statement_timeout','1s',false) FROM demo_table");
        queries.put("greenplum", "EXPLAIN SELECT set_config('statement_timeout','1s',false) FROM demo_table");
        queries.put("gauss", "EXPLAIN SELECT set_config('statement_timeout','1s',false) FROM demo_table");
        queries.put("dameng", "EXPLAIN SELECT CHECKPOINT() FROM demo_table");
        queries.put("oracle", "EXPLAIN PLAN FOR SELECT UTL_HTTP.REQUEST('http://localhost') FROM demo_table");
        queries.put("yashandb_oracle", "EXPLAIN PLAN FOR SELECT UTL_HTTP.REQUEST('http://localhost') FROM demo_table");
        return queries.entrySet().stream().map(entry -> DynamicTest.dynamicTest(entry.getKey(), () -> {
            var engine = SqlTestSupport.sqlEngine(entry.getKey());
            try (var stream = engine.behaviorAnalysisSpi(SqlTestSupport.parserParameters(entry.getKey()))
                .analysisBehaviorStream(new StringReader(entry.getValue()), Map.of(UmiTypes.Instance, "test/1", UmiTypes.Catalog, "testdb", UmiTypes.Schema, "public"), 1, 0)) {
                List<BehaviorRequest> requests = stream.flatMap(statement -> BehaviorRelations.flattenResource(null, null, statement.getRelations()).stream()).toList();
                Assert.assertFalse(requests.toString(), requests.stream()
                    .filter(request -> !(request.action() == BehaviorAction.INSERT && request.resource().getObjectType() == TargetType.Table
                                         && request.resource().getObjectName() != null && "PLAN_TABLE".equals(request.resource().getObjectName().getObjectName())))
                    .anyMatch(request -> request.action() != BehaviorAction.CALL && request.action() != BehaviorAction.READ));
                Assert.assertFalse(requests.toString(), requests.stream()
                    .anyMatch(request -> request.resource().getObjectType() == TargetType.Function && request.action() == BehaviorAction.READ));
            }
        }));
    }

    @TestFactory
    public Stream<DynamicTest> viewDefinitionsDoNotExecuteFunctionEffects() {
        Map<String, String> queries = new LinkedHashMap<>();
        queries.put("mysql", "CREATE VIEW codex_view AS SELECT GET_LOCK('x',0)");
        queries.put("adb", "CREATE VIEW codex_view AS SELECT VERSION()");
        queries.put("por4x", "CREATE VIEW codex_view AS SELECT GET_LOCK('x',0)");
        queries.put("ob4my", "CREATE VIEW codex_view AS SELECT GET_LOCK('x',0) FROM demo_table");
        queries.put("postgres", "CREATE MATERIALIZED VIEW codex_view AS SELECT set_config('statement_timeout','1s',false) WITH NO DATA");
        queries.put("por4pg", "CREATE VIEW codex_view AS SELECT set_config('statement_timeout','1s',false)");
        queries.put("greenplum", "CREATE VIEW codex_view AS SELECT gp_truncate_error_log('t')");
        queries.put("cloudberry", "CREATE VIEW codex_view AS SELECT gp_truncate_error_log('t')");
        queries.put("hologres", "CREATE VIEW codex_view AS SELECT apply_privileges('a','b')");
        queries.put("ob4ora", "CREATE VIEW codex_view AS SELECT DBMS_LOCK.REQUEST(1) FROM demo_table");
        return queries.entrySet().stream().map(entry -> DynamicTest.dynamicTest(entry.getKey(), () -> {
            var engine = SqlTestSupport.sqlEngine(entry.getKey());
            try (var stream = engine.behaviorAnalysisSpi(SqlTestSupport.parserParameters(entry.getKey()))
                .analysisBehaviorStream(new StringReader(entry.getValue()), Map.of(UmiTypes.Instance, "test/1", UmiTypes.Catalog, "testdb", UmiTypes.Schema, "public"), 1, 0)) {
                var relations = stream.flatMap(statement -> statement.getRelations().stream()).toList();
                Assert.assertTrue(relations.toString(), relations.stream().anyMatch(relation -> relation.getAction() == BehaviorAction.CALL));
                Assert.assertFalse(relations.toString(), relations.stream()
                    .anyMatch(relation -> relation.getAction() == BehaviorAction.CONFIGURE || relation.getAction() == BehaviorAction.LOCK
                                          || relation.getAction() == BehaviorAction.IMPORT || relation.getAction() == BehaviorAction.GRANT
                                          || relation.getAction() == BehaviorAction.PURGE || relation.getAction() == BehaviorAction.UNSAFE));
            }
        }));
    }

    @Test
    public void crossCatalogMetadataDoesNotAssumeTheCurrentCatalog() throws Exception {
        List<BehaviorRequest> requests = functionRequests("sqlserver", "DB_ID('other_database')");
        Assert.assertTrue(requests.toString(), requests.stream()
            .anyMatch(request -> request.resource().getObjectType() == TargetType.Catalog && request.action() == BehaviorAction.READ
                                 && "/test/1/".equals(request.resource().getObjectPath())));
    }

    @Test
    public void oceanBaseOraclePackageCallCarriesItsSplitType() {
        var engine = SqlTestSupport.sqlEngine("ob4ora");
        try (var scripts = engine.splitAnalysisSpi(SqlTestSupport.parserParameters("ob4ora"))
            .splitScriptStream(new StringReader("CALL DBMS_SESSION.SET_IDENTIFIER('codex')"), List.of(), 1, 0)) {
            var types = scripts.flatMap(script -> script.getType().stream()).collect(Collectors.toSet());
            Assert.assertTrue(types.toString(), types.contains(SplitQueryType.SESSION_SETTING_WRITE));
        }
    }

    @TestFactory
    public Stream<DynamicTest> classifiedFunctionsRequireTheirActualResourcePermission() {
        Map<String, List<String>> cases = classifiedFunctionCases();
        return cases.entrySet().stream().map(entry -> DynamicTest.dynamicTest(entry.getKey(), () -> {
            List<String> expected = entry.getValue();
            List<BehaviorRequest> requests = functionRequests(entry.getKey(), expected.get(0));
            Assert.assertFalse("A function call must not require function READ: " + requests, requests.stream()
                .anyMatch(request -> request.resource().getObjectType() == TargetType.Function && request.action() == BehaviorAction.READ));
            Assert.assertTrue(requests.toString(), requests.stream()
                .anyMatch(request -> request.resource().getObjectType() == TargetType.valueOf(expected.get(1)) && request.action() == BehaviorAction.valueOf(expected.get(2))
                                     && request.authKind() == SecDataAuthKind.valueOf(expected.get(3))));
            Assert.assertTrue("Table READ must remain: " + requests, requests.stream()
                .anyMatch(request -> request.resource().getObjectType() == TargetType.Table && request.action() == BehaviorAction.READ
                                     && request.authKind() == SecDataAuthKind.READ));
        }));
    }

    @TestFactory
    public Stream<DynamicTest> configurationGettersKeepTheirNamedInstanceRead() {
        Map<String, List<String>> cases = new LinkedHashMap<>();
        cases.put("postgres", List.of("current_setting('max_connections')", "max_connections"));
        cases.put("greenplum", List.of("current_setting('max_connections')", "max_connections"));
        cases.put("cloudberry", List.of("current_setting('max_connections')", "max_connections"));
        cases.put("dameng", List.of("SF_GET_PARA_VALUE(1,'MAX_SESSIONS')", "MAX_SESSIONS"));
        cases.put("sqlserver", List.of("SESSION_CONTEXT(N'codex_context')", "codex_context"));
        cases.put("hana", List.of("SESSION_CONTEXT('APPLICATIONUSER')", "APPLICATIONUSER"));
        cases.put("oracle", List.of("SYS_CONTEXT('USERENV','SESSION_USER')", "USERENV.SESSION_USER"));
        cases.put("gauss", List.of("current_setting('max_connections')", "max_connections"));
        cases.put("clickhouse", List.of("getSetting('max_threads')", "max_threads"));
        return cases.entrySet().stream().map(entry -> DynamicTest.dynamicTest(entry.getKey(), () -> {
            List<BehaviorRequest> requests = functionRequests(entry.getKey(), entry.getValue().get(0));
            List<BehaviorRequest> reads = requests.stream().filter(request -> request.resource().getObjectType() == TargetType.ConfigKey).toList();
            Assert.assertEquals(requests.toString(), 1, reads.size());
            BehaviorRequest read = reads.get(0);
            Assert.assertEquals(BehaviorAction.READ, read.action());
            Assert.assertEquals(SecDataAuthKind.READ, read.authKind());
            Assert.assertEquals(entry.getValue().get(1), read.resource().getObjectName().getObjectName());
            Assert.assertEquals("/test/1/" + entry.getValue().get(1) + "/", BehaviorRelations.resourcePath(read.resource(), "/test/1/testdb/public/", "/test/1/"));
            Assert.assertFalse(requests.toString(), requests.stream()
                .anyMatch(request -> request.resource().getObjectType() == TargetType.Function && request.action() == BehaviorAction.READ));
            Assert.assertTrue(requests.toString(), requests.stream()
                .anyMatch(request -> request.resource().getObjectType() == TargetType.Function && request.action() == BehaviorAction.CALL));
            if (entry.getKey().equals("dameng")) {
                Assert.assertFalse("The parameter getter must use its ConfigKey permission: " + requests, requests.stream()
                    .anyMatch(request -> request.resource().getObjectType() == TargetType.Function && request.authKind() != null));
            }
        }));
    }

    @TestFactory
    public Stream<DynamicTest> metadataClassificationDoesNotRequireProgramPermission() {
        Map<String, String> cases = new LinkedHashMap<>();
        cases.put("mysql", "VERSION()");
        cases.put("mariadb", "VERSION()");
        cases.put("adb", "VERSION()");
        cases.put("por4x", "VERSION()");
        cases.put("ob4my", "VERSION()");
        cases.put("tidb", "TIDB_VERSION()");
        cases.put("postgres", "has_schema_privilege('public','USAGE')");
        cases.put("greenplum", "has_schema_privilege('public','USAGE')");
        cases.put("cloudberry", "has_schema_privilege('public','USAGE')");
        cases.put("cockroachdb", "has_schema_privilege('public','USAGE')");
        cases.put("hologres", "user_display_name('codex_absent')");
        cases.put("sqlserver", "OBJECT_ID('demo_table')");
        cases.put("oracle", "SYS_CONTEXT('USERENV','SESSION_USER')");
        cases.put("yashandb_oracle", "SYS_CONTEXT('USERENV','SESSION_USER')");
        cases.put("ob4ora", "SYS_CONTEXT('USERENV','SESSION_USER')");
        cases.put("hana", "SESSION_CONTEXT('APPLICATIONUSER')");
        cases.put("db2", "MON_GET_TABLE(NULL,NULL,-1)");
        cases.put("gauss", "current_setting('work_mem')");
        cases.put("dameng", "SF_GET_PARA_VALUE(1,'MAX_SESSIONS')");
        return cases.entrySet().stream().map(entry -> DynamicTest.dynamicTest(entry.getKey(), () -> {
            List<BehaviorRequest> requests = functionRequests(entry.getKey(), entry.getValue());
            Assert.assertTrue("Registered metadata CALL must remain visible: " + requests, requests.stream()
                .anyMatch(request -> request.resource().getObjectType() == TargetType.Function && request.action() == BehaviorAction.CALL && request.authKind() == null));
            Assert.assertFalse("Metadata alone is not a program permission: " + requests, requests.stream().anyMatch(request -> request.authKind() == SecDataAuthKind.PROGRAM));
            Assert.assertTrue("Table query permission must remain: " + requests, requests.stream()
                .anyMatch(request -> request.resource().getObjectType() == TargetType.Table && request.action() == BehaviorAction.READ
                                     && request.authKind() == SecDataAuthKind.READ));
        }));
    }

    @TestFactory
    public Stream<DynamicTest> compatibleSessionSettersKeepTheirResourcePermission() {
        return Stream.of("mysql", "mariadb", "adb", "por4x", "ob4my", "tidb").map(datasource -> DynamicTest.dynamicTest(datasource, () -> {
            List<BehaviorRequest> requests = functionRequests(datasource, "LAST_INSERT_ID(1)");
            Assert.assertTrue("Session mutation must retain its permission: " + requests, requests.stream()
                .anyMatch(request -> request.resource().getObjectType() == TargetType.Session && request.action() == BehaviorAction.CONFIGURE && request.authKind() != null));
            List<BehaviorRequest> getter = functionRequests(datasource, "LAST_INSERT_ID()");
            Assert.assertFalse("The getter must remain an ordinary call: " + getter, getter.stream()
                .anyMatch(request -> request.authKind() == SecDataAuthKind.PROGRAM || request.action() == BehaviorAction.CONFIGURE));
        }));
    }

    @TestFactory
    public Stream<DynamicTest> damengBareFunctionsRemainCallable() {
        return Stream.of("SYSDATE", "CURRENT_DATE", "DBMS_RANDOM.VALUE", "DBMS_UTILITY.GET_TIME").map(expression -> DynamicTest.dynamicTest(expression, () -> {
            List<BehaviorRequest> requests = functionRequests("dameng", expression);
            Assert.assertTrue(requests.toString(), requests.stream()
                .anyMatch(request -> request.resource().getObjectType() == TargetType.Function && request.action() == BehaviorAction.CALL));
            Assert.assertFalse(requests.toString(), requests.stream().anyMatch(request -> request.authKind() == SecDataAuthKind.PROGRAM));
            Assert.assertTrue(requests.toString(), requests.stream().anyMatch(request -> request.authKind() == SecDataAuthKind.READ));
        }));
    }

    @TestFactory
    public Stream<DynamicTest> damengConfigGettersResolveTheirOwnArgumentPosition() {
        return Stream
            .of("SF_GET_PARA_DOUBLE_VALUE", "SF_GET_PARA_STRING_VALUE", "SF_GET_PARA_VALUE", "SF_GET_SESSION_PARA_DOUBLE_VALUE", "SF_GET_SESSION_PARA_STRING_VALUE", "SF_GET_SESSION_PARA_VALUE")
            .map(name -> DynamicTest.dynamicTest(name, () -> {
                String arguments = "1,'MAX_SESSIONS'";
                if (name.startsWith("SF_GET_SESSION_")) {
                    arguments = "'MAX_SESSIONS'";
                }

                List<BehaviorRequest> requests = functionRequests("dameng", name + "(" + arguments + ")");
                Assert.assertTrue(requests.toString(), requests.stream()
                    .anyMatch(request -> request.resource().getObjectType() == TargetType.ConfigKey && request.action() == BehaviorAction.READ
                                         && "MAX_SESSIONS".equals(request.resource().getObjectName().getObjectName()) && request.authKind() == SecDataAuthKind.READ));
                Assert.assertFalse(requests.toString(), requests.stream().anyMatch(request -> request.authKind() == SecDataAuthKind.PROGRAM));
            }));
    }

    @TestFactory
    public Stream<DynamicTest> compatibleFileFunctionsKeepTheirResourcePermission() {
        return Stream.of("mysql", "mariadb", "adb", "por4x", "ob4my", "tidb").map(datasource -> DynamicTest.dynamicTest(datasource, () -> {
            List<BehaviorRequest> requests = functionRequests(datasource, "LOAD_FILE('/tmp/codex_absent')");
            Assert.assertTrue("File reads must retain their permission: " + requests, requests.stream()
                .anyMatch(request -> request.resource().getObjectType() == TargetType.File && request.action() == BehaviorAction.READ && request.authKind() != null));
            Assert.assertTrue("Unsafe operations must retain their permission: " + requests, requests.stream().anyMatch(request -> request.authKind() == SecDataAuthKind.UNSAFE));
        }));
    }

    @Test
    public void tiDbNativeFunctionAvailabilityUsesItsProductVersion() throws Exception {
        var registry = new TiSysObjectRegistrySpi();
        for (String name : List.of("TIDB_VERSION", "TIDB_DECODE_KEY")) {
            Assert.assertFalse(name, registry.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, name, "5.7.25-TiDB-v7.5.6"));
            Assert.assertTrue(name, registry.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, name, "5.7.25-TiDB-v8.5.0"));
            Assert.assertTrue(name, registry.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, name, "unknown"));
            Assert.assertFalse(name, registry.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, "custom_schema", name, "8.5.0"));
        }
    }

    @TestFactory
    public Stream<DynamicTest> protectedReadFunctionsKeepCallAndTargetPermissions() {
        Map<String, List<String>> cases = new LinkedHashMap<>();
        cases.put("greenplum", List.of("gp_read_error_log('other_schema.t')", "Table"));
        cases.put("cloudberry", List.of("gp_read_error_log('other_schema.t')", "Table"));
        cases.put("clickhouse", List.of("joinGet('other_schema.t','value',1)", "Table"));
        cases.put("gauss", List.of("pg_read_file('/tmp/codex_absent')", "File"));
        cases.put("oracle", List.of("UTL_HTTP.REQUEST('http://example.invalid')", ""));
        cases.put("maxcompute", List.of("GET_USER_ID()", "Instance"));
        return cases.entrySet().stream().map(entry -> DynamicTest.dynamicTest(entry.getKey(), () -> {
            List<BehaviorRequest> requests = functionRequests(entry.getKey(), entry.getValue().get(0));
            Assert.assertFalse(requests.toString(), requests.stream()
                .anyMatch(request -> request.resource().getObjectType() == TargetType.Function && request.action() == BehaviorAction.READ));
            Assert.assertTrue("The protected call must retain PROGRAM: " + requests, requests.stream()
                .anyMatch(request -> request.resource().getObjectType() == TargetType.Function && request.action() == BehaviorAction.CALL
                                     && request.authKind() == SecDataAuthKind.PROGRAM));
            String targetType = entry.getValue().get(1);
            if (!targetType.isEmpty()) {
                Assert.assertTrue("The actual resource READ must remain: " + requests, requests.stream()
                    .anyMatch(request -> request.resource().getObjectType() == TargetType.valueOf(targetType) && request.action() == BehaviorAction.READ
                                         && request.authKind() == SecDataAuthKind.READ));
            }

            if (targetType.equals("Table")) {
                BehaviorObject target = requests.stream()
                    .filter(request -> request.resource().getObjectType() == TargetType.Table && request.resource().getObjectName() == null)
                    .findFirst()
                    .orElseThrow()
                    .resource();
                Assert.assertEquals("/test/1/", BehaviorRelations.resourcePath(target, "/test/1/testdb/public/", "/test/1/"));
            }

            if (entry.getKey().equals("gauss")) {
                BehaviorObject target = requests.stream().filter(request -> request.resource().getObjectType() == TargetType.File).findFirst().orElseThrow().resource();
                Assert.assertEquals("/test/1/tmp/codex_absent/", BehaviorRelations.resourcePath(target, "/test/1/testdb/public/", "/test/1/"));
            }
        }));
    }

    @TestFactory
    public Stream<DynamicTest> functionDependenciesUseCallPermission() {
        return Stream
            .of(BehaviorAction.CREATE, BehaviorAction.ALTER, BehaviorAction.INSERT, BehaviorAction.UPDATE, BehaviorAction.DELETE, BehaviorAction.MERGE, BehaviorAction.REPLACE)
            .map(action -> DynamicTest.dynamicTest(action.name(), () -> {
                BehaviorRelation relation = relation(action, TargetType.Table);
                relation.setTarget(List.of(relation(BehaviorAction.CALL, TargetType.Function).getSubject()));
                List<BehaviorRequest> requests = BehaviorRelations.flattenResource(null, null, List.of(relation));
                Assert.assertTrue(requests.toString(), requests.stream()
                    .anyMatch(request -> request.resource().getObjectType() == TargetType.Function && request.action() == BehaviorAction.CALL
                                         && request.authKind() == SecDataAuthKind.PROGRAM));
                Assert.assertFalse(requests.toString(), requests.stream()
                    .anyMatch(request -> request.resource().getObjectType() == TargetType.Function && request.action() == BehaviorAction.READ));
                Assert.assertEquals(SecDataAuthKind.READ, flatten(BehaviorAction.READ, TargetType.Function).authKind());
            }));
    }

    @TestFactory
    public Stream<DynamicTest> externalFilePlanningKeepsRealReadTargets() {
        Map<String, String> cases = new LinkedHashMap<>();
        cases.put("doris", "EXPLAIN SELECT * FROM LOCAL('file_path'='/tmp/codex_absent.csv','backend_id'='1','format'='csv')");
        cases.put("starrocks", "EXPLAIN SELECT * FROM FILES('path'='file:///tmp/codex_absent.csv','format'='csv')");
        return cases.entrySet().stream().map(entry -> DynamicTest.dynamicTest(entry.getKey(), () -> {
            List<BehaviorRequest> requests = functionRequests(entry.getKey(), entry.getValue());
            Assert.assertTrue(requests.toString(), requests.stream()
                .anyMatch(request -> request.resource().getObjectType() == TargetType.File && request.action() == BehaviorAction.READ
                                     && request.authKind() == SecDataAuthKind.READ));
            Assert.assertFalse(requests.toString(), requests.stream()
                .anyMatch(request -> request.resource().getObjectType() == TargetType.File && request.authKind() == SecDataAuthKind.PROGRAM));
        }));
    }

    @Test
    public void permissionsComeFromBehaviorSemantics() {
        Assert.assertEquals(SecDataAuthKind.READ, flatten(BehaviorAction.READ, TargetType.Log).authKind());
        Assert.assertEquals(SecDataAuthKind.MAINTAIN, flatten(BehaviorAction.CHECKPOINT, TargetType.Instance).authKind());
        Assert.assertEquals(SecDataAuthKind.MAINTAIN, flatten(BehaviorAction.ANALYZE, TargetType.Table).authKind());
        Assert.assertEquals(SecDataAuthKind.UNSAFE, flatten(BehaviorAction.UNSAFE, TargetType.Instance).authKind());
        Assert.assertNull(flatten(BehaviorAction.LOCK, TargetType.Table).authKind());
    }

    @Test
    public void unnamedInstanceTargetsDoNotAcquireTheCurrentSchema() {
        BehaviorObject target = relation(BehaviorAction.READ, TargetType.Table).getSubject();
        target.setObjectPath("/test/1/");
        target.setObjectName(null);
        Assert.assertEquals("/test/1/", BehaviorRelations.resourcePath(target, "/test/1/testdb/public/", "/test/1/"));
        target.setObjectName(new ObjectName(null, null, "legacy_target"));
        Assert.assertEquals("/test/1/testdb/public/", BehaviorRelations.resourcePath(target, "/test/1/testdb/public/", "/test/1/"));
    }

    @Test
    public void managementObjectLifecycleUsesManagementPermission() {
        Assert.assertEquals(SecDataAuthKind.MANAGE, flatten(BehaviorAction.CREATE, TargetType.Link).authKind());
        Assert.assertEquals(SecDataAuthKind.MANAGE, flatten(BehaviorAction.ALTER, TargetType.Job).authKind());
        Assert.assertEquals(SecDataAuthKind.MANAGE, flatten(BehaviorAction.DROP, TargetType.Publication).authKind());
        Assert.assertEquals(SecDataAuthKind.MANAGE, flatten(BehaviorAction.CREATE, TargetType.RowAccessPolicy).authKind());
    }

    @Test
    public void operationalBehaviorUsesMaintenancePermission() {
        Assert.assertEquals(SecDataAuthKind.MAINTAIN, flatten(BehaviorAction.IMPORT, TargetType.Table).authKind());
        Assert.assertEquals(SecDataAuthKind.MAINTAIN, flatten(BehaviorAction.EXPORT, TargetType.File).authKind());
        Assert.assertEquals(SecDataAuthKind.MAINTAIN, flatten(BehaviorAction.CREATE, TargetType.ResourceGroup).authKind());
    }

    @Test
    public void grantsAndConfigurationUseManagementPermission() {
        Assert.assertEquals(SecDataAuthKind.MANAGE, flatten(BehaviorAction.GRANT, TargetType.UserOrRole).authKind());
        Assert.assertEquals(SecDataAuthKind.MANAGE, flatten(BehaviorAction.REVOKE, TargetType.Table).authKind());
        Assert.assertEquals(SecDataAuthKind.MANAGE, flatten(BehaviorAction.CONFIGURE, TargetType.ConfigKey).authKind());
    }

    @Test
    public void programmableObjectDefinitionsRequireDdlPermission() {
        Assert.assertEquals(SecDataAuthKind.DDL, flatten(BehaviorAction.CREATE, TargetType.Procedure).authKind());
        Assert.assertEquals(SecDataAuthKind.DDL, flatten(BehaviorAction.ALTER, TargetType.Function).authKind());
        Assert.assertEquals(SecDataAuthKind.DDL, flatten(BehaviorAction.DROP, TargetType.Trigger).authKind());
    }

    @Test
    public void programmableObjectExecutionRequiresProgramPermission() {
        Assert.assertEquals(SecDataAuthKind.PROGRAM, flatten(BehaviorAction.CALL, TargetType.Procedure).authKind());
        Assert.assertEquals(SecDataAuthKind.PROGRAM, flatten(BehaviorAction.CALL, TargetType.Function).authKind());
    }

    @Test
    @SuppressWarnings("deprecation")
    public void legacyOtherStatementKindsUseExplicitPermissions() {
        Assert.assertEquals(SecDataAuthKind.PROGRAM, RuleQueryType.BLOCK.getAuthKind());
        Assert.assertEquals(SecDataAuthKind.PROGRAM, RuleQueryType.PROGRAM_CONTROL.getAuthKind());
        Assert.assertEquals(SecDataAuthKind.READ, RuleQueryType.QUERY_LOCK.getAuthKind());
        Assert.assertEquals(SecDataAuthKind.MAINTAIN, RuleQueryType.SESSION_LOCK.getAuthKind());
        Assert.assertEquals(SecDataAuthKind.MAINTAIN, RuleQueryType.ANALYZE.getAuthKind());
        Assert.assertEquals(SecDataAuthKind.MAINTAIN, RuleQueryType.OPTIMIZE.getAuthKind());
        Assert.assertEquals(SecDataAuthKind.MAINTAIN, RuleQueryType.CHECK_TABLE.getAuthKind());
        Assert.assertEquals(SecDataAuthKind.MAINTAIN, RuleQueryType.TRANSACTION.getAuthKind());
        Assert.assertEquals(SecDataAuthKind.UNSAFE, RuleQueryType.PREPARE.getAuthKind());
        Assert.assertEquals(SecDataAuthKind.UNSAFE, RuleQueryType.EXECUTE.getAuthKind());
        Assert.assertEquals(SecDataAuthKind.UNSAFE, RuleQueryType.DEALLOCATE.getAuthKind());
        Assert.assertEquals(SecDataAuthKind.PROGRAM, RuleQueryType.SQL_BLOCK.getAuthKind());
        Assert.assertEquals(SecDataAuthKind.MAINTAIN, RuleQueryType.EXPORT.getAuthKind());
        Assert.assertEquals(SecDataAuthKind.MAINTAIN, RuleQueryType.SYNC.getAuthKind());
        Assert.assertEquals(SecDataAuthKind.UNSAFE, RuleQueryType.UNSAFE.getAuthKind());
        Assert.assertEquals(SecDataAuthKind.UNSAFE, RuleQueryType.UNKNOWN.getAuthKind());
    }

    @Test
    public void namedRegistryCanExemptBehaviorResource() throws Exception {
        BehaviorRelation relation = relation(BehaviorAction.READ, TargetType.Function);
        relation.getSubject().setObjectName(new ObjectName(null, null, "BUILT_IN"));
        SysObjectRegistrySpi registry = (action, targetType, catalog, schema, objectName, dbVersion) -> {
            return action == BehaviorAction.READ && targetType == TargetType.Function && "BUILT_IN".equals(objectName);
        };

        List<BehaviorRequest> requests = BehaviorRelations.flattenResource(registry, "8", List.of(relation));

        Assert.assertEquals(1, requests.size());
        Assert.assertNull(requests.get(0).authKind());

        for (String[] names : List
            .of(new String[] { "\"MyFn\"",
                               "MyFn" }, new String[] { "`MyFn`",
                                                        "MyFn" }, new String[] { "[MyFn]",
                                                                                 "MyFn" }, new String[] { "\"My\"\"Fn\"",
                                                                                                          "My\"Fn" }, new String[] { "`My``Fn`",
                                                                                                                                     "My`Fn" }, new String[] { "[My]]Fn]",
                                                                                                                                                               "My]Fn" })) {
            BehaviorObject function = relation(BehaviorAction.CALL, TargetType.Function).getSubject();
            function.setObjectName(new ObjectName(null, null, names[0]));
            function.setObjectPath("/test/1/testdb/public/" + names[0] + "/");
            String quotedPath = BehaviorRelations.resourcePath(function, "/test/1/testdb/public/", "/test/1/");
            function.setObjectName(new ObjectName(null, null, names[1]));
            function.setObjectPath("/test/1/testdb/public/" + names[1] + "/");
            Assert.assertEquals(BehaviorRelations.resourcePath(function, "/test/1/testdb/public/", "/test/1/"), quotedPath);
            Assert.assertEquals("/test/1/testdb/public/" + names[1] + "/", quotedPath);
        }

        List<BehaviorRequest> mixed = functionRequests("postgres", "LOWER(name), \"LOWER\"(name)");
        Assert.assertTrue(mixed.stream().anyMatch(request -> request.authKind() == SecDataAuthKind.PROGRAM));
        Assert.assertTrue(mixed.stream().anyMatch(request -> request.resource().getObjectType() == TargetType.Function && request.authKind() == null));
        List<String> mixedCasePaths = functionRequests("postgres", "\"MyFn\"(name), \"myfn\"(name)").stream()
            .filter(request -> request.resource().getObjectType() == TargetType.Function)
            .map(request -> BehaviorRelations.resourcePath(request.resource(), "/test/1/testdb/public/", "/test/1/"))
            .toList();
        Assert.assertEquals(2, mixedCasePaths.stream().distinct().count());

        BehaviorObject qualified = relation(BehaviorAction.CALL, TargetType.Function).getSubject();
        qualified.setObjectName(new ObjectName("\"Db\"", "\"Sc\"\"hema\"", "\"MyFn\""));
        qualified.setObjectPath("/test/1/\"Db\"/\"Sc\"\"hema\"/\"MyFn\"/");
        Assert.assertEquals("/test/1/Db/Sc\"hema/MyFn/", BehaviorRelations.resourcePath(qualified, "/test/1/testdb/public/", "/test/1/"));
        qualified.setObjectType(TargetType.Table);
        Assert.assertEquals("/test/1/\"Db\"/\"Sc\"\"hema\"/\"MyFn\"/", BehaviorRelations.resourcePath(qualified, "/test/1/testdb/public/", "/test/1/"));
    }

    @TestFactory
    public Stream<DynamicTest> builtinFunctionsKeepOnlyTablePermissions() {
        return Stream
            .of("mysql", "mariadb", "goldendb", "por4my", "postgres", "por4pg", "greenplum", "cloudberry", "cockroachdb", "sqlserver", "oracle", "yashandb_oracle", "db2", "hana", "doris", "selectdb", "clickhouse", "starrocks", "gauss", "gauss_og", "dameng", "maxcompute", "adb", "por4x", "tidb", "ob4my", "ob4ora", "hologres")
            .flatMap(datasource -> Stream.of("COUNT(*)", "SUM(amount)", "LOWER(name)", "ABS(amount)", "ROW_NUMBER() OVER (ORDER BY name)", "DENSE_RANK() OVER (ORDER BY name)")
                .map(expression -> DynamicTest.dynamicTest(datasource + ": " + expression, () -> {
                    List<BehaviorRequest> requests = functionRequests(datasource, expression);
                    Assert.assertTrue("Table READ must remain: " + requests, requests.stream()
                        .anyMatch(request -> request.authKind() == SecDataAuthKind.READ && request.resource().getObjectType() == TargetType.Table));
                    boolean unverified = datasource.equals("maxcompute") && (expression.startsWith("LOWER") || expression.startsWith("ABS"));
                    Assert.assertEquals("Function permission: " + requests, unverified, requests.stream()
                        .anyMatch(request -> request.resource().getObjectType() == TargetType.Function && request.authKind() != null));
                })));
    }

    @TestFactory
    public Stream<DynamicTest> userFunctionsStillRequireProgramPermission() {
        return Stream
            .of("mysql", "postgres", "sqlserver", "oracle", "yashandb_oracle", "db2", "hana", "doris", "clickhouse", "starrocks", "dameng", "tidb", "ob4my", "ob4ora", "hologres", "greenplum", "cloudberry", "cockroachdb", "gauss", "maxcompute", "adb", "por4x")
            .flatMap(datasource -> Stream.of("codex_user_func(name)", "LOWER(codex_user_func(name))", "custom_schema.lower(name)", "MIN(custom_schema.lower(name))")
                .filter(expression -> !"clickhouse".equals(datasource) || !expression.contains("custom_schema"))
                .map(expression -> DynamicTest.dynamicTest(datasource + ": " + expression, () -> {
                    List<BehaviorRequest> requests = functionRequests(datasource, expression);
                    Assert.assertTrue(requests.stream().anyMatch(request -> request.authKind() == SecDataAuthKind.PROGRAM));
                    if (expression.contains("custom_schema")) {
                        Assert.assertTrue("Qualified function must retain its schema: " + requests, requests.stream()
                            .anyMatch(request -> request.authKind() == SecDataAuthKind.PROGRAM && "custom_schema".equalsIgnoreCase(request.resource().getObjectName().getSchema())
                                                 && "lower".equalsIgnoreCase(request.resource().getObjectName().getObjectName())));
                    }
                })));
    }

    @Test
    public void ordinaryRegistriesDoNotExemptManagementOrUnknownResources() {
        for (SysObjectRegistrySpi registry : List.of(new MsSysObjectRegistrySpi(), new ChSysObjectRegistrySpi(), new SrSysObjectRegistrySpi())) {
            for (BehaviorAction action : List.of(BehaviorAction.CREATE, BehaviorAction.ALTER, BehaviorAction.DROP, BehaviorAction.CONFIGURE, BehaviorAction.UNSAFE)) {
                Assert.assertFalse(registry.isPermissionExempt(action, TargetType.Function, null, null, "count", "26.9.5.2"));
            }

            Assert.assertFalse(registry.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, "custom_schema", "count", "26.9.5.2"));
            Assert.assertFalse(registry.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "codex_user_func", "26.9.5.2"));
        }

        ChSysObjectRegistrySpi clickhouse = new ChSysObjectRegistrySpi();
        for (String function : List.of("file", "dictGet", "joinGet", "sleep", "logTrace", "getSetting")) {
            Assert.assertFalse(function, clickhouse.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, function, "26.9.5.2"));
        }

        Assert.assertTrue(clickhouse.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "sumIf", "26.9.5.2"));
        Assert.assertTrue(clickhouse.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "uniqArrayIf", "26.9.5.2"));
        Assert.assertFalse(clickhouse.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "lowerIf", "26.9.5.2"));
        Assert.assertFalse(clickhouse.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "ToString", "26.9.5.2"));
        for (String name : List.of("sumIf", "countIf", "avgIf", "sumArray", "sumArrayIf")) {
            Assert.assertTrue(name, clickhouse.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, name, "26.8.7.19"));
            Assert.assertFalse(name, clickhouse.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, name, "25.12.0"));
            Assert.assertTrue(name, clickhouse.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, name, "unknown"));
        }

        Assert.assertFalse(new SrSysObjectRegistrySpi().isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "http_request", "4.1.0"));
    }

    @Test
    public void postgresAdministrationStillRequiresPermission() throws Exception {
        for (String expression : List.of("pg_terminate_backend(12345)", "pg_reload_conf()", "set_config('application_name', 'audit', false)")) {
            Assert.assertTrue(expression, functionRequests("postgres", expression).stream()
                .anyMatch(request -> request.authKind() == SecDataAuthKind.MANAGE || request.authKind() == SecDataAuthKind.MAINTAIN));
        }
    }

    @TestFactory
    public Stream<DynamicTest> customFunctionsAreCheckedInsideDataChanges() {
        return Stream.of("oracle", "yashandb_oracle", "db2", "gauss", "maxcompute", "adb", "por4x", "ob4ora")
            .flatMap(datasource -> Stream
                .of("INSERT INTO target_table SELECT codex_user_func(name) FROM demo_table", "UPDATE demo_table SET name = codex_user_func(name)", "DELETE FROM demo_table WHERE codex_user_func(name) = 'x'")
                .map(sql -> DynamicTest.dynamicTest(datasource + ": " + sql, () -> {
                    var engine = SqlTestSupport.sqlEngine(datasource);
                    try (var reader = new StringReader(sql);
                            var stream = engine.behaviorAnalysisSpi(SqlTestSupport.parserParameters(datasource))
                                .analysisBehaviorStream(reader, Map.of(UmiTypes.Instance, "test/1", UmiTypes.Catalog, "testdb", UmiTypes.Schema, "public"), 0, 0)) {
                        var requests = stream.flatMap(statement -> BehaviorRelations.flattenResource(null, "16.0", statement.getRelations()).stream()).toList();
                        Assert.assertTrue("Missing function permission: " + requests, requests.stream().anyMatch(request -> request.authKind() == SecDataAuthKind.PROGRAM));
                    }
                })));
    }

    @Test
    public void unclassifiedAdministrativeFunctionsStillRequireProgramPermission() throws Exception {
        for (String expression : List.of("pg_stat_reset()", "nextval('test_sequence')", "lo_unlink(12345)")) {
            Assert.assertTrue(expression, functionRequests("postgres", expression).stream().anyMatch(request -> request.authKind() == SecDataAuthKind.PROGRAM));
        }
    }

    @TestFactory
    public Stream<DynamicTest> productSpecificOrdinaryFunctionsRetainTableRead() {
        Map<String, List<String>> expressions = Map.of("cockroachdb", List.of("fnv32(name)", "count_rows()", "cosine_distance('[1,2]', '[2,1]')"), "por4x", List
            .of("TO_MONTHS('2024-01-01')", "TO_WEEKS('2024-01-01')", "TIME_TO_TSO('2024-01-01 00:00:00')", "TSO_TO_TIME(1)", "VEC_TOTEXT(VEC_FROMTEXT('[1,2]'))", "VEC_DISTANCE_COSINE(VEC_FROMTEXT('[1,2]'), VEC_FROMTEXT('[2,1]'))"), "ob4my", List
                .of("NVL(name, 'fallback')", "APPROX_COUNT_DISTINCT(amount)", "RB_CARDINALITY(RB_BUILD_AGG(amount))", "TIME_TO_USEC('2024-01-01 00:00:00')"), "adb", List
                    .of("APPROX_DISTINCT(amount)", "APPROX_PERCENTILE(amount, 0.5)", "RB_CARDINALITY(RB_BUILD_AGG(amount))"), "hologres", List
                        .of("APPROX_COUNT_DISTINCT(amount)", "APPROX_PERCENTILE(amount, 0.5)", "MAX_BY(name, amount)", "MIN_BY(name, amount)", "UNIQ(name)"), "mariadb", List
                            .of("ADD_MONTHS('2024-01-01', 1)", "JSON_DETAILED(name)", "COLUMN_JSON(name)", "SYS_GUID()"));
        return expressions.entrySet().stream().flatMap(entry -> entry.getValue().stream().map(expression -> DynamicTest.dynamicTest(entry.getKey() + ": " + expression, () -> {
            List<BehaviorRequest> requests = functionRequests(entry.getKey(), expression);
            Assert.assertTrue("Table READ must remain: " + requests, requests.stream().anyMatch(request -> request.authKind() == SecDataAuthKind.READ));
            boolean unverified = entry.getKey().equals("adb") && expression.startsWith("APPROX_")
                                 || entry.getKey().equals("hologres") && expression.startsWith("APPROX_COUNT_DISTINCT");
            Assert.assertEquals("Version evidence boundary: " + requests, unverified, requests.stream().anyMatch(request -> request.authKind() == SecDataAuthKind.PROGRAM));
        })));
    }

    @Test
    public void mysqlCompatibleRegistriesKeepNonOrdinaryCallsProtected() throws Exception {
        for (SysObjectRegistrySpi registry : List.of(new PorXSysObjectRegistrySpi(), new AdsMySysObjectRegistrySpi(), new ObMySysObjectRegistrySpi())) {
            for (String name : List.of("LOAD_FILE", "GET_LOCK", "RELEASE_LOCK", "BENCHMARK", "NEXTVAL", "LBAC_CHECK", "ENCDB_REGISTER_MEK", "AI_EMBEDDING", "REMOTE_CALL")) {
                Assert.assertFalse(name, registry.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, name, "8.0.46"));
            }
        }

        var mysql = new MySysObjectRegistrySpi();
        for (String version : List.of("5.6.51", "5.7.44", "8.0.46", "8.4.7", "9.7.0")) {
            for (String name : List.of("LOAD_FILE", "GET_LOCK", "RELEASE_LOCK", "BENCHMARK")) {
                Assert.assertFalse(name + ": " + version, mysql.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, name, version));
            }

            List<BehaviorRequest> fileRequests = functionRequests("mysql", "LOAD_FILE('/tmp/codex_absent')", version);
            Assert.assertTrue(fileRequests.stream().anyMatch(request -> request.authKind() == SecDataAuthKind.MAINTAIN));
            Assert.assertTrue(fileRequests.stream().anyMatch(request -> request.authKind() == SecDataAuthKind.UNSAFE));
            Assert.assertTrue(fileRequests.stream().anyMatch(request -> request.resource().getObjectType() == TargetType.File && request.action() == BehaviorAction.READ));
            Assert.assertTrue(mysql.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "LAST_INSERT_ID", version));
        }
    }

    @TestFactory
    public Stream<DynamicTest> yashanDbOrdinaryFunctionsAreIsolatedFromOracle() {
        return Stream.of("WM_CONCAT(name)", "WM_CONCAT(DISTINCT name)", "WM_CONCAT(name) OVER (PARTITION BY amount)").map(expression -> DynamicTest.dynamicTest(expression, () -> {
            List<BehaviorRequest> requests = functionRequests("yashandb_oracle", expression);
            Assert.assertTrue(requests.stream().anyMatch(request -> request.authKind() == SecDataAuthKind.READ));
            Assert.assertTrue(requests.stream().noneMatch(request -> request.authKind() == SecDataAuthKind.PROGRAM));
            Assert.assertTrue(functionRequests("oracle", expression).stream().anyMatch(request -> request.authKind() == SecDataAuthKind.PROGRAM));
        }));
    }

    @Test
    public void yashanDbRegistryKeepsQualifiedAndQuotedFunctionsProtected() throws Exception {
        YashanDBOracleSysObjectRegistrySpi registry = new YashanDBOracleSysObjectRegistrySpi();
        Assert.assertEquals("Oracle SQL", SqlTestSupport.sqlEngine("yashandb_oracle").name());
        Assert.assertTrue(registry.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "WM_CONCAT", "23.4"));
        Assert.assertTrue(registry.isPermissionExempt(BehaviorAction.READ, TargetType.Table, null, null, "DUAL", "23.4"));
        Assert.assertFalse(registry.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, "custom_schema", "WM_CONCAT", "23.4"));
        Assert.assertFalse(registry.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "\"WM_CONCAT\"", "23.4"));
        Assert.assertFalse(registry.isPermissionExempt(BehaviorAction.DROP, TargetType.Function, null, null, "WM_CONCAT", "23.4"));
        for (String expression : List.of("custom_schema.WM_CONCAT(name)", "LOWER(custom_schema.WM_CONCAT(name))", "WM_CONCAT(codex_user_func(name))", "\"WM_CONCAT\"(name)")) {
            Assert.assertTrue(expression, functionRequests("yashandb_oracle", expression).stream().anyMatch(request -> request.authKind() == SecDataAuthKind.PROGRAM));
        }
    }

    @Test
    public void yashanDbProductVersionsDoNotBecomeOracleVersions() throws Exception {
        var registry = new YashanDBOracleSysObjectRegistrySpi();
        for (String name : List.of("COUNT", "ABS", "WM_CONCAT")) {
            Assert.assertTrue(name, registry.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, name, "23.4.7.106"));
        }

        for (String version : List.of("23.4.7.106", "23.2.0", "23.5.0", "unknown", "19.0.0")) {
            for (String name : List.of("APPROX_COUNT", "ANY_VALUE")) {
                Assert.assertFalse(name + ": " + version, registry.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, name, version));
            }

            Assert.assertFalse(registry.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "codex_user_func", version));
        }

        for (String version : List.of("23.2.0", "23.5.0", "unknown", "19.0.0")) {
            Assert.assertTrue(version, registry.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "COUNT", version));
            Assert.assertTrue(version, registry.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "WM_CONCAT", version));
        }

        String oracleVersion = "Oracle Database 19c Enterprise Edition Release 19.0.0.0.0";
        for (String name : List.of("COUNT", "ABS", "APPROX_COUNT", "ANY_VALUE")) {
            Assert.assertTrue(name, registry.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, name, oracleVersion));
            Assert.assertFalse(name, registry.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, "custom_schema", name, oracleVersion));
            Assert.assertFalse(name, registry.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "\"" + name + "\"", oracleVersion));
        }

        Assert.assertFalse(registry.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "WM_CONCAT", oracleVersion));
        Assert.assertFalse(registry.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "codex_user_func", oracleVersion));
        Assert.assertTrue(functionRequests("yashandb_oracle", "ANY_VALUE(amount)", "23.4.7.106").stream().anyMatch(request -> request.authKind() == SecDataAuthKind.PROGRAM));
        Assert.assertTrue(functionRequests("yashandb_oracle", "COUNT(*)", oracleVersion).stream().noneMatch(request -> request.authKind() == SecDataAuthKind.PROGRAM));
    }

    @TestFactory
    public Stream<DynamicTest> productCatalogsExemptOnlyOrdinaryUnqualifiedCalls() {
        Map<SysObjectRegistrySpi, String> catalogs = Map
            .of(new PorXSysObjectRegistrySpi(), "polardbx-built-in-functions.json", new AdsMySysObjectRegistrySpi(), "adb-built-in-functions.json", new HgSysObjectRegistrySpi(), "hologres-built-in-functions.json", new ObMySysObjectRegistrySpi(), "oceanbase-mysql-built-in-functions.json", new MySysObjectRegistrySpi(), "mariadb-built-in-functions.json");
        return catalogs.entrySet()
            .stream()
            .flatMap(entry -> BuiltinFunctionCatalogLoader.load(entry.getKey().getClass(), FUNCTION_RESOURCES_ROOT + entry.getValue()).stream().map(function -> {
                String name = function.name();
                return DynamicTest.dynamicTest(entry.getValue() + ": " + name, () -> {
                    SysObjectRegistrySpi registry = entry.getKey();
                    String version = productVersion(entry.getKey());
                    if (registry instanceof MySysObjectRegistrySpi) {
                        version = "12.2.1-MariaDB";
                    }

                    boolean ordinary = !function.versions().isEmpty();
                    if (registry instanceof ObMySysObjectRegistrySpi) {
                        FunctionBehavior rule = ObMyResourceRegistry.instance().functionClassification(name);
                        ordinary = ordinary && rule.action() == BehaviorAction.CALL && !rule.privileged() && !rule.unsafe();
                    }
                    Assert.assertEquals(name, ordinary, registry.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, name, version));
                    Assert.assertFalse(name, registry.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, "custom_schema", name, version));
                    Assert.assertFalse(name, registry.isPermissionExempt(BehaviorAction.DROP, TargetType.Function, null, null, name, version));
                    Assert.assertFalse(name, registry.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "\"" + name + "\"", version));
                });
            }));
    }

    @TestFactory
    public Stream<DynamicTest> qualifiedProductFunctionsRemainCustomPrograms() {
        Map<String, String> names = Map
            .of("por4x", "to_months", "adb", "approx_distinct", "hologres", "min_by", "mariadb", "json_detailed", "cockroachdb", "fnv32", "ob4my", "rb_cardinality");
        return names.entrySet().stream().map(entry -> DynamicTest.dynamicTest(entry.getKey(), () -> {
            List<BehaviorRequest> requests = functionRequests(entry.getKey(), "LOWER(custom_schema." + entry.getValue() + "(name))");
            Assert.assertTrue(requests.stream()
                .anyMatch(request -> request.authKind() == SecDataAuthKind.PROGRAM && "custom_schema".equalsIgnoreCase(request.resource().getObjectName().getSchema())
                                     && entry.getValue().equalsIgnoreCase(request.resource().getObjectName().getObjectName())));
        }));
    }

    @Test
    public void productExtensionsDoNotLeakIntoOtherEnginesOrQualifiedCalls() {
        Map<SysObjectRegistrySpi, List<String>> names = Map
            .of(new PorXSysObjectRegistrySpi(), List.of("TO_MONTHS", "TIME_TO_TSO", "VEC_FROMTEXT"), new AdsMySysObjectRegistrySpi(), List
                .of("APPROX_DISTINCT", "RB_CARDINALITY"), new HgSysObjectRegistrySpi(), List.of("approx_count_distinct", "min_by"));
        for (var entry : names.entrySet()) {
            for (String name : entry.getValue()) {
                Assert.assertEquals(name, !(entry.getKey() instanceof AdsMySysObjectRegistrySpi && name.startsWith("APPROX_"))
                                          && !(entry.getKey() instanceof HgSysObjectRegistrySpi && name.equals("approx_count_distinct")), entry.getKey()
                                              .isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, name, productVersion(entry.getKey())));
                Assert.assertFalse(name, entry.getKey().isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, "custom_schema", name, productVersion(entry.getKey())));
                Assert.assertFalse(name, entry.getKey().isPermissionExempt(BehaviorAction.DROP, TargetType.Function, null, null, name, productVersion(entry.getKey())));
                Assert
                    .assertFalse(name, new MySysObjectRegistrySpi().isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, name, productVersion(entry.getKey())));
                Assert.assertFalse(name, new PgSysObjectRegistrySpi().isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, name, "16.0"));
            }
        }

        MySysObjectRegistrySpi mysql = new MySysObjectRegistrySpi();
        for (String version : List.of("10.6.18-MariaDB", "11.4.13-MariaDB-ubu2404", "5.5.5-11.4.13-MariaDB-ubu2404")) {
            for (String name : List.of("MIN", "COUNT", "SUM", "LOWER", "ABS", "JSON_EXTRACT", "ROW_NUMBER", "DENSE_RANK", "RANK", "NTILE", "LAG", "LEAD")) {
                Assert.assertTrue(name + ": " + version, mysql.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, name, version));
                Assert.assertFalse(name, mysql.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, "custom_schema", name, version));
            }

            for (String name : List.of("ANY_VALUE", "GROUPING", "BIN_TO_UUID", "codex_user_func", "LOAD_FILE", "GET_LOCK", "BENCHMARK", "SLEEP", "NEXTVAL")) {
                Assert.assertFalse(name, mysql.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, name, version));
            }

            Assert.assertFalse(mysql.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "`MIN`", version));
        }

        for (String version : List.of("unknown-MariaDB", "99.0.0-MariaDB", "11.99.0-MariaDB", "12.3.0-MariaDB")) {
            Assert.assertTrue(version, mysql.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "COUNT", version));
            Assert.assertTrue(version, mysql.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "ADD_MONTHS", version));
        }

        Assert.assertTrue(mysql.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "JSON_DETAILED", "5.5.5-10.6.18-MariaDB"));
        Assert.assertFalse(mysql.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "VEC_DISTANCE_COSINE", "10.6.18-MariaDB"));
        Assert.assertTrue(mysql.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "VEC_DISTANCE_COSINE", "11.8.3-MariaDB"));
        Assert.assertFalse(mysql.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, "custom_schema", "JSON_DETAILED", "11.8.3-MariaDB"));
        Assert.assertFalse(mysql.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "`JSON_DETAILED`", "11.8.3-MariaDB"));
        for (String name : List.of("NEXTVAL", "SETVAL", "SPIDER_DIRECT_SQL", "MASTER_GTID_WAIT")) {
            Assert.assertFalse(name, mysql.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, name, "11.8.3-MariaDB"));
        }
    }

    @TestFactory
    public Stream<DynamicTest> derivativeFunctionCatalogsKeepPermissionBoundaries() {
        Map<SysObjectRegistrySpi, String> catalogs = Map
            .of(new GpSysObjectRegistrySpi(), GREENPLUM_BUILT_IN_FUNCTIONS_RESOURCE, new CbSysObjectRegistrySpi(), CLOUDBERRY_BUILT_IN_FUNCTIONS_RESOURCE, new ObOraSysObjectRegistrySpi(), OCEANBASE_ORACLE_BUILT_IN_FUNCTIONS_RESOURCE);
        return catalogs.entrySet().stream().flatMap(entry -> BuiltinFunctionCatalogLoader.load(entry.getKey().getClass(), entry.getValue()).stream().map(function -> {
            String functionName = function.name();
            String schema = null;
            String name = functionName;
            if (functionName.contains(".")) {
                String[] parts = functionName.split("\\.", 2);
                schema = parts[0];
                name = parts[1];
            }

            String functionSchema = schema;
            String objectName = name;
            return DynamicTest.dynamicTest(entry.getValue() + ": " + functionName, () -> {
                var registry = entry.getKey();
                boolean ordinary = true;
                if (registry instanceof ObOraSysObjectRegistrySpi) {
                    FunctionBehavior rule = ObOraResourceRegistry.instance().functionBehavior(functionName);
                    ordinary = rule.action() == BehaviorAction.CALL && !rule.privileged() && !rule.unsafe();
                }
                Assert.assertEquals(ordinary, registry.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, functionSchema, objectName, productVersion(registry)));
                Assert.assertFalse(registry.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, "custom_schema", objectName, productVersion(registry)));
                Assert.assertFalse(registry.isPermissionExempt(BehaviorAction.DROP, TargetType.Function, null, functionSchema, objectName, productVersion(registry)));
                Assert.assertFalse(registry.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, functionSchema, "\"" + objectName + "\"", productVersion(registry)));
            });
        }));
    }

    @Test
    public void greenplumArrayAggregateUsesProductVersionDefault() throws Exception {
        var greenplum = new GpSysObjectRegistrySpi();
        Assert.assertTrue(greenplum.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "gp_array_agg", "12.12 (Greenplum Database 7.0.0)"));
        for (String version : List.of("9.4.26", "6.26.2", "", "unknown")) {
            Assert.assertTrue(version, greenplum.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "gp_array_agg", version));
        }

        Assert.assertTrue(greenplum.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "gp_array_agg", null));
        Assert.assertTrue(functionRequests("greenplum", "gp_array_agg(amount)").stream().noneMatch(request -> request.authKind() == SecDataAuthKind.PROGRAM));
        Assert.assertTrue(functionRequests("cloudberry", "sm3(name)").stream().noneMatch(request -> request.authKind() == SecDataAuthKind.PROGRAM));
        Assert.assertFalse(new CbSysObjectRegistrySpi().isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "gp_array_agg", "14.4"));
        for (SysObjectRegistrySpi registry : List.of(greenplum, new CbSysObjectRegistrySpi())) {
            for (String name : List.of("enable_xform", "disable_xform", "gp_truncate_error_log", "gp_create_restore_point", "gp_percentile_disc_transition")) {
                Assert.assertFalse(name, registry.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, name, "14.4"));
            }
        }
    }

    @TestFactory
    public Stream<DynamicTest> greenplumAndCloudberryOrdinaryExpressionsKeepTableRead() {
        return Stream.of("greenplum", "cloudberry")
            .flatMap(datasource -> Stream
                .of("array_add(ARRAY[1,2], ARRAY[3,4])", "pg_catalog.array_add(ARRAY[1,2], ARRAY[3,4])", "array_sum(ARRAY[1,2])", "complex(1,2)", "complex_trig(1,2)", "conj(complex(1,2))", "dotproduct(ARRAY[complex(1,2)], ARRAY[complex(3,4)])", "im(complex(1,2))", "re(complex(1,2))", "linear_interpolate(5,0,0,10,10)", "median(amount)", "pivot_sum(ARRAY['x'],name,amount)", "interval_bound(TIMESTAMPTZ '2024-01-01',INTERVAL '1 day',0,TIMESTAMPTZ '2024-01-01')")
                .map(expression -> DynamicTest.dynamicTest(datasource + ": " + expression, () -> {
                    List<BehaviorRequest> requests = functionRequests(datasource, expression);
                    Assert.assertTrue(requests.stream().anyMatch(request -> request.authKind() == SecDataAuthKind.READ));
                    Assert.assertTrue(requests.stream()
                        .map(request -> request.resource().getObjectPath() + ":" + request.resource().getObjectName().getSchema() + ":"
                                        + request.resource().getObjectName().getObjectName())
                        .toList()
                        .toString(), requests.stream().noneMatch(request -> request.authKind() == SecDataAuthKind.PROGRAM));
                })));
    }

    @TestFactory
    public Stream<DynamicTest> oceanbaseOracleProductFunctionsKeepTableRead() {
        return Stream
            .of("WM_CONCAT(name)", "WM_CONCAT(DISTINCT name)", "WM_CONCAT(name) OVER (PARTITION BY amount)", "WMSYS.WM_CONCAT(name)", "ROUND(amount)", "TRUNC(amount)", "TO_CLOB(name)", "NORMAL(0,1,1)", "UNIFORM(0,10,1)", "RANDSTR(5,1)", "ZIPF(1,10,1)", "RANDOM()")
            .map(expression -> DynamicTest.dynamicTest(expression, () -> {
                List<BehaviorRequest> requests = functionRequests("ob4ora", expression);
                Assert.assertTrue(requests.stream().anyMatch(request -> request.authKind() == SecDataAuthKind.READ));
                Assert.assertTrue(requests.stream()
                    .map(request -> request.resource().getObjectPath() + ":" + request.resource().getObjectName().getSchema() + ":"
                                    + request.resource().getObjectName().getObjectName())
                    .toList()
                    .toString(), requests.stream().noneMatch(request -> request.authKind() == SecDataAuthKind.PROGRAM));
            }));
    }

    @TestFactory
    public Stream<DynamicTest> derivativeCatalogsDoNotExemptCustomFunctions() {
        return Stream.of("greenplum", "cloudberry", "ob4ora")
            .flatMap(datasource -> Stream
                .of("custom_schema.median(amount)", "median(codex_user_func(amount))", "WM_CONCAT(codex_user_func(name))", "custom_schema.WM_CONCAT(name)", "\"WM_CONCAT\"(name)", "\"WMSYS\".WM_CONCAT(name)", "WMSYS.codex_user_func(name)")
                .filter(expression -> "ob4ora".equals(datasource) || !expression.contains("WM_CONCAT"))
                .map(expression -> DynamicTest.dynamicTest(datasource + ": " + expression, () -> {
                    Assert.assertTrue(functionRequests(datasource, expression).stream().anyMatch(request -> request.authKind() == SecDataAuthKind.PROGRAM));
                })));
    }

    @Test
    public void cockroachFunctionRegistrationUsesNamedProductVersions() throws Exception {
        var registry = new CrdbSysObjectRegistrySpi();
        try (var input = CrdbResourceRegistry.class.getResourceAsStream(FUNCTION_RESOURCES_ROOT + "cockroachdb-built-in-functions.json")) {
            var entries = new ObjectMapper().readTree(input).path("entries");
            for (var entry : entries) {
                String name = entry.path("name").get(0).asText();
                Assert.assertEquals("24", entry.path("versions").get(0).asText());
                for (String version : List
                    .of("24.3", "24.3.1", "CockroachDB CCL v24.3.1 (aarch64-unknown-linux-gnu)", "13.0.0 (CockroachDB CCL v24.3.1 (aarch64-unknown-linux-gnu))")) {
                    Assert.assertTrue(name, registry.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, name, version));
                }

                for (String version : List.of("23.2.0", "24.2.9", "24.4.0", "25.1.0", "", "unknown")) {
                    Assert.assertTrue(name + ": " + version, registry.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, name, version));
                }

                for (String version : List.of("13.0.0", "13.0.0 (CockroachDB CCL v25.1.0)")) {
                    Assert.assertTrue(name + ": " + version, CrdbResourceRegistry.instance().contains(name, version));
                }

                Assert.assertTrue(name, registry.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, name, null));
                Assert.assertFalse(name, registry.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, "custom_schema", name, "24.3.1"));
                Assert.assertFalse(name, registry.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "\"" + name + "\"", "24.3.1"));
                Assert.assertFalse(name, registry.isPermissionExempt(BehaviorAction.DROP, TargetType.Function, null, null, name, "24.3.1"));
            }
        }
    }

    @TestFactory
    public Stream<DynamicTest> unknownVersionsUseEnumDefaults() {
        return Stream
            .of("mysql", "mariadb", "postgres", "oracle", "yashandb_oracle", "sqlserver", "db2", "hana", "gauss", "doris", "clickhouse", "starrocks", "ob4ora", "dameng", "adb", "por4x", "ob4my", "greenplum", "cloudberry", "cockroachdb", "hologres")
            .flatMap(datasource -> Stream.of("unknown", (String) null)
                .flatMap(version -> Stream.of("ABS(amount)", "COUNT(*)").map(expression -> DynamicTest.dynamicTest(datasource + ": " + version + ": " + expression, () -> {
                    // These latest enum families have no entries in the current function catalogs.
                    Assert.assertEquals(datasource.equals("db2") || datasource.equals("starrocks"), functionRequests(datasource, expression, version).stream()
                        .anyMatch(request -> request.authKind() == SecDataAuthKind.PROGRAM));
                }))));
    }

    @TestFactory
    public Stream<DynamicTest> quotedBuiltInNamesRetainFunctionPermissions() {
        Stream<DynamicTest> baseline = Stream
            .of("postgres", "oracle", "yashandb_oracle", "sqlserver", "db2", "hana", "gauss", "ob4ora", "hologres", "cockroachdb", "greenplum", "cloudberry")
            .map(datasource -> DynamicTest.dynamicTest(datasource, () -> {
                Assert.assertTrue(functionRequests(datasource, "\"LOWER\"(name)").stream().anyMatch(request -> request.authKind() == SecDataAuthKind.PROGRAM));
            }));
        Map<String, String> products = Map
            .of("hologres", "\"MIN_BY\"(name,amount)", "cockroachdb", "\"fnv32\"(name)", "greenplum", "\"linear_interpolate\"(5,0,0,10,10)", "cloudberry", "\"sm3\"(name)", "ob4my", "`TIME_TO_USEC`(name)", "adb", "`RB_CARDINALITY`(name)", "por4x", "`TO_MONTHS`(name)");
        return Stream.concat(baseline, products.entrySet().stream().map(entry -> DynamicTest.dynamicTest(entry.getKey() + ": " + entry.getValue(), () -> {
            Assert.assertTrue(functionRequests(entry.getKey(), entry.getValue()).stream().anyMatch(request -> request.authKind() == SecDataAuthKind.PROGRAM));
        })));
    }

    @Test
    public void typeConversionsKeepNestedUserFunctionPermissions() throws Exception {
        for (String version : List.of("2.0.0", "3.0.0", "4.0.0", "unknown")) {
            List<BehaviorRequest> requests = functionRequests("doris", "CAST(amount AS VARCHAR(30))", version);
            Assert.assertTrue(requests.stream().anyMatch(request -> request.authKind() == SecDataAuthKind.READ));
            Assert.assertTrue(requests.stream().noneMatch(request -> request.resource().getObjectType() == TargetType.Function));
            Assert.assertTrue(functionRequests("doris", "CAST(codex_user_func(amount) AS VARCHAR(30))", version).stream()
                .anyMatch(request -> request.authKind() == SecDataAuthKind.PROGRAM));
            Assert.assertTrue(functionRequests("doris", "`CAST`(amount)", version).stream().anyMatch(request -> request.authKind() == SecDataAuthKind.PROGRAM));
        }

        List<BehaviorRequest> converted = functionRequests("sqlserver", "CONVERT(varchar(30), amount)", "16.0.1000.6");
        List<BehaviorRequest> calls = converted.stream().filter(request -> request.resource().getObjectType() == TargetType.Function).toList();
        Assert.assertTrue(converted.stream().anyMatch(request -> request.authKind() == SecDataAuthKind.READ));
        Assert.assertEquals(1, calls.size());
        Assert.assertEquals("CONVERT", calls.get(0).resource().getObjectName().getObjectName());
        Assert.assertNull(calls.get(0).resource().getObjectName().getSchema());
        Assert.assertNull(calls.get(0).authKind());
        Assert.assertTrue(functionRequests("sqlserver", "CONVERT(varchar(30), dbo.codex_user_func(amount))", "16.0.1000.6").stream()
            .anyMatch(request -> request.authKind() == SecDataAuthKind.PROGRAM));
        Assert.assertTrue(functionRequests("sqlserver", "[CONVERT](amount)", "16.0.1000.6").stream().anyMatch(request -> request.authKind() == SecDataAuthKind.PROGRAM));
        Assert.assertTrue(functionRequests("sqlserver", "CONVERT(varchar(30), amount)", "unknown").stream().anyMatch(request -> request.authKind() == SecDataAuthKind.PROGRAM));
    }

    @Test
    public void namedVersionIntroductionBoundariesAreEnforced() throws Exception {
        var pg = new PgSysObjectRegistrySpi();
        Assert.assertTrue(pg.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, "pg_catalog", "abs", "11.3"));
        for (String expression : List.of("COUNT(*)", "SUM(amount)", "LOWER(name)", "ABS(amount)", "ROW_NUMBER() OVER (ORDER BY name)")) {
            Assert.assertTrue(expression, functionRequests("postgres", expression, "11.3").stream().noneMatch(request -> request.authKind() == SecDataAuthKind.PROGRAM));
        }

        for (String name : List.of("date_bin", "pg_stat_reset", "pg_start_backup", "pg_stop_backup", "nextval")) {
            Assert.assertFalse(name, pg.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, name, "11.3"));
        }

        for (String version : List.of("11.3", "12.0", "13.0", "14.0")) {
            for (String name : List.of("pg_start_backup", "pg_stop_backup")) {
                Assert.assertFalse(name + ": " + version, pg.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, name, version));
            }
        }

        var greenplum = new GpSysObjectRegistrySpi();
        String greenplumVersion = "12.12 (Greenplum Database 7.1.0 build commit:test)";
        Assert.assertTrue(greenplum.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "linear_interpolate", greenplumVersion));
        Assert.assertTrue(greenplum.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "abs", greenplumVersion));
        Assert.assertTrue(greenplum.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "linear_interpolate", "12.12"));
        Assert.assertTrue(greenplum.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "linear_interpolate", "unknown"));
        var cockroach = new CrdbSysObjectRegistrySpi();
        Assert.assertTrue(cockroach.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "fnv32", "13.0.0 (CockroachDB CCL v24.3.0)"));
        Assert.assertTrue(cockroach.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "abs", "13.0.0 (CockroachDB CCL v24.3.0)"));
        Assert.assertTrue(cockroach.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "fnv32", "13.0.0"));
        Assert.assertTrue(cockroach.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "fnv32", "13.0.0 (CockroachDB CCL v25.1.0)"));
        Assert.assertTrue(cockroach.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "abs", "13.0.0"));

        Assert.assertTrue(pg.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "abs", "unknown"));
        Assert.assertTrue(pg.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "abs", "99.0"));
        var dameng = new DmSysObjectRegistrySpi();
        Assert.assertTrue(dameng.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "ABS", "DM Database Server x64 V8"));
        Assert.assertTrue(dameng.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "ABS", "DM Database Server 64 V8 --08134283904"));
        Assert.assertTrue(dameng.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "ABS", "DM Database Server x64 V9"));
        Assert.assertTrue(dameng.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "ABS", "unknown"));
        Assert.assertFalse(pg.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "date_bin", "13.0"));
        Assert.assertTrue(pg.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "date_bin", "14.0"));
        var sqlserver = new MsSysObjectRegistrySpi();
        Assert.assertFalse(sqlserver.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "DATETRUNC", "15.0.0"));
        Assert.assertTrue(sqlserver.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "DATETRUNC", "16.0.0"));
        Assert.assertTrue(functionRequests("sqlserver", "COUNT(*)", "Microsoft SQL Server 2022 (RTM-CU22) ").stream()
            .noneMatch(request -> request.authKind() == SecDataAuthKind.PROGRAM));
        Assert.assertTrue(sqlserver.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "DATETRUNC", "Microsoft SQL Server 2022 (RTM"));
        Assert.assertFalse(sqlserver.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "DATETRUNC", "Microsoft SQL Server 2019 (RTM"));
        Assert.assertTrue(sqlserver.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "COUNT", "Microsoft SQL Server unknown"));
        Assert.assertTrue(sqlserver.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "COUNT", "Microsoft SQL Server 2099"));
        Assert.assertTrue(functionRequests("oracle", "JSON_SERIALIZE('{}')", "18.0.0").stream().anyMatch(request -> request.authKind() == SecDataAuthKind.PROGRAM));
        Assert.assertTrue(functionRequests("oracle", "JSON_SERIALIZE('{}')", "19.0.0").stream().noneMatch(request -> request.authKind() == SecDataAuthKind.PROGRAM));
        for (String version : List.of("11.2.0.4", "12.2.0.1", "18.0.0", "19.0.0", "21.0.0")) {
            for (String expression : List.of("ROUND(ABS(amount),1)", "TO_CHAR(DATE '2024-01-01','YYYY-MM-DD')")) {
                Assert.assertTrue(expression + ": " + version, functionRequests("oracle", expression, version).stream()
                    .noneMatch(request -> request.authKind() == SecDataAuthKind.PROGRAM));
            }
        }

        for (String expression : List.of("ROUND(ABS(amount),1)", "TO_CHAR(DATE '2024-01-01','YYYY-MM-DD')")) {
            Assert.assertTrue(expression, functionRequests("oracle", expression, "unknown").stream().anyMatch(request -> request.authKind() == SecDataAuthKind.PROGRAM));
        }

        Assert.assertTrue(functionRequests("mariadb", "JSON_EXTRACT('{\"x\":1}','$.x')", "10.6.18-MariaDB").stream()
            .noneMatch(request -> request.authKind() == SecDataAuthKind.PROGRAM));
        Assert.assertTrue(functionRequests("mariadb", "JSON_EXTRACT('{\"x\":1}','$.x')", "unknown-MariaDB").stream()
            .noneMatch(request -> request.authKind() == SecDataAuthKind.PROGRAM));
        Assert.assertTrue(functionRequests("starrocks", "BITMAP_COUNT(BITMAP_UNION(TO_BITMAP(amount)))", "4.1.4-4a9848e").stream()
            .noneMatch(request -> request.authKind() == SecDataAuthKind.PROGRAM));
        Assert.assertTrue(functionRequests("starrocks", "custom_schema.BITMAP_COUNT(TO_BITMAP(amount))", "4.1.4-4a9848e").stream()
            .anyMatch(request -> request.authKind() == SecDataAuthKind.PROGRAM));
        var hologres = new HgSysObjectRegistrySpi();
        Assert.assertTrue(hologres.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "min_by", "Hologres 1.3.35"));
        Assert.assertTrue(hologres.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "min_by", "Hologres 1.3.36"));
        var adb = new AdsMySysObjectRegistrySpi();
        Assert.assertTrue(adb.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "RB_SELECT", "3.1.9.0"));
        Assert.assertTrue(adb.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "RB_SELECT", "3.1.10.0"));
        Assert.assertTrue(adb.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "RB_AND", "3.1.6.3"));
        Assert.assertTrue(adb.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "RB_AND", "3.1.6.4"));
        Assert.assertTrue(adb.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "RB_SELECT", "3.2.0.0"));
        Assert.assertTrue(adb.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "RB_SELECT", null));
        for (String version : List.of("3.2", "3.3.0.0", "unknown")) {
            Assert.assertTrue(adb.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "RB_SELECT", version));
        }

    }

    @Test
    public void functionReleaseEnumsPreserveGrammarCompatibility() {
        Assert.assertSame(AdsMyVersion.LATEST, AdsMyVersion.parse("unknown"));
        Assert.assertSame(CloudberryVersion.LATEST, CloudberryVersion.parse("unknown"));
        Assert.assertSame(CockroachDbVersion.LATEST, CockroachDbVersion.parse("unknown"));
        Assert.assertSame(GaussVersion.LATEST, GaussVersion.parse("unknown"));
        Assert.assertSame(GreenplumVersion.LATEST, GreenplumVersion.parse("unknown"));
        Assert.assertSame(HologresVersion.LATEST, HologresVersion.parse("unknown"));
        Assert.assertSame(OceanBaseVersion.LATEST, OceanBaseVersion.parse("unknown"));
        Assert.assertSame(PolarDbXVersion.LATEST, PolarDbXVersion.parse("unknown"));
        Assert.assertSame(YashanDbVersion.LATEST, YashanDbVersion.parse("unknown"));
        Assert.assertSame(MariaDbVersion.LATEST, MariaDbVersion.parse("unknown"));

        Assert.assertSame(ClickHouseVersion.CH_26, ClickHouseVersion.LATEST);
        Assert.assertSame(StarRocksVersion.SR_5, StarRocksVersion.LATEST);
        Assert.assertSame(DorisVersion.DORIS_4, DorisVersion.LATEST);
        Assert.assertSame(HanaVersion.HANA_2, HanaVersion.LATEST);
        Assert.assertSame(Db2Version.DB2_11, Db2Version.LATEST);
        Assert.assertEquals("26", ClickHouseVersion.parse("26.8.7.19").versionString());
        Assert.assertEquals("4", StarRocksVersion.parse("4.1.4").versionString());
        Assert.assertEquals("3", DorisVersion.parse("3.1.2").versionString());
        Assert.assertSame(OracleVersion.ORACLE_11, OracleVersion.parse("11.2.0.4"));
        Assert.assertSame(HanaVersion.HANA_2, HanaVersion.parse("2.00.050.00"));
        Assert.assertSame(Db2Version.DB2_10, Db2Version.parse("10.5.0"));
    }

    @TestFactory
    public Stream<DynamicTest> classifiedFunctionsCarryEverySplitType() {
        return classifiedFunctionCases().entrySet().stream().map(entry -> DynamicTest.dynamicTest(entry.getKey(), () -> {
            var engine = SqlTestSupport.sqlEngine(entry.getKey());
            String sql = entry.getValue().get(0);
            if (!sql.startsWith("SELECT ")) {
                sql = "SELECT " + sql + " FROM demo_table";
            }
            try (var scripts = engine.splitAnalysisSpi(SqlTestSupport.parserParameters(entry.getKey())).splitScriptStream(new StringReader(sql), List.of(), 1, 0)) {
                var types = scripts.flatMap(script -> script.getType().stream()).collect(Collectors.toSet());
                for (String expected : entry.getValue().get(4).split(",")) {
                    Assert.assertTrue(types.toString(), types.contains(SplitQueryType.valueOf(expected)));
                }
            }
        }));
    }

    private static Map<String, List<String>> classifiedFunctionCases() {
        Map<String, List<String>> cases = new LinkedHashMap<>();
        cases.put("mysql", List.of("GET_LOCK('x',0)", "Session", "LOCK", "MANAGE", "SESSION_LOCK"));
        cases.put("mariadb", List.of("GET_LOCK('x',0)", "Session", "LOCK", "MANAGE", "SESSION_LOCK"));
        cases.put("por4my", List.of("GET_LOCK('x',0)", "Session", "LOCK", "MANAGE", "SESSION_LOCK"));
        cases.put("goldendb", List.of("GET_LOCK('x',0)", "Session", "LOCK", "MANAGE", "SESSION_LOCK"));
        cases.put("adb", List.of("VERSION()", "Instance", "READ", "READ", "METADATA"));
        cases.put("por4x", List.of("GET_LOCK('x',0)", "Session", "LOCK", "MANAGE", "SESSION_LOCK"));
        cases.put("ob4my", List.of("GET_LOCK('x',0)", "Session", "LOCK", "MANAGE", "SESSION_LOCK"));
        cases.put("tidb", List.of("GET_LOCK('x',0)", "Session", "LOCK", "MANAGE", "SESSION_LOCK"));
        cases.put("dameng", List.of("SF_GET_PARA_VALUE(1,'MAX_SESSIONS')", "ConfigKey", "READ", "READ", "METADATA"));
        cases.put("oracle", List.of("SYS_CONTEXT('USERENV','SESSION_USER')", "ConfigKey", "READ", "READ", "METADATA"));
        cases.put("yashandb_oracle", List.of("SYS_CONTEXT('USERENV','SESSION_USER')", "ConfigKey", "READ", "READ", "METADATA"));
        cases.put("ob4ora", List.of("DBMS_LOCK.REQUEST(1)", "Session", "LOCK", "MANAGE", "SESSION_LOCK"));
        cases.put("sqlserver", List.of("SESSION_CONTEXT(N'codex_context')", "ConfigKey", "READ", "READ", "METADATA"));
        cases.put("hana", List.of("SESSION_CONTEXT('APPLICATIONUSER')", "ConfigKey", "READ", "READ", "METADATA"));
        cases.put("db2", List.of("SELECT demo_table.* FROM demo_table, TABLE(MON_GET_TABLE(NULL,NULL,-1)) AS codex_monitor", "Statistics", "READ", "READ", "METADATA,PERFORMANCE"));
        cases.put("gauss", List.of("set_config('statement_timeout','1s',false)", "ConfigKey", "CONFIGURE", "MANAGE", "SESSION_SETTING_WRITE"));
        cases.put("postgres", List.of("set_config('statement_timeout','1s',false)", "ConfigKey", "CONFIGURE", "MANAGE", "SESSION_SETTING_WRITE"));
        cases.put("por4pg", List.of("set_config('statement_timeout','1s',false)", "ConfigKey", "CONFIGURE", "MANAGE", "SESSION_SETTING_WRITE"));
        cases.put("greenplum", List.of("gp_truncate_error_log('t')", "Table", "PURGE", "MAINTAIN", "MAINTAIN_LOG,UNSAFE"));
        cases.put("cloudberry", List.of("gp_truncate_error_log('t')", "Table", "PURGE", "MAINTAIN", "MAINTAIN_LOG,UNSAFE"));
        cases.put("cockroachdb", List.of("crdb_internal.force_retry('1s'::INTERVAL)", "Function", "UNSAFE", "UNSAFE", "PROGRAM_CONTROL"));
        cases.put("hologres", List.of("apply_privileges('source_table','target_table')", "Table", "GRANT", "MANAGE", "GRANT,UNSAFE"));
        cases.put("clickhouse", List.of("getSetting('max_threads')", "ConfigKey", "READ", "READ", "METADATA,SESSION_VARIABLE_RW"));
        cases.put("starrocks", List.of("sleep(0)", "Function", "CALL", "PROGRAM", "PERFORMANCE"));
        cases.put("doris", List.of("sleep(0)", "Function", "CALL", "PROGRAM", "PERFORMANCE"));
        cases.put("maxcompute", List.of("GET_USER_ID()", "Instance", "READ", "READ", "METADATA"));
        return cases;
    }

    private static String productVersion(SysObjectRegistrySpi registry) {
        if (registry instanceof PorXSysObjectRegistrySpi) {
            return "5.7.25-TDDL-5.4.21";
        }
        if (registry instanceof AdsMySysObjectRegistrySpi) {
            return "3.1.10.0";
        }
        if (registry instanceof HgSysObjectRegistrySpi) {
            return "12.0 (Hologres 3.1.0)";
        }
        if (registry instanceof ObMySysObjectRegistrySpi) {
            return "4.3.5.0";
        }
        if (registry instanceof ObOraSysObjectRegistrySpi) {
            return "OceanBase 4.3.5.0";
        }
        if (registry instanceof GpSysObjectRegistrySpi) {
            return "12.12 (Greenplum Database 7.0.0)";
        }
        if (registry instanceof CbSysObjectRegistrySpi) {
            return "14.4 (Cloudberry Database 2.0.0)";
        }
        return "24.3.0";
    }

    private static List<BehaviorRequest> functionRequests(String datasource, String expression) throws Exception {
        String version = switch (datasource) {
            case "sqlserver" -> "16.0.1000.6";
            case "oracle" -> "19.0.0";
            case "hana" -> "2.00.050.00";
            case "db2" -> "DB2/LINUXX8664 10.5.0";
            case "gauss", "gauss_og" -> "openGauss 6.0.0";
            case "doris", "selectdb" -> "4.0.0";
            case "greenplum" -> "12.12 (Greenplum Database 7.0.0)";
            case "cloudberry" -> "14.4 (Cloudberry Database 2.0.0)";
            case "hologres" -> "12.0 (Hologres 3.1.0)";
            case "por4x" -> "5.7.25-TDDL-5.4.21";
            case "adb" -> "3.1.10.0";
            case "ob4my" -> "4.3.5.0";
            case "ob4ora" -> "OceanBase 4.3.5.0";
            case "cockroachdb" -> "24.3.0";
            case "yashandb_oracle" -> "23.4";
            case "mariadb" -> "10.6.18-MariaDB";
            case "clickhouse" -> "26.9.5.2";
            case "starrocks" -> "4.1.0";
            case "mysql", "goldendb", "por4my", "tidb" -> "8.0.46";
            case "dameng" -> "8.1";
            default -> "16.0";
        };
        return functionRequests(datasource, expression, version);
    }

    private static List<BehaviorRequest> functionRequests(String datasource, String expression, String version) throws Exception {
        SysObjectRegistrySpi registry = switch (datasource) {
            case "oracle" -> new OraSysObjectRegistrySpi();
            case "hana" -> new HanaSysObjectRegistrySpi();
            case "db2" -> new Db2SysObjectRegistrySpi();
            case "gauss", "gauss_og" -> new GaussSysObjectRegistrySpi();
            case "mysql", "mariadb", "goldendb", "por4my" -> new MySysObjectRegistrySpi();
            case "ob4my" -> new ObMySysObjectRegistrySpi();
            case "ob4ora" -> new ObOraSysObjectRegistrySpi();
            case "adb" -> new AdsMySysObjectRegistrySpi();
            case "por4x" -> new PorXSysObjectRegistrySpi();
            case "postgres", "por4pg" -> new PgSysObjectRegistrySpi();
            case "greenplum" -> new GpSysObjectRegistrySpi();
            case "cloudberry" -> new CbSysObjectRegistrySpi();
            case "cockroachdb" -> new CrdbSysObjectRegistrySpi();
            case "sqlserver" -> new MsSysObjectRegistrySpi();
            case "yashandb_oracle" -> new YashanDBOracleSysObjectRegistrySpi();
            case "doris", "selectdb" -> new DrSysObjectRegistrySpi();
            case "dameng" -> new DmSysObjectRegistrySpi();
            case "hologres" -> new HgSysObjectRegistrySpi();
            case "tidb" -> new TiSysObjectRegistrySpi();
            case "clickhouse" -> new ChSysObjectRegistrySpi();
            case "starrocks" -> new SrSysObjectRegistrySpi();
            default -> null;
        };
        var engine = SqlTestSupport.sqlEngine(datasource);
        String sql = expression;
        if (!sql.startsWith("SELECT ") && !sql.startsWith("EXPLAIN ")) {
            sql = "SELECT " + sql + " FROM demo_table";
        }
        try (var reader = new StringReader(sql);
                var stream = engine.behaviorAnalysisSpi(SqlTestSupport.parserParameters(datasource))
                    .analysisBehaviorStream(reader, Map.of(UmiTypes.Instance, "test/1", UmiTypes.Catalog, "testdb", UmiTypes.Schema, "public"), 0, 0)) {
            return stream.flatMap(statement -> BehaviorRelations.flattenResource(registry, version, statement.getRelations()).stream()).toList();
        }
    }

    private static BehaviorRequest flatten(BehaviorAction action, TargetType targetType) {
        List<BehaviorRequest> requests = BehaviorRelations.flattenResource(null, null, List.of(relation(action, targetType)));
        Assert.assertEquals(1, requests.size());
        return requests.get(0);
    }

    private static BehaviorRelation relation(BehaviorAction action, TargetType targetType) {
        BehaviorObject object = new BehaviorObject();
        object.setObjectType(targetType);
        object.setObjectPath("/test/1/");

        BehaviorRelation relation = new BehaviorRelation();
        relation.setAction(action);
        relation.setSubject(object);
        return relation;
    }
}
