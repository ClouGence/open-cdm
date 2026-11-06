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
package com.clougence.clouddm.ds.yashandb.sql;

import java.util.Locale;

import com.clougence.clouddm.ds.yashandb.sql.analysis.reference.YashanResourceRegistry;
import com.clougence.clouddm.ds.yashandb.sql.parser.YashanDbVersion;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.sql.common.analysis.sysobj.AbstractSysObjectRegistrySpi;
import com.clougence.sql.common.registry.FunctionBehavior;
import com.clougence.sql.oracle.analysis.reference.OraResourceRegistry;
import com.clougence.sql.oracle.analysis.sysobj.OraSysObjectRegistrySpi;

public final class YashanDBOracleSysObjectRegistrySpi extends AbstractSysObjectRegistrySpi {
    private final YashanResourceRegistry  resources = YashanResourceRegistry.instance();

    private final OraSysObjectRegistrySpi oracle    = new OraSysObjectRegistrySpi();

    @Override
    protected boolean isRegisteredResource(BehaviorAction action, TargetType targetType, String catalog, String schema, String objectName, String databaseVersion) {
        FunctionBehavior classification = OraResourceRegistry.instance().functionBehavior(objectName, null);
        if (targetType == TargetType.Function && (classification.action() != BehaviorAction.CALL || classification.privileged() || classification.unsafe())) {
            return false;
        }

        YashanDbVersion release = YashanDbVersion.parse(databaseVersion);

        if (targetType != TargetType.Function) {
            return oracle.isPermissionExempt(action, targetType, catalog, schema, objectName, null);
        }

        if (databaseVersion != null && databaseVersion.toLowerCase(Locale.ROOT).startsWith("oracle database")) {
            return oracle.isPermissionExempt(action, targetType, catalog, schema, objectName, databaseVersion);
        }
        return action == BehaviorAction.CALL && catalog == null && schema == null && resources.isSystemFunction(objectName, release);
    }
}
