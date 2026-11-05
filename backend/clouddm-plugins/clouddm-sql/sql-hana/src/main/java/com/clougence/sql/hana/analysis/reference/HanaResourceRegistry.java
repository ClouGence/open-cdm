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
package com.clougence.sql.hana.analysis.reference;

import static com.clougence.sql.common.registry.RegisteredResourceType.FUNCTION;

import java.util.Locale;

import com.clougence.sql.common.registry.FunctionBehavior;
import com.clougence.sql.common.registry.VersionedFunctionCatalogLoader;
import com.clougence.sql.common.registry.VersionedResourceRegistry;
import com.clougence.sql.hana.parser.HanaVersion;
import com.clougence.sql.hana.parser.antlr.HanaParser;

public final class HanaResourceRegistry {

    private static final String                               ANALYSIS_RESOURCES          = "/META-INF/clougence/hana-analysis-resources.json";
    private static final String                               BUILT_IN_FUNCTIONS_RESOURCE = "/META-INF/clougence/hana-built-in-functions.json";
    private static final HanaResourceRegistry                 INSTANCE                    = new HanaResourceRegistry();

    private final VersionedResourceRegistry<FunctionBehavior> analysisFunctions           = new VersionedResourceRegistry<>(name -> name.toLowerCase(Locale.ROOT));
    private final VersionedResourceRegistry<Boolean>          functions                   = new VersionedResourceRegistry<>(name -> name.toLowerCase(Locale.ROOT));

    public static HanaResourceRegistry instance() {
        return INSTANCE;
    }

    private HanaResourceRegistry(){
        VersionedFunctionCatalogLoader
            .loadAnalysisResources(HanaResourceRegistry.class, ANALYSIS_RESOURCES, analysisFunctions, version -> HanaVersion.parse(version).ordinal() + 1);

        VersionedFunctionCatalogLoader.load(HanaResourceRegistry.class, BUILT_IN_FUNCTIONS_RESOURCE, functions, version -> HanaVersion.parse(version).ordinal() + 1);
    }

    public FunctionBehavior functionBehavior(String name, String databaseVersion) {
        int code = HanaVersion.parse(databaseVersion).ordinal() + 1;
        return analysisFunctions.find(FUNCTION, code, name).orElse(FunctionBehavior.DEFAULT);
    }

    public boolean isBuiltIn(HanaParser.QualifiedNameContext function) {
        return function.identifier().size() == 1 && function.identifier(0).QUOTED_IDENTIFIER() == null
               && functions.contains(FUNCTION, HanaVersion.HANA_2.ordinal() + 1, function.getText());
    }

    public boolean isSystemFunction(String name, HanaVersion version) {
        return functions.contains(FUNCTION, version.ordinal() + 1, name);
    }
}
