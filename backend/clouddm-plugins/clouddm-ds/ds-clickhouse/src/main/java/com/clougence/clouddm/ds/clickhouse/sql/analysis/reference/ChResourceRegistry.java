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
package com.clougence.clouddm.ds.clickhouse.sql.analysis.reference;

import static com.clougence.sql.common.registry.RegisteredResourceType.FUNCTION;

import java.util.List;
import java.util.Locale;

import com.clougence.clouddm.ds.clickhouse.sql.parser.ClickHouseVersion;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.sql.common.registry.*;

/** ClickHouse built-ins, preserving server case sensitivity and aggregate combinators. */
public final class ChResourceRegistry {

    private static final String                               ANALYSIS_RESOURCES             = "/META-INF/clougence/clickhouse-analysis-resources.json";
    private static final String                               AGGREGATE_COMBINATORS_RESOURCE = "/META-INF/clougence/clickhouse-aggregate-combinators.json";
    private static final String                               BUILT_IN_FUNCTIONS_RESOURCE    = "/META-INF/clougence/clickhouse-built-in-functions.json";
    private static final ChResourceRegistry                   INSTANCE                       = new ChResourceRegistry();

    private final VersionedResourceRegistry<FunctionBehavior> analysisFunctions              = new VersionedResourceRegistry<>(name -> name);
    private final VersionedResourceRegistry<Boolean>          functions                      = new VersionedResourceRegistry<>(name -> name);
    private final VersionedResourceRegistry<Boolean>          foldedFunctions                = new VersionedResourceRegistry<>(name -> name.toLowerCase(Locale.ROOT));
    private final VersionedResourceRegistry<Boolean>          aggregates                     = new VersionedResourceRegistry<>(name -> name);
    private final VersionedResourceRegistry<Boolean>          foldedAggregates               = new VersionedResourceRegistry<>(name -> name.toLowerCase(Locale.ROOT));
    private final VersionedResourceRegistry<Boolean>          combinators                    = new VersionedResourceRegistry<>(name -> name);
    private final List<BuiltinFunctionDefinition>             combinatorDefinitions;

    public static ChResourceRegistry instance() {
        return INSTANCE;
    }

    private ChResourceRegistry(){
        VersionedFunctionCatalogLoader
            .loadAnalysisResources(ChResourceRegistry.class, ANALYSIS_RESOURCES, analysisFunctions, version -> Integer.parseInt(ClickHouseVersion.parse(version).versionString()));
        VersionedFunctionCatalogLoader
            .load(ChResourceRegistry.class, BUILT_IN_FUNCTIONS_RESOURCE, functions, version -> Integer.parseInt(ClickHouseVersion.parse(version).versionString()));
        VersionedFunctionCatalogLoader
            .load(ChResourceRegistry.class, AGGREGATE_COMBINATORS_RESOURCE, combinators, version -> Integer.parseInt(ClickHouseVersion.parse(version).versionString()));
        combinatorDefinitions = BuiltinFunctionCatalogLoader.load(ChResourceRegistry.class, AGGREGATE_COMBINATORS_RESOURCE);
        for (var function : BuiltinFunctionCatalogLoader.load(ChResourceRegistry.class, BUILT_IN_FUNCTIONS_RESOURCE)) {
            for (String version : function.versions()) {
                int code = Integer.parseInt(ClickHouseVersion.parse(version).versionString());
                if (function.caseInsensitive()) {
                    foldedFunctions.register(FUNCTION, code, code, true, function.name());
                }
                if (function.aggregate()) {
                    aggregates.register(FUNCTION, code, code, true, function.name());
                    if (function.caseInsensitive()) {
                        foldedAggregates.register(FUNCTION, code, code, true, function.name());
                    }
                }
            }
        }
    }

    public boolean isOrdinaryFunction(String name) {
        FunctionBehavior rule = functionBehavior(name);
        return !rule.privileged() && !rule.unsafe() && rule.action() == BehaviorAction.CALL;
    }

    public boolean isSystemFunction(String name, ClickHouseVersion release) {
        int version = Integer.parseInt(release.versionString());
        return functions.contains(FUNCTION, version, name) || foldedFunctions.contains(FUNCTION, version, name) || isAggregate(name, version);
    }

    private boolean isAggregate(String name, int version) {
        if (aggregates.contains(FUNCTION, version, name) || foldedAggregates.contains(FUNCTION, version, name)) {
            return true;
        }
        for (var combinator : combinatorDefinitions) {
            String suffix = combinator.name();
            if (combinators.contains(FUNCTION, version, suffix) && name.endsWith(suffix) && isAggregate(name.substring(0, name.length() - suffix.length()), version)) {
                return true;
            }
        }
        return false;
    }

    public FunctionBehavior functionBehavior(String name) {
        if (name == null) {
            return FunctionBehavior.DEFAULT;
        }
        if (name.length() >= 2 && (name.startsWith("`") || name.startsWith("\"")) && name.charAt(0) == name.charAt(name.length() - 1)) {
            String quote = name.substring(0, 1);
            name = name.substring(1, name.length() - 1).replace(quote + quote, quote);
            StringBuilder identifier = new StringBuilder();
            for (int i = 0; i < name.length(); i++) {
                char ch = name.charAt(i);
                if (ch == '\\') {
                    if (i + 1 < name.length() && name.charAt(i + 1) == 'N') {
                        i++;
                        continue;
                    }
                    if (i + 3 < name.length() && name.charAt(i + 1) == 'x' && Character.digit(name.charAt(i + 2), 16) >= 0 && Character.digit(name.charAt(i + 3), 16) >= 0) {
                        ch = (char) (Character.digit(name.charAt(i + 2), 16) * 16 + Character.digit(name.charAt(i + 3), 16));
                        i += 3;
                    } else {
                        // Other escapes cannot produce an ASCII native function name.
                        return FunctionBehavior.DEFAULT;
                    }
                }
                identifier.append(ch);
            }
            name = identifier.toString();
        }
        return analysisFunctions.find(FUNCTION, Integer.parseInt(ClickHouseVersion.LATEST.versionString()), name).orElse(FunctionBehavior.DEFAULT);
    }
}
