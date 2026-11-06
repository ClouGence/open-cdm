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
package com.clougence.sql.mysql.analysis.sysobj;

import java.util.Locale;

import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.sql.common.analysis.sysobj.AbstractSysObjectRegistrySpi;
import com.clougence.sql.common.registry.FunctionBehavior;
import com.clougence.sql.mysql.MySqlEngineSpi;
import com.clougence.sql.mysql.analysis.reference.MariaDbResourceRegistry;
import com.clougence.sql.mysql.analysis.reference.MySqlResourceRegistry;
import com.clougence.sql.mysql.parser.MariaDbVersion;
import com.clougence.sql.mysql.parser.MySqlVersion;
import com.clougence.utils.StringUtils;

/** MySQL permission-exempt resources backed by the parser resource registry. */
public final class MySysObjectRegistrySpi extends AbstractSysObjectRegistrySpi {

    private final MySqlResourceRegistry resources = MySqlResourceRegistry.instance();

    @Override
    public String name() {
        return MySqlEngineSpi.NAME;
    }

    public boolean isOrdinaryFunction(String catalog, String schema, String objectName, String databaseVersion) {
        return isPermissionExempt(BehaviorAction.CALL, TargetType.Function, catalog, schema, objectName, databaseVersion);
    }

    @Override
    protected boolean isRegisteredResource(BehaviorAction action, TargetType targetType, String catalog, String schema, String objectName, String databaseVersion) {
        if (databaseVersion != null && databaseVersion.toLowerCase(Locale.ROOT).contains("mariadb")) {
            if (targetType != TargetType.Function || action != BehaviorAction.CALL || catalog != null || schema != null) {
                return false;
            }

            MariaDbVersion release = MariaDbVersion.parse(databaseVersion);
            FunctionBehavior classification = MariaDbResourceRegistry.instance().functionClassification(objectName, release.exactVersion());
            if (classification.action() != BehaviorAction.CALL || classification.privileged() || classification.unsafe()) {
                return false;
            }

            if (MariaDbResourceRegistry.instance().contains(objectName, release.exactVersion())) {
                return true;
            }

            // Newer MySQL-only functions do not establish MariaDB compatibility.
            MySqlVersion baseline = MySqlVersion.MYSQL_8_0;
            if (!resources.isBuiltInFunction(objectName, MySqlVersion.MYSQL_5_6)) {
                return false;
            }

            if (!resources.isBuiltInFunction(objectName, baseline)) {
                return false;
            }

            return true;
        }

        MySqlVersion version = MySqlVersion.parse(databaseVersion);
        if (targetType == TargetType.Function && action == BehaviorAction.CALL) {
            FunctionBehavior classification = resources.functionClassification(objectName, version.exactVersion());
            if (classification.action() != BehaviorAction.CALL || classification.privileged() || classification.unsafe()) {
                return false;
            }
            return schema == null ? !resources.isUserDefinedFunction(objectName, false, version) : resources.isSystemFunction(schema, objectName, version);
        }
        if (targetType == TargetType.Procedure && action == BehaviorAction.CALL) {
            return schema != null && resources.isSystemProcedure(schema, objectName, version);
        }
        if (targetType == TargetType.Table && action == BehaviorAction.READ) {
            return schema == null && StringUtils.equalsIgnoreCase("DUAL", objectName) || schema != null && resources.isMetadataTable(schema, objectName, version);
        }
        return false;
    }
}
