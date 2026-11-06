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
package com.clougence.clouddm.ds.ads.sql.ads4my;

import com.clougence.clouddm.ds.ads.sql.ads4my.analysis.reference.AdsMyResourceRegistry;
import com.clougence.clouddm.ds.ads.sql.ads4my.parser.AdsMyVersion;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.sql.common.analysis.sysobj.AbstractSysObjectRegistrySpi;
import com.clougence.sql.common.registry.FunctionBehavior;
import com.clougence.sql.mysql.analysis.sysobj.MySysObjectRegistrySpi;

public final class AdsMySysObjectRegistrySpi extends AbstractSysObjectRegistrySpi {
    private final AdsMyResourceRegistry  resources = AdsMyResourceRegistry.instance();

    private final MySysObjectRegistrySpi mysql     = new MySysObjectRegistrySpi();

    @Override
    public String name() {
        return AdsMySqlEngineSpi.NAME;
    }

    @Override
    protected boolean isRegisteredResource(BehaviorAction action, TargetType targetType, String catalog, String schema, String objectName, String databaseVersion) {
        if (targetType != TargetType.Function || action != BehaviorAction.CALL) {
            return false;
        }

        AdsMyVersion release = AdsMyVersion.parse(databaseVersion);
        FunctionBehavior classification = resources.functionClassification(objectName);
        if (classification.action() != BehaviorAction.CALL || classification.privileged() || classification.unsafe()) {
            return false;
        }

        if (catalog == null && schema == null && resources.isSystemFunction(objectName, release)) {
            return true;
        }
        return mysql.isOrdinaryFunction(catalog, schema, objectName, databaseVersion);
    }
}
