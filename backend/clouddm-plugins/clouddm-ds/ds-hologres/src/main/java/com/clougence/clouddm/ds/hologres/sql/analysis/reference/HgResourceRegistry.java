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
package com.clougence.clouddm.ds.hologres.sql.analysis.reference;

import static com.clougence.sql.common.registry.RegisteredResourceType.FUNCTION;

import java.util.List;
import java.util.Locale;

import com.clougence.clouddm.ds.hologres.sql.parser.HologresVersion;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.sql.common.registry.FunctionBehavior;
import com.clougence.sql.common.registry.VersionedFunctionCatalogLoader;
import com.clougence.sql.common.registry.VersionedResourceRegistry;
import com.clougence.sql.postgres.analysis.reference.PgFunctionBehavior;
import com.clougence.sql.postgres.analysis.reference.PgResourceDialect;

public final class HgResourceRegistry {

    private static final String                               ANALYSIS_RESOURCES          = "/META-INF/clougence/hologres-analysis-resources.json";
    private static final String                               BUILT_IN_FUNCTIONS_RESOURCE = "/META-INF/clougence/hologres-built-in-functions.json";
    private static final HgResourceRegistry                   INSTANCE                    = new HgResourceRegistry();

    private final VersionedResourceRegistry<FunctionBehavior> analysis                    = new VersionedResourceRegistry<>(name -> name.toLowerCase(Locale.ROOT));
    private final VersionedResourceRegistry<Boolean>          functions                   = new VersionedResourceRegistry<>(name -> name.toLowerCase(Locale.ROOT));

    public static HgResourceRegistry instance() {
        return INSTANCE;
    }

    private HgResourceRegistry(){
        VersionedFunctionCatalogLoader.load(HgResourceRegistry.class, BUILT_IN_FUNCTIONS_RESOURCE, functions, version -> HologresVersion.parse(version).ordinal() + 1);
        VersionedFunctionCatalogLoader.loadAnalysisResources(HgResourceRegistry.class, ANALYSIS_RESOURCES, analysis, version -> HologresVersion.parse(version).ordinal() + 1);
    }

    public boolean contains(String name) {
        return functions.contains(FUNCTION, HologresVersion.LATEST.ordinal() + 1, name);
    }

    public boolean contains(String name, String databaseVersion) {
        HologresVersion release = HologresVersion.parse(databaseVersion);

        FunctionBehavior rule = analysis.find(FUNCTION, release.ordinal() + 1, name).orElse(FunctionBehavior.DEFAULT);
        if (rule.privileged() || rule.unsafe() || rule.action() != BehaviorAction.CALL) {
            return false;
        }
        return functions.contains(FUNCTION, release.ordinal() + 1, name);
    }

    public FunctionBehavior functionBehavior(List<String> names) {
        if (names.isEmpty() || names.size() > 2 || names.size() == 2 && !"pg_catalog".equals(PgResourceDialect.INSTANCE.normalizeIdentifier(names.get(0)))) {
            return FunctionBehavior.DEFAULT;
        }
        String identifier = names.get(names.size() - 1);
        String name = PgResourceDialect.INSTANCE.normalizeIdentifier(identifier);
        if (identifier.startsWith("\"") && !name.equals(name.toLowerCase(Locale.ROOT))) {
            return FunctionBehavior.DEFAULT;
        }
        return analysis.find(FUNCTION, HologresVersion.LATEST.ordinal() + 1, name).orElse(FunctionBehavior.DEFAULT);
    }

    public PgFunctionBehavior pgFunctionBehavior(List<String> names) {
        FunctionBehavior rule = functionBehavior(names);
        return new PgFunctionBehavior(rule.action(), rule.targetType(), rule.targetAction(), rule.unsafe());
    }
}
