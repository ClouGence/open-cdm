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
package com.clougence.clouddm.ds.dameng.sql.analysis.sysobj;

import com.clougence.clouddm.ds.dameng.sql.DmSqlEngineSpi;
import com.clougence.clouddm.ds.dameng.sql.analysis.reference.DmResourceRegistry;
import com.clougence.clouddm.ds.dameng.sql.parser.DmVersion;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.sql.common.analysis.sysobj.AbstractSysObjectRegistrySpi;
import com.clougence.sql.common.registry.FunctionBehavior;

/** Dameng permission-exempt resources backed by the parser resource registry. */
public final class DmSysObjectRegistrySpi extends AbstractSysObjectRegistrySpi {

    private final DmResourceRegistry resources = DmResourceRegistry.instance();

    @Override
    public String name() {
        return DmSqlEngineSpi.NAME;
    }

    @Override
    protected boolean isRegisteredResource(BehaviorAction action, TargetType targetType, String catalog, String schema, String objectName, String databaseVersion) {
        DmVersion version = DmVersion.parse(databaseVersion);
        if (targetType == TargetType.Function) {
            FunctionBehavior classification = resources.functionClassification(objectName);
            boolean registered = schema == null && resources.isSystemFunction(version, objectName);
            if (schema != null) {
                classification = resources.functionClassification(schema + "." + objectName);
                registered = false;
                if (catalog != null) {
                    registered = resources.isSystemFunction(version, catalog, schema, objectName);
                }
                if (!registered) {
                    registered = resources.isSystemFunction(version, schema, objectName);
                }
            }
            return registered && action == BehaviorAction.CALL && classification.action() == BehaviorAction.CALL && !classification.privileged() && !classification.unsafe();
        }
        if (targetType == TargetType.Procedure && action == BehaviorAction.CALL) {
            if (catalog != null && schema != null && resources.isSystemProcedure(version, catalog, schema, objectName)) {
                return true;
            }
            if (schema != null && resources.isSystemProcedure(version, schema, objectName)) {
                return true;
            }
            return schema == null && resources.isSystemProcedure(version, objectName);
        }
        if ((targetType == TargetType.Table || targetType == TargetType.View) && action == BehaviorAction.READ) {
            if (catalog != null && schema != null && resources.isSystemView(version, catalog, schema, objectName)) {
                return true;
            }
            if (schema != null && resources.isSystemView(version, schema, objectName)) {
                return true;
            }
            return schema == null && resources.isSystemView(version, objectName);
        }
        if (targetType == TargetType.Type && action == BehaviorAction.READ) {
            if (catalog != null && schema != null && resources.isSystemType(version, catalog, schema, objectName)) {
                return true;
            }
            if (schema != null && resources.isSystemType(version, schema, objectName)) {
                return true;
            }
            return schema == null && resources.isSystemType(version, objectName);
        }
        return false;
    }
}
