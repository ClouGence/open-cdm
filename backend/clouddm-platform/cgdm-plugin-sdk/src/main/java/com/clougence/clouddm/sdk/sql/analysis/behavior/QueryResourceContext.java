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
package com.clougence.clouddm.sdk.sql.analysis.behavior;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import com.clougence.clouddm.sdk.execute.meta.DsElement;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;

/** Session scope and live metadata lookups for resolving qualified resources. */
public record QueryResourceContext(Map<UmiTypes, Object> levels, boolean dataSourceOwner, BiFunction<List<UmiTypes>, Map<UmiTypes, Object>, List<DsElement>> listLevels,
                                   BiFunction<Map<UmiTypes, Object>, String, Value> selectObject) {
}
