package com.clougence.clouddm.dsfamily.analysis.column;

import java.util.*;

import com.clougence.clouddm.dsfamily.analysis.secrules.column.QueryItem;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.*;
import com.clougence.clouddm.sdk.analysis.column.SelectColumnAnalysisSpi;
import com.clougence.clouddm.sdk.analysis.column.SelectItem;
import com.clougence.clouddm.sdk.service.execute.MetaCol;
import com.clougence.clouddm.sdk.service.execute.MetaColConvert;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

public abstract class AbstractSelectColumnAnalysisSpi implements SelectColumnAnalysisSpi {

    protected final MetaService metaService;

    public AbstractSelectColumnAnalysisSpi(MetaService metaService){
        this.metaService = metaService;
    }

    protected List<SelectItem> analyzeRealColumn(String uid, long dsID, Map<UmiTypes, Object> levelsParam, List<RuleDomain> domains) {
        List<List<SelectItem>> result = new ArrayList<>();
        for (RuleDomain domain : domains) {
            RdbSelectDomain selectDomain = (RdbSelectDomain) domain;
            List<SelectItem> selectItems = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(selectDomain.getChildren())) {
                for (RuleDomain child : selectDomain.getChildren()) {
                    selectItems.addAll(parseTableDomain(uid, dsID, levelsParam, (RdbTableDomain) child));
                }

                // <columnName,<tableName,SelectColumn>>
                Map<String, Map<String, SelectItem>> columnMap = new LinkedHashMap<>();

                for (SelectItem selectItem : selectItems) {
                    columnMap.computeIfAbsent(selectItem.getItemAlias(), k -> new LinkedHashMap<>()).put(selectItem.getTableAlias(), selectItem);
                }

                if (CollectionUtils.isNotEmpty(selectDomain.getColumns())) {
                    if (selectDomain.getMode() == RdbQueryMode.WITH) {
                        if (selectDomain.getColumns().size() != selectItems.size()) {
                            throw new RuntimeException();
                        } else {
                            for (int i = 0; i < selectItems.size(); i++) {
                                SelectItem selectItem = selectItems.get(i);
                                QueryItem queryItem = selectDomain.getColumns().get(i);
                                selectItem.setItemAlias(queryItem.getColumn());
                                selectItem.setTableAlias(queryItem.getTable());
                            }
                        }
                        result.add(selectItems);
                    } else {
                        result.add(parseColumns(uid, dsID, levelsParam, selectDomain, selectItems, columnMap));
                    }

                } else {
                    result.add(selectItems);
                }
            } else {
                for (QueryItem column : selectDomain.getColumns()) {
                    SelectItem selectItem = new SelectItem();
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

        List<SelectItem> selectItems = result.get(0);
        for (int i = 1; i < result.size(); i++) {
            List<SelectItem> selectColumns1 = result.get(i);
            if (selectColumns1.size() != selectItems.size()) {
                throw new RuntimeException();
            }
            for (int j = 0; j < selectItems.size(); j++) {
                selectItems.get(j).getColumns().addAll(selectColumns1.get(j).getColumns());
            }
        }
        return selectItems;
    }

    protected boolean needAlias(QueryItem queryItem) {
        if (queryItem.getItemAlias() == null) {
            for (RuleDomain column : queryItem.getColumns()) {
                if (!(column instanceof RdbColumnDomain)) {
                    return true;
                }
            }
            if (CollectionUtils.isEmpty(queryItem.getColumns())) {
                return true;
            }
        }
        return false;
    }

    private List<SelectItem> parseColumns(String uid, long dsID, Map<UmiTypes, Object> levelsParam, RdbSelectDomain selectDomain, List<SelectItem> selectItems,
                                          Map<String, Map<String, SelectItem>> columnMap) {
        List<SelectItem> result = new ArrayList<>();
        for (QueryItem queryItem : selectDomain.getColumns()) {
            if (needAlias(queryItem)) {
                throw new RuntimeException("select element: " + queryItem.getColumn() + " need set alias");
            }
            if (queryItem.isSelectAll()) {
                if (StringUtils.isEmpty(queryItem.getTable())) {
                    result.addAll(selectItems);
                } else {
                    for (SelectItem column : selectItems) {
                        if (column.getTableAlias().equals(queryItem.getTable())) {
                            result.add(column);
                        }
                    }
                }
                continue;
            }
            SelectItem column1 = new SelectItem();

            if (queryItem.getItemAlias() != null) {
                column1.setItemAlias(queryItem.getItemAlias());
            } else {
                column1.setItemAlias(queryItem.getColumn());
            }
            for (RuleDomain selectColumn : queryItem.getColumns()) {
                if (selectColumn instanceof RdbColumnDomain) {
                    RdbColumnDomain rdbColumnDomain = (RdbColumnDomain) selectColumn;

                    SelectItem column = findColumn(columnMap, rdbColumnDomain);

                    if (StringUtils.isNotEmpty(queryItem.getItemAlias())) {
                        column.setItemAlias(queryItem.getItemAlias());
                    }

                    column1.getColumns().addAll(column.getColumns());

                } else if (selectColumn instanceof RdbCallDomain) {
                    SelectItem column = parseCallDomain(uid, dsID, levelsParam, (RdbCallDomain) selectColumn, queryItem, selectDomain, columnMap);
                    column1.getColumns().addAll(column.getColumns());
                } else if (selectColumn instanceof RdbSelectDomain) {
                    List<SelectItem> columnList = analyzeRealColumn(uid, dsID, levelsParam, Collections.singletonList(selectColumn));
                    if (columnList.size() > 1) {
                        throw new RuntimeException("The query statement in the selection column should only return one column");
                    }
                    SelectItem column = columnList.get(0);
                    column1.getColumns().addAll(column.getColumns());
                }
            }
            result.add(column1);
        }
        return result;
    }

    private static SelectItem findColumn(Map<String, Map<String, SelectItem>> columnMap, RdbColumnDomain rdbColumnDomain) {
        Map<String, SelectItem> tableMap = columnMap.get(rdbColumnDomain.getColumn());
        if (tableMap == null) {
            throw new RuntimeException("Can't find such table: " + rdbColumnDomain.getTable());
        }
        SelectItem column;
        if (StringUtils.isEmpty(rdbColumnDomain.getTable())) {
            if (tableMap.size() != 1) {
                throw new RuntimeException("Column '" + rdbColumnDomain.getColumn() + "' in field list is ambiguous");
            }
            column = tableMap.values().iterator().next();
        } else {
            column = tableMap.get(rdbColumnDomain.getTable());
        }
        if (column == null) {
            throw new RuntimeException("Can't find such column '" + rdbColumnDomain.getColumn() + "' at: " + rdbColumnDomain.getTable());
        }
        return column;
    }

    private List<SelectItem> parseTableDomain(String uid, long dsID, Map<UmiTypes, Object> levelsParam, RdbTableDomain tableDomain) {
        if (tableDomain.isVirtual()) {
            List<SelectItem> selectItems = analyzeRealColumn(uid, dsID, levelsParam, tableDomain.getChildren());
            for (SelectItem selectItem : selectItems) {
                selectItem.setTableAlias(tableDomain.getTable());
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
            List<MetaCol> metaCols = this.metaService.fetchTableColumns(uid, dsID, levels, tableDomain.getTable(), 0);
            List<SelectItem> selectItems = MetaColConvert.toSelectItems(metaCols, 0);
            for (SelectItem selectItem : selectItems) {
                if (tableDomain.getAlias() != null) {
                    selectItem.setTableAlias(tableDomain.getAlias());
                } else {
                    selectItem.setTableAlias(tableDomain.getTable());
                }
                selectItem.setItemAlias(selectItem.getColumns().get(0).getColumn());
            }
            return selectItems;
        }
    }

    private SelectItem parseCallDomain(String uid, long dsID, Map<UmiTypes, Object> levelsParam, RdbCallDomain callDomain, QueryItem queryItem, RdbSelectDomain selectDomain,
                                       Map<String, Map<String, SelectItem>> columnMap) {
        SelectItem selectItem = new SelectItem();
        selectItem.setItemAlias(queryItem.getItemAlias());

        if (CollectionUtils.isEmpty(callDomain.getChildren())) {
            return selectItem;
        }

        for (RuleDomain child : callDomain.getChildren()) {
            if (child instanceof RdbSelectDomain) {
                List<SelectItem> selectItems = analyzeRealColumn(uid, dsID, levelsParam, Collections.singletonList(child));
                if (selectItems.size() > 1) {
                    throw new RuntimeException("The query statement in the function should only return one column");
                } else {
                    selectItem.addAllRealColumns(selectItems.get(0).getColumns());
                }
            } else if (child instanceof RdbCallDomain) {
                SelectItem callColumn = parseCallDomain(uid, dsID, levelsParam, (RdbCallDomain) child, queryItem, selectDomain, columnMap);
                selectItem.addAllRealColumns(callColumn.getColumns());
            } else if (child instanceof RdbColumnDomain) {
                RdbColumnDomain columnDomain = (RdbColumnDomain) child;
                SelectItem column = findColumn(columnMap, columnDomain);
                selectItem.addAllRealColumns(column.getColumns());
            }
        }
        return selectItem;
    }
}
