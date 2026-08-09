/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.mysql.analysis.sysobj;

import static com.clougence.sql.common.registry.RegisteredResourceType.FUNCTION;
import static com.clougence.sql.common.registry.RegisteredResourceType.PROCEDURE;
import static com.clougence.sql.common.registry.RegisteredResourceType.TABLE;

import java.util.*;
import java.util.stream.Collectors;

import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementType;
import com.clougence.sql.common.registry.*;
import com.clougence.sql.mysql.parser.MySqlVersion;
import com.clougence.utils.StringUtils;

/**
 * MySQL-owned registered resource facts shared by split, resource, and behavior analysis.
 */
public final class MySqlResourceRegistry {

    private static final String                             DATABASE_RESOURCES      = "/META-INF/clougence/mysql-database-resources.xml";

    /**
     * Function-keyword tokens whose identifier/function interpretation is
     * controlled by {@code IGNORE_SPACE}. This is parser metadata, not a
     * general list of built-in functions: functions such as CURRENT_DATE may
     * legally appear without parentheses and therefore must not be included.
     */
    private static final Set<String>                        LEXER_SPECIAL_FUNCTIONS = Set
        .of("ADDDATE", "BIT_AND", "BIT_OR", "BIT_XOR", "CAST", "COUNT", "CURDATE", "CURTIME", "DATE_ADD", "DATE_SUB", "EXTRACT", "GROUP_CONCAT", "JSON_ARRAYAGG", "JSON_DUALITY_OBJECT", "JSON_OBJECTAGG", "MAX", "MID", "MIN", "NOW", "POSITION", "PI", "SESSION_USER", "STD", "STDDEV", "STDDEV_POP", "STDDEV_SAMP", "ST_COLLECT", "SUBDATE", "SUBSTR", "SUBSTRING", "SUM", "SYSDATE", "SYSTEM_USER", "TRIM", "VARIANCE", "VAR_POP", "VAR_SAMP");

    private static final MySqlResourceRegistry              INSTANCE                = new MySqlResourceRegistry(MySqlResourceDialect.INSTANCE);

    private final ResourceRegistryDialect                   dialect;
    private final List<DatabaseResource>                    resourceCatalog;
    private final VersionedResourceRegistry<Boolean>        builtInFunctions;
    private final VersionedResourceRegistry<Boolean>        systemFunctions;
    private final VersionedResourceRegistry<Boolean>        aggregateFunctions;
    private final VersionedResourceRegistry<Boolean>        systemResources;
    private final VersionedResourceRegistry<StatementType>  functionStatementTypes;
    private final VersionedResourceRegistry<BehaviorAction> functionBehaviors;

    public static MySqlResourceRegistry instance() {
        return INSTANCE;
    }

    public MySqlResourceRegistry(ResourceRegistryDialect dialect){
        this.dialect = dialect;
        this.resourceCatalog = DatabaseResourceXmlLoader.load(MySqlResourceRegistry.class, DATABASE_RESOURCES);
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

    public boolean isLexerSpecialFunction(String functionName) {
        return functionName != null && LEXER_SPECIAL_FUNCTIONS.contains(dialect.normalizeIdentifier(functionName).toUpperCase(Locale.ROOT));
    }

    public boolean isBuiltInAggregateFunction(String functionName, int exactVersion) {
        return aggregateFunctions.contains(FUNCTION, exactVersion, functionName);
    }

    public BehaviorAction functionBehavior(String functionName, int exactVersion) {
        return functionBehaviors.find(FUNCTION, exactVersion, functionName).orElse(BehaviorAction.CALL);
    }

    public StatementType functionStatementType(String functionName, MySqlVersion version, boolean hasArguments) {
        if (!isSystemFunction(functionName, version)) {
            return null;
        }
        String normalized = dialect.normalizeIdentifier(functionName);
        if (StringUtils.equalsIgnoreCase("last_insert_id", normalized) && !hasArguments) {
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

    public boolean isSystemProcedure(String schema, String object, MySqlVersion version) {
        return systemResources.contains(PROCEDURE, versionCode(version), schema, object);
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
        for (DatabaseResource entry : resourceCatalog) {
            if (entry.type() != FUNCTION || !entry.isNameMatched()) {
                continue;
            }
            String name = entry.name();
            entry.versions().forEach(version -> {
                int exactVersion = parseVersion(version).exactVersion();
                systemFunctions.register(FUNCTION, exactVersion, exactVersion, true, name);
                builtInFunctions.register(FUNCTION, exactVersion, exactVersion, true, name);
            });
        }
    }

    private void registerAggregateFunctions() {
        MySqlVersion[] supportedVersions = MySqlVersion.values();
        for (DatabaseResource entry : resourceCatalog) {
            if (entry.type() != FUNCTION || !entry.aggregate() || !entry.isNameMatched()) {
                continue;
            }
            String name = entry.name();
            for (String versionText : entry.versions()) {
                MySqlVersion version = parseVersion(versionText);
                int minimumVersion = version.exactVersion();
                if (("JSON_ARRAYAGG".equals(name) || "JSON_OBJECTAGG".equals(name)) && version == MySqlVersion.MYSQL_5_7) {
                    minimumVersion = 50722;
                } else if ("ST_COLLECT".equals(name) && version == MySqlVersion.MYSQL_8_0) {
                    minimumVersion = 80024;
                }
                int index = version.ordinal();
                int maximumVersion = Integer.MAX_VALUE;
                if (index + 1 < supportedVersions.length) {
                    maximumVersion = supportedVersions[index + 1].exactVersion() - 1;
                }
                aggregateFunctions.register(FUNCTION, minimumVersion, maximumVersion, true, name);
            }
        }
    }

    private void registerSystemResources() {
        for (DatabaseResource entry : resourceCatalog) {
            if (!entry.skipPermission() || entry.isNameMatched()) {
                continue;
            }
            String[] nameParts = entry.registrationNameParts();
            entry.versions().forEach(version -> {
                int exactVersion = parseVersion(version).exactVersion();
                systemResources.register(entry.type(), exactVersion, exactVersion, true, nameParts);
            });
        }
    }

    private void registerFunctionStatementTypes() {
        registerStatement(StatementType.SESSION_LOCK, "GET_LOCK", "RELEASE_LOCK", "RELEASE_ALL_LOCKS", "SERVICE_GET_READ_LOCKS", "SERVICE_GET_WRITE_LOCKS", "SERVICE_RELEASE_LOCKS", "VERSION_TOKENS_LOCK_SHARED", "VERSION_TOKENS_LOCK_EXCLUSIVE", "VERSION_TOKENS_UNLOCK");
        registerStatement(StatementType.ALTER_REPLICATION, "ASYNCHRONOUS_CONNECTION_FAILOVER_ADD_MANAGED", "ASYNCHRONOUS_CONNECTION_FAILOVER_ADD_SOURCE", "ASYNCHRONOUS_CONNECTION_FAILOVER_DELETE_MANAGED", "ASYNCHRONOUS_CONNECTION_FAILOVER_DELETE_SOURCE", "ASYNCHRONOUS_CONNECTION_FAILOVER_RESET", "GROUP_REPLICATION_DISABLE_MEMBER_ACTION", "GROUP_REPLICATION_ENABLE_MEMBER_ACTION", "GROUP_REPLICATION_RESET_MEMBER_ACTIONS", "GROUP_REPLICATION_SET_AS_PRIMARY", "GROUP_REPLICATION_SET_COMMUNICATION_PROTOCOL", "GROUP_REPLICATION_SET_WRITE_CONCURRENCY", "GROUP_REPLICATION_SWITCH_TO_MULTI_PRIMARY_MODE", "GROUP_REPLICATION_SWITCH_TO_SINGLE_PRIMARY_MODE");
        registerStatement(StatementType.ADMIN_REPLICATION, "MASTER_POS_WAIT", "SOURCE_POS_WAIT", "WAIT_FOR_EXECUTED_GTID_SET", "WAIT_UNTIL_SQL_THREAD_AFTER_GTIDS");
        registerStatement(StatementType.LOG_READ, "AUDIT_LOG_READ", "AUDIT_LOG_READ_BOOKMARK");
        registerStatement(StatementType.DATA_IMPORT, "LOAD_FILE");
        registerStatement(StatementType.METADATA, "OPTION_TRACKER_USAGE_GET");
        registerStatement(StatementType.ADMIN, "AUDIT_LOG_ENCRYPTION_PASSWORD_GET", "CREATE_ASYMMETRIC_PRIV_KEY", "KEYRING_KEY_FETCH", "MYSQL_FIREWALL_FLUSH_STATUS");
        registerStatement(StatementType.ADMIN_LOG, "AUDIT_API_MESSAGE_EMIT_UDF", "AUDIT_LOG_FILTER_FLUSH", "AUDIT_LOG_FILTER_REMOVE_FILTER", "AUDIT_LOG_FILTER_REMOVE_USER", "AUDIT_LOG_FILTER_SET_FILTER", "AUDIT_LOG_FILTER_SET_USER");
        registerStatement(StatementType.MAINTAIN_LOG, "AUDIT_LOG_ROTATE");
        registerStatement(StatementType.ALTER_POLICY, "FIREWALL_GROUP_DELIST", "FIREWALL_GROUP_ENLIST", "FIREWALL_GROUP_RENAME", "READ_FIREWALL_GROUPS", "READ_FIREWALL_GROUP_ALLOWLIST", "READ_FIREWALL_USERS", "READ_FIREWALL_WHITELIST", "SET_FIREWALL_GROUP_MODE", "SET_FIREWALL_MODE");
        registerStatement(StatementType.DROP_POLICY, "FIREWALL_GROUP_REMOVE");
        registerStatement(StatementType.SYSTEM_SETTING_WRITE, "KEYRING_AWS_ROTATE_CMK", "KEYRING_AWS_ROTATE_KEYS", "KEYRING_HASHICORP_UPDATE_CONFIG", "KEYRING_KEY_GENERATE", "KEYRING_KEY_REMOVE", "KEYRING_KEY_STORE", "GEN_DICTIONARY_DROP", "GEN_DICTIONARY_LOAD", "MASKING_DICTIONARIES_FLUSH", "MASKING_DICTIONARY_REMOVE", "MASKING_DICTIONARY_TERM_ADD", "MASKING_DICTIONARY_TERM_REMOVE", "LOAD_REWRITE_RULES", "REMOVE_DD_PROPERTY_KEY", "AUDIT_LOG_ENCRYPTION_PASSWORD_SET", "OPTION_TRACKER_OPTION_REGISTER", "OPTION_TRACKER_OPTION_UNREGISTER", "OPTION_TRACKER_USAGE_SET", "VERSION_TOKENS_DELETE", "VERSION_TOKENS_EDIT", "VERSION_TOKENS_SET");
        registerStatement(StatementType.SESSION_SETTING_WRITE, "MLE_SESSION_RESET", "MLE_SET_SESSION_STATE", "LAST_INSERT_ID");
        registerStatement(StatementType.PERFORMANCE, "BENCHMARK");
    }

    private void registerFunctionBehaviors() {
        register(functionBehaviors, FUNCTION, 50600, Integer.MAX_VALUE, BehaviorAction.LOCK, "GET_LOCK", "RELEASE_LOCK");
        register(functionBehaviors, FUNCTION, 50705, Integer.MAX_VALUE, BehaviorAction.LOCK, "RELEASE_ALL_LOCKS");
        register(functionBehaviors, FUNCTION, 50700, Integer.MAX_VALUE, BehaviorAction.LOCK, "SERVICE_GET_READ_LOCKS", "SERVICE_GET_WRITE_LOCKS", "SERVICE_RELEASE_LOCKS");
        register(functionBehaviors, FUNCTION, 50600, Integer.MAX_VALUE, BehaviorAction.CONFIGURE, "SET_FIREWALL_MODE");
        register(functionBehaviors, FUNCTION, 80023, Integer.MAX_VALUE, BehaviorAction.CONFIGURE, "SET_FIREWALL_GROUP_MODE");
        register(functionBehaviors, FUNCTION, 80013, Integer.MAX_VALUE, BehaviorAction.SWITCH, "GROUP_REPLICATION_SET_AS_PRIMARY", "GROUP_REPLICATION_SWITCH_TO_MULTI_PRIMARY_MODE", "GROUP_REPLICATION_SWITCH_TO_SINGLE_PRIMARY_MODE");
        register(functionBehaviors, FUNCTION, 80013, Integer.MAX_VALUE, BehaviorAction.CONFIGURE, "GROUP_REPLICATION_SET_WRITE_CONCURRENCY");
        register(functionBehaviors, FUNCTION, 80016, Integer.MAX_VALUE, BehaviorAction.CONFIGURE, "GROUP_REPLICATION_SET_COMMUNICATION_PROTOCOL");
        register(functionBehaviors, FUNCTION, 80026, Integer.MAX_VALUE, BehaviorAction.CONFIGURE, "GROUP_REPLICATION_DISABLE_MEMBER_ACTION", "GROUP_REPLICATION_ENABLE_MEMBER_ACTION");
        register(functionBehaviors, FUNCTION, 80026, Integer.MAX_VALUE, BehaviorAction.RESET, "GROUP_REPLICATION_RESET_MEMBER_ACTIONS");
        register(functionBehaviors, FUNCTION, 80022, Integer.MAX_VALUE, BehaviorAction.ALTER, "ASYNCHRONOUS_CONNECTION_FAILOVER_ADD_SOURCE", "ASYNCHRONOUS_CONNECTION_FAILOVER_DELETE_SOURCE");
        register(functionBehaviors, FUNCTION, 80023, Integer.MAX_VALUE, BehaviorAction.ALTER, "ASYNCHRONOUS_CONNECTION_FAILOVER_ADD_MANAGED", "ASYNCHRONOUS_CONNECTION_FAILOVER_DELETE_MANAGED");
        register(functionBehaviors, FUNCTION, 80027, Integer.MAX_VALUE, BehaviorAction.RESET, "ASYNCHRONOUS_CONNECTION_FAILOVER_RESET");
    }

    private void registerStatement(StatementType type, String... functionNames) {
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

    private static MySqlVersion parseVersion(String version) {
        MySqlVersion parsed = MySqlVersion.parse(version);
        if (!parsed.versionString().equals(version)) {
            throw new IllegalStateException("Unsupported MySQL database-resource version: " + version);
        }
        return parsed;
    }
}
