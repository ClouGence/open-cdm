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
package com.clougence.clouddm.ds.registry.mysql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;

import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.sql.common.registry.RegisteredResourceType;
import com.clougence.sql.mysql.analysis.sysobj.MySqlResourceRegistry;
import com.clougence.sql.mysql.parser.MySqlVersion;

public class MySqlResourceRegistryTest {

    @Test
    public void shouldRespectAggregateVersionBoundaries() {
        MySqlResourceRegistry registry = MySqlResourceRegistry.instance();

        assertTrue(registry.isBuiltInAggregateFunction("`count`", 50600));
        assertFalse(registry.isBuiltInAggregateFunction("JSON_ARRAYAGG", 50721));
        assertTrue(registry.isBuiltInAggregateFunction("json_arrayagg", 50722));
        assertFalse(registry.isBuiltInAggregateFunction("ST_COLLECT", 80023));
        assertTrue(registry.isBuiltInAggregateFunction("st_collect", 80024));
    }

    @Test
    public void shouldLoadEveryResourceGroupFromUnifiedCatalog() {
        ClassLoader loader = MySqlResourceRegistryTest.class.getClassLoader();
        assertNotNull(loader.getResource("META-INF/clougence/mysql-database-resources.xml"));

        MySqlResourceRegistry registry = MySqlResourceRegistry.instance();
        assertTrue(registry.isBuiltInFunction("ABS", MySqlVersion.MYSQL_5_6));
        assertTrue(registry.isSystemFunction("AUDIT_LOG_READ", MySqlVersion.MYSQL_8_0));
        assertTrue(registry.isMetadataTable("information_schema", "tables", MySqlVersion.MYSQL_8_4));
        assertTrue(registry.isSystemProcedure("sys", "diagnostics", MySqlVersion.MYSQL_5_7));
        assertTrue(registry.skipsPermission(RegisteredResourceType.FUNCTION, MySqlVersion.MYSQL_8_4, "ABS"));
        assertFalse(registry.skipsPermission(RegisteredResourceType.FUNCTION, MySqlVersion.MYSQL_8_4, "LOAD_FILE"));
        assertFalse(registry.skipsPermission(RegisteredResourceType.TABLE, MySqlVersion.MYSQL_8_4,
                "information_schema", "tables"));
        assertFalse(registry.skipsPermission(RegisteredResourceType.PROCEDURE, MySqlVersion.MYSQL_5_7,
                "sys", "diagnostics"));
    }

    @Test
    public void everyRegisteredAggregateMustHaveOfficialEvidence() throws IOException {
        Set<String> registered = MySqlResourceRegistry.instance().registeredAggregateFunctions(99999);

        assertEquals(loadAggregateEvidence(), registered,
                "registry and official evidence ledger must contain exactly the same normalized names");
    }

    @Test
    public void ordinaryAndAggregateFunctionsDefaultToCall() {
        MySqlResourceRegistry registry = MySqlResourceRegistry.instance();

        assertEquals(BehaviorAction.CALL, registry.functionBehavior("COUNT", 80400));
        assertEquals(BehaviorAction.CALL, registry.functionBehavior("NOW", 80400));
        assertEquals(BehaviorAction.CALL, registry.functionBehavior("score_udf", 80400));
    }

    @Test
    public void lockingFunctionsUseDocumentedOperationalActions() {
        MySqlResourceRegistry registry = MySqlResourceRegistry.instance();

        assertEquals(BehaviorAction.LOCK, registry.functionBehavior("get_lock", 50600));
        assertEquals(BehaviorAction.CALL, registry.functionBehavior("`IS_FREE_LOCK`", 50600));
        assertEquals(BehaviorAction.CALL, registry.functionBehavior("RELEASE_ALL_LOCKS", 50704));
        assertEquals(BehaviorAction.LOCK, registry.functionBehavior("RELEASE_ALL_LOCKS", 50705));

        Map<String, BehaviorAction> functions = registry.registeredFunctionBehaviors(80400);
        assertEquals(21, functions.size());
        assertEquals(BehaviorAction.LOCK, functions.get("SERVICE_GET_WRITE_LOCKS"));
        assertEquals(BehaviorAction.SWITCH, functions.get("GROUP_REPLICATION_SET_AS_PRIMARY"));
        assertEquals(BehaviorAction.RESET, functions.get("ASYNCHRONOUS_CONNECTION_FAILOVER_RESET"));
        assertEquals(BehaviorAction.CONFIGURE, functions.get("SET_FIREWALL_MODE"));
    }

    @Test
    public void everyFunctionalFunctionMustHaveOfficialEvidence() throws IOException {
        assertEquals(loadFunctionalEvidence(), MySqlResourceRegistry.instance().registeredFunctionBehaviors(99999));
    }

    private static Set<String> loadAggregateEvidence() throws IOException {
        String resource = "registry/mysql/_evidence/built-in-aggregate-functions.csv";
        InputStream input = MySqlResourceRegistryTest.class.getClassLoader().getResourceAsStream(resource);
        assertNotNull(input, "missing evidence ledger: " + resource);

        Set<String> names = new TreeSet<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
            String line;
            boolean header = true;
            while ((line = reader.readLine()) != null) {
                if (header) {
                    header = false;
                    continue;
                }
                if (!line.isBlank()) {
                    String[] columns = line.split(",", -1);
                    assertTrue(columns.length >= 6, "invalid evidence row: " + line);
                    assertTrue(columns[3].startsWith("https://dev.mysql.com/") || columns[3].startsWith("https://downloads.mysql.com/"),
                            "evidence URL must point to the database vendor website: " + line);
                    assertFalse(columns[4].isBlank(), "evidence document section is required: " + line);
                    assertFalse(columns[5].isBlank(), "evidence verification date is required: " + line);
                    assertTrue(names.add(columns[1]), "duplicate evidence name: " + columns[1]);
                }
            }
        }
        return names;
    }

    private static Map<String, BehaviorAction> loadFunctionalEvidence() throws IOException {
        String resource = "registry/mysql/_evidence/functional-functions.csv";
        InputStream input = MySqlResourceRegistryTest.class.getClassLoader().getResourceAsStream(resource);
        assertNotNull(input, "missing evidence ledger: " + resource);

        Map<String, BehaviorAction> functions = new TreeMap<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
            String line;
            boolean header = true;
            while ((line = reader.readLine()) != null) {
                if (header) {
                    header = false;
                    continue;
                }
                if (line.isBlank()) {
                    continue;
                }
                String[] columns = line.split(",", -1);
                assertTrue(columns.length >= 7, "invalid evidence row: " + line);
                assertTrue(columns[4].startsWith("https://dev.mysql.com/") || columns[4].startsWith("https://downloads.mysql.com/"),
                        "evidence URL must point to the database vendor website: " + line);
                assertFalse(columns[5].isBlank(), "evidence document section is required: " + line);
                assertFalse(columns[6].isBlank(), "evidence verification date is required: " + line);
                BehaviorAction previous = functions.put(columns[1], BehaviorAction.valueOf(columns[3]));
                assertNull(previous, "duplicate evidence name: " + columns[1]);
            }
        }
        return functions;
    }
}
