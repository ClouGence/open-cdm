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
package com.clougence.sql.hana.analysis;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.RdbConstraintDomain;

public final class HanaConstraintDomain extends RdbConstraintDomain {
    @Override
    public List<Map<TargetType, String>> resolveResource() {
        Map<TargetType, String> resource = new EnumMap<>(TargetType.class);
        resource.put(TargetType.Catalog, getTableCatalog());
        resource.put(TargetType.Schema, getTableSchema());
        resource.put(TargetType.Table, getTableName());
        return List.of(resource);
    }
}
