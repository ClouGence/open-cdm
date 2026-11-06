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
package com.clougence.clouddm.ds.oceanbase.sql.ob4ora.analysis.reference;

import static com.clougence.sql.common.registry.RegisteredResourceType.FUNCTION;

import java.util.Locale;

import com.clougence.clouddm.ds.oceanbase.sql.parser.OceanBaseVersion;
import com.clougence.sql.common.registry.FunctionBehavior;
import com.clougence.sql.common.registry.VersionedFunctionCatalogLoader;
import com.clougence.sql.common.registry.VersionedResourceRegistry;

public final class ObOraResourceRegistry {

    private static final String                               ANALYSIS_RESOURCES          = "/META-INF/clougence/oceanbase-oracle-analysis-resources.json";
    private static final String                               BUILT_IN_FUNCTIONS_RESOURCE = "/META-INF/clougence/oceanbase-oracle-built-in-functions.json";
    private static final ObOraResourceRegistry                INSTANCE                    = new ObOraResourceRegistry();

    private final VersionedResourceRegistry<FunctionBehavior> analysis                    = new VersionedResourceRegistry<>(name -> name.toUpperCase(Locale.ROOT));
    private final VersionedResourceRegistry<Boolean>          functions                   = new VersionedResourceRegistry<>(name -> name.toLowerCase(Locale.ROOT));

    public static ObOraResourceRegistry instance() {
        return INSTANCE;
    }

    private ObOraResourceRegistry(){
        VersionedFunctionCatalogLoader.loadAnalysisResources(ObOraResourceRegistry.class, ANALYSIS_RESOURCES, analysis, version -> OceanBaseVersion.parse(version).ordinal() + 1);
        VersionedFunctionCatalogLoader.load(ObOraResourceRegistry.class, BUILT_IN_FUNCTIONS_RESOURCE, functions, version -> OceanBaseVersion.parse(version).ordinal() + 1);
    }

    public FunctionBehavior functionBehavior(String name) {
        if (name.indexOf('`') >= 0) {
            return FunctionBehavior.DEFAULT;
        }
        String canonical = name.toUpperCase(Locale.ROOT);
        if (name.indexOf('"') >= 0) {
            String[] parts = name.split("\\.");
            for (int i = 0; i < parts.length; i++) {
                if (parts[i].indexOf('"') < 0) {
                    continue;
                }
                if (!parts[i].startsWith("\"") || !parts[i].endsWith("\"")) {
                    return FunctionBehavior.DEFAULT;
                }
                String identifier = parts[i].substring(1, parts[i].length() - 1);
                if (!identifier.equals(identifier.toUpperCase(Locale.ROOT)) || identifier.indexOf('"') >= 0) {
                    return FunctionBehavior.DEFAULT;
                }
                parts[i] = identifier;
            }
            canonical = String.join(".", parts).toUpperCase(Locale.ROOT);
            String packageName = parts[0];
            if (parts.length == 3 && "SYS".equalsIgnoreCase(parts[0])) {
                packageName = parts[1];
            }
            if (!"DBMS_LOCK".equalsIgnoreCase(packageName) && !"DBMS_SESSION".equalsIgnoreCase(packageName)) {
                return FunctionBehavior.DEFAULT;
            }
        }
        if (canonical.startsWith("SYS.")) {
            canonical = canonical.substring(4);
        }
        return analysis.find(FUNCTION, OceanBaseVersion.LATEST.ordinal() + 1, canonical).orElse(FunctionBehavior.DEFAULT);
    }

    public boolean isSystemFunction(String name, OceanBaseVersion version) {
        return functions.contains(FUNCTION, version.ordinal() + 1, name);
    }
}
