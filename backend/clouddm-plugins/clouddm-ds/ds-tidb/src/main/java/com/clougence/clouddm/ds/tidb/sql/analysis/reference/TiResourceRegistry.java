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

import java.util.Objects;

import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.sql.common.registry.RegisteredResourceType;
import com.clougence.sql.common.registry.ResourceRegistryDialect;
import com.clougence.sql.common.registry.VersionedResourceRegistry;
import com.clougence.sql.mysql.analysis.reference.MySqlResourceDialect;
import com.clougence.sql.mysql.analysis.reference.MySqlResourceRegistry;
import com.clougence.sql.mysql.parser.MySqlVersion;

/** TiDB-owned registered resource facts shared by split, resource, and behavior analysis. */
public final class TiResourceRegistry {

    private static final int                                VERSION  = 0;
    private static final TiResourceRegistry                 INSTANCE = new TiResourceRegistry(MySqlResourceDialect.INSTANCE, MySqlResourceRegistry.instance());

    private final ResourceRegistryDialect                   dialect;
    private final MySqlResourceRegistry                     mysqlResources;
    private final VersionedResourceRegistry<Boolean>        resources;
    private final VersionedResourceRegistry<SplitQueryType> functionStatementTypes;
    private final VersionedResourceRegistry<BehaviorAction> functionBehaviors;

    public static TiResourceRegistry instance() {
        return INSTANCE;
    }

    TiResourceRegistry(ResourceRegistryDialect dialect, MySqlResourceRegistry mysqlResources){
        this.dialect = Objects.requireNonNull(dialect, "dialect");
        this.mysqlResources = Objects.requireNonNull(mysqlResources, "mysqlResources");
        this.resources = new VersionedResourceRegistry<>(dialect);
        this.functionStatementTypes = new VersionedResourceRegistry<>(dialect);
        this.functionBehaviors = new VersionedResourceRegistry<>(dialect);
        registerFunctions();
        registerFunctionStatementTypes();
        registerFunctionBehaviors();
        registerSystemTables();
    }

    public boolean isUserDefinedFunction(String functionName, boolean qualified) {
        return qualified || dialect.isQuotedIdentifier(functionName) || !isSystemFunction(functionName);
    }

    public boolean isSystemFunction(String functionName) {
        return resources.contains(FUNCTION, VERSION, functionName) || mysqlResources.isSystemFunction(functionName, MySqlVersion.LATEST);
    }

    public BehaviorAction functionBehavior(String functionName, boolean qualified) {
        if (qualified) {
            return BehaviorAction.CALL;
        }
        return functionBehaviors.find(FUNCTION, VERSION, functionName).orElse(BehaviorAction.CALL);
    }

    public SplitQueryType functionStatementType(String functionName, boolean qualified) {
        if (isUserDefinedFunction(functionName, qualified)) {
            return SplitQueryType.CALL_PROG_OBJ;
        }
        return nativeFunctionStatementType(functionName);
    }

    public SplitQueryType nativeFunctionStatementType(String functionName) {
        return functionStatementTypes.find(FUNCTION, VERSION, functionName).orElse(null);
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

    private void registerFunctions() {
        register(resources, FUNCTION, true, "PASSWORD", "CURRENT_RESOURCE_GROUP", "FORMAT_NANO_TIME", "JSON_SUM_CRC32", "APPROX_COUNT_DISTINCT", "APPROX_PERCENTILE", "EMBED_TEXT", "LASTVAL", "NEXTVAL", "SETVAL", "SM3", "TIDB_BOUNDED_STALENESS", "TIDB_CURRENT_TSO", "TIDB_DECODE_BASE64_KEY", "TIDB_DECODE_BINARY_PLAN", "TIDB_DECODE_KEY", "TIDB_DECODE_PLAN", "TIDB_DECODE_SQL_DIGESTS", "TIDB_ENCODE_INDEX_KEY", "TIDB_ENCODE_RECORD_KEY", "TIDB_ENCODE_SQL_DIGEST", "TIDB_IS_DDL_OWNER", "TIDB_MVCC_INFO", "TIDB_PARSE_TSO", "TIDB_PARSE_TSO_LOGICAL", "TIDB_ROW_CHECKSUM", "TIDB_SHARD", "TIDB_VERSION", "TRANSLATE", "VEC_AS_TEXT", "VEC_COSINE_DISTANCE", "VEC_DIMS", "VEC_EMBED_COSINE_DISTANCE", "VEC_EMBED_L2_DISTANCE", "VEC_FROM_TEXT", "VEC_L1_DISTANCE", "VEC_L2_DISTANCE", "VEC_L2_NORM", "VEC_NEGATIVE_INNER_PRODUCT", "VITESS_HASH");
    }

    private void registerFunctionStatementTypes() {
        register(functionStatementTypes, FUNCTION, SplitQueryType.SESSION_LOCK, "GET_LOCK", "RELEASE_LOCK", "RELEASE_ALL_LOCKS");
        register(functionStatementTypes, FUNCTION, SplitQueryType.PERFORMANCE, "BENCHMARK");
        register(functionStatementTypes, FUNCTION, SplitQueryType.ADMIN_REPLICATION, "MASTER_POS_WAIT", "SOURCE_POS_WAIT", "WAIT_FOR_EXECUTED_GTID_SET", "WAIT_UNTIL_SQL_THREAD_AFTER_GTIDS");
    }

    private void registerFunctionBehaviors() {
        register(functionBehaviors, FUNCTION, BehaviorAction.LOCK, "GET_LOCK");
        register(functionBehaviors, FUNCTION, BehaviorAction.UNLOCK, "RELEASE_LOCK", "RELEASE_ALL_LOCKS");
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

    private static <T> void register(VersionedResourceRegistry<T> registry, RegisteredResourceType type, T value, String... names) {
        for (String name : names) {
            registry.register(type, VERSION, VERSION, value, name);
        }
    }
}
