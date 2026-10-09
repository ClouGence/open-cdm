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

public record ResolvedRelation(String catalog, String schema, String name, List<ResolvedColumn> columns, List<ResolvedRelation> children, boolean caseSensitive) {

    public ResolvedRelation{
        columns = List.copyOf(columns);
        children = children == null ? List.of() : List.copyOf(children);
    }

    public ResolvedRelation(String catalog, String schema, String name, List<ResolvedColumn> columns, List<ResolvedRelation> children) {
        this(catalog, schema, name, columns, children, false);
    }

    public ResolvedRelation(String name, List<ResolvedColumn> columns){
        this(null, null, name, columns, List.of());
    }

    public ResolvedRelation(String name, List<ResolvedColumn> columns, List<ResolvedRelation> children){
        this(null, null, name, columns, children);
    }

    public List<ResolvedColumn> findColumns(String catalogName, String schemaName, String qualifier, String column) {
        if (qualifier == null || qualifier.isBlank()) {
            return columns.stream().filter(candidate -> sameName(column, candidate.name())).toList();
        }
        if (matches(catalogName, schemaName, qualifier)) {
            return columns.stream().filter(candidate -> sameName(column, candidate.name())).toList();
        }
        return children.stream().flatMap(child -> child.findColumns(catalogName, schemaName, qualifier, column).stream()).toList();
    }

    public List<ResolvedRelation> findRelations(String qualifier) {
        if (matchesQualifiedName(qualifier)) {
            return List.of(this);
        }
        return children.stream().flatMap(child -> child.findRelations(qualifier).stream()).toList();
    }

    private boolean sameName(String left, String right) {
        if (caseSensitive) {
            return left.equals(right);
        }
        return left.equalsIgnoreCase(right);
    }

    private boolean matches(String catalogName, String schemaName, String qualifier) {
        if (!sameName(qualifier, name)) {
            return false;
        }
        if (schemaName != null && !schemaName.isBlank() && (!sameName(schemaName, schema))) {
            return false;
        }
        return catalogName == null || catalogName.isBlank() || sameName(catalogName, catalog);
    }

    private boolean matchesQualifiedName(String qualifier) {
        if (sameName(qualifier, name)) {
            return true;
        }
        if (schema != null && sameName(qualifier, schema + "." + name)) {
            return true;
        }
        return catalog != null && schema != null && sameName(qualifier, catalog + "." + schema + "." + name);
    }
}
