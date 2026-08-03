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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.clougence.clouddm.ds.tidb.sql.parser.TiDBVersion;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;

/** TiDB-owned function and metadata facts derived from the TiDB source tree. */
public final class TiDBResourceRegistry {

    private static final String                       BUILT_IN_FUNCTIONS = "/META-INF/clougence/tidb-built-in-functions.txt";
    private static final String                       METADATA_TABLES    = "/META-INF/clougence/tidb-metadata-tables.txt";
    private static final TiDBResourceRegistry         INSTANCE           = new TiDBResourceRegistry();
    private static final Set<String>                  AGGREGATE_FUNCTIONS = Set
        .of("APPROX_COUNT_DISTINCT", "APPROX_PERCENTILE", "AVG", "BIT_AND", "BIT_OR", "BIT_XOR", "COUNT", "GROUP_CONCAT", "GROUPING", "JSON_ARRAYAGG",
            "JSON_OBJECTAGG", "MAX", "MIN", "STD", "STDDEV", "STDDEV_POP", "STDDEV_SAMP", "ST_COLLECT", "SUM", "VAR_POP", "VAR_SAMP", "VARIANCE");
    private static final Set<String>                  SESSION_LOCK_FUNCTIONS = Set.of("GET_LOCK", "RELEASE_LOCK", "RELEASE_ALL_LOCKS",
        "SERVICE_GET_READ_LOCKS", "SERVICE_GET_WRITE_LOCKS", "SERVICE_RELEASE_LOCKS", "VERSION_TOKENS_LOCK_SHARED",
        "VERSION_TOKENS_LOCK_EXCLUSIVE", "VERSION_TOKENS_UNLOCK");
    private static final Set<String>                  ALTER_REPLICATION_FUNCTIONS = Set.of("ASYNCHRONOUS_CONNECTION_FAILOVER_ADD_MANAGED",
        "ASYNCHRONOUS_CONNECTION_FAILOVER_ADD_SOURCE", "ASYNCHRONOUS_CONNECTION_FAILOVER_DELETE_MANAGED",
        "ASYNCHRONOUS_CONNECTION_FAILOVER_DELETE_SOURCE", "ASYNCHRONOUS_CONNECTION_FAILOVER_RESET",
        "GROUP_REPLICATION_DISABLE_MEMBER_ACTION", "GROUP_REPLICATION_ENABLE_MEMBER_ACTION", "GROUP_REPLICATION_RESET_MEMBER_ACTIONS",
        "GROUP_REPLICATION_SET_AS_PRIMARY", "GROUP_REPLICATION_SET_COMMUNICATION_PROTOCOL", "GROUP_REPLICATION_SET_WRITE_CONCURRENCY",
        "GROUP_REPLICATION_SWITCH_TO_MULTI_PRIMARY_MODE", "GROUP_REPLICATION_SWITCH_TO_SINGLE_PRIMARY_MODE");
    private static final Set<String>                  ADMIN_REPLICATION_FUNCTIONS = Set.of("MASTER_POS_WAIT", "SOURCE_POS_WAIT",
        "WAIT_FOR_EXECUTED_GTID_SET", "WAIT_UNTIL_SQL_THREAD_AFTER_GTIDS");
    private static final Set<String>                  SYSTEM_SETTING_FUNCTIONS = Set.of("KEYRING_AWS_ROTATE_CMK", "KEYRING_AWS_ROTATE_KEYS",
        "KEYRING_HASHICORP_UPDATE_CONFIG", "KEYRING_KEY_GENERATE", "KEYRING_KEY_REMOVE", "KEYRING_KEY_STORE", "GEN_DICTIONARY_DROP",
        "GEN_DICTIONARY_LOAD", "MASKING_DICTIONARIES_FLUSH", "MASKING_DICTIONARY_REMOVE", "MASKING_DICTIONARY_TERM_ADD",
        "MASKING_DICTIONARY_TERM_REMOVE", "LOAD_REWRITE_RULES", "REMOVE_DD_PROPERTY_KEY", "AUDIT_LOG_ENCRYPTION_PASSWORD_SET",
        "OPTION_TRACKER_OPTION_REGISTER", "OPTION_TRACKER_OPTION_UNREGISTER", "OPTION_TRACKER_USAGE_SET", "VERSION_TOKENS_DELETE",
        "VERSION_TOKENS_EDIT", "VERSION_TOKENS_SET");
    private static final Set<String>                  SESSION_SETTING_FUNCTIONS = Set.of("MLE_SESSION_RESET", "MLE_SET_SESSION_STATE", "LAST_INSERT_ID");
    private static final Map<String, BehaviorAction> FUNCTION_ACTIONS = Map.ofEntries(
        Map.entry("GET_LOCK", BehaviorAction.LOCK), Map.entry("RELEASE_LOCK", BehaviorAction.LOCK), Map.entry("RELEASE_ALL_LOCKS", BehaviorAction.LOCK),
        Map.entry("SERVICE_GET_READ_LOCKS", BehaviorAction.LOCK), Map.entry("SERVICE_GET_WRITE_LOCKS", BehaviorAction.LOCK),
        Map.entry("SERVICE_RELEASE_LOCKS", BehaviorAction.LOCK), Map.entry("SET_FIREWALL_MODE", BehaviorAction.CONFIGURE),
        Map.entry("SET_FIREWALL_GROUP_MODE", BehaviorAction.CONFIGURE), Map.entry("GROUP_REPLICATION_SET_AS_PRIMARY", BehaviorAction.SWITCH),
        Map.entry("GROUP_REPLICATION_SWITCH_TO_MULTI_PRIMARY_MODE", BehaviorAction.SWITCH),
        Map.entry("GROUP_REPLICATION_SWITCH_TO_SINGLE_PRIMARY_MODE", BehaviorAction.SWITCH),
        Map.entry("GROUP_REPLICATION_SET_WRITE_CONCURRENCY", BehaviorAction.CONFIGURE),
        Map.entry("GROUP_REPLICATION_SET_COMMUNICATION_PROTOCOL", BehaviorAction.CONFIGURE),
        Map.entry("GROUP_REPLICATION_DISABLE_MEMBER_ACTION", BehaviorAction.CONFIGURE),
        Map.entry("GROUP_REPLICATION_ENABLE_MEMBER_ACTION", BehaviorAction.CONFIGURE),
        Map.entry("GROUP_REPLICATION_RESET_MEMBER_ACTIONS", BehaviorAction.RESET),
        Map.entry("ASYNCHRONOUS_CONNECTION_FAILOVER_ADD_SOURCE", BehaviorAction.ALTER),
        Map.entry("ASYNCHRONOUS_CONNECTION_FAILOVER_DELETE_SOURCE", BehaviorAction.ALTER),
        Map.entry("ASYNCHRONOUS_CONNECTION_FAILOVER_ADD_MANAGED", BehaviorAction.ALTER),
        Map.entry("ASYNCHRONOUS_CONNECTION_FAILOVER_DELETE_MANAGED", BehaviorAction.ALTER),
        Map.entry("ASYNCHRONOUS_CONNECTION_FAILOVER_RESET", BehaviorAction.RESET));

    private final Set<String> builtInFunctions;
    private final Set<String> metadataTables;

    private TiDBResourceRegistry(){
        this.builtInFunctions = loadNames(BUILT_IN_FUNCTIONS);
        this.metadataTables = loadNames(METADATA_TABLES);
    }

    public static TiDBResourceRegistry instance() {
        return INSTANCE;
    }

    public boolean isUserDefinedFunction(String functionName, boolean qualified, TiDBVersion version) {
        return qualified || isQuoted(functionName) || !builtInFunctions.contains(normalize(functionName));
    }

    public boolean isBuiltInAggregateFunction(String functionName, int exactVersion) {
        return AGGREGATE_FUNCTIONS.contains(normalize(functionName));
    }

    public BehaviorAction functionBehavior(String functionName, int exactVersion) {
        String normalized = normalize(functionName);
        if (!builtInFunctions.contains(normalized)) {
            return BehaviorAction.CALL;
        }
        return FUNCTION_ACTIONS.getOrDefault(normalized, BehaviorAction.CALL);
    }

    public SplitQueryType functionStatementType(String functionName, TiDBVersion version, boolean hasArguments) {
        String normalized = normalize(functionName);
        if (!builtInFunctions.contains(normalized)) {
            return null;
        }
        if ("LAST_INSERT_ID".equals(normalized) && !hasArguments) {
            return null;
        }
        if (SESSION_LOCK_FUNCTIONS.contains(normalized)) {
            return SplitQueryType.SESSION_LOCK;
        }
        if (ALTER_REPLICATION_FUNCTIONS.contains(normalized)) {
            return SplitQueryType.ALTER_REPLICATION;
        }
        if (ADMIN_REPLICATION_FUNCTIONS.contains(normalized)) {
            return SplitQueryType.ADMIN_REPLICATION;
        }
        if (SYSTEM_SETTING_FUNCTIONS.contains(normalized)) {
            return SplitQueryType.SYSTEM_SETTING_WRITE;
        }
        if (SESSION_SETTING_FUNCTIONS.contains(normalized)) {
            return SplitQueryType.SESSION_SETTING_WRITE;
        }
        return switch (normalized) {
            case "AUDIT_LOG_READ", "AUDIT_LOG_READ_BOOKMARK" -> SplitQueryType.LOG_READ;
            case "LOAD_FILE" -> SplitQueryType.DATA_IMPORT;
            case "OPTION_TRACKER_USAGE_GET" -> SplitQueryType.METADATA;
            case "AUDIT_LOG_ENCRYPTION_PASSWORD_GET", "CREATE_ASYMMETRIC_PRIV_KEY", "KEYRING_KEY_FETCH", "MYSQL_FIREWALL_FLUSH_STATUS" -> SplitQueryType.ADMIN;
            case "AUDIT_API_MESSAGE_EMIT_UDF", "AUDIT_LOG_FILTER_FLUSH", "AUDIT_LOG_FILTER_REMOVE_FILTER", "AUDIT_LOG_FILTER_REMOVE_USER",
                    "AUDIT_LOG_FILTER_SET_FILTER", "AUDIT_LOG_FILTER_SET_USER" -> SplitQueryType.ADMIN_LOG;
            case "AUDIT_LOG_ROTATE" -> SplitQueryType.MAINTAIN_LOG;
            case "FIREWALL_GROUP_DELIST", "FIREWALL_GROUP_ENLIST", "FIREWALL_GROUP_RENAME", "READ_FIREWALL_GROUPS",
                    "READ_FIREWALL_GROUP_ALLOWLIST", "READ_FIREWALL_USERS", "READ_FIREWALL_WHITELIST", "SET_FIREWALL_GROUP_MODE",
                    "SET_FIREWALL_MODE" -> SplitQueryType.ALTER_POLICY;
            case "FIREWALL_GROUP_REMOVE" -> SplitQueryType.DROP_POLICY;
            case "BENCHMARK" -> SplitQueryType.PERFORMANCE;
            default -> null;
        };
    }

    public boolean isMetadataTable(String schema, String object, TiDBVersion version) {
        return metadataTables.contains(normalize(schema) + "." + normalize(object));
    }

    private static Set<String> loadNames(String resource) {
        InputStream input = TiDBResourceRegistry.class.getResourceAsStream(resource);
        if (input == null) {
            throw new IllegalStateException("Missing TiDB resource registry: " + resource);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
            return reader.lines().map(String::strip).filter(line -> !line.isEmpty() && !line.startsWith("#")).map(TiDBResourceRegistry::normalize)
                .collect(Collectors.toUnmodifiableSet());
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read TiDB resource registry: " + resource, e);
        }
    }

    private static boolean isQuoted(String name) {
        String value = name == null ? "" : name.strip();
        return value.length() >= 2 && ((value.charAt(0) == '`' && value.charAt(value.length() - 1) == '`')
                                     || (value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"'));
    }

    private static String normalize(String value) {
        if (value == null) {
            return "";
        }
        String normalized = value.strip();
        if (isQuoted(normalized)) {
            normalized = normalized.substring(1, normalized.length() - 1);
        }
        return normalized.toUpperCase(Locale.ROOT);
    }
}
