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
package com.clougence.clouddm.ds.greenplum.sql.analysis.sysobj;

import com.clougence.clouddm.ds.greenplum.sql.GpSqlEngineSpi;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.sql.common.analysis.sysobj.AbstractSysObjectRegistrySpi;
import com.clougence.sql.postgres.analysis.reference.PgResourceRegistry;
import com.clougence.sql.postgres.parser.PostgresVersion;

/** Keep Greenplum built-in exemptions out of PostgreSQL authorization. */
public final class GpSysObjectRegistrySpi extends AbstractSysObjectRegistrySpi {

    private final PgResourceRegistry resources = PgResourceRegistry.instance();

    @Override
    public String name() {
        return GpSqlEngineSpi.NAME;
    }

    @Override
    protected boolean isRegisteredResource(BehaviorAction action, TargetType targetType, String catalog, String schema, String objectName, String databaseVersion) {
        if (targetType == TargetType.Function && action == BehaviorAction.CALL && (schema == null || "pg_catalog".equals(schema))
            && ("gp_array_agg".equals(objectName) || "median".equals(objectName) || "pivot_sum".equals(objectName))) {
            return true;
        }
        PostgresVersion version = PostgresVersion.parse(databaseVersion);
        if (targetType == TargetType.Function) {
            boolean systemFunction = schema == null ? resources.isSystemFunction(objectName, version) : resources.isSystemFunction(schema, objectName, version);
            return systemFunction && action == resources.functionBehavior(objectName, version);
        }
        return (targetType == TargetType.Table || targetType == TargetType.View) && action == BehaviorAction.READ && resources.isSystemSchema(schema);
    }
}
