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
package com.clougence.sql.db2.analysis.reference;

import static com.clougence.sql.common.registry.RegisteredResourceType.FUNCTION;

import java.util.Locale;

import com.clougence.sql.common.registry.FunctionBehavior;
import com.clougence.sql.common.registry.VersionedFunctionCatalogLoader;
import com.clougence.sql.common.registry.VersionedResourceRegistry;
import com.clougence.sql.db2.parser.Db2Version;

public final class Db2ResourceRegistry {

    private static final String                               ANALYSIS_RESOURCES          = "/META-INF/clougence/db2-analysis-resources.json";
    private static final String                               BUILT_IN_FUNCTIONS_RESOURCE = "/META-INF/clougence/db2-built-in-functions.json";
    private static final Db2ResourceRegistry                  INSTANCE                    = new Db2ResourceRegistry();

    private final VersionedResourceRegistry<FunctionBehavior> analysisFunctions           = new VersionedResourceRegistry<>(name -> name.toLowerCase(Locale.ROOT));
    private final VersionedResourceRegistry<Boolean>          functions                   = new VersionedResourceRegistry<>(name -> name.toLowerCase(Locale.ROOT));

    public static Db2ResourceRegistry instance() {
        return INSTANCE;
    }

    private Db2ResourceRegistry(){
        VersionedFunctionCatalogLoader.loadAnalysisResources(Db2ResourceRegistry.class, ANALYSIS_RESOURCES, analysisFunctions, version -> Db2Version.parse(version).ordinal() + 1);

        VersionedFunctionCatalogLoader.load(Db2ResourceRegistry.class, BUILT_IN_FUNCTIONS_RESOURCE, functions, version -> Db2Version.parse(version).ordinal() + 1);
    }

    public FunctionBehavior functionBehavior(String name, String databaseVersion) {
        int code = Db2Version.parse(databaseVersion).ordinal() + 1;
        String[] parts = name.split("\\.", -1);
        if (parts.length > 2) {
            return FunctionBehavior.DEFAULT;
        }

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (part.startsWith("\"") && part.endsWith("\"") && part.length() > 2) {
                part = part.substring(1, part.length() - 1);
                while (part.endsWith(" ")) {
                    part = part.substring(0, part.length() - 1);
                }

                if (!part.equals(part.toUpperCase(Locale.ROOT))) {
                    return FunctionBehavior.DEFAULT;
                }
            }

            if (part.isEmpty() || part.contains("\"")) {
                return FunctionBehavior.DEFAULT;
            }

            parts[i] = part;
        }
        return analysisFunctions.find(FUNCTION, code, String.join(".", parts)).orElse(FunctionBehavior.DEFAULT);
    }

    public boolean isSystemFunction(String name, Db2Version version) {
        return functions.contains(FUNCTION, version.ordinal() + 1, name);
    }
}
