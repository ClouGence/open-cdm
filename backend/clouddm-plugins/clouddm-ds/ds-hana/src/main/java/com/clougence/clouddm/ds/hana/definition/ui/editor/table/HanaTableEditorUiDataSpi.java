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
package com.clougence.clouddm.ds.hana.definition.ui.editor.table;

import com.clougence.adapter.hana.HanaAttributeNames;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiData;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiDataSpi;
import com.clougence.schema.editor.domain.*;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

import java.util.*;

/**
 * @author chunlin
 * @date 2024/4/2
 */
public class HanaTableEditorUiDataSpi implements TableEditorUiDataSpi, HanaTableEditorFields {

    public static String safeToString(Object obj) {
        return (obj == null) ? null : obj.toString();
    }

    @Override
    public void fillETable(EditorViewMode viewMode, TableEditorUiData uiData, ETable eTable, String mainVersion) {
        // column
        List<EColumn> columnList = eTable.getColumnList();
        for (EColumn eColumn : columnList) {
            Map<String, Object> columnMap = uiData.findColumn(eColumn.getName());
            if (columnMap != null) {
                Object obj = columnMap.get(MODE_COLUMN_DEFAULT);
                if (obj != null) {
                    String defaultValue = obj.toString();
                    if (defaultValue.equalsIgnoreCase("NULL")) {
                        eColumn.setDefaultValue(null);
                    } else if (defaultValue.equalsIgnoreCase("")) {
                        eColumn.setDefaultValue("''");
                    } else if (defaultValue.equalsIgnoreCase(SPI_COLUMNS_CUSTOM)) {
                        Object o = columnMap.get(SPI_COLUMNS_CUSTOM);
                        eColumn.setDefaultValue(safeToString(o));
                    }
                }
                eColumn.setDefaultValueIsFunc(true);
                eColumn.getAttribute().remove(SPI_COLUMNS_CUSTOM);
            }
        }

        // index
        for (EIndex index : eTable.getIndices()) {
            Map<String, Object> idxMap = uiData.findIndex(index.getName());
            if (idxMap != null) {
                String indexType = safeToString(idxMap.get(MODE_INDEX_TYPE));
                if (indexType != null || index.getAttribute().containsKey(HanaAttributeNames.INDEX_TYPE.getCodeKey())) {
                    HanaAttributeNames.INDEX_TYPE.setValue(index.getAttribute(), indexType);
                }

                if (idxMap.containsKey(MODE_INDEX_COLUMNS)) {
                    Object obj = idxMap.get(MODE_INDEX_COLUMNS);
                    List<String> newColumnList = new ArrayList<>();
                    Map<String, String> subOrderMap = new LinkedHashMap<>();
                    if (obj instanceof List) {
                        List<Map<String, Object>> columnListMap = (List<Map<String, Object>>) obj;
                        for (Map<String, Object> map : columnListMap) {
                            // name
                            String name = safeToString(map.get(MODE_INDEX_NAME));
                            newColumnList.add(name);

                            // order
                            String order = safeToString(map.get(SPI_INDEX_COLUMNS_ORDER));
                            subOrderMap.put(name, order);
                        }
                    }
                    index.setColumnList(newColumnList);
                    writeColumnOrder(index.getAttribute(), subOrderMap);
                }

                if (idxMap.containsKey(HanaAttributeNames.INDEX_WAY.getCodeKey())) {
                    String idxWay = safeToString(idxMap.get(HanaAttributeNames.INDEX_WAY.getCodeKey()));
                    if (StringUtils.equalsIgnoreCase(idxWay, "UNIQUE")) {
                        index.setType(EIndexType.Unique);
                    } else {
                        index.setType(EIndexType.Normal);
                    }
                }

            }

        }

        // The shared form mapper also copies the FK name into its attribute map.
        for (EForeignKey foreignKey : eTable.getForeignKeys()) {
            foreignKey.getAttribute().remove(MODE_FOREIGN_KEY_NAME);
        }

        // pk
        if (uiData.getKeys() != null) {
            Object obj = uiData.getKeys().get(MODE_KEY_COLUMNS);
            if (obj instanceof List) {
                List<Map<String, Object>> columnListMap = (List<Map<String, Object>>) obj;
                List<String> newKeyColumns = new ArrayList<>();
                Map<String, String> subOrderMap = new LinkedHashMap<>();

                for (Map<String, Object> map : columnListMap) {
                    // name
                    String name = safeToString(map.get(MODE_KEY_COLUMNS_NAME));
                    newKeyColumns.add(name);

                    // order
                    String order = safeToString(map.get(SPI_PK_COLUMNS_ORDER));
                    subOrderMap.put(name, order);
                }
                eTable.getPrimaryKey().setColumnList(newKeyColumns);
                writeColumnOrder(eTable.getPrimaryKey().getAttribute(), subOrderMap);
            }
        }
    }

    private void writeColumnOrder(Map<String, String> attributes, Map<String, String> orders) {
        String original = HanaAttributeNames.ORDER_TYPE.getValue(attributes);
        Map<?, ?> previous = Map.of();
        if (StringUtils.isNotBlank(original)) {
            previous = JsonUtils.toObj(original, Map.class);
        }
        // JSON key order is not an index change. Preserve the original serialized attribute on a no-op save.
        if (!Objects.equals(previous, orders)) {
            if (previous.isEmpty() && orders.values().stream().allMatch(Objects::isNull)) {
                return;
            }
            HanaAttributeNames.ORDER_TYPE.setValue(attributes, JsonUtils.toJson(orders));
        }
    }

    @Override
    public void fillUiData(EditorViewMode viewMode, ETable eTable, TableEditorUiData uiData, String mainVersion) {
        for (EColumn eColumn : eTable.getColumnList()) {
            Map<String, Object> columnMap = uiData.findColumn(eColumn.getName());
            if (columnMap != null) {
                Object obj = columnMap.get(MODE_COLUMN_DEFAULT);
                if (obj != null) {
                    String defaultValue = obj.toString();
                    if (defaultValue.equals("''")) {
                        columnMap.put(MODE_COLUMN_DEFAULT, "");
                    } else {
                        columnMap.put(MODE_COLUMN_DEFAULT, "custom");
                        columnMap.put(SPI_COLUMNS_CUSTOM, defaultValue);
                    }
                } else {
                    columnMap.put(MODE_COLUMN_DEFAULT, "NULL");
                }
            }
        }

        // index
        for (EIndex index : eTable.getIndices()) {
            Map<String, Object> idxMap = uiData.findIndex(index.getName());
            if (idxMap != null) {
                idxMap.put(HanaAttributeNames.INDEX_WAY.getCodeKey(), index.getType().getTypeName());
                idxMap.put(MODE_INDEX_TYPE, HanaAttributeNames.INDEX_TYPE.getValue(index.getAttribute()));

                Object obj = idxMap.get(MODE_INDEX_COLUMNS);
                if (obj instanceof List) {
                    List<Map<String, Object>> mapList = (List<Map<String, Object>>) obj;
                    for (Map<String, Object> map : mapList) {
                        String orderMapValue = HanaAttributeNames.ORDER_TYPE.getValue(index.getAttribute());
                        Map<String, String> subOrderMap = new LinkedHashMap<>();
                        if (StringUtils.isNotBlank(orderMapValue)) {
                            subOrderMap = JsonUtils.toObj(orderMapValue, Map.class);
                        }
                        String subOrderValue = subOrderMap.get(safeToString(map.get(MODE_INDEX_NAME)));
                        map.put(SPI_INDEX_COLUMNS_ORDER, subOrderValue);
                    }
                }
            }

        }

        // pk
        if (uiData.getKeys() != null) {
            Object obj = uiData.getKeys().get(MODE_KEY_COLUMNS);
            if (obj instanceof List) {
                List<Map<String, Object>> columnListMap = (List<Map<String, Object>>) obj;
                for (Map<String, Object> map : columnListMap) {
                    String orderMapValue = HanaAttributeNames.ORDER_TYPE.getValue(eTable.getPrimaryKey().getAttribute());
                    Map<String, String> subOrderMap = new LinkedHashMap<>();
                    if (StringUtils.isNotBlank(orderMapValue)) {
                        subOrderMap = JsonUtils.toObj(orderMapValue, Map.class);
                    }
                    String subOrderValue = subOrderMap.get(safeToString(map.get(MODE_KEY_COLUMNS_NAME)));
                    map.put(SPI_PK_COLUMNS_ORDER, subOrderValue);
                }
            }
        }
    }
}
