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
package com.clougence.sql.common.analysis.lineage.resolve;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.sdk.sql.analysis.lineage.LineageColumn;
import com.clougence.clouddm.sdk.sql.analysis.lineage.SourceName;
import com.clougence.sql.common.analysis.lineage.model.*;
import com.clougence.sql.common.analysis.lineage.scope.CteBinding;
import com.clougence.sql.common.analysis.lineage.scope.CteScope;
import com.clougence.sql.common.analysis.lineage.scope.RelationScope;
import com.clougence.sql.common.analysis.lineage.scope.ResolvedRelation;

public final class LineageResolver {

    private static final RelationScope    UNRESOLVED_SCOPE = new RelationScope(List.of(), null, true);
    private final LineageMetadataResolver metadataResolver;

    public LineageResolver(LineageMetadataResolver metadataResolver){
        this.metadataResolver = metadataResolver;
    }

    public List<LineageColumn> resolve(LineageQuery query) {
        return resolveQuery(query, UNRESOLVED_SCOPE, null).stream().map(column -> new LineageColumn(column.name(), column.sources())).toList();
    }

    private List<ResolvedColumn> resolveQuery(LineageQuery query, RelationScope outerScope, CteScope outerCteScope) {
        CteScope cteScope = registerCtes(query.ctes(), outerCteScope, outerScope);

        List<ResolvedColumn> result = null;
        for (LineageQueryBlock branch : query.branches()) {
            List<ResolvedColumn> branchColumns = resolveBlock(branch, outerScope, cteScope);
            if (result == null) {
                result = new ArrayList<>(branchColumns);
                continue;
            }
            if (result.size() != branchColumns.size()) {
                throw new IllegalArgumentException("UNION column count mismatch: first=" + result.size() + ", branch=" + branchColumns.size());
            }
            for (int i = 0; i < result.size(); i++) {
                ResolvedColumn first = result.get(i);
                List<SourceName> sources = new ArrayList<>(first.sources());
                sources.addAll(branchColumns.get(i).sources());
                result.set(i, new ResolvedColumn(first.name(), sources));
            }
        }
        return result == null ? List.of() : List.copyOf(result);
    }

    private List<ResolvedColumn> resolveBlock(LineageQueryBlock block, RelationScope outerScope, CteScope cteScope) {
        CteScope blockCteScope = registerCtes(block.ctes(), cteScope, outerScope);
        List<ResolvedRelation> relations = new ArrayList<>();
        for (LineageRelation relation : block.relations()) {
            RelationScope visibleScope = new RelationScope(relations, outerScope);
            relations.add(resolveRelation(relation, visibleScope, blockCteScope));
        }
        RelationScope scope = new RelationScope(relations, outerScope);

        List<ResolvedColumn> result = new ArrayList<>();
        for (LineageSelectItem item : block.selectItems()) {
            if (item.wildcard()) {
                List<ResolvedRelation> wildcardRelations = scope.findRelations(item.wildcardQualifier());
                for (ResolvedRelation relation : wildcardRelations) {
                    for (ResolvedColumn column : relation.columns()) {
                        result.add(new ResolvedColumn(column.name(), bindUnknownRanges(column.sources(), item.range())));
                    }
                }
                continue;
            }

            List<SourceName> sources = resolveValues(item.values(), scope, blockCteScope, result);
            result.add(new ResolvedColumn(item.outputName(), sources));
        }
        return List.copyOf(result);
    }

    private CteScope registerCtes(List<LineageCte> ctes, CteScope outerCteScope, RelationScope definitionRelationScope) {
        CteScope cteScope = new CteScope(outerCteScope);
        for (LineageCte cte : ctes) {
            CteScope previousScope = cteScope;
            CteScope currentScope = new CteScope(previousScope);
            CteBinding binding = currentScope.register(cte);
            binding.definitionScope(cte.recursive() ? currentScope : previousScope, definitionRelationScope);
            cteScope = currentScope;
        }
        return cteScope;
    }

    private ResolvedRelation resolveRelation(LineageRelation relation, RelationScope visibleScope, CteScope cteScope) {
        if (relation instanceof LineageJoinRelation join) {
            ResolvedRelation left = resolveRelation(join.left(), visibleScope, cteScope);
            RelationScope rightScope = new RelationScope(List.of(left), visibleScope);
            ResolvedRelation right = resolveRelation(join.right(), rightScope, cteScope);
            return new ResolvedRelation("", joinColumns(left.columns(), right.columns(), join), List.of(left, right));
        }

        List<ResolvedColumn> columns;
        String defaultName;
        String catalog = null;
        String schema = null;
        if (relation instanceof LineageDerivedRelation derived) {
            RelationScope derivedOuterScope = derived.lateral() ? visibleScope : null;
            columns = resolveQuery(derived.query(), derivedOuterScope, cteScope);
            defaultName = "";
        } else if (relation instanceof LineageNamedRelation named) {
            CteBinding cte = named.catalog() == null && named.schema() == null ? cteScope.find(named.name()) : null;
            if (cte != null) {
                columns = resolveCte(cte);
            } else {
                List<SourceName> sources = metadataResolver.resolveColumns(new LineageTableName(named.catalog(), named.schema(), named.name()));
                columns = sources.stream().map(source -> new ResolvedColumn(source.column(), List.of(source))).toList();
                if (named.alias() == null || named.alias().isBlank()) {
                    catalog = named.catalog();
                    schema = named.schema();
                }
            }
            defaultName = named.name();
        } else if (relation instanceof LineageTableFunctionRelation tableFunction) {
            columns = tableFunction.columns().stream().map(column -> new ResolvedColumn(column.name(), resolveValues(column.values(), visibleScope, cteScope, List.of()))).toList();
            defaultName = "";
        } else {
            throw new IllegalArgumentException("Unsupported lineage relation: " + relation.getClass().getName());
        }

        columns = applyAliases(columns, relation.columnAliases());
        String relationName = relation.alias();
        if (relationName == null || relationName.isBlank()) {
            relationName = defaultName;
        }
        return new ResolvedRelation(catalog, schema, relationName, columns, List.of());
    }

    private static List<ResolvedColumn> joinColumns(List<ResolvedColumn> left, List<ResolvedColumn> right, LineageJoinRelation join) {
        if (!join.natural() && join.usingColumns().isEmpty()) {
            List<ResolvedColumn> columns = new ArrayList<>(left);
            columns.addAll(right);
            return List.copyOf(columns);
        }

        List<String> mergedNames = new ArrayList<>();
        if (join.natural()) {
            for (ResolvedColumn column : left) {
                if (containsColumn(right, column.name()) && !containsName(mergedNames, column.name())) {
                    mergedNames.add(column.name());
                }
            }
        } else {
            mergedNames.addAll(join.usingColumns());
        }
        List<ResolvedColumn> columns = new ArrayList<>();
        for (String name : mergedNames) {
            List<SourceName> sources = new ArrayList<>();
            matchingColumns(left, name).forEach(column -> sources.addAll(column.sources()));
            matchingColumns(right, name).forEach(column -> sources.addAll(column.sources()));
            columns.add(new ResolvedColumn(name, sources));
        }
        left.stream().filter(column -> !containsName(mergedNames, column.name())).forEach(columns::add);
        right.stream().filter(column -> !containsName(mergedNames, column.name())).forEach(columns::add);
        return List.copyOf(columns);
    }

    private static boolean containsColumn(List<ResolvedColumn> columns, String name) {
        return columns.stream().anyMatch(column -> name.equalsIgnoreCase(column.name()));
    }

    private static List<ResolvedColumn> matchingColumns(List<ResolvedColumn> columns, String name) {
        return columns.stream().filter(column -> name.equalsIgnoreCase(column.name())).toList();
    }

    private static boolean containsName(List<String> names, String name) {
        return names.stream().anyMatch(candidate -> candidate.equalsIgnoreCase(name));
    }

    private List<SourceName> resolveValues(List<LineageValue> values, RelationScope scope, CteScope cteScope, List<ResolvedColumn> selectItems) {
        List<SourceName> sources = new ArrayList<>();
        for (LineageValue value : values) {
            if (value instanceof LineageColumnReference reference) {
                ResolvedColumn column = null;
                if (reference.selectAliasVisible() && reference.qualifier() == null) {
                    column = findSelectItem(selectItems, reference.column());
                }
                if (column == null) {
                    column = scope.findColumn(reference.catalog(), reference.schema(), reference.qualifier(), reference.column());
                }
                sources.addAll(bindUnknownRanges(column.sources(), reference.range()));
            } else if (value instanceof LineageSubqueryValue subquery) {
                List<ResolvedColumn> subqueryColumns = resolveQuery(subquery.query(), scope, cteScope);
                subqueryColumns.forEach(column -> sources.addAll(column.sources()));
            }
        }
        return List.copyOf(sources);
    }

    private static ResolvedColumn findSelectItem(List<ResolvedColumn> selectItems, String name) {
        List<ResolvedColumn> matches = selectItems.stream().filter(column -> {
            return name.equalsIgnoreCase(column.name());
        }).toList();
        if (matches.size() > 1) {
            throw new IllegalArgumentException("Select alias '" + name + "' is ambiguous");
        }
        return matches.isEmpty() ? null : matches.get(0);
    }

    private List<ResolvedColumn> resolveCte(CteBinding binding) {
        if (binding.resolved() != null) {
            return binding.resolved();
        }
        if (binding.resolving()) {
            throw new IllegalArgumentException("Recursive CTE requires an anchor implementation: " + binding.cte().name());
        }
        binding.resolving(true);
        try {
            List<ResolvedColumn> columns;
            if (binding.cte().recursive()) {
                columns = resolveRecursiveCte(binding);
            } else {
                columns = resolveQuery(binding.cte().query(), binding.definitionRelationScope(), binding.definitionCteScope());
            }
            columns = applyAliases(columns, binding.cte().columnAliases());
            binding.resolved(columns);
            return columns;
        } finally {
            binding.resolving(false);
        }
    }

    private List<ResolvedColumn> resolveRecursiveCte(CteBinding binding) {
        List<LineageQueryBlock> branches = binding.cte().query().branches();
        if (branches.isEmpty()) {
            return List.of();
        }

        List<ResolvedColumn> result = new ArrayList<>(resolveBlock(branches.get(0), binding.definitionRelationScope(), binding.definitionCteScope()));
        result = new ArrayList<>(applyAliases(result, binding.cte().columnAliases()));
        binding.resolved(result);

        for (int branchIndex = 1; branchIndex < branches.size(); branchIndex++) {
            List<ResolvedColumn> branch = resolveBlock(branches.get(branchIndex), binding.definitionRelationScope(), binding.definitionCteScope());
            if (result.size() != branch.size()) {
                throw new IllegalArgumentException("Recursive CTE column count mismatch: anchor=" + result.size() + ", branch=" + branch.size());
            }
            for (int columnIndex = 0; columnIndex < result.size(); columnIndex++) {
                ResolvedColumn anchorColumn = result.get(columnIndex);
                List<SourceName> sources = new ArrayList<>(anchorColumn.sources());
                for (SourceName source : branch.get(columnIndex).sources()) {
                    if (!sources.contains(source)) {
                        sources.add(source);
                    }
                }
                result.set(columnIndex, new ResolvedColumn(anchorColumn.name(), sources));
            }
            binding.resolved(result);
        }
        return List.copyOf(result);
    }

    private static List<ResolvedColumn> applyAliases(List<ResolvedColumn> columns, List<String> aliases) {
        if (aliases.isEmpty()) {
            return columns;
        }
        if (aliases.size() != columns.size()) {
            throw new IllegalArgumentException("Derived column list size does not match select column count");
        }
        List<ResolvedColumn> renamed = new ArrayList<>();
        for (int i = 0; i < aliases.size(); i++) {
            renamed.add(new ResolvedColumn(aliases.get(i), columns.get(i).sources()));
        }
        return List.copyOf(renamed);
    }

    private static List<SourceName> bindUnknownRanges(List<SourceName> sources, SourceRange range) {
        List<SourceName> bound = new ArrayList<>();
        for (SourceName source : sources) {
            if (source.startLine() != 0 || source.endLine() != 0) {
                bound.add(source);
            } else {
                bound.add(new SourceName(source
                    .catalog(), source.schema(), source.table(), source.column(), range.startLine(), range.startColumn(), range.endLine(), range.endColumn()));
            }
        }
        return List.copyOf(bound);
    }
}
