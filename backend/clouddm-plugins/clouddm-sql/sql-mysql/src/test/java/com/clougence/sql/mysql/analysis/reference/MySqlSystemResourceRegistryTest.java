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
package com.clougence.sql.mysql.analysis.reference;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.sql.mysql.parser.MySqlVersion;

public class MySqlSystemResourceRegistryTest {

    @Test
    public void registryRequiresAnExactFullyQualifiedObject() {
        MySqlResourceRegistry registry = MySqlResourceRegistry.instance();
        Assertions.assertTrue(registry.isMetadataTable("information_schema", "columns", MySqlVersion.MYSQL_8_4));
        Assertions.assertTrue(registry.isMetadataTable("`INFORMATION_SCHEMA`", "`COLUMNS`", MySqlVersion.MYSQL_8_4));
        Assertions.assertTrue(registry.isMetadataTable("sys", "host_summary", MySqlVersion.MYSQL_8_4));
        Assertions.assertFalse(registry.isMetadataTable("app_schema", "columns", MySqlVersion.MYSQL_8_4));
        Assertions.assertFalse(registry.isMetadataTable("information_schema", "not_registered", MySqlVersion.MYSQL_8_4));
    }

    @Test
    public void registriesRespectMySqlVersionFamilies() {
        MySqlResourceRegistry registry = MySqlResourceRegistry.instance();
        Assertions.assertFalse(registry.isMetadataTable("information_schema", "column_statistics", MySqlVersion.MYSQL_5_7));
        Assertions.assertTrue(registry.isMetadataTable("information_schema", "column_statistics", MySqlVersion.MYSQL_8_0));
        Assertions.assertFalse(registry.isSystemFunction("sys", "format_bytes", MySqlVersion.MYSQL_5_6));
        Assertions.assertTrue(registry.isSystemFunction("sys", "format_bytes", MySqlVersion.MYSQL_5_7));

        Assertions.assertTrue(registry.isUserDefinedFunction("asynchronous_connection_failover_reset", false, MySqlVersion.MYSQL_5_7));
        Assertions.assertFalse(registry.isUserDefinedFunction("asynchronous_connection_failover_reset", false, MySqlVersion.MYSQL_8_0));
        Assertions.assertFalse(registry.isUserDefinedFunction("set_firewall_mode", false, MySqlVersion.MYSQL_5_6));
        Assertions.assertTrue(registry.isUserDefinedFunction("from_vector", false, MySqlVersion.MYSQL_8_4));
        Assertions.assertFalse(registry.isUserDefinedFunction("from_vector", false, MySqlVersion.MYSQL_9_7));
    }

    @Test
    public void splitFunctionTypesUseTheSameRegistry() {
        MySqlResourceRegistry registry = MySqlResourceRegistry.instance();

        Assertions.assertEquals(SplitQueryType.SESSION_LOCK, registry.functionStatementType("GET_LOCK", MySqlVersion.MYSQL_8_0, true));
        Assertions.assertEquals(SplitQueryType.ALTER_REPLICATION, registry.functionStatementType("GROUP_REPLICATION_SET_AS_PRIMARY", MySqlVersion.MYSQL_8_0, true));
        Assertions.assertEquals(SplitQueryType.ADMIN, registry.functionStatementType("MYSQL_FIREWALL_FLUSH_STATUS", MySqlVersion.MYSQL_8_0, false));
        Assertions.assertEquals(SplitQueryType.SYSTEM_SETTING_WRITE, registry.functionStatementType("AUDIT_LOG_ENCRYPTION_PASSWORD_SET", MySqlVersion.MYSQL_8_0, true));
        Assertions.assertEquals(SplitQueryType.SYSTEM_SETTING_WRITE, registry.functionStatementType("OPTION_TRACKER_USAGE_SET", MySqlVersion.MYSQL_9_7, true));
        Assertions.assertNull(registry.functionStatementType("LAST_INSERT_ID", MySqlVersion.MYSQL_8_0, false));
        Assertions.assertEquals(SplitQueryType.SESSION_SETTING_WRITE, registry.functionStatementType("LAST_INSERT_ID", MySqlVersion.MYSQL_8_0, true));
        Assertions.assertNull(registry.functionStatementType("score_udf", MySqlVersion.MYSQL_8_0, true));
    }

    @Test
    public void concreteDataSourceDialectCanBridgeIdentifierRules() {
        MySqlResourceRegistry registry = new MySqlResourceRegistry(MySqlResourceDialect.INSTANCE);

        Assertions.assertTrue(registry.isMetadataTable("`INFORMATION_SCHEMA`", "`COLUMNS`", MySqlVersion.MYSQL_8_4));
        Assertions.assertTrue(registry.isUserDefinedFunction("`COUNT`", false, MySqlVersion.MYSQL_8_4));
    }
}
