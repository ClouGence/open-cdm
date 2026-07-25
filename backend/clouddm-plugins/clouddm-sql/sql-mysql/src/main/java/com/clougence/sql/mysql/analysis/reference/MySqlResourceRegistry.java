/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.mysql.analysis.reference;

import static com.clougence.sql.common.registry.RegisteredResourceType.FUNCTION;
import static com.clougence.sql.common.registry.RegisteredResourceType.PROCEDURE;
import static com.clougence.sql.common.registry.RegisteredResourceType.TABLE;

import java.util.*;
import java.util.stream.Collectors;

import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.security.auth.SecDataAuthKind;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.sql.common.registry.RegisteredResourceType;
import com.clougence.sql.common.registry.ResourceRegistryDialect;
import com.clougence.sql.common.registry.VersionedResourceRegistry;
import com.clougence.sql.mysql.parser.MySqlVersion;

/**
 * MySQL-owned registered resource facts shared by split, resource, and behavior analysis.
 */
public final class MySqlResourceRegistry {

    private static final String                             BUILT_IN_FUNCTIONS_RESOURCE       = "/META-INF/clougence/mysql-built-in-functions.json";
    private static final String                             SYSTEM_LOADABLE_FUNCTION_RESOURCE = "/META-INF/clougence/mysql-system-loadable-functions.json";
    private static final String                             SYSTEM_RESOURCES                  = "/META-INF/clougence/mysql-skip-permission-resources.json";

    private static final MySqlResourceRegistry              INSTANCE                          = new MySqlResourceRegistry(MySqlResourceDialect.INSTANCE);

    private final ResourceRegistryDialect                   dialect;
    private final VersionedResourceRegistry<Boolean>        builtInFunctions;
    private final VersionedResourceRegistry<Boolean>        systemFunctions;
    private final VersionedResourceRegistry<Boolean>        aggregateFunctions;
    private final VersionedResourceRegistry<Boolean>        systemResources;
    private final VersionedResourceRegistry<SecQueryType>   functionStatementTypes;
    private final VersionedResourceRegistry<BehaviorAction> functionBehaviors;

    public static MySqlResourceRegistry instance() {
        return INSTANCE;
    }

    public MySqlResourceRegistry(ResourceRegistryDialect dialect){
        this.dialect = dialect;
        this.builtInFunctions = new VersionedResourceRegistry<>(dialect);
        this.systemFunctions = new VersionedResourceRegistry<>(dialect);
        this.aggregateFunctions = new VersionedResourceRegistry<>(dialect);
        this.systemResources = new VersionedResourceRegistry<>(dialect);
        this.functionStatementTypes = new VersionedResourceRegistry<>(dialect);
        this.functionBehaviors = new VersionedResourceRegistry<>(dialect);
        registerFunctionCatalogs();
        registerAggregateFunctions();
        registerSystemResources();
        registerFunctionStatementTypes();
        registerFunctionBehaviors();
    }

    public boolean isUserDefinedFunction(String functionName, boolean qualified) {
        return isUserDefinedFunction(functionName, qualified, MySqlVersion.LATEST);
    }

    public boolean isUserDefinedFunction(String functionName, boolean qualified, MySqlVersion version) {
        return qualified || isQuoted(functionName) || !isSystemFunction(functionName, version);
    }

    public boolean isBuiltInFunction(String functionName, MySqlVersion version) {
        return builtInFunctions.contains(FUNCTION, versionCode(version), functionName);
    }

    public boolean isSystemFunction(String functionName, MySqlVersion version) {
        return systemFunctions.contains(FUNCTION, versionCode(version), functionName);
    }

    public boolean isBuiltInAggregateFunction(String functionName, int exactVersion) {
        return aggregateFunctions.contains(FUNCTION, exactVersion, functionName);
    }

    public BehaviorAction functionBehavior(String functionName, int exactVersion) {
        return functionBehaviors.find(FUNCTION, exactVersion, functionName).orElse(BehaviorAction.CALL);
    }

    public SecQueryType functionStatementType(String functionName, MySqlVersion version, boolean hasArguments) {
        if (!isSystemFunction(functionName, version)) {
            return null;
        }
        String normalized = dialect.normalizeIdentifier(functionName);
        if ("last_insert_id".equals(normalized) && !hasArguments) {
            return null;
        }
        return functionStatementTypes.find(FUNCTION, versionCode(version), functionName).orElse(null);
    }

    public boolean isMetadataTable(String schema, String object, MySqlVersion version) {
        return systemResources.contains(TABLE, versionCode(version), schema, object);
    }

    public boolean isSystemFunction(String schema, String object, MySqlVersion version) {
        return systemResources.contains(FUNCTION, versionCode(version), schema, object);
    }

    public boolean shouldSkipPermission(SecQueryType sqlType, TargetType targetType, List<String> nodes, MySqlVersion version) {
        if (sqlType == null || targetType == null || nodes == null || nodes.size() < 2) {
            return false;
        }
        String schema = nodes.get(nodes.size() - 2);
        String object = nodes.get(nodes.size() - 1);
        if (sqlType.getAuthKind() == SecDataAuthKind.READ && targetType == TargetType.Table) {
            return systemResources.contains(TABLE, versionCode(version), schema, object);
        }
        if (sqlType == SecQueryType.CALL_PROG_OBJ && targetType == TargetType.Procedure) {
            return systemResources.contains(PROCEDURE, versionCode(version), schema, object);
        }
        if (sqlType == SecQueryType.CALL_PROG_OBJ && targetType == TargetType.Function) {
            return systemResources.contains(FUNCTION, versionCode(version), schema, object);
        }
        return false;
    }

    public Set<String> registeredAggregateFunctions(int exactVersion) {
        return aggregateFunctions.registeredResources(FUNCTION, exactVersion).keySet().stream().map(name -> name.toUpperCase(Locale.ROOT)).collect(Collectors.toUnmodifiableSet());
    }

    public Map<String, BehaviorAction> registeredFunctionBehaviors(int exactVersion) {
        Map<String, BehaviorAction> result = new LinkedHashMap<>();
        functionBehaviors.registeredResources(FUNCTION, exactVersion).forEach((name, action) -> result.put(name.toUpperCase(Locale.ROOT), action));
        return Map.copyOf(result);
    }

    private void registerFunctionCatalogs() {
        registerFunctionNames(BUILT_IN_FUNCTIONS_RESOURCE, builtInFunctions);
        registerFunctionNames(BUILT_IN_FUNCTIONS_RESOURCE, systemFunctions);
        registerFunctionNames(SYSTEM_LOADABLE_FUNCTION_RESOURCE, systemFunctions);
    }

    private void registerFunctionNames(String resource, VersionedResourceRegistry<Boolean> registry) {
        Set<String> names = new HashSet<>();
        for (MySqlRegistryResourceLoader.Entry entry : MySqlRegistryResourceLoader.load(MySqlResourceRegistry.class, resource)) {
            if (entry.type() != FUNCTION || entry.nameParts().size() != 1) {
                throw new IllegalStateException("MySQL function resource requires one FUNCTION name part: " + entry);
            }
            String name = entry.nameParts().get(0);
            if (!names.add(dialect.normalizeIdentifier(name))) {
                throw new IllegalStateException("Duplicate MySQL function resource: " + name);
            }
            entry.versions().forEach(version -> registry.register(FUNCTION, version.exactVersion(), version.exactVersion(), true, name));
        }
    }

    private void registerAggregateFunctions() {
        register(aggregateFunctions, FUNCTION, 50600, Integer.MAX_VALUE, true, "AVG", "BIT_AND", "BIT_OR", "BIT_XOR", "COUNT", "GROUP_CONCAT", "MAX", "MIN", "STD", "STDDEV", "STDDEV_POP", "STDDEV_SAMP", "SUM", "VAR_POP", "VAR_SAMP", "VARIANCE");
        register(aggregateFunctions, FUNCTION, 50722, Integer.MAX_VALUE, true, "JSON_ARRAYAGG", "JSON_OBJECTAGG");
        register(aggregateFunctions, FUNCTION, 80024, Integer.MAX_VALUE, true, "ST_COLLECT");
    }

    private void registerSystemResources() {
        for (MySqlRegistryResourceLoader.Entry entry : MySqlRegistryResourceLoader.load(MySqlResourceRegistry.class, SYSTEM_RESOURCES)) {
            if (entry.nameParts().size() != 2) {
                throw new IllegalStateException("MySQL system resource must have schema and object name parts: " + entry);
            }
            String[] nameParts = entry.nameParts().toArray(String[]::new);
            entry.versions().forEach(version -> systemResources.register(entry.type(), version.exactVersion(), version.exactVersion(), true, nameParts));
        }
    }

    private void registerFunctionStatementTypes() {
        registerStatement(SecQueryType.SESSION_LOCK, "GET_LOCK", "RELEASE_LOCK", "RELEASE_ALL_LOCKS", "SERVICE_GET_READ_LOCKS", "SERVICE_GET_WRITE_LOCKS", "SERVICE_RELEASE_LOCKS", "VERSION_TOKENS_LOCK_SHARED", "VERSION_TOKENS_LOCK_EXCLUSIVE", "VERSION_TOKENS_UNLOCK");
        registerStatement(SecQueryType.ALTER_REPLICATION, "ASYNCHRONOUS_CONNECTION_FAILOVER_ADD_MANAGED", "ASYNCHRONOUS_CONNECTION_FAILOVER_ADD_SOURCE", "ASYNCHRONOUS_CONNECTION_FAILOVER_DELETE_MANAGED", "ASYNCHRONOUS_CONNECTION_FAILOVER_DELETE_SOURCE", "ASYNCHRONOUS_CONNECTION_FAILOVER_RESET", "GROUP_REPLICATION_DISABLE_MEMBER_ACTION", "GROUP_REPLICATION_ENABLE_MEMBER_ACTION", "GROUP_REPLICATION_RESET_MEMBER_ACTIONS", "GROUP_REPLICATION_SET_AS_PRIMARY", "GROUP_REPLICATION_SET_COMMUNICATION_PROTOCOL", "GROUP_REPLICATION_SET_WRITE_CONCURRENCY", "GROUP_REPLICATION_SWITCH_TO_MULTI_PRIMARY_MODE", "GROUP_REPLICATION_SWITCH_TO_SINGLE_PRIMARY_MODE");
        registerStatement(SecQueryType.ADMIN_REPLICATION, "MASTER_POS_WAIT", "SOURCE_POS_WAIT", "WAIT_FOR_EXECUTED_GTID_SET", "WAIT_UNTIL_SQL_THREAD_AFTER_GTIDS");
        registerStatement(SecQueryType.LOG_READ, "AUDIT_LOG_READ", "AUDIT_LOG_READ_BOOKMARK");
        registerStatement(SecQueryType.DATA_IMPORT, "LOAD_FILE");
        registerStatement(SecQueryType.METADATA, "OPTION_TRACKER_USAGE_GET");
        registerStatement(SecQueryType.ADMIN, "AUDIT_LOG_ENCRYPTION_PASSWORD_GET", "CREATE_ASYMMETRIC_PRIV_KEY", "KEYRING_KEY_FETCH", "MYSQL_FIREWALL_FLUSH_STATUS");
        registerStatement(SecQueryType.ADMIN_LOG, "AUDIT_API_MESSAGE_EMIT_UDF", "AUDIT_LOG_FILTER_FLUSH", "AUDIT_LOG_FILTER_REMOVE_FILTER", "AUDIT_LOG_FILTER_REMOVE_USER", "AUDIT_LOG_FILTER_SET_FILTER", "AUDIT_LOG_FILTER_SET_USER");
        registerStatement(SecQueryType.MAINTAIN_LOG, "AUDIT_LOG_ROTATE");
        registerStatement(SecQueryType.ALTER_POLICY, "FIREWALL_GROUP_DELIST", "FIREWALL_GROUP_ENLIST", "FIREWALL_GROUP_RENAME", "READ_FIREWALL_GROUPS", "READ_FIREWALL_GROUP_ALLOWLIST", "READ_FIREWALL_USERS", "READ_FIREWALL_WHITELIST", "SET_FIREWALL_GROUP_MODE", "SET_FIREWALL_MODE");
        registerStatement(SecQueryType.DROP_POLICY, "FIREWALL_GROUP_REMOVE");
        registerStatement(SecQueryType.SYSTEM_SETTING_WRITE, "KEYRING_AWS_ROTATE_CMK", "KEYRING_AWS_ROTATE_KEYS", "KEYRING_HASHICORP_UPDATE_CONFIG", "KEYRING_KEY_GENERATE", "KEYRING_KEY_REMOVE", "KEYRING_KEY_STORE", "GEN_DICTIONARY_DROP", "GEN_DICTIONARY_LOAD", "MASKING_DICTIONARIES_FLUSH", "MASKING_DICTIONARY_REMOVE", "MASKING_DICTIONARY_TERM_ADD", "MASKING_DICTIONARY_TERM_REMOVE", "LOAD_REWRITE_RULES", "REMOVE_DD_PROPERTY_KEY", "AUDIT_LOG_ENCRYPTION_PASSWORD_SET", "OPTION_TRACKER_OPTION_REGISTER", "OPTION_TRACKER_OPTION_UNREGISTER", "VERSION_TOKENS_DELETE", "VERSION_TOKENS_EDIT", "VERSION_TOKENS_SET");
        registerStatement(SecQueryType.SESSION_SETTING_WRITE, "MLE_SESSION_RESET", "MLE_SET_SESSION_STATE", "OPTION_TRACKER_USAGE_SET", "LAST_INSERT_ID");
        registerStatement(SecQueryType.PERFORMANCE, "BENCHMARK");
    }

    private void registerFunctionBehaviors() {
        register(functionBehaviors, FUNCTION, 50600, Integer.MAX_VALUE, BehaviorAction.LOCK, "GET_LOCK", "RELEASE_LOCK");
        register(functionBehaviors, FUNCTION, 50705, Integer.MAX_VALUE, BehaviorAction.LOCK, "RELEASE_ALL_LOCKS");
        register(functionBehaviors, FUNCTION, 50700, Integer.MAX_VALUE, BehaviorAction.LOCK, "SERVICE_GET_READ_LOCKS", "SERVICE_GET_WRITE_LOCKS", "SERVICE_RELEASE_LOCKS");
        register(functionBehaviors, FUNCTION, 50600, Integer.MAX_VALUE, BehaviorAction.CONFIGURE, "SET_FIREWALL_MODE");
        register(functionBehaviors, FUNCTION, 80023, Integer.MAX_VALUE, BehaviorAction.CONFIGURE, "SET_FIREWALL_GROUP_MODE");
        register(functionBehaviors, FUNCTION, 80013, Integer.MAX_VALUE, BehaviorAction.CONFIGURE, "GROUP_REPLICATION_SET_AS_PRIMARY", "GROUP_REPLICATION_SET_WRITE_CONCURRENCY", "GROUP_REPLICATION_SWITCH_TO_MULTI_PRIMARY_MODE", "GROUP_REPLICATION_SWITCH_TO_SINGLE_PRIMARY_MODE");
        register(functionBehaviors, FUNCTION, 80016, Integer.MAX_VALUE, BehaviorAction.CONFIGURE, "GROUP_REPLICATION_SET_COMMUNICATION_PROTOCOL");
        register(functionBehaviors, FUNCTION, 80026, Integer.MAX_VALUE, BehaviorAction.CONFIGURE, "GROUP_REPLICATION_DISABLE_MEMBER_ACTION", "GROUP_REPLICATION_ENABLE_MEMBER_ACTION", "GROUP_REPLICATION_RESET_MEMBER_ACTIONS");
        register(functionBehaviors, FUNCTION, 80022, Integer.MAX_VALUE, BehaviorAction.CONFIGURE, "ASYNCHRONOUS_CONNECTION_FAILOVER_ADD_SOURCE", "ASYNCHRONOUS_CONNECTION_FAILOVER_DELETE_SOURCE");
        register(functionBehaviors, FUNCTION, 80023, Integer.MAX_VALUE, BehaviorAction.CONFIGURE, "ASYNCHRONOUS_CONNECTION_FAILOVER_ADD_MANAGED", "ASYNCHRONOUS_CONNECTION_FAILOVER_DELETE_MANAGED");
        register(functionBehaviors, FUNCTION, 80027, Integer.MAX_VALUE, BehaviorAction.CONFIGURE, "ASYNCHRONOUS_CONNECTION_FAILOVER_RESET");
    }

    private void registerStatement(SecQueryType type, String... functionNames) {
        register(functionStatementTypes, FUNCTION, 0, Integer.MAX_VALUE, type, functionNames);
    }

    private static <T> void register(VersionedResourceRegistry<T> registry, RegisteredResourceType type, int minimumVersion, int maximumVersion, T value, String... names) {
        for (String name : names) {
            registry.register(type, minimumVersion, maximumVersion, value, name);
        }
    }

    private boolean isQuoted(String identifier) {
        return MySqlResourceDialect.INSTANCE.isQuotedIdentifier(identifier);
    }

    private static int versionCode(MySqlVersion version) {
        return (version == null ? MySqlVersion.LATEST : version).exactVersion();
    }
}
