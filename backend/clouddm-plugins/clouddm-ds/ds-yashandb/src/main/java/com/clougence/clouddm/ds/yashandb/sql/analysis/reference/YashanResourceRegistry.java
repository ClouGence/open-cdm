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
package com.clougence.clouddm.ds.yashandb.sql.analysis.reference;

import static com.clougence.sql.common.registry.RegisteredResourceType.FUNCTION;

import java.util.Locale;

import com.clougence.clouddm.ds.yashandb.sql.parser.YashanDbVersion;
import com.clougence.sql.common.registry.VersionedFunctionCatalogLoader;
import com.clougence.sql.common.registry.VersionedResourceRegistry;

public final class YashanResourceRegistry {

    private static final String                      BUILT_IN_FUNCTIONS_RESOURCE = "/META-INF/clougence/yashandb-oracle-built-in-functions.json";
    private static final YashanResourceRegistry      INSTANCE                    = new YashanResourceRegistry();

    private final VersionedResourceRegistry<Boolean> functions                   = new VersionedResourceRegistry<>(name -> name.toLowerCase(Locale.ROOT));

    public static YashanResourceRegistry instance() {
        return INSTANCE;
    }

    private YashanResourceRegistry(){
        VersionedFunctionCatalogLoader.load(YashanResourceRegistry.class, BUILT_IN_FUNCTIONS_RESOURCE, functions, version -> YashanDbVersion.parse(version).ordinal() + 1);
    }

    public boolean isSystemFunction(String name, YashanDbVersion version) {
        return functions.contains(FUNCTION, version.ordinal() + 1, name);
    }
}
