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
package com.clougence.clouddm.ds.oceanbase.sql.ob4my.analysis.reference;

import static com.clougence.sql.common.registry.RegisteredResourceType.FUNCTION;

import java.util.Locale;

import com.clougence.clouddm.ds.oceanbase.sql.parser.OceanBaseVersion;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.sql.common.registry.FunctionBehavior;
import com.clougence.sql.common.registry.VersionedFunctionCatalogLoader;
import com.clougence.sql.common.registry.VersionedResourceRegistry;
import com.clougence.sql.mysql.analysis.reference.MySqlResourceRegistry;
import com.clougence.sql.mysql.parser.MySqlVersion;

public final class ObMyResourceRegistry {

    private static final String                               ANALYSIS_RESOURCES          = "/META-INF/clougence/oceanbase-mysql-analysis-resources.json";
    private static final String                               BUILT_IN_FUNCTIONS_RESOURCE = "/META-INF/clougence/oceanbase-mysql-built-in-functions.json";
    private static final ObMyResourceRegistry                 INSTANCE                    = new ObMyResourceRegistry();

    private final VersionedResourceRegistry<Boolean>          functions                   = new VersionedResourceRegistry<>(name -> name.toLowerCase(Locale.ROOT));
    private final VersionedResourceRegistry<FunctionBehavior> analysisFunctions           = new VersionedResourceRegistry<>(name -> name.toLowerCase(Locale.ROOT));

    public static ObMyResourceRegistry instance() {
        return INSTANCE;
    }

    private ObMyResourceRegistry(){
        VersionedFunctionCatalogLoader.load(ObMyResourceRegistry.class, BUILT_IN_FUNCTIONS_RESOURCE, functions, version -> OceanBaseVersion.parse(version).ordinal() + 1);
        VersionedFunctionCatalogLoader
            .loadAnalysisResources(ObMyResourceRegistry.class, ANALYSIS_RESOURCES, analysisFunctions, version -> OceanBaseVersion.parse(version).ordinal() + 1);
    }

    public FunctionBehavior functionClassification(String name, boolean hasArguments) {
        if (!hasArguments && "LAST_INSERT_ID".equalsIgnoreCase(name)) {
            return FunctionBehavior.DEFAULT;
        }

        FunctionBehavior classification = functionClassification(name);
        if (classification.statementTypes().contains(SplitQueryType.SESSION_SETTING_WRITE)) {
            return new FunctionBehavior(classification.statementTypes(),
                BehaviorAction.CONFIGURE,
                TargetType.Session,
                BehaviorAction.CONFIGURE,
                classification.unsafe(),
                classification.metadata(),
                classification.privileged());
        }
        return classification;
    }

    public FunctionBehavior functionClassification(String name) {
        if (name.startsWith("`") || name.startsWith("\"")) {
            return FunctionBehavior.DEFAULT;
        }
        return analysisFunctions.find(FUNCTION, OceanBaseVersion.LATEST.ordinal() + 1, name)
            .orElseGet(() -> MySqlResourceRegistry.instance().functionClassification(name, MySqlVersion.LATEST.exactVersion()));
    }

    public boolean isSystemFunction(String name, OceanBaseVersion version) {
        return functions.contains(FUNCTION, version.ordinal() + 1, name);
    }
}
