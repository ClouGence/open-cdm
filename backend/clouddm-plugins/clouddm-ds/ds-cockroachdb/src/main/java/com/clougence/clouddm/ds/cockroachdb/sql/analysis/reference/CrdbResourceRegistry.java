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
package com.clougence.clouddm.ds.cockroachdb.sql.analysis.reference;

import static com.clougence.sql.common.registry.RegisteredResourceType.FUNCTION;

import java.util.List;
import java.util.Locale;

import com.clougence.clouddm.ds.cockroachdb.sql.parser.CockroachDbVersion;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.sql.common.registry.FunctionBehavior;
import com.clougence.sql.common.registry.VersionedFunctionCatalogLoader;
import com.clougence.sql.common.registry.VersionedResourceRegistry;
import com.clougence.sql.postgres.analysis.reference.PgFunctionBehavior;
import com.clougence.sql.postgres.analysis.reference.PgResourceDialect;

public final class CrdbResourceRegistry {

    private static final String                               ANALYSIS_RESOURCES          = "/META-INF/clougence/cockroachdb-analysis-resources.json";
    private static final String                               BUILT_IN_FUNCTIONS_RESOURCE = "/META-INF/clougence/cockroachdb-built-in-functions.json";
    private static final CrdbResourceRegistry                 INSTANCE                    = new CrdbResourceRegistry();

    private final VersionedResourceRegistry<FunctionBehavior> analysis                    = new VersionedResourceRegistry<>(name -> name.toLowerCase(Locale.ROOT));
    private final VersionedResourceRegistry<Boolean>          functions                   = new VersionedResourceRegistry<>(name -> name.toLowerCase(Locale.ROOT));

    public static CrdbResourceRegistry instance() {
        return INSTANCE;
    }

    private CrdbResourceRegistry(){
        VersionedFunctionCatalogLoader.load(CrdbResourceRegistry.class, BUILT_IN_FUNCTIONS_RESOURCE, functions, version -> CockroachDbVersion.parse(version).ordinal() + 1);
        VersionedFunctionCatalogLoader.loadAnalysisResources(CrdbResourceRegistry.class, ANALYSIS_RESOURCES, analysis, version -> CockroachDbVersion.parse(version).ordinal() + 1);
    }

    public boolean contains(String name) {
        return functions.contains(FUNCTION, CockroachDbVersion.LATEST.ordinal() + 1, name);
    }

    public boolean contains(String name, String databaseVersion) {
        CockroachDbVersion release = CockroachDbVersion.parse(databaseVersion);

        FunctionBehavior rule = analysis.find(FUNCTION, release.ordinal() + 1, name).orElse(FunctionBehavior.DEFAULT);
        if (rule.privileged() || rule.unsafe() || rule.action() != BehaviorAction.CALL) {
            return false;
        }
        return functions.contains(FUNCTION, release.ordinal() + 1, name);
    }

    public FunctionBehavior functionBehavior(List<String> names) {
        if (names.isEmpty() || names.size() > 2 || names.size() == 2 && !"crdb_internal".equals(PgResourceDialect.INSTANCE.normalizeIdentifier(names.get(0)))) {
            return FunctionBehavior.DEFAULT;
        }
        String identifier = names.get(names.size() - 1);
        String name = PgResourceDialect.INSTANCE.normalizeIdentifier(identifier);
        if (name.indexOf('.') >= 0 || identifier.startsWith("\"") && !name.equals(name.toLowerCase(Locale.ROOT))) {
            return FunctionBehavior.DEFAULT;
        }
        if (names.size() == 2) {
            name = "crdb_internal." + name;
        }
        return analysis.find(FUNCTION, CockroachDbVersion.LATEST.ordinal() + 1, name).orElse(FunctionBehavior.DEFAULT);
    }

    public PgFunctionBehavior pgFunctionBehavior(List<String> names) {
        FunctionBehavior rule = functionBehavior(names);
        return new PgFunctionBehavior(rule.action(), rule.targetType(), rule.targetAction(), rule.unsafe());
    }
}
