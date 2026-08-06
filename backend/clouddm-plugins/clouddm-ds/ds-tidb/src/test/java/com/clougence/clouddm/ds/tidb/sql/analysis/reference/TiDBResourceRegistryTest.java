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
package com.clougence.clouddm.ds.tidb.sql.analysis.reference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.clougence.clouddm.ds.tidb.sql.parser.TiDBVersion;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;

class TiDBResourceRegistryTest {

    private final TiDBResourceRegistry resources = TiDBResourceRegistry.instance();

    @Test
    void shouldRecognizeTiDBFunctionsWithoutInheritingMySqlPluginFunctions() {
        assertFalse(resources.isUserDefinedFunction("TIDB_CURRENT_TSO", false, TiDBVersion.LATEST));
        assertFalse(resources.isUserDefinedFunction("VEC_L2_DISTANCE", false, TiDBVersion.LATEST));

        assertTrue(resources.isUserDefinedFunction("AUDIT_LOG_READ", false, TiDBVersion.LATEST));
        assertTrue(resources.isUserDefinedFunction("GROUP_REPLICATION_SET_AS_PRIMARY", false, TiDBVersion.LATEST));
        assertNull(resources.functionStatementType("AUDIT_LOG_READ", TiDBVersion.LATEST, true));
        assertEquals(BehaviorAction.CALL, resources.functionBehavior("GROUP_REPLICATION_SET_AS_PRIMARY", 90000));
    }

    @Test
    void shouldRecognizeOnlySourceRegisteredTiDBMetadataObjects() {
        assertTrue(resources.isMetadataTable("information_schema", "cluster_info", TiDBVersion.LATEST));
        assertTrue(resources.isMetadataTable("metrics_schema", "tidb_qps", TiDBVersion.LATEST));
        assertTrue(resources.isMetadataTable("mysql", "tidb_ddl_job", TiDBVersion.LATEST));
        assertTrue(resources.isMetadataTable("sys", "schema_unused_indexes", TiDBVersion.LATEST));
        assertTrue(resources.isMetadataTable("workload_schema", "hist_memory_usage", TiDBVersion.LATEST));

        assertFalse(resources.isMetadataTable("information_schema", "innodb_buffer_page", TiDBVersion.LATEST));
        assertFalse(resources.isMetadataTable("performance_schema", "replication_group_members", TiDBVersion.LATEST));
    }

    @Test
    void shouldParseDirectAndServerTiDBVersionsWithoutPatternMatching() {
        assertEquals(TiDBVersion.TIDB_5, TiDBVersion.parse("v5.4.3"));
        assertEquals(TiDBVersion.TIDB_6, TiDBVersion.parse("6.5.12"));
        assertEquals(TiDBVersion.TIDB_7, TiDBVersion.parse("5.7.25-TiDB-v7.5.7"));
        assertEquals(TiDBVersion.TIDB_8, TiDBVersion.parse("TiDB Server: V8.5.7"));
        assertEquals(TiDBVersion.TIDB_9, TiDBVersion.parse("TiDB_VERSION-v9.0.0"));
        assertEquals(TiDBVersion.LATEST, TiDBVersion.parse("MySQL 8.0.36"));
    }
}
