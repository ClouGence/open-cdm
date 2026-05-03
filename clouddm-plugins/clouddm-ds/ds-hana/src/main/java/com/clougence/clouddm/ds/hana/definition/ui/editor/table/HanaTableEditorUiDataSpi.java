package com.clougence.clouddm.ds.hana.definition.ui.editor.table;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.clougence.adapter.hana.HanaAttributeNames;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiData;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiDataSpi;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.domain.EIndex;
import com.clougence.schema.editor.domain.EIndexType;
import com.clougence.schema.editor.domain.ETable;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

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
            if (StringUtils.isNotBlank(eColumn.getDbType())) {
                String dbType = eColumn.getDbType();
                if (dbType.equalsIgnoreCase("VARCHAR")) {
                    if (eColumn.getLength() == null || eColumn.getLength() == 0) {
                        eColumn.setLength(100L);
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
            }
        }

        // index
        for (EIndex index : eTable.getIndices()) {
            Map<String, Object> idxMap = uiData.findIndex(index.getName());
            if (idxMap != null) {
                HanaAttributeNames.INDEX_TYPE.setValue(index.getAttribute(), safeToString(idxMap.get(MODE_INDEX_TYPE)));

                if (idxMap.containsKey(MODE_INDEX_COLUMNS)) {
                    Object obj = idxMap.get(MODE_INDEX_COLUMNS);
                    List<String> newColumnList = new ArrayList<>();
                    Map<String, String> subPartMap = new LinkedHashMap<>();
                    Map<String, String> subOrderMap = new LinkedHashMap<>();
                    if (obj instanceof List) {
                        List<Map<String, Object>> columnListMap = (List<Map<String, Object>>) obj;
                        for (Map<String, Object> map : columnListMap) {
                            // name
                            String name = safeToString(map.get(MODE_INDEX_NAME));
                            newColumnList.add(name);

                            // length
                            String length = safeToString(map.get(SPI_INDEX_COLUMNS_LENGTH));
                            subPartMap.put(name, length);

                            // order
                            String order = safeToString(map.get(SPI_INDEX_COLUMNS_ORDER));
                            subOrderMap.put(name, order);
                        }
                    }
                    index.setColumnList(newColumnList);
                    index.getAttribute().put(HanaAttributeNames.ORDER_TYPE.getCodeKey(), JsonUtils.toJson(subOrderMap));
                }

                if (idxMap.containsKey(MODE_INDEX_TYPE) && idxMap.containsKey(HanaAttributeNames.INDEX_WAY.getCodeKey())) {
                    String idxWay = safeToString(idxMap.get(HanaAttributeNames.INDEX_WAY.getCodeKey()));
                    if (StringUtils.equalsIgnoreCase(idxWay, "UNIQUE")) {
                        index.setType(EIndexType.Unique);
                    } else {
                        index.setType(EIndexType.Normal);
                    }
                }

            }

        }

        // pk
        if (uiData.getKeys() != null) {
            Object obj = uiData.getKeys().get(MODE_KEY_COLUMNS);
            if (obj instanceof List) {
                List<Map<String, Object>> columnListMap = (List<Map<String, Object>>) obj;
                List<String> newKeyColumns = new ArrayList<>();
                Map<String, String> subPartMap = new LinkedHashMap<>();
                Map<String, String> subOrderMap = new LinkedHashMap<>();

                for (Map<String, Object> map : columnListMap) {
                    // name
                    String name = safeToString(map.get(MODE_KEY_COLUMNS_NAME));
                    newKeyColumns.add(name);

                    // length
                    String length = safeToString(map.get(SPI_PK_COLUMNS_LENGTH));
                    subPartMap.put(name, length);

                    // order
                    String order = safeToString(map.get(SPI_PK_COLUMNS_ORDER));
                    subOrderMap.put(name, order);
                }
                eTable.getPrimaryKey().setColumnList(newKeyColumns);
                eTable.getPrimaryKey().getAttribute().put(HanaAttributeNames.ORDER_TYPE.getCodeKey(), JsonUtils.toJson(subOrderMap));
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
                idxMap.put(MODE_INDEX_TYPE, HanaAttributeNames.INDEX_TYPE.getValue(index.getAttribute()));

                Object obj = idxMap.get(MODE_INDEX_COLUMNS);
                if (obj instanceof List) {
                    List<Map<String, Object>> mapList = (List<Map<String, Object>>) obj;
                    for (Map<String, Object> map : mapList) {
                        String orderMapValue = HanaAttributeNames.ORDER_TYPE.getValue(index.getAttribute());
                        Map<String, String> subOrderMap = JsonUtils.toObj(orderMapValue, Map.class);
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
                    Map<String, String> subOrderMap = JsonUtils.toObj(orderMapValue, Map.class);
                    String subOrderValue = subOrderMap.get(safeToString(map.get(MODE_KEY_COLUMNS_NAME)));
                    map.put(SPI_PK_COLUMNS_ORDER, subOrderValue);
                }
            }
        }
    }
}
