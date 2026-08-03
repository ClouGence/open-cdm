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

import com.clougence.sql.common.analysis.lineage.resolve.ResolvedColumn;

public record ResolvedRelation(String catalog, String schema, String name, List<ResolvedColumn> columns, List<ResolvedRelation> children) {

    public ResolvedRelation{
        columns = List.copyOf(columns);
        children = children == null ? List.of() : List.copyOf(children);
    }

    public ResolvedRelation(String name, List<ResolvedColumn> columns){
        this(null, null, name, columns, List.of());
    }

    public ResolvedRelation(String name, List<ResolvedColumn> columns, List<ResolvedRelation> children){
        this(null, null, name, columns, children);
    }

    public List<ResolvedColumn> findColumns(String catalogName, String schemaName, String qualifier, String column) {
        if (qualifier == null || qualifier.isBlank()) {
            return columns.stream().filter(candidate -> column.equalsIgnoreCase(candidate.name())).toList();
        }
        if (matches(catalogName, schemaName, qualifier)) {
            return columns.stream().filter(candidate -> column.equalsIgnoreCase(candidate.name())).toList();
        }
        return children.stream().flatMap(child -> child.findColumns(catalogName, schemaName, qualifier, column).stream()).toList();
    }

    public List<ResolvedRelation> findRelations(String qualifier) {
        if (matchesQualifiedName(qualifier)) {
            return List.of(this);
        }
        return children.stream().flatMap(child -> child.findRelations(qualifier).stream()).toList();
    }

    private boolean matches(String catalogName, String schemaName, String qualifier) {
        if (!qualifier.equalsIgnoreCase(name)) {
            return false;
        }
        if (schemaName != null && !schemaName.isBlank() && (!schemaName.equalsIgnoreCase(schema))) {
            return false;
        }
        return catalogName == null || catalogName.isBlank() || catalogName.equalsIgnoreCase(catalog);
    }

    private boolean matchesQualifiedName(String qualifier) {
        if (qualifier.equalsIgnoreCase(name)) {
            return true;
        }
        if (schema != null && qualifier.equalsIgnoreCase(schema + "." + name)) {
            return true;
        }
        return catalog != null && schema != null && qualifier.equalsIgnoreCase(catalog + "." + schema + "." + name);
    }
}
