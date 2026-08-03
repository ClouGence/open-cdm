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
package com.clougence.sql.common.analysis.lineage.scope;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import com.clougence.sql.common.analysis.lineage.model.LineageCte;

public final class CteScope {

    private final Map<String, CteBinding> bindings = new LinkedHashMap<>();
    private final CteScope                outer;

    public CteScope(CteScope outer){
        this.outer = outer;
    }

    public CteBinding register(LineageCte cte) {
        CteBinding binding = new CteBinding(cte);
        bindings.put(cte.name().toLowerCase(Locale.ROOT), binding);
        return binding;
    }

    public CteBinding find(String name) {
        CteBinding binding = bindings.get(name.toLowerCase(Locale.ROOT));
        if (binding != null || outer == null) {
            return binding;
        }
        return outer.find(name);
    }
}
