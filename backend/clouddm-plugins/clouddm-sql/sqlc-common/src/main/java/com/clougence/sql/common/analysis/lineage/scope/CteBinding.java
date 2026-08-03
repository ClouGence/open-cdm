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

import java.util.List;

import com.clougence.sql.common.analysis.lineage.model.LineageCte;
import com.clougence.sql.common.analysis.lineage.resolve.ResolvedColumn;

public final class CteBinding {

    private final LineageCte     cte;
    private CteScope             definitionCteScope;
    private RelationScope        definitionRelationScope;
    private List<ResolvedColumn> resolved;
    private boolean              resolving;

    CteBinding(LineageCte cte){
        this.cte = cte;
    }

    public LineageCte cte() {
        return cte;
    }

    public CteScope definitionCteScope() {
        return definitionCteScope;
    }

    public RelationScope definitionRelationScope() {
        return definitionRelationScope;
    }

    public void definitionScope(CteScope cteScope, RelationScope relationScope) {
        this.definitionCteScope = cteScope;
        this.definitionRelationScope = relationScope;
    }

    public List<ResolvedColumn> resolved() {
        return resolved;
    }

    public void resolved(List<ResolvedColumn> resolved) {
        this.resolved = List.copyOf(resolved);
    }

    public boolean resolving() {
        return resolving;
    }

    public void resolving(boolean resolving) {
        this.resolving = resolving;
    }
}
