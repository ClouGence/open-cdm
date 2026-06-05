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
package com.clougence.clouddm.ds.starrocks.definition.ui.editor.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.clougence.adapter.starrocks.StarRocksAttributeNames;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiData;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiDataSpi;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.domain.EPrimaryKey;
import com.clougence.schema.editor.domain.ETable;
import com.clougence.utils.StringUtils;

/**
 * @Author: Ekko
 * @Date: 2023-09-04 19:41
 */
public class SrTableEditorUiDataSpi implements TableEditorUiDataSpi, SrTableEditorFields {

    // sr black list
    static {
        TABLE_BLACK_LIST_ATTRS.add(StarRocksAttributeNames.DISTRIBUTED_BY_COLUMNS.getCodeKey());
        TABLE_BLACK_LIST_ATTRS.add(StarRocksAttributeNames.ORDER_BY.getCodeKey());
    }

    @Override
    public void fillETable(EditorViewMode viewMode, TableEditorUiData uiData, ETable eTable, String mainVersion) {
        // Table
        Map<String, String> attribute = eTable.getAttribute();
        Object dbObj = uiData.getTableInfo().get(StarRocksAttributeNames.DISTRIBUTED_BY_COLUMNS.getCodeKey());
        if (dbObj instanceof List) {
            List<Map<String, String>> dbColumns = (List<Map<String, String>>) dbObj;
            List<String> columns = new ArrayList<>();
            for (Map<String, String> dbColumn : dbColumns) {
                columns.add(dbColumn.get(SPI_TABLE_DISTRIBUTED_COLUMNS_NAME));
            }
            attribute.put(StarRocksAttributeNames.DISTRIBUTED_BY_COLUMNS.getCodeKey(), JSON.toJSONString(columns));
        }

        if (eTable.getPrimaryKey() != null) {
            Map<String, String> pkAttribute = eTable.getPrimaryKey().getAttribute();
            Object obObj = uiData.getKeys().get(StarRocksAttributeNames.ORDER_BY.getCodeKey());
            if (obObj instanceof List) {
                List<Map<String, String>> obColumns = (List<Map<String, String>>) obObj;
                List<String> columns = new ArrayList<>();
                for (Map<String, String> obColumn : obColumns) {
                    columns.add(obColumn.get(SPI_TABLE_ORDER_BY_COLUMNS_NAME));
                }
                pkAttribute.put(StarRocksAttributeNames.ORDER_BY.getCodeKey(), JSON.toJSONString(columns));
            }
            EPrimaryKey primaryKey = eTable.getPrimaryKey();
            primaryKey.setAttribute(pkAttribute);
            eTable.setPrimaryKey(primaryKey);
        }

        // column
        List<EColumn> columnList = eTable.getColumnList();
        for (EColumn eColumn : columnList) {

            if (StringUtils.isNotBlank(eColumn.getDbType())) {
                String dbType = eColumn.getDbType();
                if (dbType.equalsIgnoreCase("VARCHAR")) {
                    if (eColumn.getLength() == null || eColumn.getLength() == 0) {
                        eColumn.setLength(255L); // varchar default length
                    }
                }
            }

            Map<String, Object> columnMap = uiData.findColumn(eColumn.getName());
            if (columnMap != null) {
                Object obj = columnMap.get(MODE_COLUMN_DEFAULT);
                if (obj != null) {
                    String defaultValue = obj.toString();
                    if (defaultValue.equalsIgnoreCase("NULL")) {
                        eColumn.setDefaultValue(null);
                    } else if (defaultValue.equalsIgnoreCase("")) {
                        eColumn.setDefaultValue("");
                    } else if (defaultValue.equalsIgnoreCase(SPI_COLUMNS_CUSTOM)) {
                        Object o = columnMap.get(SPI_COLUMNS_CUSTOM);
                        eColumn.setDefaultValue(safeToString(o));
                    }
                }

                eColumn.getAttribute().remove(SPI_COLUMNS_CUSTOM);
                //                if (viewMode == TableEditorViewMode.Alter) {
                //                    eColumn.getAttribute().remove(StarRocksTableEditorUiPanelFactory.FIELD_COLUMN_AGG_TYPE);
                //                }
            }
        }
    }

    @Override
    public void fillUiData(EditorViewMode viewMode, ETable eTable, TableEditorUiData uiData, String mainVersion) {
        // table
        Map<String, String> attribute = eTable.getAttribute();
        String jsonDbColumn = attribute.get(StarRocksAttributeNames.DISTRIBUTED_BY_COLUMNS.getCodeKey());
        Object objDbColumn = JSON.parse(jsonDbColumn);
        if (objDbColumn instanceof List) {
            List<Map<String, String>> columnList = (List<Map<String, String>>) objDbColumn;
            uiData.getTableInfo().put(StarRocksAttributeNames.DISTRIBUTED_BY_COLUMNS.getCodeKey(), columnList);
        }

        Map<String, String> pkAttribute = eTable.getPrimaryKey().getAttribute();
        String jsonObColumn = pkAttribute.get(StarRocksAttributeNames.ORDER_BY.getCodeKey());
        Object objObColumn = JSON.parse(jsonObColumn);
        if (objObColumn instanceof List) {
            List<Map<String, String>> columnList = (List<Map<String, String>>) objObColumn;
            uiData.getKeys().put(StarRocksAttributeNames.ORDER_BY.getCodeKey(), columnList);
        }

        // column
        for (EColumn eColumn : eTable.getColumnList()) {
            Map<String, Object> columnMap = uiData.findColumn(eColumn.getName());
            if (columnMap != null) {
                Object obj = columnMap.get(MODE_COLUMN_DEFAULT);
                if (obj != null) {
                    String defaultValue = obj.toString();
                    if (defaultValue.equals("")) {
                        columnMap.put(MODE_COLUMN_DEFAULT, "");
                    } else {
                        columnMap.put(MODE_COLUMN_DEFAULT, SPI_COLUMNS_CUSTOM);
                        columnMap.put(SPI_COLUMNS_CUSTOM, defaultValue);
                    }
                } else {
                    columnMap.put(MODE_COLUMN_DEFAULT, "NULL");
                }
            }
        }
    }

    protected static String safeToString(Object obj) {
        return (obj == null) ? null : obj.toString();
    }
}
