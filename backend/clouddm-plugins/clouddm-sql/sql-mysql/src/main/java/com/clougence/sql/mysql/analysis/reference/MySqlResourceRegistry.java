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

import static com.clougence.sql.common.registry.RegisteredResourceType.*;

import java.util.*;
import java.util.stream.Collectors;

import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.sql.common.registry.*;
import com.clougence.sql.mysql.parser.MySqlParserConfig;
import com.clougence.sql.mysql.parser.MySqlVersion;
import com.clougence.utils.StringUtils;

/**
 * MySQL-owned registered resource facts shared by split, resource, and behavior analysis.
 */
public final class MySqlResourceRegistry {

    private static final String                               BUILT_IN_FUNCTIONS_RESOURCE       = "/META-INF/clougence/mysql-built-in-functions.json";
    private static final String                               SYSTEM_LOADABLE_FUNCTION_RESOURCE = "/META-INF/clougence/mysql-system-loadable-functions.json";
    private static final String                               SYSTEM_RESOURCES                  = "/META-INF/clougence/mysql-skip-permission-resources.json";

    private static final MySqlResourceRegistry                INSTANCE                          = new MySqlResourceRegistry(MySqlResourceDialect.INSTANCE);

    private final ResourceRegistryDialect                     dialect;
    private int                                               mariaDbVersion;
    private final VersionedResourceRegistry<Boolean>          builtInFunctions;
    private final VersionedResourceRegistry<Boolean>          systemFunctions;
    private final VersionedResourceRegistry<Boolean>          aggregateFunctions;
    private final VersionedResourceRegistry<Boolean>          systemResources;
    private final VersionedResourceRegistry<FunctionBehavior> analysisFunctions;

    public static MySqlResourceRegistry instance() {
        return INSTANCE;
    }

    public MySqlResourceRegistry(ResourceRegistryDialect dialect){
        this.dialect = dialect;
        this.builtInFunctions = new VersionedResourceRegistry<>(dialect);
        this.systemFunctions = new VersionedResourceRegistry<>(dialect);
        this.aggregateFunctions = new VersionedResourceRegistry<>(dialect);
        this.systemResources = new VersionedResourceRegistry<>(dialect);
        this.analysisFunctions = new VersionedResourceRegistry<>(dialect);
        registerFunctionCatalogs();
        registerAggregateFunctions();
        registerSystemResources();
        VersionedFunctionCatalogLoader
            .loadAnalysisResources(MySqlResourceRegistry.class, "/META-INF/clougence/mysql-analysis-resources.json", analysisFunctions, version -> MySqlVersion.parse(version)
                .exactVersion());
    }

    public MySqlResourceRegistry(MySqlParserConfig config){
        this(MySqlResourceDialect.INSTANCE);
        this.mariaDbVersion = config.exactVersion();
        int version = config.grammarVersion().exactVersion();
        for (String name : MariaDbResourceRegistry.instance().functions(config.exactVersion())) {
            builtInFunctions.register(FUNCTION, version, version, true, name);
            systemFunctions.register(FUNCTION, version, version, true, name);
        }
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

    public FunctionBehavior functionClassification(String functionName, int exactVersion) {
        if (isQuoted(functionName)) {
            return FunctionBehavior.DEFAULT;
        }
        if (mariaDbVersion > 0) {
            return MariaDbResourceRegistry.instance().functionClassification(functionName, mariaDbVersion);
        }
        return analysisFunctions.find(FUNCTION, exactVersion / 100 * 100, functionName).orElse(FunctionBehavior.DEFAULT);
    }

    public BehaviorAction functionBehavior(String functionName, int exactVersion) {
        return functionClassification(functionName, exactVersion).action();
    }

    public SplitQueryType functionStatementType(String functionName, MySqlVersion version, boolean hasArguments) {
        if (!isSystemFunction(functionName, version)) {
            return null;
        }
        String normalized = dialect.normalizeIdentifier(functionName);
        if (StringUtils.equalsIgnoreCase("last_insert_id", normalized) && !hasArguments) {
            return null;
        }
        return functionClassification(functionName, versionCode(version)).statementTypes().stream().findFirst().orElse(null);
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
        analysisFunctions.registeredResources(FUNCTION, exactVersion / 100 * 100).forEach((name, facts) -> {
            if (facts.action() != BehaviorAction.CALL) {
                result.put(name.toUpperCase(Locale.ROOT), facts.action());
            }
        });
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
