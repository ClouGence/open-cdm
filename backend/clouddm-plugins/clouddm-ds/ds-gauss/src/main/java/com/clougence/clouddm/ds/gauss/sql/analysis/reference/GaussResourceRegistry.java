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
package com.clougence.clouddm.ds.gauss.sql.analysis.reference;

import static com.clougence.sql.common.registry.RegisteredResourceType.FUNCTION;

import java.util.Locale;

import com.clougence.clouddm.ds.gauss.sql.parser.GaussVersion;
import com.clougence.sql.common.registry.FunctionBehavior;
import com.clougence.sql.common.registry.VersionedFunctionCatalogLoader;
import com.clougence.sql.common.registry.VersionedResourceRegistry;

public final class GaussResourceRegistry {

    private static final String                               ANALYSIS_RESOURCES          = "/META-INF/clougence/gauss-analysis-resources.json";
    private static final String                               BUILT_IN_FUNCTIONS_RESOURCE = "/META-INF/clougence/gauss-built-in-functions.json";
    private static final GaussResourceRegistry                INSTANCE                    = new GaussResourceRegistry();

    private final VersionedResourceRegistry<FunctionBehavior> analysisFunctions           = new VersionedResourceRegistry<>(name -> name.toLowerCase(Locale.ROOT));
    private final VersionedResourceRegistry<Boolean>          functions                   = new VersionedResourceRegistry<>(name -> name.toLowerCase(Locale.ROOT));

    public static GaussResourceRegistry instance() {
        return INSTANCE;
    }

    private GaussResourceRegistry(){
        VersionedFunctionCatalogLoader
            .loadAnalysisResources(GaussResourceRegistry.class, ANALYSIS_RESOURCES, analysisFunctions, version -> GaussVersion.parse(version).ordinal() + 1);

        VersionedFunctionCatalogLoader.load(GaussResourceRegistry.class, BUILT_IN_FUNCTIONS_RESOURCE, functions, version -> GaussVersion.parse(version).ordinal() + 1);
    }

    public FunctionBehavior functionBehavior(String name, String databaseVersion) {
        String[] parts = name.split("\\.", -1);
        if (parts.length > 2) {
            return FunctionBehavior.DEFAULT;
        }

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (part.startsWith("\"") && part.endsWith("\"") && part.length() > 2) {
                part = part.substring(1, part.length() - 1);
                if (!part.equals(part.toLowerCase(Locale.ROOT))) {
                    return FunctionBehavior.DEFAULT;
                }
            }

            if (part.contains("\"")) {
                return FunctionBehavior.DEFAULT;
            }

            parts[i] = part;
        }

        if (parts.length == 2 && !"pg_catalog".equalsIgnoreCase(parts[0])) {
            return FunctionBehavior.DEFAULT;
        }

        int code = GaussVersion.parse(databaseVersion).ordinal() + 1;
        return analysisFunctions.find(FUNCTION, code, parts[parts.length - 1]).orElse(FunctionBehavior.DEFAULT);
    }

    public boolean isSystemFunction(String name, GaussVersion version) {
        return functions.contains(FUNCTION, version.ordinal() + 1, name);
    }
}
