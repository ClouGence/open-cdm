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
package com.clougence.sql.common.analysis.lineage;

import java.util.*;

import com.clougence.clouddm.sdk.service.execute.MetaCol;
import com.clougence.clouddm.sdk.service.execute.MetaColConvert;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.sql.analysis.lineage.ColumnLineage;
import com.clougence.clouddm.sdk.sql.analysis.lineage.LineageAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.lineage.SourceName;
import com.clougence.clouddm.sdk.sql.analysis.security.column.QueryItem;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.*;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;

public abstract class AbstractLineageAnalysisSpi implements LineageAnalysisSpi {

    private static final String NATURAL_JOIN_OPTION = "naturalJoin";

    protected final MetaService metaService;

    @Getter
    @Setter
    protected static class MutableColumnLineage {

        private final List<SourceName> columns = new ArrayList<>();
        private String                 itemAlias;
        private String                 tableAlias;

        public void addAllSources(List<SourceName> columns) {
            this.columns.addAll(columns);
        }
    }

    public AbstractLineageAnalysisSpi(MetaService metaService){
        this.metaService = metaService;
    }

    protected List<MutableColumnLineage> analyzeColumns(String uid, long dsID, Map<UmiTypes, Object> levelsParam, List<RuleDomain> domains) {
        return analyzeColumns(uid, dsID, levelsParam, domains, null);
    }

    private List<MutableColumnLineage> analyzeColumns(String uid, long dsID, Map<UmiTypes, Object> levelsParam, List<RuleDomain> domains, ColumnScope outerScope) {
        List<List<MutableColumnLineage>> result = new ArrayList<>();
        for (RuleDomain domain : domains) {
            RdbSelectDomain selectDomain = (RdbSelectDomain) domain;
            List<MutableColumnLineage> selectItems = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(selectDomain.getChildren())) {
                boolean naturalJoin = selectDomain.getOptions() != null && Boolean.parseBoolean(selectDomain.getOptions().get(NATURAL_JOIN_OPTION));
                for (RuleDomain child : selectDomain.getChildren()) {
                    ColumnScope tableOuterScope = selectItems
                        .isEmpty() ? outerScope : new ColumnScope(columnMap(selectItems), outerScope, naturalJoin, selectDomain.getJoinUsingColumns());
                    selectItems.addAll(parseTableDomain(uid, dsID, levelsParam, (RdbTableDomain) child, tableOuterScope));
                }

                // <columnName,<tableName,SelectColumn>>
                ColumnScope scope = new ColumnScope(columnMap(selectItems), outerScope, naturalJoin, selectDomain.getJoinUsingColumns());

                if (CollectionUtils.isNotEmpty(selectDomain.getColumns())) {
                    if (selectDomain.getMode() == RdbQueryMode.WITH) {
                        if (selectDomain.getColumns().size() != selectItems.size()) {
                            throw new RuntimeException();
                        } else {
                            for (int i = 0; i < selectItems.size(); i++) {
                                MutableColumnLineage selectItem = selectItems.get(i);
                                QueryItem queryItem = selectDomain.getColumns().get(i);
                                selectItem.setItemAlias(queryItem.getColumn());
                                selectItem.setTableAlias(queryItem.getTable());
                            }
                        }
                        result.add(selectItems);
                    } else {
                        result.add(parseColumns(uid, dsID, levelsParam, selectDomain, selectItems, scope));
                    }

                } else {
                    result.add(selectItems);
                }
            } else {
                for (QueryItem column : selectDomain.getColumns()) {
                    MutableColumnLineage selectItem = new MutableColumnLineage();
                    if (needAlias(column)) {
                        throw new RuntimeException("select element: " + column.getColumn() + " need set alias");
                    }
                    if (column.getItemAlias() == null) {
                        selectItem.setItemAlias(column.getColumn());
                    } else {
                        selectItem.setItemAlias(column.getItemAlias());
                    }
                    selectItems.add(selectItem);

                }
                result.add(selectItems);
            }
        }

        // no from

        List<MutableColumnLineage> selectItems = result.get(0);
        for (int i = 1; i < result.size(); i++) {
            List<MutableColumnLineage> selectColumns1 = result.get(i);
            if (selectColumns1.size() != selectItems.size()) {
                throw new RuntimeException("UNION column count mismatch: first=" + selectItems.size() + ", branch=" + selectColumns1.size() + ", branchIndex=" + i);
            }
            for (int j = 0; j < selectItems.size(); j++) {
                selectItems.get(j).getColumns().addAll(selectColumns1.get(j).getColumns());
            }
        }
        return selectItems;
    }

    private static Map<String, Map<String, MutableColumnLineage>> columnMap(List<MutableColumnLineage> columns) {
        Map<String, Map<String, MutableColumnLineage>> columnMap = new LinkedHashMap<>();
        for (MutableColumnLineage column : columns) {
            columnMap.computeIfAbsent(column.getItemAlias(), ignored -> new LinkedHashMap<>()).put(column.getTableAlias(), column);
        }
        return columnMap;
    }

    protected boolean needAlias(QueryItem queryItem) {
        if (queryItem.getItemAlias() == null) {
            for (RuleDomain column : queryItem.getColumns()) {
                if (!(column instanceof RdbColumnDomain)) {
                    return true;
                }
            }
            return CollectionUtils.isEmpty(queryItem.getColumns());
        }
        return false;
    }

    private List<MutableColumnLineage> parseColumns(String uid, long dsID, Map<UmiTypes, Object> levelsParam, RdbSelectDomain selectDomain, List<MutableColumnLineage> selectItems,
                                                    ColumnScope scope) {
        List<MutableColumnLineage> result = new ArrayList<>();
        for (QueryItem queryItem : selectDomain.getColumns()) {
            if (needAlias(queryItem)) {
                throw new RuntimeException("select element: " + queryItem.getColumn() + " need set alias");
            }
            if (queryItem.isSelectAll()) {
                if (StringUtils.isEmpty(queryItem.getTable())) {
                    result.addAll(selectItems);
                } else {
                    for (MutableColumnLineage column : selectItems) {
                        if (column.getTableAlias().equals(queryItem.getTable())) {
                            result.add(column);
                        }
                    }
                }
                continue;
            }
            MutableColumnLineage column1 = new MutableColumnLineage();

            if (queryItem.getItemAlias() != null) {
                column1.setItemAlias(queryItem.getItemAlias());
            } else {
                column1.setItemAlias(queryItem.getColumn());
            }
            for (RuleDomain selectColumn : queryItem.getColumns()) {
                if (selectColumn instanceof RdbColumnDomain rdbColumnDomain) {

                    MutableColumnLineage column = findColumn(scope, rdbColumnDomain);

                    if (StringUtils.isNotEmpty(queryItem.getItemAlias())) {
                        column.setItemAlias(queryItem.getItemAlias());
                    }

                    column1.getColumns().addAll(column.getColumns());

                } else if (selectColumn instanceof RdbCallDomain) {
                    MutableColumnLineage column = parseCallDomain(uid, dsID, levelsParam, (RdbCallDomain) selectColumn, queryItem, selectDomain, scope);
                    column1.getColumns().addAll(column.getColumns());
                } else if (selectColumn instanceof RdbSelectDomain) {
                    List<MutableColumnLineage> columnList = analyzeColumns(uid, dsID, levelsParam, Collections.singletonList(selectColumn), scope);
                    for (MutableColumnLineage column : columnList) {
                        column1.getColumns().addAll(column.getColumns());
                    }
                }
            }
            result.add(column1);
        }
        return result;
    }

    private static MutableColumnLineage findColumn(ColumnScope scope, RdbColumnDomain rdbColumnDomain) {
        Map<String, MutableColumnLineage> tableMap = scope.columns().get(rdbColumnDomain.getColumn());
        if (tableMap == null) {
            if (scope.outer() != null) {
                return findColumn(scope.outer(), rdbColumnDomain);
            }
            throw new RuntimeException("Can't find such column: " + rdbColumnDomain.getColumn());
        }
        MutableColumnLineage column;
        if (StringUtils.isEmpty(rdbColumnDomain.getTable())) {
            if (tableMap.size() != 1) {
                if (!scope.naturalJoin() && !scope.isUsingColumn(rdbColumnDomain.getColumn())) {
                    throw new RuntimeException("Column '" + rdbColumnDomain.getColumn() + "' in field list is ambiguous");
                }
                MutableColumnLineage merged = new MutableColumnLineage();
                merged.setItemAlias(rdbColumnDomain.getColumn());
                tableMap.values().forEach(item -> merged.addAllSources(item.getColumns()));
                return merged;
            }
            column = tableMap.values().iterator().next();
        } else {
            column = tableMap.get(rdbColumnDomain.getTable());
        }
        if (column == null) {
            if (scope.outer() != null) {
                return findColumn(scope.outer(), rdbColumnDomain);
            }
            throw new RuntimeException("Can't find such column '" + rdbColumnDomain.getColumn() + "' at: " + rdbColumnDomain.getTable());
        }
        return column;
    }

    private List<MutableColumnLineage> parseTableDomain(String uid, long dsID, Map<UmiTypes, Object> levelsParam, RdbTableDomain tableDomain, ColumnScope outerScope) {
        if (tableDomain.isVirtual()) {
            List<MutableColumnLineage> selectItems = analyzeColumns(uid, dsID, levelsParam, tableDomain.getChildren(), outerScope);
            List<String> derivedColumnNames = tableDomain.getDerivedColumnNames();
            if (CollectionUtils.isNotEmpty(derivedColumnNames)) {
                if (derivedColumnNames.size() != selectItems.size()) {
                    throw new RuntimeException("Derived column list size does not match select column count");
                }
                for (int i = 0; i < derivedColumnNames.size(); i++) {
                    selectItems.get(i).setItemAlias(derivedColumnNames.get(i));
                }
            }
            for (MutableColumnLineage selectItem : selectItems) {
                selectItem.setTableAlias(tableDomain.getAlias() == null ? tableDomain.getTable() : tableDomain.getAlias());
            }
            return selectItems;
        } else {
            Map<UmiTypes, Object> levels = new HashMap<>(levelsParam);
            if (tableDomain.getCatalog() != null) {
                levels.put(UmiTypes.Catalog, tableDomain.getCatalog());
            }
            if (tableDomain.getSchema() != null) {
                levels.put(UmiTypes.Schema, tableDomain.getSchema());
            }
            List<MetaCol> metaCols = this.metaService.fetchTableColumns(uid, dsID, levels, tableDomain.getTable());
            List<MutableColumnLineage> selectItems = MetaColConvert.toSourceNames(metaCols).stream().map(sourceName -> {
                MutableColumnLineage item = new MutableColumnLineage();
                item.getColumns().add(sourceName);
                item.setItemAlias(sourceName.column());
                return item;
            }).toList();
            for (MutableColumnLineage selectItem : selectItems) {
                if (tableDomain.getAlias() != null) {
                    selectItem.setTableAlias(tableDomain.getAlias());
                } else {
                    selectItem.setTableAlias(tableDomain.getTable());
                }
                selectItem.setItemAlias(selectItem.getColumns().get(0).column());
            }
            return selectItems;
        }
    }

    private MutableColumnLineage parseCallDomain(String uid, long dsID, Map<UmiTypes, Object> levelsParam, RdbCallDomain callDomain, QueryItem queryItem,
                                                 RdbSelectDomain selectDomain, ColumnScope scope) {
        MutableColumnLineage selectItem = new MutableColumnLineage();
        selectItem.setItemAlias(queryItem.getItemAlias());

        if (CollectionUtils.isEmpty(callDomain.getChildren())) {
            return selectItem;
        }

        for (RuleDomain child : callDomain.getChildren()) {
            if (child instanceof RdbSelectDomain) {
                List<MutableColumnLineage> selectItems = analyzeColumns(uid, dsID, levelsParam, Collections.singletonList(child), scope);
                if (selectItems.size() > 1) {
                    throw new RuntimeException("The query statement in the function should only return one column");
                } else {
                    selectItem.addAllSources(selectItems.get(0).getColumns());
                }
            } else if (child instanceof RdbCallDomain) {
                MutableColumnLineage callColumn = parseCallDomain(uid, dsID, levelsParam, (RdbCallDomain) child, queryItem, selectDomain, scope);
                selectItem.addAllSources(callColumn.getColumns());
            } else if (child instanceof RdbColumnDomain columnDomain) {
                MutableColumnLineage column = findColumn(scope, columnDomain);
                selectItem.addAllSources(column.getColumns());
            }
        }
        return selectItem;
    }

    private record ColumnScope(Map<String, Map<String, MutableColumnLineage>> columns, ColumnScope outer, boolean naturalJoin, Set<String> usingColumns) {

        boolean isUsingColumn(String column) {
            return usingColumns != null && usingColumns.stream().anyMatch(name -> StringUtils.equalsIgnoreCase(name, column));
        }
    }

    protected List<ColumnLineage> toResultColumns(List<MutableColumnLineage> columns) {
        return this.toResultColumns(columns, null, null);
    }

    protected List<ColumnLineage> toResultColumns(List<MutableColumnLineage> columns, String defaultCatalog, String defaultSchema) {
        List<ColumnLineage> result = new ArrayList<>();
        for (MutableColumnLineage column : columns) {
            List<SourceName> sourceNames = column.getColumns().stream().map(source -> {
                String catalog = StringUtils.isEmpty(source.catalog()) ? defaultCatalog : source.catalog();
                String schema = StringUtils.isEmpty(source.schema()) ? defaultSchema : source.schema();
                return new SourceName(catalog, schema, source.table(), source.column());
            }).toList();
            result.add(new ColumnLineage(column.getItemAlias(), sourceNames));
        }
        return result;
    }
}
