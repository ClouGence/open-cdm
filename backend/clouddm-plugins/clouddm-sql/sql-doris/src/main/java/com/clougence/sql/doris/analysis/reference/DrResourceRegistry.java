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

import java.util.Set;

import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.sql.common.registry.VersionedFunctionCatalogLoader;
import com.clougence.sql.common.registry.VersionedResourceRegistry;
import com.clougence.sql.doris.parser.DorisVersion;

/** Doris-owned resource facts shared by split and behavior analysis. */
public final class DrResourceRegistry {

    private static final String                      BUILT_IN_FUNCTIONS = "/META-INF/clougence/doris-built-in-functions.txt";
    private static final DrResourceRegistry          INSTANCE           = new DrResourceRegistry();

    private final VersionedResourceRegistry<Boolean> builtInFunctions   = new VersionedResourceRegistry<>(DrResourceDialect.INSTANCE);
    private final Set<String>                        importTableFunctions;
    private final Set<String>                        externalFileTableFunctions;

    public static DrResourceRegistry instance() {
        return INSTANCE;
    }

    private DrResourceRegistry(){
        VersionedFunctionCatalogLoader.load(DrResourceRegistry.class, BUILT_IN_FUNCTIONS, builtInFunctions);
        importTableFunctions = Set.of("file", "s3", "hdfs", "local", "http", "azure", "gcs", "jdbc", "odbc");
        externalFileTableFunctions = Set.of("file", "s3", "hdfs", "local", "http", "azure", "gcs");
    }

    public String normalizeIdentifier(String identifier) {
        return DrResourceDialect.INSTANCE.normalizeIdentifier(identifier);
    }

    public boolean isSystemFunction(String functionName) {
        return builtInFunctions.contains(FUNCTION, versionCode(DorisVersion.LATEST), functionName);
    }

    public boolean isUserDefinedFunction(String functionName, boolean qualified) {
        return qualified || !isSystemFunction(functionName);
    }

    public SplitQueryType functionStatementType(String functionName, boolean qualified) {
        return isUserDefinedFunction(functionName, qualified) ? SplitQueryType.CALL_PROG_OBJ : null;
    }

    public boolean isImportTableFunction(String functionName) {
        return importTableFunctions.contains(normalizeIdentifier(functionName));
    }

    public boolean isExternalFileTableFunction(String functionName) {
        return externalFileTableFunctions.contains(normalizeIdentifier(functionName));
    }

    private static int versionCode(DorisVersion version) {
        return Integer.parseInt((version == null ? DorisVersion.LATEST : version).versionString());
    }
}
