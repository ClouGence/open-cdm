package com.clougence.clouddm.ds.doris.definition.ui.editor.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.clougence.adapter.doris.DorisAttributeNames;
import com.clougence.clouddm.dsfamily.definition.ui.editor.table.DsFamilyTableEditorUiDataSpi;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiData;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.domain.ETable;
import com.clougence.utils.StringUtils;

/**
 * @Author: Ekko
 * @Date: 2023-09-04 19:41
 */
public class DrTableEditorUiDataSpi extends DsFamilyTableEditorUiDataSpi implements DrTableEditorFields {

    @Override
    public void fillETable(EditorViewMode viewMode, TableEditorUiData uiData, ETable eTable, String mainVersion) {

        //table
        if (viewMode == EditorViewMode.Alter) {
            eTable.getAttribute().remove(DrTableEditorUiPanelFactory.FIELD_TABLE_COLUMN_KEY_TYPE);
        }

        Map<String, String> attribute = eTable.getAttribute();
        Object dbObj = uiData.getTableInfo().get(DorisAttributeNames.DISTRIBUTED_BY_COLUMNS.getCodeKey());
        if (dbObj instanceof List) {
            List<Map<String, String>> dbColumns = (List<Map<String, String>>) dbObj;
            List<String> columns = new ArrayList<>();
            for (Map<String, String> dbColumn : dbColumns) {
                columns.add(dbColumn.get(FIELD_TABLE_DISTRIBUTED_BY_COLUMNS_NAME));
            }
            attribute.put(DorisAttributeNames.DISTRIBUTED_BY_COLUMNS.getCodeKey(), JSON.toJSONString(columns));
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
                if (viewMode == EditorViewMode.Alter) {
                    eColumn.getAttribute().remove(DrTableEditorUiPanelFactory.FIELD_COLUMN_AGG_TYPE);
                }
            }
        }
    }

    @Override
    public void fillUiData(EditorViewMode viewMode, ETable eTable, TableEditorUiData uiData, String mainVersion) {
        // table
        Map<String, String> attribute = eTable.getAttribute();
        String jsonDbColumn = attribute.get(DorisAttributeNames.DISTRIBUTED_BY_COLUMNS.getCodeKey());
        Object objDbColumn = JSON.parse(jsonDbColumn);
        if (objDbColumn instanceof List) {
            List<Map<String, String>> columnList = (List<Map<String, String>>) objDbColumn;
            uiData.getTableInfo().put(DorisAttributeNames.DISTRIBUTED_BY_COLUMNS.getCodeKey(), columnList);
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
                        columnMap.put(MODE_COLUMN_DEFAULT, "customDefault");
                        columnMap.put(SPI_COLUMNS_CUSTOM, defaultValue);
                    }
                } else {
                    columnMap.put(MODE_COLUMN_DEFAULT, "NULL");
                }
            }
        }
    }
}
