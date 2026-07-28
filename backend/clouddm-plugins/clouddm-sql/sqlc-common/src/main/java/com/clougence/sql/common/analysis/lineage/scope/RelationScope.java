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

import java.util.ArrayList;
import java.util.List;

import com.clougence.sql.common.analysis.lineage.resolve.ResolvedColumn;

public record RelationScope(List<ResolvedRelation> relations, RelationScope outer, boolean unresolvedAllowed) {

    public RelationScope{
        relations = List.copyOf(relations);
    }

    public RelationScope(List<ResolvedRelation> relations, RelationScope outer){
        this(relations, outer, outer != null && outer.unresolvedAllowed());
    }

    public ResolvedColumn findColumn(String catalog, String schema, String qualifier, String column) {
        List<ResolvedColumn> matches = new ArrayList<>();
        for (ResolvedRelation relation : relations) {
            matches.addAll(relation.findColumns(catalog, schema, qualifier, column));
        }
        if (matches.size() == 1) {
            return matches.get(0);
        }
        if (matches.size() > 1) {
            throw new IllegalArgumentException("Column '" + column + "' in field list is ambiguous");
        }
        if (outer != null) {
            return outer.findColumn(catalog, schema, qualifier, column);
        }
        if (unresolvedAllowed) {
            return new ResolvedColumn(column, List.of());
        }
        String at = qualifier == null ? "" : " at: " + qualifier;
        throw new IllegalArgumentException("Can't find such column: " + column + at);
    }

    public List<ResolvedRelation> findRelations(String qualifier) {
        if (qualifier == null || qualifier.isBlank()) {
            return relations;
        }
        List<ResolvedRelation> matches = relations.stream().flatMap(relation -> relation.findRelations(qualifier).stream()).toList();
        if (!matches.isEmpty() || outer == null) {
            return matches;
        }
        return outer.findRelations(qualifier);
    }
}
