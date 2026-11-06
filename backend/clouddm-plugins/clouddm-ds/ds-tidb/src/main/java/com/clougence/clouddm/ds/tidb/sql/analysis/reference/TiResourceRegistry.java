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

import static com.clougence.sql.common.registry.RegisteredResourceType.FUNCTION;
import static com.clougence.sql.common.registry.RegisteredResourceType.TABLE;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import com.clougence.clouddm.ds.tidb.sql.parser.TiDbVersion;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.sql.common.registry.FunctionBehavior;
import com.clougence.sql.common.registry.ResourceRegistryDialect;
import com.clougence.sql.common.registry.VersionedFunctionCatalogLoader;
import com.clougence.sql.common.registry.VersionedResourceRegistry;
import com.clougence.sql.mysql.analysis.reference.MySqlResourceDialect;
import com.clougence.sql.mysql.analysis.reference.MySqlResourceRegistry;
import com.clougence.sql.mysql.parser.MySqlVersion;

/** TiDB-owned registered resource facts shared by split, resource, and behavior analysis. */
public final class TiResourceRegistry {

    private static final String                               BUILT_IN_FUNCTIONS_RESOURCE = "/META-INF/clougence/tidb-built-in-functions.json";
    private static final int                                  VERSION                     = 8;
    private static final TiResourceRegistry                   INSTANCE                    = new TiResourceRegistry(MySqlResourceDialect.INSTANCE, MySqlResourceRegistry.instance());

    private final ResourceRegistryDialect                     dialect;
    private final MySqlResourceRegistry                       mysqlResources;
    private final VersionedResourceRegistry<Boolean>          resources;
    private final VersionedResourceRegistry<FunctionBehavior> analysisFunctions;

    public static TiResourceRegistry instance() {
        return INSTANCE;
    }

    TiResourceRegistry(ResourceRegistryDialect dialect, MySqlResourceRegistry mysqlResources){
        this.dialect = Objects.requireNonNull(dialect, "dialect");
        this.mysqlResources = Objects.requireNonNull(mysqlResources, "mysqlResources");
        this.resources = new VersionedResourceRegistry<>(dialect);
        this.analysisFunctions = new VersionedResourceRegistry<>(dialect);
        VersionedFunctionCatalogLoader.load(TiResourceRegistry.class, BUILT_IN_FUNCTIONS_RESOURCE, resources, Integer::parseInt);
        VersionedFunctionCatalogLoader.loadAnalysisResources(TiResourceRegistry.class, "/META-INF/clougence/tidb-analysis-resources.json", analysisFunctions, Integer::parseInt);
        registerSystemTables();
    }

    public boolean isUserDefinedFunction(String functionName, boolean qualified) {
        return qualified || dialect.isQuotedIdentifier(functionName) || !isSystemFunction(functionName);
    }

    public boolean isSystemFunction(String functionName) {
        return isSystemFunction(functionName, TiDbVersion.LATEST);
    }

    public boolean isSystemFunction(String functionName, TiDbVersion version) {
        return resources.contains(FUNCTION, Integer.parseInt(version.versionString()), functionName) || mysqlResources.isSystemFunction(functionName, MySqlVersion.LATEST);
    }

    public FunctionBehavior functionClassification(String functionName) {
        return functionClassification(functionName, TiDbVersion.LATEST);
    }

    public FunctionBehavior functionClassification(String functionName, TiDbVersion version) {
        if (dialect.isQuotedIdentifier(functionName)) {
            return FunctionBehavior.DEFAULT;
        }
        FunctionBehavior classification = analysisFunctions.find(FUNCTION, Integer.parseInt(version.versionString()), functionName).orElse(FunctionBehavior.DEFAULT);
        FunctionBehavior compatible = mysqlResources.functionClassification(functionName, MySqlVersion.LATEST.exactVersion());
        if (classification == FunctionBehavior.DEFAULT) {
            return compatible;
        }

        Set<SplitQueryType> types = new LinkedHashSet<>(compatible.statementTypes());
        types.addAll(classification.statementTypes());
        TargetType targetType = classification.targetType();
        BehaviorAction targetAction = classification.targetAction();
        if (targetType == null) {
            targetType = compatible.targetType();
            targetAction = compatible.targetAction();
        }
        return new FunctionBehavior(types,
            classification.action(),
            targetType,
            targetAction,
            classification.unsafe() || compatible.unsafe(),
            classification.metadata() || compatible.metadata(),
            classification.privileged() || compatible.privileged());
    }

    public BehaviorAction functionBehavior(String functionName, boolean qualified) {
        if (qualified) {
            return BehaviorAction.CALL;
        }
        return functionClassification(functionName).action();
    }

    public SplitQueryType functionStatementType(String functionName, boolean qualified) {
        if (isUserDefinedFunction(functionName, qualified)) {
            return SplitQueryType.CALL_PROG_OBJ;
        }
        return nativeFunctionStatementType(functionName);
    }

    public SplitQueryType nativeFunctionStatementType(String functionName) {
        return functionClassification(functionName).statementTypes().stream().findFirst().orElse(null);
    }

    public boolean isMetadataTable(String schemaName, String tableName) {
        if (schemaName == null) {
            return dialect.normalizeIdentifier("DUAL").equals(dialect.normalizeIdentifier(tableName));
        }
        if (dialect.normalizeIdentifier("information_schema").equals(dialect.normalizeIdentifier(schemaName))) {
            return true;
        }
        return resources.contains(TABLE, VERSION, schemaName, tableName);
    }

    private void registerSystemTables() {
        registerTables("mysql", "analyze_jobs", "analyze_options", "bind_info", "column_stats_usage", "columns_priv", "db", "default_roles", "expr_pushdown_blacklist", "global_grants", "global_priv", "global_variables", "help_topic", "opt_rule_blacklist", "password_history", "role_edges", "schema_index_usage", "stats_buckets", "stats_extended", "stats_feedback", "stats_fm_sketch", "stats_histograms", "stats_history", "stats_meta", "stats_meta_history", "stats_table_locked", "stats_top_n", "tables_priv", "tidb", "user");
        registerTables("sys", "schema_unused_indexes");
    }

    private void registerTables(String schema, String... tableNames) {
        for (String tableName : tableNames) {
            resources.register(TABLE, VERSION, VERSION, true, schema, tableName);
        }
    }

}
