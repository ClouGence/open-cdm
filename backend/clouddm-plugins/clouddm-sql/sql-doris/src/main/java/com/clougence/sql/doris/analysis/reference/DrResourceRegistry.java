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
package com.clougence.sql.doris.analysis.reference;

import static com.clougence.sql.common.registry.RegisteredResourceType.FUNCTION;

import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.sql.common.registry.FunctionBehavior;
import com.clougence.sql.common.registry.VersionedFunctionCatalogLoader;
import com.clougence.sql.common.registry.VersionedResourceRegistry;
import com.clougence.sql.doris.parser.DorisVersion;

/** Doris-owned resource facts shared by split and behavior analysis. */
public final class DrResourceRegistry {

    private static final String                               ANALYSIS_RESOURCES = "/META-INF/clougence/doris-analysis-resources.json";
    private static final String                               BUILT_IN_FUNCTIONS = "/META-INF/clougence/doris-built-in-functions.json";
    private static final DrResourceRegistry                   INSTANCE           = new DrResourceRegistry();

    private final VersionedResourceRegistry<FunctionBehavior> analysis           = new VersionedResourceRegistry<>(DrResourceDialect.INSTANCE);
    private final VersionedResourceRegistry<Boolean>          builtInFunctions   = new VersionedResourceRegistry<>(DrResourceDialect.INSTANCE);

    public static DrResourceRegistry instance() {
        return INSTANCE;
    }

    private DrResourceRegistry(){
        VersionedFunctionCatalogLoader.loadAnalysisResources(DrResourceRegistry.class, ANALYSIS_RESOURCES, analysis, version -> DorisVersion.parse(version).ordinal() + 1);
        VersionedFunctionCatalogLoader.load(DrResourceRegistry.class, BUILT_IN_FUNCTIONS, builtInFunctions, version -> DorisVersion.parse(version).ordinal() + 1);
    }

    public String normalizeIdentifier(String identifier) {
        return DrResourceDialect.INSTANCE.normalizeIdentifier(identifier);
    }

    public boolean isSystemFunction(String functionName) {
        return builtInFunctions.contains(FUNCTION, DorisVersion.LATEST.ordinal() + 1, functionName);
    }

    public boolean isSystemFunction(String name, String version) {
        DorisVersion release = DorisVersion.parse(version);
        FunctionBehavior rule = functionBehavior(name);
        return !rule.privileged() && !rule.unsafe() && rule.action() == BehaviorAction.CALL && builtInFunctions.contains(FUNCTION, release.ordinal() + 1, name);
    }

    public boolean isUserDefinedFunction(String functionName, boolean qualified) {
        return qualified || !isSystemFunction(functionName);
    }

    public SplitQueryType functionStatementType(String functionName, boolean qualified) {
        if (!qualified) {
            FunctionBehavior rule = functionBehavior(functionName);
            if (!rule.statementTypes().isEmpty()) {
                return rule.statementTypes().iterator().next();
            }
        }
        return isUserDefinedFunction(functionName, qualified) ? SplitQueryType.CALL_PROG_OBJ : null;
    }

    public boolean isImportTableFunction(String functionName) {
        return functionBehavior(functionName).statementTypes().contains(SplitQueryType.DATA_IMPORT);
    }

    public boolean isExternalFileTableFunction(String functionName) {
        return functionBehavior(functionName).targetType() == TargetType.File;
    }

    public FunctionBehavior functionBehavior(String name) {
        if (name == null) {
            return FunctionBehavior.DEFAULT;
        }
        if (name.indexOf('.') >= 0 || name.startsWith("\"") || name.startsWith("[")) {
            return FunctionBehavior.DEFAULT;
        }
        return analysis.find(FUNCTION, DorisVersion.LATEST.ordinal() + 1, name).orElse(FunctionBehavior.DEFAULT);
    }
}
