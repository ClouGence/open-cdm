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
package com.clougence.clouddm.ds.tidb.sql.analysis.sysobj;

import java.util.Locale;
import java.util.Set;

import com.clougence.clouddm.ds.tidb.sql.TiSqlEngineSpi;
import com.clougence.clouddm.ds.tidb.sql.analysis.reference.TiFunctionRegistry;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.sql.common.analysis.sysobj.AbstractSysObjectRegistrySpi;

/** TiDB system objects which do not require user-object authorization. */
public final class TiSysObjectRegistrySpi extends AbstractSysObjectRegistrySpi {

    private static final Set<String> MYSQL_SYSTEM_TABLES = Set.of(
        "analyze_jobs",
        "analyze_options",
        "bind_info",
        "column_stats_usage",
        "columns_priv",
        "db",
        "default_roles",
        "expr_pushdown_blacklist",
        "global_grants",
        "global_priv",
        "global_variables",
        "help_topic",
        "opt_rule_blacklist",
        "password_history",
        "role_edges",
        "schema_index_usage",
        "stats_buckets",
        "stats_extended",
        "stats_feedback",
        "stats_fm_sketch",
        "stats_histograms",
        "stats_history",
        "stats_meta",
        "stats_meta_history",
        "stats_table_locked",
        "stats_top_n",
        "tables_priv",
        "tidb",
        "user");

    @Override
    public String name() {
        return TiSqlEngineSpi.NAME;
    }

    @Override
    protected boolean isRegisteredResource(BehaviorAction action, TargetType targetType, String catalog, String schema, String objectName, String databaseVersion) {
        if (targetType == TargetType.Function && schema == null) {
            BehaviorAction expectedAction = TiFunctionRegistry.behavior(objectName, false);
            return action == expectedAction && !TiFunctionRegistry.isUserDefined(objectName, false);
        }
        if ((targetType == TargetType.Table || targetType == TargetType.View) && action == BehaviorAction.READ) {
            return schema == null && "DUAL".equalsIgnoreCase(objectName) || schema != null && isMetadataTable(schema, objectName);
        }
        return false;
    }

    public boolean isMetadataTable(String schemaName, String tableName) {
        String schema = schemaName.toLowerCase(Locale.ROOT);
        String table = tableName.toLowerCase(Locale.ROOT);
        if ("information_schema".equals(schema)) {
            return true;
        }
        if ("mysql".equals(schema)) {
            return MYSQL_SYSTEM_TABLES.contains(table);
        }
        return "sys".equals(schema) && "schema_unused_indexes".equals(table);
    }
}
