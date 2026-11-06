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
package com.clougence.clouddm.ds.clickhouse.sql;

import com.clougence.clouddm.ds.clickhouse.sql.analysis.reference.ChResourceRegistry;
import com.clougence.clouddm.ds.clickhouse.sql.parser.ClickHouseVersion;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.sql.common.analysis.sysobj.AbstractSysObjectRegistrySpi;

/** ClickHouse system functions which do not require user-object authorization. */
public final class ChSysObjectRegistrySpi extends AbstractSysObjectRegistrySpi {

    private final ChResourceRegistry resources = ChResourceRegistry.instance();

    @Override
    public String name() {
        return ChSqlEngineSpi.NAME;
    }

    @Override
    protected boolean isRegisteredResource(BehaviorAction action, TargetType targetType, String catalog, String schema, String objectName, String databaseVersion) {
        if (action != BehaviorAction.CALL || targetType != TargetType.Function || catalog != null || schema != null) {
            return false;
        }
        ClickHouseVersion version = ClickHouseVersion.parse(databaseVersion);
        return resources.isSystemFunction(objectName, version) && resources.isOrdinaryFunction(objectName);
    }
}
