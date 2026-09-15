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
package com.clougence.sql.postgres.analysis.reference;

import static com.clougence.sql.common.registry.RegisteredResourceType.FUNCTION;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.sql.common.registry.VersionedResourceRegistry;
import com.clougence.sql.postgres.parser.PostgresVersion;

/** PostgreSQL-owned resource facts shared by split and behavior analysis. */
public final class PgResourceRegistry {

    private static final String                                  ANALYSIS_RESOURCES     = "/META-INF/clougence/postgres-analysis-resources.json";
    private static final String                                  BUILT_IN_FUNCTIONS     = "/META-INF/clougence/postgres-built-in-functions.json";
    private static final PgResourceRegistry                      INSTANCE               = new PgResourceRegistry();

    private final VersionedResourceRegistry<Boolean>             builtInFunctions       = new VersionedResourceRegistry<>(PgResourceDialect.INSTANCE);
    private final VersionedResourceRegistry<Boolean>             metadataFunctions      = new VersionedResourceRegistry<>(PgResourceDialect.INSTANCE);
    private final VersionedResourceRegistry<Set<SplitQueryType>> functionStatementTypes = new VersionedResourceRegistry<>(PgResourceDialect.INSTANCE);
    private final VersionedResourceRegistry<PgFunctionBehavior>  functionBehaviors      = new VersionedResourceRegistry<>(PgResourceDialect.INSTANCE);
    private final Set<String>                                    performanceRelations;
    private final Set<String>                                    systemSchemas;

    public static PgResourceRegistry instance() {
        return INSTANCE;
    }

    private PgResourceRegistry(){
        PgRegistryResourceLoader.loadBuiltInFunctions(PgResourceRegistry.class, BUILT_IN_FUNCTIONS, builtInFunctions);
        Set<String> registeredPerformanceRelations = new HashSet<>();
        Set<String> registeredSystemSchemas = new HashSet<>();
        PgRegistryResourceLoader.loadAnalysisResources(PgResourceRegistry.class, ANALYSIS_RESOURCES, metadataFunctions,
            functionStatementTypes, functionBehaviors, registeredPerformanceRelations, registeredSystemSchemas);
        performanceRelations = Set.copyOf(registeredPerformanceRelations);
        systemSchemas = Set.copyOf(registeredSystemSchemas);
    }

    public String normalizeIdentifier(String identifier) {
        return PgResourceDialect.INSTANCE.normalizeIdentifier(identifier);
    }

    public boolean isSystemFunction(String functionName, PostgresVersion version) {
        return isFoldedIdentifier(functionName) && builtInFunctions.contains(FUNCTION, versionCode(version), functionName);
    }

    public boolean isSystemFunction(String schema, String functionName, PostgresVersion version) {
        return "pg_catalog".equals(normalizeIdentifier(schema)) && isSystemFunction(functionName, version);
    }

    public boolean isMetadataFunction(String functionName, PostgresVersion version) {
        return isFoldedIdentifier(functionName) && metadataFunctions.contains(FUNCTION, versionCode(version), functionName);
    }

    public Set<SplitQueryType> functionStatementTypes(String functionName, PostgresVersion version) {
        if (!isFoldedIdentifier(functionName)) {
            return Set.of();
        }
        return functionStatementTypes.find(FUNCTION, versionCode(version), functionName).orElse(Set.of());
    }

    public PgFunctionBehavior functionBehavior(List<String> names, PostgresVersion version) {
        if (names.isEmpty() || names.size() > 2) {
            return PgFunctionBehavior.DEFAULT;
        }
        String functionName = names.get(names.size() - 1);
        if (!isFoldedIdentifier(functionName) || names.size() == 2 && !"pg_catalog".equals(names.get(0))) {
            return PgFunctionBehavior.DEFAULT;
        }
        return functionBehaviors.find(FUNCTION, versionCode(version), functionName).orElse(PgFunctionBehavior.DEFAULT);
    }

    public BehaviorAction functionBehavior(String functionName, PostgresVersion version) {
        if (!isFoldedIdentifier(functionName)) {
            return BehaviorAction.CALL;
        }
        return functionBehaviors.find(FUNCTION, versionCode(version), functionName).orElse(PgFunctionBehavior.DEFAULT).action();
    }

    public boolean isPerformanceRelation(String relationName) {
        return performanceRelations.contains(normalizeIdentifier(relationName));
    }

    public boolean isSystemSchema(String schemaName) {
        return systemSchemas.contains(normalizeIdentifier(schemaName));
    }

    private static int versionCode(PostgresVersion version) {
        return Integer.parseInt((version == null ? PostgresVersion.LATEST : version).versionString());
    }

    private static boolean isFoldedIdentifier(String identifier) {
        for (int i = 0; i < identifier.length(); i++) {
            char value = identifier.charAt(i);
            if (value >= 'A' && value <= 'Z') {
                return false;
            }
        }
        return true;
    }
}
