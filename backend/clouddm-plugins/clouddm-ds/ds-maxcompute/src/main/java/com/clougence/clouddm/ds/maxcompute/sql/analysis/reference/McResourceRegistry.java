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
package com.clougence.clouddm.ds.maxcompute.sql.analysis.reference;

import static com.clougence.sql.common.registry.RegisteredResourceType.FUNCTION;

import java.util.Locale;

import com.clougence.sql.common.registry.FunctionBehavior;
import com.clougence.sql.common.registry.VersionedFunctionCatalogLoader;
import com.clougence.sql.common.registry.VersionedResourceRegistry;

public final class McResourceRegistry {

    private static final String                               ANALYSIS_RESOURCES = "/META-INF/clougence/maxcompute-analysis-resources.json";
    private static final int                                  UNVERSIONED        = 1;
    private static final McResourceRegistry                   INSTANCE           = new McResourceRegistry();

    private final VersionedResourceRegistry<FunctionBehavior> analysisFunctions  = new VersionedResourceRegistry<>(name -> name.toUpperCase(Locale.ROOT));

    public static McResourceRegistry instance() {
        return INSTANCE;
    }

    private McResourceRegistry(){
        VersionedFunctionCatalogLoader.loadAnalysisResources(McResourceRegistry.class, ANALYSIS_RESOURCES, analysisFunctions, version -> UNVERSIONED);
    }

    public FunctionBehavior functionBehavior(String name) {
        if (name == null) {
            return FunctionBehavior.DEFAULT;
        }
        if (name.length() >= 2 && name.startsWith("`") && name.endsWith("`")) {
            name = name.substring(1, name.length() - 1).replace("``", "`");
        }
        return analysisFunctions.find(FUNCTION, UNVERSIONED, name).orElse(FunctionBehavior.DEFAULT);
    }
}
