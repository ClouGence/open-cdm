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
package com.clougence.clouddm.dsfamily.postgres.definition.ui.editor.table;

import java.util.*;

import com.clougence.adapter.postgre.PostgreAttributeNames;
import com.clougence.clouddm.dsfamily.postgres.execute.PgMetaProviderUtils;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiData;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiDataSpi;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.domain.EIndex;
import com.clougence.schema.editor.domain.ETable;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

public class PgTableEditorUiDataSpi implements TableEditorUiDataSpi, PgTableEditorFields {

    @Override
    public void fillETable(EditorViewMode viewMode, TableEditorUiData uiData, ETable eTable, String mainVersion) {
        // column  inditidy must not null
        List<EColumn> columnList = eTable.getColumnList();
        for (EColumn eColumn : columnList) {
            if (StringUtils.isNotEmpty(eColumn.getDefaultValue())) {
                if (PgMetaProviderUtils.isFunc(eColumn.getDefaultValue())) {
                    eColumn.setDefaultValueIsFunc(true);
                }
            }
            Map<String, String> attrMap = eColumn.getAttribute();
            String vt = attrMap.get(PostgreAttributeNames.VIRTUAL_TYPE.getCodeKey());
            if (StringUtils.isNotBlank(vt) && !"STORED".equalsIgnoreCase(vt)) {
                eColumn.setNullable(false);
            }
        }
        //if precision and scale is "", the backend will resolve to 0;
        List<Map<String, Object>> uiDataColumns = uiData.getColumns();
        for (Map<String, Object> column : uiDataColumns) {
            Object p = column.get(MODE_COLUMN_NUM_P);
            Object s = column.get(MODE_COLUMN_NUM_S);
            Object l = column.get(MODE_COLUMN_LENGTH);
            Object dp = column.get(MODE_COLUMN_DATE_P);
            if ("".equals(p) || "".equals(s) || "".equals(l) || "".equals(dp)) {
                Object name = column.get(MODE_COLUMN_SOURCE);
                if (name == null || "".equals(name)) {
                    name = column.get(MODE_COLUMN_NAME);
                }
                Object finalName = name;
                EColumn eColumn = columnList.stream().filter(item -> item.getName().equals(finalName)).findFirst().get();
                eColumn.setNumericScale(null);
                eColumn.setNumericPrecision(null);
                eColumn.setLength(null);
                eColumn.setDatetimePrecision(null);
            }
        }

        //pk column must not null 
        Map<String, Object> keys = uiData.getKeys();
        if (keys != null) {
            Object columns = keys.get(MODE_KEY_COLUMNS);
            if (columns instanceof List) {
                List<Map<String, Object>> columnMap = (List<Map<String, Object>>) columns;
                for (Map<String, Object> item : columnMap) {
                    String newName = safeToString(item.get(MODE_KEY_NAME));
                    EColumn eColumn = findColumn(columnList, newName);
                    eColumn.setNullable(false);
                    // column rename & update attribute
                    item.put("name", eColumn.getName());
                }
            }
        } else {
            if (eTable.getPrimaryKey() != null) {
                eTable.getPrimaryKey().setPrimaryKeyName(null);
            }
        }

        // index
        for (EIndex index : eTable.getIndices()) {
            Map<String, Object> idxMap = uiData.findIndex(index.getName());
            if (idxMap != null) {
                PostgreAttributeNames.INDEX_TYPE.setValue(index.getAttribute(), safeToString(idxMap.get(FIELD_INDEXES_INDEX_TYPE)));
                PostgreAttributeNames.INDEX_WAY.setValue(index.getAttribute(), safeToString(idxMap.get(FIELD_INDEXES_INDEX_WAY)));
                if (idxMap.containsKey(MODE_INDEX_COLUMNS)) {
                    Object obj = idxMap.get(MODE_INDEX_COLUMNS);
                    List<String> newColumnList = new ArrayList<>();
                    //LinkedHashMap keep field order
                    Map<String, String> subPartMap = new LinkedHashMap<>();
                    Map<String, String> subOrderMap = new LinkedHashMap<>();
                    Map<String, String> nullsSortMap = new LinkedHashMap<>();
                    Map<String, String> operatorTypeMap = new LinkedHashMap<>();
                    Map<String, String> sortRulesMap = new LinkedHashMap<>();
                    Map<String, String> attCollationMap = new LinkedHashMap<>();
                    if (obj instanceof List) {
                        List<Map<String, Object>> columnListMap = (List<Map<String, Object>>) obj;
                        for (Map<String, Object> map : columnListMap) {
                            // name
                            String name = safeToString(map.get(MODE_INDEX_NAME));
                            // column rename & update attribute
                            newColumnList.add(safeToString(name));
                            // length
                            String length = safeToString(map.get(SPI_INDEX_COLUMNS_LENGTH));
                            subPartMap.put(name, length);
                            // order
                            String order = safeToString(map.get(SPI_INDEX_COLUMNS_ORDER));
                            subOrderMap.put(name, order);
                            // nulls sort
                            String nullsSort = safeToString(map.get(SPI_INDEX_NULLS_SORT));
                            nullsSortMap.put(name, nullsSort);
                            // operator type
                            String operatorType = safeToString(map.get(SPI_INDEX_OPERATOR_TYPE));
                            operatorTypeMap.put(name, operatorType);
                            // sort rules
                            String sortRules = safeToString(map.get(SPI_INDEX_SORT_RULES));
                            sortRulesMap.put(name, sortRules);
                        }
                    }
                    index.setColumnList(newColumnList);
                    index.getAttribute().put(PostgreAttributeNames.INDEX_PREFIX_LENGTH.getCodeKey(), JsonUtils.toJson(subPartMap));
                    //index.getAttribute().put(PostgreAttributeNames.INDEX_SORT_ORDER.getCodeKey(), JsonUtils.toJson(subOrderMap));
                    //index.getAttribute().put(PostgreAttributeNames.INDEX_NULLS_SORT.getCodeKey(), JsonUtils.toJson(nullsSortMap));
                    //index.getAttribute().put(PostgreAttributeNames.INDEX_OPERATOR_TYPE.getCodeKey(), JsonUtils.toJson(operatorTypeMap));
                    //index.getAttribute().put(PostgreAttributeNames.INDEX_SORT_RULES.getCodeKey(), JsonUtils.toJson(sortRulesMap));
                }
            }
        }

        // pk
        if (uiData.getKeys() != null) {
            Object obj = uiData.getKeys().get(MODE_KEY_COLUMNS);
            if (obj instanceof List) {
                List<Map<String, Object>> columnListMap = (List<Map<String, Object>>) obj;
                List<String> newKeyColumns = new ArrayList<>();
                for (Map<String, Object> map : columnListMap) {
                    //name
                    String name = safeToString(map.get(MODE_KEY_COLUMNS_NAME));
                    newKeyColumns.add(name);
                }
                eTable.getPrimaryKey().setColumnList(newKeyColumns);
            }
        }
    }

    @Override
    public void fillUiData(EditorViewMode viewMode, ETable eTable, TableEditorUiData uiData, String mainVersion) {
        // index
        for (EIndex index : eTable.getIndices()) {
            Map<String, Object> idxMap = uiData.findIndex(index.getName());
            if (idxMap != null) {
                idxMap.put(FIELD_INDEXES_INDEX_TYPE, PostgreAttributeNames.INDEX_TYPE.getValue(index.getAttribute()));
                idxMap.put(FIELD_INDEXES_INDEX_WAY, PostgreAttributeNames.INDEX_WAY.getValue(index.getAttribute()));

                Object obj = idxMap.get(MODE_INDEX_COLUMNS);
                if (obj instanceof List) {
                    List<Map<String, Object>> mapList = (List<Map<String, Object>>) obj;
                    for (Map<String, Object> map : mapList) {
                        String partMapValue = PostgreAttributeNames.INDEX_PREFIX_LENGTH.getValue(index.getAttribute());
                        Map<String, String> subPartMap = JsonUtils.toMap(partMapValue);
                        String subPartValue = subPartMap.get(safeToString(map.get(MODE_INDEX_NAME)));
                        map.put(SPI_INDEX_COLUMNS_LENGTH, subPartValue);

                        // String orderMapValue = PostgreAttributeNames.INDEX_SORT_ORDER.getValue(index.getAttribute());
                        // Map<String, String> subOrderMap = JsonUtils.toMap(orderMapValue);
                        // String subOrderValue = subOrderMap.get(safeToString(map.get(MODE_INDEX_NAME)));
                        // map.put(SPI_INDEX_COLUMNS_ORDER, subOrderValue);

                        // String nullsSortMapValue = PostgreAttributeNames.INDEX_NULLS_SORT.getValue(index.getAttribute());
                        // Map<String, String> nullsSortMap = JsonUtils.toMap(nullsSortMapValue);
                        // String nullsSortValue = nullsSortMap.get(safeToString(map.get(MODE_INDEX_NAME)));
                        // map.put(SPI_INDEX_NULLS_SORT, nullsSortValue);

                        // String operatorTypeMapValue = PostgreAttributeNames.INDEX_OPERATOR_TYPE.getValue(index.getAttribute());
                        // Map<String, String> operatorTypeMap = JsonUtils.toMap(operatorTypeMapValue);
                        // String operatorTypeValue = operatorTypeMap.get(safeToString(map.get(MODE_INDEX_NAME)));
                        // map.put(SPI_INDEX_OPERATOR_TYPE, operatorTypeValue);

                        // String sortRulesMapValue = PostgreAttributeNames.INDEX_SORT_RULES.getValue(index.getAttribute());
                        // Map<String, String> sortRulesMap = JsonUtils.toMap(sortRulesMapValue);
                        // String sortRulesValue = sortRulesMap.get(safeToString(map.get(MODE_INDEX_NAME)));
                        // map.put(SPI_INDEX_SORT_RULES, sortRulesValue);
                    }
                }
            }

        }

        //tag array
        Map<String, Object> tableInfo = uiData.getTableInfo();
        String father = (String) tableInfo.get(PostgreAttributeNames.INHERITED_FROM.getCodeKey());
        if (StringUtils.isNotBlank(father)) {
            String[] split = father.split(",");
            tableInfo.put(PostgreAttributeNames.INHERITED_FROM.getCodeKey(), split);
        }

        // pk
        if (eTable.getPrimaryKey() != null) {
            List<Map<String, Object>> columnListMap = new ArrayList<>();

            for (String colName : eTable.getPrimaryKey().getColumnList()) {
                Map<String, Object> colMap = new HashMap<>();
                colMap.put(MODE_KEY_COLUMNS_NAME, colName);
                columnListMap.add(colMap);
            }

            uiData.getKeys().put(MODE_KEY_COLUMNS, columnListMap);

            Map<String, String> attribute = eTable.getPrimaryKey().getAttribute();
            if (attribute.containsKey(FIELD_PRIMARY_CONSTRAINT_DELAY)) {
                String data = attribute.get(FIELD_PRIMARY_CONSTRAINT_DELAY);
                uiData.getKeys().put(FIELD_PRIMARY_CONSTRAINT_DELAY, data);
            }
        }
    }

    private static String safeToString(Object obj) {
        return (obj == null) ? null : obj.toString();
    }

    private static EColumn findColumn(List<EColumn> columnList, String newName) {
        EColumn eColumn = columnList.stream().filter(column -> {
            String source = column.getSource();
            if (StringUtils.isNotBlank(source)) {
                return source.equals(newName);
            } else {
                return column.getName().equals(newName);
            }
        }).findFirst().orElse(null);
        if (Objects.isNull(eColumn)) {
            eColumn = new EColumn();
            eColumn.setName(newName);
        }
        return eColumn;
    }
}
