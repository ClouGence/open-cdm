/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.dameng.sql.analysis.sysobj;

import static com.clougence.sql.common.registry.RegisteredResourceType.FUNCTION;
import static com.clougence.sql.common.registry.RegisteredResourceType.PROCEDURE;
import static com.clougence.sql.common.registry.RegisteredResourceType.TABLE;
import static com.clougence.sql.common.registry.RegisteredResourceType.TYPE;

import java.util.*;

import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementType;
import com.clougence.sql.common.registry.*;

/**
 * Dameng-owned registered resource facts shared by SQL analysis implementations.
 */
public final class DmResourceRegistry {

    public static final int                                 DM8                = 8;
    private static final String                             DATABASE_RESOURCES = "/META-INF/clougence/dameng-database-resources.xml";
    private static final DmResourceRegistry                 INSTANCE           = new DmResourceRegistry(DmResourceDialect.INSTANCE);

    private final ResourceRegistryDialect                   dialect;
    private final VersionedResourceRegistry<Boolean>        resources;
    private final VersionedResourceRegistry<Boolean>        nameMatchedResources;
    private final VersionedResourceRegistry<Boolean>        aggregateFunctions;
    private final VersionedResourceRegistry<Boolean>        noParenthesesFunctions;
    private final VersionedResourceRegistry<BehaviorAction> functionBehaviors;
    private final VersionedResourceRegistry<Integer>        functionConfigArguments;
    private final VersionedResourceRegistry<StatementType>  functionTypes;

    public static DmResourceRegistry instance() {
        return INSTANCE;
    }

    DmResourceRegistry(ResourceRegistryDialect dialect){
        this.dialect = dialect;
        this.resources = new VersionedResourceRegistry<>(dialect);
        this.nameMatchedResources = new VersionedResourceRegistry<>(dialect);
        this.aggregateFunctions = new VersionedResourceRegistry<>(dialect);
        this.noParenthesesFunctions = new VersionedResourceRegistry<>(dialect);
        this.functionBehaviors = new VersionedResourceRegistry<>(dialect);
        this.functionConfigArguments = new VersionedResourceRegistry<>(dialect);
        this.functionTypes = new VersionedResourceRegistry<>(dialect);
        registerDatabaseResources();
        registerNoParenthesesFunctions();
        registerFunctionBehaviors();
    }

    public boolean isUserDefinedFunction(String functionName, boolean qualified) {
        return isUserDefinedFunction(functionName, qualified, DM8);
    }

    public boolean isUserDefinedFunction(String functionName, boolean qualified, int exactVersion) {
        return !contains(FUNCTION, exactVersion, functionName);
    }

    public boolean isBuiltInFunction(String functionName) {
        return isBuiltInFunction(functionName, DM8);
    }

    public boolean isBuiltInFunction(String functionName, int exactVersion) {
        return contains(FUNCTION, exactVersion, functionName);
    }

    public boolean isBuiltInAggregateFunction(String functionName, int exactVersion) {
        return aggregateFunctions.contains(FUNCTION, exactVersion, functionName);
    }

    public boolean isSystemFunction(String functionName, int exactVersion) {
        return contains(FUNCTION, exactVersion, functionName);
    }

    public boolean isSystemFunction(String packageName, String functionName, int exactVersion) {
        return contains(FUNCTION, exactVersion, packageName, functionName);
    }

    public boolean isSystemFunction(String catalogName, String packageName, String functionName, int exactVersion) {
        return contains(FUNCTION, exactVersion, catalogName, packageName, functionName);
    }

    public boolean isSystemProcedure(String procedureName) {
        return isSystemProcedure(procedureName, DM8);
    }

    public boolean isSystemProcedure(String procedureName, int exactVersion) {
        return contains(PROCEDURE, exactVersion, procedureName);
    }

    public boolean isSystemProcedure(String packageName, String procedureName, int exactVersion) {
        return contains(PROCEDURE, exactVersion, packageName, procedureName);
    }

    public boolean isSystemProcedure(String catalogName, String packageName, String procedureName, int exactVersion) {
        return contains(PROCEDURE, exactVersion, catalogName, packageName, procedureName);
    }

    public boolean isSystemView(String viewName) {
        return isSystemView(viewName, DM8);
    }

    public boolean isSystemView(String viewName, int exactVersion) {
        return contains(TABLE, exactVersion, viewName);
    }

    public boolean isSystemView(String schemaName, String viewName, int exactVersion) {
        return contains(TABLE, exactVersion, schemaName, viewName);
    }

    public boolean isSystemView(String catalogName, String schemaName, String viewName, int exactVersion) {
        return contains(TABLE, exactVersion, catalogName, schemaName, viewName);
    }

    public boolean isSystemType(String typeName, int exactVersion) {
        return contains(TYPE, exactVersion, typeName);
    }

    public boolean isSystemType(String packageName, String typeName, int exactVersion) {
        return contains(TYPE, exactVersion, packageName, typeName);
    }

    public boolean isSystemType(String catalogName, String packageName, String typeName, int exactVersion) {
        return contains(TYPE, exactVersion, catalogName, packageName, typeName);
    }

    public boolean skipsPermission(RegisteredResourceType type, int exactVersion, String... nameParts) {
        Optional<Boolean> exact = resources.find(type, exactVersion, nameParts);
        if (exact.isPresent()) {
            return exact.get();
        }
        return nameMatchedResources.find(type, exactVersion, nameParts[nameParts.length - 1]).orElse(false);
    }

    public boolean isNoParenthesesFunction(String packageName, String functionName) {
        return noParenthesesFunctions.contains(FUNCTION, DM8, packageName, functionName);
    }

    public boolean isNoParenthesesFunction(String functionName) {
        return noParenthesesFunctions.contains(FUNCTION, DM8, functionName);
    }

    public BehaviorAction functionBehavior(String functionName) {
        return functionBehavior(functionName, DM8);
    }

    public BehaviorAction functionBehavior(String functionName, int exactVersion) {
        return functionBehaviors.find(FUNCTION, exactVersion, functionName).orElse(BehaviorAction.CALL);
    }

    public BehaviorAction functionBehavior(String packageName, String functionName) {
        return functionBehaviors.find(FUNCTION, DM8, packageName, functionName).orElse(BehaviorAction.CALL);
    }

    public Optional<StatementType> functionType(String functionName) {
        return functionTypes.find(FUNCTION, DM8, functionName);
    }

    public Map<String, BehaviorAction> registeredFunctionBehaviors() {
        return registeredFunctionBehaviors(DM8);
    }

    public Map<String, BehaviorAction> registeredFunctionBehaviors(int exactVersion) {
        Map<String, BehaviorAction> result = new LinkedHashMap<>();
        functionBehaviors.registeredResources(FUNCTION, exactVersion).forEach((name, action) -> result.put(name.toUpperCase(Locale.ROOT), action));
        return Map.copyOf(result);
    }

    public OptionalInt functionConfigArgument(String functionName) {
        return functionConfigArgument(functionName, DM8);
    }

    public OptionalInt functionConfigArgument(String functionName, int exactVersion) {
        Optional<Integer> argument = functionConfigArguments.find(FUNCTION, exactVersion, functionName);
        if (argument.isEmpty()) {
            return OptionalInt.empty();
        }
        return OptionalInt.of(argument.get());
    }

    private void registerDatabaseResources() {
        Set<String> names = new HashSet<>();
        for (DatabaseResource resource : DatabaseResourceXmlLoader.load(DmResourceRegistry.class, DATABASE_RESOURCES)) {
            String[] nameParts = resource.registrationNameParts();
            String key = resource.type() + ":" + Arrays.stream(nameParts).map(dialect::normalizeIdentifier).reduce((left, right) -> left + "." + right).orElseThrow();
            if (!names.add(key)) {
                throw new IllegalStateException("Duplicate Dameng system resource: " + String.join(".", nameParts));
            }
            VersionedResourceRegistry<Boolean> target = resources;
            if (resource.isNameMatched()) {
                target = nameMatchedResources;
            }
            VersionedResourceRegistry<Boolean> registry = target;
            resource.versions().forEach(versionName -> {
                int version = parserVersion(versionName);
                registry.register(resource.type(), version, version, resource.skipPermission(), nameParts);
                if (resource.aggregate()) {
                    aggregateFunctions.register(resource.type(), version, version, true, resource.name());
                }
            });
        }
    }

    private static int parserVersion(String versionName) {
        int version;
        try {
            version = Integer.parseInt(versionName);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Unsupported Dameng parser version: " + versionName, e);
        }
        if (version != DM8) {
            throw new IllegalStateException("Unsupported Dameng parser version: " + versionName);
        }
        return version;
    }

    private boolean contains(com.clougence.sql.common.registry.RegisteredResourceType type, int exactVersion, String... nameParts) {
        if (resources.contains(type, exactVersion, nameParts)) {
            return true;
        }
        return nameMatchedResources.contains(type, exactVersion, nameParts[nameParts.length - 1]);
    }

    private void registerFunctionBehaviors() {
        functionBehaviors.register(FUNCTION, DM8, DM8, BehaviorAction.CHECKPOINT, "CHECKPOINT");
        functionBehaviors.register(FUNCTION, DM8, DM8, BehaviorAction.READ, "SF_GET_PARA_DOUBLE_VALUE");
        functionBehaviors.register(FUNCTION, DM8, DM8, BehaviorAction.READ, "SF_GET_PARA_STRING_VALUE");
        functionBehaviors.register(FUNCTION, DM8, DM8, BehaviorAction.READ, "SF_GET_PARA_VALUE");
        functionBehaviors.register(FUNCTION, DM8, DM8, BehaviorAction.READ, "SF_GET_SESSION_MPP_SELECT_LOCAL");
        functionBehaviors.register(FUNCTION, DM8, DM8, BehaviorAction.READ, "SF_GET_SESSION_PARA_VALUE");
        functionTypes.register(FUNCTION, DM8, DM8, StatementType.SESSION_VARIABLE_RW, "SF_GET_SESSION_PARA_VALUE");
        functionBehaviors.register(FUNCTION, DM8, DM8, BehaviorAction.ALTER, "SF_MPP_INST_ADD");
        functionBehaviors.register(FUNCTION, DM8, DM8, BehaviorAction.UNSAFE, "SF_MPP_INST_REMOVE");
        functionConfigArguments.register(FUNCTION, DM8, DM8, 1, "SF_GET_PARA_DOUBLE_VALUE");
        functionConfigArguments.register(FUNCTION, DM8, DM8, 1, "SF_GET_PARA_STRING_VALUE");
        functionConfigArguments.register(FUNCTION, DM8, DM8, 1, "SF_GET_PARA_VALUE");
        functionConfigArguments.register(FUNCTION, DM8, DM8, 0, "SF_GET_SESSION_PARA_VALUE");
        functionBehaviors.register(FUNCTION, DM8, DM8, BehaviorAction.CREATE, "DBMS_SQLTUNE", "CREATE_TUNING_TASK");
        functionBehaviors.register(FUNCTION, DM8, DM8, BehaviorAction.CALL, "DBMS_SQLTUNE", "EXECUTE_TUNING_TASK");
        functionBehaviors.register(FUNCTION, DM8, DM8, BehaviorAction.READ, "DBMS_SQLTUNE", "REPORT_SQL_MONITOR");
        functionBehaviors.register(FUNCTION, DM8, DM8, BehaviorAction.READ, "DBMS_SQLTUNE", "REPORT_SQL_MONITOR_LIST");
        functionBehaviors.register(FUNCTION, DM8, DM8, BehaviorAction.READ, "DBMS_SQLTUNE", "REPORT_TUNING_TASK");
    }

    private void registerNoParenthesesFunctions() {
        noParenthesesFunctions.register(FUNCTION, DM8, DM8, true, "CURDATE");
        noParenthesesFunctions.register(FUNCTION, DM8, DM8, true, "CURTIME");
        noParenthesesFunctions.register(FUNCTION, DM8, DM8, true, "CURRENT_DATE");
        noParenthesesFunctions.register(FUNCTION, DM8, DM8, true, "CURRENT_TIME");
        noParenthesesFunctions.register(FUNCTION, DM8, DM8, true, "CURRENT_TIMESTAMP");
        noParenthesesFunctions.register(FUNCTION, DM8, DM8, true, "DBTIMEZONE");
        noParenthesesFunctions.register(FUNCTION, DM8, DM8, true, "GETDATE");
        noParenthesesFunctions.register(FUNCTION, DM8, DM8, true, "LOCALTIME");
        noParenthesesFunctions.register(FUNCTION, DM8, DM8, true, "LOCALTIMESTAMP");
        noParenthesesFunctions.register(FUNCTION, DM8, DM8, true, "NOW");
        noParenthesesFunctions.register(FUNCTION, DM8, DM8, true, "SESSIONTIMEZONE");
        noParenthesesFunctions.register(FUNCTION, DM8, DM8, true, "SYSDATE");
        noParenthesesFunctions.register(FUNCTION, DM8, DM8, true, "SYSTIMESTAMP");
        noParenthesesFunctions.register(FUNCTION, DM8, DM8, true, "UID");
        noParenthesesFunctions.register(FUNCTION, DM8, DM8, true, "USER");
        noParenthesesFunctions.register(FUNCTION, DM8, DM8, true, "DBMS_FLASHBACK", "GET_SYSTEM_CHANGE_NUMBER");
        noParenthesesFunctions.register(FUNCTION, DM8, DM8, true, "DBMS_RANDOM", "RANDOM");
        noParenthesesFunctions.register(FUNCTION, DM8, DM8, true, "DBMS_RANDOM", "RANDOM_NORMAL");
        noParenthesesFunctions.register(FUNCTION, DM8, DM8, true, "DBMS_RANDOM", "VALUE");
        noParenthesesFunctions.register(FUNCTION, DM8, DM8, true, "DBMS_SESSION", "UNIQUE_SESSION_ID");
        noParenthesesFunctions.register(FUNCTION, DM8, DM8, true, "DBMS_SPACE", "EXTENT_SIZE_GET");
        noParenthesesFunctions.register(FUNCTION, DM8, DM8, true, "DBMS_SPACE", "PAGE_N_GET");
        noParenthesesFunctions.register(FUNCTION, DM8, DM8, true, "DBMS_SPACE", "TS_ALL_GET");
        noParenthesesFunctions.register(FUNCTION, DM8, DM8, true, "DBMS_SPACE", "TS_N_GET");
        noParenthesesFunctions.register(FUNCTION, DM8, DM8, true, "DBMS_UTILITY", "FORMAT_CALL_STACK");
        noParenthesesFunctions.register(FUNCTION, DM8, DM8, true, "DBMS_UTILITY", "FORMAT_ERROR_BACKTRACE");
        noParenthesesFunctions.register(FUNCTION, DM8, DM8, true, "DBMS_UTILITY", "FORMAT_ERROR_STACK");
        noParenthesesFunctions.register(FUNCTION, DM8, DM8, true, "DBMS_UTILITY", "GET_TIME");
        noParenthesesFunctions.register(FUNCTION, DM8, DM8, true, "DBMS_WORKLOAD_REPOSITORY", "CREATE_SNAPSHOT");
    }
}
