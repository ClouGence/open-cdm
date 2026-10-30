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
package com.clougence.clouddm.ds.hologres;

import static org.junit.jupiter.api.Assertions.*;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.ds.SqlTestSupport;
import com.clougence.clouddm.ds.hologres.dsconf.HgConfig;
import com.clougence.clouddm.ds.hologres.dsconf.HgConfigSpi;
import com.clougence.clouddm.ds.hologres.sql.HgSqlEngineSpi;
import com.clougence.clouddm.ds.hologres.sql.analysis.HgSysObjectRegistrySpi;
import com.clougence.clouddm.sdk.service.secrules.RuleQueryType;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementBehavior;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.RdbGrantDomain;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.RdbResourceDomain;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.RdbRevokeDomain;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.postgres.PgSqlEngineSpi;

public class HologresDclTest {
    private final HgSqlEngineSpi        engine     = new HgSqlEngineSpi(SqlTestSupport.metaService());
    private final SqlParserParameters   parameters = SqlParserParameters.empty().put(SqlParserParameters.VERSION, "11.3");
    private final Map<UmiTypes, Object> levels     = Map.of(UmiTypes.Instance, "/test/1", UmiTypes.Catalog, "catalog1", UmiTypes.Schema, "schema1");

    @TestFactory
    Stream<DynamicTest> membershipCombinations() {
        List<DynamicTest> tests = new ArrayList<>();
        for (String model : List.of("spm", "slpm")) {
            for (String operation : List.of("grant", "revoke", "create_user")) {
                for (String prefix : List.of("", "hologres.", "\"hologres\".")) {
                    for (String user : List.of("p4_test_user", "RAM$test:reader", "\"test@example.com\"", "o'brien")) {
                        String role = "catalog1_viewer";
                        if (model.equals("slpm")) {
                            role = "catalog1.schema1.viewer";
                        }

                        String args = "'" + role + "', '" + user.replace("'", "''") + "'";
                        if (operation.equals("create_user")) {
                            args = "'" + user.replace("'", "''") + "', '" + role + "'";
                        }

                        String sql = "CALL " + prefix + model + "_" + operation + "(" + args + ")";
                        String expectedRole = role;
                        tests.add(DynamicTest.dynamicTest(sql, () -> {
                            var behavior = behavior(sql);
                            int index = 0;
                            if (operation.equals("create_user")) {
                                assertEquals(SplitQueryType.CREATE_USER, behavior.getStatementType());
                                assertEquals(BehaviorAction.CREATE, behavior.getRelations().get(0).getAction());
                                assertEquals("/test/1/" + user + "/", behavior.getRelations().get(0).getSubject().getObjectPath());
                                index = 1;
                            }

                            var membership = behavior.getRelations().get(index);
                            BehaviorAction action = BehaviorAction.GRANT;
                            RuleQueryType rule = RuleQueryType.GRANT;
                            if (operation.equals("revoke")) {
                                action = BehaviorAction.REVOKE;
                                rule = RuleQueryType.REVOKE;
                            }

                            assertEquals(action, membership.getAction());
                            assertEquals(TargetType.Role, membership.getSubject().getObjectType());
                            assertEquals("/test/1/" + expectedRole + "/", membership.getSubject().getObjectPath());
                            assertEquals("/test/1/" + user + "/", membership.getTarget().get(0).getObjectPath());
                            try (var domains = engine.secDomainResolveSpi(parameters).resolveDomainStream(DataSourceType.Hologres, new StringReader(sql), 1, 0, null)) {
                                var domain = assertInstanceOf(RdbResourceDomain.class, domains.toList().get(index));
                                assertEquals(rule, domain.getSqlType());
                                assertEquals(user, domain.getName());
                            }
                        }));
                    }
                }
            }
        }
        return tests.stream();
    }

    @TestFactory
    Stream<DynamicTest> expertDclDomains() {
        List<DynamicTest> tests = new ArrayList<>();
        for (String sql : List
            .of("GRANT SELECT ON TABLE schema1.table1 TO reader", "REVOKE SELECT ON TABLE schema1.table1 FROM reader", "GRANT reader TO app_user WITH ADMIN OPTION", "REVOKE ADMIN OPTION FOR reader FROM app_user CASCADE", "ALTER DEFAULT PRIVILEGES FOR ROLE owner IN SCHEMA schema1 GRANT SELECT ON TABLES TO reader", "ALTER DEFAULT PRIVILEGES FOR ROLE owner IN SCHEMA schema1 REVOKE SELECT ON TABLES FROM reader", "ALTER ROLE app_user NOLOGIN", "ALTER USER app_user NOSUPERUSER")) {
            tests.add(DynamicTest.dynamicTest(sql, () -> {
                assertFalse(behavior(sql).getRelations().isEmpty());
                try (var stream = engine.secDomainResolveSpi(parameters).resolveDomainStream(DataSourceType.Hologres, new StringReader(sql), 1, 0, null)) {
                    var domains = stream.toList();
                    assertEquals(1, domains.size());
                    var domain = domains.get(0);
                    RuleQueryType expectedType = RuleQueryType.GRANT;
                    if (sql.contains("REVOKE")) {
                        expectedType = RuleQueryType.REVOKE;
                    } else if (sql.startsWith("ALTER ROLE") || sql.startsWith("ALTER USER")) {
                        expectedType = RuleQueryType.ALTER_USER;
                    }

                    assertEquals(expectedType, domain.getSqlType());
                    String expectedName = "reader";
                    if (!sql.contains(" ON ") && !sql.startsWith("ALTER DEFAULT")) {
                        expectedName = "app_user";
                    }

                    if (domain instanceof RdbGrantDomain grant) {
                        assertEquals(expectedName, grant.getName());
                    } else if (domain instanceof RdbRevokeDomain revoke) {
                        assertEquals(expectedName, revoke.getName());
                    } else {
                        assertEquals(expectedName, assertInstanceOf(RdbResourceDomain.class, domain).getName());
                    }
                }
            }));
        }
        return tests.stream();
    }

    @Test
    void dynamicPrincipalsAndModelChangesRemainUnsafe() {
        for (String sql : List
            .of("CALL spm_grant('db_viewer', current_user)", "CALL slpm_grant('db.public.viewer', 'p4_' || 'test')", "CALL spm_enable()", "CALL slpm_migrate(64)", "CALL spm_cleanup('db')", "CALL apply_privileges('t1', 't2')", "CALL apply_privileges('t1', 't2', false)", "CALL slpm_rename_schema('old', 'new')")) {
            assertEquals(BehaviorAction.UNSAFE, behavior(sql).getRelations().get(0).getAction());
            try (var domains = engine.secDomainResolveSpi(parameters).resolveDomainStream(DataSourceType.Hologres, new StringReader(sql), 1, 0, null)) {
                assertEquals(RuleQueryType.UNSAFE, domains.toList().get(0).getSqlType());
            }
        }
    }

    @Test
    void userProceduresAndPostgresKeepTheirOriginalMeaning() {
        for (String name : List.of("app.spm_grant", "\"SPM_GRANT\"", "\"hologres.spm_grant\"")) {
            var behavior = behavior("CALL " + name + "('db_viewer', 'test')");
            assertEquals(SplitQueryType.CALL_PROG_OBJ, behavior.getStatementType());
            assertEquals(BehaviorAction.CALL, behavior.getRelations().get(0).getAction());
        }

        var postgres = new PgSqlEngineSpi(SqlTestSupport.metaService());
        try (var stream = postgres.behaviorAnalysisSpi(parameters).analysisBehaviorStream(new StringReader("CALL spm_grant('db_viewer', 'test')"), levels, 1, 0)) {
            assertEquals(SplitQueryType.CALL_PROG_OBJ, stream.toList().get(0).getStatementType());
        }

        var registry = new HgSysObjectRegistrySpi();
        assertFalse(registry.isPermissionExempt(BehaviorAction.CALL, TargetType.Procedure, null, "hologres", "spm_grant", "11.3"));
        assertTrue(registry.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, "pg_catalog", "abs", "11.3"));
        assertFalse(registry.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, "app", "abs", "11.3"));
    }

    @TestFactory
    Stream<DynamicTest> malformedDclIsRejected() {
        return List.of("GRANT SELECT ON TABLE t", "REVOKE SELECT ON TABLE t TO reader", "CALL spm_grant('role',)", "ALTER DEFAULT PRIVILEGES GRANT SELECT TABLES TO reader")
            .stream()
            .map(sql -> DynamicTest.dynamicTest(sql, () -> assertThrows(RuntimeException.class, () -> {
                try (var stream = engine.splitAnalysisSpi(parameters).splitScriptStream(new StringReader(sql), List.of(), 1, 0)) {
                    stream.toList();
                }
            })));
    }

    @Test
    void storedPostgresEngineUsesHologresAnalysis() {
        HgConfig config = new HgConfig();
        config.setSqlEngine("PG SQL");
        HgConfigSpi spi = new HgConfigSpi();
        spi.fillConfig(config, Map.of());
        assertEquals(HgSqlEngineSpi.NAME, config.getSqlEngine());
        Map<String, String> ui = new HashMap<>();
        spi.customizeUiMap(ui, Map.of("sqlEngine", "PG SQL"));
        assertEquals(HgSqlEngineSpi.NAME, ui.get("sqlEngine"));
    }

    private StatementBehavior behavior(String sql) {
        try (var stream = engine.behaviorAnalysisSpi(parameters).analysisBehaviorStream(new StringReader(sql), levels, 1, 0)) {
            return stream.toList().get(0);
        }
    }
}
