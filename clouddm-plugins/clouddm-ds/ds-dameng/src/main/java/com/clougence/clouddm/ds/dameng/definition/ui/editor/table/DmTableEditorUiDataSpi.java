package com.clougence.clouddm.ds.dameng.definition.ui.editor.table;

import java.util.List;
import java.util.Map;

import com.clougence.adapter.dameng.DmAttributeNames;
import com.clougence.clouddm.dsfamily.definition.ui.editor.table.DsFamilyTableEditorUiDataSpi;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiData;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.domain.ETable;
import com.clougence.utils.StringUtils;

/**
 * @Author: Ekko
 * @Date: 2023-09-25 15:08
 */
public class DmTableEditorUiDataSpi extends DsFamilyTableEditorUiDataSpi implements DmTableEditorFields {

    @Override
    public void fillETable(EditorViewMode viewMode, TableEditorUiData uiData, ETable eTable, String mainVersion) {
        // column
        List<EColumn> columnList = eTable.getColumnList();
        for (EColumn eColumn : columnList) {
            boolean identity = Boolean.parseBoolean(eColumn.getAttribute().get(DmAttributeNames.IDENTITY.getCodeKey()));
            if (identity) {
                eColumn.setNullable(false);
            }

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
                    } else if (defaultValue.equalsIgnoreCase("EMPTY")) {
                        eColumn.setDefaultValue("");
                    } else if (defaultValue.equalsIgnoreCase(SPI_COLUMNS_CUSTOM)) {
                        Object o = columnMap.get(SPI_COLUMNS_CUSTOM);
                        eColumn.setDefaultValue(safeToString(o));
                    }
                }
                eColumn.getAttribute().remove(SPI_COLUMNS_CUSTOM);
            }
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
                    if (defaultValue.equals("")) {
                        columnMap.put(MODE_COLUMN_DEFAULT, "EMPTY");
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
}
