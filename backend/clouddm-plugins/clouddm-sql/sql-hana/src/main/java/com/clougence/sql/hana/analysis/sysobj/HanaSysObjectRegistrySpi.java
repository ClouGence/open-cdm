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
package com.clougence.sql.hana.analysis.sysobj;

import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.sql.common.analysis.sysobj.AbstractSysObjectRegistrySpi;
import com.clougence.sql.common.registry.FunctionBehavior;
import com.clougence.sql.hana.HanaSqlEngineSpi;
import com.clougence.sql.hana.analysis.reference.HanaResourceRegistry;
import com.clougence.sql.hana.parser.HanaVersion;

public final class HanaSysObjectRegistrySpi extends AbstractSysObjectRegistrySpi {
    private final HanaResourceRegistry resources = HanaResourceRegistry.instance();

    @Override
    public String name() {
        return HanaSqlEngineSpi.NAME;
    }

    @Override
    protected boolean isRegisteredResource(BehaviorAction action, TargetType targetType, String catalog, String schema, String objectName, String databaseVersion) {
        FunctionBehavior classification = resources.functionBehavior(objectName, databaseVersion);
        if (targetType == TargetType.Function && (classification.action() != BehaviorAction.CALL || classification.privileged() || classification.unsafe())) {
            return false;
        }

        HanaVersion release = HanaVersion.parse(databaseVersion);

        return action == BehaviorAction.CALL && targetType == TargetType.Function && catalog == null && schema == null && resources.isSystemFunction(objectName, release);
    }
}
