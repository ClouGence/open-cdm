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
package com.clougence.sql.mysql.analysis.reference;

import static com.clougence.sql.common.registry.RegisteredResourceType.FUNCTION;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.sql.common.registry.BuiltinFunctionCatalogLoader;
import com.clougence.sql.common.registry.FunctionBehavior;
import com.clougence.sql.common.registry.VersionedFunctionCatalogLoader;
import com.clougence.sql.common.registry.VersionedResourceRegistry;
import com.clougence.sql.mysql.parser.MariaDbVersion;
import com.clougence.sql.mysql.parser.MySqlVersion;

public final class MariaDbResourceRegistry {

    private static final String                               ANALYSIS_RESOURCES          = "/META-INF/clougence/mariadb-analysis-resources.json";
    private static final String                               BUILT_IN_FUNCTIONS_RESOURCE = "/META-INF/clougence/mariadb-built-in-functions.json";
    private static final MariaDbResourceRegistry              INSTANCE                    = new MariaDbResourceRegistry();

    private final VersionedResourceRegistry<Boolean>          functions                   = new VersionedResourceRegistry<>(name -> name.toUpperCase(Locale.ROOT));
    private final VersionedResourceRegistry<FunctionBehavior> analysisFunctions           = new VersionedResourceRegistry<>(name -> name.toUpperCase(Locale.ROOT));

    public static MariaDbResourceRegistry instance() {
        return INSTANCE;
    }

    private MariaDbResourceRegistry(){
        VersionedFunctionCatalogLoader.loadAnalysisResources(MariaDbResourceRegistry.class, ANALYSIS_RESOURCES, analysisFunctions, version -> Integer.parseInt(version) * 10000);
        for (var function : BuiltinFunctionCatalogLoader.load(MariaDbResourceRegistry.class, BUILT_IN_FUNCTIONS_RESOURCE)) {
            for (String version : function.versions()) {
                for (MariaDbVersion release : MariaDbVersion.values()) {
                    if (release.versionString().equals(version)) {
                        int minimum = release.exactVersion();
                        functions.register(FUNCTION, minimum, minimum + 9999, true, function.name());
                        break;
                    }
                }
            }
        }
    }

    public FunctionBehavior functionClassification(String name, int version) {
        if (name.startsWith("`") || name.startsWith("\"")) {
            return FunctionBehavior.DEFAULT;
        }
        FunctionBehavior classification = analysisFunctions.find(FUNCTION, version / 10000 * 10000, name).orElse(FunctionBehavior.DEFAULT);
        FunctionBehavior compatible = MySqlResourceRegistry.instance().functionClassification(name, MySqlVersion.MYSQL_8_0.exactVersion());
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

    public boolean contains(String name, int version) {
        return functions.contains(FUNCTION, version, name);
    }

    public Set<String> functions(int version) {
        return functions.registeredResources(FUNCTION, version).keySet();
    }
}
