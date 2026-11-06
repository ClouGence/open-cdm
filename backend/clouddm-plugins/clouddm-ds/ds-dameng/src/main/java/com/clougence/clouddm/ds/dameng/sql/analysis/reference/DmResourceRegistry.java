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
package com.clougence.clouddm.ds.dameng.sql.analysis.reference;

import static com.clougence.sql.common.registry.RegisteredResourceType.*;

import com.clougence.clouddm.ds.dameng.sql.parser.DmVersion;
import com.clougence.sql.common.registry.FunctionBehavior;
import com.clougence.sql.common.registry.ResourceRegistryDialect;
import com.clougence.sql.common.registry.VersionedFunctionCatalogLoader;
import com.clougence.sql.common.registry.VersionedResourceRegistry;

/**
 * Dameng-owned registered resource facts shared by SQL analysis implementations.
 */
public final class DmResourceRegistry {

    private static final String                               ANALYSIS_RESOURCES          = "/META-INF/clougence/dameng-analysis-resources.json";
    private static final String                               BUILT_IN_FUNCTIONS_RESOURCE = "/META-INF/clougence/dameng-built-in-functions.json";
    private static final String                               SYSTEM_RESOURCES            = "/META-INF/clougence/dameng-skip-permission-resources.json";
    private static final DmResourceRegistry                   INSTANCE                    = new DmResourceRegistry(DmResourceDialect.INSTANCE);

    private final VersionedResourceRegistry<Boolean>          resources;
    private final VersionedResourceRegistry<FunctionBehavior> analysisFunctions;

    public static DmResourceRegistry instance() {
        return INSTANCE;
    }

    private DmResourceRegistry(ResourceRegistryDialect dialect){
        this.resources = new VersionedResourceRegistry<>(dialect);
        this.analysisFunctions = new VersionedResourceRegistry<>(dialect);
        DmRegistryResourceLoader.loadBuiltInFunctions(DmResourceRegistry.class, BUILT_IN_FUNCTIONS_RESOURCE, resources, dialect);
        DmRegistryResourceLoader.loadSystemResources(DmResourceRegistry.class, SYSTEM_RESOURCES, resources, dialect);
        VersionedFunctionCatalogLoader.loadAnalysisResources(DmResourceRegistry.class, ANALYSIS_RESOURCES, analysisFunctions, Integer::parseInt);
    }

    public boolean isSystemFunction(DmVersion version, String... nameParts) {
        return resources.contains(FUNCTION, versionCode(version), nameParts);
    }

    public boolean isSystemProcedure(DmVersion version, String... nameParts) {
        return resources.contains(PROCEDURE, versionCode(version), nameParts);
    }

    public boolean isSystemView(DmVersion version, String... nameParts) {
        return resources.contains(TABLE, versionCode(version), nameParts);
    }

    public boolean isSystemType(DmVersion version, String... nameParts) {
        return resources.contains(TYPE, versionCode(version), nameParts);
    }

    public FunctionBehavior functionClassification(String functionName) {
        if (functionName.contains("\"")) {
            return FunctionBehavior.DEFAULT;
        }
        return analysisFunctions.find(FUNCTION, versionCode(DmVersion.LATEST), functionName).orElse(FunctionBehavior.DEFAULT);
    }

    private static int versionCode(DmVersion version) {
        return Integer.parseInt(version.versionString());
    }
}
